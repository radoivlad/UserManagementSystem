package com.itfactory.controller;

import com.itfactory.dao.JobDaoIntegrationTest;
import com.itfactory.dao.PersonDaoIntegrationTest;
import com.itfactory.model.Person;
import com.itfactory.exceptions.DatabaseOperationException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Writing JUnit INTEGRATION tests for each of the 8 PersonRestController methods;
 * Further testing can be done on the local server, verifying accessibility through endpoints - using Postman;
 * Slightly different testing methodology than PersonDaoIntegrationTest, with valid and invalid scenarios in a single complete method;
 * Asserting by ResponseEntity<String> response body;
 */

@SpringBootTest
class PersonRestControllerIntegrationTest {

    @Autowired
    private PersonRestController personRestController;

    @Test
    public void getPersonByIdCompleteTest() throws DatabaseOperationException {

        int existentId = PersonDaoIntegrationTest.generateExistentTestId();

        assertDoesNotThrow(() -> personRestController.getPersonById(String.valueOf(existentId)));

        assertTrue(personRestController.getPersonById(String.valueOf(existentId)).toString().contains("successfully"));

        assertTrue(personRestController.getPersonById(
                String.valueOf(PersonDaoIntegrationTest.generateInvalidTestId())).toString().contains("Failed"));
    }

    @Test
    public void deletePersonCompleteTest () throws DatabaseOperationException {

        Person testPerson = new Person();
        testPerson.setId(PersonDaoIntegrationTest.generateInvalidTestId());
        testPerson.setName("Test Person");
        testPerson.setEmail("Test Email");
        testPerson.setJobId(JobDaoIntegrationTest.generateExistentTestId());

        personRestController.insertPerson(testPerson);

        assertTrue(personRestController.deletePerson(String.valueOf(testPerson.getId())).toString().contains("successfully"));
        assertTrue(personRestController.getPersonById(String.valueOf(testPerson.getId())).toString().contains("Failed"));

        assertTrue(personRestController.deletePerson(String.valueOf(testPerson.getId())).toString().contains("Failed"));
    }

    @Test
    public void getAllPersonsTest () throws DatabaseOperationException {

        Person testPerson1 = new Person();
        testPerson1.setId(PersonDaoIntegrationTest.generateInvalidTestId());
        testPerson1.setName("Test Person One");
        testPerson1.setEmail("Test Email");
        testPerson1.setJobId(JobDaoIntegrationTest.generateExistentTestId());
        personRestController.insertPerson(testPerson1);

        Person testPerson2 = new Person();
        testPerson2.setId(PersonDaoIntegrationTest.generateInvalidTestId());
        testPerson2.setName("Test Person Two");
        testPerson2.setEmail("Test Email");
        testPerson2.setJobId(JobDaoIntegrationTest.generateExistentTestId());
        personRestController.insertPerson(testPerson2);

        assertTrue(personRestController.getAllPersons().toString().contains("successfully"));

        personRestController.deletePerson(String.valueOf(testPerson1.getId()));
        personRestController.deletePerson(String.valueOf(testPerson2.getId()));
    }

    @Test
    public void insertPersonCompleteTest () throws DatabaseOperationException {

        Person testPerson = new Person();
        testPerson.setId(PersonDaoIntegrationTest.generateInvalidTestId());
        testPerson.setName("Test Person");
        testPerson.setEmail("Test Email");
        testPerson.setJobId(JobDaoIntegrationTest.generateExistentTestId());

        assertTrue(personRestController.insertPerson(testPerson).toString().contains("successfully"));

        assertTrue(personRestController.insertPerson(testPerson).toString().contains("Failed"));

        personRestController.deletePerson(String.valueOf(testPerson.getId()));
    }

    @Test
    public void updateSalaryIndexCompleteTest () throws DatabaseOperationException {

        Person testPerson = new Person();
        testPerson.setId(PersonDaoIntegrationTest.generateInvalidTestId());
        testPerson.setName("Test Person");
        testPerson.setEmail("Test Email");
        testPerson.setJobId(JobDaoIntegrationTest.generateExistentTestId());
        testPerson.setSalaryIndex(2);
        double newSalaryIndex = 2.5;

        personRestController.insertPerson(testPerson);

        assertTrue(personRestController.updateSalaryIndex(String.valueOf(testPerson.getId()), String.valueOf(newSalaryIndex))
                .toString().contains("successfully"));

        assertTrue(personRestController.updateSalaryIndex(String.valueOf(testPerson.getId()), String.valueOf(newSalaryIndex))
                .toString().contains("Failed"));

        personRestController.deletePerson(String.valueOf(testPerson.getId()));

        assertTrue(personRestController.updateSalaryIndex(String.valueOf(testPerson.getId()), String.valueOf(newSalaryIndex))
                .toString().contains("Failed"));
    }

    @Test
    public void getPersonJobCompleteTest() throws DatabaseOperationException {

        Person testPerson = new Person();
        testPerson.setId(PersonDaoIntegrationTest.generateInvalidTestId());
        testPerson.setName("Test Person");
        testPerson.setEmail("Test Email");
        testPerson.setJobId(JobDaoIntegrationTest.generateExistentTestId());

        personRestController.insertPerson(testPerson);

        assertTrue(personRestController.getPersonJob(String.valueOf(testPerson.getId())).toString().contains("successfully"));

        personRestController.deletePerson(String.valueOf(testPerson.getId()));

        assertTrue(personRestController.getPersonJob(String.valueOf(testPerson.getId())).toString().contains("Failed"));
    }

    @Test
    public void getPersonSalaryCompleteTest() throws DatabaseOperationException {

        Person testPerson = new Person();
        testPerson.setId(PersonDaoIntegrationTest.generateInvalidTestId());
        testPerson.setName("Test Person");
        testPerson.setEmail("Test Email");
        testPerson.setJobId(JobDaoIntegrationTest.generateExistentTestId());

        personRestController.insertPerson(testPerson);

        assertTrue(personRestController.getPersonSalary(String.valueOf(testPerson.getId())).toString().contains("successfully"));

        personRestController.deletePerson(String.valueOf(testPerson.getId()));

        assertTrue(personRestController.getPersonSalary(String.valueOf(testPerson.getId())).toString().contains("Failed"));
    }

    @Test
    public void getPersonWorkExperienceCompleteTest() throws DatabaseOperationException {

        Person testPerson = new Person();
        testPerson.setId(PersonDaoIntegrationTest.generateInvalidTestId());
        testPerson.setName("Test Person");
        testPerson.setEmail("Test Email");
        testPerson.setJobId(JobDaoIntegrationTest.generateExistentTestId());

        personRestController.insertPerson(testPerson);

        assertTrue(personRestController.getPersonWorkExperience(String.valueOf(testPerson.getId())).toString().contains("successfully"));

        personRestController.deletePerson(String.valueOf(testPerson.getId()));

        assertTrue(personRestController.getPersonWorkExperience(String.valueOf(testPerson.getId())).toString().contains("Failed"));
    }
}