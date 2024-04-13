package com.itfactory.model;

import com.itfactory.dao.JobDao;
import com.itfactory.service.JobService;
import com.itfactory.exceptions.DatabaseOperationException;

/**
 * Implementing extra functionality by implementing UserManager interface;
 */

public class PersonManager implements UserManager{

    protected JobDao jobDao = new JobDao();

    protected JobService jobService = new JobService(jobDao);

    private final Person person;

    public PersonManager(Person person) {
        this.person = person;
    }

    @Override
    public Job getJob() throws DatabaseOperationException {

        return jobDao.getJobById(person.getJobId());
    }

    @Override
    public double getSalary() throws DatabaseOperationException {

        return jobService.calculateSalary(person, jobDao.getJobById(person.getJobId()));
    }

    @Override
    public String getWorkExperience() {

        if(person.getSalaryIndex() < 1 || person.getSalaryIndex() > 3) return "Invalid salary index value, outside of range 1 - 3";

        if(person.getSalaryIndex() < 1.4) {
            System.out.println(person.getName() + WorkExperience.ENTRY.getWorkExperience());
            return person.getName() + WorkExperience.ENTRY.getWorkExperience();
        }
        if(person.getSalaryIndex() < 1.8) {
            System.out.println(person.getName() + WorkExperience.ENTRYTOMID.getWorkExperience());
            return person.getName() + WorkExperience.ENTRYTOMID.getWorkExperience();
        }
        if(person.getSalaryIndex() < 2.2) {
            System.out.println(person.getName() + WorkExperience.MIDDLE.getWorkExperience());
            return person.getName() + WorkExperience.MIDDLE.getWorkExperience();
        }

        System.out.println(person.getName() + WorkExperience.SENIOR.getWorkExperience());
        return person.getName() + WorkExperience.SENIOR.getWorkExperience();
    }
}
