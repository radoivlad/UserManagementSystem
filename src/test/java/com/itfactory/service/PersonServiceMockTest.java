package com.itfactory.service;

import com.itfactory.dao.PersonDao;
import com.itfactory.exceptions.DatabaseOperationException;
import com.itfactory.model.Person;
import com.itfactory.utility.TestIdGenerator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;

import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Writing JUnit MOCK tests for each of the 5 CRUD service methods in PersonService class;
 * Included - tested scenarios where invalid id values are being passed in PersonService methods;
 * Similar testing methodology to PersonDaoMockTest (as described in comments);
 */

@SpringBootTest
class PersonServiceMockTest {

    //a PersonDao object is mocked, then used to instantiate a PersonService object;
    @Mock
    private PersonDao personDao;

    private PersonService personService;

    //before each test method, personService is instantiated, using the mocked personDao;
    //every test method assumes mocked methods of personDao, then using their mocked behaviours to call personService methods;
    @BeforeEach
    void setUp() {

        personService = new PersonService(personDao);
    }

    @Test
    public void getPersonByIdMockTest() throws DatabaseOperationException {

        int existentId = TestIdGenerator.generateExistentTestId();
        Person mockPerson = new Person();
        mockPerson.setId(existentId);
        mockPerson.setName("Test Mock Person");

        when(personDao.getPersonById(existentId)).thenReturn(mockPerson);

        Person foundPerson = personService.getPersonById(existentId);

        assertNotNull(foundPerson);
        assertEquals(existentId, foundPerson.getId());

        verify(personDao, times(1))
                .getPersonById(existentId);
        verifyNoMoreInteractions(personDao);
    }

    @Test
    public void getPersonByIdInvalidMockTest () throws DatabaseOperationException {

        int invalidId = TestIdGenerator.generateInvalidTestId();

        doThrow(DatabaseOperationException.class).when(personDao).getPersonById(invalidId);

        assertThrows(DatabaseOperationException.class, () -> personService.getPersonById(invalidId));

        verify(personDao, times(1)).getPersonById(invalidId);
        verifyNoMoreInteractions(personDao);
    }

    @Test
    public void deletePersonMockTest() throws DatabaseOperationException {

        Person mockPerson = new Person();
        mockPerson.setId(TestIdGenerator.generateInvalidTestId());
        mockPerson.setName("Test Mock Person");
        mockPerson.setEmail("testmock@email.com");
        mockPerson.setJobId(TestIdGenerator.generateExistentTestId());

        personService.insertPerson(mockPerson);

        when(personDao.getPersonById(mockPerson.getId())).thenReturn(mockPerson);

        personService.deletePerson(mockPerson.getId());

        verify(personDao, times(1)).insertPerson(mockPerson);
        verify(personDao, times(1)).deletePerson(mockPerson.getId());
        verify(personDao, times(1)).getPersonById(mockPerson.getId());
        verifyNoMoreInteractions(personDao);
    }

    @Test
    public void deletePersonByIdInvalidMockTest () throws DatabaseOperationException {

        int invalidId = TestIdGenerator.generateInvalidTestId();

        doThrow(DatabaseOperationException.class).when(personDao).getPersonById(invalidId);

        assertThrows(DatabaseOperationException.class, () -> personService.deletePerson(invalidId));

        verify(personDao, times(1)).getPersonById(invalidId);
        verify(personDao, never()).deletePerson(invalidId);
        verifyNoMoreInteractions(personDao);
    }

    @Test
    public void getAllPersonsMockTest() throws DatabaseOperationException {

        Person mockPerson1 = new Person();
        mockPerson1.setId(TestIdGenerator.generateExistentTestId());

        Person mockPerson2 = new Person();
        mockPerson2.setId(TestIdGenerator.generateExistentTestId() + 1);

        List<Person> testPersonsInserted = new ArrayList<>(List.of(mockPerson1, mockPerson2));

        when(personDao.getAllPersons()).thenReturn(testPersonsInserted);

        List<Person> getAllResult = personService.getAllPersons();

        assertNotNull(getAllResult);
        assertEquals(testPersonsInserted.size(), getAllResult.size());
        assertTrue(getAllResult.containsAll(testPersonsInserted));

        verify(personDao, times(1)).getAllPersons();
        verifyNoMoreInteractions(personDao);
    }

