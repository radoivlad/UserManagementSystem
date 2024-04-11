package com.itfactory.service;

import com.itfactory.dao.JobDao;
import com.itfactory.dao.JobDaoIntegrationTest;
import com.itfactory.exceptions.DatabaseOperationException;
import com.itfactory.model.Job;
import com.itfactory.model.Person;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * Writing JUnit MOCK tests for each of the 6 service methods in JobService class;
 * Included - tested scenarios where invalid id values are being passed in JobService methods;
 * Similar testing methodology to JobDaoMockTest (as described in comments);
 */

@SpringBootTest
class JobServiceMockTest {

    //a JobDao object is mocked, then used to instantiate a JobService object;
    @Mock
    private JobDao jobDao;

    private JobService jobService;

    //before each test method, jobService is instantiated, using the mocked jobDao;
    //every test method assumes mocked methods of jobDao, then using their mocked behaviours to call jobService methods;
    @BeforeEach
    void setUp() {

        jobService = new JobService(jobDao);
    }

    @Test
    public void getJobByIdMockTest() throws DatabaseOperationException {

        int existentId = JobDaoIntegrationTest.generateExistentTestId();
        Job mockJob = new Job();
        mockJob.setId(existentId);
        mockJob.setName("Mock Job");

        when(jobDao.getJobById(existentId)).thenReturn(mockJob);

        Job foundJob = jobService.getJobById(existentId);

        assertNotNull(foundJob);
        assertEquals(existentId, foundJob.getId());

        verify(jobDao, times(1)).getJobById(existentId);
        verifyNoMoreInteractions(jobDao);
    }

    @Test
    public void getJobByIdInvalidMockTest () throws DatabaseOperationException {

        int invalidId = JobDaoIntegrationTest.generateInvalidTestId();

        doThrow(DatabaseOperationException.class).when(jobDao).getJobById(invalidId);

        assertThrows(DatabaseOperationException.class, () -> jobService.getJobById(invalidId));

        verify(jobDao, times(1)).getJobById(invalidId);
        verifyNoMoreInteractions(jobDao);
    }

    @Test
    public void deleteJobMockTest() throws DatabaseOperationException {

        Job mockJob = new Job();
        mockJob.setId(JobDaoIntegrationTest.generateInvalidTestId());
        mockJob.setName("Mock Job");
        mockJob.setDomain("Mock Job Domain");
        mockJob.setBaseSalary(3000);

        jobService.insertJob(mockJob);

        when(jobDao.getJobById(mockJob.getId())).thenReturn(mockJob);

        jobService.deleteJob(mockJob.getId());

        verify(jobDao, times(1)).insertJob(mockJob);
        verify(jobDao, times(1)).deleteJob(mockJob.getId());
        verify(jobDao, times(1)).getJobById(mockJob.getId());
        verifyNoMoreInteractions(jobDao);
    }

    @Test
    public void deleteJobByIdInvalidMockTest () throws DatabaseOperationException {

        int invalidId = JobDaoIntegrationTest.generateInvalidTestId();

        doThrow(DatabaseOperationException.class).when(jobDao).getJobById(invalidId);

        assertThrows(DatabaseOperationException.class, () -> jobService.deleteJob(invalidId));

        verify(jobDao, times(1)).getJobById(invalidId);
        verify(jobDao, never()).deleteJob(invalidId);
        verifyNoMoreInteractions(jobDao);
    }

    @Test
    public void getAllJobsMockTest() throws DatabaseOperationException {

        Job mockJob1 = new Job();
        mockJob1.setId(JobDaoIntegrationTest.generateExistentTestId());

        Job mockJob2 = new Job();
        mockJob2.setId(JobDaoIntegrationTest.generateExistentTestId());

        List<Job> testJobsInserted = new ArrayList<>(List.of(mockJob1, mockJob2));

        when(jobDao.getAllJobs()).thenReturn(testJobsInserted);

        List<Job> getAllResult = jobService.getAllJobs();

        assertNotNull(getAllResult);
        assertEquals(testJobsInserted.size(), getAllResult.size());
        assertTrue(getAllResult.containsAll(testJobsInserted));

        verify(jobDao, times(1)).getAllJobs();
        verifyNoMoreInteractions(jobDao);
    }

