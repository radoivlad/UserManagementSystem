package com.itfactory.model;

import java.util.Objects;
import java.util.Random;

/**
 * Our Person model;
 * Using 2 toString variants (toStringLine used for printing getAllPersons result to console);
 * Encapsulated class - private fields and setters/getters usable;
 */

public class Person extends BaseModel {

    private String email;

    private int jobId;

    private double salaryIndex;

    public Person() {

        salaryIndex = new Random().nextInt(1, 3);
    }

    public Person(int id, String name, String email, int jobId, double salaryIndex) {

        super(id, name);
        this.email = email;
        this.jobId = jobId;
        this.salaryIndex = salaryIndex;
    }

    public String getEmail() {

        return email;
    }

    public int getJobId() {

        return jobId;
    }

    public double getSalaryIndex() {

        return salaryIndex;
    }

    public void setSalaryIndex(double salaryIndex) {

        this.salaryIndex = salaryIndex;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public void setJobId(int jobId) {

        this.jobId = jobId;
    }

    @Override
    public String toString() {

        return String.format("\nPerson database entry for:\n name = %s,\n id = %d,\n email = %s,\n job id = %d,\n salary index = %.1f\n\n" + " ",
                getName(), getId(), email, jobId, salaryIndex);
    }

    public String toStringLine() {

        return String.format("Person database entry for: name = %18s, id = %4d, email = %25s, job id = %3d, salary index = %5.1f",
                getName(), getId(), email, jobId, salaryIndex);
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return jobId == person.jobId && Double.compare(salaryIndex, person.salaryIndex) == 0 && Objects.equals(email, person.email);
    }

    @Override
    public int hashCode() {

        return Objects.hash(email, jobId, salaryIndex);
    }
}