package com.itfactory.model;

/**
 * Enum used in the UserManager-implemented method from PersonManager (getting work experience);
 */

public enum WorkExperience {

    ENTRY, ENTRYTOMID, MIDDLE, SENIOR;

    public String getWorkExperience(){

        return switch(this) {

            case ENTRY -> " is entry level; they have been working here less than 1 year;";
            case ENTRYTOMID -> " is entry-to-mid level; they have been working more than 1 year;";
            case MIDDLE -> " is mid level; they have been working more than 2 years;";
            default -> " is further than mid level; they have been working here more than 3 years;";
        };
    }
}