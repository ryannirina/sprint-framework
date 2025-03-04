package com.sprintframework.web.exception;

public class SprintFrameworkException extends RuntimeException {
    private final ErrorType errorType;

    public SprintFrameworkException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public SprintFrameworkException(ErrorType errorType, String message, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public enum ErrorType {
        PACKAGE_NOT_FOUND("Package not found"),
        EMPTY_PACKAGE("No controllers found in package"),
        DUPLICATE_URL("Duplicate URL mapping found"),
        URL_NOT_FOUND("URL not found"),
        UNSUPPORTED_RETURN_TYPE("Unsupported controller method return type");

        private final String description;

        ErrorType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
