package com.itfactory.utility;

import com.itfactory.exceptions.DatabaseOperationException;

/**
 * Creating a utility class for generating valid and invalid test id values, for our Integration and Mock Tests;
 * The helper methods allow for efficiency, avoiding unnecessary database calls and returning hardcoded values;
 */

public class TestIdGenerator {

    public static int generateInvalidTestId() throws DatabaseOperationException {

        return -1;
    }

    public static int generateExistentTestId() throws DatabaseOperationException {

        return 1;
    }
}