package com.itfactory.model;

import com.itfactory.dao.JobDao;
import com.itfactory.dao.JobDaoIntegrationTest;
import com.itfactory.exceptions.DatabaseOperationException;
import com.itfactory.service.JobService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;

import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Writing mock tests for the 3 methods in PersonManager class;
 */

@SpringBootTest
class PersonManagerMockTest {

    //mocking jobDao, jobService and person, then, using them to initialize personManager, as to be able to mock behaviour;
    @Mock
    private JobDao jobDao;
    @Mock
    private JobService jobService;
    @Mock
    private Person person;

    private PersonManager personManager;

    @BeforeEach
    void setUp() throws DatabaseOperationException {

        person.setJobId(JobDaoIntegrationTest.generateExistentTestId());

        personManager = new PersonManager(person);
        personManager.jobDao = jobDao;
        personManager.jobService = jobService;
    }

    //creating a mock job, mocking jobDao behaviour, calling the tested method, asserting between initial mock job and obtained job;
    //verifying successful calling;
    //invalid scenario - setting invalid job id for person, mocking jobDao to throw exception, asserting it;
    @Test
    public void getJobMockTest() throws DatabaseOperationException {

        Job actualMockJob = new Job();
        actualMockJob.setId(person.getJobId());

        when(jobDao.getJobById(person.getJobId())).thenReturn(actualMockJob);

        Job getMockJob = personManager.getJob();

        assertEquals(getMockJob.getId(), actualMockJob.getId());
        assertEquals(getMockJob.getName(), actualMockJob.getName());
        assertEquals(getMockJob.getDomain(), actualMockJob.getDomain());
        assertEquals(getMockJob.getBaseSalary(), actualMockJob.getBaseSalary());

        verify(jobDao, times(1)).getJobById(person.getJobId());
        verifyNoMoreInteractions(jobDao);
    }

    @Test
    public void getJobInvalidIdMockTest() throws DatabaseOperationException {

        person.setJobId(JobDaoIntegrationTest.generateInvalidTestId());

        when(jobDao.getJobById(person.getJobId())).thenThrow(DatabaseOperationException.class);

        assertThrows(DatabaseOperationException.class, () -> personManager.getJob());

        verify(jobDao, times(1)).getJobById(person.getJobId());
        verifyNoMoreInteractions(jobDao);
    }

    //creating a custom salary, setting salary index for person and base salary for a created mock job, mocking jobService to return custom salary;
    //calling tested method, asserting between custom salary and obtained salary, verifying successful calling;
    //invalid scenario - setting invalid job id for person, mocking jobDao to throw exception (argument in jobService method), asserting it;
    @Test
    public void getSalaryMockTest() throws DatabaseOperationException {

        double actualSalary = 10000;

        person.setSalaryIndex(2.0);

        Job mockJob = new Job();
        mockJob.setId(person.getJobId());
        mockJob.setBaseSalary(5000);

        when(jobService.calculateSalary(person, jobDao.getJobById(person.getJobId()))).thenReturn(actualSalary);

        double getSalary = personManager.getSalary();

        assertEquals(actualSalary, getSalary);

        verify(jobService, times(1)).calculateSalary(person, jobDao.getJobById(person.getJobId()));
        verifyNoMoreInteractions(jobService);
    }

    @Test
    public void getSalaryInvalidIdMockTest() throws DatabaseOperationException {

        person.setJobId(JobDaoIntegrationTest.generateInvalidTestId());

        when(jobDao.getJobById(person.getJobId())).thenThrow(DatabaseOperationException.class);

        assertThrows(DatabaseOperationException.class, () -> personManager.getSalary());

        verify(jobDao, times(1)).getJobById(person.getJobId());
        verifyNoMoreInteractions(jobDao);
    }

    //creating a custom work experience, setting salary index for person, mocking person to return set salary index;
    //calling tested method, asserting between custom work experience and obtained work experience, verifying successful calling;
    //invalid scenario - setting invalid salary index for person, mock person to return set salary index, assert if corresponding message is returned;
    @Test
    public void getWorkExperienceMockTest() {

        person.setSalaryIndex(2.0);

        String actualWorkExperience = "mid level";

        when(person.getSalaryIndex()).thenReturn(2.0);

        String getWorkExperience = personManager.getWorkExperience();

        assertTrue(getWorkExperience.contains(actualWorkExperience));

        verify(person, times(5)).getSalaryIndex();
    }

    @Test
    public void getWorkExperienceInvalidMockTest() {

        double invalidSalaryIndex = 5.0;

        when(person.getSalaryIndex()).thenReturn(invalidSalaryIndex);

        assertTrue(personManager.getWorkExperience().contains("outside of range 1 - 3"));

        verify(person, times(2)).getSalaryIndex();
        verify(person, never()).getName();
    }
}