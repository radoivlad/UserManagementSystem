package com.itfactory.service;

import com.itfactory.dao.JobDao;
import com.itfactory.dao.JobDaoIntegrationTest;
import com.itfactory.dao.PersonDaoIntegrationTest;
import com.itfactory.model.Job;
import com.itfactory.model.Person;
import com.itfactory.model.WorkExperience;
import com.itfactory.exceptions.DatabaseOperationException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * Writing JUnit INTEGRATION tests for each of the 8 service methods in PersonService class;
 * Included - tested scenarios where invalid id values are being passed in PersonService methods;
 * Similar testing methodology to PersonDaoIntegrationTest, as described in comments;
 */

@SpringBootTest
public class PersonServiceIntegrationTest {

    @Autowired
    private PersonService personService;

    @Test
    public void getPersonByIdTest() throws DatabaseOperationException {

        int existentId = PersonDaoIntegrationTest.generateExistentTestId();

        assertDoesNotThrow(() -> personService.getPersonById(existentId));

        Person foundTestPersonById = personService.getPersonById(existentId);

        assertNotNull(foundTestPersonById);
        assertEquals(existentId, foundTestPersonById.getId());
    }

    @Test
    public void getPersonByInvalidIdTest (){

        assertThrows(DatabaseOperationException.class,
                () -> personService.getPersonById(PersonDaoIntegrationTest
                        .generateInvalidTestId()));
    }

    @Test
    public void deletePersonTest () throws DatabaseOperationException {

        Person testPerson = new Person();
        testPerson.setId(PersonDaoIntegrationTest.generateInvalidTestId());
        testPerson.setName("Test Person");
        testPerson.setEmail("Test Email");
        testPerson.setJobId(JobDaoIntegrationTest.generateExistentTestId());

        personService.insertPerson(testPerson);

        personService.deletePerson(testPerson.getId());

        assertThrows(DatabaseOperationException.class,
                () -> personService.getPersonById(testPerson.getId()));
    }

    @Test
    public void deletePersonByInvalidIdTest (){

        assertThrows(DatabaseOperationException.class,
                () -> personService.deletePerson(PersonDaoIntegrationTest.generateInvalidTestId()));
    }

    @Test
    public void getAllPersonsTest () throws DatabaseOperationException {

        int actualListSize = personService.getAllPersons().size();

        Person testPerson1 = new Person();
        testPerson1.setId(PersonDaoIntegrationTest.generateInvalidTestId());
        testPerson1.setName("Test Person One");
        testPerson1.setEmail("Test Email");
        testPerson1.setJobId(JobDaoIntegrationTest.generateExistentTestId());
        personService.insertPerson(testPerson1);

        Person testPerson2 = new Person();
        testPerson2.setId(PersonDaoIntegrationTest.generateInvalidTestId());
        testPerson2.setName("Test Person Two");
        testPerson2.setEmail("Test Email");
        testPerson2.setJobId(JobDaoIntegrationTest.generateExistentTestId());
        personService.insertPerson(testPerson2);

        List<Person> testPersonsInserted = personService.getAllPersons();

        assertNotNull(testPersonsInserted);
        assertEquals(actualListSize + 2, testPersonsInserted.size());

        personService.deletePerson(testPerson1.getId());
        personService.deletePerson(testPerson2.getId());
    }

    @Test
    public void insertPersonTest () throws DatabaseOperationException {

        Person testPerson = new Person();
        testPerson.setId(PersonDaoIntegrationTest.generateInvalidTestId());
        testPerson.setName("Test Person");
        testPerson.setEmail("test@email.com");
        testPerson.setJobId(JobDaoIntegrationTest.generateExistentTestId());
        testPerson.setSalaryIndex(2);

        personService.insertPerson(testPerson);

        Person foundInsertedPerson = personService.getPersonById(testPerson.getId());

        assertNotNull(foundInsertedPerson);
        assertEquals(testPerson.getId(), foundInsertedPerson.getId());
        assertEquals(testPerson.getName(), foundInsertedPerson.getName());
        assertEquals(testPerson.getEmail(), foundInsertedPerson.getEmail());
        assertEquals(testPerson.getJobId(), foundInsertedPerson.getJobId());
        assertEquals(testPerson.getSalaryIndex(), foundInsertedPerson.getSalaryIndex());

        personService.deletePerson(testPerson.getId());
    }

    @Test
    public void insertPersonInvalidIdTest (){

        assertThrows(DatabaseOperationException.class,
                () -> personService.insertPerson(personService.getPersonById(PersonDaoIntegrationTest.generateExistentTestId()))
        );
    }

