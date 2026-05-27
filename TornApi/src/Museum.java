
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Sheep Plushie	186
Teddy Bear Plushie	187
Kitten Plushie		215
Jaguar Plushie		258
Wolverine Plushie	261
Nessie Plushie		266
Red Fox Plushie		268
Monkey Plushie		269
Chamois Plushie		273
Panda Plushie		274
Lion Plushie		281
Camel Plushie		384
Stingray Plushie	618
Dahlia				260
Crocus				263
Orchid				264
Heather				267
Ceibo Flower		271
Edelweiss			272
Peony				276
Cherry Blossom		277
African Violet		282
Tribulus Omanense	385
Banana Orchid		617
 *
 * @author deeagraw
 */
public class Museum {

	static int[] itemList = { 186, 187, 215, 258, 261, 266, 268, 269, 273, 274, 281, 384, 618, 260, 263, 264, 267, 271,
			272, 276, 277, 282, 385, 617 };
	static String[] itemNames = {"Sheep Plushie","Teddy Bear Plushie","Kitten Plushie","Jaguar Plushie","Wolverine Plushie",
			"Nessie Plushie","Red Fox Plushie","Monkey Plushie","Chamois Plushie","Panda Plushie","Lion Plushie","Camel Plushie",
			"Stingray Plushie","Dahlia","Crocus","Orchid","Heather","Ceibo Flower","Edelweiss","Peony","Cherry Blossom",
			"African Violet","Tribulus Omanense","Banana Orchid"};
	static List excludeList = Arrays.asList();

	/**
	 * @param args
	 * @throws Exception
	 */
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
        Properties systemProperties = System.getProperties();
        systemProperties.setProperty("https.proxyHost", "www-proxy.idc.oracle.com");
        systemProperties.setProperty("https.proxyPort", "80");
        String msg = "";
       
        int argsLen = args.length;
        HashMap marketValue = new HashMap();
        String apiKey = "wr0oZF1rPax0mIMn";
        String u = "https://api.torn.com/torn/186,187,215,258,261,266,268,269,273,274,281,384,618,260,263,264,267,271,272,276,277,282,385,617?selections=items&comment=TornAPI&key=" + apiKey;
        URL url = new URL(u);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        StringBuilder input = new StringBuilder();
        String inputLine;
        while ((inputLine = br.readLine()) != null) {
            input.append(inputLine);
        }
        String output = input.toString();
        JSONParser jsonParser = new JSONParser();
        JSONObject obj = (JSONObject) jsonParser.parse(output);
        JSONObject items = (JSONObject) obj.get("items");
        Set ids = items.keySet();
        Iterator it = ids.iterator();
        while(it.hasNext())
        {
        	String key =(String)it.next();
        	long mvalue = (Long)(((JSONObject)items.get(key)).get("market_value"));
        	marketValue.put(key, mvalue);
        	
        }
        while(true) {
        	msg ="";
        	for(int i = 0;i<itemList.length;i++) {
		        u = "https://api.torn.com/market/"+itemList[i]+"?selections=bazaar&comment=TornAPI&key=" + apiKey;
		        url = new URL(u);
		        conn = (HttpsURLConnection) url.openConnection();
		        conn.setRequestMethod("GET");
		        conn.setRequestProperty("Accept", "application/json");
		        if (conn.getResponseCode() != 200) {
		            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		        }
		        br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		        input = new StringBuilder();
		        while ((inputLine = br.readLine()) != null) {
		            input.append(inputLine);
		        }
		        output = input.toString();
		        jsonParser = new JSONParser();
		        obj = (JSONObject) jsonParser.parse(output);
		        JSONArray bazaar = (JSONArray) obj.get("bazaar");
		        JSONObject arr = (JSONObject)bazaar.get(0);
		        long bprice = (Long)arr.get("cost");
		        if(bprice <= (long)marketValue.get(""+itemList[i]) && !excludeList.contains(itemList[i]))
		        	msg+="Buy "+itemNames[i]+" Market price - "+marketValue.get(""+itemList[i])+"\n";
        	}
        	JFrame parent = new JFrame();
            parent.setAlwaysOnTop(true);
            parent.setLocation(800, 450);
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        	JOptionPane.showMessageDialog(parent, msg);
        	Thread.sleep(1000 * 3 * 60);
        }
        
       // Thread.sleep(1000 * 1 * 60);
    }

}
