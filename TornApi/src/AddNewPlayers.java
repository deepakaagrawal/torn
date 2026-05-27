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

/** OnhandUtil is a standalone java class which returns handle to the
 * QuantityTreeManager. This is to avoid nesting QunatityTreeAM in the callers'
 * AM. This Class is also used as a utility class for onhand LBA.
 */
class AddNewPlayers implements Runnable {

  static Connection con = null;
  static Statement stmt = null;
  static int activeProfiles = 0;
  static long startTime = 0;
  static long currentTime = 0;
  static int apiHit = 0;
  static String apiKey = "tbQgiQjG3KMGQQI8";
  static String apiKey2 = "EoK4v6gV";
  static String apiKey3 = "e8StmbbT";
  static int userProcessed = 0;
  int startUserId = 0;
  int endUserId = 0;
  String mode = null;
  int count1 = 0;
  int count2 = 0;
  int count3 = 0;
  int count4 = 0;
  int count5 = 0;
  int count6 = 0;
  int count7 = 0;
  int count8 = 0;
  int count9 = 0;
  int count10 = 0;
  int count11 = 0;
  int count12 = 0;
  int count13 = 0;
  int count14 = 0;
  int count15 = 0;
  //   static String output;
  static int x = 0;
  static ArrayList al = new ArrayList();
  static List sub1 = new ArrayList();
  static List sub2 = new ArrayList();
  static List sub3 = new ArrayList();
  static List sub4 = new ArrayList();
  static List sub5 = new ArrayList();
  static List sub6 = new ArrayList();
  static List sub7 = new ArrayList();
  static List sub8 = new ArrayList();
  static List sub9 = new ArrayList();
  static List sub10 = new ArrayList();
  static List sub11 = new ArrayList();
  static List sub12 = new ArrayList();
  static List sub13 = new ArrayList();
  static List sub14 = new ArrayList();
  static List sub15 = new ArrayList();
  static int done = 0;
  static ArrayList errored = new ArrayList();

  //  static QuantityTreeManager qtam = null;

  public AddNewPlayers() {}

  public AddNewPlayers(int start, int end, String mode) {
    this.startUserId = start;
    this.endUserId = end;
    this.mode = mode;
  }

