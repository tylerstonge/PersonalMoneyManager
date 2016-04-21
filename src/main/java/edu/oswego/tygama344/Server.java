package edu.oswego.tygama344;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Server {

    public static final String serverUrl = "http://52.90.21.236:8080";

    public Server() {

    }

    /**
     * Requests a userid from the server.
     * @return err if there was an error, a userid on success.
     */
    public String requestUserId() {
        RequestUserId rui = new RequestUserId();
        Thread t = new Thread(rui);
        t.start();

        while (rui.getUserId() == null) {
            // wait for response
        }

        return rui.getUserId();
    }

    private class RequestUserId implements Runnable {

        private String userid = null;

        @Override
        public void run() {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(serverUrl + "/newuser");
            try {
                HttpResponse res = httpClient.execute(httpPost);
                BufferedReader reader = new BufferedReader(new InputStreamReader(res.getEntity().getContent(), "UTF-8"));
                String json = reader.readLine();
                JSONObject result = new JSONObject(json);
                userid = result.getString("userid");
            } catch (Exception e) {
                userid = "err";
                e.printStackTrace();
            }
        }

        public String getUserId() {
            return userid;
        }
    }

}
