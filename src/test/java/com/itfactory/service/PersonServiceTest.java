package com.itfactory.service;

import com.itfactory.dao.JobDao;
import com.itfactory.dao.JobDaoTest;
import com.itfactory.dao.PersonDao;
import com.itfactory.dao.PersonDaoTest;
import com.itfactory.model.Job;
import com.itfactory.model.Person;
import com.itfactory.model.WorkExperience;
import com.itfactory.exceptions.DatabaseOperationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Writing JUnit tests for each of the 8 service methods in PersonService class;
 * Included - tested scenarios where invalid id values are being passed in PersonService methods;
 */

@SpringBootTest
public class PersonServiceTest {
    @Autowired
    PersonService personService;

    @Test
    public void findPersonByIdTest() throws DatabaseOperationException {

        int existentId = PersonDaoTest.generateExistentTestId();

        assertDoesNotThrow(() -> personService.getPersonById(existentId));

        Person foundTestPersonById = personService.getPersonById(existentId);

        assertNotNull(foundTestPersonById);
        assertEquals(existentId, foundTestPersonById.getId());
    }

    @Test
    public void findPersonByInvalidIdTest (){

        assertThrows(DatabaseOperationException.class,
                () -> personService.getPersonById(PersonDaoTest
                        .generateInvalidTestId()));
    }

    @Test
    public void deletePersonTest () throws DatabaseOperationException {

        Person testPerson = new Person();
        testPerson.setId(PersonDaoTest.generateInvalidTestId());
        testPerson.setName("Test Person");
        testPerson.setEmail("Test Email");
        testPerson.setJobId(JobDaoTest.generateExistentTestId());

        personService.insertPerson(testPerson);

        personService.deletePerson(testPerson.getId());

        assertThrows(DatabaseOperationException.class,
                () -> personService.getPersonById(testPerson.getId()));
    }

    @Test
    public void deletePersonByInvalidIdTest (){

        assertThrows(DatabaseOperationException.class,
                () -> personService.deletePerson(PersonDaoTest.generateInvalidTestId()));
    }

    @Test
    public void getAllPersonsTest () throws DatabaseOperationException {

        int actualListSize = personService.getAllPersons().size();

        Person testPerson1 = new Person();
        testPerson1.setId(PersonDaoTest.generateInvalidTestId());
        testPerson1.setName("Test Person One");
        testPerson1.setEmail("Test Email");
        testPerson1.setJobId(JobDaoTest.generateExistentTestId());
        personService.insertPerson(testPerson1);

        Person testPerson2 = new Person();
        testPerson2.setId(PersonDaoTest.generateInvalidTestId());
        testPerson2.setName("Test Person Two");
        testPerson2.setEmail("Test Email");
        testPerson2.setJobId(JobDaoTest.generateExistentTestId());
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
        testPerson.setId(PersonDaoTest.generateInvalidTestId());
        testPerson.setName("Test Person");
        testPerson.setEmail("test@email.com");
        testPerson.setJobId(JobDaoTest.generateExistentTestId());
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
                () -> personService.insertPerson(personService.getPersonById(PersonDaoTest.generateExistentTestId()))
        );
    }

    @Test
    public void updateSalaryIndexTest () throws DatabaseOperationException {

        Person testPerson = new Person();
        testPerson.setId(PersonDaoTest.generateInvalidTestId());
        testPerson.setName("Test Person");
        testPerson.setEmail("Test Email");
        testPerson.setJobId(JobDaoTest.generateExistentTestId());
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

        assertThrows(DatabaseOperationException.class,
                () -> personService.updateSalaryIndex(PersonDaoTest.generateInvalidTestId(), 2.0));

        Person testPerson = new Person();
        testPerson.setId(PersonDaoTest.generateInvalidTestId());
        testPerson.setName("Test Person");
        testPerson.setEmail("Test Email");
        testPerson.setJobId(JobDaoTest.generateExistentTestId());
        testPerson.setSalaryIndex(2.0);

        personService.insertPerson(testPerson);

        assertThrows(DatabaseOperationException.class,
                () -> personService.updateSalaryIndex(testPerson.getId(), 2.0));

        personService.deletePerson(testPerson.getId());
    }

    @Test
    public void getPersonJobTest() throws DatabaseOperationException {

        Person testPerson = personService.getPersonById(PersonDaoTest.generateExistentTestId());

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
                () -> personService.getPersonJob(PersonDaoTest.generateInvalidTestId()));
    }

    @Test
    public void getPersonSalaryTest() throws DatabaseOperationException {

        Person testPerson = personService.getPersonById(PersonDaoTest.generateExistentTestId());

        double getSalaryOfTestPerson = personService.getPersonSalary(testPerson.getId());
        double actualSalaryOfTestPerson = new JobService(new JobDao()).calculateSalary(testPerson, new JobDao().getJobById(testPerson.getJobId()));

        assertEquals(getSalaryOfTestPerson, actualSalaryOfTestPerson);
    }

    @Test
    public void getPersonSalaryInvalidIdTest (){

        assertThrows(DatabaseOperationException.class,
                () -> personService.getPersonSalary(PersonDaoTest.generateInvalidTestId()));
    }

    @Test
    public void getPersonWorkExperienceTest() throws DatabaseOperationException {

        Person testPerson = personService.getPersonById(1);

        String getWorkExpOfTestPerson = personService.getPersonWorkExperience(testPerson.getId());
        String actualWorkExpOfTestPerson = testPerson.getName() + WorkExperience.ENTRYTOMID.getWorkExperience();

        assertEquals(getWorkExpOfTestPerson, actualWorkExpOfTestPerson);
        assertTrue(getWorkExpOfTestPerson.contains("they have been working"));
    }

    @Test
    public void getPersonWorkExperienceInvalidIdTest(){

        assertThrows(DatabaseOperationException.class,
                () -> personService.getPersonWorkExperience(PersonDaoTest.generateInvalidTestId()));
    }
}