    @Test
    public void insertJobMockTest () throws DatabaseOperationException {

        Job mockJob = new Job();
        mockJob.setId(JobDaoIntegrationTest.generateInvalidTestId());
        mockJob.setName("Mock Job");
        mockJob.setDomain("Mock Job Domain");
        mockJob.setBaseSalary(3000);

        jobService.insertJob(mockJob);

        when(jobDao.getJobById(mockJob.getId())).thenReturn(mockJob);

        Job foundInsertedJob = jobService.getJobById(mockJob.getId());

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
    public void insertJobInvalidIdMockTest () throws DatabaseOperationException {

        Job mockJob = new Job();
        mockJob.setId(JobDaoIntegrationTest.generateExistentTestId());
        mockJob.setName("Mock Job");
        mockJob.setDomain("Mock Job Domain");
        mockJob.setBaseSalary(3000);

        doThrow(DatabaseOperationException.class).when(jobDao).insertJob(mockJob);

        assertThrows(DatabaseOperationException.class, () -> jobService.insertJob(mockJob));

        verify(jobDao, times(1)).insertJob(mockJob);
        verifyNoMoreInteractions(jobDao);
    }

    @Test
    public void updateBaseSalaryMockTest() throws DatabaseOperationException {

        Job mockJob = new Job();
        mockJob.setId(JobDaoIntegrationTest.generateExistentTestId());
        mockJob.setBaseSalary(3000);
        double newBaseSalary = 4000;

        Job updatedJob = new Job();
        updatedJob.setId(mockJob.getId());
        updatedJob.setBaseSalary(newBaseSalary);

        when(jobDao.getJobById(mockJob.getId())).thenReturn(mockJob);
        when(jobDao.updateBaseSalary(mockJob.getId(), newBaseSalary)).thenReturn(updatedJob);

        Job retrievedUpdatedJob = jobService.updateBaseSalary(mockJob.getId(), newBaseSalary);

        assertEquals(newBaseSalary, retrievedUpdatedJob.getBaseSalary());

        verify(jobDao, times(1)).getJobById(mockJob.getId());
        verify(jobDao, times(1)).updateBaseSalary(mockJob.getId(), newBaseSalary);
        verifyNoMoreInteractions(jobDao);
    }

    @Test
    public void updateBaseSalaryInvalidMockTest() throws DatabaseOperationException {

        int invalidId = JobDaoIntegrationTest.generateInvalidTestId();

        Job mockJob = new Job();
        mockJob.setId(JobDaoIntegrationTest.generateExistentTestId());
        mockJob.setBaseSalary(3000);
        double existingBaseSalary = 3000;

        doThrow(DatabaseOperationException.class).when(jobDao).getJobById(invalidId);

        assertThrows(DatabaseOperationException.class, () -> jobService.updateBaseSalary(invalidId, existingBaseSalary));

        when(jobDao.getJobById(mockJob.getId())).thenReturn(mockJob);

        assertThrows(DatabaseOperationException.class, () -> jobService.updateBaseSalary(
                mockJob.getId(), existingBaseSalary));

        verify(jobDao, times(1)).getJobById(mockJob.getId());
        verify(jobDao, times(1)).getJobById(invalidId);
        verify(jobDao, never()).updateBaseSalary(invalidId, 2.0);
        verify(jobDao, never()).updateBaseSalary(mockJob.getId(), existingBaseSalary);
        verifyNoMoreInteractions(jobDao);
    }

    //mock Person and Job objects, including their behaviours, call the tested method, assert equivalence of salaries, verify successful calling;
    //invalid scenarios - assert if exceptions are thrown when using invalid salary index and base salary;
    @Test
    public void calculateSalaryMockTest() throws DatabaseOperationException {

        Person testPerson = mock(Person.class);
        Job testJob = mock(Job.class);

        when(testPerson.getName()).thenReturn("Test person");
        when(testPerson.getSalaryIndex()).thenReturn(2.0);
        when(testJob.getBaseSalary()).thenReturn(1000.0);

        double calculatedSalary = jobService.calculateSalary(testPerson, testJob);

        assertEquals(2000, calculatedSalary);

        verify(testPerson, times(1)).getName();
        verify(testPerson, times(4)).getSalaryIndex();
        verify(testJob, times(3)).getBaseSalary();
        verifyNoMoreInteractions(testPerson, testJob);
    }

    @Test
    public void calculateSalaryInvalidMockTest() {

        Person testPerson = mock(Person.class);
        Job testJob = mock(Job.class);

        //scenario 1 - invalid salary index;
        when(testPerson.getSalaryIndex()).thenReturn(5.0);
        when(testJob.getBaseSalary()).thenReturn(1000.0);

        assertThrows(DatabaseOperationException.class,
                () -> jobService.calculateSalary(testPerson, testJob));

        verify(testPerson, times(2)).getSalaryIndex();
        verify(testJob, never()).getBaseSalary();
        verifyNoMoreInteractions(testPerson, testJob);

        //scenario 2 - invalid base salary;
        when(testPerson.getSalaryIndex()).thenReturn(2.0);
        when(testJob.getBaseSalary()).thenReturn(300.0);

        assertThrows(DatabaseOperationException.class,
                () -> jobService.calculateSalary(testPerson, testJob));

        verify(testPerson, times(4)).getSalaryIndex();
        verify(testJob, times(1)).getBaseSalary();
        verifyNoMoreInteractions(testPerson, testJob);
    }
}