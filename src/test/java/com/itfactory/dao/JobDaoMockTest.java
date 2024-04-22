package com.itfactory.dao;

import com.itfactory.exceptions.DatabaseOperationException;
import com.itfactory.model.Job;
import com.itfactory.utility.TestIdGenerator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Writing JUnit MOCK (mocked beans and behaviours) tests for each of the 5 database manipulation methods (CRUD) in JobDao class;
 * Included - invalid scenarios;
 * Similar testing methodology to PersonDaoMockTest (as described in the comments);
 */

@SpringBootTest
class JobDaoMockTest {

    //creating a mock instance of the JobDao class, to be used for the tests;
    //with every test, a new Bean of jobDao gets created, thus resetting its mocked behaviour;
    @MockBean
    private JobDao jobDao;

    @Test
    public void getJobByIdMockTest() throws DatabaseOperationException {

        int existentId = TestIdGenerator.generateExistentTestId();
        Job mockJob = new Job();
        mockJob.setId(existentId);
        mockJob.setName("Test Mock Job");

        when(jobDao.getJobById(existentId)).thenReturn(mockJob);

        Job foundJob = jobDao.getJobById(existentId);

        assertNotNull(foundJob);
        assertEquals(existentId, foundJob.getId());

        verify(jobDao, times(1)).getJobById(existentId);
        verifyNoMoreInteractions(jobDao);
    }

    @Test
    public void getJobByIdInvalidMockTest() throws DatabaseOperationException {

        int invalidId = TestIdGenerator.generateInvalidTestId();

        doThrow(DatabaseOperationException.class).when(jobDao).getJobById(invalidId);

        assertThrows(DatabaseOperationException.class, () -> jobDao.getJobById(invalidId));

        verify(jobDao, times(1)).getJobById(invalidId);
        verifyNoMoreInteractions(jobDao);
    }

    @Test
    public void deleteJobMockTest() throws DatabaseOperationException {

        Job mockJob = new Job();
        mockJob.setId(TestIdGenerator.generateInvalidTestId());
        mockJob.setName("Test Job");

        jobDao.insertJob(mockJob);

        jobDao.deleteJob(mockJob.getId());

        verify(jobDao, times(1)).insertJob(mockJob);
        verify(jobDao, times(1)).deleteJob(mockJob.getId());
        verifyNoMoreInteractions(jobDao);
    }

    @Test
    public void deleteJobInvalidIdMockTest() throws DatabaseOperationException {

        int invalidId = TestIdGenerator.generateInvalidTestId();

        doThrow(DatabaseOperationException.class).when(jobDao).deleteJob(invalidId);

        assertThrows(DatabaseOperationException.class, () -> jobDao.deleteJob(invalidId));

        verify(jobDao, times(1)).deleteJob(invalidId);
        verifyNoMoreInteractions(jobDao);
    }

    @Test
    public void getAllJobsMockTest() throws DatabaseOperationException {

        Job mockJob1 = new Job();
        mockJob1.setId(TestIdGenerator.generateExistentTestId());
        mockJob1.setName("Test Mock Job One");

        Job mockJob2 = new Job();
        mockJob2.setId(TestIdGenerator.generateExistentTestId() - 1);
        mockJob2.setName("Test Mock Job Two");

        List<Job> testJobsInserted = new ArrayList<>(List.of(mockJob1, mockJob2));

        when(jobDao.getAllJobs()).thenReturn(testJobsInserted);

        List<Job> getAllResult = jobDao.getAllJobs();

        assertNotNull(getAllResult);
        assertEquals(testJobsInserted.size(), getAllResult.size());
        assertTrue(getAllResult.containsAll(testJobsInserted));

        verify(jobDao, times(1)).getAllJobs();
        verifyNoMoreInteractions(jobDao);
    }

    @Test
    public void insertJobMockTest() throws DatabaseOperationException {

        Job mockJob = new Job();
        mockJob.setId(TestIdGenerator.generateInvalidTestId());
        mockJob.setName("Test Mock Job");
        mockJob.setDomain("mock@email.com");
        mockJob.setBaseSalary(3000);

        jobDao.insertJob(mockJob);

        when(jobDao.getJobById(mockJob.getId())).thenReturn(mockJob);

        Job foundInsertedJob = jobDao.getJobById(mockJob.getId());

        assertNotNull(foundInsertedJob);
        assertEquals(mockJob.getId(), foundInsertedJob.getId());
        assertEquals(mockJob.getName(), foundInsertedJob.getName());
        assertEquals(mockJob.getDomain(), foundInsertedJob.getDomain());
        assertEquals(mockJob.getBaseSalary(), foundInsertedJob.getBaseSalary());

        verify(jobDao, times(1)).insertJob(mockJob);
        verify(jobDao, times(1)).getJobById(mockJob.getId());
        verifyNoMoreInteractions(jobDao);
    }

    @Test
    public void insertJobInvalidMockTest() throws DatabaseOperationException {

        Job mockJob = new Job();
        mockJob.setId(TestIdGenerator.generateExistentTestId());

        doThrow(DatabaseOperationException.class).when(jobDao).insertJob(mockJob);

        assertThrows(DatabaseOperationException.class, () -> jobDao.insertJob(mockJob));

        verify(jobDao, times(1)).insertJob(mockJob);
        verifyNoMoreInteractions(jobDao);
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

        when(jobDao.updateBaseSalary(mockJob.getId(), newBaseSalary)).thenReturn(updatedJob);

        Job retrievedUpdatedJob = jobDao.updateBaseSalary(mockJob.getId(), newBaseSalary);

        assertEquals(newBaseSalary, retrievedUpdatedJob.getBaseSalary());

        verify(jobDao, times(1)).updateBaseSalary(mockJob.getId(), newBaseSalary);
        verifyNoMoreInteractions(jobDao);
    }

    @Test
    public void updateBaseSalaryInvalidMockTest() throws DatabaseOperationException {

        int invalidId = TestIdGenerator.generateInvalidTestId();

        Job mockJob = new Job();
        mockJob.setBaseSalary(3000);
        double existingBaseSalary = 3000;

        doThrow(DatabaseOperationException.class).when(jobDao).updateBaseSalary(invalidId, existingBaseSalary);
        doThrow(DatabaseOperationException.class).when(jobDao).updateBaseSalary(mockJob.getId(), existingBaseSalary);

        assertThrows(DatabaseOperationException.class, () -> jobDao.updateBaseSalary(invalidId, existingBaseSalary));
        assertThrows(DatabaseOperationException.class, () -> jobDao.updateBaseSalary(mockJob.getId(), existingBaseSalary));

        verify(jobDao, times(1)).updateBaseSalary(invalidId, existingBaseSalary);
        verify(jobDao, times(1)).updateBaseSalary(mockJob.getId(), existingBaseSalary);
        verifyNoMoreInteractions(jobDao);
    }
}