    @Test
    public void updateSalaryIndexTest () throws DatabaseOperationException {

        Person testPerson = new Person();
        testPerson.setId(PersonDaoIntegrationTest.generateInvalidTestId());
        testPerson.setName("Test Person");
        testPerson.setEmail("Test Email");
        testPerson.setJobId(JobDaoIntegrationTest.generateExistentTestId());
        testPerson.setSalaryIndex(2);
        double newSalaryIndex = 2.5;

        personService.insertPerson(testPerson);

        personService.updateSalaryIndex(testPerson.getId(), newSalaryIndex);

        Person foundInsertedPerson = personService.getPersonById(testPerson.getId());
        assertEquals(newSalaryIndex, foundInsertedPerson.getSalaryIndex());

        personService.deletePerson(testPerson.getId());
    }

    @Test
    public void updateSalaryIndexInvalidInputTest () throws DatabaseOperationException {

        //invalid scenario 1 - update salary index of invalid id;
        assertThrows(DatabaseOperationException.class,
                () -> personService.updateSalaryIndex(PersonDaoIntegrationTest.generateInvalidTestId(), 2.0));

        //invalid scenario 2 - update salary index of valid id, but existing salary index;
        Person testPerson = new Person();
        testPerson.setId(PersonDaoIntegrationTest.generateInvalidTestId());
        testPerson.setName("Test Person");
        testPerson.setEmail("Test Email");
        testPerson.setJobId(JobDaoIntegrationTest.generateExistentTestId());
        testPerson.setSalaryIndex(2.0);

        personService.insertPerson(testPerson);

        assertThrows(DatabaseOperationException.class,
                () -> personService.updateSalaryIndex(testPerson.getId(), 2.0));

        personService.deletePerson(testPerson.getId());
    }

    //retrieve an existing database person, get job by tested method and JobDao method, compare the 2 obtained jobs;
    //invalid scenario - get job of non-existing database person;
    @Test
    public void getPersonJobTest() throws DatabaseOperationException {

        Person testPerson = personService.getPersonById(PersonDaoIntegrationTest.generateExistentTestId());

        Job getJobOfTestPerson = personService.getPersonJob(testPerson.getId());
        Job actualJobOfTestPerson = new JobDao().getJobById(testPerson.getJobId());

        assertEquals(getJobOfTestPerson.getId(), actualJobOfTestPerson.getId());
        assertEquals(getJobOfTestPerson.getName(), actualJobOfTestPerson.getName());
        assertEquals(getJobOfTestPerson.getDomain(), actualJobOfTestPerson.getDomain());
        assertEquals(getJobOfTestPerson.getBaseSalary(), actualJobOfTestPerson.getBaseSalary());
    }

    @Test
    public void getPersonJobInvalidIdTest (){

        assertThrows(DatabaseOperationException.class,
                () -> personService.getPersonJob(PersonDaoIntegrationTest.generateInvalidTestId()));
    }

    //retrieve an existing database person, get salary by tested method and JobService method, compare the 2 obtained salaries;
    //invalid scenario - get salary of non-existing database person;
    @Test
    public void getPersonSalaryTest() throws DatabaseOperationException {

        Person testPerson = personService.getPersonById(PersonDaoIntegrationTest.generateExistentTestId());

        double getSalaryOfTestPerson = personService.getPersonSalary(testPerson.getId());
        double actualSalaryOfTestPerson = new JobService(new JobDao()).calculateSalary(testPerson, new JobDao().getJobById(testPerson.getJobId()));

        assertEquals(getSalaryOfTestPerson, actualSalaryOfTestPerson);
    }

    @Test
    public void getPersonSalaryInvalidIdTest (){

        assertThrows(DatabaseOperationException.class,
                () -> personService.getPersonSalary(PersonDaoIntegrationTest.generateInvalidTestId()));
    }

    //retrieve an existing database person, get work experience by tested method, compare to actual work experience of person;
    //invalid scenario - get work experience of non-existing database person;
    @Test
    public void getPersonWorkExperienceTest() throws DatabaseOperationException {

        Person testPerson = personService.getPersonById(1);

        String getWorkExpOfTestPerson = personService.getPersonWorkExperience(testPerson.getId());
        String actualWorkExpOfTestPerson = testPerson.getName() + WorkExperience.ENTRY.getWorkExperience();

        assertEquals(getWorkExpOfTestPerson, actualWorkExpOfTestPerson);
        assertTrue(getWorkExpOfTestPerson.contains("they have been working"));
    }

    @Test
    public void getPersonWorkExperienceInvalidIdTest(){

        assertThrows(DatabaseOperationException.class,
                () -> personService.getPersonWorkExperience(PersonDaoIntegrationTest.generateInvalidTestId()));
    }
}

