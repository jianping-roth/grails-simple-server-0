package com.jroth

class UserService {

    /**
     * Retrieves all users from the system.
     * @return all users.
     */
    List getAllUsers() {
        return User.list()
    }

    /**
     * Creates a new user with the given set of properties. username and password must be present; i
     * f enabled is not present, the newly created user will be enabled.
     *
     * @param userProps the user properties.
     * @return the newly created users.
     */
    User createUser(Map userProps) {
        def existing = User.findByUsername(userProps.username)
        if (existing) {
            throw new ApiException("The username is already used.", ApiException.ErrorCode.InvalidProperty)
        }

        ['username', 'password'].each {
            if (!userProps."${it}") {
                throw new ApiException("user property ${it} must be present.", ApiException.ErrorCode.InvalidProperty)
            }
        }
        User newUser = new User(userProps).save()

        // handle roles
        Role.findAllByAuthorityInList(userProps.roles?.authority ? userProps.roles?.authority : []).each {
            new UserRole(user: newUser, role: it).save()
        }

        return newUser
    }

    /**
     * Updates the user with the given username and all associated properties. All user properties must be present; however,
     * properties may remain unchanged.
     *
     * @param id the id of the user to be updated.
     * @param newUserProps the update user properties.
     * @return the newly created users.
     */
    User updateUser(Long id, Map newUserProps) {
        // ensure the user specified on the request uri exists
        def existing = findAndEnsureUserExists(id)

        // ensure the new username is not already used.
        if (newUserProps.username && existing.username != newUserProps.username && User.findByUsername(newUserProps.username)) {
            throw new ApiException("The username is already used.", ApiException.ErrorCode.InvalidProperty)
        }

        ['username', 'password','enabled'].each {
            def newValue = newUserProps."${it}"
            if (newValue == null || newValue.toString().isEmpty()) {
                throw new ApiException("user property ${it} must be present.", ApiException.ErrorCode.InvalidProperty)
            }

            existing."${it}" = newValue
        }

        // delete the old roles
        UserRole.findAllByUser(existing).each {
            it.delete()
        }

        // handle roles
        Role.findAllByAuthorityInList(newUserProps.roles ? newUserProps.roles : []).each {
            new UserRole(user: existing, role: it).save()
        }

        return existing.save()
    }

    /**
     * Creates a new user with the given set of properties.
     * @param id the id of the user to be deleted.
     * @return the newly created users.
     */
    void deleteUser(Long id) {
        def toDelete = findAndEnsureUserExists(id)
        toDelete.delete()
    }

    /**
     * Retrieves the user for the given user and ensure the user exists.
     * @param username the user properties.
     * @return the user with the matching username.
     * @throws ApiException if the username is null or the user does not exist.
     */
    User findAndEnsureUserExists(Long id) {
        if (!id) {
            throw new ApiException("User id cannot be null.", ApiException.ErrorCode.InvalidProperty)
        }

        User user = User.get(id)
        if (!user) {
            throw new ApiException("No user with id ${id} exists.", ApiException.ErrorCode.UserNotExist)
        }

        return user;
    }
}
