package com.itfactory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Defining user-friendliness through application-use instructions;
 */
@Controller
class WelcomeRestController {
    @GetMapping
    public String welcomeMessage(){
        return "welcome-index";
    }
}