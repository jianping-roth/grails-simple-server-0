package com.jroth

/**
 * The class communicates the problems when api calls were rejected.
 */
class ApiException extends RuntimeException {
    enum ErrorCode {UserNotExist, InvalidProperty}
    final ErrorCode code

    /**
     * Constructs an ApiException with the given message and httpStatus.
     */
    ApiException(String message, ErrorCode code) {
        super(message)
        this.code = code
    }
}
