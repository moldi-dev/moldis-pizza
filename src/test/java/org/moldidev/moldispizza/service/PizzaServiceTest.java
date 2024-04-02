package org.moldidev.moldispizza.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.moldidev.moldispizza.dto.PizzaDTO;
import org.moldidev.moldispizza.dto.PizzaDTOMapper;
import org.moldidev.moldispizza.entity.Pizza;
import org.moldidev.moldispizza.exception.InvalidArgumentException;
import org.moldidev.moldispizza.exception.ResourceAlreadyExistsException;
import org.moldidev.moldispizza.exception.ResourceNotFoundException;
import org.moldidev.moldispizza.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {PizzaService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class PizzaServiceTest {
    @MockBean
    private PizzaDTOMapper pizzaDTOMapper;

    @MockBean
    private PizzaRepository pizzaRepository;

    @Autowired
    private PizzaService pizzaService;

    /**
     * Method under test: {@link PizzaService#getAllPizzas()}
     */
    @Test
    void getAllPizzasTest() throws ResourceNotFoundException {
        // Arrange
        when(pizzaRepository.findAll()).thenReturn(new ArrayList<>());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> pizzaService.getAllPizzas());
        verify(pizzaRepository).findAll();
    }

    /**
     * Method under test: {@link PizzaService#getAllPizzas()}
     */
    @Test
    void getAllPizzasTest2() throws ResourceNotFoundException {
        // Arrange
        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("there are no pizzas");
        pizza.setName("there are no pizzas");
        pizza.setPrice(10.0d);

        ArrayList<Pizza> pizzaList = new ArrayList<>();
        pizzaList.add(pizza);
        when(pizzaRepository.findAll()).thenReturn(pizzaList);
        PizzaDTO pizzaDTO = new PizzaDTO(1L, "Name", "Ingredients", 10.0d);

        when(pizzaDTOMapper.apply(Mockito.<Pizza>any())).thenReturn(pizzaDTO);

        // Act
        List<PizzaDTO> actualAllPizzas = pizzaService.getAllPizzas();

        // Assert
        verify(pizzaDTOMapper).apply(isA(Pizza.class));
        verify(pizzaRepository).findAll();
        assertEquals(1, actualAllPizzas.size());
        assertSame(pizzaDTO, actualAllPizzas.get(0));
    }

    /**
     * Method under test: {@link PizzaService#getAllPizzas()}
     */
    @Test
    void getAllPizzasTest3() throws ResourceNotFoundException {
        // Arrange
        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("there are no pizzas");
        pizza.setName("there are no pizzas");
        pizza.setPrice(10.0d);

        Pizza pizza2 = new Pizza();
        pizza2.setId(2L);
        pizza2.setIngredients("42");
        pizza2.setName("42");
        pizza2.setPrice(0.5d);

        ArrayList<Pizza> pizzaList = new ArrayList<>();
        pizzaList.add(pizza2);
        pizzaList.add(pizza);
        when(pizzaRepository.findAll()).thenReturn(pizzaList);
        PizzaDTO pizzaDTO = new PizzaDTO(1L, "Name", "Ingredients", 10.0d);

        when(pizzaDTOMapper.apply(Mockito.<Pizza>any())).thenReturn(pizzaDTO);

        // Act
        List<PizzaDTO> actualAllPizzas = pizzaService.getAllPizzas();

        // Assert
        verify(pizzaDTOMapper, atLeast(1)).apply(Mockito.<Pizza>any());
        verify(pizzaRepository).findAll();
        assertEquals(2, actualAllPizzas.size());
        assertSame(pizzaDTO, actualAllPizzas.get(0));
    }

    /**
     * Method under test: {@link PizzaService#getAllPizzas()}
     */
    @Test
    void getAllPizzasTest4() throws ResourceNotFoundException {
        // Arrange
        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("there are no pizzas");
        pizza.setName("there are no pizzas");
        pizza.setPrice(10.0d);

        ArrayList<Pizza> pizzaList = new ArrayList<>();
        pizzaList.add(pizza);
        when(pizzaRepository.findAll()).thenReturn(pizzaList);
        when(pizzaDTOMapper.apply(Mockito.<Pizza>any())).thenThrow(new ResourceNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> pizzaService.getAllPizzas());
        verify(pizzaDTOMapper).apply(isA(Pizza.class));
        verify(pizzaRepository).findAll();
    }

    /**
     * Method under test: {@link PizzaService#getPizzaById(Long)}
     */
    @Test
    void getPizzaByIdTest() throws ResourceNotFoundException {
        // Arrange
        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);
        Optional<Pizza> ofResult = Optional.of(pizza);
        when(pizzaRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        PizzaDTO pizzaDTO = new PizzaDTO(1L, "Name", "Ingredients", 10.0d);

        when(pizzaDTOMapper.apply(Mockito.<Pizza>any())).thenReturn(pizzaDTO);

        // Act
        PizzaDTO actualPizzaById = pizzaService.getPizzaById(1L);

        // Assert
        verify(pizzaDTOMapper).apply(isA(Pizza.class));
        verify(pizzaRepository).findById(isA(Long.class));
        assertSame(pizzaDTO, actualPizzaById);
    }

    /**
     * Method under test: {@link PizzaService#getPizzaById(Long)}
     */
    @Test
    void getPizzaByIdTest2() throws ResourceNotFoundException {
        // Arrange
        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);
        Optional<Pizza> ofResult = Optional.of(pizza);
        when(pizzaRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(pizzaDTOMapper.apply(Mockito.<Pizza>any())).thenThrow(new ResourceNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> pizzaService.getPizzaById(1L));
        verify(pizzaDTOMapper).apply(isA(Pizza.class));
        verify(pizzaRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link PizzaService#getPizzaById(Long)}
     */
    @Test
    void getPizzaByIdTest3() throws ResourceNotFoundException {
        // Arrange
        Optional<Pizza> emptyResult = Optional.empty();
        when(pizzaRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> pizzaService.getPizzaById(1L));
        verify(pizzaRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link PizzaService#getPizzaByPizzaName(String)}
     */
    @Test
    void getPizzaByPizzaNameTest() throws ResourceNotFoundException {
        // Arrange
        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);
        Optional<Pizza> ofResult = Optional.of(pizza);
        when(pizzaRepository.findPizzaByName(Mockito.<String>any())).thenReturn(ofResult);
        PizzaDTO pizzaDTO = new PizzaDTO(1L, "Name", "Ingredients", 10.0d);

        when(pizzaDTOMapper.apply(Mockito.<Pizza>any())).thenReturn(pizzaDTO);

        // Act
        PizzaDTO actualPizzaByPizzaName = pizzaService.getPizzaByPizzaName("Name");

        // Assert
        verify(pizzaDTOMapper).apply(isA(Pizza.class));
        verify(pizzaRepository).findPizzaByName(eq("Name"));
        assertSame(pizzaDTO, actualPizzaByPizzaName);
    }

    /**
     * Method under test: {@link PizzaService#getPizzaByPizzaName(String)}
     */
    @Test
    void getPizzaByPizzaNameTest2() throws ResourceNotFoundException {
        // Arrange
        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);
        Optional<Pizza> ofResult = Optional.of(pizza);
        when(pizzaRepository.findPizzaByName(Mockito.<String>any())).thenReturn(ofResult);
        when(pizzaDTOMapper.apply(Mockito.<Pizza>any())).thenThrow(new ResourceNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> pizzaService.getPizzaByPizzaName("Name"));
        verify(pizzaDTOMapper).apply(isA(Pizza.class));
        verify(pizzaRepository).findPizzaByName(eq("Name"));
    }

    /**
     * Method under test: {@link PizzaService#getPizzaByPizzaName(String)}
     */
    @Test
    void getPizzaByPizzaNameTest3() throws ResourceNotFoundException {
        // Arrange
        Optional<Pizza> emptyResult = Optional.empty();
        when(pizzaRepository.findPizzaByName(Mockito.<String>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> pizzaService.getPizzaByPizzaName("Name"));
        verify(pizzaRepository).findPizzaByName(eq("Name"));
    }

    /**
     * Method under test: {@link PizzaService#addPizza(Pizza)}
     */
    @Test
    void addPizzaTest() throws InvalidArgumentException, ResourceAlreadyExistsException {
        // Arrange
        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);
        Optional<Pizza> ofResult = Optional.of(pizza);
        when(pizzaRepository.findPizzaByName(Mockito.<String>any())).thenReturn(ofResult);

        Pizza pizza2 = new Pizza();
        pizza2.setId(1L);
        pizza2.setIngredients("Ingredients");
        pizza2.setName("Name");
        pizza2.setPrice(10.0d);

        // Act and Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> pizzaService.addPizza(pizza2));
        verify(pizzaRepository).findPizzaByName(eq("Name"));
    }

    /**
     * Method under test: {@link PizzaService#addPizza(Pizza)}
     */
    @Test
    void addPizzaTest2() throws InvalidArgumentException, ResourceAlreadyExistsException {
        // Arrange
        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);
        when(pizzaRepository.save(Mockito.<Pizza>any())).thenReturn(pizza);
        Optional<Pizza> emptyResult = Optional.empty();
        when(pizzaRepository.findPizzaByName(Mockito.<String>any())).thenReturn(emptyResult);
        PizzaDTO pizzaDTO = new PizzaDTO(1L, "Name", "Ingredients", 10.0d);

        when(pizzaDTOMapper.apply(Mockito.<Pizza>any())).thenReturn(pizzaDTO);

        Pizza pizza2 = new Pizza();
        pizza2.setId(1L);
        pizza2.setIngredients("Ingredients");
        pizza2.setName("Name");
        pizza2.setPrice(10.0d);

        // Act
        PizzaDTO actualAddPizzaResult = pizzaService.addPizza(pizza2);

        // Assert
        verify(pizzaDTOMapper).apply(isA(Pizza.class));
        verify(pizzaRepository).findPizzaByName(eq("Name"));
        verify(pizzaRepository).save(isA(Pizza.class));
        assertSame(pizzaDTO, actualAddPizzaResult);
    }

    /**
     * Method under test: {@link PizzaService#addPizza(Pizza)}
     */
    @Test
    void addPizzaTest3() throws InvalidArgumentException, ResourceAlreadyExistsException {
        // Arrange
        Optional<Pizza> emptyResult = Optional.empty();
        when(pizzaRepository.findPizzaByName(Mockito.<String>any())).thenReturn(emptyResult);
        Pizza pizza = mock(Pizza.class);
        when(pizza.getPrice()).thenThrow(new ResourceNotFoundException("An error occurred"));
        when(pizza.getName()).thenReturn("Name");
        doNothing().when(pizza).setId(Mockito.<Long>any());
        doNothing().when(pizza).setIngredients(Mockito.<String>any());
        doNothing().when(pizza).setName(Mockito.<String>any());
        doNothing().when(pizza).setPrice(anyDouble());
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> pizzaService.addPizza(pizza));
        verify(pizza, atLeast(1)).getName();
        verify(pizza).getPrice();
        verify(pizza).setId(isA(Long.class));
        verify(pizza).setIngredients(eq("Ingredients"));
        verify(pizza).setName(eq("Name"));
        verify(pizza).setPrice(eq(10.0d));
        verify(pizzaRepository).findPizzaByName(eq("Name"));
    }

    /**
     * Method under test: {@link PizzaService#addPizza(Pizza)}
     */
    @Test
    void addPizzaTest4() throws InvalidArgumentException, ResourceAlreadyExistsException {
        // Arrange
        Optional<Pizza> emptyResult = Optional.empty();
        when(pizzaRepository.findPizzaByName(Mockito.<String>any())).thenReturn(emptyResult);
        Pizza pizza = mock(Pizza.class);
        when(pizza.getPrice()).thenReturn(-0.5d);
        when(pizza.getName()).thenReturn("Name");
        doNothing().when(pizza).setId(Mockito.<Long>any());
        doNothing().when(pizza).setIngredients(Mockito.<String>any());
        doNothing().when(pizza).setName(Mockito.<String>any());
        doNothing().when(pizza).setPrice(anyDouble());
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);

        // Act and Assert
        assertThrows(InvalidArgumentException.class, () -> pizzaService.addPizza(pizza));
        verify(pizza, atLeast(1)).getName();
        verify(pizza).getPrice();
        verify(pizza).setId(isA(Long.class));
        verify(pizza).setIngredients(eq("Ingredients"));
        verify(pizza).setName(eq("Name"));
        verify(pizza).setPrice(eq(10.0d));
        verify(pizzaRepository).findPizzaByName(eq("Name"));
    }

    /**
     * Method under test: {@link PizzaService#updatePizzaById(Long, Pizza)}
     */
    @Test
    void updatePizzaByIdTest() throws InvalidArgumentException, ResourceNotFoundException {
        // Arrange
        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);
        Optional<Pizza> ofResult = Optional.of(pizza);

        Pizza pizza2 = new Pizza();
        pizza2.setId(1L);
        pizza2.setIngredients("Ingredients");
        pizza2.setName("Name");
        pizza2.setPrice(10.0d);
        when(pizzaRepository.save(Mockito.<Pizza>any())).thenReturn(pizza2);
        when(pizzaRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        PizzaDTO pizzaDTO = new PizzaDTO(1L, "Name", "Ingredients", 10.0d);

        when(pizzaDTOMapper.apply(Mockito.<Pizza>any())).thenReturn(pizzaDTO);

        Pizza newPizza = new Pizza();
        newPizza.setId(1L);
        newPizza.setIngredients("Ingredients");
        newPizza.setName("Name");
        newPizza.setPrice(10.0d);

        // Act
        PizzaDTO actualUpdatePizzaByIdResult = pizzaService.updatePizzaById(1L, newPizza);

        // Assert
        verify(pizzaDTOMapper).apply(isA(Pizza.class));
        verify(pizzaRepository).findById(isA(Long.class));
        verify(pizzaRepository).save(isA(Pizza.class));
        assertSame(pizzaDTO, actualUpdatePizzaByIdResult);
    }

    /**
     * Method under test: {@link PizzaService#updatePizzaById(Long, Pizza)}
     */
    @Test
    void updatePizzaByIdTest2() throws InvalidArgumentException, ResourceNotFoundException {
        // Arrange
        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);
        Optional<Pizza> ofResult = Optional.of(pizza);

        Pizza pizza2 = new Pizza();
        pizza2.setId(1L);
        pizza2.setIngredients("Ingredients");
        pizza2.setName("Name");
        pizza2.setPrice(10.0d);
        when(pizzaRepository.save(Mockito.<Pizza>any())).thenReturn(pizza2);
        when(pizzaRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(pizzaDTOMapper.apply(Mockito.<Pizza>any())).thenThrow(new ResourceNotFoundException("An error occurred"));

        Pizza newPizza = new Pizza();
        newPizza.setId(1L);
        newPizza.setIngredients("Ingredients");
        newPizza.setName("Name");
        newPizza.setPrice(10.0d);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> pizzaService.updatePizzaById(1L, newPizza));
        verify(pizzaDTOMapper).apply(isA(Pizza.class));
        verify(pizzaRepository).findById(isA(Long.class));
        verify(pizzaRepository).save(isA(Pizza.class));
    }

    /**
     * Method under test: {@link PizzaService#updatePizzaById(Long, Pizza)}
     */
    @Test
    void updatePizzaByIdTest3() throws InvalidArgumentException, ResourceNotFoundException {
        // Arrange
        Pizza pizza = mock(Pizza.class);
        when(pizza.getPrice()).thenThrow(new ResourceNotFoundException("An error occurred"));
        when(pizza.getName()).thenReturn("Name");
        doNothing().when(pizza).setId(Mockito.<Long>any());
        doNothing().when(pizza).setIngredients(Mockito.<String>any());
        doNothing().when(pizza).setName(Mockito.<String>any());
        doNothing().when(pizza).setPrice(anyDouble());
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);
        Optional<Pizza> ofResult = Optional.of(pizza);
        when(pizzaRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Pizza newPizza = new Pizza();
        newPizza.setId(1L);
        newPizza.setIngredients("Ingredients");
        newPizza.setName("Name");
        newPizza.setPrice(10.0d);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> pizzaService.updatePizzaById(1L, newPizza));
        verify(pizza).getName();
        verify(pizza).getPrice();
        verify(pizza).setId(isA(Long.class));
        verify(pizza).setIngredients(eq("Ingredients"));
        verify(pizza).setName(eq("Name"));
        verify(pizza).setPrice(eq(10.0d));
        verify(pizzaRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link PizzaService#updatePizzaById(Long, Pizza)}
     */
    @Test
    void updatePizzaByIdTest4() throws InvalidArgumentException, ResourceNotFoundException {
        // Arrange
        Pizza pizza = mock(Pizza.class);
        when(pizza.getPrice()).thenReturn(-0.5d);
        when(pizza.getName()).thenReturn("Name");
        doNothing().when(pizza).setId(Mockito.<Long>any());
        doNothing().when(pizza).setIngredients(Mockito.<String>any());
        doNothing().when(pizza).setName(Mockito.<String>any());
        doNothing().when(pizza).setPrice(anyDouble());
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);
        Optional<Pizza> ofResult = Optional.of(pizza);
        when(pizzaRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Pizza newPizza = new Pizza();
        newPizza.setId(1L);
        newPizza.setIngredients("Ingredients");
        newPizza.setName("Name");
        newPizza.setPrice(10.0d);

        // Act and Assert
        assertThrows(InvalidArgumentException.class, () -> pizzaService.updatePizzaById(1L, newPizza));
        verify(pizza).getName();
        verify(pizza).getPrice();
        verify(pizza).setId(isA(Long.class));
        verify(pizza).setIngredients(eq("Ingredients"));
        verify(pizza).setName(eq("Name"));
        verify(pizza).setPrice(eq(10.0d));
        verify(pizzaRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link PizzaService#updatePizzaById(Long, Pizza)}
     */
    @Test
    void updatePizzaByIdTest5() throws InvalidArgumentException, ResourceNotFoundException {
        // Arrange
        Optional<Pizza> emptyResult = Optional.empty();
        when(pizzaRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        Pizza newPizza = new Pizza();
        newPizza.setId(1L);
        newPizza.setIngredients("Ingredients");
        newPizza.setName("Name");
        newPizza.setPrice(10.0d);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> pizzaService.updatePizzaById(1L, newPizza));
        verify(pizzaRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link PizzaService#updatePizzaByPizzaName(String, Pizza)}
     */
    @Test
    void updatePizzaByPizzaNameTest() throws ResourceNotFoundException {
        // Arrange
        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);
        Optional<Pizza> ofResult = Optional.of(pizza);

        Pizza pizza2 = new Pizza();
        pizza2.setId(1L);
        pizza2.setIngredients("Ingredients");
        pizza2.setName("Name");
        pizza2.setPrice(10.0d);
        when(pizzaRepository.save(Mockito.<Pizza>any())).thenReturn(pizza2);
        when(pizzaRepository.findPizzaByName(Mockito.<String>any())).thenReturn(ofResult);
        PizzaDTO pizzaDTO = new PizzaDTO(1L, "Name", "Ingredients", 10.0d);

        when(pizzaDTOMapper.apply(Mockito.<Pizza>any())).thenReturn(pizzaDTO);

        Pizza newPizza = new Pizza();
        newPizza.setId(1L);
        newPizza.setIngredients("Ingredients");
        newPizza.setName("Name");
        newPizza.setPrice(10.0d);

        // Act
        PizzaDTO actualUpdatePizzaByPizzaNameResult = pizzaService.updatePizzaByPizzaName("Pizza Name", newPizza);

        // Assert
        verify(pizzaDTOMapper).apply(isA(Pizza.class));
        verify(pizzaRepository).findPizzaByName(eq("Pizza Name"));
        verify(pizzaRepository).save(isA(Pizza.class));
        assertSame(pizzaDTO, actualUpdatePizzaByPizzaNameResult);
    }

    /**
     * Method under test: {@link PizzaService#updatePizzaByPizzaName(String, Pizza)}
     */
    @Test
    void updatePizzaByPizzaNameTest2() throws ResourceNotFoundException {
        // Arrange
        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);
        Optional<Pizza> ofResult = Optional.of(pizza);

        Pizza pizza2 = new Pizza();
        pizza2.setId(1L);
        pizza2.setIngredients("Ingredients");
        pizza2.setName("Name");
        pizza2.setPrice(10.0d);
        when(pizzaRepository.save(Mockito.<Pizza>any())).thenReturn(pizza2);
        when(pizzaRepository.findPizzaByName(Mockito.<String>any())).thenReturn(ofResult);
        when(pizzaDTOMapper.apply(Mockito.<Pizza>any())).thenThrow(new ResourceNotFoundException("An error occurred"));

        Pizza newPizza = new Pizza();
        newPizza.setId(1L);
        newPizza.setIngredients("Ingredients");
        newPizza.setName("Name");
        newPizza.setPrice(10.0d);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> pizzaService.updatePizzaByPizzaName("Pizza Name", newPizza));
        verify(pizzaDTOMapper).apply(isA(Pizza.class));
        verify(pizzaRepository).findPizzaByName(eq("Pizza Name"));
        verify(pizzaRepository).save(isA(Pizza.class));
    }

    /**
     * Method under test: {@link PizzaService#updatePizzaByPizzaName(String, Pizza)}
     */
    @Test
    void updatePizzaByPizzaNameTest3() throws ResourceNotFoundException {
        // Arrange
        Pizza pizza = mock(Pizza.class);
        when(pizza.getPrice()).thenThrow(new ResourceNotFoundException("An error occurred"));
        when(pizza.getName()).thenReturn("Name");
        doNothing().when(pizza).setId(Mockito.<Long>any());
        doNothing().when(pizza).setIngredients(Mockito.<String>any());
        doNothing().when(pizza).setName(Mockito.<String>any());
        doNothing().when(pizza).setPrice(anyDouble());
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);
        Optional<Pizza> ofResult = Optional.of(pizza);
        when(pizzaRepository.findPizzaByName(Mockito.<String>any())).thenReturn(ofResult);

        Pizza newPizza = new Pizza();
        newPizza.setId(1L);
        newPizza.setIngredients("Ingredients");
        newPizza.setName("Name");
        newPizza.setPrice(10.0d);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> pizzaService.updatePizzaByPizzaName("Pizza Name", newPizza));
        verify(pizza).getName();
        verify(pizza).getPrice();
        verify(pizza).setId(isA(Long.class));
        verify(pizza).setIngredients(eq("Ingredients"));
        verify(pizza).setName(eq("Name"));
        verify(pizza).setPrice(eq(10.0d));
        verify(pizzaRepository).findPizzaByName(eq("Pizza Name"));
    }

    /**
     * Method under test: {@link PizzaService#updatePizzaByPizzaName(String, Pizza)}
     */
    @Test
    void updatePizzaByPizzaNameTest4() throws ResourceNotFoundException {
        // Arrange
        Pizza pizza = mock(Pizza.class);
        when(pizza.getPrice()).thenReturn(-0.5d);
        when(pizza.getName()).thenReturn("Name");
        doNothing().when(pizza).setId(Mockito.<Long>any());
        doNothing().when(pizza).setIngredients(Mockito.<String>any());
        doNothing().when(pizza).setName(Mockito.<String>any());
        doNothing().when(pizza).setPrice(anyDouble());
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);
        Optional<Pizza> ofResult = Optional.of(pizza);
        when(pizzaRepository.findPizzaByName(Mockito.<String>any())).thenReturn(ofResult);

        Pizza newPizza = new Pizza();
        newPizza.setId(1L);
        newPizza.setIngredients("Ingredients");
        newPizza.setName("Name");
        newPizza.setPrice(10.0d);

        // Act and Assert
        assertThrows(InvalidArgumentException.class, () -> pizzaService.updatePizzaByPizzaName("Pizza Name", newPizza));
        verify(pizza).getName();
        verify(pizza).getPrice();
        verify(pizza).setId(isA(Long.class));
        verify(pizza).setIngredients(eq("Ingredients"));
        verify(pizza).setName(eq("Name"));
        verify(pizza).setPrice(eq(10.0d));
        verify(pizzaRepository).findPizzaByName(eq("Pizza Name"));
    }

    /**
     * Method under test: {@link PizzaService#updatePizzaByPizzaName(String, Pizza)}
     */
    @Test
    void updatePizzaByPizzaNameTest5() throws ResourceNotFoundException {
        // Arrange
        Optional<Pizza> emptyResult = Optional.empty();
        when(pizzaRepository.findPizzaByName(Mockito.<String>any())).thenReturn(emptyResult);

        Pizza newPizza = new Pizza();
        newPizza.setId(1L);
        newPizza.setIngredients("Ingredients");
        newPizza.setName("Name");
        newPizza.setPrice(10.0d);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> pizzaService.updatePizzaByPizzaName("Pizza Name", newPizza));
        verify(pizzaRepository).findPizzaByName(eq("Pizza Name"));
    }

    /**
     * Method under test: {@link PizzaService#deletePizzaById(Long)}
     */
    @Test
    void deletePizzaByIdTest() throws ResourceNotFoundException {
        // Arrange
        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);
        Optional<Pizza> ofResult = Optional.of(pizza);
        doNothing().when(pizzaRepository).deleteById(Mockito.<Long>any());
        when(pizzaRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act
        pizzaService.deletePizzaById(1L);

        // Assert that nothing has changed
        verify(pizzaRepository).deleteById(isA(Long.class));
        verify(pizzaRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link PizzaService#deletePizzaById(Long)}
     */
    @Test
    void deletePizzaByIdTest2() throws ResourceNotFoundException {
        // Arrange
        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);
        Optional<Pizza> ofResult = Optional.of(pizza);
        doThrow(new ResourceNotFoundException("An error occurred")).when(pizzaRepository).deleteById(Mockito.<Long>any());
        when(pizzaRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> pizzaService.deletePizzaById(1L));
        verify(pizzaRepository).deleteById(isA(Long.class));
        verify(pizzaRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link PizzaService#deletePizzaById(Long)}
     */
    @Test
    void deletePizzaByIdTest3() throws ResourceNotFoundException {
        // Arrange
        Optional<Pizza> emptyResult = Optional.empty();
        when(pizzaRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> pizzaService.deletePizzaById(1L));
        verify(pizzaRepository).findById(isA(Long.class));
    }

    /**
     * Method under test: {@link PizzaService#deletePizzaByPizzaName(String)}
     */
    @Test
    void deletePizzaByPizzaNameTest() {
        // Arrange
        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);
        Optional<Pizza> ofResult = Optional.of(pizza);
        when(pizzaRepository.deletePizzaByName(Mockito.<String>any())).thenReturn(1);
        when(pizzaRepository.findPizzaByName(Mockito.<String>any())).thenReturn(ofResult);

        // Act
        pizzaService.deletePizzaByPizzaName("Pizza Name");

        // Assert that nothing has changed
        verify(pizzaRepository).deletePizzaByName(eq("Pizza Name"));
        verify(pizzaRepository).findPizzaByName(eq("Pizza Name"));
    }

    /**
     * Method under test: {@link PizzaService#deletePizzaByPizzaName(String)}
     */
    @Test
    void deletePizzaByPizzaNameTest2() {
        // Arrange
        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setIngredients("Ingredients");
        pizza.setName("Name");
        pizza.setPrice(10.0d);
        Optional<Pizza> ofResult = Optional.of(pizza);
        when(pizzaRepository.deletePizzaByName(Mockito.<String>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));
        when(pizzaRepository.findPizzaByName(Mockito.<String>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> pizzaService.deletePizzaByPizzaName("Pizza Name"));
        verify(pizzaRepository).deletePizzaByName(eq("Pizza Name"));
        verify(pizzaRepository).findPizzaByName(eq("Pizza Name"));
    }

    /**
     * Method under test: {@link PizzaService#deletePizzaByPizzaName(String)}
     */
    @Test
    void deletePizzaByPizzaNameTest3() {
        // Arrange
        Optional<Pizza> emptyResult = Optional.empty();
        when(pizzaRepository.findPizzaByName(Mockito.<String>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> pizzaService.deletePizzaByPizzaName("Pizza Name"));
        verify(pizzaRepository).findPizzaByName(eq("Pizza Name"));
    }
}
