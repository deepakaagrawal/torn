/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HttpsURLConnection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author deeagraw
 */
public class DonationReport {

    static Connection con = null;
    static Statement stmt = null;
    static int activeProfiles = 0;
    static long startTime = 0;
    static long currentTime = 0;
    static Long maxTime = 0L;
    static Long maxTimeCompany = 0L;
    static List revivers = new LinkedList(Arrays.asList(459981,
152511,
344092,
85533,
1311530,
38520,
448041,
667647,
759678,
851730,
1749287,
1324531,
1580562,
1028750,
1652143,
588903,
784338,
1757281,
583088,
124851,
1612389,
108922,
844721,
974287,
292171,
51443,
1673331,
158993,
261002,
1606049,
93389,
42350,
1384349,
1013101,
617925,
1021623,
1617737,
1499158,
125670,
502675,
1160594,
1210256,
252071,
114232,
307791,
119480,
109574,
100814,
1709053,
1976145,
889032,
983854,
671324,
1075937,
1013101,
1986784,
1042466,
13524,
208223,
854724,
61156,
1760324,
331193,
557703,
889930,
902946,
1062284,
488305,
20811,
249471,
1884988,
427897,
1519576,
980485,
1732919,
688884,
1189980,
1974211,
271756,
2037305,
2071751,
278031,
1624592,
1641178,
1764304,
1771931,
1758760,
1604584,
525636,
1976488,
918378,
1834881,
1770454,
2019938,
1370954,
2085074,
1651531,
261250,
1556969,
160746
    ));
    static String apiKey = "fR7vDJBA";

    /**
     * @param args the command line arguments
     */
    static Properties systemProperties = System.getProperties();

    public static void main(String[] args) {
        // TODO code application logic here
        try {
            systemProperties.setProperty("https.proxyHost", "www-proxy.idc.oracle.com");
            systemProperties.setProperty("https.proxyPort", "80");
            Class.forName("oracle.jdbc.driver.OracleDriver");
            //   con = DriverManager.getConnection("jdbc:oracle:thin:@indl72105.in.oracle.com:1522:in72105", "fusion", "fusion");
            //   stmt = con.createStatement();
            //ResultSet rs=stmt.executeQuery("select * from emp");
           // parseFactionMembers();
            //  parseDoantionNews();
 //             parseFactionAttacks();
          //  getCompanyDetails();
//            while(true){
  //            checkTravel("2032144");
//             // checkTravel("2071318");
                getName();
//            }
           //   checkNerve();
          //    parseFactionNews();
            
             
            //  checkStocks();
        } catch (Exception e) {
            // systemProperties.setProperty("http.proxyHost", "www-proxy.us.oracle.com");
            //    systemProperties.setProperty("http.proxyPort", "80");
//                try{
//                //checkTravel();}catch(Exception ex){}
            e.printStackTrace();
        }
    }
    
