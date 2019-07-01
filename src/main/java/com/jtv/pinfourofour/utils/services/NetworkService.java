package com.jtv.pinfourofour.utils.services;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetworkService {
    private int linkResponseCode;
    private String redirectLocation;
    private int redirectLocationResponseCode;
    HttpURLConnection conn;

    public void execute(String link){
        boolean redirect = false;
        try {
            connect (link);
            if(conn.getResponseCode () == HttpURLConnection.HTTP_OK){
                linkResponseCode = 200;
                System.out.println("Request URL ... " + link + " Status:" + linkResponseCode );
            }
            if (conn.getResponseCode () != HttpURLConnection.HTTP_OK) {
                //3XX
                if (conn.getResponseCode () == HttpURLConnection.HTTP_MOVED_PERM || conn.getResponseCode () == HttpURLConnection.HTTP_MOVED_TEMP) {
                    redirect = true;
                    linkResponseCode= conn.getResponseCode();
                    redirectLocation = conn.getHeaderField ("Location");
                    System.out.println ("Redirected to: " + redirectLocation);
                    conn.disconnect ();
                }
                if (conn.getResponseCode () == HttpURLConnection.HTTP_NOT_FOUND || conn.getResponseCode () == HttpURLConnection.HTTP_FORBIDDEN) {
                    linkResponseCode= conn.getResponseCode();
                    System.out.println ("Not found: "+linkResponseCode);
                    conn.disconnect ();
                }
            }
            if (redirect) {
                connect (redirectLocation);
                redirectLocationResponseCode = conn.getResponseCode ();
                conn.disconnect ();
            }
        } catch (MalformedURLException e) {
            System.err.println("=================== URL Check Complete ===================");
            System.err.println("Bad url. Cannot check this link. "+e.getMessage());
        } catch (IOException e){
            System.err.println(e.getMessage());
        }

        System.out.println ("=================== URL Check Complete ===================");
        System.out.println("~~~~~"+link+" checked, resulted in: ~~~~");
        System.out.println("--Response Code: "+linkResponseCode);
        System.out.println("--Redirected To: "+redirectLocation);
        System.out.println("--Redirect Location Code: "+redirectLocationResponseCode);
    }

    private void connect(String loc) throws  IOException{
        URL url = new URL (loc);
        conn = (HttpURLConnection) url.openConnection ();
//        conn.setRequestMethod ("HEAD");
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