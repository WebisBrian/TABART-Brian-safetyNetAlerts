package com.safetynetalerts.model.exception;

/**
 * Exception levée quand la requête est invalide (paramètre manquant, format incorrect...).
 * Sera convertie en HTTP 400 par le GlobalExceptionHandler.
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}