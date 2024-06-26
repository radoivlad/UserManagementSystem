package com.itfactory.dao;

import com.itfactory.exceptions.DatabaseOperationException;
import com.itfactory.model.Person;
import com.itfactory.utility.TestIdGenerator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Writing JUnit MOCK (mocked beans and behaviours) tests for each of the 5 database manipulation methods (CRUD) in PersonDao class;
 * Included - invalid scenarios;
 */

//@SpringBootTest annotation is used so all Mock objects are initialized in the Spring application context, without the need to do it manually;
@SpringBootTest
class PersonDaoMockTest {

    //creating a mock instance of the PersonDao class, to be used for the tests;
    //with every test, a new Bean of personDao gets created, thus resetting its mocked behaviour;
    @MockBean
    private PersonDao personDao;

    //creating mock person, mocking behaviour of getPersonById to return mockPerson, calling the method, verifying successful calling;
    //also, asserting equivalence of created mock person and returned person;
    //invalid scenario - mock behaviour to throw exception when called with invalid id, assert if exception is thrown, verify successful calling;
    @Test
    public void getPersonByIdMockTest() throws DatabaseOperationException {

        int existentId = TestIdGenerator.generateExistentTestId();
        Person mockPerson = new Person();
        mockPerson.setId(existentId);
        mockPerson.setName("Test Mock Person");

        when(personDao.getPersonById(existentId)).thenReturn(mockPerson);

        Person foundPerson = personDao.getPersonById(existentId);

        assertNotNull(foundPerson);
        assertEquals(existentId, foundPerson.getId());

        verify(personDao, times(1)).getPersonById(existentId);
        verifyNoMoreInteractions(personDao);
    }

    @Test
    public void getPersonByIdInvalidMockTest() throws DatabaseOperationException {

        int invalidId = TestIdGenerator.generateInvalidTestId();

        doThrow(DatabaseOperationException.class).when(personDao).getPersonById(invalidId);

        assertThrows(DatabaseOperationException.class, () -> personDao.getPersonById(invalidId));

        verify(personDao, times(1)).getPersonById(invalidId);
        verifyNoMoreInteractions(personDao);
    }

    //create mock person, insert + verify insert, call delete method + verify successful calling;
    //no need to mock behaviours of personDao insert and delete methods, they are void return type (and personDao is mocked);
    //invalid scenario - create invalid id, mock to throw exception when deleting with invalid id, assert if exception was thrown, verify call of delete;
    @Test
    public void deletePersonMockTest() throws DatabaseOperationException {

        Person mockPerson = new Person();
        mockPerson.setId(TestIdGenerator.generateInvalidTestId());
        mockPerson.setName("Test Person");
        mockPerson.setJobId(TestIdGenerator.generateExistentTestId());

        personDao.insertPerson(mockPerson);

        personDao.deletePerson(mockPerson.getId());

        verify(personDao, times(1)).insertPerson(mockPerson);
        verify(personDao, times(1)).deletePerson(mockPerson.getId());
        verifyNoMoreInteractions(personDao);
    }

    @Test
    public void deletePersonInvalidIdMockTest() throws DatabaseOperationException {

        int invalidId = TestIdGenerator.generateInvalidTestId();

        doThrow(DatabaseOperationException.class).when(personDao).deletePerson(invalidId);

        assertThrows(DatabaseOperationException.class, () -> personDao.deletePerson(invalidId));

        verify(personDao, times(1)).deletePerson(invalidId);
        verifyNoMoreInteractions(personDao);
    }

    //create list with 2 mock persons, mock method to return created list, call method + verify, assert equivalence of initial list and returned list;
    @Test
    public void getAllPersonsMockTest() throws DatabaseOperationException {

        Person mockPerson1 = new Person();
        mockPerson1.setId(TestIdGenerator.generateExistentTestId());

        Person mockPerson2 = new Person();
        mockPerson2.setId(TestIdGenerator.generateExistentTestId() + 1);

        List<Person> testPersonsInserted = new ArrayList<>(List.of(mockPerson1, mockPerson2));

        when(personDao.getAllPersons()).thenReturn(testPersonsInserted);

        List<Person> getAllResult = personDao.getAllPersons();

        assertNotNull(getAllResult);
        assertEquals(testPersonsInserted.size(), getAllResult.size());
        assertTrue(getAllResult.containsAll(testPersonsInserted));

        verify(personDao, times(1)).getAllPersons();
        verifyNoMoreInteractions(personDao);
    }

