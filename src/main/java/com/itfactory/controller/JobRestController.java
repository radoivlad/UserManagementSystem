package com.itfactory.controller;

import com.itfactory.model.Job;
import com.itfactory.service.JobService;
import com.itfactory.exceptions.DatabaseOperationException;
import com.itfactory.console.InteractiveConsole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * JobRestController contains the REST web services for interacting with the job database;
 * Defining HTTP methods and endpoints for our user management system;
 * Controller methods return a ResponseEntity of type String, including an HTTP status code;
 */

@Controller
@RequestMapping("/job")
public class JobRestController {

    private final JobService jobService;

    @Autowired
    public JobRestController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public String welcomeMessage(){
        return "job-index";
    }

    @GetMapping("/menu")
    public String getJobMenu() {

        InteractiveConsole.startJobConsole();
        return "job-index";
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getJobById(@PathVariable int id) {
        try{
            Job job = jobService.getJobById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Job retrieved by id successfully: " + "\n" + job);
        } catch (DatabaseOperationException e) {
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
    public ResponseEntity<String> deleteJob(@PathVariable int id){
        try{
            jobService.deleteJob(id);
            return ResponseEntity.status(HttpStatus.OK).body("Job deleted successfully.");
        } catch (DatabaseOperationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete job: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/{baseSalary}")
    public ResponseEntity<String> updateBaseSalary(@PathVariable int id, @PathVariable double baseSalary){
        try{
            jobService.updateBaseSalary(id, baseSalary);
            return ResponseEntity.status(HttpStatus.OK).body("Job base salary updated successfully.");
        } catch (DatabaseOperationException e) {
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
