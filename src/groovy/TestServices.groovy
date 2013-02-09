import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.UsernamePasswordCredentials
import org.apache.commons.httpclient.auth.AuthScope
import org.apache.commons.httpclient.methods.DeleteMethod
import org.apache.commons.httpclient.methods.GetMethod
import org.apache.commons.httpclient.methods.PostMethod
import org.apache.commons.httpclient.methods.PutMethod
import org.apache.commons.httpclient.methods.StringRequestEntity
import org.codehaus.groovy.grails.web.json.JSONObject

final String url = "http://localhost:8080/simple-server/api/users"

// get all users
println ("Get all users ...")
final HttpClient httpClient = new HttpClient()

httpClient.getState().setCredentials(
        new AuthScope("ipaddress", 443, "realm"),
        new UsernamePasswordCredentials("admin", "pass")
);

GetMethod getMethod = new GetMethod(url)
getMethod.addRequestHeader("Content-Type", "application/json")
httpClient.executeMethod(getMethod);
JSONObject json = new JSONObject(getMethod.responseBodyAsString)
println json

// get each user
println ("Get users details ...")
json.users.uri.each {
    getMethod = new GetMethod(it);
    getMethod.addRequestHeader("Content-Type", "application/json")
    httpClient.executeMethod(getMethod)
    json = new JSONObject(getMethod.responseBodyAsString)
    println json
}

// create a new user
println ("Creating a user ...")
PostMethod postMethod = new PostMethod(url);
postMethod.addRequestHeader("Content-Type", "application/json")

StringRequestEntity requestEntity = new StringRequestEntity(
        """
        {
            "user":{
            "username":"newTestUser-${System.currentTimeMillis()}",
            "password":"pass",
            "enabled":true
            }
        }
        """,
        "application/json",
        "UTF-8");
postMethod.setRequestEntity(requestEntity)
httpClient.executeMethod(postMethod);
json = new JSONObject(postMethod.responseBodyAsString)
println ("Created a new user")
println json

// update the user
println ("Updating the new user ...")
PutMethod putMethod = new PutMethod(json.user.uri);
postMethod.addRequestHeader("Content-Type", "application/json")
requestEntity = new StringRequestEntity(
        """
        {
            "user":{
            "username":"changed-${json.user.username}",
            "password":"pass",
            "enabled":false
            }
        }
        """,
        "application/json",
        "UTF-8");
putMethod.setRequestEntity(requestEntity)
httpClient.executeMethod(putMethod);
json = new JSONObject(putMethod.responseBodyAsString)
println ("Updated the new user.")
println json

// delete the new user
println ("Deleting the new user ...")
DeleteMethod deleteMethod = new DeleteMethod(json.user.uri);
httpClient.executeMethod(deleteMethod);
println ("Deleted the new user.")

// update
getMethod.releaseConnection()
postMethod.releaseConnection()
deleteMethod.releaseConnection()