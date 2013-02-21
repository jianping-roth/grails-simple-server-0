package com.jroth.simpleserver

import grails.plugins.springsecurity.Secured
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import org.codehaus.groovy.grails.web.json.JSONObject

@Secured(['ROLE_USER'])
class HomeController {

    def index() {
        HttpClient httpClient = null
        HttpGet httpGet = null
        InputStream inputStream = null
        try {
            httpClient = new DefaultHttpClient();
            httpGet = new HttpGet("http://search.twitter.com/search.json?q=vancouver");
            HttpResponse response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() != HttpURLConnection.HTTP_OK)  {
                render (view: "index", model: [error: "You are out of luck. Try again later."])
            }

            // Get hold of the response entity
            HttpEntity entity = response.getEntity();

            // If the response does not enclose an entity, there is no need
            // to worry about connection release
            if (entity != null) {
                inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                JSONObject entries = new JSONObject(reader.readLine());
                render(view: "index", model: [data: entries.results.collect{
                    [created: it.created_at, image: it.profile_image_url, text: it.text]
                }])
            }
        } catch (IOException ex) {
            // In case of an IOException the connection will be released
            // back to the connection manager automatically
            log.error(ex)
            render (view: "index", model: [error: "You are out of luck. Try again later."])
        } catch (RuntimeException ex) {
            // In case of an unexpected exception you may want to abort
            // the HTTP request in order to shut down the underlying
            // connection and release it back to the connection manager.
            log.error(ex)
            httpGet?.abort();
            render (view: "index", model: [error: "You are out of luck. Try again later."])
        } finally {
            inputStream?.close();
            httpClient?.getConnectionManager().shutdown();
        }
    }
}