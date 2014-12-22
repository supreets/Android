package com.example.instagramgallery.network;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.util.Log;

public class WebInterface {
	
	public static JSONObject requestWebService(String serviceUrl) {
		HttpURLConnection urlConnection = null;
		try {
			
			URL urlToRequest = new URL(serviceUrl);
			urlConnection = (HttpURLConnection) 
					urlToRequest.openConnection();
			urlConnection.setConnectTimeout(0);
			urlConnection.setReadTimeout(0);

			int statusCode = urlConnection.getResponseCode();
			if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
				
			} else if (statusCode != HttpURLConnection.HTTP_OK) {
				
			}

			
			InputStream in = new BufferedInputStream(
					urlConnection.getInputStream());
			return new JSONObject(getResponseText(in));

		} catch (MalformedURLException e) {
			
		} catch (SocketTimeoutException e) {
			
		} catch (IOException e) {
			
		} catch (JSONException e) {
					} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}		

		return null;
	}
	
	public static Bitmap downloadBitmap(String url) {
	    final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
	    final HttpGet getRequest = new HttpGet(url);

	    try {
	        HttpResponse response = client.execute(getRequest);
	        final int statusCode = response.getStatusLine().getStatusCode();
	        if (statusCode != HttpStatus.SC_OK) { 
	            Log.w("ImageDownloader", "Error " + statusCode + " while retrieving bitmap from " + url); 
	            return null;
	        }
	        
	        final HttpEntity entity = response.getEntity();
	        if (entity != null) {
	            InputStream inputStream = null;
	            try {
	                inputStream = entity.getContent(); 
	                final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
	                return bitmap;
	            } finally {
	                if (inputStream != null) {
	                    inputStream.close();  
	                }
	                entity.consumeContent();
	            }
	        }
	    } catch (Exception e) {
	       
	        getRequest.abort();
	        Log.w("ImageDownloader", "Error while retrieving bitmap from " + url, e);
	    } finally {
	        if (client != null) {
	            client.close();
	        }
	    }
	    return null;
	}

	private static String getResponseText(InputStream inStream) {
		
		
		return new Scanner(inStream).useDelimiter("\\A").next();
	}

}