    @Test
    public void insertPersonMockTest () throws DatabaseOperationException {

        Person mockPerson = new Person();
        mockPerson.setId(TestIdGenerator.generateInvalidTestId());
        mockPerson.setName("Test Mock Person");
        mockPerson.setEmail("test@email.com");
        mockPerson.setJobId(TestIdGenerator.generateExistentTestId());
        mockPerson.setSalaryIndex(2);

        personService.insertPerson(mockPerson);

        when(personDao.getPersonById(mockPerson.getId())).thenReturn(mockPerson);

        Person foundInsertedPerson = personService.getPersonById(mockPerson.getId());

        assertNotNull(foundInsertedPerson);
        assertEquals(mockPerson.getId(), foundInsertedPerson.getId());
        assertEquals(mockPerson.getName(), foundInsertedPerson.getName());
        assertEquals(mockPerson.getEmail(), foundInsertedPerson.getEmail());
        assertEquals(mockPerson.getJobId(), foundInsertedPerson.getJobId());
        assertEquals(mockPerson.getSalaryIndex(), foundInsertedPerson.getSalaryIndex());

        verify(personDao, times(1)).insertPerson(mockPerson);
        verify(personDao, times(1)).getPersonById(mockPerson.getId());
        verifyNoMoreInteractions(personDao);
    }

    @Test
    public void insertPersonInvalidIdMockTest () throws DatabaseOperationException {

        Person mockPerson = new Person();
        mockPerson.setId(TestIdGenerator.generateExistentTestId());
        mockPerson.setName("Test Mock Person");
        mockPerson.setEmail("test@email.com");
        mockPerson.setJobId(TestIdGenerator.generateExistentTestId());
        mockPerson.setSalaryIndex(2);

        doThrow(DatabaseOperationException.class).when(personDao).insertPerson(mockPerson);

        assertThrows(DatabaseOperationException.class, () -> personService.insertPerson(mockPerson));

        verify(personDao, times(1)).insertPerson(mockPerson);
        verifyNoMoreInteractions(personDao);
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

        when(personDao.getPersonById(mockPerson.getId())).thenReturn(mockPerson);
        when(personDao.updateSalaryIndex(mockPerson.getId(), newSalaryIndex)).thenReturn(updatedPerson);

        Person retrievedUpdatedPerson = personService.updateSalaryIndex(mockPerson.getId(), newSalaryIndex);

        assertEquals(newSalaryIndex, retrievedUpdatedPerson.getSalaryIndex());

        verify(personDao, times(1)).getPersonById(mockPerson.getId());
        verify(personDao, times(1)).updateSalaryIndex(mockPerson.getId(), newSalaryIndex);
        verifyNoMoreInteractions(personDao);
    }

    @Test
    public void updateSalaryIndexInvalidMockTest() throws DatabaseOperationException {

        int invalidId = TestIdGenerator.generateInvalidTestId();

        Person mockPerson = new Person();
        mockPerson.setId(TestIdGenerator.generateExistentTestId());
        mockPerson.setSalaryIndex(2.0);
        double existingSalaryIndex = 2.0;

        doThrow(DatabaseOperationException.class).when(personDao).getPersonById(invalidId);

        assertThrows(DatabaseOperationException.class, () -> personService.updateSalaryIndex(invalidId, 2.0));

        when(personDao.getPersonById(mockPerson.getId())).thenReturn(mockPerson);

        assertThrows(DatabaseOperationException.class, () -> personService.updateSalaryIndex(
                mockPerson.getId(), existingSalaryIndex));

        verify(personDao, times(1)).getPersonById(mockPerson.getId());
        verify(personDao, times(1)).getPersonById(invalidId);
        verify(personDao, never()).updateSalaryIndex(invalidId, 2.0);
        verify(personDao, never()).updateSalaryIndex(mockPerson.getId(), existingSalaryIndex);
        verifyNoMoreInteractions(personDao);
    }
}