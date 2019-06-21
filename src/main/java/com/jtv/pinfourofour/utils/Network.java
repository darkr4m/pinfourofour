package com.jtv.pinfourofour.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

public class Network {

//    private boolean proxy;
//    private String http_proxy_addr;
//    private int port;
    private String location = null;
    int status = 0;
    int redir_status= 0;

    public Network(){
        System.out.println ("Network initialized.");
//        Properties props = new Properties ();
//        try {
//            props.load (new BufferedReader (new FileReader ("config/pin.config")));
//            proxy = Boolean.parseBoolean (props.getProperty("proxy"));
//            http_proxy_addr = props.getProperty ("http_proxy_addr");
//            port = Integer.parseInt (props.getProperty("port"));
//            System.out.println ("Proxy = "+proxy);
//        } catch (Exception e) {
//            e.printStackTrace ();
//        }
    }

    public int checkStatus(String link){
        HttpURLConnection conn;
        try {
            URL url = new URL(link);
//            if(proxy){
//                url = new URL ("http", http_proxy_addr, port, "http://www.jtv.com" );
//                conn = (HttpURLConnection) url.openConnection();
//                conn.setRequestMethod ("HEAD");
//                conn.connect();
//                status = conn.getResponseCode ();
//                System.out.println (status);
//            } else {
                conn = (HttpURLConnection) url.openConnection ();
                conn.setRequestMethod ("HEAD");
                conn.setConnectTimeout (4000);
                conn.setReadTimeout (4000);
                conn.connect ();
//            }

            boolean redirect = false;
            status = conn.getResponseCode();
            System.out.println("Request URL ... " + url + " Status:" + status );
            if (status != HttpURLConnection.HTTP_OK) {
                //3XX
                if (status == HttpURLConnection.HTTP_MOVED_PERM || status == HttpURLConnection.HTTP_MOVED_TEMP) {
                    location = conn.getHeaderField ("Location");
                    redirect = true;
                    System.out.println ("Redirected to: " + location);
                    conn.disconnect ();
                }
            }

            if (redirect) {
//                if(proxy){
//                    url = new URL ( location );
//                    conn = (HttpURLConnection)url.openConnection();
//                    conn.setRequestMethod ("HEAD");
//                    conn.connect();
//                    status = conn.getResponseCode ();
//                    System.out.println (status);
//                } else {
                    url = new URL (location );
                    conn = (HttpURLConnection) url.openConnection ();
                    conn.setRequestMethod ("HEAD");
                    conn.setConnectTimeout (4000);
                    conn.setReadTimeout (4000);
                    conn.connect ();
                }
                redir_status = conn.getResponseCode ();
//            }
        } catch (Exception e) {
            e.printStackTrace ();
        }
        System.out.println ("Done..." + link + " resulted in status code: " + redir_status + "\n ---------");
        return status;
    }

    public String getLocation() {
        return location;
    }

    public int getRedir_status() {
        return redir_status;
    }
}