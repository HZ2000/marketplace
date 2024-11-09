package com.haykz.exception;

/**
 * Custom exception thrown when a requested resource is not found.
 * <p>
 * This exception is typically used in scenarios where the requested data
 * cannot be found in the database or the system, allowing the caller to handle
 * the missing resource case appropriately.
 * </p>
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
