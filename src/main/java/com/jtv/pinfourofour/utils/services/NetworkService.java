package com.jtv.pinfourofour.utils.services;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetworkService {
    private String link;
    private int linkResponseCode;
    private String redirectLocation;
    private int redirectLocationResponseCode;
    HttpURLConnection conn;

//    public NetworkService(String link){
//        this.link = link;
//    }

    public void execute(String link){
        boolean redirect = false;
        try {
            connect (link);
            if(conn.getResponseCode () == HttpURLConnection.HTTP_OK){
                linkResponseCode = conn.getResponseCode();
                System.out.println("Request URL ... " + link + " Status:" + linkResponseCode );
                return;
            }
            if (conn.getResponseCode () != HttpURLConnection.HTTP_OK) {
                //3XX
                if (conn.getResponseCode () == HttpURLConnection.HTTP_MOVED_PERM || conn.getResponseCode () == HttpURLConnection.HTTP_MOVED_TEMP) {
                    redirect = true;
                    redirectLocation = conn.getHeaderField ("Location");
                    System.out.println ("Redirected to: " + redirectLocation);
                    conn.disconnect ();
                }
            }
            if (redirect) {
                connect (redirectLocation);
                redirectLocationResponseCode = conn.getResponseCode ();
                conn.disconnect ();
            }
        } catch (MalformedURLException e) {
            System.err.println("Bad url. Cannot check this link.");
        } catch (IOException e){
            System.err.println(e.getMessage());
        }
        System.out.println ("Done..." + redirectLocation + " resulted in status code: " + redirectLocationResponseCode + "\n ---------");
    }

    private void connect(String loc) throws  IOException{
        URL url = new URL (loc);
        conn = (HttpURLConnection) url.openConnection ();
        conn.setRequestMethod ("HEAD");
        conn.setConnectTimeout (4000);
        conn.setReadTimeout (4000);
        conn.connect ();
    }

    public int getLinkResponseCode() {
        return linkResponseCode;
    }


    public String getRedirectLocation() {
        return redirectLocation;
    }


    public int getRedirectLocationResponseCode() {
        return redirectLocationResponseCode;
    }

    public void setRedirectLocationResponseCode(int redirectLocationResponseCode) {
        this.redirectLocationResponseCode = redirectLocationResponseCode;
    }
}