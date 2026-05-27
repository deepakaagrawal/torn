import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
    
import javax.mail.*;    
import javax.mail.internet.*; 

public class Cron {
	
	static final String hostname = "mumbai51408.bom2.fusionappsdbom1.oraclevcn.com";
	static final int port = 1522;
	static final String sid = "mumbai51";
	static Connection con = null;
	static Statement statement = null;
	static Statement statement1 = null;
	static Set obj = new HashSet();
	static String msg="";
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Timer t = new Timer();  
		TimerTask tt = new TimerTask() {  
		    @Override  
		    public void run() {  
		     
		       actualMethod(14);
		       actualMethod(17);
		       generateReport();
		       System.out.println(msg);
		       Mailer.send("torngamer1@gmail.com","Your torn summary",msg);  
		    };  
		};  
		t.schedule(tt, new Date(),86400000);  
	}
	
	static void actualMethod(int cat) {
		try {
			//Properties systemProperties = System.getProperties();
			//systemProperties.setProperty("https.proxyHost", "www-proxy.us.oracle.com");
			//systemProperties.setProperty("https.proxyPort", "80");
			
			/*Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection("jdbc:oracle:thin:@"+hostname+":"+port+":"+sid, "fusion","fusion");
			statement = con.createStatement();
			statement1 = con.createStatement();*/
			System.out.println("Task is on");  
			c5920 o5920 = new c5920();
			c8321 o8321 = new c8321();
			c5010 o5010 = new c5010();
			c8340 o8340 = new c8340();
			c1220 o1220 = new c1220();
			c6005 o6005 = new c6005();
			c8310 o8310 = new c8310();
			c4201 o4201 = new c4201();
			c8350 o8350 = new c8350();
			c8320 o8320 = new c8320();
			c8355 o8355 = new c8355();
			c1221 o1221 = new c1221();
			c5720 o5720 = new c5720();
			c6221 o6221 = new c6221();
			
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
			long e = System.currentTimeMillis()/1000;
			long s = e-86400;
			while(output == null) {
				u = "https://api.torn.com/user/?selections=log&cat="+cat+"&key=HCiishg1krSG4VDX&from="+s+"&to="+e;
				System.out.println(u);
				count++;
				// System.out.println(u);
				if (count % 75 == 0) {
					ct = (System.currentTimeMillis() - ot) / (1000);
					// System.out.println("count = "+count+" ct = "+ct);
					if (ct < 60) {
					//	 System.out.println("Sleeping for " + (60 - ct) + " seconds");
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
				Thread.sleep(1000 * 60 * 5);
			} else if (!output.contains("NPC")) {
				JSONParser jsonParser = new JSONParser();
				JSONObject JSONoutput = (JSONObject) jsonParser.parse(output);
				JSONObject log = (JSONObject) JSONoutput.get("log");
				Set types = new HashSet();
				Set set = log.entrySet();
				Iterator ei = set.iterator();
				System.out.println(log.toString());
				while (ei.hasNext()) {
					Map.Entry map = (Map.Entry) ei.next();
					String key = (String) map.getKey();
					JSONObject logType = (JSONObject) log.get(key);
					JSONObject logDetails = (JSONObject) logType.get("data");
					System.out.println(logType.toString());
					Long type = (Long)logType.get("log");
					switch(type.intValue()) {
					case 5920: o5920.upkeep_paid += (Long)logDetails.get("upkeep_paid");
								obj.add(o5920);
								break;
					case 8321: o8321.bet_amount+=(Long)logDetails.get("bet_amount");
								o8321.won_amount+=(Long)logDetails.get("won_amount");
								obj.add(o8321);
								break;
					case 5010: o5010.quantity+=(Long)logDetails.get("quantity");
								o5010.cost_total+=(Long)logDetails.get("cost_total");
								obj.add(o5010);
								break;
					case 8340: o8340.lottery+=(String)logDetails.get("lottery");
								o8340.cost+=(Long)logDetails.get("cost");
								obj.add(o8340);
								break;
					case 1220: o1220.item+=(Long)logDetails.get("item");
								o1220.quantity+=(Long)logDetails.get("quantity");
								o1220.total_cost+=(Long)logDetails.get("total_cost");
								obj.add(o1220);
								break;
					case 6005: o6005.cost+=(Long)logDetails.get("cost");
								o6005.rehab_times+=(Long)logDetails.get("rehab_times");
								obj.add(o6005);
								break;
					case 8310: o8310.bet_amount+=(Long)logDetails.get("bet_amount");
								obj.add(o8310);
								break;
					case 4201: o4201.item+=(Long)logDetails.get("item");
								o4201.quantity+=(Long)logDetails.get("quantity");
								o4201.total_cost+=(Long)logDetails.get("total_cost");
								obj.add(o4201);
								break;
					case 8350: o8350.bet+=(Long)logDetails.get("bet");
								obj.add(o8350);
								break;
					case 8320: o8320.bet_amount+=(Long)logDetails.get("bet_amount");
								o8320.won_amount+=(Long)logDetails.get("won_amount");
								obj.add(o8320);
								break;
					case 8355: o8355.winnings+=(Long)logDetails.get("winnings");
								obj.add(o8355);
								break;
					case 1221: o1221.item+=(Long)logDetails.get("item");
								o1221.quantity+=(Long)logDetails.get("quantity");
								o1221.total_cost+=(Long)logDetails.get("total_cost");
								obj.add(o1221);
								break;
					case 5720: o5720.crime+=(Long)logDetails.get("crime");
								o5720.nerve+=(Long)logDetails.get("nerve");
								o5720.money_gained+=(Long)logDetails.get("money_gained");
								obj.add(o5720);
								break;
					case 6221: o6221.pay+=(Long)logDetails.get("pay");
								obj.add(o6221);
								break;
					default:	msg+="There is new case "+type+"\n";
								break;
					}
				}
				
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}	
	
	}
	static void generateReport(){
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

		symbols.setGroupingSeparator(',');
		formatter.setDecimalFormatSymbols(symbols);
		
		int size = obj.size();
		for(Object i:obj) {
			if(i instanceof c5920) {
				msg+="Spent "+formatter.format(((c5920) i).upkeep_paid)+" on upkeep\n";
				continue;
			} 
			if(i instanceof c8321) {
				msg+="Lost "+formatter.format(((c8321) i).bet_amount)+" on keno\n";
				continue;
			} 
			if(i instanceof c5010) {
				msg+="Bought "+formatter.format(((c5010) i).quantity)+" points worth "+formatter.format(((c5010) i).cost_total)+"\n";
				continue;
			} 
			if(i instanceof c8340) {
				msg+="Spent "+formatter.format(((c8340) i).cost)+" on Lotteries\n";
				continue;
			} 
			if(i instanceof c1220) {
				msg+="Bought "+formatter.format(((c1220) i).quantity)+" "+((c1220) i).item+" worth "+formatter.format(((c1220) i).total_cost)+"\n";
				continue;
			}
			if(i instanceof c6005) {
				msg+="Spent "+formatter.format(((c6005) i).cost)+" rehabbing "+formatter.format(((c6005) i).rehab_times)+" times\n";
				continue;
			}
			if(i instanceof c8310) {
				msg+="Spent "+formatter.format(((c8310) i).bet_amount)+" on high-low \n";
				continue;
			}
			if(i instanceof c4201) {
				msg+="Bought "+formatter.format(((c4201) i).quantity)+" "+((c4201) i).item+" worth "+formatter.format(((c4201) i).total_cost)+"\n";
				continue;
			}
			if(i instanceof c8350) {
				msg+="Lost "+formatter.format(((c8350) i).bet)+" in blackjack\n";
				continue;
			}
			if(i instanceof c8320) {
				msg+="Won "+formatter.format((((c8320) i).won_amount - ((c8320) i).bet_amount))+" in keno\n";
				continue;
			}
			if(i instanceof c8355) {
				msg+="Won "+formatter.format(((c8355) i).winnings) +" in blackjack\n";
				continue;
			}
			if(i instanceof c1221) {
				msg+="Sold "+formatter.format(((c1221) i).quantity)+" "+((c1221) i).item+" worth "+formatter.format(((c1221) i).total_cost)+"\n";
				continue;
			}
			if(i instanceof c5720) {
				msg+="Made "+formatter.format(((c5720) i).money_gained)+" using "+formatter.format(((c5720) i).nerve)+" nerve from crimes\n";
				continue;
			}
			if(i instanceof c6221) {
				msg+="Paid "+formatter.format(((c6221) i).pay)+" from company\n";
				continue;
			}
		}
	}

}

class c8350{
	String title = "Casino blackjack start";
	long bet;
	long bet_amount;
	long won_amount;
	long cost;
	long rehab_times;
	long item;
	long quantity;
	long total_cost;
	String lottery;
	long upkeep_paid;
	long cost_total;
	long pay;
	long winnings;
	long crime;
	long nerve;
	long money_gained;
}

class c8321{
	String title = "Casino keno lose";
	long bet;
	long bet_amount;
	long won_amount;
	long cost;
	long rehab_times;
	long item;
	long quantity;
	long total_cost;
	String lottery;
	long upkeep_paid;
	long cost_total;
	long pay;
	long winnings;
	long crime;
	long nerve;
	long money_gained;
}

class c6005{
	String title = "Rehab";
	long bet;
	long bet_amount;
	long won_amount;
	long cost;
	long rehab_times;
	long item;
	long quantity;
	long total_cost;
	String lottery;
	long upkeep_paid;
	long cost_total;
	long pay;
	long winnings;
	long crime;
	long nerve;
	long money_gained;
}

class c8310{
	String title = "Casino high-low start";
	long bet;
	long bet_amount;
	long won_amount;
	long cost;
	long rehab_times;
	long item;
	long quantity;
	long total_cost;
	String lottery;
	long upkeep_paid;
	long cost_total;
	long pay;
	long winnings;
	long crime;
	long nerve;
	long money_gained;
}

class c4201{
	String title = "Item abroad buy";
	long bet;
	long bet_amount;
	long won_amount;
	long cost;
	long rehab_times;
	long item;
	long quantity;
	long total_cost;
	String lottery;
	long upkeep_paid;
	long cost_total;
	long pay;
	long winnings;
	long crime;
	long nerve;
	long money_gained;
}

class c1220{
	String title = "Bazaar buy";
	long bet;
	long bet_amount;
	long won_amount;
	long cost;
	long rehab_times;
	long item;
	long quantity;
	long total_cost;
	String lottery;
	long upkeep_paid;
	long cost_total;
	long pay;
	long winnings;
	long crime;
	long nerve;
	long money_gained;
}

class c8340{
	String title = "Casino lottery bet";
	long bet;
	long bet_amount;
	long won_amount;
	long cost;
	long rehab_times;
	long item;
	long quantity;
	long total_cost;
	String lottery;
	long upkeep_paid;
	long cost_total;
	long pay;
	long winnings;
	long crime;
	long nerve;
	long money_gained;
}

class c5920{
	String title = "Property upkeep";
	long bet;
	long bet_amount;
	long won_amount;
	long cost;
	long rehab_times;
	long item;
	long quantity;
	long total_cost;
	String lottery;
	long upkeep_paid;
	long cost_total;
	long pay;
	long winnings;
	long crime;
	long nerve;
	long money_gained;
}

class c5010{
	String title = "Points market buy";
	long bet;
	long bet_amount;
	long won_amount;
	long cost;
	long rehab_times;
	long item;
	long quantity;
	long total_cost;
	String lottery;
	long upkeep_paid;
	long cost_total;
	long pay;
	long winnings;
	long crime;
	long nerve;
	long money_gained;
}

class c6221{
	String title = "Company employee pay";
	long bet;
	long bet_amount;
	long won_amount;
	long cost;
	long rehab_times;
	long item;
	long quantity;
	long total_cost;
	String lottery;
	long upkeep_paid;
	long cost_total;
	long pay;
	long winnings;
	long crime;
	long nerve;
	long money_gained;
}

class c8355{
	String title = "Casino blackjack win";
	long bet;
	long bet_amount;
	long won_amount;
	long cost;
	long rehab_times;
	long item;
	long quantity;
	long total_cost;
	String lottery;
	long upkeep_paid;
	long cost_total;
	long pay;
	long winnings;
	long crime;
	long nerve;
	long money_gained;
}

class c8320{
	String title = "Casino keno win";
	long bet;
	long bet_amount;
	long won_amount;
	long cost;
	long rehab_times;
	long item;
	long quantity;
	long total_cost;
	String lottery;
	long upkeep_paid;
	long cost_total;
	long pay;
	long winnings;
	long crime;
	long nerve;
	long money_gained;
}

class c1221{
	String title = "Bazaar sell";
	long bet;
	long bet_amount;
	long won_amount;
	long cost;
	long rehab_times;
	long item;
	long quantity;
	long total_cost;
	String lottery;
	long upkeep_paid;
	long cost_total;
	long pay;
	long winnings;
	long crime;
	long nerve;
	long money_gained;
}

class c5720{
	String title = "Crime success money gain";
	long bet;
	long bet_amount;
	long won_amount;
	long cost;
	long rehab_times;
	long item;
	long quantity;
	long total_cost;
	String lottery;
	long upkeep_paid;
	long cost_total;
	long pay;
	long winnings;
	long crime;
	long nerve;
	long money_gained;
}

class Mailer{  
    public static void send(String to,String sub,String msg){  
    	Properties props = System.getProperties();
		props.setProperty("https.proxyHost", "www-proxy.us.oracle.com");
		props.setProperty("https.proxyPort", "80");
          //Get properties object    
          props.remove("https.proxyHost");
          props.remove("https.proxyPort");
          props.put("mail.smtp.host", "smtp.gmail.com");    
          props.put("mail.smtp.socketFactory.port", "465");    
          props.put("mail.smtp.socketFactory.class",    
                    "javax.net.ssl.SSLSocketFactory");   
          
          props.put("mail.smtp.auth", "true");    
          props.put("mail.smtp.port", "465");  
         
          //get Session   
          Session session = Session.getDefaultInstance(props,    
           new javax.mail.Authenticator() {    
           protected PasswordAuthentication getPasswordAuthentication() {    
           return new PasswordAuthentication("torngamer1@gmail.com","oneighty");  
           }    
          });    
          //compose message    
          try {    
           MimeMessage message = new MimeMessage(session);    
           message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));    
           message.setSubject(sub);    
           message.setText(msg);    
           //send message  
           Transport.send(message);    
           System.out.println("message sent successfully");    
          } catch (MessagingException e) {
        	  e.printStackTrace();
        	  throw new RuntimeException(e);}    
             
    }  
}  
