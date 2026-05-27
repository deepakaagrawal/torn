import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ProfitCalc {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		long moneyGained = moneyIncoming();
		System.out.println("Money Gained ="+ moneyGained);
		Thread.sleep(1000 * 60);
		long moneySpent = moneyOutgoing();
		
		System.out.println("Money Spent ="+ moneySpent);
		System.out.println("\nTotal Profit ="+ (moneyGained-moneySpent));

	}
	public static long moneyOutgoing() throws Exception {
		long totalMoney = 0L;
		long timestamp = 1622637600L;
		long startTime = System.currentTimeMillis();
		long ct = 0;
		long ct1 = 0;
		long ot = startTime;
		long ot1 = startTime;
		int count = 0;
		int count1 = 0;
		
		SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = null;
		String u = null;
		String output = null;
		long currentTime = System.currentTimeMillis()/1000;
		while(true) {
			timestamp++;
			//System.out.println(timestamp);
			output = null;
		while(output==null) {
			
			u = "https://api.torn.com/user/?selections=log&cat=14&log=1220,1103,5010&key=HCiishg1krSG4VDX&from="+timestamp+"&to="+(timestamp+=120);
			//System.out.println(u);
			count++;
			// System.out.println(u);
			if (count % 75 == 0) {
				ct = (System.currentTimeMillis() - ot) / (1000);
				// System.out.println("count = "+count+" ct = "+ct);
				if (ct < 60) {
					 System.out.println("Sleeping for " + (60 - ct) + " seconds");
					Thread.sleep((60 - ct) * 1000);
				}
				ot = System.currentTimeMillis();

			}

			long starttime = System.currentTimeMillis();
			URL url = new URL(u);

			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

			if (conn.getResponseCode() != 200) {
				System.out.println("Failed : HTTP error code : " + conn.getResponseCode());
				//i--;
				continue;
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			// System.out.println("Output from Server .... \n");
			output = br.readLine();
			if(output==null)
				Thread.sleep(1000);
		}
		
		 if (output.contains("Too many requests")) {
			System.out.println("Too many reqs");
			Thread.sleep(1000 * 60 * 2);
		} else if (!output.contains("NPC")) {
			JSONParser jsonParser = new JSONParser();
			JSONObject JSONoutput = (JSONObject) jsonParser.parse(output);
			JSONObject log = (JSONObject) JSONoutput.get("log");
			if(log==null && timestamp<System.currentTimeMillis()/1000)
				continue;
			if(log==null && timestamp>System.currentTimeMillis()/1000)
				break;
			Set types = new HashSet();
			Set set = log.entrySet();
			Iterator ei = set.iterator();
			//System.out.println(log.toString());
			while (ei.hasNext()) {
				Map.Entry map = (Map.Entry) ei.next();
				String key = (String) map.getKey();
				JSONObject logType = (JSONObject) log.get(key);
				Long logTime = (Long)logType.get("timestamp");
				JSONObject logDetails = (JSONObject) logType.get("data");
				//System.out.println(logType.toString());
				Long type = (Long)logType.get("log");
				switch(type.intValue()) {
				case 1220: totalMoney += (Long)logDetails.get("total_cost");
				
						   if(timestamp < logTime)
							   timestamp = logTime;
						   break;
				case 1103: totalMoney +=(Long)logDetails.get("cost");
				
							if(timestamp < logTime)
								   timestamp = logTime;
							break;
				case 5010: totalMoney +=(Long)logDetails.get("cost_total");
				
							if(timestamp < logTime)
								   timestamp = logTime;
							break;
				default: System.out.println("Something weird");
						break;
				}
			}
		}
	}
	return totalMoney;
	}
	public static long moneyIncoming() throws Exception {
		long totalMoney = 0L;
		long timestamp = 1622637600L;
		long startTime = System.currentTimeMillis();
		long ct = 0;
		long ct1 = 0;
		long ot = startTime;
		long ot1 = startTime;
		int count = 0;
		int count1 = 0;
		
		SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = null;
		String u = null;
		String output = null;
		long currentTime = System.currentTimeMillis()/1000;
		while(true) {
			timestamp++;
			//System.out.println(timestamp);
			output = null;
		while(output==null) {
			
			u = "https://api.torn.com/user/?selections=log&cat=17&log=4220,5011&key=HCiishg1krSG4VDX&from="+timestamp+"&to="+(timestamp+=120);
			//System.out.println(u);
			count++;
			// System.out.println(u);
			if (count % 75 == 0) {
				ct = (System.currentTimeMillis() - ot) / (1000);
				// System.out.println("count = "+count+" ct = "+ct);
				if (ct < 60) {
					 System.out.println("Sleeping for " + (60 - ct) + " seconds");
					Thread.sleep((60 - ct) * 1000);
				}
				ot = System.currentTimeMillis();

			}

			long starttime = System.currentTimeMillis();
			URL url = new URL(u);

			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

			if (conn.getResponseCode() != 200) {
				System.out.println("Failed : HTTP error code : " + conn.getResponseCode());
				//i--;
				continue;
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			// System.out.println("Output from Server .... \n");
			output = br.readLine();
			if(output==null)
				Thread.sleep(1000);
		}
		
		 if (output.contains("Too many requests")) {
			System.out.println("Too many reqs");
			Thread.sleep(1000 * 60 * 2);
		} else if (!output.contains("NPC")) {
			JSONParser jsonParser = new JSONParser();
			JSONObject JSONoutput = (JSONObject) jsonParser.parse(output);
			JSONObject log = (JSONObject) JSONoutput.get("log");
			if(log==null && timestamp<System.currentTimeMillis()/1000)
				continue;
			if(log==null && timestamp>System.currentTimeMillis()/1000)
				break;
			Set types = new HashSet();
			Set set = log.entrySet();
			Iterator ei = set.iterator();
			//System.out.println(log.toString());
			while (ei.hasNext()) {
				Map.Entry map = (Map.Entry) ei.next();
				String key = (String) map.getKey();
				JSONObject logType = (JSONObject) log.get(key);
				Long logTime = (Long)logType.get("timestamp");
				JSONObject logDetails = (JSONObject) logType.get("data");
				//System.out.println(logType.toString());
				Long type = (Long)logType.get("log");
				switch(type.intValue()) {
				case 4220: totalMoney += (Long)logDetails.get("total_value");
						   if(timestamp < logTime)
							   timestamp = logTime;
						   break;
				case 5011: totalMoney +=(Long)logDetails.get("cost_total");
							if(timestamp < logTime)
								   timestamp = logTime;
							break;
				default: System.out.println("Something weird");
						break;
				}
			}
		}
	}
	return totalMoney;
	}

}
