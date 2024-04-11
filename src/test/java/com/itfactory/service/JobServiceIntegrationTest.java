package com.itfactory.service;

import com.itfactory.dao.*;
import com.itfactory.dao.JobDaoIntegrationTest;
import com.itfactory.exceptions.DatabaseOperationException;
import com.itfactory.model.Job;
import com.itfactory.model.Person;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * Writing JUnit tests for each of the 6 service methods in JobService class;
 * Included - tested scenarios where invalid id values are being passed in JobService methods;
 * Similar testing methodology to JobDaoIntegrationTest, as described in comments;
 */

@SpringBootTest
public class JobServiceIntegrationTest {

    @Autowired
    private JobService jobService;

    @Test
    public void getJobById() throws DatabaseOperationException {

        int existentId = JobDaoIntegrationTest.generateExistentTestId();

        assertDoesNotThrow(() -> jobService.getJobById(existentId));

        Job foundTestJobById = jobService.getJobById(existentId);

        assertNotNull(foundTestJobById);
        assertEquals(existentId, foundTestJobById.getId());
    }

    @Test
    public void getJobByInvalidIdTest (){

        assertThrows(DatabaseOperationException.class,
                () -> jobService.getJobById(JobDaoIntegrationTest.generateInvalidTestId()));
    }

    @Test
    public void deleteJobTest () throws DatabaseOperationException {

        Job testJob = new Job();
        testJob.setId(JobDaoIntegrationTest.generateInvalidTestId());
        testJob.setName("Test Job");
        testJob.setDomain("Test Domain");

        jobService.insertJob(testJob);

        jobService.deleteJob(testJob.getId());

        assertThrows(DatabaseOperationException.class,
                () -> jobService.getJobById(testJob.getId()));
    }

    @Test
    public void deleteJobByInvalidIdTest (){

        assertThrows(DatabaseOperationException.class,
                () -> jobService.deleteJob(JobDaoIntegrationTest.generateInvalidTestId()));
    }

    @Test
    public void getAllJobsTest () throws DatabaseOperationException {

        int actualListSize = jobService.getAllJobs().size();

        Job testJob1 = new Job();
        testJob1.setId(JobDaoIntegrationTest.generateInvalidTestId());
        testJob1.setName("Test Job One");
        testJob1.setDomain("Test Domain");
        jobService.insertJob(testJob1);

        Job testJob2 = new Job();
        testJob2.setId(JobDaoIntegrationTest.generateInvalidTestId());
        testJob2.setName("Test Job Two");
        testJob2.setDomain("Test Domain");
        jobService.insertJob(testJob2);

        List<Job> testJobsInserted = jobService.getAllJobs();

        assertNotNull(testJobsInserted);
        assertEquals(actualListSize + 2, testJobsInserted.size());

        jobService.deleteJob(testJob1.getId());
        jobService.deleteJob(testJob2.getId());
    }

    @Test
    public void insertJobTest () throws DatabaseOperationException {

        Job testJob = new Job();
        testJob.setId(JobDaoIntegrationTest.generateInvalidTestId());
        testJob.setName("Test Job");
        testJob.setDomain("Test Domain");
        testJob.setBaseSalary(2000);

        jobService.insertJob(testJob);

        Job foundInsertedJob = jobService.getJobById(testJob.getId());

        assertNotNull(foundInsertedJob);
        assertEquals(testJob.getId(), foundInsertedJob.getId());
        assertEquals(testJob.getName(), foundInsertedJob.getName());
        assertEquals(testJob.getDomain(), foundInsertedJob.getDomain());
        assertEquals(testJob.getBaseSalary(), foundInsertedJob.getBaseSalary());

        jobService.deleteJob(testJob.getId());
    }

    @Test
    public void insertJobInvalidInputTest (){

       assertThrows(DatabaseOperationException.class,
               () -> jobService.insertJob(jobService.getJobById(JobDaoIntegrationTest.generateExistentTestId()))
       );
    }

    @Test
    public void updateBaseSalaryTest () throws DatabaseOperationException {

        Job testJob = new Job();
        testJob.setId(JobDaoIntegrationTest.generateInvalidTestId());
        testJob.setName("Test Job");
        testJob.setDomain("Test Domain");
        testJob.setBaseSalary(1000);
        double newBaseSalary = 2000;

        jobService.insertJob(testJob);

        jobService.updateBaseSalary(testJob.getId(), newBaseSalary);

        Job foundInsertedJob = jobService.getJobById(testJob.getId());
        assertEquals(newBaseSalary, foundInsertedJob.getBaseSalary());

        jobService.deleteJob(testJob.getId());
    }

    @Test
    public void updateBaseSalaryInvalidInputTest () throws DatabaseOperationException {

        //invalid scenario 1 - update base salary of invalid id;
        assertThrows(DatabaseOperationException.class,
                () -> jobService.updateBaseSalary(JobDaoIntegrationTest.generateInvalidTestId(), 2000));

        //invalid scenario 2 - update base salary of valid id, but existing base salary;
        Job testJob = new Job();
        testJob.setId(JobDaoIntegrationTest.generateInvalidTestId());
        testJob.setName("Test Job");
        testJob.setDomain("Test Domain");
        testJob.setBaseSalary(2000);

        jobService.insertJob(testJob);

        assertThrows(DatabaseOperationException.class,
                () -> jobService.updateBaseSalary(testJob.getId(), 2000));

        jobService.deleteJob(testJob.getId());
    }

    //create test person and test job, assert correct calculation of salary;
    //invalid scenarios - using a person with invalid salary index and using a job with invalid base salary, asserting exception throwing;
    @Test
    void calculateSalaryValidInputsTest() throws DatabaseOperationException {

        Person testPerson = new Person();
        testPerson.setSalaryIndex(1.5);

        Job testJob = new Job();
        testJob.setBaseSalary(5000);

        assertEquals(7500, jobService.calculateSalary(testPerson, testJob));
    }

    @Test
    void calculateSalaryInvalidInputsTest() throws DatabaseOperationException {

        Person testPerson = new Person();
        testPerson.setId(PersonDaoIntegrationTest.generateInvalidTestId());
        testPerson.setSalaryIndex(5);

        Job testJob = new Job();
        testJob.setId(JobDaoIntegrationTest.generateInvalidTestId());
        testJob.setBaseSalary(300);

        assertThrows(DatabaseOperationException.class,
                () -> jobService.calculateSalary(
                        testPerson,
                        jobService.getJobById(JobDaoIntegrationTest.generateExistentTestId())
                )
        );

        assertThrows(DatabaseOperationException.class,
                () -> jobService.calculateSalary(
                        new PersonService(new PersonDao()).getPersonById(PersonDaoIntegrationTest.generateExistentTestId()),
                        testJob
                )
        );
    }
}