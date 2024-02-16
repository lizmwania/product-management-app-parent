package com.produductmanagementapp.productservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.produductmanagementapp.productservice.dto.ProductRequest;
import com.produductmanagementapp.productservice.dto.ProductResponse;
import com.produductmanagementapp.productservice.model.Product;
import com.produductmanagementapp.productservice.repository.ProductRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
//import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {
	@Container
	static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(
			"postgres:15-alpine")
			.withUsername("postgres")
			.withPassword("postgres")
			.withDatabaseName("productmanagement");
//	static KafkaContainer kafka = new KafkaContainer(
//			DockerImageName.parse("confluentinc/cp-kafka:7.5.0"));
	@BeforeAll
	static  void startContainers(){
		postgreSQLContainer.start();
	}

	@AfterAll
	static  void stopContainers(){
		postgreSQLContainer.stop();
	}

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ProductRepository productRepository;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
		registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
		registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
		registry.add("spring.datasource.driver-class-name", postgreSQLContainer::getDriverClassName);
	}

	@BeforeEach
	void setUp(){
		for (int i = 1; i <= 5; i++) {
			Product product = new Product();
			product.setName("Test Product " + i);
			product.setDescription("Description " + i);
			product.setPrice(BigDecimal.valueOf(100));

			productRepository.save(product);
		}
	}
	@Test
	void shouldCreateProduct() throws Exception {
		ProductRequest productRequest = getProductRequest();
		String productRequestString = objectMapper.writeValueAsString(productRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
						.contentType(MediaType.APPLICATION_JSON)
						.content(productRequestString))
				.andExpect(status().isCreated());
        //Assertions.assertEquals(1, productRepository.findAll().size());
	}

	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("Dell Laptop")
				.description("Latitute 5032")
				.price(BigDecimal.valueOf(1200))
				.build();
	}
	@Test
	void shouldGetAllProducts() throws Exception {
		// Perform a GET request to your endpoint for fetching all products
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/product")
						.contentType(MediaType.APPLICATION_JSON))
				// Verify that the response status is 200 OK
				.andExpect(status().isOk())
				// Verify that the response content type is JSON
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		// Extract response content as String
		String responseBody = result.getResponse().getContentAsString();

		// Parse JSON response to an array of ProductResponse objects
		ObjectMapper objectMapper = new ObjectMapper();
		ProductResponse[] products = objectMapper.readValue(responseBody, ProductResponse[].class);

		// Verify that the response contains a JSON array with a size of 5 (assuming 5 test products)
		//assertThat(products, hasSize(5));

		// Iterate over each product and verify its attributes
		for (int i = 0; i < 5; i++) {
			ProductResponse product = products[i];
			assertEquals("Test Product " + (i+1), product.getName());
			assertEquals("Description " + (i+1), product.getDescription());
			//assertEquals(BigDecimal.valueOf(100 * (i+1)), product.getPrice());
			assertEquals(BigDecimal.valueOf(100).setScale(2), product.getPrice().setScale(2));

			assertNotNull(product.getId()); // Ensure that id is not null
		}

	}


}
