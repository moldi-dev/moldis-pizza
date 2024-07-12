package org.moldidev.moldispizza.service.implementation;

import lombok.RequiredArgsConstructor;
import org.moldidev.moldispizza.dto.ImageDTO;
import org.moldidev.moldispizza.dto.PizzaDTO;
import org.moldidev.moldispizza.entity.Image;
import org.moldidev.moldispizza.entity.Order;
import org.moldidev.moldispizza.entity.Pizza;
import org.moldidev.moldispizza.entity.Review;
import org.moldidev.moldispizza.exception.ResourceAlreadyExistsException;
import org.moldidev.moldispizza.exception.ResourceNotFoundException;
import org.moldidev.moldispizza.mapper.PizzaDTOMapper;
import org.moldidev.moldispizza.repository.ImageRepository;
import org.moldidev.moldispizza.repository.OrderRepository;
import org.moldidev.moldispizza.repository.PizzaRepository;
import org.moldidev.moldispizza.repository.ReviewRepository;
import org.moldidev.moldispizza.request.admin.PizzaCreateAdminRequest;
import org.moldidev.moldispizza.request.admin.PizzaUpdateDetailsAdminRequest;
import org.moldidev.moldispizza.service.ImageService;
import org.moldidev.moldispizza.service.OrderService;
import org.moldidev.moldispizza.service.PizzaService;
import org.moldidev.moldispizza.service.ReviewService;
import org.moldidev.moldispizza.validation.ObjectValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PizzaServiceImplementation implements PizzaService {

    private final PizzaRepository pizzaRepository;
    private final PizzaDTOMapper pizzaDTOMapper;
    private final ImageRepository imageRepository;
    private final ImageService imageService;
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    private final ObjectValidator<PizzaCreateAdminRequest> pizzaCreateAdminRequestValidator;
    private final ObjectValidator<PizzaUpdateDetailsAdminRequest> pizzaUpdateDetailsAdminRequestValidator;
    private final ReviewRepository reviewRepository;
    private final ReviewService reviewService;

    @Override
    public PizzaDTO save(PizzaCreateAdminRequest request) {
        pizzaCreateAdminRequestValidator.validate(request);

        Optional<Pizza> foundPizza = pizzaRepository.findByNameIgnoreCase(request.name());

        if (foundPizza.isPresent()) {
            throw new ResourceAlreadyExistsException("This pizza name is already taken");
        }

        Pizza pizza = new Pizza();

        pizza.setImages(new ArrayList<>());
        pizza.setName(request.name());
        pizza.setIngredients(request.ingredients());
        pizza.setPrice(request.price());

        pizzaRepository.save(pizza);

        List<ImageDTO> imagesDTOs = new ArrayList<>();

        for (MultipartFile image : request.images()) {
            imagesDTOs.add(imageService.save(image));
        }

        for (ImageDTO imageDTO : imagesDTOs) {
            Image image = new Image();

            image.setImageId(imageDTO.imageId());
            image.setUrl(imageDTO.url());
            image.setType(imageDTO.type());

            pizza.getImages().add(image);
        }

        return pizzaDTOMapper.apply(pizzaRepository.save(pizza));
    }

    @Override
    public PizzaDTO findById(Long pizzaId) {
        Pizza foundPizza = pizzaRepository.findById(pizzaId)
                .orElseThrow(() -> new ResourceNotFoundException("The pizza by the provided id doesn't exist"));

        return pizzaDTOMapper.apply(foundPizza);
    }

    @Override
    public PizzaDTO findByName(String name) {
        Pizza foundPizza = pizzaRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("The pizza by the provided name doesn't exist"));

        return pizzaDTOMapper.apply(foundPizza);
    }

    @Override
    public Page<PizzaDTO> findAll(int page, int size) {
        Page<Pizza> pizzas = pizzaRepository.findAll(PageRequest.of(page, size));

        if (pizzas.isEmpty()) {
            throw new ResourceNotFoundException("No pizzas exist");
        }

        return pizzas.map(pizzaDTOMapper);
    }

    @Override
    public PizzaDTO updateById(Long pizzaId, PizzaUpdateDetailsAdminRequest request) {
        Pizza foundPizza = pizzaRepository.findById(pizzaId)
                .orElseThrow(() -> new ResourceNotFoundException("The pizza by the provided id doesn't exist"));

        pizzaUpdateDetailsAdminRequestValidator.validate(request);

        Optional<Pizza> foundPizzaByName = pizzaRepository.findByNameIgnoreCase(request.name());

        if (!request.name().matches(foundPizza.getName()) && foundPizzaByName.isPresent()) {
            throw new ResourceAlreadyExistsException("This pizza name is already taken");
        }

        List<Image> foundPizzaImages = imageRepository.findAllByPizzaId(pizzaId);

        if (!foundPizzaImages.isEmpty()) {
            foundPizzaImages.forEach(imageService::delete);
        }

        List<ImageDTO> imagesDTOs = new ArrayList<>();
        List<Image> finalImageList = new ArrayList<>();

        for (MultipartFile image : request.images()) {
            imagesDTOs.add(imageService.save(image));
        }

        for (ImageDTO imageDTO : imagesDTOs) {
            Image image = new Image();

            image.setImageId(imageDTO.imageId());
            image.setUrl(imageDTO.url());
            image.setType(imageDTO.type());

            finalImageList.add(image);
        }

        foundPizza.setName(request.name());
        foundPizza.setIngredients(request.ingredients());
        foundPizza.setPrice(request.price());
        foundPizza.setImages(finalImageList);

        return pizzaDTOMapper.apply(pizzaRepository.save(foundPizza));
    }

    @Override
    public void deleteById(Long pizzaId) {
        Pizza foundPizza = pizzaRepository.findById(pizzaId)
                .orElseThrow(() -> new ResourceNotFoundException("The pizza by the provided id doesn't exist"));

        Page<Review> foundPizzaReviews = reviewRepository.findAllByPizzaPizzaId(pizzaId, null);
        List<Image> foundPizzaImages = imageRepository.findAllByPizzaId(pizzaId);
        Page<Order> foundPizzaOrders = orderRepository.findAllByPizzasPizzaId(pizzaId, null);

        foundPizzaReviews.forEach(reviewService::delete);
        foundPizzaImages.forEach(imageService::delete);
        foundPizzaOrders.forEach(orderService::delete);

        pizzaRepository.delete(foundPizza);
    }
}
