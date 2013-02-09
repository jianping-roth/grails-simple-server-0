import com.jroth.Role
import com.jroth.User
import com.jroth.UserRole

class BootStrap {

    def init = { servletContext ->
        def adminRole = Role.findByAuthority('ROLE_ADMIN') ?: new Role(authority: 'ROLE_ADMIN').save(failOnError: true)
        def userRole = Role.findByAuthority('ROLE_USER') ?: new Role(authority: 'ROLE_USER').save(failOnError: true)

        def jroth = User.findByUsername('jroth') ?: new User(username: 'jroth', enabled: true, password: 'pass', firstName: 'Jianping', lastName: 'Roth', phone: '250-477-8759').save(failOnError: true)
        if (!jroth.authorities.contains(userRole)) {
            UserRole.create jroth, userRole, true
        }

        def proth = User.findByUsername('proth') ?: new User(username: 'proth', enabled: true, password: 'pass', firstName: 'Paul', lastName: 'Roth', phone: '25-4778759').save(failOnError: true)
        if (!proth.authorities.contains(userRole)) {
            UserRole.create proth, userRole, true
        }
        if (!proth.authorities.contains(adminRole)) {
            UserRole.create proth, adminRole, true
        }

        def admin = User.findByUsername('admin') ?: new User(username: 'admin', enabled: true, password: 'pass', firstName: 'admin', lastName: 'Roth', phone: '25-4778759').save(failOnError: true)
        if (!admin.authorities.contains(userRole)) {
            UserRole.create admin, userRole, true
        }
        if (!admin.authorities.contains(adminRole)) {
            UserRole.create admin, adminRole, true
        }

        assert User.count() == 3
        assert Role.count() == 2
        assert UserRole.count() == 5
    }

    def destroy = {
    }
}
