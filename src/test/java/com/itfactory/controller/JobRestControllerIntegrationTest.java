package com.itfactory.controller;

import com.itfactory.exceptions.DatabaseOperationException;
import com.itfactory.model.Job;
import com.itfactory.utility.TestIdGenerator;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Writing JUnit INTEGRATION tests for each of the 5 CRUD JobRestController methods;
 * Further testing can be done on the local server, verifying accessibility through endpoints - using Postman;
 * Slightly different testing methodology than JobDaoIntegrationTest, with valid and invalid scenarios in a single complete method;
 * Asserting by ResponseEntity<String> response body;
 */

@SpringBootTest
class JobRestControllerIntegrationTest {

    @Autowired
    private JobRestController jobRestController;

    @Test
    public void getJobByIdCompleteTest () throws DatabaseOperationException {

        int existentId = TestIdGenerator.generateExistentTestId();

        assertDoesNotThrow(() -> jobRestController.getJobById(String.valueOf(existentId)));

        assertTrue(jobRestController.getJobById(String.valueOf(existentId)).toString().contains("successfully"));

        assertTrue(jobRestController.getJobById(
                String.valueOf(TestIdGenerator.generateInvalidTestId())).toString().contains("Failed"));
    }

    @Test
    public void deleteJobCompleteTest () throws DatabaseOperationException {

        Job testJob = new Job();
        testJob.setId(TestIdGenerator.generateInvalidTestId());
        testJob.setName("Test Job");
        testJob.setDomain("Test Domain");
        testJob.setBaseSalary(2000);

        jobRestController.insertJob(testJob);

        assertTrue(jobRestController.deleteJob(String.valueOf(testJob.getId())).toString().contains("successfully"));
        assertTrue(jobRestController.getJobById(String.valueOf(testJob.getId())).toString().contains("Failed"));

        assertTrue(jobRestController.deleteJob(String.valueOf(testJob.getId())).toString().contains("Failed"));
    }

    @Test
    public void getAllJobsTest () throws DatabaseOperationException {

        Job testJob1 = new Job();
        testJob1.setId(TestIdGenerator.generateInvalidTestId());
        testJob1.setName("Test Job One");
        testJob1.setDomain("Test Domain");
        jobRestController.insertJob(testJob1);

        Job testJob2 = new Job();
        testJob2.setId(TestIdGenerator.generateInvalidTestId() - 1);
        testJob2.setName("Test Job Two");
        testJob2.setDomain("Test Domain");
        jobRestController.insertJob(testJob2);

        assertTrue(jobRestController.getAllJobs().toString().contains("successfully"));

        jobRestController.deleteJob(String.valueOf(testJob1.getId()));
        jobRestController.deleteJob(String.valueOf(testJob2.getId()));
    }

    @Test
    public void insertJobCompleteTest () throws DatabaseOperationException {

        Job testJob = new Job();
        testJob.setId(TestIdGenerator.generateInvalidTestId());
        testJob.setName("Test Job");
        testJob.setDomain("Test Domain");
        testJob.setBaseSalary(2000);

        assertTrue(jobRestController.insertJob(testJob).toString()
                .contains("successfully"));

        assertTrue(jobRestController.insertJob(testJob).toString()
                .contains("Failed"));

        jobRestController.deleteJob(String.valueOf(testJob.getId()));
    }

    @Test
    public void testUpdateBaseSalaryCompleteTest () throws DatabaseOperationException {

        Job testJob = new Job();
        testJob.setId(TestIdGenerator.generateInvalidTestId());
        testJob.setName("Test Job");
        testJob.setDomain("Test Domain");
        testJob.setBaseSalary(2000);
        double newBaseSalary = 2500;

        jobRestController.insertJob(testJob);

        assertTrue(jobRestController.updateBaseSalary(String.valueOf(testJob.getId()), String.valueOf(newBaseSalary))
                .toString().contains("successfully"));

        assertTrue(jobRestController.updateBaseSalary(String.valueOf(testJob.getId()), String.valueOf(newBaseSalary))
                .toString().contains("Failed"));

        jobRestController.deleteJob(String.valueOf(testJob.getId()));

        assertTrue(jobRestController.updateBaseSalary(String.valueOf(testJob.getId()), String.valueOf(newBaseSalary))
                .toString().contains("Failed"));
    }

}