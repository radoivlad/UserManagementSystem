package com.itfactory.model;

import com.itfactory.exceptions.DatabaseOperationException;

/**
 * Further ways of interacting with the application and receiving user-related information;
 */

public interface UserManager {
    Job getJob() throws DatabaseOperationException;

    double getSalary() throws DatabaseOperationException;

    String getWorkExperience();
}
