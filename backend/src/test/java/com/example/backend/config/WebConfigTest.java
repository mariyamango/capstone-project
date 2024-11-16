package com.example.backend.config;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@AutoConfigureMockMvc
@SpringBootTest
class WebConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testRedirectCarId() throws Exception {
        mockMvc.perform(get("/car/123"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testRedirectAbout() throws Exception {
        mockMvc.perform(get("/about"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testRedirectHome() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}