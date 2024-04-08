package com.itfactory.controller;

import com.itfactory.dao.JobDaoTest;
import com.itfactory.exceptions.DatabaseOperationException;
import com.itfactory.model.Job;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Testing the functionality of JobRestController methods;
 * Further testing can be done on the local server, verifying accessibility through endpoints - using Postman;
 */

@SpringBootTest
class JobRestControllerTest {

    @Autowired
    private JobRestController jobRestController;

    @Test
    public void findJobByIdCompleteTest () throws DatabaseOperationException {

        int existentId = JobDaoTest.generateExistentTestId();

        assertDoesNotThrow(() -> jobRestController.getJobById(String.valueOf(existentId)));

        assertTrue(jobRestController.getJobById(String.valueOf(existentId)).toString().contains("successfully"));

        assertTrue(jobRestController.getJobById(String.valueOf(JobDaoTest.generateInvalidTestId())).toString().contains("Failed"));
    }

    @Test
    public void deleteJobCompleteTest () throws DatabaseOperationException {

        Job testJob = new Job();
        testJob.setId(JobDaoTest.generateInvalidTestId());
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
        testJob1.setId(JobDaoTest.generateInvalidTestId());
        testJob1.setName("Test Job One");
        testJob1.setDomain("Test Domain");
        jobRestController.insertJob(testJob1);

        Job testJob2 = new Job();
        testJob2.setId(JobDaoTest.generateInvalidTestId());
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
        testJob.setId(JobDaoTest.generateInvalidTestId());
        testJob.setName("Test Job");
        testJob.setDomain("Test Domain");
        testJob.setBaseSalary(2000);

        assertTrue(jobRestController.insertJob(testJob).toString().contains("successfully"));

        assertTrue(jobRestController.insertJob(testJob).toString().contains("Failed"));

        jobRestController.deleteJob(String.valueOf(testJob.getId()));
    }

    @Test
    public void testUpdateBaseSalary () throws DatabaseOperationException {

        Job testJob = new Job();
        testJob.setId(JobDaoTest.generateInvalidTestId());
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