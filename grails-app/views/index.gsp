<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Welcome to Grails</title>
</head>

<body>
<div id="page-body" role="main">

    <h2 class="heading">Jianping's simple server to demo the following features</h2>

    <div id="initial-page">
        <ul>
            <li>Login</li>
            <li>A simple api for listing, creating, updating, and deleting users.
            You must login as an admin user in order to use the api.
                <table>
                    <thead>
                        <tr>
                            <th>Function</th>
                            <th>End-Point</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>List users</td>
                            <td>simple-server/api/users</td>
                        </tr>
                        <tr>
                            <td>
                                Get a user details
                            </td>
                            <td>
                                simple-server/api/users/id
                            </td>
                        </tr>
                        <tr>
                            <td>
                                Update a user details
                            </td>
                            <td>
                                simple-server/api/users/id
                            </td>
                        </tr>
                        <tr>
                            <td>
                                Create a new user
                            </td>
                            <td>
                                simple-server/api/users/
                            </td>
                        </tr>
                        <tr>
                            <td>
                                Delete a user
                            </td>
                            <td>
                                simple-server/api/users/id
                            </td>
                        </tr>
                    </tbody>
                </table>
                Note: you can test the api methods via UserApiControllerTest, or use TestSimpleServer.groovy, a HttpClient, for testing.
            </li>
        </ul>
    </div>
</div>
</body>
</html>
