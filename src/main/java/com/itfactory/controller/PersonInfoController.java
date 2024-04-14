package com.itfactory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Defining user-friendliness through application-use instructions for "person" database;
 */

@Controller
@RequestMapping("/person-info")
class PersonInfoController {

    @GetMapping
    public String getPersonInfo(){

        return "person-index";
    }
}