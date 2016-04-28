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
        UserIdRequest rui = new UserIdRequest();
        Thread t = new Thread(rui);
        t.start();

        while (rui.getUserId() == null) {
            // wait for response
        }

        return rui.getUserId();
    }

    /**
     * Returns the current totalratio from the server
     * @return totalratio
     */
    public float getTotalRatio() {
        TotalRatioRequest gtr = new TotalRatioRequest();
        Thread t = new Thread(gtr);
        t.start();

        while (!gtr.complete) {
            // wait to fail or complete
        }

        return gtr.getTotalratio();
    }

    private class UserIdRequest implements Runnable {

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

    private class TotalRatioRequest implements Runnable {

        float totalratio;
        boolean complete = false;

        @Override
        public void run() {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(serverUrl + "/gimme/totalratio");
            try {
                HttpResponse res = httpClient.execute(httpPost);
                BufferedReader reader = new BufferedReader(new InputStreamReader(res.getEntity().getContent(), "UTF-8"));
                String json = reader.readLine();
                JSONObject result = new JSONObject(json);
                totalratio = (float) result.getDouble("totalratio");
            } catch (Exception e) {
                totalratio = -1;
                e.printStackTrace();
            }
            complete = true;
        }

        public float getTotalratio() {
            return totalratio;
        }
    }

}
