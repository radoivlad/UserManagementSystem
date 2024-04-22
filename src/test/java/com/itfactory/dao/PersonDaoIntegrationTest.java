package com.itfactory.dao;

import com.itfactory.exceptions.DatabaseOperationException;
import com.itfactory.model.Person;
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
 * Writing JUnit INTEGRATION tests for each of the 5 database manipulation methods (CRUD) in PersonDAO class;
 * Included - connection test and invalid scenarios;
 * Using static helper methods to generate existent and non-existent database id;
 */

@SpringBootTest
public class PersonDaoIntegrationTest {

    //injecting by @Autowired - the PersonDao object, used in each test;
    @Autowired
    private PersonDao personDao;

    //every test assumes a test person or test id, which is subjected to the respective database manipulation method;

    //generate valid id, verify successful extraction (does not throw exception, not null result, equivalent id);
    //invalid scenario - extract using invalid id - throws exception;
    @Test
    public void getPersonByIdTest() throws DatabaseOperationException {

        int existentId = TestIdGenerator.generateExistentTestId();

        assertDoesNotThrow(() -> personDao.getPersonById(existentId));

        Person foundTestPersonById = personDao.getPersonById(existentId);

        assertNotNull(foundTestPersonById);
        assertEquals(existentId, foundTestPersonById.getId());
    }

    @Test
    public void getPersonByIdInvalidTest() {

        assertThrows(DatabaseOperationException.class,
                () -> personDao.getPersonById(TestIdGenerator.generateInvalidTestId()));
    }

    //generate person, insert, delete, verify successful removal (a subsequent extraction throws exception);
    //invalid scenario - delete using invalid id - throws exception;
    @Test
    public void deletePersonTest() throws DatabaseOperationException {

        Person testPerson = new Person();
        testPerson.setId(TestIdGenerator.generateInvalidTestId());
        testPerson.setName("Test Person");
        testPerson.setEmail("Test Person Email");
        testPerson.setJobId(TestIdGenerator.generateExistentTestId());

        personDao.insertPerson(testPerson);

        personDao.deletePerson(testPerson.getId());

        assertThrows(DatabaseOperationException.class,
                () -> personDao.getPersonById(testPerson.getId()));
    }

    @Test
    public void deletePersonInvalidTest() {

        assertThrows(DatabaseOperationException.class,
                () -> personDao.deletePerson(TestIdGenerator.generateInvalidTestId()));
    }

    //generate persons, insert, extract all persons, verify successful extraction (not null result, new list size increased accordingly);
    @Test
    public void getAllPersonsTest() throws DatabaseOperationException {

        int actualListSize = personDao.getAllPersons().size();

        Person testPerson1 = new Person();
        testPerson1.setId(TestIdGenerator.generateInvalidTestId());
        testPerson1.setName("Test Person 1");
        testPerson1.setEmail("Test Person 1 Email");
        testPerson1.setJobId(TestIdGenerator.generateExistentTestId());
        personDao.insertPerson(testPerson1);

        Person testPerson2 = new Person();
        testPerson2.setId(TestIdGenerator.generateInvalidTestId() - 1);
        testPerson2.setName("Test Person 2");
        testPerson2.setEmail("Test Person 2 Email");
        testPerson2.setJobId(TestIdGenerator.generateExistentTestId());
        personDao.insertPerson(testPerson2);

        List<Person> testPersonsInserted = personDao.getAllPersons();

        assertNotNull(testPersonsInserted);
        assertEquals(actualListSize + 2, testPersonsInserted.size());

        personDao.deletePerson(testPerson1.getId());
        personDao.deletePerson(testPerson2.getId());
    }

    //generate person, insert, verify successful insertion (extract inserted person, compare all attributes to generated person);
    //invalid scenario - insert existing person - throws exception;
    @Test
    public void insertPersonTest() throws DatabaseOperationException {

        Person testPerson = new Person();
        testPerson.setId(TestIdGenerator.generateInvalidTestId());
        testPerson.setName("Test Person");
        testPerson.setEmail("test@email.com");
        testPerson.setJobId(TestIdGenerator.generateExistentTestId());
        testPerson.setSalaryIndex(2);

        personDao.insertPerson(testPerson);

        Person foundInsertedPerson = personDao.getPersonById(testPerson.getId());

        assertNotNull(foundInsertedPerson);
        assertEquals(testPerson.getId(), foundInsertedPerson.getId());
        assertEquals(testPerson.getName(), foundInsertedPerson.getName());
        assertEquals(testPerson.getEmail(), foundInsertedPerson.getEmail());
        assertEquals(testPerson.getJobId(), foundInsertedPerson.getJobId());
        assertEquals(testPerson.getSalaryIndex(), foundInsertedPerson.getSalaryIndex());

        personDao.deletePerson(testPerson.getId());
    }

    @Test
    public void insertPersonInvalidTest() throws DatabaseOperationException {

        Person testPerson = new Person();
        testPerson.setId(TestIdGenerator.generateExistentTestId());

        assertThrows(DatabaseOperationException.class, () -> personDao.insertPerson(testPerson));
    }

    //generate person, generate new salary index, insert person, update their salary index, extract person, compare salary index;
    //invalid scenario - update salary index of invalid id (throwing exception);
    @Test
    public void updateSalaryIndexTest() throws DatabaseOperationException {

        Person testPerson = new Person();
        testPerson.setId(TestIdGenerator.generateInvalidTestId());
        testPerson.setName("Test Person");
        testPerson.setEmail("Test Person Email");
        testPerson.setJobId(TestIdGenerator.generateExistentTestId());
        testPerson.setSalaryIndex(2);
        double newSalaryIndex = 2.5;

        personDao.insertPerson(testPerson);

        personDao.updateSalaryIndex(testPerson.getId(), newSalaryIndex);

        Person foundInsertedPerson = personDao.getPersonById(testPerson.getId());
        assertEquals(newSalaryIndex, foundInsertedPerson.getSalaryIndex());

        personDao.deletePerson(testPerson.getId());
    }

    @Test
    public void updateSalaryIndexInvalidTest() {

        assertThrows(DatabaseOperationException.class,
                () -> personDao.updateSalaryIndex(TestIdGenerator.generateInvalidTestId(), 2.0));
    }

    //verify method behaviour to bad connection parameters (catch clause);
    @Test
    public void connectionTest() {

        try {
            Connection connection = DriverManager.getConnection("testUrl");

            PreparedStatement statement = connection.prepareStatement("DELETE FROM person WHERE id = ?");

            statement.setInt(1, 1);

            statement.execute();

        } catch (SQLException e) {

            assertEquals("No suitable driver found for testUrl", e.getMessage());
        }
    }
}