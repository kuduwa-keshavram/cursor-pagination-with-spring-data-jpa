package com.examples.pagination;

import static org.assertj.core.api.Assertions.assertThat;

import com.examples.pagination.customer.Customer;
import com.examples.pagination.pagination.CursorPaging;
import com.fasterxml.jackson.databind.JsonNode;
import com.examples.pagination.customer.CustomerService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class PaginationApplicationTests {

  public static final int TOTAL_RECORDS = 1000;
  @LocalServerPort private Integer port;

  @Autowired private TestRestTemplate restTemplate;
  @Autowired private CustomerService customerService;

  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

  @BeforeAll
  static void beforeAll() {
    postgres.start();
  }

  @AfterAll
  static void afterAll() {
    postgres.stop();
  }

  @BeforeEach
  void beforeEach() {
    customerService.deleteAll();
    addAllCustomersToDatabase();
  }

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
  }

  @Test
  void getAllCustomers() {
    String baseUrl = getBaseUrl();
    JsonNode resp1 = this.restTemplate.getForObject(baseUrl + "/customers", JsonNode.class);
    assertThat(resp1.hasNonNull("previousPageCursor")).isFalse();
    assertThat(resp1.hasNonNull("nextPageCursor")).isTrue();
    assertThat(resp1.get("nextPageCursor").asText()).isNotBlank();
    assertThat(resp1.get("content")).isNotEmpty().hasSize(CursorPaging.DEFAULT_PAGE_SIZE);

    int customPageSize = 10;
    JsonNode resp2 =
        this.restTemplate.getForObject(
            baseUrl + "/customers?pageSize=" + customPageSize, JsonNode.class);
    assertThat(resp2.hasNonNull("previousPageCursor")).isFalse();
    assertThat(resp2.hasNonNull("nextPageCursor")).isTrue();

    String nextPageCursor = resp2.get("nextPageCursor").asText();
    assertThat(nextPageCursor).isNotBlank();
    assertThat(resp2.get("content")).isNotEmpty().hasSize(customPageSize);

    JsonNode resp3 =
        this.restTemplate.getForObject(
            baseUrl + "/customers?bookmark=" + nextPageCursor, JsonNode.class);
    assertThat(resp3.hasNonNull("previousPageCursor")).isTrue();
    String previousPageCursor = resp3.get("previousPageCursor").asText();
    assertThat(previousPageCursor).isNotBlank();
    assertThat(resp3.hasNonNull("nextPageCursor")).isTrue();
    assertThat(resp3.get("content")).isNotEmpty().hasSize(customPageSize);
    assertThat(resp2.get("content").toString()).isNotEqualTo(resp3.get("content").toString());

    JsonNode resp4 =
        this.restTemplate.getForObject(
            baseUrl + "/customers?bookmark=" + previousPageCursor, JsonNode.class);
    assertThat(resp4.hasNonNull("previousPageCursor")).isFalse();
    assertThat(resp4.hasNonNull("nextPageCursor")).isTrue();
    assertThat(resp4.get("content")).isNotEmpty().hasSize(customPageSize);
    assertThat(resp2.get("content").toString()).isEqualTo(resp4.get("content").toString());
  }

  private String getBaseUrl() {
    return "http://localhost:" + port;
  }

  private void addAllCustomersToDatabase() {
    List<Customer> customers = new ArrayList<>();
    IntStream.range(0, TOTAL_RECORDS)
        .forEach(
            index -> {
              String name = RandomStringUtils.randomAlphabetic(10);
              String email = name + "@" + RandomStringUtils.randomAlphabetic(3) + ".com";
              Customer customer = Customer.builder().name(name).email(email).build();
              customers.add(customer);
            });
    customerService.saveAll(customers);
  }
}
