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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.moldidev.moldispizza.dto.BasketDTO;
import org.moldidev.moldispizza.dto.BasketDTOMapper;
import org.moldidev.moldispizza.dto.UserDTO;
import org.moldidev.moldispizza.entity.Basket;
import org.moldidev.moldispizza.entity.Pizza;
import org.moldidev.moldispizza.entity.User;
import org.moldidev.moldispizza.exception.ResourceAlreadyExistsException;
import org.moldidev.moldispizza.exception.ResourceNotFoundException;
import org.moldidev.moldispizza.repository.BasketRepository;
import org.moldidev.moldispizza.repository.PizzaRepository;
import org.moldidev.moldispizza.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {BasketService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class BasketServiceTest {
    @MockBean
    private BasketDTOMapper basketDTOMapper;

    @MockBean
    private BasketRepository basketRepository;

    @Autowired
    private BasketService basketService;

    @MockBean
    private PizzaRepository pizzaRepository;

    @MockBean
    private UserRepository userRepository;

    /**
     * Method under test: {@link BasketService#getAllBaskets()}
     */
    @Test
    void getAllBasketsTest() throws ResourceNotFoundException {
        // Arrange
        when(basketRepository.findAll()).thenReturn(new ArrayList<>());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.getAllBaskets());
        verify(basketRepository).findAll();
    }

    /**
     * Method under test: {@link BasketService#getAllBaskets()}
     */
    @Test
    void getAllBasketsTest2() throws ResourceNotFoundException {
        // Arrange
        User user = new User();
        user.setAddress("42 Main St");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole("there are no baskets");
        user.setUsername("janedoe");

        Basket basket = new Basket();
        basket.setId(1L);
        basket.setPizzaList(new ArrayList<>());
        basket.setUser(user);

        ArrayList<Basket> basketList = new ArrayList<>();
        basketList.add(basket);
        when(basketRepository.findAll()).thenReturn(basketList);
        UserDTO userDTO = new UserDTO(1L, "janedoe", "Jane", "Doe", "42 Main St", "Role", "jane.doe@example.org");

        BasketDTO basketDTO = new BasketDTO(1L, userDTO, new ArrayList<>());

        when(basketDTOMapper.apply(Mockito.<Basket>any())).thenReturn(basketDTO);

        // Act
        List<BasketDTO> actualAllBaskets = basketService.getAllBaskets();

        // Assert
        verify(basketDTOMapper).apply(isA(Basket.class));
        verify(basketRepository).findAll();
        assertEquals(1, actualAllBaskets.size());
        assertSame(basketDTO, actualAllBaskets.get(0));
    }

    /**
     * Method under test: {@link BasketService#getAllBaskets()}
     */
    @Test
    void getAllBasketsTest3() throws ResourceNotFoundException {
        // Arrange
        User user = new User();
        user.setAddress("42 Main St");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole("there are no baskets");
        user.setUsername("janedoe");

        Basket basket = new Basket();
        basket.setId(1L);
        basket.setPizzaList(new ArrayList<>());
        basket.setUser(user);

        User user2 = new User();
        user2.setAddress("17 High St");
        user2.setEmail("john.smith@example.org");
        user2.setFirstName("John");
        user2.setId(2L);
        user2.setLastName("Smith");
        user2.setPassword("Password");
        user2.setRole("CUSTOMER");
        user2.setUsername("Username");

        Basket basket2 = new Basket();
        basket2.setId(2L);
        basket2.setPizzaList(new ArrayList<>());
        basket2.setUser(user2);

        ArrayList<Basket> basketList = new ArrayList<>();
        basketList.add(basket2);
        basketList.add(basket);
        when(basketRepository.findAll()).thenReturn(basketList);
        UserDTO userDTO = new UserDTO(1L, "janedoe", "Jane", "Doe", "42 Main St", "Role", "jane.doe@example.org");

        BasketDTO basketDTO = new BasketDTO(1L, userDTO, new ArrayList<>());

        when(basketDTOMapper.apply(Mockito.<Basket>any())).thenReturn(basketDTO);

        // Act
        List<BasketDTO> actualAllBaskets = basketService.getAllBaskets();

        // Assert
        verify(basketDTOMapper, atLeast(1)).apply(Mockito.<Basket>any());
        verify(basketRepository).findAll();
        assertEquals(2, actualAllBaskets.size());
        assertSame(basketDTO, actualAllBaskets.get(0));
    }

    /**
     * Method under test: {@link BasketService#getAllBaskets()}
     */
    @Test
    void getAllBasketsTest4() throws ResourceNotFoundException {
        // Arrange
        User user = new User();
        user.setAddress("42 Main St");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole("there are no baskets");
        user.setUsername("janedoe");

        Basket basket = new Basket();
        basket.setId(1L);
        basket.setPizzaList(new ArrayList<>());
        basket.setUser(user);

        ArrayList<Basket> basketList = new ArrayList<>();
        basketList.add(basket);
        when(basketRepository.findAll()).thenReturn(basketList);
        when(basketDTOMapper.apply(Mockito.<Basket>any())).thenThrow(new ResourceNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.getAllBaskets());
        verify(basketDTOMapper).apply(isA(Basket.class));
        verify(basketRepository).findAll();
    }

    /**
     * Method under test: {@link BasketService#getBasketById(Long)}
     */
    @Test
    void getBasketByIdTest() throws ResourceNotFoundException {
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
        when(basketRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        UserDTO userDTO = new UserDTO(1L, "janedoe", "Jane", "Doe", "42 Main St", "Role", "jane.doe@example.org");

        BasketDTO basketDTO = new BasketDTO(1L, userDTO, new ArrayList<>());

        when(basketDTOMapper.apply(Mockito.<Basket>any())).thenReturn(basketDTO);

        // Act
        BasketDTO actualBasketById = basketService.getBasketById(1L);

        // Assert
        verify(basketDTOMapper).apply(isA(Basket.class));
        verify(basketRepository).findById(isA(Long.class));
        assertSame(basketDTO, actualBasketById);
    }

    /**
     * Method under test: {@link BasketService#getBasketById(Long)}
     */
    @Test
    void getBasketByIdTest2() throws ResourceNotFoundException {
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
        when(basketRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(basketDTOMapper.apply(Mockito.<Basket>any())).thenThrow(new ResourceNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.getBasketById(1L));
        verify(basketDTOMapper).apply(isA(Basket.class));
        verify(basketRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link BasketService#getBasketById(Long)}
     */
    @Test
    void getBasketByIdTest3() throws ResourceNotFoundException {
        // Arrange
        Optional<Basket> emptyResult = Optional.empty();
        when(basketRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.getBasketById(1L));
        verify(basketRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link BasketService#getBasketByUserId(Long)}
     */
    @Test
    void getBasketByUserIdTest() throws ResourceNotFoundException {
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

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");
        Optional<User> ofResult2 = Optional.of(user2);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        UserDTO userDTO = new UserDTO(1L, "janedoe", "Jane", "Doe", "42 Main St", "Role", "jane.doe@example.org");

        BasketDTO basketDTO = new BasketDTO(1L, userDTO, new ArrayList<>());

        when(basketDTOMapper.apply(Mockito.<Basket>any())).thenReturn(basketDTO);

        // Act
        BasketDTO actualBasketByUserId = basketService.getBasketByUserId(1L);

        // Assert
        verify(basketDTOMapper).apply(isA(Basket.class));
        verify(basketRepository).getBasketByUserId(isA(Long.class));
        verify(userRepository).findById(isA(Long.class));
        assertSame(basketDTO, actualBasketByUserId);
    }

    /**
     * Method under test: {@link BasketService#getBasketByUserId(Long)}
     */
    @Test
    void getBasketByUserIdTest2() throws ResourceNotFoundException {
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

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");
        Optional<User> ofResult2 = Optional.of(user2);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        when(basketDTOMapper.apply(Mockito.<Basket>any())).thenThrow(new ResourceNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.getBasketByUserId(1L));
        verify(basketDTOMapper).apply(isA(Basket.class));
        verify(basketRepository).getBasketByUserId(isA(Long.class));
        verify(userRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link BasketService#getBasketByUserId(Long)}
     */
    @Test
    void getBasketByUserIdTest3() throws ResourceNotFoundException {
        // Arrange
        Optional<Basket> emptyResult = Optional.empty();
        when(basketRepository.getBasketByUserId(Mockito.<Long>any())).thenReturn(emptyResult);

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
        assertThrows(ResourceNotFoundException.class, () -> basketService.getBasketByUserId(1L));
        verify(basketRepository).getBasketByUserId(isA(Long.class));
        verify(userRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link BasketService#getBasketByUserId(Long)}
     */
    @Test
    void getBasketByUserIdTest4() throws ResourceNotFoundException {
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
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.getBasketByUserId(1L));
        verify(basketRepository).getBasketByUserId(isA(Long.class));
        verify(userRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link BasketService#getBasketByUsername(String)}
     */
    @Test
    void getBasketByUsernameTest() throws ResourceNotFoundException {
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

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");
        Optional<User> ofResult2 = Optional.of(user2);
        when(userRepository.findUserByUsername(Mockito.<String>any())).thenReturn(ofResult2);
        UserDTO userDTO = new UserDTO(1L, "janedoe", "Jane", "Doe", "42 Main St", "Role", "jane.doe@example.org");

        BasketDTO basketDTO = new BasketDTO(1L, userDTO, new ArrayList<>());

        when(basketDTOMapper.apply(Mockito.<Basket>any())).thenReturn(basketDTO);

        // Act
        BasketDTO actualBasketByUsername = basketService.getBasketByUsername("janedoe");

        // Assert
        verify(basketDTOMapper).apply(isA(Basket.class));
        verify(basketRepository).getBasketByUsername(eq("janedoe"));
        verify(userRepository).findUserByUsername(eq("janedoe"));
        assertSame(basketDTO, actualBasketByUsername);
    }

    /**
     * Method under test: {@link BasketService#getBasketByUsername(String)}
     */
    @Test
    void getBasketByUsernameTest2() throws ResourceNotFoundException {
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

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");
        Optional<User> ofResult2 = Optional.of(user2);
        when(userRepository.findUserByUsername(Mockito.<String>any())).thenReturn(ofResult2);
        when(basketDTOMapper.apply(Mockito.<Basket>any())).thenThrow(new ResourceNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.getBasketByUsername("janedoe"));
        verify(basketDTOMapper).apply(isA(Basket.class));
        verify(basketRepository).getBasketByUsername(eq("janedoe"));
        verify(userRepository).findUserByUsername(eq("janedoe"));
    }

    /**
     * Method under test: {@link BasketService#getBasketByUsername(String)}
     */
    @Test
    void getBasketByUsernameTest3() throws ResourceNotFoundException {
        // Arrange
        Optional<Basket> emptyResult = Optional.empty();
        when(basketRepository.getBasketByUsername(Mockito.<String>any())).thenReturn(emptyResult);

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
        assertThrows(ResourceNotFoundException.class, () -> basketService.getBasketByUsername("janedoe"));
        verify(basketRepository).getBasketByUsername(eq("janedoe"));
        verify(userRepository).findUserByUsername(eq("janedoe"));
    }

    /**
     * Method under test: {@link BasketService#getBasketByUsername(String)}
     */
    @Test
    void getBasketByUsernameTest4() throws ResourceNotFoundException {
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
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findUserByUsername(Mockito.<String>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.getBasketByUsername("janedoe"));
        verify(basketRepository).getBasketByUsername(eq("janedoe"));
        verify(userRepository).findUserByUsername(eq("janedoe"));
    }

    /**
     * Method under test: {@link BasketService#getBasketTotalPriceByUserId(Long)}
     */
    @Test
    void getBasketTotalPriceByUserIdTest() throws ResourceNotFoundException {
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

        // Act
        Double actualBasketTotalPriceByUserId = basketService.getBasketTotalPriceByUserId(1L);

        // Assert
        verify(basketRepository).getBasketByUserId(isA(Long.class));
        verify(basketRepository).getBasketTotalPriceByUserId(isA(Long.class));
        verify(userRepository).findById(isA(Long.class));
        assertEquals(10.0d, actualBasketTotalPriceByUserId.doubleValue());
    }

    /**
     * Method under test: {@link BasketService#getBasketTotalPriceByUserId(Long)}
     */
    @Test
    void getBasketTotalPriceByUserIdTest2() throws ResourceNotFoundException {
        // Arrange
        when(userRepository.findById(Mockito.<Long>any())).thenThrow(new ResourceNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.getBasketTotalPriceByUserId(1L));
        verify(userRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link BasketService#getBasketTotalPriceByUserId(Long)}
     */
    @Test
    void getBasketTotalPriceByUserIdTest3() throws ResourceNotFoundException {
        // Arrange
        Optional<Basket> emptyResult = Optional.empty();
        when(basketRepository.getBasketByUserId(Mockito.<Long>any())).thenReturn(emptyResult);
        Optional<Double> ofResult = Optional.<Double>of(10.0d);
        when(basketRepository.getBasketTotalPriceByUserId(Mockito.<Long>any())).thenReturn(ofResult);

        User user = new User();
        user.setAddress("42 Main St");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUsername("janedoe");
        Optional<User> ofResult2 = Optional.of(user);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.getBasketTotalPriceByUserId(1L));
        verify(basketRepository).getBasketByUserId(isA(Long.class));
        verify(basketRepository).getBasketTotalPriceByUserId(isA(Long.class));
        verify(userRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link BasketService#getBasketTotalPriceByUserId(Long)}
     */
    @Test
    void getBasketTotalPriceByUserIdTest4() throws ResourceNotFoundException {
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
        Optional<Double> emptyResult = Optional.empty();
        when(basketRepository.getBasketTotalPriceByUserId(Mockito.<Long>any())).thenReturn(emptyResult);

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");
        Optional<User> ofResult2 = Optional.of(user2);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.getBasketTotalPriceByUserId(1L));
        verify(basketRepository).getBasketByUserId(isA(Long.class));
        verify(basketRepository).getBasketTotalPriceByUserId(isA(Long.class));
        verify(userRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link BasketService#getBasketTotalPriceByUserId(Long)}
     */
    @Test
    void getBasketTotalPriceByUserIdTest5() throws ResourceNotFoundException {
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
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.getBasketTotalPriceByUserId(1L));
        verify(basketRepository).getBasketByUserId(isA(Long.class));
        verify(basketRepository).getBasketTotalPriceByUserId(isA(Long.class));
        verify(userRepository).findById(isA(Long.class));
    }

    /**
     * Method under test:
     * {@link BasketService#getBasketTotalPriceByUsername(String)}
     */
    @Test
    void getBasketTotalPriceByUsernameTest() throws ResourceNotFoundException {
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

        // Act
        Double actualBasketTotalPriceByUsername = basketService.getBasketTotalPriceByUsername("janedoe");

        // Assert
        verify(basketRepository).getBasketByUsername(eq("janedoe"));
        verify(basketRepository).getBasketTotalPriceByUsername(eq("janedoe"));
        verify(userRepository).findUserByUsername(eq("janedoe"));
        assertEquals(10.0d, actualBasketTotalPriceByUsername.doubleValue());
    }

    /**
     * Method under test:
     * {@link BasketService#getBasketTotalPriceByUsername(String)}
     */
    @Test
    void getBasketTotalPriceByUsernameTest2() throws ResourceNotFoundException {
        // Arrange
        when(userRepository.findUserByUsername(Mockito.<String>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.getBasketTotalPriceByUsername("janedoe"));
        verify(userRepository).findUserByUsername(eq("janedoe"));
    }

    /**
     * Method under test:
     * {@link BasketService#getBasketTotalPriceByUsername(String)}
     */
    @Test
    void getBasketTotalPriceByUsernameTest3() throws ResourceNotFoundException {
        // Arrange
        Optional<Basket> emptyResult = Optional.empty();
        when(basketRepository.getBasketByUsername(Mockito.<String>any())).thenReturn(emptyResult);
        Optional<Double> ofResult = Optional.<Double>of(10.0d);
        when(basketRepository.getBasketTotalPriceByUsername(Mockito.<String>any())).thenReturn(ofResult);

        User user = new User();
        user.setAddress("42 Main St");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUsername("janedoe");
        Optional<User> ofResult2 = Optional.of(user);
        when(userRepository.findUserByUsername(Mockito.<String>any())).thenReturn(ofResult2);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.getBasketTotalPriceByUsername("janedoe"));
        verify(basketRepository).getBasketByUsername(eq("janedoe"));
        verify(basketRepository).getBasketTotalPriceByUsername(eq("janedoe"));
        verify(userRepository).findUserByUsername(eq("janedoe"));
    }

    /**
     * Method under test:
     * {@link BasketService#getBasketTotalPriceByUsername(String)}
     */
    @Test
    void getBasketTotalPriceByUsernameTest4() throws ResourceNotFoundException {
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
        Optional<Double> emptyResult = Optional.empty();
        when(basketRepository.getBasketTotalPriceByUsername(Mockito.<String>any())).thenReturn(emptyResult);

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");
        Optional<User> ofResult2 = Optional.of(user2);
        when(userRepository.findUserByUsername(Mockito.<String>any())).thenReturn(ofResult2);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.getBasketTotalPriceByUsername("janedoe"));
        verify(basketRepository).getBasketByUsername(eq("janedoe"));
        verify(basketRepository).getBasketTotalPriceByUsername(eq("janedoe"));
        verify(userRepository).findUserByUsername(eq("janedoe"));
    }

    /**
     * Method under test:
     * {@link BasketService#getBasketTotalPriceByUsername(String)}
     */
    @Test
    void getBasketTotalPriceByUsernameTest5() throws ResourceNotFoundException {
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
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findUserByUsername(Mockito.<String>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.getBasketTotalPriceByUsername("janedoe"));
        verify(basketRepository).getBasketByUsername(eq("janedoe"));
        verify(basketRepository).getBasketTotalPriceByUsername(eq("janedoe"));
        verify(userRepository).findUserByUsername(eq("janedoe"));
    }

    /**
     * Method under test: {@link BasketService#addBasket(Basket)}
     */
    @Test
    void addBasketTest() throws ResourceAlreadyExistsException, ResourceNotFoundException {
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

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");
        Optional<User> ofResult2 = Optional.of(user2);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

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

        // Act and Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> basketService.addBasket(basket2));
        verify(basketRepository).getBasketByUserId(isA(Long.class));
        verify(userRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link BasketService#addBasket(Basket)}
     */
    @Test
    void addBasketTest2() throws ResourceAlreadyExistsException, ResourceNotFoundException {
        // Arrange
        when(userRepository.findById(Mockito.<Long>any())).thenThrow(new ResourceNotFoundException("An error occurred"));

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

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.addBasket(basket));
        verify(userRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link BasketService#addBasket(Basket)}
     */
    @Test
    void addBasketTest3() throws ResourceAlreadyExistsException, ResourceNotFoundException {
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
        when(basketRepository.save(Mockito.<Basket>any())).thenReturn(basket);
        Optional<Basket> emptyResult = Optional.empty();
        when(basketRepository.getBasketByUserId(Mockito.<Long>any())).thenReturn(emptyResult);

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

        BasketDTO basketDTO = new BasketDTO(1L, userDTO, new ArrayList<>());

        when(basketDTOMapper.apply(Mockito.<Basket>any())).thenReturn(basketDTO);

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

        // Act
        BasketDTO actualAddBasketResult = basketService.addBasket(basket2);

        // Assert
        verify(basketDTOMapper).apply(isA(Basket.class));
        verify(basketRepository).getBasketByUserId(isA(Long.class));
        verify(userRepository).findById(isA(Long.class));
        verify(basketRepository).save(isA(Basket.class));
        assertSame(basketDTO, actualAddBasketResult);
    }

    /**
     * Method under test: {@link BasketService#addBasket(Basket)}
     */
    @Test
    void addBasketTest4() throws ResourceAlreadyExistsException, ResourceNotFoundException {
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
        when(basketRepository.save(Mockito.<Basket>any())).thenReturn(basket);
        Optional<Basket> emptyResult = Optional.empty();
        when(basketRepository.getBasketByUserId(Mockito.<Long>any())).thenReturn(emptyResult);

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
        when(basketDTOMapper.apply(Mockito.<Basket>any())).thenThrow(new ResourceNotFoundException("An error occurred"));

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

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.addBasket(basket2));
        verify(basketDTOMapper).apply(isA(Basket.class));
        verify(basketRepository).getBasketByUserId(isA(Long.class));
        verify(userRepository).findById(isA(Long.class));
        verify(basketRepository).save(isA(Basket.class));
    }

    /**
     * Method under test: {@link BasketService#addBasket(Basket)}
     */
    @Test
    void addBasketTest5() throws ResourceAlreadyExistsException, ResourceNotFoundException {
        // Arrange
        Optional<Basket> emptyResult = Optional.empty();
        when(basketRepository.getBasketByUserId(Mockito.<Long>any())).thenReturn(emptyResult);
        Optional<User> emptyResult2 = Optional.empty();
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult2);

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

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.addBasket(basket));
        verify(basketRepository).getBasketByUserId(isA(Long.class));
        verify(userRepository).findById(isA(Long.class));
    }

    /**
     * Method under test:
     * {@link BasketService#addPizzaToBasketByBasketIdAndPizzaId(Long, Long)}
     */
    @Test
    void addPizzaToBasketByBasketIdAndPizzaIdTest() throws ResourceNotFoundException {
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

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");

        Basket basket2 = new Basket();
        basket2.setId(1L);
        basket2.setPizzaList(new ArrayList<>());
        basket2.setUser(user2);
        when(basketRepository.save(Mockito.<Basket>any())).thenReturn(basket2);
        when(basketRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);
        Optional<Pizza> ofResult2 = Optional.of(pizza);
        when(pizzaRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        UserDTO userDTO = new UserDTO(1L, "janedoe", "Jane", "Doe", "42 Main St", "Role", "jane.doe@example.org");

        BasketDTO basketDTO = new BasketDTO(1L, userDTO, new ArrayList<>());

        when(basketDTOMapper.apply(Mockito.<Basket>any())).thenReturn(basketDTO);

        // Act
        BasketDTO actualAddPizzaToBasketByBasketIdAndPizzaIdResult = basketService.addPizzaToBasketByBasketIdAndPizzaId(1L,
                1L);

        // Assert
        verify(basketDTOMapper).apply(isA(Basket.class));
        verify(basketRepository).findById(isA(Long.class));
        verify(pizzaRepository).findById(isA(Long.class));
        verify(basketRepository).save(isA(Basket.class));
        assertSame(basketDTO, actualAddPizzaToBasketByBasketIdAndPizzaIdResult);
    }

    /**
     * Method under test:
     * {@link BasketService#addPizzaToBasketByBasketIdAndPizzaId(Long, Long)}
     */
    @Test
    void addPizzaToBasketByBasketIdAndPizzaIdTest2() throws ResourceNotFoundException {
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

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");

        Basket basket2 = new Basket();
        basket2.setId(1L);
        basket2.setPizzaList(new ArrayList<>());
        basket2.setUser(user2);
        when(basketRepository.save(Mockito.<Basket>any())).thenReturn(basket2);
        when(basketRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);
        Optional<Pizza> ofResult2 = Optional.of(pizza);
        when(pizzaRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        when(basketDTOMapper.apply(Mockito.<Basket>any())).thenThrow(new ResourceNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.addPizzaToBasketByBasketIdAndPizzaId(1L, 1L));
        verify(basketDTOMapper).apply(isA(Basket.class));
        verify(basketRepository).findById(isA(Long.class));
        verify(pizzaRepository).findById(isA(Long.class));
        verify(basketRepository).save(isA(Basket.class));
    }

    /**
     * Method under test:
     * {@link BasketService#addPizzaToBasketByBasketIdAndPizzaId(Long, Long)}
     */
    @Test
    void addPizzaToBasketByBasketIdAndPizzaIdTest3() throws ResourceNotFoundException {
        // Arrange
        Optional<Basket> emptyResult = Optional.empty();
        when(basketRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);
        Optional<Pizza> ofResult = Optional.of(pizza);
        when(pizzaRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.addPizzaToBasketByBasketIdAndPizzaId(1L, 1L));
        verify(basketRepository).findById(isA(Long.class));
        verify(pizzaRepository).findById(isA(Long.class));
    }

    /**
     * Method under test:
     * {@link BasketService#addPizzaToBasketByBasketIdAndPizzaId(Long, Long)}
     */
    @Test
    void addPizzaToBasketByBasketIdAndPizzaIdTest4() throws ResourceNotFoundException {
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
        when(basketRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        Optional<Pizza> emptyResult = Optional.empty();
        when(pizzaRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.addPizzaToBasketByBasketIdAndPizzaId(1L, 1L));
        verify(basketRepository).findById(isA(Long.class));
        verify(pizzaRepository).findById(isA(Long.class));
    }

    /**
     * Method under test:
     * {@link BasketService#addPizzaToBasketByUserIdAndPizzaId(Long, Long)}
     */
    @Test
    void addPizzaToBasketByUserIdAndPizzaIdTest() throws ResourceNotFoundException {
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

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");

        Basket basket2 = new Basket();
        basket2.setId(1L);
        basket2.setPizzaList(new ArrayList<>());
        basket2.setUser(user2);
        when(basketRepository.save(Mockito.<Basket>any())).thenReturn(basket2);
        when(basketRepository.getBasketByUserId(Mockito.<Long>any())).thenReturn(ofResult);

        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);
        Optional<Pizza> ofResult2 = Optional.of(pizza);
        when(pizzaRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        User user3 = new User();
        user3.setAddress("42 Main St");
        user3.setEmail("jane.doe@example.org");
        user3.setFirstName("Jane");
        user3.setId(1L);
        user3.setLastName("Doe");
        user3.setPassword("iloveyou");
        user3.setRole("Role");
        user3.setUsername("janedoe");
        Optional<User> ofResult3 = Optional.of(user3);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult3);
        UserDTO userDTO = new UserDTO(1L, "janedoe", "Jane", "Doe", "42 Main St", "Role", "jane.doe@example.org");

        BasketDTO basketDTO = new BasketDTO(1L, userDTO, new ArrayList<>());

        when(basketDTOMapper.apply(Mockito.<Basket>any())).thenReturn(basketDTO);

        // Act
        BasketDTO actualAddPizzaToBasketByUserIdAndPizzaIdResult = basketService.addPizzaToBasketByUserIdAndPizzaId(1L, 1L);

        // Assert
        verify(basketDTOMapper).apply(isA(Basket.class));
        verify(basketRepository).getBasketByUserId(isA(Long.class));
        verify(pizzaRepository).findById(isA(Long.class));
        verify(userRepository).findById(isA(Long.class));
        verify(basketRepository).save(isA(Basket.class));
        assertSame(basketDTO, actualAddPizzaToBasketByUserIdAndPizzaIdResult);
    }

    /**
     * Method under test:
     * {@link BasketService#addPizzaToBasketByUserIdAndPizzaId(Long, Long)}
     */
    @Test
    void addPizzaToBasketByUserIdAndPizzaIdTest2() throws ResourceNotFoundException {
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

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");

        Basket basket2 = new Basket();
        basket2.setId(1L);
        basket2.setPizzaList(new ArrayList<>());
        basket2.setUser(user2);
        when(basketRepository.save(Mockito.<Basket>any())).thenReturn(basket2);
        when(basketRepository.getBasketByUserId(Mockito.<Long>any())).thenReturn(ofResult);

        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);
        Optional<Pizza> ofResult2 = Optional.of(pizza);
        when(pizzaRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        User user3 = new User();
        user3.setAddress("42 Main St");
        user3.setEmail("jane.doe@example.org");
        user3.setFirstName("Jane");
        user3.setId(1L);
        user3.setLastName("Doe");
        user3.setPassword("iloveyou");
        user3.setRole("Role");
        user3.setUsername("janedoe");
        Optional<User> ofResult3 = Optional.of(user3);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult3);
        when(basketDTOMapper.apply(Mockito.<Basket>any())).thenThrow(new ResourceNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.addPizzaToBasketByUserIdAndPizzaId(1L, 1L));
        verify(basketDTOMapper).apply(isA(Basket.class));
        verify(basketRepository).getBasketByUserId(isA(Long.class));
        verify(pizzaRepository).findById(isA(Long.class));
        verify(userRepository).findById(isA(Long.class));
        verify(basketRepository).save(isA(Basket.class));
    }

    /**
     * Method under test:
     * {@link BasketService#addPizzaToBasketByUserIdAndPizzaId(Long, Long)}
     */
    @Test
    void addPizzaToBasketByUserIdAndPizzaIdTest3() throws ResourceNotFoundException {
        // Arrange
        Optional<Basket> emptyResult = Optional.empty();
        when(basketRepository.getBasketByUserId(Mockito.<Long>any())).thenReturn(emptyResult);

        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);
        Optional<Pizza> ofResult = Optional.of(pizza);
        when(pizzaRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        User user = new User();
        user.setAddress("42 Main St");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUsername("janedoe");
        Optional<User> ofResult2 = Optional.of(user);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.addPizzaToBasketByUserIdAndPizzaId(1L, 1L));
        verify(basketRepository).getBasketByUserId(isA(Long.class));
        verify(pizzaRepository).findById(isA(Long.class));
        verify(userRepository).findById(isA(Long.class));
    }

    /**
     * Method under test:
     * {@link BasketService#addPizzaToBasketByUserIdAndPizzaId(Long, Long)}
     */
    @Test
    void addPizzaToBasketByUserIdAndPizzaIdTest4() throws ResourceNotFoundException {
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
        Optional<Pizza> emptyResult = Optional.empty();
        when(pizzaRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");
        Optional<User> ofResult2 = Optional.of(user2);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.addPizzaToBasketByUserIdAndPizzaId(1L, 1L));
        verify(basketRepository).getBasketByUserId(isA(Long.class));
        verify(pizzaRepository).findById(isA(Long.class));
        verify(userRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link BasketService#updateBasketById(Long, Basket)}
     */
    @Test
    void updateBasketByIdTest() throws ResourceNotFoundException {
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

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");

        Basket basket2 = new Basket();
        basket2.setId(1L);
        basket2.setPizzaList(new ArrayList<>());
        basket2.setUser(user2);
        when(basketRepository.save(Mockito.<Basket>any())).thenReturn(basket2);
        when(basketRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        UserDTO userDTO = new UserDTO(1L, "janedoe", "Jane", "Doe", "42 Main St", "Role", "jane.doe@example.org");

        BasketDTO basketDTO = new BasketDTO(1L, userDTO, new ArrayList<>());

        when(basketDTOMapper.apply(Mockito.<Basket>any())).thenReturn(basketDTO);

        User user3 = new User();
        user3.setAddress("42 Main St");
        user3.setEmail("jane.doe@example.org");
        user3.setFirstName("Jane");
        user3.setId(1L);
        user3.setLastName("Doe");
        user3.setPassword("iloveyou");
        user3.setRole("Role");
        user3.setUsername("janedoe");

        Basket newBasket = new Basket();
        newBasket.setId(1L);
        newBasket.setPizzaList(new ArrayList<>());
        newBasket.setUser(user3);

        // Act
        BasketDTO actualUpdateBasketByIdResult = basketService.updateBasketById(1L, newBasket);

        // Assert
        verify(basketDTOMapper).apply(isA(Basket.class));
        verify(basketRepository).findById(isA(Long.class));
        verify(basketRepository).save(isA(Basket.class));
        assertSame(basketDTO, actualUpdateBasketByIdResult);
    }

    /**
     * Method under test: {@link BasketService#updateBasketById(Long, Basket)}
     */
    @Test
    void updateBasketByIdTest2() throws ResourceNotFoundException {
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

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");

        Basket basket2 = new Basket();
        basket2.setId(1L);
        basket2.setPizzaList(new ArrayList<>());
        basket2.setUser(user2);
        when(basketRepository.save(Mockito.<Basket>any())).thenReturn(basket2);
        when(basketRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(basketDTOMapper.apply(Mockito.<Basket>any())).thenThrow(new ResourceNotFoundException("An error occurred"));

        User user3 = new User();
        user3.setAddress("42 Main St");
        user3.setEmail("jane.doe@example.org");
        user3.setFirstName("Jane");
        user3.setId(1L);
        user3.setLastName("Doe");
        user3.setPassword("iloveyou");
        user3.setRole("Role");
        user3.setUsername("janedoe");

        Basket newBasket = new Basket();
        newBasket.setId(1L);
        newBasket.setPizzaList(new ArrayList<>());
        newBasket.setUser(user3);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.updateBasketById(1L, newBasket));
        verify(basketDTOMapper).apply(isA(Basket.class));
        verify(basketRepository).findById(isA(Long.class));
        verify(basketRepository).save(isA(Basket.class));
    }

    /**
     * Method under test: {@link BasketService#updateBasketById(Long, Basket)}
     */
    @Test
    void updateBasketByIdTest3() throws ResourceNotFoundException {
        // Arrange
        Optional<Basket> emptyResult = Optional.empty();
        when(basketRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        User user = new User();
        user.setAddress("42 Main St");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUsername("janedoe");

        Basket newBasket = new Basket();
        newBasket.setId(1L);
        newBasket.setPizzaList(new ArrayList<>());
        newBasket.setUser(user);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.updateBasketById(1L, newBasket));
        verify(basketRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link BasketService#updateBasketByUserId(Long, Basket)}
     */
    @Test
    void updateBasketByUserIdTest() throws ResourceNotFoundException {
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

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");

        Basket basket2 = new Basket();
        basket2.setId(1L);
        basket2.setPizzaList(new ArrayList<>());
        basket2.setUser(user2);
        when(basketRepository.save(Mockito.<Basket>any())).thenReturn(basket2);
        when(basketRepository.getBasketByUserId(Mockito.<Long>any())).thenReturn(ofResult);

        User user3 = new User();
        user3.setAddress("42 Main St");
        user3.setEmail("jane.doe@example.org");
        user3.setFirstName("Jane");
        user3.setId(1L);
        user3.setLastName("Doe");
        user3.setPassword("iloveyou");
        user3.setRole("Role");
        user3.setUsername("janedoe");
        Optional<User> ofResult2 = Optional.of(user3);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        UserDTO userDTO = new UserDTO(1L, "janedoe", "Jane", "Doe", "42 Main St", "Role", "jane.doe@example.org");

        BasketDTO basketDTO = new BasketDTO(1L, userDTO, new ArrayList<>());

        when(basketDTOMapper.apply(Mockito.<Basket>any())).thenReturn(basketDTO);

        User user4 = new User();
        user4.setAddress("42 Main St");
        user4.setEmail("jane.doe@example.org");
        user4.setFirstName("Jane");
        user4.setId(1L);
        user4.setLastName("Doe");
        user4.setPassword("iloveyou");
        user4.setRole("Role");
        user4.setUsername("janedoe");

        Basket newBasket = new Basket();
        newBasket.setId(1L);
        newBasket.setPizzaList(new ArrayList<>());
        newBasket.setUser(user4);

        // Act
        BasketDTO actualUpdateBasketByUserIdResult = basketService.updateBasketByUserId(1L, newBasket);

        // Assert
        verify(basketDTOMapper).apply(isA(Basket.class));
        verify(basketRepository).getBasketByUserId(isA(Long.class));
        verify(userRepository).findById(isA(Long.class));
        verify(basketRepository).save(isA(Basket.class));
        assertSame(basketDTO, actualUpdateBasketByUserIdResult);
    }

    /**
     * Method under test: {@link BasketService#updateBasketByUserId(Long, Basket)}
     */
    @Test
    void updateBasketByUserIdTest2() throws ResourceNotFoundException {
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

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");

        Basket basket2 = new Basket();
        basket2.setId(1L);
        basket2.setPizzaList(new ArrayList<>());
        basket2.setUser(user2);
        when(basketRepository.save(Mockito.<Basket>any())).thenReturn(basket2);
        when(basketRepository.getBasketByUserId(Mockito.<Long>any())).thenReturn(ofResult);

        User user3 = new User();
        user3.setAddress("42 Main St");
        user3.setEmail("jane.doe@example.org");
        user3.setFirstName("Jane");
        user3.setId(1L);
        user3.setLastName("Doe");
        user3.setPassword("iloveyou");
        user3.setRole("Role");
        user3.setUsername("janedoe");
        Optional<User> ofResult2 = Optional.of(user3);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        when(basketDTOMapper.apply(Mockito.<Basket>any())).thenThrow(new ResourceNotFoundException("An error occurred"));

        User user4 = new User();
        user4.setAddress("42 Main St");
        user4.setEmail("jane.doe@example.org");
        user4.setFirstName("Jane");
        user4.setId(1L);
        user4.setLastName("Doe");
        user4.setPassword("iloveyou");
        user4.setRole("Role");
        user4.setUsername("janedoe");

        Basket newBasket = new Basket();
        newBasket.setId(1L);
        newBasket.setPizzaList(new ArrayList<>());
        newBasket.setUser(user4);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.updateBasketByUserId(1L, newBasket));
        verify(basketDTOMapper).apply(isA(Basket.class));
        verify(basketRepository).getBasketByUserId(isA(Long.class));
        verify(userRepository).findById(isA(Long.class));
        verify(basketRepository).save(isA(Basket.class));
    }

    /**
     * Method under test: {@link BasketService#updateBasketByUserId(Long, Basket)}
     */
    @Test
    void updateBasketByUserIdTest3() throws ResourceNotFoundException {
        // Arrange
        Optional<Basket> emptyResult = Optional.empty();
        when(basketRepository.getBasketByUserId(Mockito.<Long>any())).thenReturn(emptyResult);

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

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");

        Basket newBasket = new Basket();
        newBasket.setId(1L);
        newBasket.setPizzaList(new ArrayList<>());
        newBasket.setUser(user2);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.updateBasketByUserId(1L, newBasket));
        verify(basketRepository).getBasketByUserId(isA(Long.class));
        verify(userRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link BasketService#updateBasketByUserId(Long, Basket)}
     */
    @Test
    void updateBasketByUserIdTest4() throws ResourceNotFoundException {
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
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");

        Basket newBasket = new Basket();
        newBasket.setId(1L);
        newBasket.setPizzaList(new ArrayList<>());
        newBasket.setUser(user2);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.updateBasketByUserId(1L, newBasket));
        verify(basketRepository).getBasketByUserId(isA(Long.class));
        verify(userRepository).findById(isA(Long.class));
    }

    /**
     * Method under test:
     * {@link BasketService#updateBasketByUsername(String, Basket)}
     */
    @Test
    void updateBasketByUsernameTest() throws ResourceNotFoundException {
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

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");

        Basket basket2 = new Basket();
        basket2.setId(1L);
        basket2.setPizzaList(new ArrayList<>());
        basket2.setUser(user2);
        when(basketRepository.save(Mockito.<Basket>any())).thenReturn(basket2);
        when(basketRepository.getBasketByUsername(Mockito.<String>any())).thenReturn(ofResult);

        User user3 = new User();
        user3.setAddress("42 Main St");
        user3.setEmail("jane.doe@example.org");
        user3.setFirstName("Jane");
        user3.setId(1L);
        user3.setLastName("Doe");
        user3.setPassword("iloveyou");
        user3.setRole("Role");
        user3.setUsername("janedoe");
        Optional<User> ofResult2 = Optional.of(user3);
        when(userRepository.findUserByUsername(Mockito.<String>any())).thenReturn(ofResult2);
        UserDTO userDTO = new UserDTO(1L, "janedoe", "Jane", "Doe", "42 Main St", "Role", "jane.doe@example.org");

        BasketDTO basketDTO = new BasketDTO(1L, userDTO, new ArrayList<>());

        when(basketDTOMapper.apply(Mockito.<Basket>any())).thenReturn(basketDTO);

        User user4 = new User();
        user4.setAddress("42 Main St");
        user4.setEmail("jane.doe@example.org");
        user4.setFirstName("Jane");
        user4.setId(1L);
        user4.setLastName("Doe");
        user4.setPassword("iloveyou");
        user4.setRole("Role");
        user4.setUsername("janedoe");

        Basket newBasket = new Basket();
        newBasket.setId(1L);
        newBasket.setPizzaList(new ArrayList<>());
        newBasket.setUser(user4);

        // Act
        BasketDTO actualUpdateBasketByUsernameResult = basketService.updateBasketByUsername("janedoe", newBasket);

        // Assert
        verify(basketDTOMapper).apply(isA(Basket.class));
        verify(basketRepository).getBasketByUsername(eq("janedoe"));
        verify(userRepository).findUserByUsername(eq("janedoe"));
        verify(basketRepository).save(isA(Basket.class));
        assertSame(basketDTO, actualUpdateBasketByUsernameResult);
    }

    /**
     * Method under test:
     * {@link BasketService#updateBasketByUsername(String, Basket)}
     */
    @Test
    void updateBasketByUsernameTest2() throws ResourceNotFoundException {
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

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");

        Basket basket2 = new Basket();
        basket2.setId(1L);
        basket2.setPizzaList(new ArrayList<>());
        basket2.setUser(user2);
        when(basketRepository.save(Mockito.<Basket>any())).thenReturn(basket2);
        when(basketRepository.getBasketByUsername(Mockito.<String>any())).thenReturn(ofResult);

        User user3 = new User();
        user3.setAddress("42 Main St");
        user3.setEmail("jane.doe@example.org");
        user3.setFirstName("Jane");
        user3.setId(1L);
        user3.setLastName("Doe");
        user3.setPassword("iloveyou");
        user3.setRole("Role");
        user3.setUsername("janedoe");
        Optional<User> ofResult2 = Optional.of(user3);
        when(userRepository.findUserByUsername(Mockito.<String>any())).thenReturn(ofResult2);
        when(basketDTOMapper.apply(Mockito.<Basket>any())).thenThrow(new ResourceNotFoundException("An error occurred"));

        User user4 = new User();
        user4.setAddress("42 Main St");
        user4.setEmail("jane.doe@example.org");
        user4.setFirstName("Jane");
        user4.setId(1L);
        user4.setLastName("Doe");
        user4.setPassword("iloveyou");
        user4.setRole("Role");
        user4.setUsername("janedoe");

        Basket newBasket = new Basket();
        newBasket.setId(1L);
        newBasket.setPizzaList(new ArrayList<>());
        newBasket.setUser(user4);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.updateBasketByUsername("janedoe", newBasket));
        verify(basketDTOMapper).apply(isA(Basket.class));
        verify(basketRepository).getBasketByUsername(eq("janedoe"));
        verify(userRepository).findUserByUsername(eq("janedoe"));
        verify(basketRepository).save(isA(Basket.class));
    }

    /**
     * Method under test:
     * {@link BasketService#updateBasketByUsername(String, Basket)}
     */
    @Test
    void updateBasketByUsernameTest3() throws ResourceNotFoundException {
        // Arrange
        Optional<Basket> emptyResult = Optional.empty();
        when(basketRepository.getBasketByUsername(Mockito.<String>any())).thenReturn(emptyResult);

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

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");

        Basket newBasket = new Basket();
        newBasket.setId(1L);
        newBasket.setPizzaList(new ArrayList<>());
        newBasket.setUser(user2);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.updateBasketByUsername("janedoe", newBasket));
        verify(basketRepository).getBasketByUsername(eq("janedoe"));
        verify(userRepository).findUserByUsername(eq("janedoe"));
    }

    /**
     * Method under test:
     * {@link BasketService#updateBasketByUsername(String, Basket)}
     */
    @Test
    void updateBasketByUsernameTest4() throws ResourceNotFoundException {
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
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findUserByUsername(Mockito.<String>any())).thenReturn(emptyResult);

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");

        Basket newBasket = new Basket();
        newBasket.setId(1L);
        newBasket.setPizzaList(new ArrayList<>());
        newBasket.setUser(user2);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.updateBasketByUsername("janedoe", newBasket));
        verify(basketRepository).getBasketByUsername(eq("janedoe"));
        verify(userRepository).findUserByUsername(eq("janedoe"));
    }

    /**
     * Method under test:
     * {@link BasketService#deletePizzaFromBasketByBasketIdAndPizzaId(Long, Long)}
     */
    @Test
    void deletePizzaFromBasketByBasketIdAndPizzaIdTest() throws ResourceNotFoundException {
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

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");

        Basket basket2 = new Basket();
        basket2.setId(1L);
        basket2.setPizzaList(new ArrayList<>());
        basket2.setUser(user2);
        when(basketRepository.save(Mockito.<Basket>any())).thenReturn(basket2);
        when(basketRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);
        Optional<Pizza> ofResult2 = Optional.of(pizza);
        when(pizzaRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        UserDTO userDTO = new UserDTO(1L, "janedoe", "Jane", "Doe", "42 Main St", "Role", "jane.doe@example.org");

        BasketDTO basketDTO = new BasketDTO(1L, userDTO, new ArrayList<>());

        when(basketDTOMapper.apply(Mockito.<Basket>any())).thenReturn(basketDTO);

        // Act
        BasketDTO actualDeletePizzaFromBasketByBasketIdAndPizzaIdResult = basketService
                .deletePizzaFromBasketByBasketIdAndPizzaId(1L, 1L);

        // Assert
        verify(basketDTOMapper).apply(isA(Basket.class));
        verify(basketRepository).findById(isA(Long.class));
        verify(pizzaRepository).findById(isA(Long.class));
        verify(basketRepository).save(isA(Basket.class));
        assertSame(basketDTO, actualDeletePizzaFromBasketByBasketIdAndPizzaIdResult);
    }

    /**
     * Method under test:
     * {@link BasketService#deletePizzaFromBasketByBasketIdAndPizzaId(Long, Long)}
     */
    @Test
    void deletePizzaFromBasketByBasketIdAndPizzaIdTest2() throws ResourceNotFoundException {
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

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");

        Basket basket2 = new Basket();
        basket2.setId(1L);
        basket2.setPizzaList(new ArrayList<>());
        basket2.setUser(user2);
        when(basketRepository.save(Mockito.<Basket>any())).thenReturn(basket2);
        when(basketRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);
        Optional<Pizza> ofResult2 = Optional.of(pizza);
        when(pizzaRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        when(basketDTOMapper.apply(Mockito.<Basket>any())).thenThrow(new ResourceNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class,
                () -> basketService.deletePizzaFromBasketByBasketIdAndPizzaId(1L, 1L));
        verify(basketDTOMapper).apply(isA(Basket.class));
        verify(basketRepository).findById(isA(Long.class));
        verify(pizzaRepository).findById(isA(Long.class));
        verify(basketRepository).save(isA(Basket.class));
    }

    /**
     * Method under test:
     * {@link BasketService#deletePizzaFromBasketByBasketIdAndPizzaId(Long, Long)}
     */
    @Test
    void deletePizzaFromBasketByBasketIdAndPizzaIdTest3() throws ResourceNotFoundException {
        // Arrange
        Optional<Basket> emptyResult = Optional.empty();
        when(basketRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);
        Optional<Pizza> ofResult = Optional.of(pizza);
        when(pizzaRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class,
                () -> basketService.deletePizzaFromBasketByBasketIdAndPizzaId(1L, 1L));
        verify(basketRepository).findById(isA(Long.class));
        verify(pizzaRepository).findById(isA(Long.class));
    }

    /**
     * Method under test:
     * {@link BasketService#deletePizzaFromBasketByBasketIdAndPizzaId(Long, Long)}
     */
    @Test
    void deletePizzaFromBasketByBasketIdAndPizzaIdTest4() throws ResourceNotFoundException {
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
        when(basketRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        Optional<Pizza> emptyResult = Optional.empty();
        when(pizzaRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class,
                () -> basketService.deletePizzaFromBasketByBasketIdAndPizzaId(1L, 1L));
        verify(basketRepository).findById(isA(Long.class));
        verify(pizzaRepository).findById(isA(Long.class));
    }

    /**
     * Method under test:
     * {@link BasketService#deletePizzaFromBasketByUserIdAndPizzaId(Long, Long)}
     */
    @Test
    void deletePizzaFromBasketByUserIdAndPizzaIdTest() throws ResourceNotFoundException {
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

        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);
        Optional<Pizza> ofResult2 = Optional.of(pizza);
        when(pizzaRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

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
        assertThrows(ResourceNotFoundException.class, () -> basketService.deletePizzaFromBasketByUserIdAndPizzaId(1L, 1L));
        verify(basketRepository).getBasketByUserId(isA(Long.class));
        verify(pizzaRepository).findById(isA(Long.class));
        verify(userRepository).findById(isA(Long.class));
    }

    /**
     * Method under test:
     * {@link BasketService#deletePizzaFromBasketByUserIdAndPizzaId(Long, Long)}
     */
    @Test
    void deletePizzaFromBasketByUserIdAndPizzaIdTest2() throws ResourceNotFoundException {
        // Arrange
        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);
        Optional<Pizza> ofResult = Optional.of(pizza);
        when(pizzaRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(userRepository.findById(Mockito.<Long>any())).thenThrow(new ResourceNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.deletePizzaFromBasketByUserIdAndPizzaId(1L, 1L));
        verify(userRepository).findById(isA(Long.class));
    }

    /**
     * Method under test:
     * {@link BasketService#deletePizzaFromBasketByUserIdAndPizzaId(Long, Long)}
     */
    @Test
    void deletePizzaFromBasketByUserIdAndPizzaIdTest3() throws ResourceNotFoundException {
        // Arrange
        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);

        ArrayList<Pizza> pizzaList = new ArrayList<>();
        pizzaList.add(pizza);

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
        basket.setPizzaList(pizzaList);
        basket.setUser(user);
        Optional<Basket> ofResult = Optional.of(basket);

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");

        Basket basket2 = new Basket();
        basket2.setId(1L);
        basket2.setPizzaList(new ArrayList<>());
        basket2.setUser(user2);
        when(basketRepository.save(Mockito.<Basket>any())).thenReturn(basket2);
        when(basketRepository.getBasketByUserId(Mockito.<Long>any())).thenReturn(ofResult);

        Pizza pizza2 = new Pizza();
        pizza2.setId(1L);
        pizza2.setIngredients("Ingredients");
        pizza2.setName("Name");
        pizza2.setPrice(10.0d);
        Optional<Pizza> ofResult2 = Optional.of(pizza2);
        when(pizzaRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        User user3 = new User();
        user3.setAddress("42 Main St");
        user3.setEmail("jane.doe@example.org");
        user3.setFirstName("Jane");
        user3.setId(1L);
        user3.setLastName("Doe");
        user3.setPassword("iloveyou");
        user3.setRole("Role");
        user3.setUsername("janedoe");
        Optional<User> ofResult3 = Optional.of(user3);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult3);
        UserDTO userDTO = new UserDTO(1L, "janedoe", "Jane", "Doe", "42 Main St", "Role", "jane.doe@example.org");

        BasketDTO basketDTO = new BasketDTO(1L, userDTO, new ArrayList<>());

        when(basketDTOMapper.apply(Mockito.<Basket>any())).thenReturn(basketDTO);

        // Act
        BasketDTO actualDeletePizzaFromBasketByUserIdAndPizzaIdResult = basketService
                .deletePizzaFromBasketByUserIdAndPizzaId(1L, 1L);

        // Assert
        verify(basketDTOMapper).apply(isA(Basket.class));
        verify(basketRepository).getBasketByUserId(isA(Long.class));
        verify(pizzaRepository).findById(isA(Long.class));
        verify(userRepository).findById(isA(Long.class));
        verify(basketRepository).save(isA(Basket.class));
        assertSame(basketDTO, actualDeletePizzaFromBasketByUserIdAndPizzaIdResult);
    }

    /**
     * Method under test:
     * {@link BasketService#deletePizzaFromBasketByUserIdAndPizzaId(Long, Long)}
     */
    @Test
    void deletePizzaFromBasketByUserIdAndPizzaIdTest4() throws ResourceNotFoundException {
        // Arrange
        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);

        ArrayList<Pizza> pizzaList = new ArrayList<>();
        pizzaList.add(pizza);

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
        basket.setPizzaList(pizzaList);
        basket.setUser(user);
        Optional<Basket> ofResult = Optional.of(basket);

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");

        Basket basket2 = new Basket();
        basket2.setId(1L);
        basket2.setPizzaList(new ArrayList<>());
        basket2.setUser(user2);
        when(basketRepository.save(Mockito.<Basket>any())).thenReturn(basket2);
        when(basketRepository.getBasketByUserId(Mockito.<Long>any())).thenReturn(ofResult);

        Pizza pizza2 = new Pizza();
        pizza2.setId(1L);
        pizza2.setIngredients("Ingredients");
        pizza2.setName("Name");
        pizza2.setPrice(10.0d);
        Optional<Pizza> ofResult2 = Optional.of(pizza2);
        when(pizzaRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        User user3 = new User();
        user3.setAddress("42 Main St");
        user3.setEmail("jane.doe@example.org");
        user3.setFirstName("Jane");
        user3.setId(1L);
        user3.setLastName("Doe");
        user3.setPassword("iloveyou");
        user3.setRole("Role");
        user3.setUsername("janedoe");
        Optional<User> ofResult3 = Optional.of(user3);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult3);
        when(basketDTOMapper.apply(Mockito.<Basket>any())).thenThrow(new ResourceNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.deletePizzaFromBasketByUserIdAndPizzaId(1L, 1L));
        verify(basketDTOMapper).apply(isA(Basket.class));
        verify(basketRepository).getBasketByUserId(isA(Long.class));
        verify(pizzaRepository).findById(isA(Long.class));
        verify(userRepository).findById(isA(Long.class));
        verify(basketRepository).save(isA(Basket.class));
    }

    /**
     * Method under test:
     * {@link BasketService#deletePizzaFromBasketByUserIdAndPizzaId(Long, Long)}
     */
    @Test
    void deletePizzaFromBasketByUserIdAndPizzaIdTest5() throws ResourceNotFoundException {
        // Arrange
        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);

        Pizza pizza2 = new Pizza();
        pizza2.setId(2L);
        pizza2.setIngredients("Ingredients");
        pizza2.setName("42");
        pizza2.setPrice(0.5d);

        ArrayList<Pizza> pizzaList = new ArrayList<>();
        pizzaList.add(pizza2);
        pizzaList.add(pizza);

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
        basket.setPizzaList(pizzaList);
        basket.setUser(user);
        Optional<Basket> ofResult = Optional.of(basket);

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");

        Basket basket2 = new Basket();
        basket2.setId(1L);
        basket2.setPizzaList(new ArrayList<>());
        basket2.setUser(user2);
        when(basketRepository.save(Mockito.<Basket>any())).thenReturn(basket2);
        when(basketRepository.getBasketByUserId(Mockito.<Long>any())).thenReturn(ofResult);

        Pizza pizza3 = new Pizza();
        pizza3.setId(1L);
        pizza3.setIngredients("Ingredients");
        pizza3.setName("Name");
        pizza3.setPrice(10.0d);
        Optional<Pizza> ofResult2 = Optional.of(pizza3);
        when(pizzaRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        User user3 = new User();
        user3.setAddress("42 Main St");
        user3.setEmail("jane.doe@example.org");
        user3.setFirstName("Jane");
        user3.setId(1L);
        user3.setLastName("Doe");
        user3.setPassword("iloveyou");
        user3.setRole("Role");
        user3.setUsername("janedoe");
        Optional<User> ofResult3 = Optional.of(user3);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult3);
        UserDTO userDTO = new UserDTO(1L, "janedoe", "Jane", "Doe", "42 Main St", "Role", "jane.doe@example.org");

        BasketDTO basketDTO = new BasketDTO(1L, userDTO, new ArrayList<>());

        when(basketDTOMapper.apply(Mockito.<Basket>any())).thenReturn(basketDTO);

        // Act
        BasketDTO actualDeletePizzaFromBasketByUserIdAndPizzaIdResult = basketService
                .deletePizzaFromBasketByUserIdAndPizzaId(1L, 1L);

        // Assert
        verify(basketDTOMapper).apply(isA(Basket.class));
        verify(basketRepository).getBasketByUserId(isA(Long.class));
        verify(pizzaRepository).findById(isA(Long.class));
        verify(userRepository).findById(isA(Long.class));
        verify(basketRepository).save(isA(Basket.class));
        assertSame(basketDTO, actualDeletePizzaFromBasketByUserIdAndPizzaIdResult);
    }

    /**
     * Method under test:
     * {@link BasketService#deletePizzaFromBasketByUsernameAndPizzaId(String, Long)}
     */
    @Test
    void deletePizzaFromBasketByUsernameAndPizzaIdTest() throws ResourceNotFoundException {
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

        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);
        Optional<Pizza> ofResult2 = Optional.of(pizza);
        when(pizzaRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

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
        assertThrows(ResourceNotFoundException.class,
                () -> basketService.deletePizzaFromBasketByUsernameAndPizzaId("janedoe", 1L));
        verify(basketRepository).getBasketByUsername(eq("janedoe"));
        verify(userRepository).findUserByUsername(eq("janedoe"));
        verify(pizzaRepository).findById(isA(Long.class));
    }

    /**
     * Method under test:
     * {@link BasketService#deletePizzaFromBasketByUsernameAndPizzaId(String, Long)}
     */
    @Test
    void deletePizzaFromBasketByUsernameAndPizzaIdTest2() throws ResourceNotFoundException {
        // Arrange
        when(userRepository.findUserByUsername(Mockito.<String>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class,
                () -> basketService.deletePizzaFromBasketByUsernameAndPizzaId("janedoe", 1L));
        verify(userRepository).findUserByUsername(eq("janedoe"));
    }

    /**
     * Method under test:
     * {@link BasketService#deletePizzaFromBasketByUsernameAndPizzaId(String, Long)}
     */
    @Test
    void deletePizzaFromBasketByUsernameAndPizzaIdTest3() throws ResourceNotFoundException {
        // Arrange
        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);

        ArrayList<Pizza> pizzaList = new ArrayList<>();
        pizzaList.add(pizza);

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
        basket.setPizzaList(pizzaList);
        basket.setUser(user);
        Optional<Basket> ofResult = Optional.of(basket);

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");

        Basket basket2 = new Basket();
        basket2.setId(1L);
        basket2.setPizzaList(new ArrayList<>());
        basket2.setUser(user2);
        when(basketRepository.save(Mockito.<Basket>any())).thenReturn(basket2);
        when(basketRepository.getBasketByUsername(Mockito.<String>any())).thenReturn(ofResult);

        Pizza pizza2 = new Pizza();
        pizza2.setId(1L);
        pizza2.setIngredients("Ingredients");
        pizza2.setName("Name");
        pizza2.setPrice(10.0d);
        Optional<Pizza> ofResult2 = Optional.of(pizza2);
        when(pizzaRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        User user3 = new User();
        user3.setAddress("42 Main St");
        user3.setEmail("jane.doe@example.org");
        user3.setFirstName("Jane");
        user3.setId(1L);
        user3.setLastName("Doe");
        user3.setPassword("iloveyou");
        user3.setRole("Role");
        user3.setUsername("janedoe");
        Optional<User> ofResult3 = Optional.of(user3);
        when(userRepository.findUserByUsername(Mockito.<String>any())).thenReturn(ofResult3);
        UserDTO userDTO = new UserDTO(1L, "janedoe", "Jane", "Doe", "42 Main St", "Role", "jane.doe@example.org");

        BasketDTO basketDTO = new BasketDTO(1L, userDTO, new ArrayList<>());

        when(basketDTOMapper.apply(Mockito.<Basket>any())).thenReturn(basketDTO);

        // Act
        BasketDTO actualDeletePizzaFromBasketByUsernameAndPizzaIdResult = basketService
                .deletePizzaFromBasketByUsernameAndPizzaId("janedoe", 1L);

        // Assert
        verify(basketDTOMapper).apply(isA(Basket.class));
        verify(basketRepository).getBasketByUsername(eq("janedoe"));
        verify(userRepository).findUserByUsername(eq("janedoe"));
        verify(pizzaRepository).findById(isA(Long.class));
        verify(basketRepository).save(isA(Basket.class));
        assertSame(basketDTO, actualDeletePizzaFromBasketByUsernameAndPizzaIdResult);
    }

    /**
     * Method under test:
     * {@link BasketService#deletePizzaFromBasketByUsernameAndPizzaId(String, Long)}
     */
    @Test
    void deletePizzaFromBasketByUsernameAndPizzaIdTest4() throws ResourceNotFoundException {
        // Arrange
        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);

        ArrayList<Pizza> pizzaList = new ArrayList<>();
        pizzaList.add(pizza);

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
        basket.setPizzaList(pizzaList);
        basket.setUser(user);
        Optional<Basket> ofResult = Optional.of(basket);

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");

        Basket basket2 = new Basket();
        basket2.setId(1L);
        basket2.setPizzaList(new ArrayList<>());
        basket2.setUser(user2);
        when(basketRepository.save(Mockito.<Basket>any())).thenReturn(basket2);
        when(basketRepository.getBasketByUsername(Mockito.<String>any())).thenReturn(ofResult);

        Pizza pizza2 = new Pizza();
        pizza2.setId(1L);
        pizza2.setIngredients("Ingredients");
        pizza2.setName("Name");
        pizza2.setPrice(10.0d);
        Optional<Pizza> ofResult2 = Optional.of(pizza2);
        when(pizzaRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        User user3 = new User();
        user3.setAddress("42 Main St");
        user3.setEmail("jane.doe@example.org");
        user3.setFirstName("Jane");
        user3.setId(1L);
        user3.setLastName("Doe");
        user3.setPassword("iloveyou");
        user3.setRole("Role");
        user3.setUsername("janedoe");
        Optional<User> ofResult3 = Optional.of(user3);
        when(userRepository.findUserByUsername(Mockito.<String>any())).thenReturn(ofResult3);
        when(basketDTOMapper.apply(Mockito.<Basket>any())).thenThrow(new ResourceNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class,
                () -> basketService.deletePizzaFromBasketByUsernameAndPizzaId("janedoe", 1L));
        verify(basketDTOMapper).apply(isA(Basket.class));
        verify(basketRepository).getBasketByUsername(eq("janedoe"));
        verify(userRepository).findUserByUsername(eq("janedoe"));
        verify(pizzaRepository).findById(isA(Long.class));
        verify(basketRepository).save(isA(Basket.class));
    }

    /**
     * Method under test:
     * {@link BasketService#deletePizzaFromBasketByUsernameAndPizzaId(String, Long)}
     */
    @Test
    void deletePizzaFromBasketByUsernameAndPizzaIdTest5() throws ResourceNotFoundException {
        // Arrange
        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);

        Pizza pizza2 = new Pizza();
        pizza2.setId(2L);
        pizza2.setIngredients("Ingredients");
        pizza2.setName("42");
        pizza2.setPrice(0.5d);

        ArrayList<Pizza> pizzaList = new ArrayList<>();
        pizzaList.add(pizza2);
        pizzaList.add(pizza);

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
        basket.setPizzaList(pizzaList);
        basket.setUser(user);
        Optional<Basket> ofResult = Optional.of(basket);

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");

        Basket basket2 = new Basket();
        basket2.setId(1L);
        basket2.setPizzaList(new ArrayList<>());
        basket2.setUser(user2);
        when(basketRepository.save(Mockito.<Basket>any())).thenReturn(basket2);
        when(basketRepository.getBasketByUsername(Mockito.<String>any())).thenReturn(ofResult);

        Pizza pizza3 = new Pizza();
        pizza3.setId(1L);
        pizza3.setIngredients("Ingredients");
        pizza3.setName("Name");
        pizza3.setPrice(10.0d);
        Optional<Pizza> ofResult2 = Optional.of(pizza3);
        when(pizzaRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        User user3 = new User();
        user3.setAddress("42 Main St");
        user3.setEmail("jane.doe@example.org");
        user3.setFirstName("Jane");
        user3.setId(1L);
        user3.setLastName("Doe");
        user3.setPassword("iloveyou");
        user3.setRole("Role");
        user3.setUsername("janedoe");
        Optional<User> ofResult3 = Optional.of(user3);
        when(userRepository.findUserByUsername(Mockito.<String>any())).thenReturn(ofResult3);
        UserDTO userDTO = new UserDTO(1L, "janedoe", "Jane", "Doe", "42 Main St", "Role", "jane.doe@example.org");

        BasketDTO basketDTO = new BasketDTO(1L, userDTO, new ArrayList<>());

        when(basketDTOMapper.apply(Mockito.<Basket>any())).thenReturn(basketDTO);

        // Act
        BasketDTO actualDeletePizzaFromBasketByUsernameAndPizzaIdResult = basketService
                .deletePizzaFromBasketByUsernameAndPizzaId("janedoe", 1L);

        // Assert
        verify(basketDTOMapper).apply(isA(Basket.class));
        verify(basketRepository).getBasketByUsername(eq("janedoe"));
        verify(userRepository).findUserByUsername(eq("janedoe"));
        verify(pizzaRepository).findById(isA(Long.class));
        verify(basketRepository).save(isA(Basket.class));
        assertSame(basketDTO, actualDeletePizzaFromBasketByUsernameAndPizzaIdResult);
    }

    /**
     * Method under test: {@link BasketService#deleteBasketById(Long)}
     */
    @Test
    void deleteBasketByIdTest() throws ResourceNotFoundException {
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
        doNothing().when(basketRepository).deleteById(Mockito.<Long>any());
        when(basketRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act
        basketService.deleteBasketById(1L);

        // Assert that nothing has changed
        verify(basketRepository).deleteById(isA(Long.class));
        verify(basketRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link BasketService#deleteBasketById(Long)}
     */
    @Test
    void deleteBasketByIdTest2() throws ResourceNotFoundException {
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
        doThrow(new ResourceNotFoundException("An error occurred")).when(basketRepository).deleteById(Mockito.<Long>any());
        when(basketRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.deleteBasketById(1L));
        verify(basketRepository).deleteById(isA(Long.class));
        verify(basketRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link BasketService#deleteBasketById(Long)}
     */
    @Test
    void deleteBasketByIdTest3() throws ResourceNotFoundException {
        // Arrange
        Optional<Basket> emptyResult = Optional.empty();
        when(basketRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.deleteBasketById(1L));
        verify(basketRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link BasketService#deleteBasketByUserId(Long)}
     */
    @Test
    void deleteBasketByUserIdTest() throws ResourceNotFoundException {
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
        when(basketRepository.deleteBasketByUserId(Mockito.<Long>any())).thenReturn(1);
        when(basketRepository.getBasketByUserId(Mockito.<Long>any())).thenReturn(ofResult);

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");
        Optional<User> ofResult2 = Optional.of(user2);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        // Act
        basketService.deleteBasketByUserId(1L);

        // Assert that nothing has changed
        verify(basketRepository).deleteBasketByUserId(isA(Long.class));
        verify(basketRepository).getBasketByUserId(isA(Long.class));
        verify(userRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link BasketService#deleteBasketByUserId(Long)}
     */
    @Test
    void deleteBasketByUserIdTest2() throws ResourceNotFoundException {
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
        when(basketRepository.deleteBasketByUserId(Mockito.<Long>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));
        when(basketRepository.getBasketByUserId(Mockito.<Long>any())).thenReturn(ofResult);

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");
        Optional<User> ofResult2 = Optional.of(user2);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.deleteBasketByUserId(1L));
        verify(basketRepository).deleteBasketByUserId(isA(Long.class));
        verify(basketRepository).getBasketByUserId(isA(Long.class));
        verify(userRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link BasketService#deleteBasketByUserId(Long)}
     */
    @Test
    void deleteBasketByUserIdTest3() throws ResourceNotFoundException {
        // Arrange
        Optional<Basket> emptyResult = Optional.empty();
        when(basketRepository.getBasketByUserId(Mockito.<Long>any())).thenReturn(emptyResult);

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
        assertThrows(ResourceNotFoundException.class, () -> basketService.deleteBasketByUserId(1L));
        verify(basketRepository).getBasketByUserId(isA(Long.class));
        verify(userRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link BasketService#deleteBasketByUserId(Long)}
     */
    @Test
    void deleteBasketByUserIdTest4() throws ResourceNotFoundException {
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
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.deleteBasketByUserId(1L));
        verify(basketRepository).getBasketByUserId(isA(Long.class));
        verify(userRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link BasketService#deleteBasketByUsername(String)}
     */
    @Test
    void deleteBasketByUsernameTest() throws ResourceNotFoundException {
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
        when(basketRepository.deleteBasketByUsername(Mockito.<String>any())).thenReturn(1);
        when(basketRepository.getBasketByUsername(Mockito.<String>any())).thenReturn(ofResult);

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");
        Optional<User> ofResult2 = Optional.of(user2);
        when(userRepository.findUserByUsername(Mockito.<String>any())).thenReturn(ofResult2);

        // Act
        basketService.deleteBasketByUsername("janedoe");

        // Assert that nothing has changed
        verify(basketRepository).deleteBasketByUsername(eq("janedoe"));
        verify(basketRepository).getBasketByUsername(eq("janedoe"));
        verify(userRepository).findUserByUsername(eq("janedoe"));
    }

    /**
     * Method under test: {@link BasketService#deleteBasketByUsername(String)}
     */
    @Test
    void deleteBasketByUsernameTest2() throws ResourceNotFoundException {
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
        when(basketRepository.deleteBasketByUsername(Mockito.<String>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));
        when(basketRepository.getBasketByUsername(Mockito.<String>any())).thenReturn(ofResult);

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole("Role");
        user2.setUsername("janedoe");
        Optional<User> ofResult2 = Optional.of(user2);
        when(userRepository.findUserByUsername(Mockito.<String>any())).thenReturn(ofResult2);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.deleteBasketByUsername("janedoe"));
        verify(basketRepository).deleteBasketByUsername(eq("janedoe"));
        verify(basketRepository).getBasketByUsername(eq("janedoe"));
        verify(userRepository).findUserByUsername(eq("janedoe"));
    }

    /**
     * Method under test: {@link BasketService#deleteBasketByUsername(String)}
     */
    @Test
    void deleteBasketByUsernameTest3() throws ResourceNotFoundException {
        // Arrange
        Optional<Basket> emptyResult = Optional.empty();
        when(basketRepository.getBasketByUsername(Mockito.<String>any())).thenReturn(emptyResult);

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
        assertThrows(ResourceNotFoundException.class, () -> basketService.deleteBasketByUsername("janedoe"));
        verify(basketRepository).getBasketByUsername(eq("janedoe"));
        verify(userRepository).findUserByUsername(eq("janedoe"));
    }

    /**
     * Method under test: {@link BasketService#deleteBasketByUsername(String)}
     */
    @Test
    void deleteBasketByUsernameTest4() throws ResourceNotFoundException {
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
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findUserByUsername(Mockito.<String>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> basketService.deleteBasketByUsername("janedoe"));
        verify(basketRepository).getBasketByUsername(eq("janedoe"));
        verify(userRepository).findUserByUsername(eq("janedoe"));
    }
}
