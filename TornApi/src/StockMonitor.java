
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import javax.net.ssl.HttpsURLConnection;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
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
public class StockMonitor {

    static String[] stocksList = {"TCSE", "TSBC", "TCB", "SYS", "SLAG", "IOU", "GRN", "TCHS", "YAZ", "TCT",
        "CNC", "MSG", "TMI", "TCP", "IIL", "FHG", "SYM", "LSC", "PRN", "EWM", "TCM", "ELBT", "HRG", "TGP", null,
        "WSSB", "ISTC", "BAG", "EVL", "MCS", "WLT", "TCC"};
    static String[] forecast = new String[32];
    static String[] demand = new String[32];
    static long[] shares = new long[32];
    static long[] totalShares = new long[32];
    static double[] price = new double[32];
    static String message = "";
    public static void main(String[] args) throws Exception {
        Properties systemProperties = System.getProperties();
        systemProperties.setProperty("https.proxyHost", "www-proxy.idc.oracle.com");
        systemProperties.setProperty("https.proxyPort", "80");
        int argsLen = args.length;
        if (argsLen < 1) {
            System.out.println("Invalid syntax");
            System.exit(1);
        }
        String apiKey = args[0];
        ArrayList al = new ArrayList();
        for (int i = 1; i < argsLen; i++) {
            al.add(args[i]);
        }
        int count = 1;
        double oldValue = 0.0;
        while (true) {
            message="";
            String u = "https://api.torn.com/torn/?selections=stocks&key=" + apiKey;
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
            JSONObject profile = (JSONObject) jsonParser.parse(output);
            JSONObject name = (JSONObject) profile.get("stocks");
           
            String av = "";
            String inc = "";
            for (int i = 1; i < argsLen; i++) {
                inc = "increased";
                int code = getStockId(args[i].toString());
                if (code < 0) {
                    System.out.println("Invalid stock code");
                    System.exit(1);
                }
                oldValue= price[code];
                av = ((JSONObject) name.get(new Integer(code).toString())).get("current_price").toString();
                double newValue = Double.parseDouble(av);
                if (newValue != oldValue) {
                    if (newValue < oldValue) {
                        inc = "decreased";
                    }
                    if (count != 1) {
                        message += "\n" + Math.round(Math.abs(newValue - oldValue) * 1000d) / 1000d + " " + args[i] + " value " + inc;
                    }
                    price[code] = newValue;
                }
            }
            for (int i = 0; i < 32; i++) {
                if (i == 24) {
                    continue;
                }
                String nts = ((JSONObject) name.get(new Integer(i).toString())).get("total_shares").toString();
                long newTotalShares = Long.parseLong(nts);
                long oldTotalShares = totalShares[i];
                if(newTotalShares>oldTotalShares && count!=1)
                {
                    message += "\n"+Math.abs(newTotalShares-oldTotalShares)+" shares of "+ stocksList[i] +" added by NPC";
                }
                totalShares[i] = newTotalShares;
                av = ((JSONObject) name.get(new Integer(i).toString())).get("available_shares").toString();
                long newShares = Long.parseLong(av);
                long oldShares = shares[i];
                inc = "increased";
                if (newShares != oldShares) {
                    shares[i] = newShares;
                    if (newShares < oldShares) {
                        inc = "decreased";
                    }
                    if (count != 1) {
                        message += "\n" + Math.abs(newShares - oldShares) + " " + stocksList[i] + " shares " + inc;
                        System.out.println(message);
                    }
                }
                String newForecast = ((JSONObject) name.get(new Integer(i).toString())).get("forecast").toString();
                String oldForecast = forecast[i];
                if (!newForecast.equals(oldForecast)) {
                    if (count != 1) {
                        message += "\nForecast of " + stocksList[i] + " changed from " + oldForecast + " to " + newForecast;
                        System.out.println(message);
                    }
                    forecast[i] = newForecast;
                }
                String newDemand = ((JSONObject) name.get(new Integer(i).toString())).get("demand").toString();
                String oldDemand = demand[i];
                if (!newDemand.equals(oldDemand)) {
                    if (count != 1) {
                        message += "\nDemand of " + stocksList[i] + " changed from " + oldDemand + " to " + newDemand;
                        System.out.println(message);
                    }
                    demand[i] = newDemand;
                }
                if(oldShares == 0 && newShares !=0 && count!=1)
                    message += "\n" + Math.abs(newShares-oldShares) + " shares of "+stocksList[i] + " are now available and demand is "+newDemand;
            }
            JFrame parent = new JFrame();
            parent.setAlwaysOnTop(true);
            parent.setLocation(800, 450);
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            if (!message.equals("")) {
                JOptionPane.showMessageDialog(parent, message);
            }
            count++;
            Thread.sleep(1000 * 1 * 60);
        }
    }

    static int getStockId(String stockCode) {
        for (int i = 0; i < stocksList.length; i++) {
            if (stockCode.equals(stocksList[i])) {
                return i;
            }
        }
        return -1;
    }
}
