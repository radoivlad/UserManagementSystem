package com.itfactory.model;

import java.util.Random;

/**
 * Our Job model;
 * Using 2 toString variants (toStringLine used for printing getAllJobs result to console);
 * Encapsulated class - private fields and setters/getters usable;
 */

public class Job extends BaseModel{

    private String domain;
    private double baseSalary;

    public Job() {

        baseSalary = new Random().nextDouble(500, 10000);
    }

    public Job(int id, String name, String domain, double baseSalary) {

        super(id, name);
        this.domain = domain;
        this.baseSalary = baseSalary;
    }

    public String getDomain() {
        return domain;
    }

    public double getBaseSalary() {
        return baseSalary;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setBaseSalary(double baseSalary) {

        this.baseSalary = baseSalary;
    }

    @Override
    public String toString() {
        return String.format("Job database entry for:\n %s,\n id = %d,\n domain = %s,\n base salary = %.1f\n",
                getName(), getId(), domain, baseSalary);
    }


    public String toStringLine() {
        return String.format("Job database entry for: %20s, id = %4d, domain = %10s, base salary = %5.1f",
                getName(), getId(), domain, baseSalary);
    }

}
