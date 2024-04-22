package com.itfactory.dao;

import com.itfactory.exceptions.DatabaseOperationException;
import com.itfactory.model.Job;
import com.itfactory.utility.TestIdGenerator;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Writing JUnit tests for each of the 5 database manipulation methods (CRUD) in JobDao class;
 * Included - connection test and invalid scenarios;
 * Using static helper methods to generate existent and non-existent database id;
 * Similar testing methodology to PersonDaoIntegrationTest, as described in comments;
 */

@SpringBootTest
public class JobDaoIntegrationTest {

    @Autowired
    private JobDao jobDao;

    @Test
    public void getJobByIdTest() throws DatabaseOperationException {

        int existentId = TestIdGenerator.generateExistentTestId();

        assertDoesNotThrow(() -> jobDao.getJobById(existentId));

        Job foundTestJobById = jobDao.getJobById(existentId);

        assertNotNull(foundTestJobById);
        assertEquals(existentId, foundTestJobById.getId());
    }

    @Test
    public void getJobByIdInvalidTest() {

        assertThrows(DatabaseOperationException.class,
                () -> jobDao.getJobById(TestIdGenerator.generateInvalidTestId()));
    }

    @Test
    public void deleteJobTest() throws DatabaseOperationException {

        Job testJob = new Job();
        testJob.setId(TestIdGenerator.generateInvalidTestId());
        testJob.setName("Test Job");
        testJob.setDomain("Test Job Domain");
        testJob.setBaseSalary(3000);

        jobDao.insertJob(testJob);

        jobDao.deleteJob(testJob.getId());

        assertThrows(DatabaseOperationException.class,
                () -> jobDao.getJobById(testJob.getId()));
    }

    @Test
    public void deleteJobInvalidTest() {

        assertThrows(DatabaseOperationException.class,
                () -> jobDao.deleteJob(TestIdGenerator.generateInvalidTestId()));
    }

    @Test
    public void getAllJobsTest() throws DatabaseOperationException {

        int actualListSize = jobDao.getAllJobs().size();

        Job testJob1 = new Job();
        testJob1.setId(TestIdGenerator.generateInvalidTestId());
        testJob1.setName("Test Job 1");
        testJob1.setDomain("Test Job 1 Domain");
        testJob1.setBaseSalary(3000);
        jobDao.insertJob(testJob1);

        Job testJob2 = new Job();
        testJob2.setId(TestIdGenerator.generateInvalidTestId() - 1);
        testJob2.setName("Test Job 2");
        testJob2.setDomain("Test Job 2 Domain");
        testJob2.setBaseSalary(4000);
        jobDao.insertJob(testJob2);

        List<Job> testJobsInserted = jobDao.getAllJobs();

        assertNotNull(testJobsInserted);
        assertEquals(actualListSize + 2, testJobsInserted.size());

        jobDao.deleteJob(testJob1.getId());
        jobDao.deleteJob(testJob2.getId());
    }

    @Test
    public void insertJobTest() throws DatabaseOperationException {

        Job testJob = new Job();
        testJob.setId(TestIdGenerator.generateInvalidTestId());
        testJob.setName("Test Job");
        testJob.setDomain("Test Domain");
        testJob.setBaseSalary(1000);

        jobDao.insertJob(testJob);

        Job foundInsertedJob = jobDao.getJobById(testJob.getId());

        assertNotNull(foundInsertedJob);
        assertEquals(testJob.getId(), foundInsertedJob.getId());
        assertEquals(testJob.getName(), foundInsertedJob.getName());
        assertEquals(testJob.getDomain(), foundInsertedJob.getDomain());
        assertEquals(testJob.getBaseSalary(), foundInsertedJob.getBaseSalary());

        jobDao.deleteJob(testJob.getId());
    }

    @Test
    public void insertJobInvalidTest() throws DatabaseOperationException {

        Job testJob = new Job();
        testJob.setId(TestIdGenerator.generateExistentTestId());

        assertThrows(DatabaseOperationException.class, () -> jobDao.insertJob(testJob));
    }

    @Test
    public void updateBaseSalaryTest() throws DatabaseOperationException {

        Job testJob = new Job();
        testJob.setId(TestIdGenerator.generateInvalidTestId());
        testJob.setName("Test Job");
        testJob.setDomain("Test Job Domain");
        testJob.setBaseSalary(1000);
        double newBaseSalary = 2000;

        jobDao.insertJob(testJob);

        jobDao.updateBaseSalary(testJob.getId(), newBaseSalary);

        Job foundInsertedJob = jobDao.getJobById(testJob.getId());
        assertEquals(newBaseSalary, foundInsertedJob.getBaseSalary());

        jobDao.deleteJob(testJob.getId());
    }

    @Test
    public void updateBaseSalaryInvalidTest() {

        assertThrows(DatabaseOperationException.class,
                () -> jobDao.updateBaseSalary(TestIdGenerator.generateInvalidTestId(), 4000));
    }

    @Test
    public void connectionTest() {

        try {

            Connection connection = DriverManager.getConnection("testUrl");

            PreparedStatement statement = connection.prepareStatement("DELETE FROM job WHERE id = ?");

            statement.setInt(1, 1);

            statement.execute();

        } catch(SQLException e){

            assertEquals("No suitable driver found for testUrl", e.getMessage());
        }
    }
}