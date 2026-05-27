import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.net.ssl.HttpsURLConnection;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class TornGeneral {

  static String apiKey = "";
  static Connection con = null;
  static PreparedStatement stmt = null;
  static PreparedStatement stmt1 = null;
  static PreparedStatement stmt2 = null;
  static PreparedStatement stmt3 = null;
  static PreparedStatement stmt4 = null;
  static PreparedStatement stmt5 = null;
  static PreparedStatement playerUpdateStmt = null;
  static PreparedStatement personalStatsStmt = null;
  static Statement statement = null;
  static Statement statement1 = null;
  static ArrayList al = new ArrayList();
  static ArrayList updateCompanyList = new ArrayList();
  static ArrayList updateFactionList = new ArrayList();
  static ArrayList updateLastActionList = new ArrayList();
  static ArrayList insertOrUpdateList = new ArrayList();
  static ArrayList newIds = new ArrayList();
  static long startTime = 0;
  static long currentTime = 0;
  static int userProcessed = 0;
  static int maxUserId = 0;
  static int maxCompanyId = 0;
  static int maxFactionId = 0;
  static int scanAll = 0;
  static int noApiKeys = 0;
  static int choice = 0;
  static int input = 0;
  static String sql = "";
  static List sub1 = new ArrayList();
  static List sub2 = new ArrayList();
  static final String hostname =
    "bomvm53.bomsubnet1.fusionappsdbom1.oraclevcn.com";
  static final int port = 1530;
  static final String sid = "ems8009_FDB";

  public TornGeneral(String apiKey) {
    this.apiKey = apiKey;
  }

  public static void main(String[] args) {
    // TODO code application logic here
    try {
      Properties systemProperties = System.getProperties();
      systemProperties.setProperty(
        "https.proxyHost",
        "www-proxy.us.oracle.com"
      );
      systemProperties.setProperty("https.proxyPort", "80");
      Class.forName("oracle.jdbc.driver.OracleDriver");
      con = DriverManager.getConnection(
        "jdbc:oracle:thin:@" + hostname + ":" + port + "/" + sid,
        "fusion",
        "fusion"
      );
      statement = con.createStatement();
      statement1 = con.createStatement();

      checkApiKeys();

      try {
        System.out.println("******* What do you want to do *********");
        System.out.println("0. Add new data");
        System.out.println("1. Update data for active users");
        System.out.println("2. Update data for users updated in last n days");
        System.out.println("3. Update remaining user's data");
        System.out.println("4. Update specific type of company");
        System.out.println("5. Update my company data");
        System.out.println("6. Update my faction data");
        System.out.println("7. Update data for all users");
        System.out.println("8. Add missing players");
        System.out.println("9. Update all companies");
        System.out.println("10. Update all factions");
        System.out.println("11. Enter custom SQL");
        System.out.println("12. Calculate Drug Addiction");
        System.out.println("");
        System.out.println("Enter your choice: ");
        InputStreamReader in = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(in);
        choice = Integer.parseInt(br.readLine());
        if (choice == 4) {
          System.out.println("Enter company type: ");
          input = Integer.parseInt(br.readLine());
        }
        if (choice == 2) {
          System.out.println("Enter no. of days: ");
          input = Integer.parseInt(br.readLine());
        }
        if (choice == 11) {
          System.out.println("Enter SQL: ");
          sql = br.readLine();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }

      ResultSet rs = statement.executeQuery(
        "select count(*) from torn_apikeys where status = 'VALID'"
      );
      rs.next();
      noApiKeys = rs.getInt(1);
      System.out.println("No. of API keys: " + noApiKeys);
      ResultSet rs1 = statement.executeQuery(
        "select apikey from torn_apikeys where status = 'VALID'"
      );
      Long tname = 1L;
      if (choice != 12) {
        for (int i = 1; i <= noApiKeys; i++) {
          rs1.next();
          apiKey = rs1.getString(1);
          //	Runnable r1 = new TornGeneral(apiKey);
          updates t1 = new updates(choice, noApiKeys, apiKey, input, sql);
          t1.setName("" + i);
          System.out.println("Starting thread " + t1.getName());
          t1.start();
        }
      } else {
        updates t1 = new updates(choice, noApiKeys, apiKey, input, sql);
        t1.start();
      }
      rs1.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  static void checkApiKeys() throws Exception {
    PreparedStatement stmt1 = con.prepareStatement(
      "update torn_apikeys set status = ?, error = ? where apikey = ?"
    );
    PreparedStatement stmt2 = con.prepareStatement(
      "update torn_apikeys set status = ?, owner = ?, error = null where apikey = ?"
    );

    ResultSet rs = statement.executeQuery("select apikey from torn_apikeys");
    while (rs.next()) {
      String apiKey = rs.getString(1);
      URL url = new URL("https://api.torn.com/user/?selections=&key=" + apiKey);
      HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Accept", "application/json");
      conn.setRequestProperty(
        "User-Agent",
        "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11"
      );
      BufferedReader br = new BufferedReader(
        new InputStreamReader((conn.getInputStream()))
      );
      String output = br.readLine();
      JSONParser jp = new JSONParser();
      JSONObject jo = (JSONObject) jp.parse(output);
      JSONObject error = (JSONObject) jo.get("error");
      if (error != null) {
        String errorMsg = (String) error.get("error");
        stmt1.setString(1, "INVALID");
        stmt1.setString(2, errorMsg);
        stmt1.setString(3, apiKey);
        stmt1.execute();
      } else {
        String owner = (String) jo.get("name");
        stmt2.setString(1, "VALID");
        stmt2.setString(2, owner);
        stmt2.setString(3, apiKey);
        stmt2.execute();
      }
    }
  }
}

class updates extends Thread {

  String apiKey = "";
  static Connection con = null;
  PreparedStatement stmt = null;
  PreparedStatement stmt1 = null;
  PreparedStatement stmt2 = null;
  PreparedStatement stmt3 = null;
  PreparedStatement stmt4 = null;
  PreparedStatement stmt5 = null;
  PreparedStatement playerUpdateStmt = null;
  PreparedStatement personalStatsStmt = null;
  Statement statement = null;
  Statement statement1 = null;
  ArrayList al = new ArrayList();
  static ArrayList updateCompanyList = new ArrayList();
  static ArrayList updateFactionList = new ArrayList();
  static ArrayList updateLastActionList = new ArrayList();
  static ArrayList insertOrUpdateList = new ArrayList();
  static ArrayList newIds = new ArrayList();
  static long startTime = 0;
  static long currentTime = 0;
  static int userProcessed = 0;
  static int userId = 0;
  static int companyId = 0;
  static int factionId = 0;
  static int maxUserId = 0;
  static int maxCompanyId = 0;
  static int maxFactionId = 0;
  static int maxLastAction = 0;
  static int maxInsertUpdateId = 0;
  static int scanAll = 0;
  static int noApiKeys = 0;
  static int input = 0;
  static String sql = "";
  static List sub1 = new ArrayList();
  static List sub2 = new ArrayList();
  static final String hostname =
    "bomvm53.bomsubnet1.fusionappsdbom1.oraclevcn.com";
  static final int port = 1530;
  static final String sid = "ems8009_FDB";
  static int alSize = 0;
  static int x = 0;
  static int newUserSubSize = 0;
  static int newCompanySubSize = 0;
  static int newFactionSubSize = 0;
  static int updateLastActionSubSize = 0;
  static int insertUpdateSubSize = 0;
  int choice = 0;
  String connectionString =
    "jdbc:oracle:thin:@" + hostname + ":" + port + "/" + sid;

  updates(int choice, int noApiKeys, String apiKey, int input, String sql) {
    this.choice = choice;
    this.noApiKeys = noApiKeys;
    this.apiKey = apiKey;
    this.input = input;
    this.sql = sql;
  }

  public void run() {
    try {
      Properties systemProperties = System.getProperties();
      systemProperties.setProperty(
        "https.proxyHost",
        "www-proxy.us.oracle.com"
      );
      systemProperties.setProperty("https.proxyPort", "80");
      Class.forName("oracle.jdbc.driver.OracleDriver");
      con = DriverManager.getConnection(connectionString, "fusion", "fusion");
      statement = con.createStatement();
      statement1 = con.createStatement();

      switch (choice) {
        case 0:
          addNewPlayers();
          addCompanyDetails();
          addFactionDetails();
          break;
        case 1:
          updateCompanyDetails(0);
          updateFactionDetails();
          updateLastAction(0);
          insertOrUpdatePersonalStats(0, "main");
          break;
        case 2:
          updateCompanyDetails(0);
          updateFactionDetails();
          updateLastAction(input);
          insertOrUpdatePersonalStats(input, "main");
          break;
        case 3 /*scanAll = 1;
						if ("HCiishg1krSG4VDX".equals(apiKey)) {
							addCompanyDetails();
							updateCompanyDetails(0);
						} 
						else {
							addNewPlayers();
							addFactionDetails();
							updateFactionDetails();
						}
						updateLastAction(1);
						insertOrUpdatePersonalStats(1,"main");*/:
          break;
        case 4:
          updateCompanyDetails(input);
          break;
        case 5:
          input = 100236;
          updateCompanyDetails(input);
          break;
        case 6:
          while (true) {
            updateMyFaction();
            insertOrUpdatePersonalStats(1, "myFaction");
            Thread.sleep(1000 * 900);
          }
        //System.exit(0);
        case 7:
          refreshAllData();
          updateLastAction(0);
          insertOrUpdatePersonalStats(0, "main");
          break;
        case 8:
          addMissingPlayers();
          break;
        case 9:
          updateCompanyDetails(0);
          break;
        case 10:
          updateFactionDetails();
          break;
        case 11:
          updateLastAction(input);
          insertOrUpdatePersonalStats(input, "main");
          break;
        case 12:
          calculateDrugAddiction();
          break;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void addMissingPlayers() throws Exception {
    ArrayList al = new ArrayList();
    List sub = new ArrayList();
    int count = 0;
    int count1 = 0;
    ResultSet rs = statement.executeQuery(
      "select user_id from torn_missing_players "
    );
    //	rs.next();
    //	int userId = rs.getInt(1);
    startTime = System.currentTimeMillis();
    long ct = 0;
    long ct1 = 0;
    long ot = startTime;
    long ot1 = startTime;
    SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date date = null;
    String u = null;
    System.out.println("userid = " + userId);

    String query =
      "insert into torn_players(user_id,last_action,last_action_time, last_action_unit, status, creation_date) values(?,?,?,?,?,sysdate)";

    synchronized (TornGeneral.class) {
      System.out.println("Max userid = " + maxUserId);
      while (rs.next()) {
        int uid = rs.getInt(1);
        //System.out.println("uid = " + uid);
        if (uid > maxUserId) al.add(uid);
        if (al.size() > 0 && al.size() % 1000 == 0) System.out.println(
          "Added " +
            al.size() +
            " players in " +
            (System.currentTimeMillis() - startTime) / (1000) +
            " seconds"
        );
      }
      Collections.sort(al);
      System.out.println("al.size = " + al.size());
      if (x == 0) x = al.size() / noApiKeys;
      sub = al.subList(0, x);
      System.out.println(
        "Sub1 starting id = " +
          sub.get(0) +
          ", ending id = " +
          sub.get(sub.size() - 1)
      );
      maxUserId = (Integer) sub.get(sub.size() - 1);
    }
    rs.close();

    Iterator it = sub.iterator();

    PreparedStatement stmt1 = con.prepareStatement(query);
    while (it.hasNext()) {
      //loopCount++;
      int i = (Integer) it.next();
      if (i % 200 == 0) {
        date = new Date();
        System.out.println(
          "In Add new players, Processing user i= " +
            i +
            " count= " +
            (count + count1) +
            " in " +
            (System.currentTimeMillis() - startTime) / (1000) +
            " seconds. Current time is " +
            dtf.format(date)
        );
      }

      String output = null;
      while (output == null) {
        u =
          "https://api.torn.com/user/" +
          i +
          "?selections=profile,personalstats,criminalrecord&key=" +
          apiKey;
        count++;
        // System.out.println(u);
        if (count % 65 == 0) {
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
        conn.setRequestProperty(
          "User-Agent",
          "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11"
        );

        if (conn.getResponseCode() != 200) {
          System.out.println(
            "Failed : HTTP error code : " + conn.getResponseCode()
          );
          //	i--;
          continue;
        }

        BufferedReader br = new BufferedReader(
          new InputStreamReader((conn.getInputStream()))
        );

        // System.out.println("Output from Server .... \n");
        output = br.readLine();
        if (output == null) Thread.sleep(1000);
      }
      if (output.contains("Incorrect ID")) continue;
      else if (output.contains("Too many requests")) {
        System.out.println("too many reqs with api " + apiKey);
        Thread.sleep(1000 * 60);
      } else if (!output.contains("role\":\"NPC")) {
        JSONParser jsonParser = new JSONParser();
        JSONObject JSONoutput = (JSONObject) jsonParser.parse(output);
        JSONObject lastAction = (JSONObject) JSONoutput.get("last_action");
        JSONObject criminalRecord = (JSONObject) JSONoutput.get(
          "criminalrecord"
        );
        JSONObject competition = (JSONObject) JSONoutput.get("competition");
        if (
          competition != null && competition.get("score") == null
        ) competition = null;
        al = getProfileStats(JSONoutput, criminalRecord, competition);
        if (lastAction == null) {
          i--;
          continue;
        }
        JSONObject statusObject = (JSONObject) JSONoutput.get("status");
        String action = (String) lastAction.get("relative");
        if ("No Last Action".equals(action)) continue;
        String status = (String) statusObject.get("description");
        String[] spl = action.split(" ago");
        // if (spl.length < 2)
        // continue;
        String[] spl1 = spl[0].split(" ");
        int size = spl1.length;

        int lastActionTime = Integer.parseInt(spl1[size - 2]);
        String lastActionUnit = spl1[size - 1];
        String lAction = lastActionTime + " " + lastActionUnit + " ago";

        stmt1.setInt(1, i);
        stmt1.setString(2, lAction);
        stmt1.setInt(3, lastActionTime);
        stmt1.setString(4, lastActionUnit);
        stmt1.setString(5, status);
        stmt1.addBatch();
        stmt1.clearParameters();
        if (i % 20 == 0) stmt1.executeBatch();
        // System.out.println(query);
        // stmt.executeUpdate(query);
        JSONObject personalStats = (JSONObject) JSONoutput.get("personalstats");
        String role = (String) JSONoutput.get("role");
        if (!role.contains("NPC")) {
          if (personalStats == null) continue;
          String statQuery = formQuery(personalStats, i, al);
          // stmt.executeUpdate(statQuery);
        }
      }
    }
    stmt1.executeBatch();
    stmt1.close();
  }

  public String replaceSpecialChars(String change) {
    if (change != null && change.contains("'")) {
      change = change.replaceAll("'", "''");
    }
    if (change != null && change.contains("&")) {
      change = change.replaceAll("&", "'||'&'||'");
    }
    return change;
  }

  public void updateAllUsers(String apiKey, int days) throws SQLException {
    ArrayList al = new ArrayList();
    int[] lastTime = new int[2855121];
    String[] lastUnit = new String[2855121];

    al.clear();

    ResultSet rs = null;
    int i = 0;
    int count1 = 0;
    try {
      int count = 0;

      int loopCount = 0;
      String originalApiKey = apiKey;
      String query = "";

      rs = statement.executeQuery(
        "select torn_players.user_id, last_action_time, last_action_unit from TORN_PLAYERS  where creation_date < sysdate-" +
          days
      );

      startTime = System.currentTimeMillis();

      while (rs.next()) {
        int id = (Integer) rs.getInt(1);
        al.add(id);
        lastTime[id] = rs.getInt(2);
        lastUnit[id] = rs.getString(3);
        if (al.size() % 10000 == 0) System.out.println(
          "Added " +
            al.size() +
            " players in " +
            (System.currentTimeMillis() - startTime) / (1000) +
            " seconds"
        );
      }
      rs.close();
      Collections.sort(al);
      synchronized (TornGeneral.class) {
        int x = al.size() / noApiKeys;
        sub1 = al.subList(0, x);
        System.out.println(
          "Sub1 starting id = " +
            sub1.get(0) +
            ", ending id = " +
            sub1.get(sub1.size() - 1)
        );
        al.subList(0, x).clear();
      }

      System.out.println("sub1 size = " + sub1.size());
      Iterator it = sub1.iterator();

      long ct = 0;
      long ct1 = 0;
      long ot = startTime;
      long ot1 = startTime;
      SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      Date date = null;
      String u = null;
      String playerUpdateQuery =
        "update torn_players set last_action = ?, last_action_time =?, LAST_ACTION_UNIT = ?" +
        ", CREATION_DATE = sysdate, status = ? where user_id = ?";

      while (it.hasNext()) {
        loopCount++;
        i = (Integer) it.next();
        if (
          playerUpdateStmt == null || playerUpdateStmt.isClosed()
        ) playerUpdateStmt = con.prepareStatement(playerUpdateQuery);
        if (i % 200 == 0) {
          date = new Date();
          System.out.println(
            "In update all users, Processing user i= " +
              i +
              " count= " +
              (count) +
              "  in " +
              (System.currentTimeMillis() - startTime) / (1000) +
              " seconds. Current time is " +
              dtf.format(date)
          );
        }

        String output = null;
        while (output == null) {
          u =
            "https://api.torn.com/user/" +
            i +
            "?selections=profile,personalstats,criminalrecord&key=" +
            apiKey;
          count++;
          // System.out.println(u);
          if (count % 75 == 0) {
            ct = (System.currentTimeMillis() - ot) / (1000);
            System.out.println("count = " + count + " ct = " + ct);
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
          conn.setRequestProperty(
            "User-Agent",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11"
          );

          if (conn.getResponseCode() != 200) {
            System.out.println(
              "Failed : HTTP error code : " + conn.getResponseCode()
            );
            i--;
            continue;
          }

          BufferedReader br = new BufferedReader(
            new InputStreamReader((conn.getInputStream()))
          );

          // System.out.println("Output from Server .... \n");
          output = br.readLine();
          if (output == null) Thread.sleep(1000);
        }
        if (output.contains("Incorrect ID")) continue;
        else if (output.contains("Too many requests")) {
          System.out.println("too many reqs with api " + apiKey);
          Thread.sleep(1000 * 60);
        } else {
          JSONParser jsonParser = new JSONParser();
          JSONObject JSONoutput = (JSONObject) jsonParser.parse(output);
          JSONObject personalStats = (JSONObject) JSONoutput.get(
            "personalstats"
          );
          JSONObject criminalRecord = (JSONObject) JSONoutput.get(
            "criminalrecord"
          );
          JSONObject competition = (JSONObject) JSONoutput.get("competition");
          if (
            competition != null && competition.get("score") == null
          ) competition = null;
          al = getProfileStats(JSONoutput, criminalRecord, competition);
          String role = (String) JSONoutput.get("role");
          if (role != null && role.contains("NPC")) continue;
          JSONObject lastAction = (JSONObject) JSONoutput.get("last_action");
          if (lastAction == null) {
            // i--;
            continue;
          }
          JSONObject statusObject = (JSONObject) JSONoutput.get("status");
          String action = (String) lastAction.get("relative");
          if ("No Last Action".equals(action)) continue;
          String time[] = action.split(" ");
          String description = (String) statusObject.get("description");
          //System.out.println(" User id: "+i+" Last time: "+lastTime[i]+" Last unit: "+lastUnit[i]+
          //		" Current time: "+time[0]+" Current Unit: "+time[1]);
          if (
            "days".equals(time[1]) &&
            (lastUnit[i]).equals("days") &&
            (Integer.parseInt(time[0]) < lastTime[i])
          ) {
            count1++;
          }
          executeLastAction(lastAction, i, description);
          //System.out.println("loopCount = "+loopCount);
          if (loopCount % 50 == 0) {
            //	System.out.println("Commiting...");
            playerUpdateStmt.executeBatch();
            playerUpdateStmt.close();
          }
          if (!role.contains("NPC")) {
            if (personalStats == null) continue;
            String statQuery = formQuery(personalStats, i, al);
            //stmt1.executeUpdate(statQuery);
          }
        }
      }
      playerUpdateStmt.executeBatch();
      playerUpdateStmt.close();
    } catch (Exception e) {
      System.out.println("UpdateAllUsers For user: " + i);
      e.printStackTrace();
    } finally {
      if (rs != null) rs.close();
    }
    System.out.println("No. of returning players: " + count1);
  }

  public void insertOrUpdatePersonalStats(int scanAll, String from)
    throws SQLException {
    int days = scanAll;
    String query =
      "(select user_id from TORN_PLAYERS where creation_date < SYSDATE - 2  and not (LAST_ACTION_UNIT = 'days' and LAST_ACTION_TIME>" +
      days +
      ") )";
    if (scanAll == 0) days = 120;

    ArrayList al = new ArrayList();
    List sub = new ArrayList();
    al.clear();
    ResultSet rs = null;
    int i = 0;
    try {
      int count = 0;
      int count1 = 0;
      int loopCount = 0;
      // ResultSet rs = stmt.executeQuery("select user_id from TORN_PLAYERS");
      query = "";
      if ("main".equals(from)) query =
        "select user_id from TORN_PLAYERS tp, torn_player_stats tps where not (LAST_ACTION_UNIT = 'days' and LAST_ACTION_TIME>" +
        days +
        ") and tp.user_id = tps.userid and tps.creationdate < sysdate-3";
      //query = "select torn_players.user_id from TORN_PLAYERS where not (LAST_ACTION_UNIT = 'days' and LAST_ACTION_TIME>17)";
      else query =
        "select user_id from torn_faction_members where faction_id = 8085";

      if (!"".equals(sql)) query = sql;

      rs = statement.executeQuery(query);

      startTime = System.currentTimeMillis();

      synchronized (TornGeneral.class) {
        while (rs.next()) {
          int uid = rs.getInt(1);
          if (uid > maxInsertUpdateId) al.add(uid);
          if (al.size() > 0 && al.size() % 1000 == 0) System.out.println(
            "Added " +
              al.size() +
              " players in " +
              (System.currentTimeMillis() - startTime) / (1000) +
              " seconds"
          );
        }
        Collections.sort(al);
        if (insertUpdateSubSize == 0) insertUpdateSubSize =
          al.size() / noApiKeys;
        try {
          sub = al.subList(0, Math.min(insertUpdateSubSize, al.size()));
        } catch (Exception e) {
          e.printStackTrace();
          System.out.println("insertUpdateSubSize = " + insertUpdateSubSize);
          System.out.println("al.size() = " + al.size());
          System.out.println("noApiKeys = " + noApiKeys);
        } finally {
          System.out.println("In finally");
          System.out.println("insertUpdateSubSize = " + insertUpdateSubSize);
          System.out.println("al.size() = " + al.size());
          System.out.println("noApiKeys = " + noApiKeys);
        }
        System.out.println(
          "Sub1 starting id = " +
            sub.get(0) +
            ", ending id = " +
            sub.get(sub.size() - 1)
        );
        maxInsertUpdateId = (Integer) sub.get(sub.size() - 1);
      }
      rs.close();

      Iterator it = sub.iterator();

      long ct = 0;
      long ot = startTime;
      long ct1 = 0;
      long ot1 = startTime;
      SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      Date date = null;
      String u = null;
      String originalApiKey = apiKey;

      String playerUpdateQuery =
        "update torn_players set last_action = ?, last_action_time =?, LAST_ACTION_UNIT = ?" +
        ", CREATION_DATE = sysdate, status = ? where user_id = ?";
      playerUpdateStmt = con.prepareStatement(playerUpdateQuery);

      while (it.hasNext()) {
        loopCount++;
        i = (Integer) it.next();
        if (count % 200 == 0) {
          date = new Date();
          System.out.println(
            "In update players stats, Processing user i= " +
              i +
              " count= " +
              (count + count1) +
              " in " +
              (System.currentTimeMillis() - startTime) / (1000) +
              " seconds. Current time is " +
              dtf.format(date)
          );
        }

        String output = null;
        while (output == null) {
          u =
            "https://api.torn.com/user/" +
            i +
            "?selections=profile,personalstats,criminalrecord&key=" +
            apiKey;
          count++;

          if (count % 75 == 0) {
            ct = (System.currentTimeMillis() - ot) / (1000);
            // System.out.println("count = "+count+" ct = "+ct);
            if (ct < 60) {
              //	System.out.println("Sleeping for " + (60 - ct) + " seconds");
              Thread.sleep((60 - ct) * 1000);
            }
            ot = System.currentTimeMillis();
          }

          long starttime = System.currentTimeMillis();
          URL url = new URL(u);

          HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
          conn.setRequestMethod("GET");
          conn.setRequestProperty("Accept", "application/json");
          conn.setRequestProperty(
            "User-Agent",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11"
          );

          if (conn.getResponseCode() != 200) {
            System.out.println(
              "Failed : HTTP error code : " + conn.getResponseCode()
            );
            i--;
            continue;
          }

          BufferedReader br = new BufferedReader(
            new InputStreamReader((conn.getInputStream()))
          );

          // System.out.println("Output from Server .... \n");
          output = br.readLine();
          if (output == null) Thread.sleep(1000);
          // System.out.println(output);
        }
        if (output.contains("Incorrect ID")) continue;
        else if (output.contains("Too many requests")) {
          System.out.println("too many reqs with api " + apiKey);
          Thread.sleep(1000 * 60);
        } else {
          JSONParser jsonParser = new JSONParser();
          JSONObject JSONoutput = (JSONObject) jsonParser.parse(output);
          JSONObject lastAction = (JSONObject) JSONoutput.get("last_action");
          JSONObject personalStats = (JSONObject) JSONoutput.get(
            "personalstats"
          );
          JSONObject criminalRecord = (JSONObject) JSONoutput.get(
            "criminalrecord"
          );
          JSONObject competition = (JSONObject) JSONoutput.get("competition");
          if (
            competition != null && competition.get("score") == null
          ) competition = null;
          al = getProfileStats(JSONoutput, criminalRecord, competition);
          String role = (String) JSONoutput.get("role");
          if (role != null && !role.contains("NPC")) {
            if (personalStats == null) continue;
            String statQuery = formQuery(personalStats, i, al);
            // System.out.println(statQuery);
            JSONObject statusObject = (JSONObject) JSONoutput.get("status");
            String action = (String) lastAction.get("relative");
            if ("No Last Action".equals(action)) continue;
            String status = (String) statusObject.get("description");

            executeLastAction(lastAction, i, status);
            if (loopCount % 50 == 0) playerUpdateStmt.executeBatch();
          }
        }
      }
      playerUpdateStmt.executeBatch();
      playerUpdateStmt.close();
    } catch (Exception e) {
      System.out.println("insertOrUpdatePersonalStats For user: " + i);
      e.printStackTrace();
    } finally {
      if (rs != null) rs.close();
    }
  }

  public void updateCompanyDetails(int type) throws Exception {
    ArrayList al = new ArrayList();
    int flag = 0;
    ResultSet rs = null;
    List sub = new ArrayList();
    PreparedStatement stmt4 = null;
    al.clear();
    int count = 0;
    int count1 = 0;
    int companyId = 0;
    int loopCount = 0;
    String query = "";
    if (type == 0) query = "select id from TORN_COMPANIES";
    else if (type < 100) query =
      "select id from TORN_COMPANIES where company_type=" + type;
    else query = "select id from TORN_COMPANIES where id=" + type;
    rs = statement.executeQuery(query);
    synchronized (TornGeneral.class) {
      while (rs.next()) {
        int uid = rs.getInt(1);

        if (uid > maxCompanyId) al.add(uid);
        if (al.size() > 0 && al.size() % 1000 == 0) System.out.println(
          "Added " +
            al.size() +
            " players in " +
            (System.currentTimeMillis() - startTime) / (1000) +
            " seconds"
        );
      }

      Collections.sort(al);
      if (newCompanySubSize == 0) newCompanySubSize = al.size() / noApiKeys;
      sub = al.subList(0, newCompanySubSize);
      System.out.println(
        "Sub1 starting id = " +
          sub.get(0) +
          ", ending id = " +
          sub.get(sub.size() - 1)
      );
      maxCompanyId = (Integer) sub.get(sub.size() - 1);
    }
    rs.close();
    Iterator it = sub.iterator();
    startTime = System.currentTimeMillis();

    long ct = 0;
    long ct1 = 0;
    long ot = startTime;
    long ot1 = startTime;
    SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date date = null;
    String u = null;
    // for (int i = userId + 1; i < 2477321; i++) {
    // System.out.println("companyid = " + companyId);
    String companyEmployeesQuery = "";
    String playerUpdateQuery = "";
    query = "";
    query =
      "update torn_companies set creation_date = sysdate,COMPANY_TYPE=?,WEEKLY_INCOME=?" +
      ",DIRECTOR=?,RATING=?,WEEKLY_CUSTOMERS=?,DAILY_INCOME=?,EMPLOYEES_CAPACITY=?," +
      "DAYS_OLD=?,NAME=?,EMPLOYEES_HIRED=?,DAILY_CUSTOMERS=? where id =?";
    stmt1 = con.prepareStatement(query);
    stmt2 = con.prepareStatement(
      "delete torn_company_employees where company_id=?"
    );
    playerUpdateQuery =
      "update torn_players set last_action = ?, last_action_time =?, LAST_ACTION_UNIT = ?" +
      ", CREATION_DATE = sysdate, status = ? where user_id = ?";
    stmt4 = con.prepareStatement("delete torn_companies where id=?");
    stmt5 = con.prepareStatement(
      "delete torn_company_employees where company_id=?"
    );
    playerUpdateStmt = con.prepareStatement(playerUpdateQuery);
    int i = 0;
    try {
      // for (int i = 4209; i < 4210; i++) {//83301
      while (it.hasNext()) {
        loopCount++;
        i = (Integer) it.next();
        // System.out.println(i);
        if (i % 20 == 0) {
          date = new Date();
          System.out.println(
            "In update company details, Processing company i= " +
              i +
              " count= " +
              (count + count1) +
              " in " +
              (System.currentTimeMillis() - startTime) / (1000) +
              " seconds. Current time is " +
              dtf.format(date)
          );
        }

        String output = null;
        while (output == null) {
          u =
            "https://api.torn.com/company/" +
            i +
            "?selections=profile&key=" +
            apiKey;
          count++;
          if (count % 65 == 0) {
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
          conn.setRequestProperty(
            "User-Agent",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11"
          );

          if (conn.getResponseCode() != 200) {
            System.out.println(
              "Failed : HTTP error code : " + conn.getResponseCode()
            );
            // i--;
            // continue;
          }

          BufferedReader br = new BufferedReader(
            new InputStreamReader((conn.getInputStream()))
          );

          // System.out.println("Output from Server .... \n");
          output = br.readLine();
          if (output == null) {
            System.out.println("Output is null");
            Thread.sleep(1000);
          }
        }
        if (output.contains("Incorrect ID")) {
          System.out.println("Output is Incorrect ID");
          stmt4.setLong(1, i);
          stmt4.executeUpdate();

          stmt5.setLong(1, i);
          stmt5.executeUpdate();
          continue;
        } else if (output.contains("Too many requests")) {
          System.out.println("too many reqs with api " + apiKey);
          Thread.sleep(1000 * 60);
        } else {
          // count++;
          JSONParser jsonParser = new JSONParser();
          JSONObject JSONoutput = (JSONObject) jsonParser.parse(output);
          JSONObject company = (JSONObject) JSONoutput.get("company");
          if (company == null) {
            System.out.println(
              "Company is null, company id: " + i + " url: " + u
            );
            continue;
          }
          JSONObject employees = (JSONObject) company.get("employees");
          if (employees == null) {
            System.out.println("Employees are null");
            continue;
          }
          Set s = company.keySet();
          Iterator j = s.iterator();

          stmt1.setLong(1, (Long) company.get("company_type"));
          stmt1.setLong(2, (Long) company.get("weekly_income"));
          stmt1.setLong(3, (Long) company.get("director"));
          stmt1.setLong(4, (Long) company.get("rating"));
          stmt1.setLong(5, (Long) company.get("weekly_customers"));
          stmt1.setLong(6, (Long) company.get("daily_income"));
          stmt1.setLong(7, (Long) company.get("employees_capacity"));
          stmt1.setLong(8, (Long) company.get("days_old"));
          stmt1.setString(9, (String) company.get("name"));
          stmt1.setLong(10, (Long) company.get("employees_hired"));
          stmt1.setLong(11, (Long) company.get("daily_customers"));

          stmt1.setInt(12, i);
          // System.out.println(rs.getInt(1));
          stmt1.addBatch();
          stmt1.clearParameters();
          if (loopCount % 20 == 0) {
            stmt1.executeBatch();
          }
          // System.out.println(rs.getInt(1));

          //					stmt1 = con.prepareStatement("delete torn_company_employees where company_id=?");
          stmt2.setInt(1, i);
          stmt2.executeUpdate();
          // System.out.println(rs.getInt(1));
          int id = 0;
          String name = "";
          String position = "";
          long companyDays = 0;
          Set e = employees.entrySet();
          Iterator ei = e.iterator();
          // System.out.println("set size ="+e.size());
          // System.out.println(rs.getInt(1));
          companyEmployeesQuery =
            "insert into torn_company_employees values(?,?,?,?,?,sysdate)"; // + i + ","
          // + id
          // + ",'"
          // + name + "','" + position + "'," + companyDays + ")";

          stmt3 = con.prepareStatement(companyEmployeesQuery);
          while (ei.hasNext()) {
            Map.Entry map = (Map.Entry) ei.next();
            // System.out.println(map.getKey());// +" = "+map.getValue());
            id = Integer.parseInt((String) map.getKey());
            // System.out.println(id);
            JSONObject empDetails = (JSONObject) employees.get(map.getKey());
            JSONObject lastAction = (JSONObject) empDetails.get("last_action");
            JSONObject status = (JSONObject) empDetails.get("status");

            // String relative = (String) lastAction.get("relative");
            String description = (String) status.get("description");

            name = (String) empDetails.get("name");
            name = replaceSpecialChars(name);
            position = (String) empDetails.get("position");
            companyDays = (Long) empDetails.get("days_in_company");
            stmt3.setInt(1, i);
            stmt3.setInt(2, id);
            stmt3.setString(3, name);
            stmt3.setString(4, position);
            stmt3.setLong(5, companyDays);
            stmt3.addBatch();
            stmt3.clearParameters();
            executeLastAction(lastAction, id, description);
          }
          stmt3.executeBatch();
          playerUpdateStmt.executeBatch();
          stmt3.close();
        }

        // break;
      }
      //	stmt2.close();
      //	stmt4.close();
      stmt1.executeBatch();
      playerUpdateStmt.close();
      updateLastAction(2);
    } catch (Exception e) {
      System.out.println(companyEmployeesQuery);
      System.out.println(playerUpdateQuery);
      System.out.println(query);
      System.out.println("Company Id = " + i);
      e.printStackTrace();
    } finally {
      stmt1.close();
    }
  }

  public void addCompanyDetails() throws Exception {
    ArrayList al = new ArrayList();
    List sub = new ArrayList();
    al.clear();
    maxCompanyId = 124000;
    Thread.sleep(15 * 1000);
    int count = 0;
    int count1 = 0;

    if (companyId == 0) {
      ResultSet rs = statement.executeQuery(
        "select max(ID) from TORN_COMPANIES"
      );
      rs.next();
      companyId = rs.getInt(1);
    }
    startTime = System.currentTimeMillis();

    long ct = 0;
    long ct1 = 0;
    long ot = startTime;
    long ot1 = startTime;
    SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date date = null;
    String u = null;
    // for (int i = userId + 1; i < 2477321; i++) {
    System.out.println("companyid = " + companyId);
    String companyEmployeesQuery = "";

    String query = "";
    String playerUpdateQuery =
      "update torn_players set last_action = ?, last_action_time =?, LAST_ACTION_UNIT = ?" +
      ", CREATION_DATE = sysdate, status = ? where user_id = ?";
    playerUpdateStmt = con.prepareStatement(playerUpdateQuery);

    synchronized (TornGeneral.class) {
      for (int i = companyId + 1; i < maxCompanyId; i++) al.add(i);
      if (alSize == 0) alSize = al.size();
      if (newCompanySubSize == 0) newCompanySubSize = al.size() / noApiKeys;
      sub = al.subList(0, newCompanySubSize);

      companyId = (Integer) sub.get(sub.size() - 1);
      System.out.println("companId = " + companyId);
    }
    // maxCompanyId = companyId+1;
    try {
      System.out.println(
        "Thread " +
          Thread.currentThread().getName() +
          " Sub starting id = " +
          sub.get(0) +
          ", ending id = " +
          sub.get(sub.size() - 1)
      );
      Iterator it = sub.iterator();
      while (it.hasNext()) {
        int i = (Integer) it.next(); // 83301
        if (i % 20 == 0) {
          date = new Date();
          System.out.println(
            "In Add company details, Processing company i= " +
              i +
              " count= " +
              (count + count1) +
              " in " +
              (System.currentTimeMillis() - startTime) / (1000) +
              " seconds. Current time is " +
              dtf.format(date)
          );
        }
        String output = null;
        while (output == null) {
          u =
            "https://api.torn.com/company/" +
            i +
            "?selections=profile&key=" +
            apiKey;
          count++;
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
          conn.setRequestProperty(
            "User-Agent",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11"
          );

          if (conn.getResponseCode() != 200) {
            System.out.println(
              "Failed : HTTP error code : " + conn.getResponseCode()
            );
            i--;
            continue;
          }

          BufferedReader br = new BufferedReader(
            new InputStreamReader((conn.getInputStream()))
          );

          // System.out.println("Output from Server .... \n");
          output = br.readLine();
          if (output == null) Thread.sleep(1000);
        }
        if (output.contains("Incorrect ID")) continue;
        else if (output.contains("Too many requests")) {
          System.out.println("too many reqs with api " + apiKey);
          Thread.sleep(1000 * 60);
        } else {
          // count++;
          JSONParser jsonParser = new JSONParser();
          JSONObject JSONoutput = (JSONObject) jsonParser.parse(output);
          JSONObject company = (JSONObject) JSONoutput.get("company");
          JSONObject employees = (JSONObject) company.get("employees");
          if (employees == null) continue;
          Set s = company.keySet();
          Iterator j = s.iterator();

          while (j.hasNext()) {
            String key = (String) j.next();
            // System.out.println(key + " = "+ company.get(key));
          }
          j = s.iterator();
          query = "insert into torn_companies(creation_date,";
          while (j.hasNext()) {
            String key = (String) j.next();
            if ("employees".equals(key)) {
              if (!j.hasNext()) query = query.substring(0, query.length() - 1);
              continue;
            }
            query += key;
            if (j.hasNext()) {
              query += ",";
            }
          }
          query += ") values (sysdate,?,?,?,?,?,?,?,?,?,?,?,?)";
          PreparedStatement stmt1 = con.prepareStatement(query);
          j = s.iterator();
          int k = 1;
          while (j.hasNext()) {
            String key = (String) j.next();
            if ("employees".equals(key)) {
              if (!j.hasNext()) query = query.substring(0, query.length() - 1);
              continue;
            }
            if ("name".equals(key)) {
              // query += "\'";
              String name = replaceSpecialChars((String) company.get(key));
              stmt1.setString(k, name);
              k++;
              // query += name;
              // query += "\'";
            } else {
              // System.out.println("k="+k+" key = "+key);
              stmt1.setLong(k, (Long) company.get(key));
              k++;
            }

            //						if (j.hasNext()) {
            //							query += ",";
            //						}
          }
          // query += ")";
          stmt1.executeUpdate();
          stmt1.close();
          int id = 0;
          String name = "";
          String position = "";
          long companyDays = 0;
          Set e = employees.entrySet();
          Iterator ei = e.iterator();
          // System.out.println("set size ="+e.size());
          companyEmployeesQuery =
            "insert into torn_company_employees values(?,?,?,?,?,sysdate)"; // + i + ","
          // + id
          // + ",'"
          // + name + "','" + position + "'," + companyDays + ")";

          stmt1 = con.prepareStatement(companyEmployeesQuery);
          while (ei.hasNext()) {
            Map.Entry map = (Map.Entry) ei.next();
            // System.out.println(map.getKey());// +" = "+map.getValue());
            id = Integer.parseInt((String) map.getKey());
            // System.out.println(id);
            JSONObject empDetails = (JSONObject) employees.get(map.getKey());
            JSONObject lastAction = (JSONObject) empDetails.get("last_action");
            JSONObject status = (JSONObject) empDetails.get("status");

            // String relative = (String) lastAction.get("relative");
            String description = (String) status.get("description");

            name = (String) empDetails.get("name");
            name = replaceSpecialChars(name);
            position = (String) empDetails.get("position");
            companyDays = (Long) empDetails.get("days_in_company");

            stmt1.setInt(1, i);
            stmt1.setInt(2, id);
            stmt1.setString(3, name);
            stmt1.setString(4, position);
            stmt1.setLong(5, companyDays);
            stmt1.addBatch();
            stmt1.clearParameters();
            executeLastAction(lastAction, id, description);
          }
          stmt1.executeBatch();
          playerUpdateStmt.executeBatch();
          stmt1.close();
          // playerUpdateStmt.close();
        }
      }

      playerUpdateStmt.close();
    } catch (SQLException e) {
      System.out.println(companyEmployeesQuery);
      System.out.println(playerUpdateQuery);
      System.out.println(query);
      e.printStackTrace();
    } finally {
      if (stmt1 != null && !stmt1.isClosed()) stmt1.close();
    }
  }

  public void addNewPlayers() throws Exception {
    ArrayList al = new ArrayList();
    List sub = new ArrayList();
    int count = 0;
    int count1 = 0;

    //int userId = 0;
    synchronized (TornGeneral.class) {
      ResultSet rs = statement.executeQuery(
        "select max(user_id) from TORN_PLAYERS"
      );
      rs.next();
      userId = rs.getInt(1);
    }
    startTime = System.currentTimeMillis();

    long ct = 0;
    long ct1 = 0;
    long ot = startTime;
    long ot1 = startTime;
    SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date date = null;
    String u = null;
    // for (int i = userId + 1; i < 2477321; i++) {
    System.out.println("userid = " + userId);
    // maxUserId = userId+1;
    String query =
      "insert into torn_players(user_id,last_action,last_action_time, last_action_unit, status, creation_date) values(?,?,?,?,?,sysdate)";
    u = "https://api.torn.com/torn/?selections=stats&key=wr0oZF1rPax0mIMn";
    URL url = new URL(u);

    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
    conn.setRequestMethod("GET");
    conn.setRequestProperty("Accept", "application/json");
    conn.setRequestProperty(
      "User-Agent",
      "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11"
    );

    if (conn.getResponseCode() != 200) {
      System.out.println(
        "Failed : HTTP error code : " + conn.getResponseCode()
      );
      //i--;
      //continue;
    }

    BufferedReader br = new BufferedReader(
      new InputStreamReader((conn.getInputStream()))
    );

    // System.out.println("Output from Server .... \n");
    String output = br.readLine();
    JSONParser jp = new JSONParser();
    JSONObject jo = (JSONObject) jp.parse(output);
    JSONObject stats = (JSONObject) jo.get("stats");
    Long maxUserId = (Long) stats.get("users_total");
    maxUserId = maxUserId + 1418288;
    System.out.println("Max userid = " + maxUserId);
    System.out.println("Total no. of new players = " + (maxUserId - userId));

    synchronized (TornGeneral.class) {
      for (int i = userId + 1; i < maxUserId; i++) al.add(i);
      if (alSize == 0) alSize = al.size();
      if (newUserSubSize == 0) newUserSubSize = al.size() / noApiKeys;
      if (newUserSubSize > 0) {
        sub = al.subList(0, newUserSubSize);
        userId = (Integer) sub.get(sub.size() - 1);
      }
    }
    if (sub.isEmpty()) return;
    PreparedStatement stmt1 = con.prepareStatement(query);
    //	Thread.sleep(10000);
    System.out.println(
      "Thread " +
        Thread.currentThread().getName() +
        " Sub starting id = " +
        sub.get(0) +
        ", ending id = " +
        sub.get(sub.size() - 1)
    );
    Iterator it = sub.iterator();
    while (it.hasNext()) {
      int i = (Integer) it.next();
      if (i % 200 == 0) {
        date = new Date();
        System.out.println(
          "In Add new players, Processing user i= " +
            i +
            " count= " +
            (count + count1) +
            " in " +
            (System.currentTimeMillis() - startTime) / (1000) +
            " seconds. Current time is " +
            dtf.format(date)
        );
      }

      output = null;
      while (output == null) {
        u =
          "https://api.torn.com/user/" +
          i +
          "?selections=profile,personalstats,criminalrecord&key=" +
          apiKey;
        count++;
        // System.out.println(u);
        if (count % 65 == 0) {
          ct = (System.currentTimeMillis() - ot) / (1000);
          // System.out.println("count = "+count+" ct = "+ct);
          if (ct < 60) {
            //	 System.out.println("Sleeping for " + (60 - ct) + " seconds");
            Thread.sleep((60 - ct) * 1000);
          }
          ot = System.currentTimeMillis();
        }

        long starttime = System.currentTimeMillis();
        url = new URL(u);

        conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty(
          "User-Agent",
          "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11"
        );

        if (conn.getResponseCode() != 200) {
          System.out.println(
            "Failed : HTTP error code : " + conn.getResponseCode()
          );
          //i--;
          continue;
        }

        br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        // System.out.println("Output from Server .... \n");
        output = br.readLine();
        if (output == null) Thread.sleep(1000);
      }
      if (output.contains("Incorrect ID")) continue;
      else if (output.contains("Too many requests")) {
        System.out.println("too many reqs with api " + apiKey);
        Thread.sleep(1000 * 60);
      } else if (!output.contains("NPC")) {
        JSONParser jsonParser = new JSONParser();
        JSONObject JSONoutput = (JSONObject) jsonParser.parse(output);
        JSONObject lastAction = (JSONObject) JSONoutput.get("last_action");
        JSONObject criminalRecord = (JSONObject) JSONoutput.get(
          "criminalrecord"
        );
        JSONObject competition = (JSONObject) JSONoutput.get("competition");
        if (
          competition != null && competition.get("score") == null
        ) competition = null;
        al = getProfileStats(JSONoutput, criminalRecord, competition);
        if (lastAction == null) {
          i--;
          continue;
        }
        JSONObject statusObject = (JSONObject) JSONoutput.get("status");
        String action = (String) lastAction.get("relative");
        if ("No Last Action".equals(action)) continue;
        String status = (String) statusObject.get("description");
        String[] spl = action.split(" ago");
        // if (spl.length < 2)
        // continue;
        String[] spl1 = spl[0].split(" ");
        int size = spl1.length;

        int lastActionTime = Integer.parseInt(spl1[size - 2]);
        String lastActionUnit = spl1[size - 1];
        String lAction = lastActionTime + " " + lastActionUnit + " ago";

        stmt1.setInt(1, i);
        stmt1.setString(2, lAction);
        stmt1.setInt(3, lastActionTime);
        stmt1.setString(4, lastActionUnit);
        stmt1.setString(5, status);
        stmt1.addBatch();
        stmt1.clearParameters();

        try {
          stmt1.executeBatch();
        } catch (Exception e) {
          e.printStackTrace();
          System.out.println("For userid: " + i);
        }

        // System.out.println(query);
        // stmt.executeUpdate(query);
        JSONObject personalStats = (JSONObject) JSONoutput.get("personalstats");
        String role = (String) JSONoutput.get("role");
        if (!role.contains("NPC")) {
          if (personalStats == null) continue;
          String statQuery = formQuery(personalStats, i, al);
          // stmt.executeUpdate(statQuery);
        }
      }
    }
    stmt1.executeBatch();
    stmt1.close();
    // con.close();
  }

  public String formQuery(JSONObject personalStats, int user_id, ArrayList al) {
    //System.out.println("al.size = "+al.size());
    PreparedStatement personalStatsStmt = null;
    String query = "";
    int c = 0;
    int count = 0;
    //ResultSet rs = null;
    try (
      Connection con = DriverManager.getConnection(
        connectionString,
        "fusion",
        "fusion"
      )
    ) {
      String sql = "SELECT COUNT(*) FROM torn_player_stats WHERE userid = ?";
      try (
        // use your DataSource
        PreparedStatement ps = con.prepareStatement(sql)
      ) {
        //Statement statement1 = con.createStatement();
        //rs = statement1.executeQuery("select count(*) from torn_player_stats where userid = " + user_id);
        ps.setInt(1, user_id);
        try (ResultSet rs = ps.executeQuery()) {
          if (rs.next()) count = rs.getInt(1);
          //rs.close();
          //statement1.close();

          if (count == 0) {
            Set keys = personalStats.keySet();
            Iterator j = keys.iterator();

            if (al.size() > 15) query =
              "insert into torn_player_stats(creationdate,userid,rank,USER_LEVEL,GENDER,PROPERTY,FRIENDS,ENEMIES,FORUM_POSTS,KARMA,AGE,ROLE,DONATOR,NAME,PROPERTY_ID,revivable,spouse_id,selling_illegal_products,theft_crime,auto_theft,drug_deals,computer_crimes,murder,fraud_crimes,other_crimes,total_crimes,competition,score,team,attacks,";
            else query =
              "insert into torn_player_stats(creationdate,userid,rank,USER_LEVEL,GENDER,PROPERTY,FRIENDS,ENEMIES,FORUM_POSTS,KARMA,AGE,ROLE,DONATOR,NAME,PROPERTY_ID,revivable,spouse_id,"; //selling_illegal_products,theft_crime,auto_theft,drug_deals,computer_crimes,murder,fraud_crimes,other_crimes,total_crimes,";
            while (j.hasNext()) {
              String key = (String) j.next();
              query += key;
              c++;
              if (j.hasNext()) {
                query += ",";
              }
            }
            if (al.size() > 15) query +=
              ") values (sysdate,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,";
            else query += ") values (sysdate,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"; //?,?,?,?,?,?,?,?,?,";
            for (int i = 0; i <= c; i++) {
              query += "?";
              if (!(i == c)) query += ",";
            }
            query += ")";

            personalStatsStmt = con.prepareStatement(query);
            personalStatsStmt.setInt(1, user_id);

            personalStatsStmt.setString(2, (String) al.get(0));
            personalStatsStmt.setLong(3, (Long) al.get(1));
            personalStatsStmt.setString(4, (String) al.get(2));
            personalStatsStmt.setString(5, (String) al.get(3));
            personalStatsStmt.setLong(6, (Long) al.get(4));
            personalStatsStmt.setLong(7, (Long) al.get(5));
            personalStatsStmt.setLong(8, (Long) al.get(6));
            personalStatsStmt.setLong(9, (Long) al.get(7));
            personalStatsStmt.setLong(10, (Long) al.get(8));
            personalStatsStmt.setString(11, (String) al.get(9));
            personalStatsStmt.setLong(12, (Long) al.get(10));

            personalStatsStmt.setString(13, (String) al.get(11));
            personalStatsStmt.setLong(14, (Long) al.get(12));
            personalStatsStmt.setLong(15, (Long) al.get(13));
            personalStatsStmt.setLong(16, (Long) al.get(14));
            if (al.size() > 15) {
              personalStatsStmt.setLong(17, (Long) al.get(15));

              personalStatsStmt.setLong(18, (Long) al.get(16));
              personalStatsStmt.setLong(19, (Long) al.get(17));
              personalStatsStmt.setLong(20, (Long) al.get(18));
              personalStatsStmt.setLong(21, (Long) al.get(19));
              personalStatsStmt.setLong(22, (Long) al.get(20));
              personalStatsStmt.setLong(23, (Long) al.get(21));
              personalStatsStmt.setLong(24, (Long) al.get(22));
              personalStatsStmt.setLong(25, (Long) al.get(23));
            }
            if (al.size() > 24) {
              //					personalStatsStmt.setString(24, (String) al.get(22));
              //					personalStatsStmt.setLong(25, (Long) al.get(23));
              //					personalStatsStmt.setString(26, (String) al.get(24));
              //					if()
              //					personalStatsStmt.setLong(27, (Long) al.get(25));
              personalStatsStmt.setString(26, (String) al.get(24));
              personalStatsStmt.setLong(27, (Long) al.get(25));
              if (al.get(26) != null) personalStatsStmt.setString(
                28,
                (String) al.get(26)
              );
              else personalStatsStmt.setString(28, "Null");
              if (al.get(27) != null) personalStatsStmt.setLong(
                29,
                (Long) al.get(27)
              );
              else personalStatsStmt.setLong(29, 0L);
            }
            int i = 0;
            if (al.size() > 24) {
              i = 30;
            } else i = 26;
            if (al.size() <= 15) i = 17;
            // + user_id + ",sysdate,";

            j = keys.iterator();
            while (j.hasNext()) {
              String key = (String) j.next();
              // query += personalStats.get(key);
              // System.out.println(i);

              personalStatsStmt.setLong(i, (Long) personalStats.get(key));
              i++;

              // if (j.hasNext()) {
              // query += ",";
              // }
            }
            // System.out.println(i);
            personalStatsStmt.executeUpdate();
            personalStatsStmt.close();
          } else {
            Set s = personalStats.keySet();
            Iterator j = s.iterator();

            query = "update torn_player_stats set creationdate = sysdate,";
            while (j.hasNext()) {
              String key = (String) j.next();
              query += key + " = ?"; // + personalStats.get(key);
              if (j.hasNext()) {
                query += ",";
              }
            }

            if (al.size() > 15) query +=
              ",rank=?,USER_LEVEL=?,GENDER=?,PROPERTY=?,FRIENDS=?,ENEMIES=?,FORUM_POSTS=?,KARMA=?,AGE=?,ROLE=?,DONATOR=?,NAME=?,PROPERTY_ID=?,REVIVABLE=?,SPOUSE_ID=?,selling_illegal_products=?,theft_crime=?,auto_theft=?,drug_deals=?,computer_crimes=?,murder=?,fraud_crimes=?,other_crimes=?,total_crimes=?,competition=?,score=?,team=?,attacks=? where userid =?";
            // +
            else query +=
              ",rank=?,USER_LEVEL=?,GENDER=?,PROPERTY=?,FRIENDS=?,ENEMIES=?,FORUM_POSTS=?,KARMA=?,AGE=?,ROLE=?,DONATOR=?,NAME=?,PROPERTY_ID=?,REVIVABLE=?,SPOUSE_ID=? where userid =?"; // +
            personalStatsStmt = con.prepareStatement(query);
            //System.out.println(query);
            j = s.iterator();
            int i = 1;
            // query = "update torn_player_stats set creationdate = sysdate,";
            while (j.hasNext()) {
              String key = (String) j.next();
              personalStatsStmt.setLong(i, (Long) personalStats.get(key));
              i++;
            }
            personalStatsStmt.setString(i, (String) al.get(0));
            personalStatsStmt.setLong(i + 1, (Long) al.get(1));
            personalStatsStmt.setString(i + 2, (String) al.get(2));
            personalStatsStmt.setString(i + 3, (String) al.get(3));
            personalStatsStmt.setLong(i + 4, (Long) al.get(4));
            personalStatsStmt.setLong(i + 5, (Long) al.get(5));
            personalStatsStmt.setLong(i + 6, (Long) al.get(6));
            personalStatsStmt.setLong(i + 7, (Long) al.get(7));
            personalStatsStmt.setLong(i + 8, (Long) al.get(8));
            personalStatsStmt.setString(i + 9, (String) al.get(9));
            personalStatsStmt.setLong(i + 10, (Long) al.get(10));
            personalStatsStmt.setString(i + 11, (String) al.get(11));
            personalStatsStmt.setLong(i + 12, (Long) al.get(12));
            personalStatsStmt.setLong(i + 13, (Long) al.get(13));
            personalStatsStmt.setLong(i + 14, (Long) al.get(14));
            if (al.size() > 15) {
              personalStatsStmt.setLong(i + 15, (Long) al.get(15));
              personalStatsStmt.setLong(i + 16, (Long) al.get(16));
              personalStatsStmt.setLong(i + 17, (Long) al.get(17));
              personalStatsStmt.setLong(i + 18, (Long) al.get(18));
              personalStatsStmt.setLong(i + 19, (Long) al.get(19));
              personalStatsStmt.setLong(i + 20, (Long) al.get(20));
              personalStatsStmt.setLong(i + 21, (Long) al.get(21));
              personalStatsStmt.setLong(i + 22, (Long) al.get(22));
              personalStatsStmt.setLong(i + 23, (Long) al.get(23));

              if (al.size() > 24) {
                personalStatsStmt.setString(i + 24, (String) al.get(24));
                personalStatsStmt.setLong(i + 25, (Long) al.get(25));
                if (al.get(26) != null) personalStatsStmt.setString(
                  i + 26,
                  (String) al.get(26)
                );
                else personalStatsStmt.setString(i + 26, "Null");
                if (al.get(27) != null) personalStatsStmt.setLong(
                  i + 27,
                  (Long) al.get(27)
                );
                else personalStatsStmt.setLong(i + 27, 0L);
                personalStatsStmt.setInt(i + 28, user_id);
              }
            } else personalStatsStmt.setInt(i + 15, user_id);
            personalStatsStmt.executeUpdate();
            personalStatsStmt.close();
          }
        }
        // System.out.println(query);
      } catch (Exception e) {
        System.out.println(
          "formQuery For user: " + user_id + " and query is \n" + query
        );
        e.printStackTrace();
      }
    } catch (Exception e) {
      System.out.println(
        "formQuery For user: " + user_id + " and query is \n" + query
      );
      e.printStackTrace();
    }

    return query;
  }

  public void updateLastAction(int scanAll) throws Exception {
    int days = scanAll;
    String query =
      "(select user_id from TORN_PLAYERS where creation_date < SYSDATE - 2  and not (LAST_ACTION_UNIT = 'days' and LAST_ACTION_TIME>" +
      days +
      ") )";
    if (scanAll == 0) days = 120;
    if (!"".equals(sql)) query = sql;
    if (scanAll == 2) query =
      "select emp_id from torn_company_employees where company_id=100236";
    ArrayList al = new ArrayList();
    List sub = new ArrayList();
    al.clear();
    int i = 0;
    try {
      int count = 0;
      int count1 = 0;
      int loopCount = 0;
      String originalApiKey = apiKey;
      // ResultSet rs = statement.executeQuery("select user_id from torn_players where
      // creation_date < sysdate-5");
      ResultSet rs = statement.executeQuery(query);

      startTime = System.currentTimeMillis();

      synchronized (TornGeneral.class) {
        while (rs.next()) {
          int uid = rs.getInt(1);
          if (uid > maxLastAction) al.add(uid);
          if (al.size() > 0 && al.size() % 1000 == 0) System.out.println(
            "Added " +
              al.size() +
              " players in " +
              (System.currentTimeMillis() - startTime) / (1000) +
              " seconds"
          );
        }
        Collections.sort(al);
        if (updateLastActionSubSize == 0) updateLastActionSubSize =
          al.size() / noApiKeys;
        sub = al.subList(0, Math.min(updateLastActionSubSize, al.size()));
        if (sub.size() > 0) {
          System.out.println(
            "Sub1 starting id = " +
              sub.get(0) +
              ", ending id = " +
              sub.get(sub.size() - 1)
          );
          maxLastAction = (Integer) sub.get(sub.size() - 1);
        }
      }
      rs.close();

      System.out.println("sub1 size = " + sub.size());
      Iterator it = sub.iterator();

      long ct = 0;
      long ct1 = 0;
      long ot = startTime;
      long ot1 = startTime;
      SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      Date date = null;
      String u = null;
      String playerUpdateQuery =
        "update torn_players set last_action = ?, last_action_time =?, LAST_ACTION_UNIT = ?" +
        ", CREATION_DATE = sysdate, status = ? where user_id = ?";

      while (it.hasNext()) {
        loopCount++;
        i = (Integer) it.next();
        if (
          playerUpdateStmt == null || playerUpdateStmt.isClosed()
        ) playerUpdateStmt = con.prepareStatement(playerUpdateQuery);
        if (i % 200 == 0) {
          date = new Date();
          System.out.println(
            "In update last action, Processing user i= " +
              i +
              " count= " +
              (count + count1) +
              "  in " +
              (System.currentTimeMillis() - startTime) / (1000) +
              " seconds. Current time is " +
              dtf.format(date)
          );
        }

        String output = null;
        while (output == null) {
          u =
            "https://api.torn.com/user/" +
            i +
            "?selections=profile,personalstats,criminalrecord&key=" +
            apiKey;
          count++;
          // System.out.println(u);
          if (count % 65 == 0) {
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
          conn.setRequestProperty(
            "User-Agent",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11"
          );

          if (conn.getResponseCode() != 200) {
            System.out.println(
              "Failed : HTTP error code : " + conn.getResponseCode()
            );
            i--;
            continue;
          }

          BufferedReader br = new BufferedReader(
            new InputStreamReader((conn.getInputStream()))
          );

          // System.out.println("Output from Server .... \n");
          output = br.readLine();
          if (output == null) Thread.sleep(1000);
        }
        if (output.contains("Incorrect ID")) continue;
        else if (output.contains("Too many requests")) {
          System.out.println("too many reqs with api " + apiKey);
          Thread.sleep(1000 * 60);
        } else {
          JSONParser jsonParser = new JSONParser();
          JSONObject JSONoutput = (JSONObject) jsonParser.parse(output);
          JSONObject personalStats = (JSONObject) JSONoutput.get(
            "personalstats"
          );
          JSONObject criminalRecord = (JSONObject) JSONoutput.get(
            "criminalrecord"
          );
          JSONObject competition = (JSONObject) JSONoutput.get("competition");
          if (
            competition != null && competition.get("score") == null
          ) competition = null;
          al = getProfileStats(JSONoutput, criminalRecord, competition);
          String role = (String) JSONoutput.get("role");
          if (role != null && role.contains("NPC")) continue;
          JSONObject lastAction = (JSONObject) JSONoutput.get("last_action");
          if (lastAction == null) {
            // i--;
            continue;
          }
          JSONObject statusObject = (JSONObject) JSONoutput.get("status");
          String action = (String) lastAction.get("relative");
          if ("No Last Action".equals(action)) continue;
          String description = (String) statusObject.get("description");

          executeLastAction(lastAction, i, description);
          //System.out.println("loopCount = "+loopCount);
          if (loopCount % 50 == 0) {
            playerUpdateStmt.executeBatch();
            playerUpdateStmt.close();
          }
          if (!role.contains("NPC")) {
            if (personalStats == null) {
              System.out.println("Personal stats is null so continuing");
              continue;
            }
            String statQuery = formQuery(personalStats, i, al);
            //stmt1.executeUpdate(statQuery);
          }
        }
      }
      if (!playerUpdateStmt.isClosed()) {
        playerUpdateStmt.executeBatch();
        playerUpdateStmt.close();
      }
    } catch (Exception e) {
      System.out.println("updateLastAction For user: " + i);
      e.printStackTrace();
    }
  }

  public void updateFactionDetails() throws Exception {
    ArrayList al = new ArrayList();
    List sub = new ArrayList();
    PreparedStatement stmt4 = null;
    al.clear();
    int count = 0;
    int count1 = 0;
    int factionId = 0;
    int loopCount = 0;
    ResultSet rs = statement.executeQuery(
      "select id from torn_factions order by id"
    );
    synchronized (TornGeneral.class) {
      while (rs.next()) {
        int uid = rs.getInt(1);
        if (uid > maxFactionId) al.add(uid);
        if (al.size() > 0 && al.size() % 1000 == 0) System.out.println(
          "Added " +
            al.size() +
            " players in " +
            (System.currentTimeMillis() - startTime) / (1000) +
            " seconds"
        );
      }
      Collections.sort(al);
      if (newFactionSubSize == 0) newFactionSubSize = al.size() / noApiKeys;
      sub = al.subList(0, newFactionSubSize);
      System.out.println(
        "Sub1 starting id = " +
          sub.get(0) +
          ", ending id = " +
          sub.get(sub.size() - 1)
      );
      maxFactionId = (Integer) sub.get(sub.size() - 1);
    }
    rs.close();
    Iterator it = sub.iterator();

    startTime = System.currentTimeMillis();

    long ct = 0;
    long ct1 = 0;
    long ot = startTime;
    long ot1 = startTime;
    SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date date = null;
    String u = null;
    String factionMembersQuery = "";
    String playerUpdateQuery = "";
    String query = "";
    query =
      "update torn_factions set creation_date = sysdate,name =?,leader = ?,coleader = ?" +
      ",respect = ?, age = ?,best_chain=? where id =?";
    playerUpdateQuery =
      "update torn_players set last_action = ?, last_action_time =?, LAST_ACTION_UNIT = ?" +
      ", CREATION_DATE = sysdate, status = ? where user_id = ?";
    factionMembersQuery =
      "insert into torn_faction_members values(?,?,?,sysdate)";

    int i = 0;
    stmt1 = con.prepareStatement(query);
    stmt2 = con.prepareStatement(
      "delete torn_faction_members where faction_id=?"
    );
    stmt3 = con.prepareStatement(factionMembersQuery);
    stmt4 = con.prepareStatement("delete torn_factions where id = ?");
    stmt5 = con.prepareStatement(
      "delete torn_faction_members where faction_id=?"
    );
    playerUpdateStmt = con.prepareStatement(playerUpdateQuery);
    try {
      // for (int i = 4209; i < 4210; i++) {//83301
      while (it.hasNext()) {
        loopCount++;
        i = (Integer) it.next();

        // System.out.println(i);
        if (i % 20 == 0) {
          date = new Date();
          System.out.println(
            "In update faction details, Processing faction i= " +
              i +
              " count= " +
              (count + count1) +
              " in " +
              (System.currentTimeMillis() - startTime) / (1000) +
              " seconds. Current time is " +
              dtf.format(date)
          );
        }

        String output = null;
        while (output == null) {
          u =
            "https://api.torn.com/faction/" +
            i +
            "?selections=basic&key=" +
            apiKey;
          count++;
          if (count % 65 == 0) {
            ct = (System.currentTimeMillis() - ot) / (1000);
            // System.out.println("count = "+count+" ct = "+ct);
            if (ct < 60) {
              // System.out.println("Sleeping for " + (60 - ct) + " seconds");
              Thread.sleep((60 - ct) * 1000);
            }
            ot = System.currentTimeMillis();
          }
          long starttime = System.currentTimeMillis();
          URL url = new URL(u);

          HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
          conn.setRequestMethod("GET");
          conn.setRequestProperty("Accept", "application/json");
          conn.setRequestProperty(
            "User-Agent",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11"
          );

          if (conn.getResponseCode() != 200) {
            System.out.println(
              "Failed : HTTP error code : " + conn.getResponseCode()
            );
            // i--;
            continue;
          }

          BufferedReader br = new BufferedReader(
            new InputStreamReader((conn.getInputStream()))
          );

          // System.out.println("Output from Server .... \n");
          output = br.readLine();
          if (output == null) Thread.sleep(1000);
        }
        if (output.contains("Incorrect ID")) continue;
        else if (output.contains("Too many requests")) {
          System.out.println("too many reqs with api " + apiKey);
          Thread.sleep(1000 * 60);
        } else {
          // count++;
          JSONParser jsonParser = new JSONParser();
          JSONObject JSONoutput = (JSONObject) jsonParser.parse(output);

          JSONObject members = (JSONObject) JSONoutput.get("members");
          if (members == null) {
            System.out.println("for faction " + i + " members is empty");
            continue;
          }

          if (members.isEmpty()) {
            stmt4.setLong(1, i);
            stmt4.executeUpdate();
            //	stmt4.close();
            stmt5.setInt(1, i);
            stmt5.executeUpdate();
            //	stmt5.close();
            continue;
          }
          long id = (Long) JSONoutput.get("ID");
          String name = (String) JSONoutput.get("name");
          name = replaceSpecialChars(name);
          long leader = (Long) JSONoutput.get("leader");
          long coleader = (Long) JSONoutput.get("co-leader");
          long respect = (Long) JSONoutput.get("respect");
          long age = (Long) JSONoutput.get("age");
          long best_chain = (Long) JSONoutput.get("best_chain");
          stmt1.setString(1, name);
          stmt1.setLong(2, leader);
          stmt1.setLong(3, coleader);
          stmt1.setLong(4, respect);
          stmt1.setLong(5, age);
          stmt1.setLong(6, best_chain);
          stmt1.setInt(7, i);
          // System.out.println(rs.getInt(1));
          stmt1.addBatch();
          stmt1.clearParameters();
          if (loopCount % 20 == 0) {
            //System.out.println("loopcount is "+loopCount+" committing");
            stmt1.executeBatch();
            //	stmt1.close();
          }
          stmt2.setInt(1, i);
          stmt2.executeUpdate();
          //	stmt2.close();
          // System.out.println(rs.getInt(1));
          int userId = 0;
          long factionDays = 0;
          Set e = members.entrySet();
          Iterator ei = e.iterator();
          // System.out.println("set size ="+e.size());
          // + i + "," +
          // userId + ","
          // + factionDays + ",sysdate)";

          while (ei.hasNext()) {
            Map.Entry map = (Map.Entry) ei.next();
            // System.out.println(map.getKey());// +" = "+map.getValue());
            userId = Integer.parseInt((String) map.getKey());
            // System.out.println(userId);
            JSONObject memDetails = (JSONObject) members.get(map.getKey());
            JSONObject lastAction = (JSONObject) memDetails.get("last_action");
            JSONObject status = (JSONObject) memDetails.get("status");
            if (status == null) continue;
            // String relative = (String) lastAction.get("relative");
            String description = (String) status.get("description");

            factionDays = (Long) memDetails.get("days_in_faction");
            stmt3.setInt(1, i);
            stmt3.setInt(2, userId);
            stmt3.setLong(3, factionDays);

            stmt3.addBatch();
            stmt3.clearParameters();
            executeLastAction(lastAction, userId, description);
          }
          stmt3.executeBatch();
          playerUpdateStmt.executeBatch();
          //	stmt3.close();
        }

        //playerUpdateStmt.close();
      }
      stmt2.close();
      stmt4.close();
      stmt1.executeBatch();
      //	playerUpdateStmt.close();
    } catch (Exception e) {
      System.out.println("For faction " + i);
      e.printStackTrace();
    } finally {
      if (stmt1 != null && !stmt1.isClosed()) stmt1.close();
      if (stmt2 != null && !stmt2.isClosed()) stmt2.close();
      if (stmt4 != null && !stmt4.isClosed()) stmt4.close();
      if (
        playerUpdateStmt != null && !playerUpdateStmt.isClosed()
      ) playerUpdateStmt.close();
      if (stmt3 != null && !stmt3.isClosed()) stmt3.close();
    }
  }

  public void updateMyFaction() throws Exception {
    int count = 0;
    int count1 = 0;
    int factionId = 0;
    int loopCount = 0;

    startTime = System.currentTimeMillis();

    long ct = 0;
    long ct1 = 0;
    long ot = startTime;
    long ot1 = startTime;
    SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date date = null;
    String u = null;
    // for (int i = userId + 1; i < 2477321; i++) {
    // System.out.println("companyid = " + companyId);
    String factionMembersQuery = "";
    String playerUpdateQuery = "";
    String query = "";
    query =
      "update torn_factions set creation_date = sysdate,name =?,leader = ?,coleader = ?" +
      ",respect = ?, age = ?,best_chain=? where id =?";
    playerUpdateQuery =
      "update torn_players set last_action = ?, last_action_time =?, LAST_ACTION_UNIT = ?" +
      ", CREATION_DATE = sysdate, status = ? where user_id = ?";

    try {
      u = "https://api.torn.com/faction/?selections=&key=" + apiKey;
      URL url = new URL(u);
      HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Accept", "application/json");
      conn.setRequestProperty(
        "User-Agent",
        "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11"
      );

      if (conn.getResponseCode() != 200) {
        System.out.println(
          "Failed : HTTP error code : " + conn.getResponseCode()
        );
        // i--;
        //continue;
      }

      BufferedReader br = new BufferedReader(
        new InputStreamReader((conn.getInputStream()))
      );

      // System.out.println("Output from Server .... \n");
      String output = br.readLine();
      JSONParser jsonParser = new JSONParser();
      JSONObject JSONoutput = (JSONObject) jsonParser.parse(output);

      Long i = (Long) JSONoutput.get("ID");
      //	i = 18736L;
      conn.disconnect();
      // for (int i = 4209; i < 4210; i++) {//83301
      stmt1 = con.prepareStatement(query);
      stmt2 = con.prepareStatement(
        "delete torn_faction_members where faction_id=?"
      );
      stmt4 = con.prepareStatement("delete torn_factions where id = ?");
      playerUpdateStmt = con.prepareStatement(playerUpdateQuery);

      output = null;
      while (output == null) {
        u =
          "https://api.torn.com/faction/" +
          i +
          "?selections=basic&key=" +
          apiKey;
        count++;
        if (count % 75 == 0) {
          ct = (System.currentTimeMillis() - ot) / (1000);
          // System.out.println("count = "+count+" ct = "+ct);
          if (ct < 60) {
            // System.out.println("Sleeping for " + (60 - ct) + " seconds");
            Thread.sleep((60 - ct) * 1000);
          }
          ot = System.currentTimeMillis();
        }

        long starttime = System.currentTimeMillis();
        url = new URL(u);

        conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty(
          "User-Agent",
          "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11"
        );

        if (conn.getResponseCode() != 200) {
          System.out.println(
            "Failed : HTTP error code : " + conn.getResponseCode()
          );
          // i--;
          continue;
        }

        br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        // System.out.println("Output from Server .... \n");
        output = br.readLine();
        if (output == null) Thread.sleep(1000);
      }
      if (
        output.contains("Incorrect ID") //continue;
      ) System.out.println("Incorrect Id");
      else if (output.contains("Too many requests")) {
        System.out.println("too many reqs with api " + apiKey);
        Thread.sleep(1000 * 60);
      } else {
        // count++;
        jsonParser = new JSONParser();
        JSONoutput = (JSONObject) jsonParser.parse(output);

        JSONObject members = (JSONObject) JSONoutput.get("members");
        if (members == null) {
          System.out.println("for faction " + i + " members is empty");
          //continue;
        }

        if (members.isEmpty()) {
          stmt4.setLong(1, i);
          stmt4.executeUpdate();
          stmt4.close();
          //continue;
        }
        long id = (Long) JSONoutput.get("ID");
        String name = (String) JSONoutput.get("name");
        name = replaceSpecialChars(name);
        long leader = (Long) JSONoutput.get("leader");
        long coleader = (Long) JSONoutput.get("co-leader");
        long respect = (Long) JSONoutput.get("respect");
        long age = (Long) JSONoutput.get("age");
        long best_chain = (Long) JSONoutput.get("best_chain");
        stmt1.setString(1, name);
        stmt1.setLong(2, leader);
        stmt1.setLong(3, coleader);
        stmt1.setLong(4, respect);
        stmt1.setLong(5, age);
        stmt1.setLong(6, best_chain);
        stmt1.setLong(7, i);
        // System.out.println(rs.getInt(1));
        stmt1.addBatch();
        stmt1.clearParameters();
        if (loopCount % 20 == 0) {
          stmt1.executeBatch();
          stmt1.close();
        }
        // System.out.println(rs.getInt(1));
        //					stmt1 = con.prepareStatement("delete torn_faction_members where faction_id=?");//" + i);
        stmt2.setLong(1, i);
        stmt2.executeUpdate();
        stmt2.close();
        // System.out.println(rs.getInt(1));
        int userId = 0;
        long factionDays = 0;
        Set e = members.entrySet();
        Iterator ei = e.iterator();
        // System.out.println("set size ="+e.size());
        factionMembersQuery =
          "insert into torn_faction_members values(?,?,?,sysdate)"; // + i + "," +
        // userId + ","
        // + factionDays + ",sysdate)";
        stmt3 = con.prepareStatement(factionMembersQuery);
        while (ei.hasNext()) {
          Map.Entry map = (Map.Entry) ei.next();
          // System.out.println(map.getKey());// +" = "+map.getValue());
          userId = Integer.parseInt((String) map.getKey());
          // System.out.println(userId);
          JSONObject memDetails = (JSONObject) members.get(map.getKey());
          JSONObject lastAction = (JSONObject) memDetails.get("last_action");
          JSONObject status = (JSONObject) memDetails.get("status");
          if (status == null) continue;
          // String relative = (String) lastAction.get("relative");
          String description = (String) status.get("description");

          factionDays = (Long) memDetails.get("days_in_faction");
          stmt3.setLong(1, i);
          stmt3.setInt(2, userId);
          stmt3.setLong(3, factionDays);

          stmt3.addBatch();
          stmt3.clearParameters();
          executeLastAction(lastAction, userId, description);
        }
        stmt3.executeBatch();
        playerUpdateStmt.executeBatch();
        stmt3.close();
      }
      playerUpdateStmt.close();

      //	stmt1.executeBatch();
      //	playerUpdateStmt.close();
    } catch (Exception e) {
      //System.out.println("For faction " + i);
      e.printStackTrace();
    } finally {
      if (stmt1 != null && !stmt1.isClosed()) stmt1.close();
      if (stmt2 != null && !stmt2.isClosed()) stmt2.close();
      if (stmt4 != null && !stmt4.isClosed()) stmt4.close();
      if (
        playerUpdateStmt != null && !playerUpdateStmt.isClosed()
      ) playerUpdateStmt.close();
      if (stmt3 != null && !stmt3.isClosed()) stmt3.close();
    }
  }

  public void addFactionDetails() throws Exception {
    ArrayList al = new ArrayList();
    List sub = new ArrayList();
    maxFactionId = 57000;
    Thread.sleep(15 * 1000);
    int count = 0;
    int count1 = 0;

    int loopCount = 0;
    if (factionId == 0) {
      ResultSet rs = statement.executeQuery(
        "select max(ID) from torn_factions"
      );
      rs.next();
      factionId = rs.getInt(1);
    }

    startTime = System.currentTimeMillis();

    long ct = 0;
    long ct1 = 0;
    long ot = startTime;
    long ot1 = startTime;
    SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date date = null;
    String u = null;
    // for (int i = userId + 1; i < 2477321; i++) {
    System.out.println("factionId = " + factionId);
    String factionMembersQuery = "";
    String playerUpdateQuery = "";
    String query = "";
    // maxFactionId = 200;
    try {
      playerUpdateQuery =
        "update torn_players set last_action = ?, last_action_time =?, LAST_ACTION_UNIT = ?" +
        ", CREATION_DATE = sysdate, status = ? where user_id = ?";
      playerUpdateStmt = con.prepareStatement(playerUpdateQuery);
      query =
        "insert into torn_factions(id,name,leader,coleader,respect,age,best_chain,creation_date) values(?,?,?,?,?,?,?,sysdate)";
      // +
      // id+",'"+name+"',"+leader+","+coleader+","+respect+","+age+","+best_chain+",sysdate)";
      stmt1 = con.prepareStatement(query);
      synchronized (TornGeneral.class) {
        for (int i = factionId + 1; i < maxFactionId; i++) al.add(i);
        if (alSize == 0) alSize = al.size();
        if (newFactionSubSize == 0) newFactionSubSize = al.size() / noApiKeys;
        sub = al.subList(0, newFactionSubSize);

        factionId = (Integer) sub.get(sub.size() - 1);
      }
      // maxCompanyId = companyId+1;

      System.out.println(
        "Thread " +
          Thread.currentThread().getName() +
          " Sub starting id = " +
          sub.get(0) +
          ", ending id = " +
          sub.get(sub.size() - 1)
      );
      Iterator it = sub.iterator();
      while (it.hasNext()) {
        int i = (Integer) it.next(); // 83301
        if (i % 20 == 0) {
          date = new Date();
          System.out.println(
            "In Add faction details, Processing faction i= " +
              i +
              " count= " +
              (count + count1) +
              " in " +
              (System.currentTimeMillis() - startTime) / (1000) +
              " seconds. Current time is " +
              dtf.format(date)
          );
        }
        String output = null;
        while (output == null) {
          u =
            "https://api.torn.com/faction/" +
            i +
            "?selections=basic&key=" +
            apiKey;
          count++;
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
          conn.setRequestProperty(
            "User-Agent",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11"
          );

          if (conn.getResponseCode() != 200) {
            System.out.println(
              "Failed : HTTP error code : " + conn.getResponseCode()
            );
            i--;
            continue;
          }

          BufferedReader br = new BufferedReader(
            new InputStreamReader((conn.getInputStream()))
          );

          // System.out.println("Output from Server .... \n");
          output = br.readLine();
          if (output == null) Thread.sleep(1000);
        }
        if (output.contains("Incorrect ID")) continue;
        else if (output.contains("Too many requests")) {
          System.out.println("too many reqs with api " + apiKey);
          Thread.sleep(1000 * 60);
        } else {
          // count++;
          JSONParser jsonParser = new JSONParser();
          JSONObject JSONoutput = (JSONObject) jsonParser.parse(output);
          JSONObject members = (JSONObject) JSONoutput.get("members");
          Long id = (Long) JSONoutput.get("ID");
          if (id == null) continue;
          String name = (String) JSONoutput.get("name");
          name = replaceSpecialChars(name);
          long leader = (Long) JSONoutput.get("leader");
          long coleader = (Long) JSONoutput.get("co-leader");
          long respect = (Long) JSONoutput.get("respect");
          long age = (Long) JSONoutput.get("age");
          long best_chain = (Long) JSONoutput.get("best_chain");

          stmt1.setLong(1, id);
          stmt1.setString(2, name);
          stmt1.setLong(3, leader);
          stmt1.setLong(4, coleader);
          stmt1.setLong(5, respect);
          stmt1.setLong(6, age);
          stmt1.setLong(7, best_chain);
          loopCount++;
          // System.out.println(rs.getInt(1));
          stmt1.addBatch();
          stmt1.clearParameters();
          if (loopCount % 20 == 0) stmt1.executeBatch();
          // System.out.println(query);
          // stmt.executeUpdate(query);
          int userId = 0;
          long factionDays = 0;
          Set e = members.entrySet();
          Iterator ei = e.iterator();
          // System.out.println("set size ="+e.size());
          factionMembersQuery =
            "insert into torn_faction_members values(?,?,?,sysdate)";
          stmt2 = con.prepareStatement(factionMembersQuery);
          while (ei.hasNext()) {
            Map.Entry map = (Map.Entry) ei.next();
            // System.out.println(map.getKey());// +" = "+map.getValue());
            userId = Integer.parseInt((String) map.getKey());
            // System.out.println(userId);
            JSONObject memDetails = (JSONObject) members.get(map.getKey());
            JSONObject lastAction = (JSONObject) memDetails.get("last_action");
            JSONObject status = (JSONObject) memDetails.get("status");
            if (status == null) continue;
            String description = (String) status.get("description");

            factionDays = (Long) memDetails.get("days_in_faction");
            stmt2.setLong(1, id);
            stmt2.setInt(2, userId);
            stmt2.setLong(3, factionDays);
            try {
              stmt2.executeUpdate();
            } catch (Exception ex) {
              System.out.println("for faction id = " + id);
              System.out.println("for user id = " + userId);
            }
            executeLastAction(lastAction, userId, description);
            // stmt.executeUpdate(playerUpdateQuery);
          }
          stmt2.executeBatch();
          playerUpdateStmt.executeBatch();
          stmt2.close();
        }
      }
      stmt1.executeBatch();
      playerUpdateStmt.close();
    } catch (Exception e) {
      System.out.println(factionMembersQuery);
      System.out.println(playerUpdateQuery);
      System.out.println(query);
      e.printStackTrace();
    } finally {
      stmt1.close();
    }
  }

  public void executeLastAction(
    JSONObject lastAction,
    int userId,
    String status
  ) throws Exception {
    String relative = (String) lastAction.get("relative");
    if ("No Last Action".equals(relative)) return;
    String[] spl = relative.split(" ago");
    // if (spl.length < 2)
    // continue;
    String[] spl1 = spl[0].split(" ");
    int size = spl1.length;

    int lastActionTime = Integer.parseInt(spl1[size - 2]);
    String lastActionUnit = spl1[size - 1];
    String lAction = lastActionTime + " " + lastActionUnit + " ago";
    playerUpdateStmt.setString(1, lAction);
    playerUpdateStmt.setInt(2, lastActionTime);
    playerUpdateStmt.setString(3, lastActionUnit);
    playerUpdateStmt.setString(4, status);
    playerUpdateStmt.setInt(5, userId);

    playerUpdateStmt.addBatch();
  }

  public ArrayList getProfileStats(
    JSONObject JSONoutput,
    JSONObject criminalRecord,
    JSONObject competition
  ) {
    ArrayList al = new ArrayList();
    al.add(JSONoutput.get("rank"));
    al.add(JSONoutput.get("level"));
    al.add(JSONoutput.get("gender"));
    al.add(JSONoutput.get("property"));
    al.add(JSONoutput.get("friends"));
    al.add(JSONoutput.get("enemies"));
    al.add(JSONoutput.get("forum_posts"));
    al.add(JSONoutput.get("karma"));
    al.add(JSONoutput.get("age"));
    al.add(JSONoutput.get("role"));
    al.add(JSONoutput.get("donator"));
    al.add(JSONoutput.get("name"));
    al.add(JSONoutput.get("property_id"));
    al.add(JSONoutput.get("revivable"));
    JSONObject married = (JSONObject) JSONoutput.get("married");
    al.add(married.get("spouse_id"));

    /*	if(criminalRecord != null) {
			al.add(criminalRecord.get("selling_illegal_products"));
			al.add(criminalRecord.get("theft"));
			al.add(criminalRecord.get("auto_theft"));
			al.add(criminalRecord.get("drug_deals"));
			al.add(criminalRecord.get("computer_crimes"));
			al.add(criminalRecord.get("murder"));
			al.add(criminalRecord.get("fraud_crimes"));
			al.add(criminalRecord.get("other"));
			al.add(criminalRecord.get("total"));
		}
		
		if(competition != null) {
			al.add(competition.get("name"));
			al.add(competition.get("score"));
			al.add(competition.get("team"));
			al.add(competition.get("attacks"));
			
		}*/

    return al;
  }

  public void refreshAllData() throws SQLException {
    ArrayList al = new ArrayList();
    int[] lastTime = new int[4500000];
    String[] lastUnit = new String[4500000];

    al.clear();

    ResultSet rs = null;
    int i = 0;
    int count1 = 0;
    try {
      int count = 0;

      int loopCount = 0;
      String originalApiKey = apiKey;
      rs = statement.executeQuery(
        "select user_id, last_action_time, last_action_unit from TORN_PLAYERS"
      ); // where creation_date < to_date('26-12-24 15:19','DD-MM-YY HH24:MI')");

      startTime = System.currentTimeMillis();
      synchronized (TornGeneral.class) {
        while (rs.next()) {
          int id = (Integer) rs.getInt(1);
          if (id > maxLastAction) al.add(id);
          lastTime[id] = rs.getInt(2);
          lastUnit[id] = rs.getString(3);
          if (al.size() % 10000 == 0) System.out.println(
            "Added " +
              al.size() +
              " players in " +
              (System.currentTimeMillis() - startTime) / (1000) +
              " seconds"
          );
        }

        Collections.sort(al);
        if (updateLastActionSubSize == 0) updateLastActionSubSize =
          al.size() / noApiKeys;
        sub1 = al.subList(0, Math.min(updateLastActionSubSize, al.size()));
        if (sub1.size() > 0) System.out.println(
          "Sub1 starting id = " +
            sub1.get(0) +
            ", ending id = " +
            sub1.get(sub1.size() - 1)
        );
        maxLastAction = (Integer) sub1.get(sub1.size() - 1);
      }
      rs.close();
      System.out.println("sub1 size = " + sub1.size());
      Iterator it = sub1.iterator();

      long ct = 0;
      long ct1 = 0;
      long ot = startTime;
      long ot1 = startTime;
      SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      Date date = null;
      String u = null;

      String playerUpdateQuery =
        "update torn_players set last_action = ?, last_action_time =?, LAST_ACTION_UNIT = ?" +
        ", CREATION_DATE = sysdate, status = ? where user_id = ?";

      while (it.hasNext()) {
        loopCount++;
        i = (Integer) it.next();
        if (
          playerUpdateStmt == null || playerUpdateStmt.isClosed()
        ) playerUpdateStmt = con.prepareStatement(playerUpdateQuery);
        if (i % 200 == 0) {
          date = new Date();
          System.out.println(
            "In update all users, Processing user i= " +
              i +
              " count= " +
              (count) +
              "  in " +
              (System.currentTimeMillis() - startTime) / (1000) +
              " seconds. Current time is " +
              dtf.format(date)
          );
        }

        String output = null;
        while (output == null) {
          u =
            "https://api.torn.com/user/" +
            i +
            "?selections=profile,personalstats,criminalrecord&key=" +
            apiKey;
          count++;
          // System.out.println(u);
          if (count % 75 == 0) {
            ct = (System.currentTimeMillis() - ot) / (1000);
            //	 System.out.println("count = "+count+" ct = "+ct);
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
          conn.setRequestProperty(
            "User-Agent",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11"
          );

          if (conn.getResponseCode() != 200) {
            System.out.println(
              "Failed : HTTP error code : " + conn.getResponseCode()
            );
            i--;
            continue;
          }

          BufferedReader br = new BufferedReader(
            new InputStreamReader((conn.getInputStream()))
          );

          // System.out.println("Output from Server .... \n");
          output = br.readLine();
          if (output == null) Thread.sleep(1000);
        }
        if (output.contains("Incorrect ID")) continue;
        else if (output.contains("Too many requests")) {
          System.out.println("too many reqs with api " + apiKey);
          Thread.sleep(1000 * 60);
        } else {
          JSONParser jsonParser = new JSONParser();
          JSONObject JSONoutput = (JSONObject) jsonParser.parse(output);
          JSONObject personalStats = (JSONObject) JSONoutput.get(
            "personalstats"
          );
          JSONObject criminalRecord = (JSONObject) JSONoutput.get(
            "criminalrecord"
          );
          JSONObject competition = (JSONObject) JSONoutput.get("competition");
          if (
            competition != null && competition.get("score") == null
          ) competition = null;
          al = getProfileStats(JSONoutput, criminalRecord, competition);
          String role = (String) JSONoutput.get("role");
          if (role != null && role.contains("NPC")) continue;
          JSONObject lastAction = (JSONObject) JSONoutput.get("last_action");
          if (lastAction == null) {
            // i--;
            continue;
          }
          JSONObject statusObject = (JSONObject) JSONoutput.get("status");
          String action = (String) lastAction.get("relative");
          if ("No Last Action".equals(action)) continue;
          String time[] = action.split(" ");
          String description = (String) statusObject.get("description");
          //System.out.println(" User id: "+i+" Last time: "+lastTime[i]+" Last unit: "+lastUnit[i]+
          //		" Current time: "+time[0]+" Current Unit: "+time[1]);
          if (
            "days".equals(time[1]) &&
            (lastUnit[i]).equals("days") &&
            (Integer.parseInt(time[0]) < lastTime[i])
          ) {
            count1++;
          }
          executeLastAction(lastAction, i, description);
          //System.out.println("loopCount = "+loopCount);
          if (loopCount % 50 == 0) {
            //	System.out.println("Commiting...");
            playerUpdateStmt.executeBatch();
            playerUpdateStmt.close();
          }
          if (!role.contains("NPC")) {
            if (personalStats == null) continue;
            String statQuery = formQuery(personalStats, i, al);
            //stmt1.executeUpdate(statQuery);
          }
        }
      }
      playerUpdateStmt.executeBatch();
      playerUpdateStmt.close();
    } catch (Exception e) {
      System.out.println("refreshAllData For user: " + i);
      e.printStackTrace();
    } finally {
      if (rs != null) rs.close();
    }
    System.out.println("No. of returning players: " + count1);
  }

  public void calculateDrugAddiction() throws SQLException {
    int addiction = 0;
    long firstDecay = 1733887860L;
    ResultSet rs = null;
    long timestamp = 1733848380L;
    List s = new ArrayList();
    try {
      String u = null;
      String apiKey = "FfW9SThJpLIrAOiJ";
      int loopCount = 0;
      while (true) {
        loopCount++;

        String output = null;
        while (output == null) {
          u =
            "https://api.torn.com/user/?selections=log&cat=61&from=" +
            timestamp +
            "&to=" +
            (timestamp + (3600 * 24 * 15)) +
            "&key=" +
            apiKey;
          timestamp = timestamp + (3600 * 24 * 15) + 1;
          URL url = new URL(u);

          HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
          conn.setRequestMethod("GET");
          conn.setRequestProperty("Accept", "application/json");
          conn.setRequestProperty(
            "User-Agent",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11"
          );

          if (conn.getResponseCode() != 200) {
            System.out.println(
              "Failed : HTTP error code : " + conn.getResponseCode()
            );
            //	i--;
            continue;
          }

          BufferedReader br = new BufferedReader(
            new InputStreamReader((conn.getInputStream()))
          );

          // System.out.println("Output from Server .... \n");
          output = br.readLine();
          if (output == null) Thread.sleep(1000);
        }
        if (output.contains("Incorrect ID")) continue;
        else if (output.contains("Too many requests")) {
          System.out.println("too many reqs with api " + apiKey);
          Thread.sleep(1000 * 60);
        } else {
          JSONParser jsonParser = new JSONParser();
          JSONObject JSONoutput = (JSONObject) jsonParser.parse(output);
          if (output == null) break;
          if (!output.contains("timestamp")) break;
          JSONObject log = (JSONObject) JSONoutput.get("log");
          if (log == null || log.isEmpty()) break;
          Set k = log.keySet();
          Iterator it = k.iterator();
          while (it.hasNext()) {
            String key = (String) it.next();
            JSONObject drugLog = (JSONObject) log.get(key);

            String title = (String) drugLog.get("title");
            Long timeStamp = (Long) drugLog.get("timestamp");
            Long rehabTimes = 0L;
            Double addictionPercentage = 0.0;
            if ("Rehab".equals(title)) {
              JSONObject data = (JSONObject) drugLog.get("data");
              rehabTimes = (Long) data.get("rehab_times");
              if (data.get("addiction") instanceof Double) addictionPercentage =
                (Double) data.get("addiction");
              else addictionPercentage = (Long) data.get("addiction") * 1.0;
            }
            //System.out.println(title+"\t"+timeStamp+"\t"+rehabTimes+"\t"+addictionPercentage);
            Drugs d = new Drugs(
              title,
              timeStamp,
              rehabTimes,
              addictionPercentage
            );
            s.add(d);
          }
        }
      }
      Collections.sort(s, new SortbyTime());
      System.out.println("Loop count = " + loopCount);
      Iterator it = s.iterator();
      while (it.hasNext()) {
        Drugs d = (Drugs) it.next();
        Long timeStamp = d.timeStamp;
        //	System.out.println(timeStamp);
        String title = d.title;
        if (timeStamp > firstDecay) {
          addiction -= 20;
          firstDecay += 3600 * 24;
          if (addiction < 0) addiction = 0;
        }
        if ("Item use xanax overdose".equals(title)) addiction += 50;
        else if ("Item use xanax".equals(title)) addiction += 18;
        else if ("Item use vicodin".equals(title)) addiction += 7;
        else if ("Item use cannabis".equals(title)) addiction += 1;
        else if ("Rehab".equals(title)) {
          Long rehabTimes = d.rehabTimes;
          Double addictionPercentage = d.addictionPercentage;
          System.out.println("Addiciton: " + addiction);
          System.out.println("Rehab times: " + rehabTimes);
          System.out.println("Addcition percentage: " + addictionPercentage);
          System.out.println(
            "Each rehab reduce addiction by " +
              ((addiction * addictionPercentage) / 100) / rehabTimes
          );
          addiction = (int) (addiction -
            Math.round(((addiction * addictionPercentage) / 100)));
        } else System.out.println("New title: " + title);
      }
    } catch (Exception e) {
      System.out.println("Exception");
      e.printStackTrace();
    }
  }
}

class Drugs {

  String title;
  Long timeStamp;
  Long rehabTimes;
  Double addictionPercentage;

  public Drugs(
    String title,
    Long timeStamp,
    Long rehabTimes,
    Double addictionPercentage
  ) {
    this.title = title;
    this.timeStamp = timeStamp;
    this.rehabTimes = rehabTimes;
    this.addictionPercentage = addictionPercentage;
  }
}

class SortbyTime implements Comparator<Drugs> {

  // Compare by roll number in ascending order
  public int compare(Drugs a, Drugs b) {
    return (a.timeStamp < b.timeStamp) ? -1 : 1;
  }
}
