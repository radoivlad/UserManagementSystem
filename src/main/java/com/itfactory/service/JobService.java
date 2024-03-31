package com.itfactory.service;

import com.itfactory.dao.JobDao;
import com.itfactory.model.Job;
import com.itfactory.model.Person;
import com.itfactory.exceptions.DatabaseOperationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Job database related service methods for validating data and sending requests to DAO (by request sent through Controller class);
 */

@Service
public class JobService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonService.class);

    private final JobDao jobDao;

    @Autowired
    public JobService(JobDao jobDao) {
        this.jobDao = jobDao;
    }

    public Job getJobById(int id) throws DatabaseOperationException {

        return jobDao.getJobById(id);
    }

    public void insertJob(Job job) throws DatabaseOperationException {

        if(job.getBaseSalary() < 500) throw new DatabaseOperationException(
                "Invalid Input for Base Salary - Please specify a value greater than 500!");
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

        if(getJobById(id).getBaseSalary() == baseSalary) throw new DatabaseOperationException(
                "Job base salary is already: " + baseSalary);
        if(baseSalary < 500) throw new DatabaseOperationException(
                "Invalid Input for Base Salary - Please specify a value greater than 500!");
        return jobDao.updateBaseSalary(id, baseSalary);
    }

    public double calculateSalary(Person person, Job job) {

        System.out.println(person.getName() + "'s salary is: " + person.getSalaryIndex() * job.getBaseSalary());
        return person.getSalaryIndex() * job.getBaseSalary();
    }
}