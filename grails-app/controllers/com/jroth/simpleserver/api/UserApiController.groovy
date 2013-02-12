package com.jroth.simpleserver.api

import com.jroth.ApiException
import grails.converters.JSON
import grails.plugins.springsecurity.Secured
import org.codehaus.groovy.grails.web.json.JSONObject
import com.jroth.User

@Secured(["ROLE_ADMIN"])
class UserApiController {
    def userService

    /**
     * Build a Map suitable for JSON rendering for a failure response with an error message
     * @param message the error message
     * @param additionalAttributes additional response items.  The default value is empty.
     * @return a Map for the response
     */
    private Map buildFailureResponse(String message='', Map additionalAttributes=[:]) {
        return [
                success: false,
                message: message
        ] << additionalAttributes
    }

    /**
     * Display the details of a user.
     * @return a map of three entries with the following keys:
     * success: true
     * records: all users
     */
    def read() {
        try {
            def found = userService.findAndEnsureUserExists(params.id as Long)
            def result = [user: toMap(found)]
            response.status = HttpURLConnection.HTTP_OK
            render result as JSON
        } catch (Exception e) {
            def result = buildFailureResponse(e.message)
            response.status = getHttpCodeFromException(e)
            render result as JSON
        }
    }

    /**
     * List all users in the system.
     * @return a map of three entries with the following keys:
     * success: true
     * totalRecords: the number of total records returned
     * records: all users
     */
    def list() {
        List allUsers = userService.getAllUsers().collect{
            [username: it.username, uri: createLink([uri:  "/api/users/${it.id}", absolute: true])]
        }

        def result = [users: allUsers]
        render result as JSON
    }

    /**
     * Creates a new user for the given username, password (clear text), and if the user is enabled or not.
     * @return if the user is created successfully:
     * success: true
     * user: the new user properties
     * Otherwise,
     * success: false
     * message: the error message for why the user is not created
     */
    def create() {
        def requestBody = new JSONObject(request.reader.text)
        try {
            def created = userService.createUser(requestBody.user)
            def result = [
                    user: toMap(created)]
            response.status = HttpURLConnection.HTTP_CREATED
            render result as JSON
        } catch (Exception e) {
            def result = buildFailureResponse(e.message)
            response.status = getHttpCodeFromException(e)
            render result as JSON
        }
    }

    /**
     * Update an existing user for the given username, password (clear text), and if the user is enabled or not.
     * @return if the user is created successfully:
     * success: true
     * user: the new user properties
     * Otherwise,
     * success: false
     * message: the error message for why the user is not created
     */
    def update() {
        def requestBody = new JSONObject(request.reader.text)
        try {
            def updated = userService.updateUser(params.id as Long, requestBody.user)
            def result = [
                    user: toMap(updated)]
            response.status = HttpURLConnection.HTTP_OK
            render result as JSON
        } catch (Exception e) {
            def result = buildFailureResponse(e.message)
            response.status = getHttpCodeFromException(e)
            render result as JSON
        }
    }

    /**
     * Deletes the use. for the specified id
     */
    def delete() {
        try {
            userService.deleteUser(params.id as Long)
            def result = [success: true]
            response.status = HttpURLConnection.HTTP_ACCEPTED
            render result as JSON
        } catch (Exception e) {
            def result = buildFailureResponse(e.message)
            response.status = getHttpCodeFromException(e)
            render result as JSON
        }
    }

    /**
     * Creates a map for the give user.
     * @param user the user for which to create the map.
     * @return a map of properties for the user.
     */
    private Map toMap(User user) {
        return [uri: createLink([uri: "/api/users/${user.id}", absolute: true]), username: user.username,
                password: user.password, enabled: user.enabled, roles: user.authorities.authority ]
    }

    /**
     * Compute the Http Status based on the exception.
     * @param e the exception encountered.
     * @return the HttpStatus code for the exception.
     */
    private getHttpCodeFromException(Exception e) {
        if (e instanceof ApiException) {
            switch (((ApiException) e).code){
                case ApiException.ErrorCode.UserNotExist:
                    return HttpURLConnection.HTTP_NOT_FOUND
                case ApiException.ErrorCode.InvalidProperty:
                    return HttpURLConnection.HTTP_BAD_REQUEST
            }
        }

        return HttpURLConnection.HTTP_BAD_REQUEST
    }
}