    public static String readJSON(String u) throws Exception {
        URL url = new URL(u);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        String output = br.readLine();
        return output;
//        InputStream is = new URL(u).openStream();
//        StringBuilder sb = new StringBuilder();
//        try {
//            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
//            int cp;
//            while ((cp = rd.read()) != -1) {
//                sb.append((char) cp);
//            }
//        } finally {
//            is.close();
//        }
//        return sb.toString();
    }
     public static void getName() {
        int id = 2128293;
        while (true) {
            try {
                String u = "https://api.torn.com/user/" + id + "?selections=profile&key=e8StmbbT";
                String output = readJSON(u);
                JSONParser jsonParser = new JSONParser();
                JSONObject news = (JSONObject) jsonParser.parse(output);
                String name = (String) news.get("name");
                System.out.println(name);
                id++;
                if(id>=2128859)
                    break;
            } catch (Exception e) {
                System.out.println(e);
            }
        }
     }
    public static void parseFactionNews()  {
        JFrame parent = new JFrame();
            //  parent.setVisible(true);
        parent.setAlwaysOnTop(true);
        parent.setLocation(800, 450);
        JOptionPane.showMessageDialog(parent, "Running faction news");
        String msg = "";
        
        while(true){
            try{
        String u = "https://api.torn.com/faction/27370?selections=mainnews&key=" + apiKey;
        String output = readJSON(u);
        JSONParser jsonParser = new JSONParser();
        JSONObject news = (JSONObject) jsonParser.parse(output);
        JSONObject mainnews = (JSONObject) news.get("mainnews");
        TreeMap ts = new TreeMap();
        Set s = mainnews.keySet();
        Iterator i = s.iterator();
        while(i.hasNext()){
            String key = (String) i.next();
           //    System.out.println("key ="+key);
           //     System.out.println(key + " = " + mainnews.get(key));
            news = (JSONObject) mainnews.get(key);
            Long timeStamp = (Long) news.get("timestamp");
            String newsText = (String) news.get("news");
            ts.put(timeStamp,newsText);
            //System.out.println("timeStamp ="+timeStamp);
             //System.out.println("newsText ="+newsText);
        }
        NavigableSet nm = ts.descendingKeySet();
        i = nm.iterator();
        Long t = (Long)i.next();
        msg="";
        if(maxTime<t){
            msg+=ts.get(t);
            System.out.println(ts.get(t));
           while(i.hasNext())
           {
               Long t1 = (Long)i.next();
               if(maxTime<t1){
                  msg+="\n"+ts.get(t1);
                   System.out.println(ts.get(t1));
               }
               else
                   break;
           }
           maxTime = t;
        }
        if(msg.length()>0){
            msg = msg.replaceAll("<[^>]+>", "");
            
            JOptionPane.showMessageDialog(parent, msg);
        }
        //Thread.sleep(10000);
        
        msg = "";
        
         u = "https://api.torn.com/company/?selections=news&key=" + apiKey;
         output = readJSON(u);
         jsonParser = new JSONParser();
         news = (JSONObject) jsonParser.parse(output);
         mainnews = (JSONObject) news.get("news");
        TreeMap tsc = new TreeMap();
        Set sc = mainnews.keySet();
        Iterator it = sc.iterator();
        while(it.hasNext()){
            String key = (String) it.next();
           //    System.out.println("key ="+key);
           //     System.out.println(key + " = " + mainnews.get(key));
            news = (JSONObject) mainnews.get(key);
            Long timeStamp = (Long) news.get("timestamp");
            String newsText = (String) news.get("news");
            tsc.put(timeStamp,newsText);
            //System.out.println("timeStamp ="+timeStamp);
             //System.out.println("newsText ="+newsText);
        }
        NavigableSet nmc = tsc.descendingKeySet();
        i = nmc.iterator();
        Long tc = (Long)i.next();
        msg="";
        if(maxTimeCompany<tc){
            msg+=tsc.get(tc);
            System.out.println(tsc.get(tc));
           while(i.hasNext())
           {
               Long tc1 = (Long)i.next();
               if(maxTimeCompany<tc1){
                  msg+="\n"+tsc.get(tc1);
                   System.out.println(tsc.get(tc1));
               }
               else
                   break;
           }
           maxTimeCompany = tc;
        }
        if(msg.length()>0){
            msg = msg.replaceAll("<[^>]+>", "");
            
            JOptionPane.showMessageDialog(parent, msg);
        }
        Thread.sleep(10000);
        
        }catch(Exception e)
        {
            JOptionPane.showMessageDialog(parent, "Exception occured - "+e.getMessage());
        }
        }
    }
    public static void getCompanyDetails() throws Exception{
        con = DriverManager.getConnection("jdbc:oracle:thin:@indl72105.in.oracle.com:1522:in72105", "fusion", "fusion");
        stmt = con.createStatement();
        long startTime = System.currentTimeMillis();
        for (int i = 4209; i < 4210; i++) {
       
         if(i%100==0){
          
              long currentTime = System.currentTimeMillis();
              long diff = currentTime - startTime;
                    diff = diff / 1000;
                    int day = (int)TimeUnit.SECONDS.toDays(diff);
                    long hours = TimeUnit.SECONDS.toHours(diff) - (day * 24);
                    long minute = TimeUnit.SECONDS.toMinutes(diff) - (TimeUnit.SECONDS.toHours(diff) * 60);
                    long second = TimeUnit.SECONDS.toSeconds(diff) - (TimeUnit.SECONDS.toMinutes(diff) * 60);
                    System.out.println("...." + i + " companies processed..." + day + " Days " + hours + " Hours " + minute + " minute " + second + " Seconds elapsed");
         }
         if(i%10==0)
             Thread.sleep(3000);
             
            String u = "https://api.torn.com/company/" + i + "?selections=profile&key=" + apiKey;
            // System.out.println(u);
            String output = readJSON(u);
            JSONParser jsonParser = new JSONParser();
            JSONObject profile = (JSONObject) jsonParser.parse(output);
            if (profile.containsKey("error")) {
                JSONObject error = (JSONObject) profile.get("error");
                int code = Integer.parseInt(error.get("code").toString());
                if(code==5){
                    System.out.println("Too many requests");
                    Thread.sleep(1000*60*5);
                    i--;
                }
                continue;
            }
            JSONObject company = (JSONObject) profile.get("company");
            Set s = company.keySet();
            Iterator it = s.iterator();
            String sql = "insert into torn_company_details(company_id,";
            int count = 0;
            while (it.hasNext()) {
                String key = it.next().toString();
                if(key.equals("name"))
                        continue;
                if (key.equals("employees")) {
                    JSONObject employees = (JSONObject) company.get("employees");
                    for (int j = 1; j <= employees.size(); j++) {
                        sql += "pos" + j;
                        if (j != employees.size()) {
                            sql += ",";
                        }
                    }
                    continue;
                } else {
                    sql += key;
                    if (it.hasNext()) {
                        sql += ",";
                    }
                }
            }
            sql += ") values("+i+",";
            it = s.iterator();
            while (it.hasNext()) {
                String key = it.next().toString();
                  if(key.equals("name"))
                        continue;
                if(!key.equals("employees")){
                    sql+=company.get(key);
                    if (it.hasNext()) {
                        sql += ",";
                    }
                }
                else{
                    JSONObject employees = (JSONObject) company.get("employees");
                    Set emp = employees.keySet();
                    Iterator it2 = emp.iterator();
                    while(it2.hasNext()){
                        String key2 = it2.next().toString();
                        JSONObject details = (JSONObject)employees.get(key2);
                        
                        sql+="'"+details.get("position")+"'";
                        if (it2.hasNext()) {
                            sql += ",";
                        }
                    }
                }
                    
                
            }
            sql+=")";
           // System.out.println(sql);
           //System.out.println(i);
             stmt.executeUpdate(sql);
        }
    }
    public static void parseFactionMembers() throws Exception {
  
        String u = "https://api.torn.com/faction/27370?selections=basic&key=" + apiKey; 
        // System.out.println(u);
        String output = readJSON(u);
        JSONParser jsonParser = new JSONParser();
        JSONObject profile = (JSONObject) jsonParser.parse(output);
        JSONObject members = (JSONObject) profile.get("members");
        Set s = members.keySet();
        Iterator it = s.iterator();
        while (it.hasNext()) {
            int id = Integer.parseInt((String) it.next());
            if (!revivers.contains(id)) {
                u = "https://api.torn.com/user/" + id + "?selections=profile&key=" + apiKey;
                output = readJSON(u);
                jsonParser = new JSONParser();
                profile = (JSONObject) jsonParser.parse(output);
                String name = (String) profile.get("name");
                System.out.println(name + " [" + id + "] is not part of sheet");
            }
        }
        it = revivers.iterator();
        while (it.hasNext()) {
            String id = it.next().toString();
            if (!s.contains(id)) {
               
                u = "https://api.torn.com/user/" + id + "?selections=profile&key=" + apiKey;
                output = readJSON(u);
                jsonParser = new JSONParser();
                profile = (JSONObject) jsonParser.parse(output);
                String name = (String) profile.get("name");
                System.out.println(name + " is not part of faction");
            }
        }
    }

