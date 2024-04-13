package com.itfactory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Defining user-friendliness through application-use instructions for "job" database;
 */

@Controller
@RequestMapping("/job-info")
class JobInfoController {

    @GetMapping
    public String getJobInfo(){
        return "job-index";
    }
}