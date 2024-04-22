package com.itfactory.controller;

import com.itfactory.exceptions.DatabaseOperationException;
import com.itfactory.model.Job;
import com.itfactory.service.JobService;
import com.itfactory.utility.TestIdGenerator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Writing JUnit MOCK tests for each of the 5 CRUD controller methods in JobRestController class;
 * Included - tested scenarios where invalid id values are being passed in JobRestController methods;
 * Similar testing methodology to JobDaoMockTest (as described in comments);
 * Asserting with the ResponseEntity<String>, by both status code and String returned;
 */

@SpringBootTest
class JobRestControllerMockTest {

    //mocking the jobService, then creating the JobRestController instance using it, before each test;
    @Mock
    private JobService jobService;

    private JobRestController jobRestController;

    @BeforeEach
    void setUp() {

        jobRestController = new JobRestController(jobService);
    }

    @Test
    public void getJobByIdMockTest() throws DatabaseOperationException {

        int existentId = TestIdGenerator.generateExistentTestId();
        Job mockJob = new Job();
        mockJob.setId(existentId);
        mockJob.setName("Test Mock Job");

        when(jobService.getJobById(existentId)).thenReturn(mockJob);

        ResponseEntity<String> mockResponse = jobRestController.getJobById(String.valueOf(mockJob.getId()));

        assertEquals(HttpStatus.OK, mockResponse.getStatusCode());
        assertTrue(mockResponse.getBody().contains("successfully"));

        verify(jobService, times(1)).getJobById(existentId);
        verifyNoMoreInteractions(jobService);
    }

    @Test
    public void getJobByIdInvalidMockTest() throws DatabaseOperationException {

        int invalidId = TestIdGenerator.generateInvalidTestId();

        doThrow(DatabaseOperationException.class).when(jobService).getJobById(invalidId);

        ResponseEntity<String> mockResponse = jobRestController.getJobById(String.valueOf(invalidId));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, mockResponse.getStatusCode());
        assertTrue(mockResponse.getBody().contains("Failed"));

