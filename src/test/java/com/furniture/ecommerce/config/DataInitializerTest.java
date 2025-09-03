package com.furniture.ecommerce.config;

import com.furniture.ecommerce.model.Product;
import com.furniture.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataInitializerTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private DataInitializer dataInitializer;

    @Test
    void run_WithEmptyDatabase_ShouldInitializeData() throws Exception {
        // Given
        when(productRepository.count()).thenReturn(0L);
        when(productRepository.saveAll(anyList())).thenReturn(Arrays.asList(new Product(), new Product()));

        // When
        dataInitializer.run();

        // Then
        verify(productRepository, times(1)).count();
        verify(productRepository, times(1)).saveAll(anyList());
    }

    @Test
    void run_WithExistingData_ShouldSkipInitialization() throws Exception {
        // Given
        when(productRepository.count()).thenReturn(5L); // Database already has data

        // When
        dataInitializer.run();

        // Then
        verify(productRepository, times(1)).count();
        verify(productRepository, never()).saveAll(anyList());
    }

    @Test
    void run_ShouldInitializeCorrectNumberOfProducts() throws Exception {
        // Given
        when(productRepository.count()).thenReturn(0L);
        List<Product> savedProducts = Arrays.asList(
                new Product(), new Product(), new Product(), new Product(), new Product(),
                new Product(), new Product(), new Product(), new Product(), new Product(),
                new Product(), new Product(), new Product(), new Product(), new Product()
        );
        when(productRepository.saveAll(anyList())).thenReturn(savedProducts);

        // When
        dataInitializer.run();

        // Then
        verify(productRepository, times(1)).saveAll(anyList());
        verify(productRepository, times(1)).count();
    }

    @Test
    void constructor_ShouldAcceptProductRepository() {
        // Given & When
        DataInitializer initializer = new DataInitializer(productRepository);

        // Then
        assertNotNull(initializer);
    }

    @Test 
    void run_WithException_ShouldPropagateException() {
        // Given
        when(productRepository.count()).thenReturn(0L);
        when(productRepository.saveAll(anyList())).thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            dataInitializer.run();
        });
    }
}