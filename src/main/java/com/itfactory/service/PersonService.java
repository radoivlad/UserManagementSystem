package com.itfactory.service;

import com.itfactory.dao.PersonDao;
import com.itfactory.model.Job;
import com.itfactory.model.Person;
import com.itfactory.model.PersonManager;
import com.itfactory.exceptions.DatabaseOperationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * Separating functionalities of PersonDao and PersonService;
 * Calling the database manipulation methods of PersonDao, through PersonService (by request sent through Controller class);
 * PersonService can have built-in functionality for validating data;
 */

@Service
public class PersonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonService.class);

    //instantiating a PersonDao object;
    private final PersonDao personDao;

    //Defining the constructor, @Autowired permits injection of the PersonDao object;
    @Autowired
    public PersonService(PersonDao personDao) {

        this.personDao = personDao;
    }

    //Creating calling methods for each of the PersonDao CRUD methods;
    public Person getPersonById(int id) throws DatabaseOperationException {

        return personDao.getPersonById(id);
    }

    public void insertPerson(Person person) throws DatabaseOperationException {

        validateInsertPersonInput(person);
        personDao.insertPerson(person);
    }

    public List<Person> getAllPersons() throws DatabaseOperationException {

        return personDao.getAllPersons();
    }

    public void deletePerson(int id) throws DatabaseOperationException {

        personDao.getPersonById(id);
        personDao.deletePerson(id);
    }

    public Person updateSalaryIndex(int id, double salaryIndex) throws DatabaseOperationException {

        validateUpdateSalaryIndexInput(id, salaryIndex);
        return personDao.updateSalaryIndex(id, salaryIndex);
    }

    public Job getPersonJob(int id) throws DatabaseOperationException {

        return new PersonManager(getPersonById(id)).getJob();
    }

    public double getPersonSalary(int id) throws DatabaseOperationException {

        return new PersonManager(getPersonById(id)).getSalary();
    }

    public String getPersonWorkExperience(int id) throws DatabaseOperationException {

        return new PersonManager(getPersonById(id)).getWorkExperience();
    }

    private static void validateInsertPersonInput (Person person) throws DatabaseOperationException {

        try {

            if (!person.getName().matches("[a-zA-Z\\s]+") || !person.getEmail().matches("[a-zA-Z\\s@.]+")) {
                throw new DatabaseOperationException("Please enter letters for name or email!");
            }
            if (person.getSalaryIndex() < 1.0 || person.getSalaryIndex() > 3.0) {
                throw new DatabaseOperationException(
                        "Invalid Input for Salary Index - Please specify a value from 1 to 3!");
            }
        } catch(DatabaseOperationException | NumberFormatException e) {
            throw new DatabaseOperationException(e.getMessage());
        }
    }

    private void validateUpdateSalaryIndexInput (int id, double salaryIndex) throws DatabaseOperationException {

        if(getPersonById(id).getSalaryIndex() == salaryIndex) {
            throw new DatabaseOperationException(
                    "Person's salary index is already " + salaryIndex);
        }
        if(salaryIndex < 1.0 || salaryIndex > 3.0) throw new DatabaseOperationException(
                "Invalid Input for Salary Index - Please specify a value from 1 to 3!");
    }
}
