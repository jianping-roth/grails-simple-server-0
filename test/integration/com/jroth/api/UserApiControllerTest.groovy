package com.jroth.api

import com.jroth.*
import com.jroth.simpleserver.api.UserApiController
import grails.converters.JSON
import grails.test.mixin.TestFor

@TestFor(UserApiController)
class UserApiControllerTest extends GroovyTestCase {
    def springSecurityService

    /**
     * Ensure the controller can list users correctly.
     */
    void testListUsers() {
        request.contentType = 'application/json'
        controller.list()

        def responseMarkup = JSON.parse(controller.response.contentAsString)
        assert responseMarkup.users.size() == 3
        responseMarkup.users.collect { it.username }.sort() == ['admin', 'jroth', 'proth']
        responseMarkup.users.each {
            assert it.uri != null
        }
        assert response.status == HttpURLConnection.HTTP_OK
    }

    /**
     * Ensures the controller can retrieve user details info correctly.
     */
    void testReadUserDetails_happyPath() {
        request.contentType = 'application/json'
        controller.params.id = '1'
        controller.read()

        def responseMarkup = JSON.parse(controller.response.contentAsString).user

        ['username', 'password', 'enabled', 'roles'].each {
            assert responseMarkup.get(it) != null
        }

        assert response.status == HttpURLConnection.HTTP_OK
    }

    /**
     * Ensures the controller throws an exception if the requested user does not exist.
     */
    void testReadUserDetails_invalidUserId() {
        request.contentType = 'application/json'
        controller.params.id = '10'
        controller.read()

        def responseMarkup = JSON.parse(controller.response.contentAsString)
        assert responseMarkup.message == "No user with id ${controller.params.id} exists."
        assert response.status == HttpURLConnection.HTTP_NOT_FOUND
    }

    /**
     * Ensures the controller can update user details info correctly.
     */
    void testUpdateUser_happyPath() {
        User newUser = testCreateTestUser();
        String changedName = "${newUser.username}-changed"
        request.contentType = 'application/json'
        controller.params.id = newUser.id.toString()
        controller.request.content = """{
            user: {
                username: ${changedName},
                password: ${changedName},
                enabled: false,
                roles:[
                        "ROLE_USER"
                ]
            }
        }
        """.toString().getBytes()
        controller.request.serverPort = 8080
        controller.update()

        def responseMarkup = JSON.parse(controller.response.contentAsString).user
        [username : changedName, password: springSecurityService.encodePassword(changedName),
                uri: controller.createLink(uri:  "/api/users/${newUser.id}", absolute: true).toString(), enabled: false].each { key, value ->
            assert responseMarkup.get(key) == value
        }
        assert response.status == HttpURLConnection.HTTP_OK
    }

    /**
     * Ensures the controller throws an exception if the id of the user does not exist during update operation.
     */
    void testUpdateUser_invalidUserId() {
        request.contentType = 'application/json'
        controller.params.id = '10'
        controller.request.content = """{
        }
        """.toString().getBytes()
        controller.update()

        def responseMarkup = JSON.parse(controller.response.contentAsString)
        assert responseMarkup.message == "No user with id ${controller.params.id} exists."
        assert response.status == HttpURLConnection.HTTP_NOT_FOUND
    }

    /**
     * Ensures the controller throws an exception if the username is not provided
     */
    void testUpdateUser_missingUsername() {
        User newUser = testCreateTestUser();
        request.contentType = 'application/json'
        controller.params.id = newUser.id
        controller.request.content = """{
            user: {
                password: '${newUser.password}',
                enabled: false,
                roles:[
                        "ROLE_USER"
                ]
            }
        }
        """.toString().getBytes()
        controller.update()

        def responseMarkup = JSON.parse(controller.response.contentAsString)
        assert responseMarkup.message == "User property 'username' must be present."
        assert response.status == HttpURLConnection.HTTP_BAD_REQUEST
    }

    /**
     * Ensures the controller throws an exception if the password is not provided.
     */
    void testUpdateUser_missingPassword() {
        User newUser = testCreateTestUser();
        request.contentType = 'application/json'
        controller.params.id = newUser.id
        controller.request.content = """{
            user: {
                username: '${newUser.username}',
                enabled: false,
                roles:[
                        "ROLE_USER"
                ]
            }
        }
        """.toString().getBytes()
        controller.update()

        def responseMarkup = JSON.parse(controller.response.contentAsString)
        assert responseMarkup.message == "User property 'password' must be present."
        assert response.status == HttpURLConnection.HTTP_BAD_REQUEST
    }

    /**
     * Ensures the controller throws an exception if the updated username is taken.
     */
    void testUpdateUser_duplicatedUsername() {
        User newUser1 = testCreateTestUser();
        User newUser2 = testCreateTestUser();
        request.contentType = 'application/json'
        controller.params.id = newUser2.id
        controller.request.content = """{
            user: {
                username: ${newUser1.username},
                password: "pass",
                enabled: false
            }
        }
        """.toString().getBytes()
        controller.update()

        def responseMarkup = JSON.parse(controller.response.contentAsString)
        assert responseMarkup.message == "The username is already used."
        assert response.status == HttpURLConnection.HTTP_BAD_REQUEST
    }

    /**
     * Ensures the controller can create new users correctly.
     */
    void testCreateUser() {
        String newUser = UUID.randomUUID().toString()
        request.contentType = 'application/json'
        controller.request.content = """{
            user: {
                username: ${newUser},
                password: ${newUser},
                enabled: false,
                roles:[
                        "ROLE_USER", "ROLE_ADMIN"
                ]
            }
        }
        """.toString().getBytes()
        controller.request.serverPort = 8080
        controller.create()

        def responseMarkup = JSON.parse(controller.response.contentAsString).user
        [username : newUser, password: springSecurityService.encodePassword(newUser), enabled: false].each { key, value ->
            assert responseMarkup.get(key) == value
        }
        assert response.status == HttpURLConnection.HTTP_CREATED
    }

    /**
     * Creates a test user.
     */
    private User testCreateTestUser(Map props = [:]) {
        User newUser = new User(props)
        if (!newUser.username) {
            newUser.username = UUID.randomUUID().toString()
        }
        if (newUser.password == null) {
            newUser.password = 'pass'
        }

        newUser.save()
        List<String> roleNames = props.roles ?: ['ROLE_USER']
        List<Role> roles = Role.list()
        roleNames.each {
            def role = roles.find {role -> role.authority == it}
            assert role != null
            UserRole.create newUser, role, true
        }

        return newUser
    }

}
