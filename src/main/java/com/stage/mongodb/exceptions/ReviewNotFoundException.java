package com.stage.mongodb.exceptions;

public class ReviewNotFoundException extends IllegalArgumentException {

    private static final long serialVersionUID = 1458488340100671889L;

    public ReviewNotFoundException(String message) {
        super(message);
    }
}
