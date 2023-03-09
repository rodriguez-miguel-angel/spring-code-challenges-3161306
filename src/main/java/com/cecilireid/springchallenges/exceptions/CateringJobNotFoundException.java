package com.cecilireid.springchallenges.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * An exception that is called when a catering job is not found
 */

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CateringJobNotFoundException extends RuntimeException {

    public CateringJobNotFoundException() {
        this("No catering job(s) found");
    }

    public CateringJobNotFoundException(String message) {
        super(message);
    }
}
