class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		"/"(view:"/index")
		"500"(view:'/error')


        name userApiList: "/api/users"(controller: "userApi") {
            action = [GET: 'list', POST: 'create']
        }
        name userApiDetails: "/api/users/$id"(controller: "userApi") {
            action = [GET: 'read', PUT: 'update', DELETE: 'delete']
        }
	}
}
