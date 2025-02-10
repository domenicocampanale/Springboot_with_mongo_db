package com.stage.mongodb.exceptions;

public class MovieNotFoundException extends IllegalArgumentException {

    private static final long serialVersionUID = 1458488340100671889L;

    public MovieNotFoundException(String message) {
        super(message);
    }
}
