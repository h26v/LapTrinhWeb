package vn.iotstar;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Bt7ApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void canSearchAndPaginateProducts() throws Exception {
        mockMvc.perform(get("/api/product")
                        .param("page", "0")
                        .param("size", "2")
                        .param("search", "Watch"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.items.length()").value(1))
                .andExpect(jsonPath("$.data.items[0].productName").value("Smart Watch Series 5"))
                .andExpect(jsonPath("$.data.totalPages").value(1));
    }

    @Test
    @Order(2)
    void canCrudProduct() throws Exception {
        String product = """
                {"productName":"API Test Product","images":"","unitPrice":20.00,"discount":5,
                 "description":"Created by MockMvc","categoryId":1,"quantity":5,"status":1}
                """;

        String location = mockMvc.perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(product))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andReturn().getResponse().getContentAsString();
        Number idNumber = com.jayway.jsonpath.JsonPath.read(location, "$.data.productId");
        long id = idNumber.longValue();

        mockMvc.perform(put("/api/product/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(product.replace("API Test Product", "API Test Product Updated")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.productName").value("API Test Product Updated"));

        mockMvc.perform(delete("/api/product/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @Order(3)
    void preventsDeletingCategoryWithProducts() throws Exception {
        mockMvc.perform(delete("/api/category/1"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false));
    }
}
