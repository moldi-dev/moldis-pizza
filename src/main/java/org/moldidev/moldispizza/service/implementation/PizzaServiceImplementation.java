package org.moldidev.moldispizza.service.implementation;

import lombok.RequiredArgsConstructor;
import org.moldidev.moldispizza.dto.PizzaDTO;
import org.moldidev.moldispizza.entity.Image;
import org.moldidev.moldispizza.entity.Pizza;
import org.moldidev.moldispizza.exception.ResourceAlreadyExistsException;
import org.moldidev.moldispizza.exception.ResourceNotFoundException;
import org.moldidev.moldispizza.mapper.PizzaDTOMapper;
import org.moldidev.moldispizza.repository.ImageRepository;
import org.moldidev.moldispizza.repository.PizzaRepository;
import org.moldidev.moldispizza.service.ImageService;
import org.moldidev.moldispizza.service.PizzaService;
import org.moldidev.moldispizza.validation.ObjectValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ObjectValidator<Pizza> objectValidator;

    @Override
    public PizzaDTO save(Pizza pizza) {
        objectValidator.validate(pizza);

        Optional<Pizza> foundPizza = pizzaRepository.findByNameIgnoreCase(pizza.getName());

        if (foundPizza.isPresent()) {
            throw new ResourceAlreadyExistsException("This pizza name is already taken");
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
    public PizzaDTO updateById(Long pizzaId, Pizza updatedPizza) {
        Pizza foundPizza = pizzaRepository.findById(pizzaId)
                .orElseThrow(() -> new ResourceNotFoundException("The pizza by the provided id doesn't exist"));

        objectValidator.validate(updatedPizza);

        foundPizza.setImages(updatedPizza.getImages());
        foundPizza.setName(updatedPizza.getName());
        foundPizza.setIngredients(updatedPizza.getIngredients());
        foundPizza.setPrice(updatedPizza.getPrice());

        return pizzaDTOMapper.apply(pizzaRepository.save(foundPizza));
    }

    @Override
    public PizzaDTO addImage(Long pizzaId, Long imageId) {
        Pizza foundPizza = pizzaRepository.findById(pizzaId)
                .orElseThrow(() -> new ResourceNotFoundException("The pizza by the provided id doesn't exist"));

        Image foundImage = imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("The image by the provided id doesn't exist"));

        if (foundPizza.getImages().contains(foundImage)) {
            throw new ResourceAlreadyExistsException("The provided pizza already has this image");
        }

        foundPizza.getImages().add(foundImage);

        return pizzaDTOMapper.apply(pizzaRepository.save(foundPizza));
    }

    @Override
    public PizzaDTO removeImage(Long pizzaId, Long imageId) {
        Pizza foundPizza = pizzaRepository.findById(pizzaId)
                .orElseThrow(() -> new ResourceNotFoundException("The pizza by the provided id doesn't exist"));

        Image foundImage = imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("The image by the provided id doesn't exist"));

        if (!foundPizza.getImages().contains(foundImage)) {
            throw new ResourceNotFoundException("The provided pizza doesn't contain this image");
        }

        imageService.delete(foundImage);
        foundPizza.getImages().remove(foundImage);

        return pizzaDTOMapper.apply(pizzaRepository.save(foundPizza));
    }

    @Override
    public void deleteById(Long pizzaId) {
        Pizza foundPizza = pizzaRepository.findById(pizzaId)
                .orElseThrow(() -> new ResourceNotFoundException("The pizza by the provided id doesn't exist"));

        List<Image> foundPizzaImages = imageRepository.findAllByPizzaId(pizzaId);

        foundPizzaImages.forEach(imageService::delete);

        pizzaRepository.delete(foundPizza);
    }
}
