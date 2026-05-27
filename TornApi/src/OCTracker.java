
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
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
 *
 * @author deeagraw
 */
public class OCTracker {

    static final String apiKey = "ct3r1yts";
    static JFrame parent = new JFrame();

    public static void main(String[] args) {
        Properties systemProperties = System.getProperties();
        //       systemProperties.setProperty("http.proxyHost", "www-proxy.idc.oracle.com");
        //     systemProperties.setProperty("http.proxyPort", "80");
        parent.setAlwaysOnTop(true);
        parent.setLocation(800, 450);
        long minTime = Long.MAX_VALUE;
        while (true) {
            minTime = Long.MAX_VALUE;
            String u = "http://api.torn.com/faction/?selections=crimes&key=" + apiKey;
            try {
                URL url = new URL(u);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                if (conn.getResponseCode() != 200) {
                    //               throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
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
                JSONObject name = (JSONObject) profile.get("crimes");
                Set keys = name.keySet();
                Iterator it = keys.iterator();
                while (it.hasNext()) {
                    JSONObject OC = (JSONObject) name.get(it.next());
                    int initiated = Integer.parseInt(OC.get("initiated").toString());
                    if (initiated == 1) {
                        continue;
                    }
                    long time_left = Long.parseLong(OC.get("time_left").toString());
                    System.out.println(time_left);
                    if (minTime > time_left) {
                        minTime = time_left;
                    }
                    if (time_left == 0) {
                        //TODO
                        String players = OC.get("participants").toString();
                        checkPlayerStatuses(players);
                    } else if (time_left == 24 * 60 * 60) {
                        String crime_name = OC.get("crime_name").toString();
                        if (crime_name.equals("Robbing of a money train")) {
                            sendMail();
                        }
                    }
                }
                JOptionPane.showMessageDialog(parent, "Minimum time is " + minTime / 3600.0 + "hours");
                if (minTime > 24 * 60 * 60) {
                    Thread.sleep(1000 * (minTime - 23 * 60 * 60));
                } else if (minTime > 600) {
                    Thread.sleep(1000 * (minTime - 600));
                } else {
                    Thread.sleep(1000 * 5 * 60);
                }
            } catch (Exception e) {
                systemProperties.setProperty("http.proxyHost", "www-proxy.idc.oracle.com");
                systemProperties.setProperty("http.proxyPort", "80");
            }
        }
    }

    static void checkPlayerStatuses(String players) throws Exception {
        String tokens[] = players.split(",");
        boolean flag = true;
        for (int i = 0; i < tokens.length; i++) {
            String u = "http://api.torn.com/user/" + tokens[i] + "?selections=profile&key=" + apiKey;
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
            JSONArray name = (JSONArray) profile.get("status");
            String[] status = new String[2];
            for (int j = 0; j < name.size(); j++) {
                // JSONObject obj = (JSONObject)name.get(i);
                status[j] = (String) name.get(j);
            }
            if (!(status[0]).equals("Okay")) {
                flag = false;
                break;
            }
            if ((status[1]).contains("Traveling")) {
                flag = false;
                break;
            }
        }
        if (flag) {
            JOptionPane.showMessageDialog(parent, "OC can be initiated");
        }
    }

    static void sendMail() {
        JFrame parent = new JFrame();
        parent.setAlwaysOnTop(true);
        parent.setLocation(800, 450);
        JOptionPane.showMessageDialog(parent, "Send mail to players to rehab");
    }
}
