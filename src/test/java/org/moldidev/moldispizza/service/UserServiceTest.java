package org.moldidev.moldispizza.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.moldidev.moldispizza.dto.UserDTO;
import org.moldidev.moldispizza.dto.UserDTOMapper;
import org.moldidev.moldispizza.entity.User;
import org.moldidev.moldispizza.exception.InvalidArgumentException;
import org.moldidev.moldispizza.exception.ResourceAlreadyExistsException;
import org.moldidev.moldispizza.exception.ResourceNotFoundException;
import org.moldidev.moldispizza.repository.BasketRepository;
import org.moldidev.moldispizza.repository.OrderRepository;
import org.moldidev.moldispizza.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class UserServiceTest {
    @MockBean
    private BasketRepository basketRepository;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private UserDTOMapper userDTOMapper;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    /**
     * Method under test: {@link UserService#getAllUsers()}
     */
    @Test
    void getAllUsersTest() {
        // Arrange
        when(userRepository.findAll()).thenReturn(new ArrayList<>());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.getAllUsers());
        verify(userRepository).findAll();
    }

    /**
     * Method under test: {@link UserService#getAllUsers()}
     */
    @Test
    void getAllUsersTest2() {
        // Arrange
        User user = new User();
        user.setAddress("42 Main St");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole("there are no users");
        user.setUsername("janedoe");

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user);
        when(userRepository.findAll()).thenReturn(userList);
        when(userDTOMapper.apply(Mockito.<User>any()))
                .thenReturn(new UserDTO(1L, "janedoe", "Jane", "Doe", "42 Main St", "Role", "jane.doe@example.org"));

        // Act
        List<UserDTO> actualAllUsers = userService.getAllUsers();

        // Assert
        verify(userDTOMapper).apply(isA(User.class));
        verify(userRepository).findAll();
        assertEquals(1, actualAllUsers.size());
    }

    /**
     * Method under test: {@link UserService#getAllUsers()}
     */
    @Test
    void getAllUsersTest3() {
        // Arrange
        User user = new User();
        user.setAddress("42 Main St");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole("there are no users");
        user.setUsername("janedoe");

        User user2 = new User();
        user2.setAddress("17 High St");
        user2.setEmail("john.smith@example.org");
        user2.setFirstName("John");
        user2.setId(2L);
        user2.setLastName("Smith");
        user2.setPassword("Password");
        user2.setRole("CUSTOMER");
        user2.setUsername("Username");

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user2);
        userList.add(user);
        when(userRepository.findAll()).thenReturn(userList);
        when(userDTOMapper.apply(Mockito.<User>any()))
                .thenReturn(new UserDTO(1L, "janedoe", "Jane", "Doe", "42 Main St", "Role", "jane.doe@example.org"));

        // Act
        List<UserDTO> actualAllUsers = userService.getAllUsers();

        // Assert
        verify(userDTOMapper, atLeast(1)).apply(Mockito.<User>any());
        verify(userRepository).findAll();
        assertEquals(2, actualAllUsers.size());
    }

    /**
     * Method under test: {@link UserService#getAllUsers()}
     */
    @Test
    void getAllUsersTest4() {
        // Arrange
        User user = new User();
        user.setAddress("42 Main St");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole("there are no users");
        user.setUsername("janedoe");

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user);
        when(userRepository.findAll()).thenReturn(userList);
        when(userDTOMapper.apply(Mockito.<User>any())).thenThrow(new ResourceNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.getAllUsers());
        verify(userDTOMapper).apply(isA(User.class));
        verify(userRepository).findAll();
    }

    /**
     * Method under test: {@link UserService#getUserById(Long)}
     */
    @Test
    void getUserByIdTest() throws ResourceNotFoundException {
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
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        UserDTO userDTO = new UserDTO(1L, "janedoe", "Jane", "Doe", "42 Main St", "Role", "jane.doe@example.org");

        when(userDTOMapper.apply(Mockito.<User>any())).thenReturn(userDTO);

        // Act
        UserDTO actualUserById = userService.getUserById(1L);

        // Assert
        verify(userDTOMapper).apply(isA(User.class));
        verify(userRepository).findById(isA(Long.class));
        assertSame(userDTO, actualUserById);
    }

    /**
     * Method under test: {@link UserService#getUserById(Long)}
     */
    @Test
    void getUserByIdTest2() throws ResourceNotFoundException {
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
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(userDTOMapper.apply(Mockito.<User>any())).thenThrow(new ResourceNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1L));
        verify(userDTOMapper).apply(isA(User.class));
        verify(userRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link UserService#getUserById(Long)}
     */
    @Test
    void getUserByIdTest3() throws ResourceNotFoundException {
        // Arrange
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1L));
        verify(userRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link UserService#getUserByUsername(String)}
     */
    @Test
    void getUserByUsernameTest() throws ResourceNotFoundException {
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
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findUserByUsername(Mockito.<String>any())).thenReturn(ofResult);
        UserDTO userDTO = new UserDTO(1L, "janedoe", "Jane", "Doe", "42 Main St", "Role", "jane.doe@example.org");

        when(userDTOMapper.apply(Mockito.<User>any())).thenReturn(userDTO);

        // Act
        UserDTO actualUserByUsername = userService.getUserByUsername("janedoe");

        // Assert
        verify(userDTOMapper).apply(isA(User.class));
        verify(userRepository).findUserByUsername(eq("janedoe"));
        assertSame(userDTO, actualUserByUsername);
    }

    /**
     * Method under test: {@link UserService#getUserByUsername(String)}
     */
    @Test
    void getUserByUsernameTest2() throws ResourceNotFoundException {
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
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findUserByUsername(Mockito.<String>any())).thenReturn(ofResult);
        when(userDTOMapper.apply(Mockito.<User>any())).thenThrow(new ResourceNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserByUsername("janedoe"));
        verify(userDTOMapper).apply(isA(User.class));
        verify(userRepository).findUserByUsername(eq("janedoe"));
    }

    /**
     * Method under test: {@link UserService#getUserByUsername(String)}
     */
    @Test
    void getUserByUsernameTest3() throws ResourceNotFoundException {
        // Arrange
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findUserByUsername(Mockito.<String>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserByUsername("janedoe"));
        verify(userRepository).findUserByUsername(eq("janedoe"));
    }

    /**
     * Method under test: {@link UserService#addUser(User)}
     */
    @Test
    void addUserTest() throws InvalidArgumentException, ResourceAlreadyExistsException {
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

        // Act and Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> userService.addUser(user2));
        verify(userRepository).findUserByUsername(eq("janedoe"));
    }

    /**
     * Method under test: {@link UserService#addUser(User)}
     */
    @Test
    void addUserTest2() throws InvalidArgumentException, ResourceAlreadyExistsException {
        // Arrange
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findUserByUsername(Mockito.<String>any())).thenReturn(emptyResult);

        User user = new User();
        user.setAddress("42 Main St");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUsername("janedoe");

        // Act and Assert
        assertThrows(InvalidArgumentException.class, () -> userService.addUser(user));
        verify(userRepository).findUserByUsername(eq("janedoe"));
    }

    /**
     * Method under test: {@link UserService#addUser(User)}
     */
    @Test
    void addUserTest3() throws InvalidArgumentException, ResourceAlreadyExistsException {
        // Arrange
        when(userRepository.findUserByUsername(Mockito.<String>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));

        User user = new User();
        user.setAddress("42 Main St");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUsername("janedoe");

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.addUser(user));
        verify(userRepository).findUserByUsername(eq("janedoe"));
    }

    /**
     * Method under test: {@link UserService#updateUserById(Long, User)}
     */
    @Test
    void updateUserByIdTest() throws InvalidArgumentException, ResourceNotFoundException {
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
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        User newUser = new User();
        newUser.setAddress("42 Main St");
        newUser.setEmail("jane.doe@example.org");
        newUser.setFirstName("Jane");
        newUser.setId(1L);
        newUser.setLastName("Doe");
        newUser.setPassword("iloveyou");
        newUser.setRole("Role");
        newUser.setUsername("janedoe");

        // Act and Assert
        assertThrows(InvalidArgumentException.class, () -> userService.updateUserById(1L, newUser));
        verify(userRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link UserService#updateUserById(Long, User)}
     */
    @Test
    void updateUserByIdTest2() throws InvalidArgumentException, ResourceNotFoundException {
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
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        User newUser = mock(User.class);
        when(newUser.getPassword()).thenReturn("iloveyou");
        when(newUser.getUsername()).thenReturn("CUSTOMER");
        doNothing().when(newUser).setAddress(Mockito.<String>any());
        doNothing().when(newUser).setEmail(Mockito.<String>any());
        doNothing().when(newUser).setFirstName(Mockito.<String>any());
        doNothing().when(newUser).setId(Mockito.<Long>any());
        doNothing().when(newUser).setLastName(Mockito.<String>any());
        doNothing().when(newUser).setPassword(Mockito.<String>any());
        doNothing().when(newUser).setRole(Mockito.<String>any());
        doNothing().when(newUser).setUsername(Mockito.<String>any());
        newUser.setAddress("42 Main St");
        newUser.setEmail("jane.doe@example.org");
        newUser.setFirstName("Jane");
        newUser.setId(1L);
        newUser.setLastName("Doe");
        newUser.setPassword("iloveyou");
        newUser.setRole("Role");
        newUser.setUsername("janedoe");

        // Act and Assert
        assertThrows(InvalidArgumentException.class, () -> userService.updateUserById(1L, newUser));
        verify(newUser).getPassword();
        verify(newUser).getUsername();
        verify(newUser).setAddress(eq("42 Main St"));
        verify(newUser).setEmail(eq("jane.doe@example.org"));
        verify(newUser).setFirstName(eq("Jane"));
        verify(newUser).setId(isA(Long.class));
        verify(newUser).setLastName(eq("Doe"));
        verify(newUser).setPassword(eq("iloveyou"));
        verify(newUser).setRole(eq("Role"));
        verify(newUser).setUsername(eq("janedoe"));
        verify(userRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link UserService#updateUserById(Long, User)}
     */
    @Test
    void updateUserByIdTest10() throws InvalidArgumentException, ResourceNotFoundException {
        // Arrange
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        User newUser = new User();
        newUser.setAddress("42 Main St");
        newUser.setEmail("jane.doe@example.org");
        newUser.setFirstName("Jane");
        newUser.setId(1L);
        newUser.setLastName("Doe");
        newUser.setPassword("iloveyou");
        newUser.setRole("Role");
        newUser.setUsername("janedoe");

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.updateUserById(1L, newUser));
        verify(userRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link UserService#updateUserByUsername(String, User)}
     */
    @Test
    void updateUserByUsernameTest() throws ResourceNotFoundException {
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
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findUserByUsername(Mockito.<String>any())).thenReturn(ofResult);

        User newUser = new User();
        newUser.setAddress("42 Main St");
        newUser.setEmail("jane.doe@example.org");
        newUser.setFirstName("Jane");
        newUser.setId(1L);
        newUser.setLastName("Doe");
        newUser.setPassword("iloveyou");
        newUser.setRole("Role");
        newUser.setUsername("janedoe");

        // Act and Assert
        assertThrows(InvalidArgumentException.class, () -> userService.updateUserByUsername("janedoe", newUser));
        verify(userRepository).findUserByUsername(eq("janedoe"));
    }

    /**
     * Method under test: {@link UserService#updateUserByUsername(String, User)}
     */
    @Test
    void updateUserByUsernameTest2() throws ResourceNotFoundException {
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
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findUserByUsername(Mockito.<String>any())).thenReturn(ofResult);
        User newUser = mock(User.class);
        when(newUser.getUsername()).thenReturn("CUSTOMER");
        doNothing().when(newUser).setAddress(Mockito.<String>any());
        doNothing().when(newUser).setEmail(Mockito.<String>any());
        doNothing().when(newUser).setFirstName(Mockito.<String>any());
        doNothing().when(newUser).setId(Mockito.<Long>any());
        doNothing().when(newUser).setLastName(Mockito.<String>any());
        doNothing().when(newUser).setPassword(Mockito.<String>any());
        doNothing().when(newUser).setRole(Mockito.<String>any());
        doNothing().when(newUser).setUsername(Mockito.<String>any());
        newUser.setAddress("42 Main St");
        newUser.setEmail("jane.doe@example.org");
        newUser.setFirstName("Jane");
        newUser.setId(1L);
        newUser.setLastName("Doe");
        newUser.setPassword("iloveyou");
        newUser.setRole("Role");
        newUser.setUsername("janedoe");

        // Act and Assert
        assertThrows(InvalidArgumentException.class, () -> userService.updateUserByUsername("janedoe", newUser));
        verify(newUser, atLeast(1)).getUsername();
        verify(newUser).setAddress(eq("42 Main St"));
        verify(newUser).setEmail(eq("jane.doe@example.org"));
        verify(newUser).setFirstName(eq("Jane"));
        verify(newUser).setId(isA(Long.class));
        verify(newUser).setLastName(eq("Doe"));
        verify(newUser).setPassword(eq("iloveyou"));
        verify(newUser).setRole(eq("Role"));
        verify(newUser).setUsername(eq("janedoe"));
        verify(userRepository).findUserByUsername(eq("janedoe"));
    }

    /**
     * Method under test: {@link UserService#updateUserByUsername(String, User)}
     */
    @Test
    void updateUserByUsernameTest10() throws ResourceNotFoundException {
        // Arrange
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findUserByUsername(Mockito.<String>any())).thenReturn(emptyResult);

        User newUser = new User();
        newUser.setAddress("42 Main St");
        newUser.setEmail("jane.doe@example.org");
        newUser.setFirstName("Jane");
        newUser.setId(1L);
        newUser.setLastName("Doe");
        newUser.setPassword("iloveyou");
        newUser.setRole("Role");
        newUser.setUsername("janedoe");

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.updateUserByUsername("janedoe", newUser));
        verify(userRepository).findUserByUsername(eq("janedoe"));
    }

    /**
     * Method under test: {@link UserService#deleteUserById(Long)}
     */
    @Test
    void deleteUserByIdTest() throws ResourceNotFoundException {
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
        Optional<User> ofResult = Optional.of(user);
        doNothing().when(userRepository).deleteById(Mockito.<Long>any());
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(basketRepository.deleteBasketByUserId(Mockito.<Long>any())).thenReturn(1);
        when(orderRepository.deleteAllOrdersByUserId(Mockito.<Long>any())).thenReturn(1);

        // Act
        userService.deleteUserById(1L);

        // Assert that nothing has changed
        verify(basketRepository).deleteBasketByUserId(isA(Long.class));
        verify(orderRepository).deleteAllOrdersByUserId(isA(Long.class));
        verify(userRepository).deleteById(isA(Long.class));
        verify(userRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link UserService#deleteUserById(Long)}
     */
    @Test
    void deleteUserByIdTest2() throws ResourceNotFoundException {
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
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(orderRepository.deleteAllOrdersByUserId(Mockito.<Long>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUserById(1L));
        verify(orderRepository).deleteAllOrdersByUserId(isA(Long.class));
        verify(userRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link UserService#deleteUserById(Long)}
     */
    @Test
    void deleteUserByIdTest3() throws ResourceNotFoundException {
        // Arrange
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUserById(1L));
        verify(userRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link UserService#deleteUserByUsername(String)}
     */
    @Test
    void deleteUserByUsernameTest() throws ResourceNotFoundException {
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
        Optional<User> ofResult = Optional.of(user);
        doNothing().when(userRepository).deleteUserByUsername(Mockito.<String>any());
        when(userRepository.findUserByUsername(Mockito.<String>any())).thenReturn(ofResult);
        when(basketRepository.deleteBasketByUsername(Mockito.<String>any())).thenReturn(1);
        when(orderRepository.deleteAllOrdersByUsername(Mockito.<String>any())).thenReturn(1);

        // Act
        userService.deleteUserByUsername("janedoe");

        // Assert that nothing has changed
        verify(basketRepository).deleteBasketByUsername(eq("janedoe"));
        verify(orderRepository).deleteAllOrdersByUsername(eq("janedoe"));
        verify(userRepository).deleteUserByUsername(eq("janedoe"));
        verify(userRepository).findUserByUsername(eq("janedoe"));
    }

    /**
     * Method under test: {@link UserService#deleteUserByUsername(String)}
     */
    @Test
    void deleteUserByUsernameTest2() throws ResourceNotFoundException {
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
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findUserByUsername(Mockito.<String>any())).thenReturn(ofResult);
        when(orderRepository.deleteAllOrdersByUsername(Mockito.<String>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUserByUsername("janedoe"));
        verify(orderRepository).deleteAllOrdersByUsername(eq("janedoe"));
        verify(userRepository).findUserByUsername(eq("janedoe"));
    }

    /**
     * Method under test: {@link UserService#deleteUserByUsername(String)}
     */
    @Test
    void deleteUserByUsernameTest3() throws ResourceNotFoundException {
        // Arrange
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findUserByUsername(Mockito.<String>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUserByUsername("janedoe"));
        verify(userRepository).findUserByUsername(eq("janedoe"));
    }
}
