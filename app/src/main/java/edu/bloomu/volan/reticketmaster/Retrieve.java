package edu.bloomu.volan.reticketmaster;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class will retrieve data from the url using HTTP Url Connection
 * Created by Volan on 11/22/17.
 */

public class Retrieve {

    public String readUrl(String sentUrl) throws IOException
    {
        String data ="";
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try
        {
            URL url = new URL(sentUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();

            String line ="";
            while ((line = bufferedReader.readLine()) != null)
            {
                stringBuffer.append(line);
            }

            data = stringBuffer.toString();
            bufferedReader.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {

            inputStream.close();
            urlConnection.disconnect();

        }
        return data;
    }
}