        verify(jobService, times(1)).getJobById(invalidId);
        verifyNoMoreInteractions(jobService);
    }

    @Test
    public void deleteJobMockTest() throws DatabaseOperationException {

        Job mockJob = new Job();
        mockJob.setId(TestIdGenerator.generateInvalidTestId());
        mockJob.setName("Test Mock Job");
        mockJob.setDomain("Mock Job Domain");
        mockJob.setBaseSalary(TestIdGenerator.generateExistentTestId());

        ResponseEntity<String> insertMockResponse = jobRestController.insertJob(mockJob);

        ResponseEntity<String> deleteMockResponse = jobRestController.deleteJob(String.valueOf(mockJob.getId()));

        assertEquals(HttpStatus.OK, insertMockResponse.getStatusCode());
        assertTrue(insertMockResponse.getBody().contains("successfully"));

        assertEquals(HttpStatus.OK, deleteMockResponse.getStatusCode());
        assertTrue(deleteMockResponse.getBody().contains("successfully"));

        verify(jobService, times(1)).insertJob(mockJob);
        verify(jobService, times(1)).deleteJob(mockJob.getId());
        verifyNoMoreInteractions(jobService);
    }

    @Test
    public void deleteJobByIdInvalidMockTest() throws DatabaseOperationException {

        int invalidId = TestIdGenerator.generateInvalidTestId();

        doThrow(DatabaseOperationException.class).when(jobService).deleteJob(invalidId);

        ResponseEntity<String> mockResponse = jobRestController.deleteJob(String.valueOf(invalidId));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, mockResponse.getStatusCode());
        assertTrue(mockResponse.getBody().contains("Failed"));

        verify(jobService, times(1)).deleteJob(invalidId);
        verifyNoMoreInteractions(jobService);
    }

    @Test
    public void getAllJobsMockTest() throws DatabaseOperationException {

        Job mockJob1 = new Job();
        mockJob1.setId(TestIdGenerator.generateExistentTestId());

        Job mockJob2 = new Job();
        mockJob2.setId(TestIdGenerator.generateExistentTestId() + 1);

        List<Job> testJobsInserted = new ArrayList<>(List.of(mockJob1, mockJob2));

        when(jobService.getAllJobs()).thenReturn(testJobsInserted);

        ResponseEntity<String> mockResponse = jobRestController.getAllJobs();

        assertEquals(HttpStatus.OK, mockResponse.getStatusCode());
        assertTrue(mockResponse.getBody().contains("successfully"));

        verify(jobService, times(1)).getAllJobs();
        verifyNoMoreInteractions(jobService);
    }

    @Test
    public void insertJobMockTest() throws DatabaseOperationException {

        Job mockJob = new Job();
        mockJob.setId(TestIdGenerator.generateInvalidTestId());
        mockJob.setName("Mock Job");
        mockJob.setDomain("Mock Job Domain");
        mockJob.setBaseSalary(3000);

        ResponseEntity<String> insertMockResponse = jobRestController.insertJob(mockJob);

        when(jobService.getJobById(mockJob.getId())).thenReturn(mockJob);

        ResponseEntity<String> getMockResponse = jobRestController.getJobById(String.valueOf(mockJob.getId()));

        assertEquals(HttpStatus.OK, insertMockResponse.getStatusCode());
        assertTrue(insertMockResponse.getBody().contains("successfully"));

        assertEquals(HttpStatus.OK, getMockResponse.getStatusCode());
        assertTrue(getMockResponse.getBody().contains("successfully"));

        verify(jobService, times(1)).insertJob(mockJob);
        verify(jobService, times(1)).getJobById(mockJob.getId());
        verifyNoMoreInteractions(jobService);
    }

    @Test
    public void insertJobInvalidIdMockTest() throws DatabaseOperationException {

        Job mockJob = new Job();
        mockJob.setId(TestIdGenerator.generateExistentTestId());
        mockJob.setName("Mock Job");
        mockJob.setDomain("Mock Job Domain");
        mockJob.setBaseSalary(3000);

        doThrow(DatabaseOperationException.class).when(jobService).insertJob(mockJob);

        ResponseEntity<String> mockResponse = jobRestController.insertJob(mockJob);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, mockResponse.getStatusCode());
        assertTrue(mockResponse.getBody().contains("Failed"));

        verify(jobService, times(1)).insertJob(mockJob);
        verifyNoMoreInteractions(jobService);
    }

    @Test
    public void updateBaseSalaryMockTest() throws DatabaseOperationException {

        Job mockJob = new Job();
        mockJob.setId(TestIdGenerator.generateExistentTestId());
        mockJob.setBaseSalary(3000);
        double newBaseSalary = 4000;

        Job updatedJob = new Job();
        updatedJob.setId(mockJob.getId());
        updatedJob.setBaseSalary(newBaseSalary);

        when(jobService.updateBaseSalary(mockJob.getId(), newBaseSalary)).thenReturn(updatedJob);

        ResponseEntity<String> mockResponse = jobRestController.updateBaseSalary(
                String.valueOf(mockJob.getId()), String.valueOf(newBaseSalary));

        assertEquals(HttpStatus.OK, mockResponse.getStatusCode());
        assertTrue(mockResponse.getBody().contains("successfully"));

        verify(jobService, times(1)).updateBaseSalary(mockJob.getId(), newBaseSalary);
        verifyNoMoreInteractions(jobService);
    }

    @Test
    public void updateBaseSalaryInvalidMockTest() throws DatabaseOperationException {

        int invalidId = TestIdGenerator.generateInvalidTestId();

        Job mockJob = new Job();
        mockJob.setId(TestIdGenerator.generateExistentTestId());
        mockJob.setBaseSalary(3000);
        double existingBaseSalary = 3000;

        doThrow(DatabaseOperationException.class).when(jobService).updateBaseSalary(invalidId, existingBaseSalary);

        ResponseEntity<String> mockResponseInvalidId = jobRestController.updateBaseSalary(
                String.valueOf(invalidId), String.valueOf(existingBaseSalary));

        doThrow(DatabaseOperationException.class).when(jobService).updateBaseSalary(mockJob.getId(), existingBaseSalary);

        ResponseEntity<String> mockResponseSameSalaryIndex = jobRestController.updateBaseSalary(
                String.valueOf(mockJob.getId()), String.valueOf(existingBaseSalary));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, mockResponseInvalidId.getStatusCode());
        assertTrue(mockResponseInvalidId.getBody().contains("Failed"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, mockResponseSameSalaryIndex.getStatusCode());
        assertTrue(mockResponseSameSalaryIndex.getBody().contains("Failed"));

        verify(jobService, times(1)).updateBaseSalary(invalidId, existingBaseSalary);
        verify(jobService, times(1)).updateBaseSalary(mockJob.getId(), existingBaseSalary);
        verifyNoMoreInteractions(jobService);
    }
}