    //create mock person, call insert method + verify successful calling, mock extraction method to return inserted person, call and verify it;
    //assert between extracted person and initial mock person;
    //invalid scenario - mock insert method to throw exception when inserting an existing person, assert if it throws exception, verify successful calling;
    @Test
    public void insertPersonMockTest() throws DatabaseOperationException {

        Person mockPerson = new Person();
        mockPerson.setId(TestIdGenerator.generateInvalidTestId());
        mockPerson.setName("Test Mock Person");
        mockPerson.setEmail("mock@email.com");
        mockPerson.setJobId(TestIdGenerator.generateExistentTestId());
        mockPerson.setSalaryIndex(2);

        personDao.insertPerson(mockPerson);

        when(personDao.getPersonById(mockPerson.getId())).thenReturn(mockPerson);

        Person foundInsertedPerson = personDao.getPersonById(mockPerson.getId());

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
    public void insertPersonInvalidMockTest() throws DatabaseOperationException {

        Person mockPerson = new Person();
        mockPerson.setId(TestIdGenerator.generateExistentTestId());

        doThrow(DatabaseOperationException.class).when(personDao).insertPerson(mockPerson);

        assertThrows(DatabaseOperationException.class, () -> personDao.insertPerson(mockPerson));

        verify(personDao, times(1)).insertPerson(mockPerson);
        verifyNoMoreInteractions(personDao);
    }

    //create mock person and new salary index, create expected person with new salary index, mock method to return expected person, call method;
    //assert equivalence of new salary index and expected person's salary index; all calls are verified;
    //invalid scenario - create mock person and invalid id, mock method to throw exception when updating to same salary index or invalid id, assert if exceptions are thrown in these 2 situations;
    @Test
    public void updateSalaryIndexMockTest() throws DatabaseOperationException {

        Person mockPerson = new Person();
        mockPerson.setId(TestIdGenerator.generateExistentTestId());
        mockPerson.setSalaryIndex(2);
        double newSalaryIndex = 2.5;

        Person updatedPerson = new Person();
        updatedPerson.setId(mockPerson.getId());
        updatedPerson.setSalaryIndex(newSalaryIndex);

        when(personDao.updateSalaryIndex(mockPerson.getId(), newSalaryIndex)).thenReturn(updatedPerson);

        Person retrievedUpdatedPerson = personDao.updateSalaryIndex(mockPerson.getId(), newSalaryIndex);

        assertEquals(newSalaryIndex, retrievedUpdatedPerson.getSalaryIndex());

        verify(personDao, times(1)).updateSalaryIndex(mockPerson.getId(), newSalaryIndex);
        verifyNoMoreInteractions(personDao);
    }

    @Test
    public void updateSalaryIndexInvalidMockTest() throws DatabaseOperationException {

        int invalidId = TestIdGenerator.generateInvalidTestId();

        Person mockPerson = new Person();
        mockPerson.setSalaryIndex(2.0);
        double existingSalaryIndex = 2.0;

        doThrow(DatabaseOperationException.class).when(personDao).updateSalaryIndex(invalidId, existingSalaryIndex);
        doThrow(DatabaseOperationException.class).when(personDao).updateSalaryIndex(mockPerson.getId(), existingSalaryIndex);

        assertThrows(DatabaseOperationException.class, () -> personDao.updateSalaryIndex(invalidId, existingSalaryIndex));
        assertThrows(DatabaseOperationException.class, () -> personDao.updateSalaryIndex(mockPerson.getId(), existingSalaryIndex));

        verify(personDao, times(1)).updateSalaryIndex(invalidId, existingSalaryIndex);
        verify(personDao, times(1)).updateSalaryIndex(mockPerson.getId(), existingSalaryIndex);
        verifyNoMoreInteractions(personDao);
    }
}