  public void run() {
    try {
      int userId = startUserId;
      String key = apiKey;
      if ("Update".equals(mode)) {
        while (
          count1 < x ||
          count2 < x * 2 ||
          count3 < x * 3 ||
          count4 < x * 4 ||
          count5 < x * 5 ||
          count6 < x * 6 ||
          count7 < x * 7 ||
          count8 < x * 8 ||
          count9 < x * 9 ||
          count10 < x * 10 ||
          count11 < x * 11 ||
          count12 < x * 12 ||
          count13 < x * 13 ||
          count14 < x * 14 ||
          count15 < al.size()
        ) {
          if (Thread.currentThread().getName().equals("1")) {
            if (count1 >= x) continue;
            userId = ((Integer) sub1.get(count1)).intValue();
            count1++;
          }
          if (Thread.currentThread().getName().equals("2")) {
            if (count2 >= x) continue;
            userId = ((Integer) sub2.get(count2)).intValue();
            count2++;
          }
          if (Thread.currentThread().getName().equals("3")) {
            if (count3 >= x) continue;
            userId = ((Integer) sub3.get(count3)).intValue();
            count3++;
          }
          if (Thread.currentThread().getName().equals("4")) {
            if (count4 >= x) continue;
            userId = ((Integer) sub4.get(count4)).intValue();
            count4++;
          }
          if (Thread.currentThread().getName().equals("5")) {
            if (count5 >= x) continue;
            userId = ((Integer) sub5.get(count5)).intValue();
            count5++;
          }
          if (Thread.currentThread().getName().equals("6")) {
            if (count6 >= x) continue;
            userId = ((Integer) sub6.get(count6)).intValue();
            key = apiKey2;
            count6++;
          }
          if (Thread.currentThread().getName().equals("7")) {
            if (count7 >= x) continue;
            userId = ((Integer) sub7.get(count7)).intValue();
            key = apiKey2;
            count7++;
          }
          if (Thread.currentThread().getName().equals("8")) {
            if (count8 >= x) continue;
            userId = ((Integer) sub8.get(count8)).intValue();
            key = apiKey2;
            count8++;
          }
          if (Thread.currentThread().getName().equals("9")) {
            if (count9 >= x) continue;
            userId = ((Integer) sub9.get(count9)).intValue();
            key = apiKey2;
            count9++;
          }
          if (Thread.currentThread().getName().equals("10")) {
            if (count10 >= al.size()) break;
            userId = ((Integer) sub10.get(count10)).intValue();
            key = apiKey2;
            count10++;
          }
          if (Thread.currentThread().getName().equals("11")) {
            if (count11 >= x) continue;
            userId = ((Integer) sub11.get(count11)).intValue();
            key = apiKey3;
            count11++;
          }
          if (Thread.currentThread().getName().equals("12")) {
            if (count12 >= x) continue;
            userId = ((Integer) sub12.get(count12)).intValue();
            key = apiKey3;
            count12++;
          }
          if (Thread.currentThread().getName().equals("13")) {
            if (count13 >= x) continue;
            userId = ((Integer) sub13.get(count13)).intValue();
            key = apiKey3;
            count13++;
          }
          if (Thread.currentThread().getName().equals("14")) {
            if (count14 >= x) continue;
            userId = ((Integer) sub14.get(count14)).intValue();
            key = apiKey3;
            count14++;
          }
          if (Thread.currentThread().getName().equals("15")) {
            if (count15 >= al.size()) break;
            userId = ((Integer) sub15.get(count15)).intValue();
            key = apiKey3;
            count15++;
          }
          Thread.sleep(2800);
          //   long starttime=System.currentTimeMillis();
          parsePersonalStats(userId, "personalstats,crimes,profile", key);
          //    System.out.println(System.currentTimeMillis()-starttime);
          //    parseCriminalRecord(userId, "crimes", apiKey);
          //    parseProfile(userId, "profile", apiKey);// "Update");
          if (userProcessed % 100 == 0) {
            //  if(apiHit%25==0){
            long currentTime = System.currentTimeMillis();
            long diff = currentTime - startTime;
            diff = diff / 1000;
            int day = (int) TimeUnit.SECONDS.toDays(diff);
            long hours = TimeUnit.SECONDS.toHours(diff) - (day * 24);
            long minute =
              TimeUnit.SECONDS.toMinutes(diff) -
              (TimeUnit.SECONDS.toHours(diff) * 60);
            long second =
              TimeUnit.SECONDS.toSeconds(diff) -
              (TimeUnit.SECONDS.toMinutes(diff) * 60);
            System.out.println(
              "...." +
                userProcessed +
                " users processed..." +
                activeProfiles +
                " active users found..." +
                "api hit = " +
                apiHit +
                "..." +
                day +
                " Days " +
                hours +
                " Hours " +
                minute +
                " minute " +
                second +
                " Seconds elapsed, user id =" +
                userId +
                " using key=" +
                key
            );
          }
        }
        System.out.println("No. of errored user = " + errored.size());
        Iterator it = errored.iterator();
        while (it.hasNext()) {
          System.out.println(it.next());
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    // TODO code application logic here
    try {
      Properties systemProperties = System.getProperties();
      systemProperties.setProperty("http.proxyHost", "www-proxy.us.oracle.com");
      systemProperties.setProperty("http.proxyPort", "80");
      Class.forName("oracle.jdbc.driver.OracleDriver");
      con = DriverManager.getConnection(
        "jdbc:oracle:thin:@indl72105.in.oracle.com:1522:in72105",
        "fusion",
        "fusion"
      );
      stmt = con.createStatement();

      //   updatePersonalStats();
      addNewPlayers();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void updatePersonalStats() throws Exception {
    Statement stmt1 = con.createStatement();
    ResultSet rs = stmt.executeQuery(
      "select user_id from TORN_PLAYERS where  user_id not in(select userid from torn_personal_stats)"
    );
    startTime = System.currentTimeMillis();
    while (rs.next()) {
      al.add(rs.getInt(1));
      if (al.size() % 100 == 0) System.out.println(
        "Added " +
          al.size() +
          " players in " +
          (System.currentTimeMillis() - startTime) / (1000) +
          " seconds"
      );
    }
    Collections.sort(al);
    x = al.size() / 15;
    sub1 = al.subList(0, x);
    sub2 = al.subList(x, x * 2);
    sub3 = al.subList(x * 2, x * 3);
    sub4 = al.subList(x * 3, x * 4);
    sub5 = al.subList(x * 4, x * 5);
    sub6 = al.subList(x * 5, x * 6);
    sub7 = al.subList(x * 6, x * 7);
    sub8 = al.subList(x * 7, x * 8);
    sub9 = al.subList(x * 8, x * 9);
    sub10 = al.subList(x * 9, x * 10);
    sub11 = al.subList(x * 10, x * 11);
    sub12 = al.subList(x * 11, x * 12);
    sub13 = al.subList(x * 12, x * 13);
    sub14 = al.subList(x * 13, x * 14);
    sub15 = al.subList(x * 14, al.size());
    Runnable r1 = new AddNewPlayers(1, 10, "Update");
    Runnable r2 = new AddNewPlayers(1984718, 1985000, "Update");
    Runnable r3 = new AddNewPlayers(1990583, 2000000, "Update");
    Runnable r4 = new AddNewPlayers(1975001, 1980000, "Update");
    Runnable r5 = new AddNewPlayers(1985001, 1990000, "Update");
    Runnable r6 = new AddNewPlayers(1, 10, "Update");
    Runnable r7 = new AddNewPlayers(1984718, 1985000, "Update");
    Runnable r8 = new AddNewPlayers(1990583, 2000000, "Update");
    Runnable r9 = new AddNewPlayers(1975001, 1980000, "Update");
    Runnable r10 = new AddNewPlayers(1985001, 1990000, "Update");
    Runnable r11 = new AddNewPlayers(1, 10, "Update");
    Runnable r12 = new AddNewPlayers(1984718, 1985000, "Update");
    Runnable r13 = new AddNewPlayers(1990583, 2000000, "Update");
    Runnable r14 = new AddNewPlayers(1975001, 1980000, "Update");
    Runnable r15 = new AddNewPlayers(1985001, 1990000, "Update");

    startTime = System.currentTimeMillis();
    Thread t1 = new Thread(r1);
    Thread t2 = new Thread(r2);
    Thread t3 = new Thread(r3);
    Thread t4 = new Thread(r4);
    Thread t5 = new Thread(r5);
    Thread t6 = new Thread(r6);
    Thread t7 = new Thread(r7);
    Thread t8 = new Thread(r8);
    Thread t9 = new Thread(r9);
    Thread t10 = new Thread(r10);
    Thread t11 = new Thread(r11);
    Thread t12 = new Thread(r12);
    Thread t13 = new Thread(r13);
    Thread t14 = new Thread(r14);
    Thread t15 = new Thread(r15);

    t1.setName("1");
    t2.setName("2");
    t3.setName("3");
    t4.setName("4");
    t5.setName("5");
    t6.setName("6");
    t7.setName("7");
    t8.setName("8");
    t9.setName("9");
    t10.setName("10");
    t11.setName("11");
    t12.setName("12");
    t13.setName("13");
    t14.setName("14");
    t15.setName("15");

    t1.start();
    t2.start();
    t3.start();
    t4.start();
    t5.start();
    t6.start();
    t7.start();
    t8.start();
    t9.start();
    t10.start();
    t11.start();
    t12.start();
    t13.start();
    t14.start();
    t15.start();
  }

  public static void parsePersonalStats(
    int userId,
    String selection,
    String apiKey
  ) throws Exception {
    String u =
      "http://api.torn.com/user/" +
      userId +
      "?selections=" +
      selection +
      "&key=" +
      apiKey;
    synchronized (u) {
      apiHit++;
      activeProfiles++;
      userProcessed++;
    }
    long starttime = System.currentTimeMillis();
    URL url = new URL(u);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");
    conn.setRequestProperty("Accept", "application/json");
    if (conn.getResponseCode() != 200) {
      System.out.println(
        "Failed : HTTP error code : " + conn.getResponseCode()
      );
      return;
    }

    BufferedReader br = new BufferedReader(
      new InputStreamReader((conn.getInputStream()))
    );

    //System.out.println("Output from Server .... \n");
    String output = br.readLine();

    //   System.out.println(output);
    if (output.contains("Too many requests")) {
      System.out.println(
        "++++++++++++++++++++++++++++++++Too many requests...sleeping for 15 minutes"
      );
      Thread.sleep(1000 * 60 * 15);
    }
    JSONParser jsonParser = new JSONParser();
    JSONObject personalStats = (JSONObject) jsonParser.parse(output);
    personalStats = (JSONObject) personalStats.get("personalstats");
    if (personalStats == null) {
      System.out.println("**************************NPE for userid " + userId);
      System.out.println("**************************output =  " + output);
      return;
    }
    Set s = personalStats.keySet();
    Iterator i = s.iterator();
    //                // take each value from the json array separately
    //            while (i.hasNext()) {
    //                String key = (String)i.next();
    //               // System.out.println(key + " = " + personalStats.get(key));
    //            }
    //            i = s.iterator();
    String query = "insert into torn_personal_stats(userid,";
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
    stmt.executeUpdate(query);
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
    query = "update torn_personal_stats set ";
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
    stmt.executeUpdate(query);
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
    ArrayList str = new ArrayList();
    str.add("role");
    str.add("gender");
    str.add("property");
    str.add("name");
    str.add("rank");

    i = s.iterator();
    //String query = "insert into torn_personal_stats(userid,";
    query = "update torn_personal_stats set ";
    while (i.hasNext()) {
      String key = (String) i.next();
      if ("level".equals(key)) key = "lvl";
      if (a.contains(key)) continue;
      query += key + "=";
      if (str.contains(key)) query += "'";
      if ("lvl".equals(key)) key = "level";
      query += profile.get(key);
      if (str.contains(key)) query += "'";
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

    stmt.executeUpdate(query);
  }

  public static void parseCriminalRecord(
    int userId,
    String selection,
    String apiKey
  ) throws Exception {
    String u =
      "http://api.torn.com/user/" +
      userId +
      "?selections=" +
      selection +
      "&key=" +
      apiKey;
    synchronized (u) {
      apiHit++;
    }
    URL url = new URL(u);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");
    conn.setRequestProperty("Accept", "application/json");
    if (conn.getResponseCode() != 200) {
      throw new RuntimeException(
        "Failed : HTTP error code : " + conn.getResponseCode()
      );
    }
    BufferedReader br = new BufferedReader(
      new InputStreamReader((conn.getInputStream()))
    );
    String output;
    //System.out.println("Output from Server .... \n");
    output = br.readLine();
    //   System.out.println(output);
    JSONParser jsonParser = new JSONParser();
    JSONObject criminalRecord = (JSONObject) jsonParser.parse(output);
    criminalRecord = (JSONObject) criminalRecord.get("criminalrecord");
    Set s = criminalRecord.keySet();
    Iterator i = s.iterator();
    //                // take each value from the json array separately
    while (i.hasNext()) {
      String key = (String) i.next();
      //        System.out.println(key + " = " + criminalRecord.get(key));
    }
    i = s.iterator();
    String query = "update torn_personal_stats set ";
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
    stmt.executeUpdate(query);
    //
  }

  public static void parseProfile(int userId, String selection, String apiKey)
    throws Exception {
    String u =
      "http://api.torn.com/user/" +
      userId +
      "?selections=" +
      selection +
      "&key=" +
      apiKey;
    synchronized (u) {
      apiHit++;
    }
    URL url = new URL(u);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");
    conn.setRequestProperty("Accept", "application/json");
    if (conn.getResponseCode() != 200) {
      throw new RuntimeException(
        "Failed : HTTP error code : " + conn.getResponseCode()
      );
    }
    BufferedReader br = new BufferedReader(
      new InputStreamReader((conn.getInputStream()))
    );
    String output;
    //System.out.println("Output from Server .... \n");
    output = br.readLine();
    //   System.out.println(output);
    JSONParser jsonParser = new JSONParser();
    JSONObject profile = (JSONObject) jsonParser.parse(output);

    Set s = profile.keySet();
    Iterator i = s.iterator();
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
    ArrayList str = new ArrayList();
    str.add("role");
    str.add("gender");
    str.add("property");
    str.add("name");
    str.add("rank");
    i = s.iterator();
    //String query = "insert into torn_personal_stats(userid,";
    String query = "update torn_personal_stats set ";
    while (i.hasNext()) {
      String key = (String) i.next();
      if ("level".equals(key)) key = "lvl";
      if (a.contains(key)) continue;
      query += key + "=";
      if (str.contains(key)) query += "'";
      if ("lvl".equals(key)) key = "level";
      query += profile.get(key);
      if (str.contains(key)) query += "'";
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
    if (userId == 1470) System.out.println(query);
    stmt.executeUpdate(query);
    //
  }

  public static void addNewPlayers() {
    int userId = 0;
    try {
      ResultSet rs1 = stmt.executeQuery(
        "select max(user_id) from TORN_PLAYERS"
      );
      rs1.next();
      userId = rs1.getInt(1);

      while (true) {
        if (userId % 100 == 0) System.out.println(userId);
        userId++;
        String u = "http://www.torn.com/profiles.php?XID=" + userId;

        URL url = new URL(u);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        if (conn.getResponseCode() != 200) {
          System.out.println(
            "*******************************Sleeping for 10 mins... " + userId
          );
          Thread.sleep(600000);
          conn = (HttpURLConnection) url.openConnection();
          conn.setRequestMethod("GET");
        }
        synchronized (u) {
          userProcessed++;
        }
        BufferedReader br = new BufferedReader(
          new InputStreamReader((conn.getInputStream()))
        );
        StringBuilder input = new StringBuilder();

        String inputLine;
        while ((inputLine = br.readLine()) != null) input.append(inputLine);
        String output = input.toString();

        if (output.contains("User could not be found")) break;
        String status = "Okay";
        if (output.contains("Account marked for deletion")) status =
          "Account marked for deletion";
        output.replaceAll(" ", "");
        String[] spl = output.split(" ago ");
        if (spl.length < 2) continue;
        String[] spl1 = spl[spl.length - 2].split(" ");
        int size = spl1.length;

        int lastActionTime = Integer.parseInt(spl1[size - 2]);
        String lastActionUnit = spl1[size - 1];
        String lastAction = lastActionTime + " " + lastActionUnit + " ago";
        synchronized (u) {
          activeProfiles++;
        }
        /*    if (output.contains("ederal")) {
                        if (output.contains("ultiple")) {
                            multi.add(userId);
                        }
                    }*/
        String query =
          "insert into torn_players(user_id,last_action,last_action_time, last_action_unit, status, creation_date) values(" +
          userId +
          ",'" +
          lastAction +
          "'," +
          lastActionTime +
          ",'" +
          lastActionUnit +
          "','" +
          status +
          "',sysdate)";

        //  System.out.println(query);
        stmt.executeUpdate(query);

        //       // userId++;
        if (userId % 10000 == 0) conn.disconnect();
      }
    } catch (NumberFormatException ne) {
      System.out.println(
        "Number Format Exception******************************** for " + userId
      );
      //errored.add(userId);
    } catch (Exception e) {
      System.out.println(
        "Exception=========================================for " + userId
      );
      //errored.add(userId);
    }
  }

  /* public static ViewObject getVO(String name) {
        QuantityTreeAMImpl qtam = (QuantityTreeAMImpl)getQuantityTreeManager();
        return qtam.findViewObject(name);
    }*/

  /** Utility function - equivalent of database NVL function.
   * @param a
   * @param value
   * @return
   */
  public static Object nvl(Object a, Object value) {
    if (a == null) return value;
    else return a;
  }
}
