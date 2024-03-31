package com.itfactory.service;

import com.itfactory.dao.*;
import com.itfactory.dao.JobDao;
import com.itfactory.dao.JobDaoTest;
import com.itfactory.exceptions.DatabaseOperationException;
import com.itfactory.model.Job;
import com.itfactory.model.Job;
import com.itfactory.model.Person;
import com.itfactory.model.WorkExperience;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;

import javax.xml.crypto.Data;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Writing JUnit tests for each of the 6 service methods in JobService class;
 * Included - tested scenarios where invalid id values are being passed in JobService methods;
 */

@SpringBootTest
public class JobServiceTest {

    @Autowired
    JobService jobService;

    @Test
    public void getJobById() throws DatabaseOperationException {

        int existentId = JobDaoTest.generateExistentTestId();

        assertDoesNotThrow(() -> jobService.getJobById(existentId));

        Job foundTestJobById = jobService.getJobById(existentId);

        assertNotNull(foundTestJobById);
        assertEquals(existentId, foundTestJobById.getId());
    }

    @Test
    public void findJobByInvalidIdTest (){

        assertThrows(DatabaseOperationException.class,
                () -> jobService.getJobById(JobDaoTest.generateInvalidTestId()));
    }

    @Test
    public void deleteJobTest () throws DatabaseOperationException {

        Job testJob = new Job();
        testJob.setId(JobDaoTest.generateInvalidTestId());
        testJob.setName("Test Job");

        jobService.insertJob(testJob);

        jobService.deleteJob(testJob.getId());

        assertThrows(DatabaseOperationException.class,
                () -> jobService.getJobById(testJob.getId()));
    }

    @Test
    public void deleteJobByInvalidIdTest (){

        assertThrows(DatabaseOperationException.class,
                () -> jobService.deleteJob(JobDaoTest.generateInvalidTestId()));
    }

    @Test
    public void getAllJobsTest () throws DatabaseOperationException {

        int actualListSize = jobService.getAllJobs().size();

        Job testJob1 = new Job();
        testJob1.setId(JobDaoTest.generateInvalidTestId());
        testJob1.setName("Test Job1");
        jobService.insertJob(testJob1);

        Job testJob2 = new Job();
        testJob2.setId(JobDaoTest.generateInvalidTestId());
        testJob2.setName("Test Job2");
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
        testJob.setId(JobDaoTest.generateInvalidTestId());
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
               () -> jobService.insertJob(jobService.getJobById(JobDaoTest.generateExistentTestId()))
       );
    }

    @Test
    public void updateBaseSalaryTest () throws DatabaseOperationException {

        Job testJob = new Job();
        testJob.setId(JobDaoTest.generateInvalidTestId());
        testJob.setName("Test Job");
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

        assertThrows(DatabaseOperationException.class,
                () -> jobService.updateBaseSalary(JobDaoTest.generateInvalidTestId(), 2000));

        Job testJob = new Job();
        testJob.setId(JobDaoTest.generateInvalidTestId());
        testJob.setName("Test Job");
        testJob.setBaseSalary(2000);

        jobService.insertJob(testJob);

        assertThrows(DatabaseOperationException.class,
                () -> jobService.updateBaseSalary(testJob.getId(), 2000));

        jobService.deleteJob(testJob.getId());
    }

    
    @Test
    void calculateSalaryValidInputsTest() {

        Person testPerson = new Person();
        testPerson.setSalaryIndex(1.5);

        Job testJob = new Job();
        testJob.setBaseSalary(5000);

        assertEquals(7500, jobService.calculateSalary(testPerson, testJob));
    }

    @Test
    void calculateSalaryInvalidInputsTest() {

        assertThrows(DatabaseOperationException.class,
                () -> jobService.calculateSalary(
                        new PersonService(new PersonDao()).getPersonById(PersonDaoTest.generateInvalidTestId()),
                        jobService.getJobById(JobDaoTest.generateInvalidTestId())
                )
        );
    }
}