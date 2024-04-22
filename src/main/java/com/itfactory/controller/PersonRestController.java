package com.itfactory.controller;

import com.itfactory.exceptions.DatabaseOperationException;
import com.itfactory.model.Person;
import com.itfactory.service.PersonService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PersonRestController contains the REST web services for interacting with the person database;
 * Defining HTTP methods and endpoints for our user management system;
 * Controller methods return a ResponseEntity of type String, including an HTTP status code;
 * Each handler method contains a call to the Service method;
 */

@RestController
@RequestMapping("/person")
public class PersonRestController {

    //Creating a PersonService object, as to call the PersonService methods;
    private final PersonService personService;

    //Injecting the PersonService object, by use of @Autowired;
    @Autowired
    public PersonRestController(PersonService personService) {

        this.personService = personService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getPersonById(@PathVariable String id) {

        try {
            Person person = personService.getPersonById(Integer.parseInt(id));
            return ResponseEntity.status(HttpStatus.OK).body("Person retrieved by id successfully: " + "\n" + person);
        } catch (DatabaseOperationException | NumberFormatException e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get person by id: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<String> insertPerson(@RequestBody Person person) {

        try {
            personService.insertPerson(person);
            return ResponseEntity.status(HttpStatus.OK).body("Person inserted successfully.");
        } catch (DatabaseOperationException e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to insert person: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<String> getAllPersons() {

        try {
            StringBuilder getAllHtmlResponse = getAllPersonsHtmlResponse();
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Person database retrieved successfully:\n"
                            + "<pre>" + getAllHtmlResponse + "</pre>");
        } catch (DatabaseOperationException e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    "Failed to retrieve person database: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePerson(@PathVariable String id) {

        try {
            personService.deletePerson(Integer.parseInt(id));
            return ResponseEntity.status(HttpStatus.OK).body("Person deleted successfully");
        } catch (DatabaseOperationException | NumberFormatException e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete person: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/{salaryIndex}")
    public ResponseEntity<String> updateSalaryIndex(@PathVariable String id, @PathVariable String salaryIndex) {

        try {

            if (salaryIndex.toLowerCase().contains("f") || salaryIndex.toLowerCase().contains("d")) {
                throw new DatabaseOperationException("Invalid Input for Salary Index - Please insert numeric values (1 to 3).");
            }

            personService.updateSalaryIndex(Integer.parseInt(id), Double.parseDouble(salaryIndex));
            return ResponseEntity.status(HttpStatus.OK).body("Person's salary index updated successfully");
        } catch (DatabaseOperationException | NumberFormatException e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update person's salary index: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/job")
    public ResponseEntity<String> getPersonJob(@PathVariable String id) {

        try {
            return ResponseEntity.status(HttpStatus.OK).body(personService.getPersonById(Integer.parseInt(id)).getName()
                    + "'s job retrieved successfully:" + "\n" + personService.getPersonJob(Integer.parseInt(id)));
        } catch (DatabaseOperationException | NumberFormatException e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    "Failed to retrieve person's job: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/salary")
    public ResponseEntity<String> getPersonSalary(@PathVariable String id) {

        try {
            return ResponseEntity.status(HttpStatus.OK).body(personService.getPersonById(Integer.parseInt(id)).getName()
                    + "'s salary retrieved successfully: " + "\n" + personService.getPersonSalary(Integer.parseInt(id)));
        } catch (DatabaseOperationException | NumberFormatException e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    "Failed to retrieve person's salary: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/workexperience")
    public ResponseEntity<String> getPersonWorkExperience(@PathVariable String id) {

        try {
            return ResponseEntity.status(HttpStatus.OK).body(personService.getPersonById(Integer.parseInt(id)).getName()
                    + "'s work experience retrieved successfully: " + "\n" + personService.getPersonWorkExperience(Integer.parseInt(id)));
        } catch (DatabaseOperationException | NumberFormatException e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    "Failed to retrieve person's work experience: " + e.getMessage());
        }
    }

    private StringBuilder getAllPersonsHtmlResponse() throws DatabaseOperationException {

        List<Person> persons = personService.getAllPersons();
        StringBuilder htmlResponse = new StringBuilder();

        for (Person personLooped : persons) {

            htmlResponse.append(String.format(
                    "Person id: %2d; name: %18s; email: %25s; job id: %3d; salary index: %3.1f\n",
                    personLooped.getId(), personLooped.getName(), personLooped.getEmail(),
                    personLooped.getJobId(), personLooped.getSalaryIndex()
            ));
        }

        return htmlResponse;
    }
}