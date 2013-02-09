package com.jroth.simpleserver

import grails.plugins.springsecurity.Secured

@Secured(['ROLE_USER'])
class HomeController {

    def index() { }
}
