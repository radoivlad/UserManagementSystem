package com.itfactory.model;

import com.itfactory.dao.JobDao;
import com.itfactory.dao.JobDaoTest;
import com.itfactory.exceptions.DatabaseOperationException;
import com.itfactory.service.JobService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;
import org.mockito.Mock;

import org.springframework.boot.test.context.SpringBootTest;

/**
 * Writing mock tests for PersonManager class;
 */

@SpringBootTest
class PersonManagerMockTest {

    @Mock
    private JobDao jobDao;
    @Mock
    private JobService jobService;
    @Mock
    private Person person;
    private PersonManager personManager;


    @BeforeEach
    void setUp() throws DatabaseOperationException {

        person.setJobId(JobDaoTest.generateExistentTestId());

        personManager = new PersonManager(person);
        personManager.jobDao = jobDao;
        personManager.jobService = jobService;
    }

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

        person.setJobId(JobDaoTest.generateInvalidTestId());

        when(jobDao.getJobById(person.getJobId())).thenThrow(DatabaseOperationException.class);

        assertThrows(DatabaseOperationException.class, () -> personManager.getJob());

        verify(jobDao, times(1)).getJobById(person.getJobId());
        verifyNoMoreInteractions(jobDao);
    }

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

        person.setJobId(JobDaoTest.generateInvalidTestId());

        when(jobDao.getJobById(person.getJobId())).thenThrow(DatabaseOperationException.class);

        assertThrows(DatabaseOperationException.class, () -> personManager.getSalary());

        verify(jobDao, times(1)).getJobById(person.getJobId());
        verifyNoMoreInteractions(jobDao);
    }

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