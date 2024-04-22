package com.itfactory.service;

import com.itfactory.dao.JobDao;
import com.itfactory.model.Job;
import com.itfactory.model.Person;
import com.itfactory.model.UserManager;
import com.itfactory.model.WorkExperience;
import com.itfactory.exceptions.DatabaseOperationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Providing extra functionality by implementing UserManager interface;
 */

@Service
public class PersonManager implements UserManager {

    //instantiating JobDao and JobService objects, to be injected;
    protected JobDao jobDao;

    protected JobService jobService;

    //instantiating a Person object;
    private Person person;

    //injecting jobDao and jobService by means of @Autowired and instantiating person (provided with own setter);
    @Autowired
    public PersonManager(JobService jobService, JobDao jobDao) {

        this.jobService = jobService;
        this.jobDao = jobDao;
        this.person = new Person();
    }

    public void setPerson(Person person) {

        this.person = person;
    }

    //writing the 3 methods called in PersonService, for providing information on top of the CRUD methods;
    @Override
    public Job getJob() throws DatabaseOperationException {

        return jobDao.getJobById(person.getJobId());
    }

    @Override
    public double getSalary() throws DatabaseOperationException {

        return jobService.calculateSalary(person, jobDao.getJobById(person.getJobId()));
    }

    //making use of the WorkExperience enum, in the model package;
    @Override
    public String getWorkExperience() throws DatabaseOperationException {

        if (person.getSalaryIndex() < 1 || person.getSalaryIndex() > 3) {

            throw new DatabaseOperationException("Invalid salary index value, outside of range 1 - 3");
        }

        if (person.getSalaryIndex() < 1.4) {

            System.out.println(person.getName() + WorkExperience.ENTRY.getWorkExperience());
            return person.getName() + WorkExperience.ENTRY.getWorkExperience();
        }

        if (person.getSalaryIndex() < 1.8) {

            System.out.println(person.getName() + WorkExperience.ENTRYTOMID.getWorkExperience());
            return person.getName() + WorkExperience.ENTRYTOMID.getWorkExperience();
        }

        if (person.getSalaryIndex() < 2.2) {

            System.out.println(person.getName() + WorkExperience.MIDDLE.getWorkExperience());
            return person.getName() + WorkExperience.MIDDLE.getWorkExperience();
        }

        System.out.println(person.getName() + WorkExperience.SENIOR.getWorkExperience());
        return person.getName() + WorkExperience.SENIOR.getWorkExperience();
    }
}