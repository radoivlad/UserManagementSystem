package com.itfactory.dao;

import com.itfactory.model.Job;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.itfactory.exceptions.DatabaseOperationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Repository;

/**
 * JobDao contains the database manipulation functionality (CRUD) for our job database;
 */

@Repository
public class JobDao{

    private static final Logger LOGGER = LoggerFactory.getLogger(JobDao.class);
    private static final String DB_URL = "jdbc:mysql://localhost:3306/userms";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "rootroot";

    public Job getJobById(int id) throws DatabaseOperationException {

        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            LOGGER.info("Connected to MySQL database;");

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM job WHERE id = ?");
            LOGGER.info("Prepared statement;");

            statement.setInt(1, id);

            LOGGER.info("Executing query: SELECT * FROM job WHERE id = " + id);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {

                Job retrievedJob = new Job();
                retrievedJob.setId(resultSet.getInt("id"));
                retrievedJob.setName(resultSet.getString("name"));
                retrievedJob.setDomain(resultSet.getString("domain"));
                retrievedJob.setBaseSalary(resultSet.getDouble("baseSalary"));

                LOGGER.info("Retrieved job by id successfully;");
                System.out.println(retrievedJob);
                return retrievedJob;
            } else {
                throw new DatabaseOperationException("No job was found in the database with the given id.");
            }

        } catch (SQLException e) {
            LOGGER.error("Connection failure", e);
            throw new DatabaseOperationException(e.getMessage());
        }
    }

    public void insertJob(Job job) throws DatabaseOperationException {

        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            LOGGER.info("Connected to MySQL database;");

            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO job VALUES(?, ?, ?, ?)");
            LOGGER.info("Prepared statement;");

            statement.setInt(1, job.getId());
            statement.setString(2, job.getName());
            statement.setString(3, job.getDomain());
            statement.setDouble(4, job.getBaseSalary());

            statement.execute();
            System.out.println(job);
            LOGGER.info("Entry added successfully;");

        } catch (SQLException e) {
            LOGGER.error("Connection failure", e);
            throw new DatabaseOperationException(e.getMessage());
        }
    }

    public List<Job> getAllJobs() throws DatabaseOperationException {

        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            LOGGER.info("Connected to MySQL database;");

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM job");
            LOGGER.info("Prepared statement;");

            LOGGER.info("Executing query: SELECT * FROM job");
            ResultSet resultSet = statement.executeQuery();

            List<Job> retrievedList = new ArrayList<>();

            while(resultSet.next()) {

                Job jobRetrieved = new Job();
                jobRetrieved.setId(resultSet.getInt("id"));
                jobRetrieved.setName(resultSet.getString("name"));
                jobRetrieved.setDomain(resultSet.getString("domain"));
                jobRetrieved.setBaseSalary(resultSet.getInt("baseSalary"));

                retrievedList.add(jobRetrieved);
            }

            retrievedList.forEach(job -> System.out.println(job.toStringLine()));
            LOGGER.info("Retrieved database entries successfully;");
            return retrievedList;

        } catch (SQLException e) {
            LOGGER.error("Connection failure", e);
            throw new DatabaseOperationException(e.getMessage());
        }
    }

    public void deleteJob(int id) throws DatabaseOperationException {

        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            LOGGER.info("Connected to MySQL database;");

            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM job WHERE id = ?");
            LOGGER.info("Prepared statement;");

            statement.setInt(1, id);

            statement.execute();
            LOGGER.info("Entry deleted successfully, for id = " + id);

        } catch (SQLException e) {
            LOGGER.error("Connection failure", e);
            throw new DatabaseOperationException(e.getMessage());
        }
    }

    public Job updateBaseSalary(int id, double baseSalary) throws DatabaseOperationException {

        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            LOGGER.info("Connected to MySQL database;");

            PreparedStatement statement = connection.prepareStatement("UPDATE job SET baseSalary = ? WHERE id = ?");
            LOGGER.info("Prepared statement;");

            statement.setDouble(1, baseSalary);
            statement.setInt(2, id);

            statement.execute();

            LOGGER.info("Updating base salary successfully, for id = " + id);
            return getJobById(id);

        } catch (SQLException e) {
            LOGGER.error("Connection failure", e);
            throw new DatabaseOperationException(e.getMessage());
        }
    }
}