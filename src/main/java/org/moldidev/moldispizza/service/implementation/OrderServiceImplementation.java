package org.moldidev.moldispizza.service.implementation;

import lombok.RequiredArgsConstructor;
import org.moldidev.moldispizza.dto.OrderDTO;
import org.moldidev.moldispizza.entity.Basket;
import org.moldidev.moldispizza.entity.Order;
import org.moldidev.moldispizza.entity.User;
import org.moldidev.moldispizza.enumeration.OrderStatus;
import org.moldidev.moldispizza.exception.ObjectNotValidException;
import org.moldidev.moldispizza.exception.ResourceNotFoundException;
import org.moldidev.moldispizza.mapper.OrderDTOMapper;
import org.moldidev.moldispizza.repository.BasketRepository;
import org.moldidev.moldispizza.repository.OrderRepository;
import org.moldidev.moldispizza.repository.UserRepository;
import org.moldidev.moldispizza.request.admin.OrderUpdateAdminRequest;
import org.moldidev.moldispizza.service.EmailService;
import org.moldidev.moldispizza.service.OrderService;
import org.moldidev.moldispizza.service.SecurityService;
import org.moldidev.moldispizza.validation.ObjectValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImplementation implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDTOMapper orderDTOMapper;
    private final BasketRepository basketRepository;
    private final SecurityService securityService;
    private final UserRepository userRepository;
    private final EmailService emailService;

    private final ObjectValidator<OrderUpdateAdminRequest> orderUpdateAdminRequestValidator;

    @Override
    public OrderDTO findPendingOrderById(UUID orderId, Authentication connectedUser) {
        Order foundOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("The order by the provided id doesn't exist"));

        securityService.validateAuthenticatedUser(connectedUser, foundOrder.getUser().getUserId());

        if (!foundOrder.getStatus().equals(OrderStatus.PENDING)) {
            throw new ResourceNotFoundException("The order by the provided id doesn't exist");
        }

        return orderDTOMapper.apply(foundOrder);
    }

    @Override
    public OrderDTO findById(UUID orderId) {
        Order foundOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("The order by the provided id doesn't exist"));

        return orderDTOMapper.apply(foundOrder);
    }

    @Override
    public Page<OrderDTO> findAll(int page, int size) {
        Page<Order> orders = orderRepository.findAll(PageRequest.of(page, size));

        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("No orders exist");
        }

        return orders.map(orderDTOMapper);
    }

    @Override
    public Page<OrderDTO> findAllByUserId(Long userId, int page, int size, Authentication connectedUser) {
        securityService.validateAuthenticatedUser(connectedUser, userId);

        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("The user by the provided id doesn't exist"));

        Page<Order> orders = orderRepository.findAllByUserUserId(userId, PageRequest.of(page, size));

        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("This user has no orders");
        }

        return orders.map(orderDTOMapper);
    }

    @Override
    public Boolean hasUserBoughtThePizza(Long userId, Long pizzaId, Authentication connectedUser) {
        securityService.validateAuthenticatedUser(connectedUser, userId);

        return orderRepository.existsByUserUserIdAndPizzasPizzaIdAndStatus(userId, pizzaId, OrderStatus.DELIVERED);
    }

    @Override
    public OrderDTO updateById(UUID orderId, OrderUpdateAdminRequest request) {
        Order foundOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("The order by the provided id doesn't exist"));

        orderUpdateAdminRequestValidator.validate(request);

        foundOrder.setStatus(request.status());
        foundOrder.setTotalPrice(request.totalPrice());

        return orderDTOMapper.apply(orderRepository.save(foundOrder));
    }

    @Override
    public OrderDTO setOrderAsPaidIfNotPaid(UUID orderId, Authentication connectedUser) {
        Order foundOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("The order by the provided id doesn't exist"));

        securityService.validateAuthenticatedUser(connectedUser, foundOrder.getUser().getUserId());

        if (!foundOrder.getStatus().equals(OrderStatus.PENDING)) {
            throw new ResourceNotFoundException("The order by the provided id doesn't exist");
        }

        foundOrder.setStatus(OrderStatus.PAID);

        OrderDTO paidOrder = orderDTOMapper.apply(orderRepository.save(foundOrder));

        emailService.sendOrderPaidEmail(foundOrder.getUser().getEmail(), paidOrder);

        return paidOrder;
    }

    @Override
    public OrderDTO placeOrderByUserBasket(Long userId, Authentication connectedUser) {
        securityService.validateAuthenticatedUser(connectedUser, userId);

        Basket foundBasket = basketRepository.findByUserUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("The basket by the provided user id doesn't exist"));

        if (foundBasket.getPizzas().isEmpty()) {
            HashSet<String> violations = new HashSet<>();
            violations.add("The pizza list is required");
            throw new ObjectNotValidException(violations);
        }

        Order placedOrder = new Order();

        placedOrder.setPizzas(foundBasket.getPizzas());
        placedOrder.setUser(foundBasket.getUser());
        placedOrder.setStatus(OrderStatus.PENDING);
        placedOrder.setTotalPrice(foundBasket.getTotalPrice());

        foundBasket.setTotalPrice(0.0);
        foundBasket.setPizzas(new ArrayList<>());

        basketRepository.save(foundBasket);

        return orderDTOMapper.apply(orderRepository.save(placedOrder));
    }

    @Override
    public void deleteById(UUID orderId) {
        Order foundOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("The order by the provided id doesn't exist"));

        orderRepository.delete(foundOrder);
    }

    @Override
    public void delete(Order order) {
        orderRepository.delete(order);
    }
}
