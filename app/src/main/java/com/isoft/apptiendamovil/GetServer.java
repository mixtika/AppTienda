package com.isoft.apptiendamovil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class GetServer {
    public String stringQuery(String vurl, String method){
        HttpURLConnection con=null;
        try {
            URL url = new URL(UrlConnection.URL + vurl);
            con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(3000);
            con.setRequestMethod(method);
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            if(con.getResponseCode()==HttpURLConnection.HTTP_OK) {
                return reader.readLine();
            }
            else {
                return "No string.";
            }
        }
        catch(Exception e) {
            return "Error en el servidor";
        }
    }

    public String stringQuery(String vurl){
        HttpURLConnection con;
        try {
            URL url = new URL(UrlConnection.URL+vurl);
            con = (HttpURLConnection) url.openConnection();

            //String auth = "Bearer " + oauthToken;
            //connection.setRequestProperty("Authorization", basicAuth);

            String boundary = UUID.randomUUID().toString();
            con.setConnectTimeout(3000);
            con.setRequestMethod("POST");

            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            DataOutputStream request = new DataOutputStream(uc.getOutputStream());

            request.writeBytes("--" + boundary + "\r\n");
            request.writeBytes("Content-Disposition: form-data; name=\"description\"\r\n\r\n");
            request.writeBytes(fileDescription + "\r\n");

            request.writeBytes("--" + boundary + "\r\n");
            request.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.fileName + "\"\r\n\r\n");
            request.write(FileUtils.readFileToByteArray(file));
            request.writeBytes("\r\n");

            request.writeBytes("--" + boundary + "--\r\n");
            request.flush();
            int respCode = connection.getResponseCode();
        }catch (Exception ex){}

        /*switch(respCode) {
            case 200:
                return "1";
                break;
            case 301:
            case 302:
            case 307:
              return "2";
                break;
                return "3";
            default:

        }*/
    }

}