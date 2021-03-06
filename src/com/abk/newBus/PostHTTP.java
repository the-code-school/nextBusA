package com.abk.newBus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.util.Log;

public class PostHTTP extends AsyncTask<String, Void, String> {
	
	//private static final String LOG_TAG = "HelloWorld";
	private MainActivity mactivity;
	
	public PostHTTP(MainActivity activity) {
        this.mactivity = activity;
    }
	
    @Override
    protected String doInBackground(String... s) {
    	// url, stopNo, routNo (optional)

    	int count = s.length;
    	String responseString = "No data";
    	
    	DefaultHttpClient httpClient = new DefaultHttpClient();
    	HashMap<String, String> hm = new HashMap<String, String>();
		
	    hm.put("appID", "ed824758");
	    hm.put("apiKey", "c3c57e1c882c12fcea3684ce89176205");
	    hm.put("format", "json");
	    hm.put("stopNo", s[1]);
	    
	    if (count > 2) {
	    	hm.put("routeNo", s[2]);
	    }

        HttpResponse response = doPost(s[0], hm, null, null, httpClient);
        
        HttpEntity entity = response.getEntity();
        
        if(entity != null) {
        	try {
        		InputStream stream = entity.getContent();
        		responseString = PostHTTP.convertStreamToString(stream);
                //tv_View.setText(responseString);
        	} catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return responseString;
    }
    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
    	mactivity.handleResult(result);
   }

	public static HttpResponse doPost(String mUrl, HashMap<String, String> hm, String username, String password, DefaultHttpClient httpClient) {
		HttpResponse response = null;
		if (username != null && password != null) {
			httpClient.getCredentialsProvider().setCredentials(
					new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
					new UsernamePasswordCredentials(username, password));
		}

		HttpPost postMethod = new HttpPost(mUrl);
		if (hm == null) return null;
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			Iterator<String> it = hm.keySet().iterator();
			String k, v;
			while (it.hasNext()) {
				k = it.next();
				v = hm.get(k);
				nameValuePairs.add(new BasicNameValuePair(k, v));
			}

			postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			response = httpClient.execute(postMethod);
			//Log.i(LOG_TAG, "STATUS CODE: " + String.valueOf(response.getStatusLine().getStatusCode()));

		} catch (Exception e) {
			Log.e("Exception", e.getMessage());
		} finally {
		}

		return response;
	}
	
	public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
  
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}

