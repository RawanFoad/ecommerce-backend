package com.cgm.integration;

import com.cgm.ecommerce.config.TenantContext;
import com.cgm.ecommerce.domain.Customer;
import com.cgm.ecommerce.domain.Product;
import com.cgm.ecommerce.domain.Order;
import com.cgm.ecommerce.domain.OrderItem;
import com.cgm.ecommerce.repository.CustomerRepository;
import com.cgm.ecommerce.repository.ProductRepository;
import com.cgm.ecommerce.repository.OrderDao;
import com.cgm.ecommerce.repository.OrderItemDao;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TenantIsolationTest {

    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.4")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("pass");

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderDao orderRepository;
    @Autowired
    private OrderItemDao orderItemRepository;

    @BeforeAll
    public void setUp() {
        postgres.start();
    }

    @AfterAll
    public void tearDown() {
        postgres.stop();
    }

    @BeforeEach
    public void clearTenant() {
        TenantContext.clear();
    }

    @Test
    public void testTenantIsolation() {
        // Tenant A
        TenantContext.setCurrentTenant("tenant_a");

        Customer customerA = new Customer(UUID.randomUUID(), "tenant_a", "Alice", "alice@example.com");
        customerRepository.save(customerA);

        Product productA = new Product(UUID.randomUUID(), "tenant_a", "SKU-A", "Product A", "Desc", BigDecimal.valueOf(10), 100);
        productRepository.save(productA);

        Order orderA = new Order(UUID.randomUUID(), "tenant_a", customerA, "NEW", BigDecimal.valueOf(10));
        orderRepository.save(orderA);

        OrderItem orderItemA = new OrderItem(UUID.randomUUID(), orderA.getId(), productA.getId(), 1);
        orderItemRepository.save(orderItemA);

        // Tenant B
        TenantContext.setCurrentTenant("tenant_b");

        Customer customerB = new Customer(UUID.randomUUID(), "tenant_b", "Bob", "bob@example.com");
        customerRepository.save(customerB);

        Product productB = new Product(UUID.randomUUID(), "tenant_b", "SKU-B", "Product B", "Desc", BigDecimal.valueOf(20), 50);
        productRepository.save(productB);

        Order orderB = new Order(UUID.randomUUID(), "tenant_b", customerB, "NEW", BigDecimal.valueOf(20));
        orderRepository.save(orderB);

        OrderItem orderItemB = new OrderItem(UUID.randomUUID(), orderB.getId(), productB.getId(), 2);
        orderItemRepository.save(orderItemB);

        // Assertions: tenant isolation
        TenantContext.setCurrentTenant("tenant_a");
        List<Customer> customersTenantA = customerRepository.findAll();
        assertThat(customersTenantA).hasSize(1).extracting(Customer::getName).containsExactly("Alice");

        TenantContext.setCurrentTenant("tenant_b");
        List<Customer> customersTenantB = customerRepository.findAll();
        assertThat(customersTenantB).hasSize(1).extracting(Customer::getName).containsExactly("Bob");
    }
}
