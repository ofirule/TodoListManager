package il.ac.huji.todolist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TwitObj implements  Comparable<TwitObj>  {
	
	private static final int MAX_TWITS = 100;
	
	private long id;
	private String body;
	private String dCreated;

	public TwitObj(long id, String body, String dCreated){
		this.id = id;
		this.body = body;
		this.dCreated = dCreated;
	}
	
	/////////////////////////////////////PUBLIC_METHODS//////////////////////////////////////////////////////////////

	public long getId() {return this.id;}	
	public String getBody() {return this.body;}
	public String getDateCreated() {return this.dCreated;}
	
	@Override
	public int compareTo(TwitObj another) {
		// Auto-generated method stub
		if (this.getId() == another.getId()) {return 0;}
		else if (this.getId() < another.getId()) {return -1;};
		return 1;
	}

	//limit the twits array to the earliest 100
	public static ArrayList<TwitObj> getTwitsArray (String wantedHashtag, long minimalId) throws IOException, JSONException {
		ArrayList<TwitObj> twitsArray = getAllTwitsArray(wantedHashtag, minimalId);
		Collections.sort(twitsArray);
		if (twitsArray.size() > MAX_TWITS){
			for (int i=MAX_TWITS; i<twitsArray.size(); i++) {
				twitsArray.remove(MAX_TWITS);
			}
		}
		return twitsArray;
	}
	
	
	public static long getLatestTweetIdFromArray(ArrayList<TwitObj> twitsArray) {
		long latestTweetId = 0;
		
		for (int i=0; i<twitsArray.size(); i++) {
			if (twitsArray.get(i).getId() > latestTweetId) {
				latestTweetId = twitsArray.get(i).getId();
			}
		}
		
		return latestTweetId;
	}
	/////////////////////////////////////PRIVATE_METHODS//////////////////////////////////////////////////////////////
	private static URL getURLFromHashNMinimalId (String wantedHashtag, long minimalId) throws MalformedURLException{
		return new URL("http://search.twitter.com/search.json?q=%23" + wantedHashtag + "&rpp=100&since_id=" + minimalId);
	}
	
	private static String getDataFromConnection(InputStream inStream) throws IOException {
		BufferedReader bufReader = new BufferedReader(new InputStreamReader(inStream));
		StringBuffer buf = new StringBuffer();
		for (String line = bufReader.readLine(); line != null; line = bufReader.readLine()) {
			buf.append(line);
			buf.append('\n');
		}
		return buf.toString();
	}
	
	private static String getDataFromTwitter (String wantedHashtag, long minimalId) throws IOException{
		URL url = getURLFromHashNMinimalId(wantedHashtag, minimalId);
		HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
		return getDataFromConnection(httpConnection.getInputStream());
	}
	
	
	private static ArrayList<TwitObj> getTwitsArrayFromTwitterData (String data) throws JSONException{
		JSONObject jsonObj = new JSONObject(data);
		JSONArray jsonArr = jsonObj.getJSONArray("results");
		ArrayList<TwitObj> twitsArray = new ArrayList<TwitObj>();
		for (int i=0; i<jsonArr.length(); i++) {			
			long id = jsonArr.getJSONObject(i).getLong("id");
			String text = jsonArr.getJSONObject(i).getString("text");
			String createdAt = jsonArr.getJSONObject(i).getString("created_at");
			twitsArray.add(new TwitObj(id, text, createdAt));
		}
		
		return twitsArray;
	}

	private static ArrayList<TwitObj> getAllTwitsArray(String wantedHashtag, long minimalId) throws IOException, JSONException {
		String data = getDataFromTwitter(wantedHashtag, minimalId);
		return getTwitsArrayFromTwitterData(data);
	}
	
	
}
