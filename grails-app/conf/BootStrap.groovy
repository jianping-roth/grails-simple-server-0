import com.jroth.Role
import com.jroth.User
import com.jroth.UserRole

class BootStrap {

    def init = { servletContext ->
        def adminRole = Role.findByAuthority('ROLE_ADMIN') ?: new Role(authority: 'ROLE_ADMIN').save(failOnError: true)
        def userRole = Role.findByAuthority('ROLE_USER') ?: new Role(authority: 'ROLE_USER').save(failOnError: true)

        def jroth = User.findByUsername('bobby@example.org') ?: new User(username: 'jroth', enabled: true, password: 'pass', firstName: 'Jianping', lastName: 'Roth', phone: '250-477-8759').save(failOnError: true)
        if (!jroth.authorities.contains(userRole)) {
            UserRole.create jroth, userRole, true
        }

        def proth = User.findByUsername('admin@example.org') ?: new User(username: 'proth', enabled: true, password: 'pass', firstName: 'Paul', lastName: 'Roth', phone: '25-4778759').save(failOnError: true)
        if (!proth.authorities.contains(userRole)) {
            UserRole.create proth, userRole, true
        }
        if (!proth.authorities.contains(adminRole)) {
            UserRole.create proth, adminRole, true
        }

        assert User.count() == 2
        assert Role.count() == 2
        assert UserRole.count() == 3
    }

    def destroy = {
    }
}
