package com.itfactory.controller;

import com.itfactory.dao.JobDaoTest;
import com.itfactory.dao.PersonDaoTest;
import com.itfactory.exceptions.DatabaseOperationException;
import com.itfactory.model.Job;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

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

        assertDoesNotThrow(() -> jobRestController.getJobById(existentId));

        assertTrue(jobRestController.getJobById(existentId).toString().contains("successfully"));

        assertTrue(jobRestController.getJobById(PersonDaoTest.generateInvalidTestId()).toString().contains("Failed"));
    }

    @Test
    public void deleteJobCompleteTest () throws DatabaseOperationException {

        Job testJob = new Job();
        testJob.setId(JobDaoTest.generateInvalidTestId());
        testJob.setName("Test Job");

        jobRestController.insertJob(testJob);

        assertTrue(jobRestController.deleteJob(testJob.getId()).toString().contains("successfully"));
        assertTrue(jobRestController.getJobById(testJob.getId()).toString().contains("Failed"));

        assertTrue(jobRestController.deleteJob(testJob.getId()).toString().contains("Failed"));
    }

    @Test
    public void getAllJobsTest () throws DatabaseOperationException {

        Job testJob1 = new Job();
        testJob1.setId(JobDaoTest.generateInvalidTestId());
        testJob1.setName("Test Job1");
        jobRestController.insertJob(testJob1);

        Job testJob2 = new Job();
        testJob2.setId(JobDaoTest.generateInvalidTestId());
        testJob2.setName("Test Job2");
        jobRestController.insertJob(testJob2);

        assertTrue(jobRestController.getAllJobs().toString().contains("successfully"));

        jobRestController.deleteJob(testJob1.getId());
        jobRestController.deleteJob(testJob2.getId());
    }

    @Test
    public void insertJobCompleteTest () throws DatabaseOperationException {

        Job testJob = new Job();
        testJob.setId(JobDaoTest.generateInvalidTestId());
        testJob.setName("Test Job");

        assertTrue(jobRestController.insertJob(testJob).toString().contains("successfully"));

        assertTrue(jobRestController.insertJob(testJob).toString().contains("Failed"));

        jobRestController.deleteJob(testJob.getId());
    }

    @Test
    public void testUpdateBaseSalary () throws DatabaseOperationException {

        Job testJob = new Job();
        testJob.setId(JobDaoTest.generateInvalidTestId());
        testJob.setName("Test Job");
        testJob.setBaseSalary(2000);
        double newBaseSalary = 2500;

        jobRestController.insertJob(testJob);

        assertTrue(jobRestController.updateBaseSalary(testJob.getId(), newBaseSalary)
                .toString().contains("successfully"));

        assertTrue(jobRestController.updateBaseSalary(testJob.getId(), newBaseSalary)
                .toString().contains("Failed"));

        jobRestController.deleteJob(testJob.getId());

        assertTrue(jobRestController.updateBaseSalary(testJob.getId(), newBaseSalary)
                .toString().contains("Failed"));
    }

}