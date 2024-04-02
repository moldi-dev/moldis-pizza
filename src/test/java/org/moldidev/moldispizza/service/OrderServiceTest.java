package org.moldidev.moldispizza.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.moldidev.moldispizza.dto.OrderDTO;
import org.moldidev.moldispizza.dto.OrderDTOMapper;
import org.moldidev.moldispizza.dto.UserDTO;
import org.moldidev.moldispizza.entity.Basket;
import org.moldidev.moldispizza.entity.Order;
import org.moldidev.moldispizza.entity.Pizza;
import org.moldidev.moldispizza.entity.User;
import org.moldidev.moldispizza.exception.ResourceNotFoundException;
import org.moldidev.moldispizza.repository.BasketRepository;
import org.moldidev.moldispizza.repository.OrderRepository;
import org.moldidev.moldispizza.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {OrderService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class OrderServiceTest {
    @MockBean
    private BasketRepository basketRepository;

    @MockBean
    private OrderDTOMapper orderDTOMapper;

    @MockBean
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @MockBean
    private UserRepository userRepository;

    /**
     * Method under test: {@link OrderService#getAllOrders()}
     */
    @Test
    void getAllOrdersTest() throws ResourceNotFoundException {
        // Arrange
        when(orderRepository.findAll()).thenReturn(new ArrayList<>());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> orderService.getAllOrders());
        verify(orderRepository).findAll();
    }

    /**
     * Method under test: {@link OrderService#getAllOrders()}
     */
    @Test
    void getAllOrdersTest2() throws ResourceNotFoundException {
        // Arrange
        User user = new User();
        user.setAddress("42 Main St");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole("there are no orders");
        user.setUsername("janedoe");

        Order order = new Order();
        order.setDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        order.setId(1L);
        order.setPizzaList(new ArrayList<>());
        order.setPrice(10.0d);
        order.setUser(user);

        ArrayList<Order> orderList = new ArrayList<>();
        orderList.add(order);
        when(orderRepository.findAll()).thenReturn(orderList);
        UserDTO userDTO = new UserDTO(1L, "janedoe", "Jane", "Doe", "42 Main St", "Role", "jane.doe@example.org");

        ArrayList<Pizza> pizzaList = new ArrayList<>();
        OrderDTO orderDTO = new OrderDTO(1L, userDTO, pizzaList, 10.0d,
                Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));

        when(orderDTOMapper.apply(Mockito.<Order>any())).thenReturn(orderDTO);

        // Act
        List<OrderDTO> actualAllOrders = orderService.getAllOrders();

        // Assert
        verify(orderDTOMapper).apply(isA(Order.class));
        verify(orderRepository).findAll();
        assertEquals(1, actualAllOrders.size());
        assertSame(orderDTO, actualAllOrders.get(0));
    }

    /**
     * Method under test: {@link OrderService#getAllOrders()}
     */
    @Test
    void getAllOrdersTest3() throws ResourceNotFoundException {
        // Arrange
        User user = new User();
        user.setAddress("42 Main St");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole("there are no orders");
        user.setUsername("janedoe");

        Order order = new Order();
        order.setDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        order.setId(1L);
        order.setPizzaList(new ArrayList<>());
        order.setPrice(10.0d);
        order.setUser(user);

        User user2 = new User();
        user2.setAddress("17 High St");
        user2.setEmail("john.smith@example.org");
        user2.setFirstName("John");
        user2.setId(2L);
        user2.setLastName("Smith");
        user2.setPassword("Password");
        user2.setRole("CUSTOMER");
        user2.setUsername("Username");

        Order order2 = new Order();
        order2.setDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        order2.setId(2L);
        order2.setPizzaList(new ArrayList<>());
        order2.setPrice(0.5d);
        order2.setUser(user2);

        ArrayList<Order> orderList = new ArrayList<>();
        orderList.add(order2);
        orderList.add(order);
        when(orderRepository.findAll()).thenReturn(orderList);
        UserDTO userDTO = new UserDTO(1L, "janedoe", "Jane", "Doe", "42 Main St", "Role", "jane.doe@example.org");

        ArrayList<Pizza> pizzaList = new ArrayList<>();
        OrderDTO orderDTO = new OrderDTO(1L, userDTO, pizzaList, 10.0d,
                Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));

        when(orderDTOMapper.apply(Mockito.<Order>any())).thenReturn(orderDTO);

        // Act
        List<OrderDTO> actualAllOrders = orderService.getAllOrders();

        // Assert
        verify(orderDTOMapper, atLeast(1)).apply(Mockito.<Order>any());
        verify(orderRepository).findAll();
        assertEquals(2, actualAllOrders.size());
        assertSame(orderDTO, actualAllOrders.get(0));
    }

    /**
     * Method under test: {@link OrderService#getAllOrders()}
     */
    @Test
    void getAllOrdersTest4() throws ResourceNotFoundException {
        // Arrange
        User user = new User();
        user.setAddress("42 Main St");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole("there are no orders");
        user.setUsername("janedoe");

        Order order = new Order();
        order.setDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        order.setId(1L);
        order.setPizzaList(new ArrayList<>());
        order.setPrice(10.0d);
        order.setUser(user);

        ArrayList<Order> orderList = new ArrayList<>();
        orderList.add(order);
        when(orderRepository.findAll()).thenReturn(orderList);
        when(orderDTOMapper.apply(Mockito.<Order>any())).thenThrow(new ResourceNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> orderService.getAllOrders());
        verify(orderDTOMapper).apply(isA(Order.class));
        verify(orderRepository).findAll();
    }

    /**
     * Method under test: {@link OrderService#getOrderById(Long)}
     */
    @Test
    void getOrderByIdTest() throws ResourceNotFoundException {
        // Arrange
        User user = new User();
        user.setAddress("42 Main St");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUsername("janedoe");

        Order order = new Order();
        order.setDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        order.setId(1L);
        order.setPizzaList(new ArrayList<>());
        order.setPrice(10.0d);
        order.setUser(user);
        Optional<Order> ofResult = Optional.of(order);
        when(orderRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        UserDTO userDTO = new UserDTO(1L, "janedoe", "Jane", "Doe", "42 Main St", "Role", "jane.doe@example.org");

        ArrayList<Pizza> pizzaList = new ArrayList<>();
        OrderDTO orderDTO = new OrderDTO(1L, userDTO, pizzaList, 10.0d,
                Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));

        when(orderDTOMapper.apply(Mockito.<Order>any())).thenReturn(orderDTO);

        // Act
        OrderDTO actualOrderById = orderService.getOrderById(1L);

        // Assert
        verify(orderDTOMapper).apply(isA(Order.class));
        verify(orderRepository).findById(isA(Long.class));
        assertSame(orderDTO, actualOrderById);
    }

    /**
     * Method under test: {@link OrderService#getOrderById(Long)}
     */
    @Test
    void getOrderByIdTest2() throws ResourceNotFoundException {
        // Arrange
        User user = new User();
        user.setAddress("42 Main St");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUsername("janedoe");

        Order order = new Order();
        order.setDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        order.setId(1L);
        order.setPizzaList(new ArrayList<>());
        order.setPrice(10.0d);
        order.setUser(user);
        Optional<Order> ofResult = Optional.of(order);
        when(orderRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(orderDTOMapper.apply(Mockito.<Order>any())).thenThrow(new ResourceNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(1L));
        verify(orderDTOMapper).apply(isA(Order.class));
        verify(orderRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link OrderService#getOrderById(Long)}
     */
    @Test
    void getOrderByIdTest3() throws ResourceNotFoundException {
        // Arrange
        Optional<Order> emptyResult = Optional.empty();
        when(orderRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(1L));
        verify(orderRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link OrderService#getAllOrdersByUserId(Long)}
     */
    @Test
    void getAllOrdersByUserIdTest() throws ResourceNotFoundException {
        // Arrange
        when(orderRepository.getAllOrdersByUserId(Mockito.<Long>any())).thenReturn(new ArrayList<>());

        User user = new User();
        user.setAddress("42 Main St");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> orderService.getAllOrdersByUserId(1L));
        verify(orderRepository).getAllOrdersByUserId(isA(Long.class));
        verify(userRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link OrderService#getAllOrdersByUserId(Long)}
     */
    @Test
    void getAllOrdersByUserIdTest2() throws ResourceNotFoundException {
        // Arrange
        when(userRepository.findById(Mockito.<Long>any())).thenThrow(new ResourceNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> orderService.getAllOrdersByUserId(1L));
        verify(userRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link OrderService#getAllOrdersByUserId(Long)}
     */
    @Test
    void getAllOrdersByUserIdTest3() throws ResourceNotFoundException {
        // Arrange
        User user = new User();
        user.setAddress("42 Main St");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUsername("janedoe");

        Order order = new Order();
        order.setDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        order.setId(1L);
        order.setPizzaList(new ArrayList<>());
        order.setPrice(10.0d);
        order.setUser(user);

        ArrayList<Order> orderList = new ArrayList<>();
        orderList.add(order);
        when(orderRepository.getAllOrdersByUserId(Mockito.<Long>any())).thenReturn(orderList);

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user2);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        UserDTO userDTO = new UserDTO(1L, "janedoe", "Jane", "Doe", "42 Main St", "Role", "jane.doe@example.org");

        ArrayList<Pizza> pizzaList = new ArrayList<>();
        OrderDTO orderDTO = new OrderDTO(1L, userDTO, pizzaList, 10.0d,
                Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));

        when(orderDTOMapper.apply(Mockito.<Order>any())).thenReturn(orderDTO);

        // Act
        List<OrderDTO> actualAllOrdersByUserId = orderService.getAllOrdersByUserId(1L);

        // Assert
        verify(orderDTOMapper).apply(isA(Order.class));
        verify(orderRepository).getAllOrdersByUserId(isA(Long.class));
        verify(userRepository).findById(isA(Long.class));
        assertEquals(1, actualAllOrdersByUserId.size());
        assertSame(orderDTO, actualAllOrdersByUserId.get(0));
    }

    /**
     * Method under test: {@link OrderService#getAllOrdersByUserId(Long)}
     */
    @Test
    void getAllOrdersByUserIdTest4() throws ResourceNotFoundException {
        // Arrange
        User user = new User();
        user.setAddress("42 Main St");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUsername("janedoe");

        Order order = new Order();
        order.setDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        order.setId(1L);
        order.setPizzaList(new ArrayList<>());
        order.setPrice(10.0d);
        order.setUser(user);

        User user2 = new User();
        user2.setAddress("17 High St");
        user2.setEmail("john.smith@example.org");
        user2.setFirstName("John");
        user2.setId(2L);
        user2.setLastName("Smith");
        user2.setPassword("Password");
        user2.setRole("CUSTOMER");
        user2.setUsername("Username");

        Order order2 = new Order();
        order2.setDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        order2.setId(2L);
        order2.setPizzaList(new ArrayList<>());
        order2.setPrice(0.5d);
        order2.setUser(user2);

        ArrayList<Order> orderList = new ArrayList<>();
        orderList.add(order2);
        orderList.add(order);
        when(orderRepository.getAllOrdersByUserId(Mockito.<Long>any())).thenReturn(orderList);

        User user3 = new User();
        user3.setAddress("42 Main St");
        user3.setEmail("jane.doe@example.org");
        user3.setFirstName("Jane");
        user3.setId(1L);
        user3.setLastName("Doe");
        user3.setPassword("iloveyou");
        user3.setRole("Role");
        user3.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user3);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        UserDTO userDTO = new UserDTO(1L, "janedoe", "Jane", "Doe", "42 Main St", "Role", "jane.doe@example.org");

        ArrayList<Pizza> pizzaList = new ArrayList<>();
        OrderDTO orderDTO = new OrderDTO(1L, userDTO, pizzaList, 10.0d,
                Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));

        when(orderDTOMapper.apply(Mockito.<Order>any())).thenReturn(orderDTO);

        // Act
        List<OrderDTO> actualAllOrdersByUserId = orderService.getAllOrdersByUserId(1L);

        // Assert
        verify(orderDTOMapper, atLeast(1)).apply(Mockito.<Order>any());
        verify(orderRepository).getAllOrdersByUserId(isA(Long.class));
        verify(userRepository).findById(isA(Long.class));
        assertEquals(2, actualAllOrdersByUserId.size());
        assertSame(orderDTO, actualAllOrdersByUserId.get(0));
    }

    /**
     * Method under test: {@link OrderService#getAllOrdersByUserId(Long)}
     */
    @Test
    void getAllOrdersByUserIdTest5() throws ResourceNotFoundException {
        // Arrange
        User user = new User();
        user.setAddress("42 Main St");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUsername("janedoe");

        Order order = new Order();
        order.setDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        order.setId(1L);
        order.setPizzaList(new ArrayList<>());
        order.setPrice(10.0d);
        order.setUser(user);

        ArrayList<Order> orderList = new ArrayList<>();
        orderList.add(order);
        when(orderRepository.getAllOrdersByUserId(Mockito.<Long>any())).thenReturn(orderList);
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> orderService.getAllOrdersByUserId(1L));
        verify(orderRepository).getAllOrdersByUserId(isA(Long.class));
        verify(userRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link OrderService#getAllOrdersByUsername(String)}
     */
    @Test
    void getAllOrdersByUsernameTest() throws ResourceNotFoundException {
        // Arrange
        when(orderRepository.getAllOrdersByUsername(Mockito.<String>any())).thenReturn(new ArrayList<>());

        User user = new User();
        user.setAddress("42 Main St");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findUserByUsername(Mockito.<String>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> orderService.getAllOrdersByUsername("janedoe"));
        verify(orderRepository).getAllOrdersByUsername(eq("janedoe"));
        verify(userRepository).findUserByUsername(eq("janedoe"));
    }

    /**
     * Method under test: {@link OrderService#getAllOrdersByUsername(String)}
     */
    @Test
    void getAllOrdersByUsernameTest2() throws ResourceNotFoundException {
        // Arrange
        when(userRepository.findUserByUsername(Mockito.<String>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> orderService.getAllOrdersByUsername("janedoe"));
        verify(userRepository).findUserByUsername(eq("janedoe"));
    }

    /**
     * Method under test: {@link OrderService#getAllOrdersByUsername(String)}
     */
    @Test
    void getAllOrdersByUsernameTest3() throws ResourceNotFoundException {
        // Arrange
        User user = new User();
        user.setAddress("42 Main St");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUsername("janedoe");

        Order order = new Order();
        order.setDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        order.setId(1L);
        order.setPizzaList(new ArrayList<>());
        order.setPrice(10.0d);
        order.setUser(user);

        ArrayList<Order> orderList = new ArrayList<>();
        orderList.add(order);
        when(orderRepository.getAllOrdersByUsername(Mockito.<String>any())).thenReturn(orderList);

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user2);
        when(userRepository.findUserByUsername(Mockito.<String>any())).thenReturn(ofResult);
        UserDTO userDTO = new UserDTO(1L, "janedoe", "Jane", "Doe", "42 Main St", "Role", "jane.doe@example.org");

        ArrayList<Pizza> pizzaList = new ArrayList<>();
        OrderDTO orderDTO = new OrderDTO(1L, userDTO, pizzaList, 10.0d,
                Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));

        when(orderDTOMapper.apply(Mockito.<Order>any())).thenReturn(orderDTO);

        // Act
        List<OrderDTO> actualAllOrdersByUsername = orderService.getAllOrdersByUsername("janedoe");

        // Assert
        verify(orderDTOMapper).apply(isA(Order.class));
        verify(orderRepository).getAllOrdersByUsername(eq("janedoe"));
        verify(userRepository).findUserByUsername(eq("janedoe"));
        assertEquals(1, actualAllOrdersByUsername.size());
        assertSame(orderDTO, actualAllOrdersByUsername.get(0));
    }

    /**
     * Method under test: {@link OrderService#getAllOrdersByUsername(String)}
     */
    @Test
    void getAllOrdersByUsernameTest4() throws ResourceNotFoundException {
        // Arrange
        User user = new User();
        user.setAddress("42 Main St");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUsername("janedoe");

        Order order = new Order();
        order.setDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        order.setId(1L);
        order.setPizzaList(new ArrayList<>());
        order.setPrice(10.0d);
        order.setUser(user);

        User user2 = new User();
        user2.setAddress("17 High St");
        user2.setEmail("john.smith@example.org");
        user2.setFirstName("John");
        user2.setId(2L);
        user2.setLastName("Smith");
        user2.setPassword("Password");
        user2.setRole("CUSTOMER");
        user2.setUsername("Username");

        Order order2 = new Order();
        order2.setDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        order2.setId(2L);
        order2.setPizzaList(new ArrayList<>());
        order2.setPrice(0.5d);
        order2.setUser(user2);

        ArrayList<Order> orderList = new ArrayList<>();
        orderList.add(order2);
        orderList.add(order);
        when(orderRepository.getAllOrdersByUsername(Mockito.<String>any())).thenReturn(orderList);

        User user3 = new User();
        user3.setAddress("42 Main St");
        user3.setEmail("jane.doe@example.org");
        user3.setFirstName("Jane");
        user3.setId(1L);
        user3.setLastName("Doe");
        user3.setPassword("iloveyou");
        user3.setRole("Role");
        user3.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user3);
        when(userRepository.findUserByUsername(Mockito.<String>any())).thenReturn(ofResult);
        UserDTO userDTO = new UserDTO(1L, "janedoe", "Jane", "Doe", "42 Main St", "Role", "jane.doe@example.org");

        ArrayList<Pizza> pizzaList = new ArrayList<>();
        OrderDTO orderDTO = new OrderDTO(1L, userDTO, pizzaList, 10.0d,
                Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));

        when(orderDTOMapper.apply(Mockito.<Order>any())).thenReturn(orderDTO);

        // Act
        List<OrderDTO> actualAllOrdersByUsername = orderService.getAllOrdersByUsername("janedoe");

        // Assert
        verify(orderDTOMapper, atLeast(1)).apply(Mockito.<Order>any());
        verify(orderRepository).getAllOrdersByUsername(eq("janedoe"));
        verify(userRepository).findUserByUsername(eq("janedoe"));
        assertEquals(2, actualAllOrdersByUsername.size());
        assertSame(orderDTO, actualAllOrdersByUsername.get(0));
    }

    /**
     * Method under test: {@link OrderService#getAllOrdersByUsername(String)}
     */
    @Test
    void getAllOrdersByUsernameTest5() throws ResourceNotFoundException {
        // Arrange
        User user = new User();
        user.setAddress("42 Main St");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUsername("janedoe");

        Order order = new Order();
        order.setDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        order.setId(1L);
        order.setPizzaList(new ArrayList<>());
        order.setPrice(10.0d);
        order.setUser(user);

        ArrayList<Order> orderList = new ArrayList<>();
        orderList.add(order);
        when(orderRepository.getAllOrdersByUsername(Mockito.<String>any())).thenReturn(orderList);
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findUserByUsername(Mockito.<String>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> orderService.getAllOrdersByUsername("janedoe"));
        verify(orderRepository).getAllOrdersByUsername(eq("janedoe"));
        verify(userRepository).findUserByUsername(eq("janedoe"));
    }

    /**
     * Method under test: {@link OrderService#getAllOrdersByUsername(String)}
     */
    @Test
    void getAllOrdersByUsernameTest6() throws ResourceNotFoundException {
        // Arrange
        User user = new User();
        user.setAddress("42 Main St");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUsername("janedoe");

        Order order = new Order();
        order.setDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        order.setId(1L);
        order.setPizzaList(new ArrayList<>());
        order.setPrice(10.0d);
        order.setUser(user);

        ArrayList<Order> orderList = new ArrayList<>();
        orderList.add(order);
        when(orderRepository.getAllOrdersByUsername(Mockito.<String>any())).thenReturn(orderList);

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user2);
        when(userRepository.findUserByUsername(Mockito.<String>any())).thenReturn(ofResult);
        when(orderDTOMapper.apply(Mockito.<Order>any())).thenThrow(new ResourceNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> orderService.getAllOrdersByUsername("janedoe"));
        verify(orderDTOMapper).apply(isA(Order.class));
        verify(orderRepository).getAllOrdersByUsername(eq("janedoe"));
        verify(userRepository).findUserByUsername(eq("janedoe"));
    }

    /**
     * Method under test: {@link OrderService#addOrderByUserIdBasket(Long)}
     */
    @Test
    void addOrderByUserIdBasketTest() throws ResourceNotFoundException {
        // Arrange
        User user = new User();
        user.setAddress("42 Main St");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUsername("janedoe");

        Basket basket = new Basket();
        basket.setId(1L);
        basket.setPizzaList(new ArrayList<>());
        basket.setUser(user);
        Optional<Basket> ofResult = Optional.of(basket);
        when(basketRepository.getBasketByUserId(Mockito.<Long>any())).thenReturn(ofResult);
        Optional<Double> ofResult2 = Optional.<Double>of(10.0d);
        when(basketRepository.getBasketTotalPriceByUserId(Mockito.<Long>any())).thenReturn(ofResult2);

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");
        Optional<User> ofResult3 = Optional.of(user2);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult3);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> orderService.addOrderByUserIdBasket(1L));
        verify(basketRepository).getBasketByUserId(isA(Long.class));
        verify(basketRepository).getBasketTotalPriceByUserId(isA(Long.class));
        verify(userRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link OrderService#addOrderByUserIdBasket(Long)}
     */
    @Test
    void addOrderByUserIdBasketTest2() throws ResourceNotFoundException {
        // Arrange
        User user = new User();
        user.setAddress("42 Main St");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUsername("janedoe");

        Basket basket = new Basket();
        basket.setId(1L);
        basket.setPizzaList(new ArrayList<>());
        basket.setUser(user);
        Optional<Basket> ofResult = Optional.of(basket);
        when(basketRepository.getBasketByUserId(Mockito.<Long>any())).thenReturn(ofResult);
        Optional<Double> ofResult2 = Optional.<Double>of(10.0d);
        when(basketRepository.getBasketTotalPriceByUserId(Mockito.<Long>any())).thenReturn(ofResult2);
        when(userRepository.findById(Mockito.<Long>any())).thenThrow(new ResourceNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> orderService.addOrderByUserIdBasket(1L));
        verify(basketRepository).getBasketByUserId(isA(Long.class));
        verify(basketRepository).getBasketTotalPriceByUserId(isA(Long.class));
        verify(userRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link OrderService#addOrderByUserIdBasket(Long)}
     */
    @Test
    void addOrderByUserIdBasketTest3() throws ResourceNotFoundException {
        // Arrange
        User user = new User();
        user.setAddress("42 Main St");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUsername("janedoe");

        Order order = new Order();
        order.setDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        order.setId(1L);
        order.setPizzaList(new ArrayList<>());
        order.setPrice(10.0d);
        order.setUser(user);
        when(orderRepository.save(Mockito.<Order>any())).thenReturn(order);

        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);

        ArrayList<Pizza> pizzaList = new ArrayList<>();
        pizzaList.add(pizza);

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");

        Basket basket = new Basket();
        basket.setId(1L);
        basket.setPizzaList(pizzaList);
        basket.setUser(user2);
        Optional<Basket> ofResult = Optional.of(basket);

        User user3 = new User();
        user3.setAddress("42 Main St");
        user3.setEmail("jane.doe@example.org");
        user3.setFirstName("Jane");
        user3.setId(1L);
        user3.setLastName("Doe");
        user3.setPassword("iloveyou");
        user3.setRole("Role");
        user3.setUsername("janedoe");

        Basket basket2 = new Basket();
        basket2.setId(1L);
        basket2.setPizzaList(new ArrayList<>());
        basket2.setUser(user3);
        when(basketRepository.save(Mockito.<Basket>any())).thenReturn(basket2);
        when(basketRepository.getBasketByUserId(Mockito.<Long>any())).thenReturn(ofResult);
        Optional<Double> ofResult2 = Optional.<Double>of(10.0d);
        when(basketRepository.getBasketTotalPriceByUserId(Mockito.<Long>any())).thenReturn(ofResult2);

        User user4 = new User();
        user4.setAddress("42 Main St");
        user4.setEmail("jane.doe@example.org");
        user4.setFirstName("Jane");
        user4.setId(1L);
        user4.setLastName("Doe");
        user4.setPassword("iloveyou");
        user4.setRole("Role");
        user4.setUsername("janedoe");
        Optional<User> ofResult3 = Optional.of(user4);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult3);
        UserDTO userDTO = new UserDTO(1L, "janedoe", "Jane", "Doe", "42 Main St", "Role", "jane.doe@example.org");

        ArrayList<Pizza> pizzaList2 = new ArrayList<>();
        OrderDTO orderDTO = new OrderDTO(1L, userDTO, pizzaList2, 10.0d,
                Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));

        when(orderDTOMapper.apply(Mockito.<Order>any())).thenReturn(orderDTO);

        // Act
        OrderDTO actualAddOrderByUserIdBasketResult = orderService.addOrderByUserIdBasket(1L);

        // Assert
        verify(orderDTOMapper).apply(isA(Order.class));
        verify(basketRepository).getBasketByUserId(isA(Long.class));
        verify(basketRepository).getBasketTotalPriceByUserId(isA(Long.class));
        verify(userRepository).findById(isA(Long.class));
        verify(basketRepository).save(isA(Basket.class));
        verify(orderRepository).save(isA(Order.class));
        assertSame(orderDTO, actualAddOrderByUserIdBasketResult);
    }

    /**
     * Method under test: {@link OrderService#addOrderByUserIdBasket(Long)}
     */
    @Test
    void addOrderByUserIdBasketTest4() throws ResourceNotFoundException {
        // Arrange
        User user = new User();
        user.setAddress("42 Main St");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUsername("janedoe");

        Order order = new Order();
        order.setDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        order.setId(1L);
        order.setPizzaList(new ArrayList<>());
        order.setPrice(10.0d);
        order.setUser(user);
        when(orderRepository.save(Mockito.<Order>any())).thenReturn(order);

        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);

        ArrayList<Pizza> pizzaList = new ArrayList<>();
        pizzaList.add(pizza);

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");

        Basket basket = new Basket();
        basket.setId(1L);
        basket.setPizzaList(pizzaList);
        basket.setUser(user2);
        Optional<Basket> ofResult = Optional.of(basket);

        User user3 = new User();
        user3.setAddress("42 Main St");
        user3.setEmail("jane.doe@example.org");
        user3.setFirstName("Jane");
        user3.setId(1L);
        user3.setLastName("Doe");
        user3.setPassword("iloveyou");
        user3.setRole("Role");
        user3.setUsername("janedoe");

        Basket basket2 = new Basket();
        basket2.setId(1L);
        basket2.setPizzaList(new ArrayList<>());
        basket2.setUser(user3);
        when(basketRepository.save(Mockito.<Basket>any())).thenReturn(basket2);
        when(basketRepository.getBasketByUserId(Mockito.<Long>any())).thenReturn(ofResult);
        Optional<Double> ofResult2 = Optional.<Double>of(10.0d);
        when(basketRepository.getBasketTotalPriceByUserId(Mockito.<Long>any())).thenReturn(ofResult2);

        User user4 = new User();
        user4.setAddress("42 Main St");
        user4.setEmail("jane.doe@example.org");
        user4.setFirstName("Jane");
        user4.setId(1L);
        user4.setLastName("Doe");
        user4.setPassword("iloveyou");
        user4.setRole("Role");
        user4.setUsername("janedoe");
        Optional<User> ofResult3 = Optional.of(user4);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult3);
        when(orderDTOMapper.apply(Mockito.<Order>any())).thenThrow(new ResourceNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> orderService.addOrderByUserIdBasket(1L));
        verify(orderDTOMapper).apply(isA(Order.class));
        verify(basketRepository).getBasketByUserId(isA(Long.class));
        verify(basketRepository).getBasketTotalPriceByUserId(isA(Long.class));
        verify(userRepository).findById(isA(Long.class));
        verify(basketRepository).save(isA(Basket.class));
        verify(orderRepository).save(isA(Order.class));
    }

    /**
     * Method under test: {@link OrderService#addOrderByUsernameBasket(String)}
     */
    @Test
    void addOrderByUsernameBasketTest() {
        // Arrange
        User user = new User();
        user.setAddress("42 Main St");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUsername("janedoe");

        Basket basket = new Basket();
        basket.setId(1L);
        basket.setPizzaList(new ArrayList<>());
        basket.setUser(user);
        Optional<Basket> ofResult = Optional.of(basket);
        when(basketRepository.getBasketByUsername(Mockito.<String>any())).thenReturn(ofResult);
        Optional<Double> ofResult2 = Optional.<Double>of(10.0d);
        when(basketRepository.getBasketTotalPriceByUsername(Mockito.<String>any())).thenReturn(ofResult2);

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");
        Optional<User> ofResult3 = Optional.of(user2);
        when(userRepository.findUserByUsername(Mockito.<String>any())).thenReturn(ofResult3);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> orderService.addOrderByUsernameBasket("janedoe"));
        verify(basketRepository).getBasketByUsername(eq("janedoe"));
        verify(basketRepository).getBasketTotalPriceByUsername(eq("janedoe"));
        verify(userRepository).findUserByUsername(eq("janedoe"));
    }

    /**
     * Method under test: {@link OrderService#addOrderByUsernameBasket(String)}
     */
    @Test
    void addOrderByUsernameBasketTest2() {
        // Arrange
        User user = new User();
        user.setAddress("42 Main St");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUsername("janedoe");

        Basket basket = new Basket();
        basket.setId(1L);
        basket.setPizzaList(new ArrayList<>());
        basket.setUser(user);
        Optional<Basket> ofResult = Optional.of(basket);
        when(basketRepository.getBasketByUsername(Mockito.<String>any())).thenReturn(ofResult);
        Optional<Double> ofResult2 = Optional.<Double>of(10.0d);
        when(basketRepository.getBasketTotalPriceByUsername(Mockito.<String>any())).thenReturn(ofResult2);
        when(userRepository.findUserByUsername(Mockito.<String>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> orderService.addOrderByUsernameBasket("janedoe"));
        verify(basketRepository).getBasketByUsername(eq("janedoe"));
        verify(basketRepository).getBasketTotalPriceByUsername(eq("janedoe"));
        verify(userRepository).findUserByUsername(eq("janedoe"));
    }

    /**
     * Method under test: {@link OrderService#addOrderByUsernameBasket(String)}
     */
    @Test
    void addOrderByUsernameBasketTest3() {
        // Arrange
        User user = new User();
        user.setAddress("42 Main St");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUsername("janedoe");

        Order order = new Order();
        order.setDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        order.setId(1L);
        order.setPizzaList(new ArrayList<>());
        order.setPrice(10.0d);
        order.setUser(user);
        when(orderRepository.save(Mockito.<Order>any())).thenReturn(order);

        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);

        ArrayList<Pizza> pizzaList = new ArrayList<>();
        pizzaList.add(pizza);

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");

        Basket basket = new Basket();
        basket.setId(1L);
        basket.setPizzaList(pizzaList);
        basket.setUser(user2);
        Optional<Basket> ofResult = Optional.of(basket);

        User user3 = new User();
        user3.setAddress("42 Main St");
        user3.setEmail("jane.doe@example.org");
        user3.setFirstName("Jane");
        user3.setId(1L);
        user3.setLastName("Doe");
        user3.setPassword("iloveyou");
        user3.setRole("Role");
        user3.setUsername("janedoe");

        Basket basket2 = new Basket();
        basket2.setId(1L);
        basket2.setPizzaList(new ArrayList<>());
        basket2.setUser(user3);
        when(basketRepository.save(Mockito.<Basket>any())).thenReturn(basket2);
        when(basketRepository.getBasketByUsername(Mockito.<String>any())).thenReturn(ofResult);
        Optional<Double> ofResult2 = Optional.<Double>of(10.0d);
        when(basketRepository.getBasketTotalPriceByUsername(Mockito.<String>any())).thenReturn(ofResult2);

        User user4 = new User();
        user4.setAddress("42 Main St");
        user4.setEmail("jane.doe@example.org");
        user4.setFirstName("Jane");
        user4.setId(1L);
        user4.setLastName("Doe");
        user4.setPassword("iloveyou");
        user4.setRole("Role");
        user4.setUsername("janedoe");
        Optional<User> ofResult3 = Optional.of(user4);
        when(userRepository.findUserByUsername(Mockito.<String>any())).thenReturn(ofResult3);
        UserDTO userDTO = new UserDTO(1L, "janedoe", "Jane", "Doe", "42 Main St", "Role", "jane.doe@example.org");

        ArrayList<Pizza> pizzaList2 = new ArrayList<>();
        OrderDTO orderDTO = new OrderDTO(1L, userDTO, pizzaList2, 10.0d,
                Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));

        when(orderDTOMapper.apply(Mockito.<Order>any())).thenReturn(orderDTO);

        // Act
        OrderDTO actualAddOrderByUsernameBasketResult = orderService.addOrderByUsernameBasket("janedoe");

        // Assert
        verify(orderDTOMapper).apply(isA(Order.class));
        verify(basketRepository).getBasketByUsername(eq("janedoe"));
        verify(basketRepository).getBasketTotalPriceByUsername(eq("janedoe"));
        verify(userRepository).findUserByUsername(eq("janedoe"));
        verify(basketRepository).save(isA(Basket.class));
        verify(orderRepository).save(isA(Order.class));
        assertSame(orderDTO, actualAddOrderByUsernameBasketResult);
    }

    /**
     * Method under test: {@link OrderService#addOrderByUsernameBasket(String)}
     */
    @Test
    void addOrderByUsernameBasketTest4() {
        // Arrange
        User user = new User();
        user.setAddress("42 Main St");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUsername("janedoe");

        Order order = new Order();
        order.setDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        order.setId(1L);
        order.setPizzaList(new ArrayList<>());
        order.setPrice(10.0d);
        order.setUser(user);
        when(orderRepository.save(Mockito.<Order>any())).thenReturn(order);

        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);

        ArrayList<Pizza> pizzaList = new ArrayList<>();
        pizzaList.add(pizza);

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");

        Basket basket = new Basket();
        basket.setId(1L);
        basket.setPizzaList(pizzaList);
        basket.setUser(user2);
        Optional<Basket> ofResult = Optional.of(basket);

        User user3 = new User();
        user3.setAddress("42 Main St");
        user3.setEmail("jane.doe@example.org");
        user3.setFirstName("Jane");
        user3.setId(1L);
        user3.setLastName("Doe");
        user3.setPassword("iloveyou");
        user3.setRole("Role");
        user3.setUsername("janedoe");

        Basket basket2 = new Basket();
        basket2.setId(1L);
        basket2.setPizzaList(new ArrayList<>());
        basket2.setUser(user3);
        when(basketRepository.save(Mockito.<Basket>any())).thenReturn(basket2);
        when(basketRepository.getBasketByUsername(Mockito.<String>any())).thenReturn(ofResult);
        Optional<Double> ofResult2 = Optional.<Double>of(10.0d);
        when(basketRepository.getBasketTotalPriceByUsername(Mockito.<String>any())).thenReturn(ofResult2);

        User user4 = new User();
        user4.setAddress("42 Main St");
        user4.setEmail("jane.doe@example.org");
        user4.setFirstName("Jane");
        user4.setId(1L);
        user4.setLastName("Doe");
        user4.setPassword("iloveyou");
        user4.setRole("Role");
        user4.setUsername("janedoe");
        Optional<User> ofResult3 = Optional.of(user4);
        when(userRepository.findUserByUsername(Mockito.<String>any())).thenReturn(ofResult3);
        when(orderDTOMapper.apply(Mockito.<Order>any())).thenThrow(new ResourceNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> orderService.addOrderByUsernameBasket("janedoe"));
        verify(orderDTOMapper).apply(isA(Order.class));
        verify(basketRepository).getBasketByUsername(eq("janedoe"));
        verify(basketRepository).getBasketTotalPriceByUsername(eq("janedoe"));
        verify(userRepository).findUserByUsername(eq("janedoe"));
        verify(basketRepository).save(isA(Basket.class));
        verify(orderRepository).save(isA(Order.class));
    }

    /**
     * Method under test: {@link OrderService#deleteOrderById(Long)}
     */
    @Test
    void deleteOrderByIdTest() throws ResourceNotFoundException {
        // Arrange
        User user = new User();
        user.setAddress("42 Main St");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUsername("janedoe");

        Order order = new Order();
        order.setDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        order.setId(1L);
        order.setPizzaList(new ArrayList<>());
        order.setPrice(10.0d);
        order.setUser(user);
        Optional<Order> ofResult = Optional.of(order);
        doNothing().when(orderRepository).deleteById(Mockito.<Long>any());
        when(orderRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act
        orderService.deleteOrderById(1L);

        // Assert that nothing has changed
        verify(orderRepository).deleteById(isA(Long.class));
        verify(orderRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link OrderService#deleteOrderById(Long)}
     */
    @Test
    void deleteOrderByIdTest2() throws ResourceNotFoundException {
        // Arrange
        User user = new User();
        user.setAddress("42 Main St");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUsername("janedoe");

        Order order = new Order();
        order.setDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        order.setId(1L);
        order.setPizzaList(new ArrayList<>());
        order.setPrice(10.0d);
        order.setUser(user);
        Optional<Order> ofResult = Optional.of(order);
        doThrow(new ResourceNotFoundException("An error occurred")).when(orderRepository).deleteById(Mockito.<Long>any());
        when(orderRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> orderService.deleteOrderById(1L));
        verify(orderRepository).deleteById(isA(Long.class));
        verify(orderRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link OrderService#deleteOrderById(Long)}
     */
    @Test
    void deleteOrderByIdTest3() throws ResourceNotFoundException {
        // Arrange
        Optional<Order> emptyResult = Optional.empty();
        when(orderRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> orderService.deleteOrderById(1L));
        verify(orderRepository).findById(isA(Long.class));
    }
}