    public static void checkStocks() throws Exception {
        while (true) {
            Thread.sleep(1000 * 1 * 60);
            String u = "http://api.torn.com/torn/?selections=stocks&key=ct3r1yts";
            URL url = new URL(u);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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
            JSONObject profile = (JSONObject) jsonParser.parse(output);
            JSONObject name = (JSONObject) profile.get("stocks");
            String av = ((JSONObject) name.get("25")).get("available_shares").toString();
            int as = Integer.parseInt(av);
            //  String lastAction = null;
            JFrame parent = new JFrame();
            //  parent.setVisible(true);
            parent.setAlwaysOnTop(true);
            parent.setLocation(800, 450);
            //  JOptionPane.showMessageDialog(parent, "wadewilson out of hospital");
            if (as > 0) {
                JOptionPane.showMessageDialog(parent, as + " WSSB shares available");
            }
        }
    }

    public static void parseFactionAttacks() throws Exception {
        // String u = "http://api.torn.com/user/" + userId + "?selections=" + selection + "&key=" + apiKey;
        String u = "https://api.torn.com/faction/27370?selections=attacksfull&key=" + apiKey;
        //String u = "http://api.torn.com/user/?selections=attacks&key="+apiKey;
        String output;
        //System.out.println("Output from Server .... \n");
        output = readJSON(u);
        //   System.out.println(output);
        HashMap map = new HashMap();
        JSONParser jsonParser = new JSONParser();
        JSONObject personalStats = (JSONObject) jsonParser.parse(output);
        personalStats = (JSONObject) personalStats.get("attacks");
        Set s = personalStats.keySet();
        Iterator i = s.iterator();
        JSONObject news;
        String str = "Nov 16 2017 20:35:21.000 IST";
        SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy HH:mm:ss.SSS zzz");
        Date date = df.parse(str);
        long epoch = date.getTime();
        System.out.println(epoch);
        str = "Nov 17 2017 01:14:31.999 IST";
        date = df.parse(str);
        long epoch1 = date.getTime();
        System.out.println(epoch1);
        while (i.hasNext()) {
            //           j = revivers.iterator();
            String key = (String) i.next();
            //   System.out.println("key ="+key);
            //     System.out.println(key + " = " + personalStats.get(key));
            news = (JSONObject) personalStats.get(key);
            Long timeStamp = (Long) news.get("timestamp_started");
            Long defFactionId = (Long) news.get("defender_faction");
            if(defFactionId==27370)
                continue;
            //     System.out.println(timeStamp);
            Long attacker = (Long) news.get("attacker_id");
            
            Long defender = (Long) news.get("defender_id");
            String result = (String) news.get("result");
          //  Double respect_gain = (Double) news.get("respect_gain");
            
            String tokens[];
            //      if (d.contains("$")) {
            //        while (j.hasNext()) {
            //          String r = j.next().toString();
            //                System.out.println(r);
            //              System.out.println(d);
            if (timeStamp >= epoch / 1000 && timeStamp <= epoch1 / 1000) {
//                        tokens = d.split(" ");
//                        tokens = tokens[5].split("$");
//                        System.out.println(tokens[0]);
                date = new Date(timeStamp * 1000 + 19800000);
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
                String formatted = format.format(date);
                Long count = (Long)map.get(attacker);
                if(count == null)
                    count = 0L;
                count+=1;
                map.put(attacker, count);
               //System.out.println(formatted + ".... " + attacker + " attacked " + defender + " and " + result);
            }
            //         }
            //       }
        }
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            Long key = (Long) it.next();
            System.out.println(key + " - " + map.get(key));
            
        }
    }

    public static void checkNerve() throws Exception {
        int[] users = {284596, 271756, 89746};
        //while (true) {
        for (int i = 0; i < users.length; i++) {
            String u = "http://api.torn.com/user/" + users[i] + "?selections=profile&key=ZgPFL7DO";
            URL url = new URL(u);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                System.out.println("conn.getResponseCode()=" + conn.getResponseCode());
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
            JSONObject profile = (JSONObject) jsonParser.parse(output);
            JSONArray name = (JSONArray) profile.get("status");
            String[] questions = new String[2];
            //  String lastAction = null;
            JFrame parent = new JFrame();
            //  parent.setVisible(true);
            parent.setAlwaysOnTop(true);
            parent.setLocation(800, 450);
            //  JOptionPane.showMessageDialog(parent, "wadewilson out of hospital");
            for (int j = 0; j < name.size(); j++) {
                // JSONObject obj = (JSONObject)name.get(i);
                questions[j] = (String) name.get(j);
            }
            String lastAction = (String) profile.get("last_action");
            if (lastAction == null) {
                //         userId++;p
                return;
            }
            String[] tokens = lastAction.split(" ");
            if ("minutes".equals(tokens[1]) || "minute".equals(tokens[1])) {
                System.out.println("check user " + users[i]);
            }
            // }
        }
    }

    public static void checkTravel(String userId) throws Exception {
        HashMap time = new HashMap();
            time.put("Mexico", 18);
            time.put("Islands", 25);
            time.put("Canada", 29);
            time.put("Hawaii", 94);
            time.put("Kingdom", 111);
            time.put("Argentina", 117);
            time.put("Switzerland", 123);
            time.put("Japan", 158);
            time.put("China", 169);
            time.put("Emirates", 190);
            time.put("Africa", 208);
            String dest = null;
      //  while (true) {
            String u = "https://api.torn.com/user/"+userId+"?selections=profile&key="+apiKey;
            String output = readJSON(u);
            JSONParser jsonParser = new JSONParser();
            JSONObject profile = (JSONObject) jsonParser.parse(output);
            JSONArray name = (JSONArray) profile.get("status");
            String[] questions = new String[2];
            //  String lastAction = null;
            JFrame parent = new JFrame();
            //  parent.setVisible(true);
            parent.setAlwaysOnTop(true);
            parent.setLocation(800, 450);
            for (int i = 0; i < name.size(); i++) {
                questions[i] = (String) name.get(i);
            }
            String lastAction = (String) profile.get("last_action");
            if (lastAction == null) {
                return;
            }
            String[] tokens = lastAction.split(" ");
            //System.out.println(questions[0]);
            //System.out.println(questions[1]);
            //System.out.println(tokens[1]);
            
            if (((questions[0]).contains("hospital for") || (questions[1]).contains("Traveling"))) {
                System.out.println("No");
                if ((questions[1]).contains("Traveling to")) {
                    System.out.println(questions[1]);
                    if (dest == null) {
                        String[] destArray = questions[1].split(" ");
                        dest = destArray[destArray.length - 1];
                        System.out.println(dest);
                    }
                    System.out.println("Sleeping for 60 sec");
                    Thread.sleep(1000 * 2);
                }
                if ((questions[1]).contains("Torn")) {
                    System.out.println(questions[1]);
                    if (dest != null) {
                        long millis = Long.parseLong((String) time.get(dest)) * 60000;
                        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                        System.out.println(hms);
                        System.out.println("Sleeping for some time");
                        Thread.sleep(millis - 200000);
                        dest = null;
                    } else {
                        System.out.println("Sleeping for 2 sec");
                        Thread.sleep(1000 * 2);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(parent, "cindy out of hospital");
                System.out.println("Sleeping for 20 sec");
                Thread.sleep(1000 * 20);
                
            }
            if (!(questions[1]).contains("Traveling")) {
                if ("minutes".equals(tokens[1]) || "minute".equals(tokens[1])) {
                    System.out.println("Sleeping for 3 sec");
                    Thread.sleep(1000 * 3);
                    
                } else {
                    System.out.println("Sleeping for 10 sec");
                    Thread.sleep(1000 * 10);
                    
                }
            }
     //   }
    }

}
