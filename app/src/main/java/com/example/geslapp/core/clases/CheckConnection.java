package com.example.geslapp.core.clases;

import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CheckConnection extends AsyncTask<String , Void ,String> {

       static String server_response;

        @Override
        protected String doInBackground(String... strings) {

                URL url;
                HttpURLConnection urlConnection;

                try {
                        url = new URL(strings[0]);
                        urlConnection = (HttpURLConnection) url.openConnection();

                        int responseCode = urlConnection.getResponseCode();

                        if (responseCode == HttpURLConnection.HTTP_OK) {
                                server_response = readStream(urlConnection.getInputStream());
                                Log.v("CatalogClient", server_response);

                        }

                } catch (IOException e) {
                        e.printStackTrace();
                }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
                super.onPostExecute(s);

                Log.e("Response", "" + server_response);


        }


// Converting InputStream to String

        private String readStream(InputStream in) {
                BufferedReader reader = null;
                StringBuilder response = new StringBuilder();
                try {
                        reader = new BufferedReader(new InputStreamReader(in));
                        String line;
                        while ((line = reader.readLine()) != null) {
                                response.append(line);
                        }
                } catch (IOException e) {
                        e.printStackTrace();
                } finally {
                        if (reader != null) {
                                try {
                                        reader.close();
                                } catch (IOException e) {
                                        e.printStackTrace();
                                }
                        }
                }
                return response.toString();
        }

        public String getServerResponse() {
                return server_response;
        }

        public void setServer_response(){server_response = null;}


}