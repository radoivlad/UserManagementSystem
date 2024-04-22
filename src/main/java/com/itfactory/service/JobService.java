package com.itfactory.service;

import com.itfactory.dao.JobDao;
import com.itfactory.exceptions.DatabaseOperationException;
import com.itfactory.model.Job;
import com.itfactory.model.Person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Job database related service methods for validating data and sending requests to DAO (by request sent through Controller class);
 * PersonService comments can be useful;
 */

@Service
public class JobService {

    private final JobDao jobDao;

    @Autowired
    public JobService(JobDao jobDao) {

        this.jobDao = jobDao;
    }

    public Job getJobById(int id) throws DatabaseOperationException {

        return jobDao.getJobById(id);
    }

    public void insertJob(Job job) throws DatabaseOperationException {

        validateInsertJobInput(job);

        jobDao.insertJob(job);
    }

    public List<Job> getAllJobs() throws DatabaseOperationException {

        return jobDao.getAllJobs();
    }

    public void deleteJob(int id) throws DatabaseOperationException {

        jobDao.getJobById(id);

        jobDao.deleteJob(id);
    }

    public Job updateBaseSalary(int id, double baseSalary) throws DatabaseOperationException {

        validateUpdateBaseSalaryInput(id, baseSalary);

        return jobDao.updateBaseSalary(id, baseSalary);
    }

    public double calculateSalary(Person person, Job job) throws DatabaseOperationException {

        validateCalculateSalaryInput(person, job);

        System.out.println(person.getName() + "'s salary is: " + person.getSalaryIndex() * job.getBaseSalary());
        return person.getSalaryIndex() * job.getBaseSalary();
    }

    //validation methods for JobService;
    private static void validateInsertJobInput (Job job) throws DatabaseOperationException {

        try {

            if (!job.getName().matches("[a-zA-Z\\s]+")) {

                throw new DatabaseOperationException("Invalid Input for Name - Please insert letters only!");
            }

            if(!job.getDomain().matches("[a-zA-Z\\s]+")) {

                throw new DatabaseOperationException("Invalid Input for Domain - Please insert letters only!");
            }

            if (job.getBaseSalary() < 500) {

                throw new DatabaseOperationException(
                        "Invalid Input for Base Salary - Please specify a value greater than 500!");
            }
        } catch(DatabaseOperationException | NumberFormatException e) {

            throw new DatabaseOperationException(e.getMessage());
        }
    }

    private void validateUpdateBaseSalaryInput (int id, double baseSalary) throws DatabaseOperationException {

        if(getJobById(id).getBaseSalary() == baseSalary) {

            throw new DatabaseOperationException(
                    "Job base salary is already: " + baseSalary);
        }

        if(baseSalary < 500) throw new DatabaseOperationException(

                "Invalid Input for Base Salary - Please specify a value greater than 500!");
    }

    private void validateCalculateSalaryInput (Person person, Job job) throws DatabaseOperationException {

        if(person.getSalaryIndex() < 1 || person.getSalaryIndex() > 3) {

            throw new DatabaseOperationException("Error: salary index outside of range 1 - 3.");
        }

        if(job.getBaseSalary() < 500) {

            throw new DatabaseOperationException("Error: base salary lesser than 500.");
        }
    }
}