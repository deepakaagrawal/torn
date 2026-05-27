

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.net.ssl.HttpsURLConnection;
import java.net.URL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

 class UpdateLastLogin {
    static Connection con = null;
    static Statement stmt = null;
    static int activeProfiles = 0;
    static long startTime = 0;
    static long currentTime = 0;
    static int apiHit = 0; 
    static String apiKey = "tbQgiQjG3KMGQQI8";
    static int userProcessed = 0;
    int startUserId = 0;
    int endUserId = 0;
    String mode = null;
    static ArrayList al = new ArrayList(); 
    //  static QuantityTreeManager qtam = null;
    
   

    public static void main(String[] args) {
        // TODO code application logic here
        try {
            Properties systemProperties = System.getProperties();
            systemProperties.setProperty("https.proxyHost", "www-proxy.us.oracle.com");
            systemProperties.setProperty("https.proxyPort", "80");
            parsePersonalStats();
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   

    public static void parsePersonalStats() throws Exception {
    	int count = 0;
    	while(true) {
	        String u = "https://api.torn.com/user/?selections=icons&key=DjsIJsTa7khbL2Vt";
	        URL url = new URL(u);
	        HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Accept", "application/json");
	        if (conn.getResponseCode() != 200) {
	            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
	        }
	        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	        String output;
	        //System.out.println("Output from Server .... \n");
	        output = br.readLine();
	        //   System.out.println(output);
	        JSONParser jsonParser = new JSONParser();
	        JSONObject personalStats = (JSONObject)jsonParser.parse(output);
	        JSONObject icons = (JSONObject)personalStats.get("icons");
	        String keys = (String)icons.get("icon63");
	        if(!keys.equals("Minor Radiation Sickness - Your battle stats in combat are slightly reduced (-16%)"))
	        {
	        	System.out.println(keys);
	        	System.out.println(System.currentTimeMillis());
	        }
	        if(count%10==0) {
	        	System.out.println(keys);
	        	System.out.println(System.currentTimeMillis());
	        }
	        Thread.sleep(60000);
	        count++;
    	}
        
    }

    
}
