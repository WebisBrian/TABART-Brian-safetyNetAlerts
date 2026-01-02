package com.safetynetalerts.model.exception;

/**
 * Exception levée quand une opération ne peut pas être effectuée,
 * car l'état est incompatible (ex: création d'un doublon).
 * Sera convertie en HTTP 409.
 */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}