package com.itfactory.controller;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Writing a mock test for the JobInfoController class;
 * Testing for the functionality of the API, accessing the job-index Thymeleaf template;
 */

@SpringBootTest
@AutoConfigureMockMvc
class JobInfoControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getJobInfoTest() throws Exception {
        mockMvc.perform(get("/job-info"))
                .andExpect(status().isOk())
                .andExpect(view().name("job-index"));
    }
}