package com.itfactory.controller;

import com.itfactory.exceptions.DatabaseOperationException;
import com.itfactory.model.Job;
import com.itfactory.model.Person;
import com.itfactory.service.PersonService;
import com.itfactory.utility.TestIdGenerator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Writing JUnit MOCK tests for each of the 8 PersonRestController methods;
 * Included - tested scenarios where invalid id values are being passed in PersonRestController methods;
 * Similar testing methodology to PersonDaoMockTest (as described in comments);
 * Asserting with the ResponseEntity<String>, by both status code and String returned;
 */

@SpringBootTest
class PersonRestControllerMockTest {

    //mocking the personService, then creating the PersonRestController instance using it, before each test;
    @Mock
    private PersonService personService;

    private PersonRestController personRestController;

    @BeforeEach
    void setUp() {

        personRestController = new PersonRestController(personService);
    }

    @Test
    public void getPersonByIdMockTest() throws DatabaseOperationException {

        int existentId = TestIdGenerator.generateExistentTestId();
        Person mockPerson = new Person();
        mockPerson.setId(existentId);
        mockPerson.setName("Test Mock Person");

        when(personService.getPersonById(existentId)).thenReturn(mockPerson);

        ResponseEntity<String> mockResponse = personRestController.getPersonById(String.valueOf(mockPerson.getId()));

        assertEquals(HttpStatus.OK, mockResponse.getStatusCode());
        assertTrue(mockResponse.getBody().contains("successfully"));

        verify(personService, times(1)).getPersonById(existentId);
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void getPersonByIdInvalidMockTest() throws DatabaseOperationException {

        int invalidId = TestIdGenerator.generateInvalidTestId();

        doThrow(DatabaseOperationException.class).when(personService).getPersonById(invalidId);

        ResponseEntity<String> mockResponse = personRestController.getPersonById(String.valueOf(invalidId));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, mockResponse.getStatusCode());
        assertTrue(mockResponse.getBody().contains("Failed"));

        verify(personService, times(1)).getPersonById(invalidId);
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void deletePersonMockTest() throws DatabaseOperationException {

        Person mockPerson = new Person();
        mockPerson.setId(TestIdGenerator.generateInvalidTestId());
        mockPerson.setName("Test Mock Person");
        mockPerson.setEmail("Email Mock Person");
        mockPerson.setJobId(TestIdGenerator.generateExistentTestId());

        ResponseEntity<String> insertMockResponse = personRestController.insertPerson(mockPerson);

        ResponseEntity<String> deleteMockResponse = personRestController.deletePerson(String.valueOf(mockPerson.getId()));

        assertEquals(HttpStatus.OK, insertMockResponse.getStatusCode());
        assertTrue(insertMockResponse.getBody().contains("successfully"));

        assertEquals(HttpStatus.OK, deleteMockResponse.getStatusCode());
        assertTrue(deleteMockResponse.getBody().contains("successfully"));

        verify(personService, times(1)).insertPerson(mockPerson);
        verify(personService, times(1)).deletePerson(mockPerson.getId());
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void deletePersonByIdInvalidMockTest() throws DatabaseOperationException {

        int invalidId = TestIdGenerator.generateInvalidTestId();

        doThrow(DatabaseOperationException.class).when(personService).deletePerson(invalidId);

        ResponseEntity<String> mockResponse = personRestController.deletePerson(String.valueOf(invalidId));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, mockResponse.getStatusCode());
        assertTrue(mockResponse.getBody().contains("Failed"));

        verify(personService, times(1)).deletePerson(invalidId);
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void getAllPersonsMockTest() throws DatabaseOperationException {

        Person mockPerson1 = new Person();
        mockPerson1.setId(TestIdGenerator.generateExistentTestId());

        Person mockPerson2 = new Person();
        mockPerson2.setId(TestIdGenerator.generateExistentTestId() + 1);

        List<Person> testPersonsInserted = new ArrayList<>(List.of(mockPerson1, mockPerson2));

        when(personService.getAllPersons()).thenReturn(testPersonsInserted);

        ResponseEntity<String> mockResponse = personRestController.getAllPersons();

        assertEquals(HttpStatus.OK, mockResponse.getStatusCode());
        assertTrue(mockResponse.getBody().contains("successfully"));

        verify(personService, times(1)).getAllPersons();
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void insertPersonMockTest() throws DatabaseOperationException {

        Person mockPerson = new Person();
        mockPerson.setId(TestIdGenerator.generateInvalidTestId());
        mockPerson.setName("Test Mock Person");
        mockPerson.setEmail("test@email.com");
        mockPerson.setJobId(TestIdGenerator.generateExistentTestId());
        mockPerson.setSalaryIndex(2);

        ResponseEntity<String> insertMockResponse = personRestController.insertPerson(mockPerson);

        when(personService.getPersonById(mockPerson.getId())).thenReturn(mockPerson);

        ResponseEntity<String> getMockResponse = personRestController.getPersonById(String.valueOf(mockPerson.getId()));

        assertEquals(HttpStatus.OK, insertMockResponse.getStatusCode());
        assertTrue(insertMockResponse.getBody().contains("successfully"));

        assertEquals(HttpStatus.OK, getMockResponse.getStatusCode());
        assertTrue(getMockResponse.getBody().contains("successfully"));

        verify(personService, times(1)).insertPerson(mockPerson);
        verify(personService, times(1)).getPersonById(mockPerson.getId());
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void insertPersonInvalidIdMockTest() throws DatabaseOperationException {

        Person mockPerson = new Person();
        mockPerson.setId(TestIdGenerator.generateExistentTestId());
        mockPerson.setName("Test Mock Person");
        mockPerson.setEmail("test@email.com");
        mockPerson.setJobId(TestIdGenerator.generateExistentTestId());
        mockPerson.setSalaryIndex(2);

        doThrow(DatabaseOperationException.class).when(personService).insertPerson(mockPerson);

        ResponseEntity<String> mockResponse = personRestController.insertPerson(mockPerson);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, mockResponse.getStatusCode());
        assertTrue(mockResponse.getBody().contains("Failed"));

        verify(personService, times(1)).insertPerson(mockPerson);
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void updateSalaryIndexMockTest() throws DatabaseOperationException {

        Person mockPerson = new Person();
        mockPerson.setId(TestIdGenerator.generateExistentTestId());
        mockPerson.setSalaryIndex(2);
        double newSalaryIndex = 2.5;

        Person updatedPerson = new Person();
        updatedPerson.setId(mockPerson.getId());
        updatedPerson.setSalaryIndex(newSalaryIndex);

        when(personService.updateSalaryIndex(mockPerson.getId(), newSalaryIndex)).thenReturn(updatedPerson);

        ResponseEntity<String> mockResponse = personRestController.updateSalaryIndex(
                String.valueOf(mockPerson.getId()), String.valueOf(newSalaryIndex));

        assertEquals(HttpStatus.OK, mockResponse.getStatusCode());
        assertTrue(mockResponse.getBody().contains("successfully"));

        verify(personService, times(1)).updateSalaryIndex(mockPerson.getId(), newSalaryIndex);
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void updateSalaryIndexInvalidMockTest() throws DatabaseOperationException {

        int invalidId = TestIdGenerator.generateInvalidTestId();

        Person mockPerson = new Person();
        mockPerson.setId(TestIdGenerator.generateExistentTestId());
        mockPerson.setSalaryIndex(2.0);
        double existingSalaryIndex = 2.0;

        doThrow(DatabaseOperationException.class).when(personService).updateSalaryIndex(invalidId, existingSalaryIndex);

        ResponseEntity<String> mockResponseInvalidId = personRestController.updateSalaryIndex(
                String.valueOf(invalidId), String.valueOf(existingSalaryIndex));

        doThrow(DatabaseOperationException.class).when(personService).updateSalaryIndex(mockPerson.getId(), existingSalaryIndex);

        ResponseEntity<String> mockResponseSameSalaryIndex = personRestController.updateSalaryIndex(
                String.valueOf(mockPerson.getId()), String.valueOf(existingSalaryIndex));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, mockResponseInvalidId.getStatusCode());
        assertTrue(mockResponseInvalidId.getBody().contains("Failed"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, mockResponseSameSalaryIndex.getStatusCode());
        assertTrue(mockResponseSameSalaryIndex.getBody().contains("Failed"));

        verify(personService, times(1)).updateSalaryIndex(invalidId, existingSalaryIndex);
        verify(personService, times(1)).updateSalaryIndex(mockPerson.getId(), existingSalaryIndex);
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void getPersonJobMockTest() throws DatabaseOperationException {

        Person mockPerson = new Person();
        mockPerson.setId(TestIdGenerator.generateExistentTestId());
        mockPerson.setJobId(TestIdGenerator.generateExistentTestId());

        Job mockJob = new Job();
        mockJob.setId(mockPerson.getJobId());

        when(personService.getPersonById(mockPerson.getId())).thenReturn(mockPerson);
        when(personService.getPersonJob(mockPerson.getId())).thenReturn(mockJob);

        ResponseEntity<String> mockResponse = personRestController.getPersonJob(String.valueOf(mockPerson.getId()));

        assertEquals(HttpStatus.OK, mockResponse.getStatusCode());
        assertTrue(mockResponse.getBody().contains("successfully"));

        verify(personService, times(1)).getPersonById(mockPerson.getId());
        verify(personService, times(1)).getPersonJob(mockPerson.getId());
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void getPersonJobInvalidIdMockTest() throws DatabaseOperationException {

        //invalid scenario 1 - invalid person id;
        Person mockPerson = new Person();
        mockPerson.setId(TestIdGenerator.generateInvalidTestId());

        doThrow(DatabaseOperationException.class).when(personService).getPersonById(mockPerson.getId());

        ResponseEntity<String> mockResponseInvalidPersonId = personRestController.getPersonJob(String.valueOf(mockPerson.getId()));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, mockResponseInvalidPersonId.getStatusCode());
        assertTrue(mockResponseInvalidPersonId.getBody().contains("Failed"));

        verify(personService, times(1)).getPersonById(mockPerson.getId());
        verify(personService, never()).getPersonJob(mockPerson.getId());

        //invalid scenario 2 - valid person id, invalid job id;
        mockPerson.setId(TestIdGenerator.generateExistentTestId());
        mockPerson.setJobId(TestIdGenerator.generateInvalidTestId());

        when(personService.getPersonById(mockPerson.getId())).thenReturn(mockPerson);
        doThrow(DatabaseOperationException.class).when(personService).getPersonJob(mockPerson.getId());

        ResponseEntity<String> mockResponseInvalidJobId = personRestController.getPersonJob(String.valueOf(mockPerson.getId()));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, mockResponseInvalidJobId.getStatusCode());
        assertTrue(mockResponseInvalidJobId.getBody().contains("Failed"));

        verify(personService, times(1)).getPersonById(mockPerson.getId());
        verify(personService, times(1)).getPersonJob(mockPerson.getId());
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void getPersonSalaryMockTest() throws DatabaseOperationException {

        Person mockPerson = new Person();
        mockPerson.setId(TestIdGenerator.generateExistentTestId());
        mockPerson.setJobId(TestIdGenerator.generateExistentTestId());
        mockPerson.setSalaryIndex(2.0);

        Job mockJob = new Job();
        mockJob.setId(mockPerson.getJobId());
        mockJob.setBaseSalary(2500);

        when(personService.getPersonById(mockPerson.getId())).thenReturn(mockPerson);
        when(personService.getPersonSalary(mockPerson.getId())).thenReturn(5000.0);

        ResponseEntity<String> mockResponse = personRestController.getPersonSalary(String.valueOf(mockPerson.getId()));

        assertEquals(HttpStatus.OK, mockResponse.getStatusCode());
        assertTrue(mockResponse.getBody().contains("successfully"));

        verify(personService, times(1)).getPersonById(mockPerson.getId());
        verify(personService, times(1)).getPersonSalary(mockPerson.getId());
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void getPersonSalaryInvalidIdMockTest() throws DatabaseOperationException {

        Person mockPerson = new Person();
        mockPerson.setId(TestIdGenerator.generateInvalidTestId());
        mockPerson.setJobId(TestIdGenerator.generateExistentTestId());
        mockPerson.setSalaryIndex(2.0);

        Job mockJob = new Job();
        mockJob.setId(mockPerson.getJobId());
        mockJob.setBaseSalary(2500);

        doThrow(DatabaseOperationException.class).when(personService).getPersonById(mockPerson.getId());

        ResponseEntity<String> mockResponseInvalidPersonId = personRestController.getPersonSalary(String.valueOf(mockPerson.getId()));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, mockResponseInvalidPersonId.getStatusCode());
        assertTrue(mockResponseInvalidPersonId.getBody().contains("Failed"));

        verify(personService, times(1)).getPersonById(mockPerson.getId());
        verify(personService, never()).getPersonSalary(mockPerson.getId());
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void getPersonWorkExperienceMockTest() throws DatabaseOperationException {

        Person mockPerson = new Person();
        mockPerson.setId(TestIdGenerator.generateExistentTestId());
        mockPerson.setSalaryIndex(2.0);

        when(personService.getPersonById(mockPerson.getId())).thenReturn(mockPerson);
        when(personService.getPersonWorkExperience(mockPerson.getId())).thenReturn("mid level");

        ResponseEntity<String> mockResponse = personRestController.getPersonWorkExperience(String.valueOf(mockPerson.getId()));

        assertEquals(HttpStatus.OK, mockResponse.getStatusCode());
        assertTrue(mockResponse.getBody().contains("successfully"));

        verify(personService, times(1)).getPersonById(mockPerson.getId());
        verify(personService, times(1)).getPersonWorkExperience(mockPerson.getId());
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void getPersonWorkExperienceInvalidIdMockTest() throws DatabaseOperationException {

        Person mockPerson = new Person();
        mockPerson.setId(TestIdGenerator.generateInvalidTestId());
        mockPerson.setSalaryIndex(2.0);

        doThrow(DatabaseOperationException.class).when(personService).getPersonById(mockPerson.getId());

        ResponseEntity<String> mockResponse = personRestController.getPersonWorkExperience(String.valueOf(mockPerson.getId()));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, mockResponse.getStatusCode());
        assertTrue(mockResponse.getBody().contains("Failed"));

        verify(personService, times(1)).getPersonById(mockPerson.getId());
        verify(personService, never()).getPersonWorkExperience(mockPerson.getId());
        verifyNoMoreInteractions(personService);
    }
}