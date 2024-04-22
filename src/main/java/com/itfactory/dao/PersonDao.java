package com.itfactory.dao;

import com.itfactory.exceptions.DatabaseOperationException;
import com.itfactory.model.Person;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PersonDao contains the database manipulation functionality (CRUD) for our person database;
 * Necessary explanations provided throughout code;
 */

@Repository
public class PersonDao {

    //Creating the Logger, to log messages regarding the different application execution steps;
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonDao.class);

    //Calling the database connection values from application.properties, by use of @Value;
    @Value("${db.url}")
    private String DB_URL;

    @Value("${db.user}")
    private String DB_USER;

    @Value("${db.pass}")
    private String DB_PASS;

    //Creating CRUD (Create, Read, Update, Delete) methods for our person database;
    public Person getPersonById(int id) throws DatabaseOperationException {

        try {
            //Creating a connection to the database, using the URL, USER and PASS established above;
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            LOGGER.info("Connected to MySQL database;");

            //Preparing a statement to be executed, inputting QUERY for the database;
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM person WHERE id = ?");
            LOGGER.info("Prepared statement;");

            //adding id value to the statement, above;
            statement.setInt(1, id);

            //Creating the resultSet, as the result of our statement execution (in our case - getting the entries where id = given id);
            LOGGER.info("Executing query: SELECT * FROM person WHERE id = " + id);
            ResultSet resultSet = statement.executeQuery();

            /*Checking if resultSet has any results returned (by resultSet.next() boolean), then creating a Person object,
            using the obtained resultSet information;*/
            if (resultSet.next()) {

                Person retrievedPerson = new Person();
                retrievedPerson.setId(resultSet.getInt("id"));
                retrievedPerson.setName(resultSet.getString("name"));
                retrievedPerson.setEmail(resultSet.getString("email"));
                retrievedPerson.setJobId(resultSet.getInt("jobId"));
                retrievedPerson.setSalaryIndex(resultSet.getDouble("salaryIndex"));

                //returning the created Person object;
                LOGGER.info("Retrieved person by id successfully;");
                System.out.println(retrievedPerson);
                return retrievedPerson;
            } else {

                throw new DatabaseOperationException("No person found with given id.");
            }

        } catch (SQLException e) {

            LOGGER.error("Connection failure", e);
            throw new DatabaseOperationException(e.getMessage());
            /*Integrating the whole code block in a try-catch statement, as to catch any
            errors in the process of executing the try block;*/
        }
    }

    public void insertPerson(Person person) throws DatabaseOperationException {

        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            LOGGER.info("Connected to MySQL database;");

            PreparedStatement statement = connection.prepareStatement("INSERT INTO person VALUES(?, ?, ?, ?, ?)");
            LOGGER.info("Prepared statement;");

            //Inserting a person into the database - preparing the statement with all the relevant data from given Person parameter;
            statement.setInt(1, person.getId());
            statement.setString(2, person.getName());
            statement.setString(3, person.getEmail());
            statement.setInt(4, person.getJobId());
            statement.setDouble(5, person.getSalaryIndex());

            //Statement execution, inserting the person into the database;
            statement.execute();
            System.out.println(person);
            LOGGER.info("Entry added successfully;");

        } catch (SQLException e) {

            LOGGER.error("Connection failure", e);
            throw new DatabaseOperationException(e.getMessage());
        }
    }

    public List<Person> getAllPersons() throws DatabaseOperationException {

        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            LOGGER.info("Connected to MySQL database;");

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM person");
            LOGGER.info("Prepared statement;");

            //Assigning to resultSet the result of executing the above query - getting all database entries;
            LOGGER.info("Executing query: SELECT * FROM person");
            ResultSet resultSet = statement.executeQuery();

            List<Person> retrievedList = new ArrayList<>();

            while (resultSet.next()) {

                //Creating a Person object from each database entry;
                Person personRetrieved = new Person();
                personRetrieved.setId(resultSet.getInt("id"));
                personRetrieved.setName(resultSet.getString("name"));
                personRetrieved.setEmail(resultSet.getString("email"));
                personRetrieved.setJobId(resultSet.getInt("jobId"));
                personRetrieved.setSalaryIndex(resultSet.getDouble("salaryIndex"));

                //adding Person object to the list;
                retrievedList.add(personRetrieved);
            }

            /*printing list of all database entries (integrating a lambda expression in place of a
            Consumer functional interface);*/
            retrievedList.forEach(person -> System.out.println(person.toStringLine()));
            LOGGER.info("Retrieved database entries successfully;");
            return retrievedList;

        } catch (SQLException e) {

            LOGGER.error("Connection failure", e);
            throw new DatabaseOperationException(e.getMessage());
        }
    }

    public void deletePerson(int id) throws DatabaseOperationException {

        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            LOGGER.info("Connected to MySQL database;");

            PreparedStatement statement = connection.prepareStatement("DELETE FROM person WHERE id = ?");
            LOGGER.info("Prepared statement;");

            //inputting into the statement the id of the Person to be removed;
            statement.setInt(1, id);

            getPersonById(id);
            //executing the statement, removing the entry;
            statement.execute();
            LOGGER.info("Entry deleted successfully, for id = " + id);

        } catch (SQLException | DatabaseOperationException e) {

            LOGGER.error("Connection failure", e);
            throw new DatabaseOperationException(e.getMessage());
        }
    }

    public Person updateSalaryIndex(int id, double salaryIndex) throws DatabaseOperationException {

        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            LOGGER.info("Connected to MySQL database;");

            PreparedStatement statement = connection.prepareStatement("UPDATE person SET salaryIndex = ? WHERE id = ?");
            LOGGER.info("Prepared statement;");

            //preparing the statement with given id and new salary index for the entry;
            statement.setDouble(1, salaryIndex);
            statement.setInt(2, id);

            //executing the statement, updating salary index for given id entry;
            statement.execute();

            //returning the updated person entry;
            LOGGER.info("Updating salary index successfully, for id = " + id);
            return getPersonById(id);

        } catch (SQLException e) {

            LOGGER.error("Connection failure", e);
            throw new DatabaseOperationException(e.getMessage());
        }
    }
}