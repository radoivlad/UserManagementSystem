package com.itfactory.controller;

import com.itfactory.exceptions.DatabaseOperationException;
import com.itfactory.model.Job;
import com.itfactory.service.JobService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * JobRestController contains the REST web services for interacting with the job database;
 * Defining HTTP methods and endpoints for our user management system;
 * Controller methods return a ResponseEntity of type String, including an HTTP status code;
 * Each handler method contains a call to the Service method;
 */

@RestController
@RequestMapping("/job")
public class JobRestController {

    private final JobService jobService;

    @Autowired
    public JobRestController(JobService jobService) {

        this.jobService = jobService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getJobById(@PathVariable String id) {

        try{
            Job job = jobService.getJobById(Integer.parseInt(id));
            return ResponseEntity.status(HttpStatus.OK).body("Job retrieved by id successfully: " + "\n" + job);
        } catch (DatabaseOperationException | NumberFormatException e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get job by id: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<String> insertJob(@RequestBody Job job){

        try{
            jobService.insertJob(job);
            return ResponseEntity.status(HttpStatus.OK).body("Job inserted successfully.");
        } catch (DatabaseOperationException e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to insert job: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<String> getAllJobs() {

        try{
            StringBuilder getAllHtmlResponse = getAllJobsHtmlResponse();
            return ResponseEntity.status(HttpStatus.OK).body("Job database retrieved successfully:\n"
                    + "<pre>\n" + getAllHtmlResponse +"<pre>");
        } catch(DatabaseOperationException e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    "Failed to retrieve job database: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable String id){

        try{
            jobService.deleteJob(Integer.parseInt(id));
            return ResponseEntity.status(HttpStatus.OK).body("Job deleted successfully.");
        } catch (DatabaseOperationException | NumberFormatException e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete job: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/{baseSalary}")
    public ResponseEntity<String> updateBaseSalary(@PathVariable String id, @PathVariable String baseSalary){

        try{
            if(baseSalary.toLowerCase().contains("f") || baseSalary.toLowerCase().contains("d")) {
                throw new DatabaseOperationException("Invalid Input for Base Salary - Please insert numeric values (minimum 500).");
            }

            jobService.updateBaseSalary(Integer.parseInt(id), Double.parseDouble(baseSalary));
            return ResponseEntity.status(HttpStatus.OK).body("Job base salary updated successfully.");
        } catch (DatabaseOperationException | NumberFormatException e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update job base salary: " + e.getMessage());
        }
    }

    private StringBuilder getAllJobsHtmlResponse() throws DatabaseOperationException {

        StringBuilder htmlResponse = new StringBuilder();
        List<Job> jobs = jobService.getAllJobs();

        for (Job jobLooped: jobs) {
            htmlResponse.append(String.format(
                    "Job id: %2d; name: %22s; domain: %15s; base salary: %3.1f\n",
                    jobLooped.getId(), jobLooped.getName(), jobLooped.getDomain(), jobLooped.getBaseSalary()
            ));
        }

        return htmlResponse;
    }
}
