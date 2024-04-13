package com.itfactory.controller;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Writing a mock test for the WelcomeController class;
 * Testing for the functionality of the API, accessing the welcome-index Thymeleaf template;
 */

@SpringBootTest
@AutoConfigureMockMvc
class WelcomeControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void welcomeMessageTest() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("welcome-index"));
    }
}