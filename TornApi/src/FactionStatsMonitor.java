
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author deeagraw
 */
public class FactionStatsMonitor {

    static Connection con = null;
    static Statement stmt = null;
    static int activeProfiles = 0;
    static long startTime = 0;
    static long currentTime = 0;
    static int apiHit = 0;
    static String apiKey = "tbQgiQjG3KMGQQI8";
    static String apiKey2 = "s1cbVVvy";
    static String apiKey3 = "z3owgU9u";
    static int userProcessed = 0;
    int startUserId = 0;
    int endUserId = 0;
    String mode = null;

    //   static String output;
    static int x = 0;
    static ArrayList al = new ArrayList();
    static int done = 0;
    static ArrayList errored = new ArrayList();
    //  static QuantityTreeManager qtam = null;

    public FactionStatsMonitor() {
    }

    public FactionStatsMonitor(int start, int end, String mode) {
        this.startUserId = start;
        this.endUserId = end;
        this.mode = mode;
    }

    public static void main(String[] args) {
        // TODO code application logic here
        try {
            Properties systemProperties = System.getProperties();
            //systemProperties.setProperty("http.proxyHost", "www-proxy-idc.in.oracle.com");
            //systemProperties.setProperty("http.proxyPort", "80");
          //  Class.forName("oracle.jdbc.driver.OracleDriver");
         //   con = DriverManager.getConnection("jdbc:oracle:thin:@indl72105.in.oracle.com:1522:in72105", "fusion", "fusion");
       //     stmt = con.createStatement();
          //  updatePersonalStats();
            factionMisc("profile","tbQgiQjG3KMGQQI8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void factionMisc(String selection, String apiKey) throws Exception {
        ArrayList al = getFactionMembers();
        for (int loop = 0; loop < al.size(); loop++) {
            int userId = Integer.parseInt(al.get(loop).toString());
            String u = "https://api.torn.com/user/" + userId + "?selections=" + selection + "&key=" + apiKey;
            URL url = new URL(u);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            if (conn.getResponseCode() != 200) {
                System.out.println("Failed : HTTP error code : " + conn.getResponseCode());
                return;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            //System.out.println("Output from Server .... \n");
            String output = br.readLine();
            //   System.out.println(output);
            if (output.contains("Too many requests")) {
                System.out.println("++++++++++++++++++++++++++++++++Too many requests for key " + apiKey + " ...sleeping for 15 minutes");
                Thread.sleep(1000 * 60 * 15);
            }
            JSONParser jsonParser = new JSONParser();
            JSONObject personalStats = (JSONObject) jsonParser.parse(output);
        //    System.out.println(personalStats);
            JSONObject married = (JSONObject) personalStats.get("married");
            //System.out.println(userId+"\t"+personalStats.toString());
            if (married == null) {
                System.out.println("**************************Not Married ");
                System.out.println("**************************output =  " + output);
                return;
            }
           // Set s = personalStats.keySet();
            if((Long)married.get("duration")==0)
            {
            	long pid = (Long)personalStats.get("property_id");
            	u = "https://api.torn.com/property/" + pid + "?selections=property&key=" + apiKey;
                url = new URL(u);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
                
                 br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                //System.out.println("Output from Server .... \n");
                output = br.readLine();
             //   System.out.println(u);
               // System.out.println(output);
                JSONObject propertyJSON = (JSONObject) jsonParser.parse(output);
                JSONObject property = (JSONObject) propertyJSON.get("property");
                long ptype = (Long)property.get("property_type");
                if(ptype == 13)
                	System.out.println(personalStats.get("name"));
                
            }
          //  Iterator i = s.iterator();
           
        }
    }

    
    public static void updatePersonalStats() throws Exception {
        Statement stmt1 = con.createStatement();
        ResultSet rs = stmt.executeQuery("select user_id from TORN_PLAYERS where  user_id not in(select userid from torn_personal_stats)");
        startTime = System.currentTimeMillis();
        while (rs.next()) {
            al.add(rs.getInt(1));
            if (al.size() % 100 == 0) {
                System.out.println("Added " + al.size() + " players in " + (System.currentTimeMillis() - startTime) / (1000) + " seconds");
            }
        }
        Collections.sort(al);
        parsePersonalStats("personalstats,crimes,profile", apiKey);
    }

    public static void parsePersonalStats(String selection, String apiKey) throws Exception {
        ArrayList al = getFactionMembers();
        for (int loop = 0; loop < al.size(); loop++) {
            int userId = Integer.parseInt(al.get(loop).toString());
            String u = "http://api.torn.com/user/" + userId + "?selections=" + selection + "&key=" + apiKey;
            Thread.sleep(1000);
            URL url = new URL(u);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                System.out.println("Failed : HTTP error code : " + conn.getResponseCode());
                return;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            //System.out.println("Output from Server .... \n");
            String output = br.readLine();
            //   System.out.println(output);
            if (output.contains("Too many requests")) {
                System.out.println("++++++++++++++++++++++++++++++++Too many requests for key " + apiKey + " ...sleeping for 15 minutes");
                Thread.sleep(1000 * 60 * 15);
            }
            JSONParser jsonParser = new JSONParser();
            JSONObject personalStats = (JSONObject) jsonParser.parse(output);
            personalStats = (JSONObject) personalStats.get("personalstats");
            if (personalStats == null) {
                System.out.println("**************************NPE for userid ");
                System.out.println("**************************output =  " + output);
                return;
            }
            Set s = personalStats.keySet();
            Iterator i = s.iterator();
//                            // take each value from the json array separately
//            while (i.hasNext()) {
//                userId = Integer.parseInt(i.next().toString());
//                al.add(userId);
//               // System.out.println(key + " = " + personalStats.get(key));
//            }
            i = s.iterator();
            String query = "insert into torn_faction_stats(userid,";
            while (i.hasNext()) {
                String key = (String) i.next();
                query += key;
                if (i.hasNext()) {
                    query += ",";
                }
            }
            query += ") values (" + userId + ",";
            i = s.iterator();
            while (i.hasNext()) {
                String key = (String) i.next();
                query += personalStats.get(key);
                if (i.hasNext()) {
                    query += ",";
                }
            }
            query += ")";
            //        System.out.println(query);
            try {
                stmt.executeUpdate(query);
            } catch (Exception e) {
                System.out.println("Error for user " + userId);
                System.out.println(query);
                e.printStackTrace();
            }
            //
            JSONObject criminalRecord = (JSONObject) jsonParser.parse(output);
            criminalRecord = (JSONObject) criminalRecord.get("criminalrecord");
            s = criminalRecord.keySet();
            i = s.iterator();
            //                // take each value from the json array separately
            while (i.hasNext()) {
                String key = (String) i.next();
                //        System.out.println(key + " = " + criminalRecord.get(key));
            }
            i = s.iterator();
            query = "update torn_faction_stats set ";
            while (i.hasNext()) {
                String key = (String) i.next();
                query += key + "=" + criminalRecord.get(key);
                if (i.hasNext()) {
                    query += ",";
                }
            }
            query += " where userid =" + userId;
            /*   i = s.iterator();
                    while (i.hasNext()) {
                        String key = (String)i.next();
                        query += criminalRecord.get(key);
                        if (i.hasNext()) {
                            query += ",";
                        }
                    }
                    query += ")";*/
            //    System.out.println(query);
            try {
                stmt.executeUpdate(query);
            } catch (Exception e) {
                System.out.println("Error for user " + userId);
                System.out.println(query);
                e.printStackTrace();
            }
            jsonParser = new JSONParser();
            JSONObject profile = (JSONObject) jsonParser.parse(output);
            s = profile.keySet();
            i = s.iterator();
            //                // take each value from the json array separately
            while (i.hasNext()) {
                String key = (String) i.next();
                //       System.out.println(key + " = " + profile.get(key));
            }
            ArrayList a = new ArrayList();
            a.add("job");
            a.add("faction");
            a.add("married");
            a.add("status");
            a.add("life");
            a.add("signup");
            a.add("last_action");
            a.add("player_id");
            a.add("criminalrecord");
            a.add("personalstats");
            a.add("icons");
            ArrayList str = new ArrayList();
            str.add("role");
            str.add("gender");
            str.add("property");
            str.add("name");
            str.add("rank");
            i = s.iterator();
            //String query = "insert into torn_personal_stats(userid,";
            query = "update torn_faction_stats set ";
            while (i.hasNext()) {
                String key = (String) i.next();
                if ("level".equals(key)) {
                    key = "lvl";
                }
                if (a.contains(key)) {
                    continue;
                }
                query += key + "=";
                if (str.contains(key)) {
                    query += "'";
                }
                if ("lvl".equals(key)) {
                    key = "level";
                }
                query += profile.get(key);
                if (str.contains(key)) {
                    query += "'";
                }
                if (i.hasNext()) {
                    query += ",";
                }
            }
            query = query.substring(0, query.length() - 1);
            query += " where userid =" + userId;
            /*   i = s.iterator();
                                while (i.hasNext()) {
                                    String key = (String)i.next();
                                     if(a.contains(key))
                                            continue;
                                     if(str.contains(key))
                                         query += "'";
                                    query += profile.get(key);
                                    if(str.contains(key))
                                         query += "'";
                                    if (i.hasNext()) {
                                       
                                        query += ",";
                                    }
                                }
                                  query=query.substring(0,query.length()-1);
                                query += ")";*/
            //if(userId==1470)
            //   System.out.println(query);
            try {
                stmt.executeUpdate(query);
            } catch (Exception e) {
                System.out.println("Error for user " + userId);
                System.out.println(query);
                e.printStackTrace();
            }
        }
    }

    public static ArrayList getFactionMembers() throws Exception {
        String u = "https://api.torn.com/faction/?selections=basic&key=" + apiKey;
        URL url = new URL(u);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        
        if (conn.getResponseCode() != 200) {
            System.out.println("Failed : HTTP error code : " + conn.getResponseCode());
            return null;
        }
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        //System.out.println("Output from Server .... \n");
        String output = br.readLine();
           System.out.println(output);
        if (output.contains("Too many requests")) {
            System.out.println("++++++++++++++++++++++++++++++++Too many requests for key " + apiKey + " ...sleeping for 15 minutes");
            Thread.sleep(1000 * 60 * 15);
        }
        JSONParser jsonParser = new JSONParser();
        JSONObject personalStats = (JSONObject) jsonParser.parse(output);
        personalStats = (JSONObject) personalStats.get("members");
        if (personalStats == null) {
            System.out.println("**************************NPE for userid ");
            System.out.println("**************************output =  " + output);
            return null;
        }
        int userId = 0;
        Set s = personalStats.keySet();
        Iterator i = s.iterator();
        // take each value from the json array separately
        while (i.hasNext()) {
            userId = Integer.parseInt(i.next().toString());
            al.add(userId);
            // System.out.println(key + " = " + personalStats.get(key));
        }
        return al;
    }

}
