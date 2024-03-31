package com.itfactory.console;

import com.itfactory.dao.JobDao;
import com.itfactory.dao.PersonDao;
import com.itfactory.exceptions.DatabaseOperationException;
import com.itfactory.exceptions.InvalidReadValueException;
import com.itfactory.model.Job;
import com.itfactory.model.Person;
import com.itfactory.service.JobService;
import com.itfactory.service.PersonService;

import java.util.List;
import java.util.Scanner;

/**
 * Interactive Console can be triggered through this class;
 * It offers multiple menu options as to how we can manipulate the 2 databases - 'person' and 'job';
 * Private helper methods used to print menu options and catch errors from invalid input;
 */

public class InteractiveConsole {

    //creating our Scanner, PersonService and JobService, to be used throughout the menu creation;
    private static final Scanner scanner = new Scanner(System.in);
    private static final PersonService personService = new PersonService(new PersonDao());
    private static final JobService jobService = new JobService(new JobDao());

    /* Person Console - running while valid input is passed to the console; helper method is used to print the menu options;
    If anything other than the available menu options is passed, console stops; */
    public static void startPersonConsole(){

        boolean flag = true;
        System.out.println("Welcome to the user management system interactive console!\n" +
                "This interactive console offers the possibility of manipulating the \"person\" database");

        while (flag) {

            printPersonMenu();

            try {
                int optionInput = Integer.parseInt(scanner.nextLine());

                switch (optionInput) {
                    case 1: {

                        personService.getAllPersons();
                        break;
                    }

                    case 2: {

                        System.out.println("Please specify the person's id, to have it retrieved.");
                        personService.getPersonById(validateIntInput(scanner.nextLine()));
                        break;
                    }

                    case 3: {

                        Person newPerson = new Person();

                        List<Person> actualPersons = personService.getAllPersons();
                        System.out.println("PLEASE SPECIFY THE PERSON'S ID, MANDATORY DIFFERENT FROM THE ABOVE LISTED!");
                        newPerson.setId(validateIntInput(scanner.nextLine()));
                        for(Person person: actualPersons) {
                            if(person.getId() == newPerson.getId()) throw new DatabaseOperationException(
                                    "Person id already used.");
                            }

                        System.out.println("Please specify the person's name.");
                        newPerson.setName(validateStringInput(scanner.nextLine()));

                        System.out.println("Please specify the person's email.");
                        newPerson.setEmail(validateStringInput(scanner.nextLine()));

                        jobService.getAllJobs();
                        System.out.println("PLEASE SPECIFY THE PERSON'S JOB ID, MANDATORY ONE OF THE ABOVE LISTED!");
                        int newPersonJobId = validateJobIdInput(scanner.nextLine());
                        newPerson.setJobId(newPersonJobId);

                        System.out.println("Please specify the person's salary index.");
                        newPerson.setSalaryIndex(validateSalaryIndexDoubleInput(scanner.nextLine()));

                        personService.insertPerson(newPerson);

                        break;
                    }

                    case 4: {

                        System.out.println("Please specify the person's id, to be deleted.");
                        personService.deletePerson(validateIntInput(scanner.nextLine()));
                        break;
                    }

                    case 5: {

                        System.out.println("Please specify the person's id, to be updated by salary index.");
                        int caseFiveId = validateIntInput(scanner.nextLine());
                        System.out.println("Please specify the person's new salary index.");
                        double caseFiveNewSalaryIndex = validateSalaryIndexDoubleInput(scanner.nextLine());
                        personService.updateSalaryIndex(caseFiveId, caseFiveNewSalaryIndex);
                        break;
                    }

                    case 6: {

                        System.out.println("Please specify the person's id, to retrieve their job.");
                        personService.getPersonJob(validateIntInput(scanner.nextLine()));
                        break;
                    }

                    case 7: {

                        System.out.println("Please specify the person's id, to retrieve their salary.");
                        personService.getPersonSalary(validateIntInput(scanner.nextLine()));
                        break;
                    }

                    case 8: {

                        System.out.println("Please specify the person's id, to retrieve their work experience.");
                        personService.getPersonWorkExperience(validateIntInput(scanner.nextLine()));
                        break;
                    }

                    default: {

                        System.out.println("Input value is not an option menu, exiting console.");
                        flag = false;
                        break;
                    }
                }
            } catch (InvalidReadValueException | DatabaseOperationException | NumberFormatException e) {
                if(e instanceof NumberFormatException) {
                    System.out.println("Input value is not an option menu, exiting console.");
                    flag = false;
                    break;
                }
                System.out.println(e.getMessage());
            }
        }
    }

