package com.safetynetalerts.model.exception;

/**
 * Exception levée quand une ressource attendue n'existe pas.
 * Sera convertie en HTTP 404.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}