    /*Job Console - running while valid input is passed to the console; helper method is used to print the menu options;
    If anything other than the available menu options is passed, console stops; */
    public static void startJobConsole(){

        boolean flag = true;
        System.out.println("Welcome to the user management system interactive console!\n" +
                "This interactive console offers the possibility of manipulating the \"job\" database");

        while (flag) {

            printJobMenu();

            try {
                int optionInput = Integer.parseInt(scanner.nextLine());

                switch (optionInput) {
                    case 1: {

                        jobService.getAllJobs();
                        break;
                    }

                    case 2: {

                        System.out.println("Please specify the job id, to have it retrieved.");
                        jobService.getJobById(validateIntInput(scanner.nextLine()));
                        break;
                    }

                    case 3: {

                        Job newJob = new Job();

                        List<Job> actualJobs = jobService.getAllJobs();
                        System.out.println("PLEASE SPECIFY THE JOB ID, MANDATORY DIFFERENT FROM THE ABOVE LISTED!");
                        newJob.setId(validateIntInput(scanner.nextLine()));
                        for(Job job: actualJobs) {
                            if(job.getId() == newJob.getId()) throw new DatabaseOperationException(
                                    "Job id already used.");
                        }

                        System.out.println("Please specify the job name.");
                        newJob.setName(validateStringInput(scanner.nextLine()));

                        System.out.println("Please specify the job domain.");
                        newJob.setDomain(validateStringInput(scanner.nextLine()));

                        System.out.println("Please specify the job base salary.");
                        newJob.setBaseSalary(validateBaseSalaryDoubleInput(scanner.nextLine()));

                        jobService.insertJob(newJob);

                        break;
                    }

                    case 4: {

                        System.out.println("Please specify the job id, to be deleted.");
                        jobService.deleteJob(validateIntInput(scanner.nextLine()));
                        break;
                    }

                    case 5: {

                        System.out.println("Please specify the job id, to be updated by base salary.");
                        int caseFiveId = validateIntInput(scanner.nextLine());
                        System.out.println("Please specify the job's new base salary.");
                        double caseFiveNewBaseSalary = validateBaseSalaryDoubleInput(scanner.nextLine());
                        jobService.updateBaseSalary(caseFiveId, caseFiveNewBaseSalary);
                        break;
                    }

                    default: {

                        System.out.println("Value input is not an option menu, exiting console.");
                        flag = false;
                        break;
                    }
                }
            } catch (InvalidReadValueException | DatabaseOperationException | NumberFormatException e) {
                if(e instanceof NumberFormatException) {
                    System.out.println("Input value is not an option menu, exiting console.");
                    flag = false;
                    break;
                }
                System.out.println(e.getMessage());
            }
        }
    }

    //helper methods - print menu options and validate input;
    private static void printPersonMenu() {

        System.out.println("""           
                
                Please select an option, from the following available:
                1. Display all persons from the database.
                2. Display a certain person, by id.
                3. Add a new person.
                4. Delete an existing person.
                5. Update a person's salary index.
                6. Display a certain person's job.
                7. Display a certain person's salary.
                8. Display a certain person's work experience.
                Type any other key to exit!
                """);
    }

    private static void printJobMenu() {

        System.out.println("""

                Please select an option, from the following available:
                1. Display all jobs from the database.
                2. Display a certain job, by id.
                3. Add a new job.
                4. Delete an existing job.
                5. Update a job's base salary.
                Type any other key to exit!
                """);
    }

    private static int validateIntInput(String input) {

        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new InvalidReadValueException();
        }
    }

    private static int validateJobIdInput(String input) {

        while (true) {

            try {

                int intInput = Integer.parseInt(input);
                jobService.getJobById(intInput);
                return intInput;
            } catch (NumberFormatException | DatabaseOperationException e) {

                if(e instanceof DatabaseOperationException) {
                    System.out.println("Input error! Please enter a valid, existent job id: ");
                } else {
                    System.out.println("Input error! Please insert whole numbers for job id: ");
                }
            }
            input = scanner.nextLine();
        }
    }

    private static String validateStringInput(String input) {

        while (true) {

            if (input.matches("[a-zA-Z]+")) return input;
            else {

                System.out.println("Input error! Please insert letters only for name, email or domain: ");
                input = scanner.nextLine();
            }
        }
    }

    private static double validateSalaryIndexDoubleInput(String input) {

        while (true) {

            try {

                double doubleInput = Double.parseDouble(input);
                if (doubleInput < 1 || doubleInput > 3) throw new NumberFormatException();

                return doubleInput;
            } catch (NumberFormatException e) {

                System.out.println("Input error! Please insert real numbers (between 1 and 3) for salary index: ");
            }
            input = scanner.nextLine();
        }
    }

    private static double validateBaseSalaryDoubleInput(String input) {

        while (true) {

            try {

                double doubleInput = Double.parseDouble(input);
                if (doubleInput < 500) throw new NumberFormatException();
                return doubleInput;
            } catch (NumberFormatException e) {

                System.out.println("Input error! Please insert real numbers (greater than 500) for base salary: ");
            }
            input = scanner.nextLine();
        }
    }
}