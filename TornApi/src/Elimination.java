import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Elimination {
	static String apiKey = "";
	static Connection con = null;
	static PreparedStatement stmt = null;
	static PreparedStatement stmt1 = null;
	static PreparedStatement stmt2 = null;
	static PreparedStatement stmt3 = null;
	static PreparedStatement stmt4 = null;
	static PreparedStatement playerUpdateStmt = null;
	static PreparedStatement personalStatsStmt = null;
	static Statement statement = null;
	static Statement statement1 = null;
	static ArrayList al = new ArrayList();
	static ArrayList newIds = new ArrayList();
	static long startTime = 0;
	static long currentTime = 0;
	static int userProcessed = 0;
	static int maxUserId = 0;
	static int maxCompanyId = 0;
	static int maxFactionId = 0;
	static int scanAll = 0;
	static List sub1 = new ArrayList();
	static List sub2 = new ArrayList();
	static final String hostname = "mumbai51408.bom2.fusionappsdbom1.oraclevcn.com";
	static final int port = 1522;
	static final String sid = "mumbai51";

	public static void main(String[] args) {
		// TODO code application logic here
		try {
			Properties systemProperties = System.getProperties();
			systemProperties.setProperty("https.proxyHost", "www-proxy.us.oracle.com");
			systemProperties.setProperty("https.proxyPort", "80");
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection("jdbc:oracle:thin:@"+hostname+":"+port+":"+sid, "fusion","fusion");
			statement = con.createStatement();
			statement1 = con.createStatement();
			// stmt = conn.createStatement();
			// stmt1 = conn.createStatement();
			if (args.length > 0) {
				apiKey = args[0];
				maxUserId = Integer.parseInt(args[1]);
				maxCompanyId = Integer.parseInt(args[2]);
				maxFactionId = Integer.parseInt(args[3]);
			} else {
				apiKey = "HCiishg1krSG4VDX";
				scanAll = 1;
			}
			
			// checkUsers();
//			if(scanAll == 0) {
//				if ("tbQgiQjG3KMGQQI8".equals(apiKey)) {
//					addNewPlayers();
//					addCompanyDetails();
//					updateCompanyDetails();
//				} else {
//					addFactionDetails();
//					updateFactionDetails();
//				}
//			}
//			addNewPlayers();
//			addCompanyDetails();
//			updateCompanyDetails();
//			addFactionDetails();
//			updateFactionDetails();
//			updateLastAction(scanAll);
			insertOrUpdatePersonalStats(scanAll);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String replaceSpecialChars(String change) {
		if (change != null && change.contains("'")) {
			change = change.replaceAll("'", "''");
		}
		if (change != null && change.contains("&")) {
			change = change.replaceAll("&", "'||'&'||'");
		}
		return change;
	}

	public static void insertOrUpdatePersonalStats(int scanAll) throws SQLException {
		al.clear();
		ResultSet rs = null;
		int i = 0;
		try {
			int count = 0;
			int count1 = 0;
			int loopCount = 0;
			// ResultSet rs = stmt.executeQuery("select user_id from TORN_PLAYERS");
			rs = statement.executeQuery(
					"select userid from torn_player_stats where team <> 'heavyweights' and team is not null and xantaken < 350 and attackswon>912 order by userid desc");
			
//			scanAll = 1;
//			if("tbQgiQjG3KMGQQI8".equals(apiKey))
//				rs = statement.executeQuery(
//						"select user_id from torn_player_stats tps, torn_players tp where total_crimes is null and  tps.userid = tp.user_id and tp.user_id < 1688459 order by tp.user_id");
//			else
//				rs = statement.executeQuery(
//						"select user_id from torn_player_stats tps, torn_players tp where total_crimes is null and  tps.userid = tp.user_id and tp.user_id >= 1688459 order by tp.user_id");
			
			//"select userid from torn_player_stats where age is null");
					// rs.next();
			// int userId = rs.getInt(1);
			startTime = System.currentTimeMillis();

			while (rs.next()) {
				al.add(rs.getInt(1));
				if (al.size() % 1000 == 0)
					System.out.println("Added " + al.size() + " players in "
							+ (System.currentTimeMillis() - startTime) / (1000) + " seconds");
			}
			rs.close();
			//Collections.sort(al);
			sub1 = al;
			if(scanAll == 0) {
				int x = al.size()/2;
				System.out.println("x = "+x);
				if("tbQgiQjG3KMGQQI8".equals(apiKey))
					sub1 = al.subList(x,al.size()); 
				else
					sub1 = al.subList(0, x);
			}
			Iterator it = sub1.iterator();

			long ct = 0;
			long ot = startTime;
			long ct1 = 0;
			long ot1 = startTime;
			SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date date = null;
			String u = null;
			String originalApiKey = apiKey;
			// for (int i = userId + 1; i < 2477321; i++) {
			// System.out.println("userid = " + userId);
			// for (int i = startUserId; i <= endUserId; i++) {
//				if (!al.contains(i))
//					continue;
			String playerUpdateQuery = "update torn_players set last_action = ?, last_action_time =?, LAST_ACTION_UNIT = ?"
					+ ", CREATION_DATE = sysdate, status = ? where user_id = ?";
			playerUpdateStmt = con.prepareStatement(playerUpdateQuery);
			
			while (it.hasNext()) {
				loopCount++;
				i = (Integer)it.next();
				if (count % 200 == 0) {
					date = new Date();
					System.out.println("In update players stats, Processing user i= " + i + " count= "
							+ (count + count1) + " in " + (System.currentTimeMillis() - startTime) / (1000)
							+ " seconds. Current time is " + dtf.format(date));
				}
//				if (apiKey.equals("tbQgiQjG3KMGQQI8")) {
//					apiKey = "grxSMUkxonbgCTTg";
//					count1++;
//				} else {
//					apiKey = "tbQgiQjG3KMGQQI8";
//					count++;
//				}
				if(i==2481851)
					apiKey = "tbQgiQjG3KMGQQI8";
				else if (i==2398000)
					apiKey = "grxSMUkxonbgCTTg";
				else
					apiKey = originalApiKey;
				String output = null;
				while(output == null) {
					u = "https://api.torn.com/user/" + i + "?selections=profile,personalstats,crimes&key=" + apiKey;
					 count++;
	
					if (count % 80 == 0) {
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
					conn.setRequestProperty("User-Agent",
							"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
	
					if (conn.getResponseCode() != 200) {
						System.out.println("Failed : HTTP error code : " + conn.getResponseCode());
						i--;
						continue;
					}
	
					BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	
					// System.out.println("Output from Server .... \n");
					output = br.readLine();
					if(output==null)
						Thread.sleep(1000);
				// System.out.println(output);
				}
				if (output.contains("Incorrect ID"))
					continue;
				else if (output.contains("Too many requests")) {
					System.out.println("Too many reqs");
					Thread.sleep(1000 * 60 * 5);
				} else {
					JSONParser jsonParser = new JSONParser();
					JSONObject JSONoutput = (JSONObject) jsonParser.parse(output);
					JSONObject lastAction = (JSONObject) JSONoutput.get("last_action");
					JSONObject personalStats = (JSONObject) JSONoutput.get("personalstats");
					JSONObject criminalRecord = (JSONObject) JSONoutput.get("criminalrecord");
					JSONObject competition = (JSONObject) JSONoutput.get("competition");
					
					ArrayList al = getProfileStats(JSONoutput,criminalRecord, competition);
					String role = (String) JSONoutput.get("role");
					if (role!=null && !role.contains("NPC")) {
						if (personalStats == null)
							continue;
						String statQuery = formQuery(personalStats, i, al);
						// System.out.println(statQuery);
						JSONObject statusObject = (JSONObject) JSONoutput.get("status");
						String action = (String) lastAction.get("relative");
						if ("No Last Action".equals(action))
							continue;
						String status = (String) statusObject.get("description");
//						String[] spl = action.split(" ago");
//						// if (spl.length < 2)
//						// continue;
//						String[] spl1 = spl[0].split(" ");
//						int size = spl1.length;
//
//						int lastActionTime = Integer.parseInt(spl1[size - 2]);
//						String lastActionUnit = spl1[size - 1];
//						String lAction = lastActionTime + " " + lastActionUnit + " ago";
//						// stmt1.executeUpdate(statQuery);
//						String playerUpdateQuery = "update torn_players set last_action = ?, last_action_time =?, LAST_ACTION_UNIT = ?"
//								+ ", CREATION_DATE = sysdate, status = ? where user_id = ?";
//						stmt1 = con.prepareStatement(playerUpdateQuery);
//						stmt1.setString(1, lAction);
//						stmt1.setInt(2, lastActionTime);
//						stmt1.setString(3, lastActionUnit);
//						stmt1.setString(4, status);
//						stmt1.setInt(5, i);
//
//						stmt1.executeUpdate();
						executeLastAction(lastAction, i, status);
						if(loopCount%50==0)
							playerUpdateStmt.executeBatch();
					}
				}
				
			}
			playerUpdateStmt.executeBatch();
			playerUpdateStmt.close();
		} catch (Exception e) {
			System.out.println("For user: "+i);
			e.printStackTrace();
		}
		finally {
			if(rs!=null)
				rs.close();
				
		}
		
	}

	public static void updateCompanyDetails() throws Exception {
		int count = 0;
		int count1 = 0;
		int companyId = 0;
		int loopCount = 0;
		ResultSet rs = statement.executeQuery("select id from TORN_COMPANIES");

		// rs.next();
		// companyId = rs.getInt(1);
		startTime = System.currentTimeMillis();
		/*
		 * while(rs.next()) { al.add(rs.getInt(1)); if(al.size()%1000==0)
		 * System.out.println("Added "+
		 * al.size()+" players in "+(System.currentTimeMillis()-startTime)/(1000)
		 * +" seconds"); } Collections.sort(al);
		 */

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
		String query = "";
		query = "update torn_companies set creation_date = sysdate,COMPANY_TYPE=?,WEEKLY_INCOME=?"
				+ ",DIRECTOR=?,RATING=?,WEEKLY_CUSTOMERS=?,DAILY_INCOME=?,EMPLOYEES_CAPACITY=?,"
				+ "DAYS_OLD=?,NAME=?,EMPLOYEES_HIRED=?,DAILY_CUSTOMERS=? where id =?";
		stmt1 = con.prepareStatement(query);
		stmt2 = con.prepareStatement("delete torn_company_employees where company_id=?");
		playerUpdateQuery = "update torn_players set last_action = ?, last_action_time =?, LAST_ACTION_UNIT = ?"
				+ ", CREATION_DATE = sysdate, status = ? where user_id = ?";
		stmt4 = con.prepareStatement("delete torn_companies where id=?");
		playerUpdateStmt = con.prepareStatement(playerUpdateQuery);
		int i =0;
		try {
			// for (int i = 4209; i < 4210; i++) {//83301
			while (rs.next()) {
				loopCount++;
				i = rs.getInt(1);
				// System.out.println(i);
				if (i % 20 == 0) {
					date = new Date();
					System.out.println("In update company details, Processing company i= " + i + " count= "
							+ (count + count1) + " in " + (System.currentTimeMillis() - startTime) / (1000)
							+ " seconds. Current time is " + dtf.format(date));
				}
//				if (apiKey.equals("tbQgiQjG3KMGQQI8")) {
//					apiKey = "grxSMUkxonbgCTTg";
//					count1++;
//				} else {
//					apiKey = "tbQgiQjG3KMGQQI8";
//					count++;
//				}
				String output = null;
				while(output == null) {
					u = "https://api.torn.com/company/" + i + "?selections=profile&key=" + apiKey;
					count++;
					if (count % 80 == 0) {
						ct = (System.currentTimeMillis() - ot) / (1000);
						// System.out.println("count = "+count+" ct = "+ct);
						if (ct < 60) {
							// System.out.println("Sleeping for " + (60 - ct) + " seconds");
							Thread.sleep((60 - ct) * 1000);
						}
						ot = System.currentTimeMillis();
	
					}
	
	//				if (apiKey.equals("grxSMUkxonbgCTTg") && count1 % 85 == 0 && count1 != 0) {
	//					ct1 = (System.currentTimeMillis() - ot1) / (1000);
	//					// System.out.println("count1 = "+count1+" ct1 = "+ct1);
	//					if (ct1 < 60) {
	//						System.out.println("Sleeping for " + (60 - ct) + " seconds");
	//						Thread.sleep((60 - ct1) * 1000);
	//					}
	//					ot1 = System.currentTimeMillis();
	//
	//				}
					// System.out.println(rs.getInt(1));
					long starttime = System.currentTimeMillis();
					URL url = new URL(u);
	
					HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setRequestProperty("Accept", "application/json");
					conn.setRequestProperty("User-Agent",
							"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
	
					if (conn.getResponseCode() != 200) {
						System.out.println("Failed : HTTP error code : " + conn.getResponseCode());
						// i--;
						// continue;
					}
	
					BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	
					// System.out.println("Output from Server .... \n");
					output = br.readLine();
					if(output==null)
						Thread.sleep(1000);
				}
				if (output.contains("Incorrect ID")) {
					stmt4.setLong(1, i);
					stmt4.executeUpdate();
					continue;
				} else if (output.contains("Too many requests")) {
					System.out.println("Too many reqs");
					Thread.sleep(1000 * 60 * 5);
				} else {
					// count++;
					JSONParser jsonParser = new JSONParser();
					JSONObject JSONoutput = (JSONObject) jsonParser.parse(output);
					JSONObject company = (JSONObject) JSONoutput.get("company");
					JSONObject employees = (JSONObject) company.get("employees");
					Set s = company.keySet();
					Iterator j = s.iterator();

//					query = "update torn_companies set creation_date = sysdate,";
//					while (j.hasNext()) {
//						String key = (String) j.next();
//						if ("employees".equals(key)) {
//							if (!j.hasNext())
//								query = query.substring(0, query.length() - 1);
//							continue;
//						}
//						// if ("name".equals(key)) {
////							query += key + "=\'";
////							String name = replaceSpecialChars((String) company.get(key));
////							query += name;
////							query += "\'";
////						} else
//						query += key + " = ?";// + company.get(key);
//
//						if (j.hasNext()) {
//							query += ",";
//						}
//					}
//					query += " where id =?";// + i;
//					stmt1 = con.prepareStatement(query);
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
					if (loopCount % 20 == 0)
						stmt1.executeBatch();
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
					companyEmployeesQuery = "insert into torn_company_employees values(?,?,?,?,?,sysdate)";// + i + ","
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
						// System.out.println("name = "+name+" position = "+position+" companyDays =
						// "+companyDays+" id = "+id+" desc ="+description+" relative = "+relative) ;
//						String[] spl = relative.split(" ago");
//						// if (spl.length < 2)
//						// continue;
//						String[] spl1 = spl[0].split(" ");
//						int size = spl1.length;
//
//						int lastActionTime = Integer.parseInt(spl1[size - 2]);
//						String lastActionUnit = spl1[size - 1];
//						String lAction = lastActionTime + " " + lastActionUnit + " ago";
//						companyEmployeesQuery = "insert into torn_company_employees values(?,?,?,?,?)";// + i + "," + id
//																										// + ",'"
//						// + name + "','" + position + "'," + companyDays + ")";
//
//						stmt1 = con.prepareStatement(companyEmployeesQuery);
						stmt3.setInt(1, i);
						stmt3.setInt(2, id);
						stmt3.setString(3, name);
						stmt3.setString(4, position);
						stmt3.setLong(5, companyDays);
						stmt3.addBatch();
						stmt3.clearParameters();
						executeLastAction(lastAction, id, description);
//						playerUpdateQuery = "update torn_players set last_action = '" + lAction
//								+ "', last_action_time =" + lastActionTime + ", LAST_ACTION_UNIT = '" + lastActionUnit
//								+ "', CREATION_DATE = sysdate, status = '" + description + "' where user_id = " + id;
						// System.out.println(companyEmployeesQuery);
						// System.out.println(playerUpdateQuery);

						// System.out.println(query);

						// stmt1.executeUpdate(companyEmployeesQuery);
						// stmt1.executeUpdate(playerUpdateQuery);
						// System.out.println(rs.getInt(1));
					}
					stmt3.executeBatch();
					playerUpdateStmt.executeBatch();
					stmt3.close();

				}
				// break;
			}
			stmt1.executeBatch();
			playerUpdateStmt.close();
		} catch (Exception e) {
			System.out.println(companyEmployeesQuery);
			System.out.println(playerUpdateQuery);
			System.out.println(query);
			System.out.println("Company Id = "+i);
			e.printStackTrace();
		} finally {
			stmt1.close();
		}
	}

	public static void addCompanyDetails() throws Exception {
		Thread.sleep(15 * 1000);
		int count = 0;
		int count1 = 0;
		int companyId = 0;
		ResultSet rs = statement.executeQuery("select max(ID) from TORN_COMPANIES");
		rs.next();
		companyId = rs.getInt(1);
		startTime = System.currentTimeMillis();
		/*
		 * while(rs.next()) { al.add(rs.getInt(1)); if(al.size()%1000==0)
		 * System.out.println("Added "+
		 * al.size()+" players in "+(System.currentTimeMillis()-startTime)/(1000)
		 * +" seconds"); } Collections.sort(al);
		 */

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
		String playerUpdateQuery = "update torn_players set last_action = ?, last_action_time =?, LAST_ACTION_UNIT = ?"
				+ ", CREATION_DATE = sysdate, status = ? where user_id = ?";
		playerUpdateStmt = con.prepareStatement(playerUpdateQuery);
		// maxCompanyId = companyId+1;
		try {
			for (int i = companyId + 1; i <= maxCompanyId; i++) {// 83301
				if (i % 20 == 0) {
					date = new Date();
					System.out.println("In Add company details, Processing company i= " + i + " count= "
							+ (count + count1) + " in " + (System.currentTimeMillis() - startTime) / (1000)
							+ " seconds. Current time is " + dtf.format(date));
				}
//				if (apiKey.equals("tbQgiQjG3KMGQQI8")) {
//					apiKey = "grxSMUkxonbgCTTg";
//					count1++;
//				} else {
//					apiKey = "tbQgiQjG3KMGQQI8";
//					count++;
//				}
				String output = null;
				while(output == null) {
					u = "https://api.torn.com/company/" + i + "?selections=profile&key=" + apiKey;
					count++;
					if (count % 80 == 0) {
						ct = (System.currentTimeMillis() - ot) / (1000);
						// System.out.println("count = "+count+" ct = "+ct);
						if (ct < 60) {
							System.out.println("Sleeping for " + (60 - ct) + " seconds");
							Thread.sleep((60 - ct) * 1000);
						}
						ot = System.currentTimeMillis();
	
					}
	
	//				if (apiKey.equals("grxSMUkxonbgCTTg") && count1 % 85 == 0 && count1 != 0) {
	//					ct1 = (System.currentTimeMillis() - ot1) / (1000);
	//					// System.out.println("count1 = "+count1+" ct1 = "+ct1);
	//					if (ct1 < 60) {
	//						System.out.println("Sleeping for " + (60 - ct) + " seconds");
	//						Thread.sleep((60 - ct1) * 1000);
	//					}
	//					ot1 = System.currentTimeMillis();
	//
	//				}
					long starttime = System.currentTimeMillis();
					URL url = new URL(u);
	
					HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setRequestProperty("Accept", "application/json");
					conn.setRequestProperty("User-Agent",
							"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
	
					if (conn.getResponseCode() != 200) {
						System.out.println("Failed : HTTP error code : " + conn.getResponseCode());
						i--;
						continue;
					}
	
					BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	
					// System.out.println("Output from Server .... \n");
					output = br.readLine();
					if(output==null)
						Thread.sleep(1000);
				}
				if (output.contains("Incorrect ID"))
					continue;
				else if (output.contains("Too many requests")) {
					System.out.println("Too many reqs");
					Thread.sleep(1000 * 60 * 5);
				} else {
					// count++;
					JSONParser jsonParser = new JSONParser();
					JSONObject JSONoutput = (JSONObject) jsonParser.parse(output);
					JSONObject company = (JSONObject) JSONoutput.get("company");
					JSONObject employees = (JSONObject) company.get("employees");
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
							if (!j.hasNext())
								query = query.substring(0, query.length() - 1);
							continue;
						}
						query += key;
						if (j.hasNext()) {
							query += ",";
						}
					}
					query += ") values (sysdate,?,?,?,?,?,?,?,?,?,?,?,?)";
					stmt1 = con.prepareStatement(query);
					j = s.iterator();
					int k = 1;
					while (j.hasNext()) {
						String key = (String) j.next();
						if ("employees".equals(key)) {
							if (!j.hasNext())
								query = query.substring(0, query.length() - 1);
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
					companyEmployeesQuery = "insert into torn_company_employees values(?,?,?,?,?,sysdate)";// + i + ","
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
						// System.out.println("name = "+name+" position = "+position+" companyDays =
						// "+companyDays+" id = "+id+" desc ="+description+" relative = "+relative) ;
//						String[] spl = relative.split(" ago");
//						// if (spl.length < 2)
//						// continue;
//						String[] spl1 = spl[0].split(" ");
//						int size = spl1.length;
//
//						int lastActionTime = Integer.parseInt(spl1[size - 2]);
//						String lastActionUnit = spl1[size - 1];
//						String lAction = lastActionTime + " " + lastActionUnit + " ago";
//						companyEmployeesQuery = "insert into torn_company_employees values(?,?,?,?,?)";// + i + "," + id
						// + ",'"
						// + name + "','" + position + "'," + companyDays + ")";

//						stmt1 = con.prepareStatement(companyEmployeesQuery);
						stmt1.setInt(1, i);
						stmt1.setInt(2, id);
						stmt1.setString(3, name);
						stmt1.setString(4, position);
						stmt1.setLong(5, companyDays);
						stmt1.addBatch();
						stmt1.clearParameters();
						executeLastAction(lastAction, id, description);
//						playerUpdateQuery = "update torn_players set last_action = '" + lAction
//								+ "', last_action_time =" + lastActionTime + ", LAST_ACTION_UNIT = '" + lastActionUnit
//								+ "', CREATION_DATE = sysdate, status = '" + description + "' where user_id = " + id;
//						// System.out.println(companyEmployeesQuery);
						// System.out.println(playerUpdateQuery);

						// System.out.println(query);

						// stmt.executeUpdate(companyEmployeesQuery);
						// stmt.executeUpdate(playerUpdateQuery);
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
			stmt1.close();
		}
	}

	public static void addNewPlayers() throws Exception {
		int count = 0;
		int count1 = 0;
		ResultSet rs = statement.executeQuery("select max(user_id) from TORN_PLAYERS");
		rs.next();
		int userId = rs.getInt(1);
		startTime = System.currentTimeMillis();
		/*
		 * while(rs.next()) { al.add(rs.getInt(1)); if(al.size()%1000==0)
		 * System.out.println("Added "+
		 * al.size()+" players in "+(System.currentTimeMillis()-startTime)/(1000)
		 * +" seconds"); } Collections.sort(al);
		 */

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
		String query = "insert into torn_players(user_id,last_action,last_action_time, last_action_unit, status, creation_date) values(?,?,?,?,?,sysdate)";
		stmt1 = con.prepareStatement(query);
		for (int i = userId + 1; i <= maxUserId; i++) {
			if (i % 200 == 0) {
				date = new Date();
				System.out.println("In Add new players, Processing user i= " + i + " count= " + (count + count1)
						+ " in " + (System.currentTimeMillis() - startTime) / (1000) + " seconds. Current time is "
						+ dtf.format(date));

			}
//			if (apiKey.equals("tbQgiQjG3KMGQQI8")) {
//				apiKey = "grxSMUkxonbgCTTg";
//				count1++;
//			} else {
//				apiKey = "tbQgiQjG3KMGQQI8";
//				count++;
//			}
			String output = null;
			while(output == null) {
				u = "https://api.torn.com/user/" + i + "?selections=profile,personalstats,crimes&key=" + apiKey;
				count++;
				// System.out.println(u);
				if (count % 80 == 0) {
					ct = (System.currentTimeMillis() - ot) / (1000);
					// System.out.println("count = "+count+" ct = "+ct);
					if (ct < 60) {
					//	 System.out.println("Sleeping for " + (60 - ct) + " seconds");
						Thread.sleep((60 - ct) * 1000);
					}
					ot = System.currentTimeMillis();
	
				}
	
	//			if (apiKey.equals("grxSMUkxonbgCTTg") && count1 % 85 == 0 && count1 != 0) {
	//				ct1 = (System.currentTimeMillis() - ot1) / (1000);
	//				// System.out.println("count1 = "+count1+" ct1 = "+ct1);
	//				if (ct1 < 60) {
	//					System.out.println("Sleeping for " + (60 - ct) + " seconds");
	//					Thread.sleep((60 - ct1) * 1000);
	//				}
	//				ot1 = System.currentTimeMillis();
	//
	//			}
	
				long starttime = System.currentTimeMillis();
				URL url = new URL(u);
	
				HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");
				conn.setRequestProperty("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
	
				if (conn.getResponseCode() != 200) {
					System.out.println("Failed : HTTP error code : " + conn.getResponseCode());
					i--;
					continue;
				}
	
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	
				// System.out.println("Output from Server .... \n");
				output = br.readLine();
				if(output==null)
					Thread.sleep(1000);
			}
			if (output.contains("Incorrect ID"))
				continue;
			else if (output.contains("Too many requests")) {
				System.out.println("Too many reqs");
				Thread.sleep(1000 * 60 * 5);
			} else if (!output.contains("NPC")) {
				JSONParser jsonParser = new JSONParser();
				JSONObject JSONoutput = (JSONObject) jsonParser.parse(output);
				JSONObject lastAction = (JSONObject) JSONoutput.get("last_action");
				JSONObject criminalRecord = (JSONObject) JSONoutput.get("criminalrecord");
				JSONObject competition = (JSONObject) JSONoutput.get("competition");
				if(competition.get("score")==null)
					competition = null;
				ArrayList al = getProfileStats(JSONoutput, criminalRecord, competition);
				if (lastAction == null) {
					i--;
					continue;
				}
				JSONObject statusObject = (JSONObject) JSONoutput.get("status");
				String action = (String) lastAction.get("relative");
				if ("No Last Action".equals(action))
					continue;
				String status = (String) statusObject.get("description");
				String[] spl = action.split(" ago");
				// if (spl.length < 2)
				// continue;
				String[] spl1 = spl[0].split(" ");
				int size = spl1.length;

				int lastActionTime = Integer.parseInt(spl1[size - 2]);
				String lastActionUnit = spl1[size - 1];
				String lAction = lastActionTime + " " + lastActionUnit + " ago";
				// String query = "insert into
				// torn_players(user_id,last_action,last_action_time, last_action_unit, status,
				// creation_date) values(?,?,?,?,?,sysdate)";
//						+ i + ",'" + lAction + "'," + lastActionTime + ",'" + lastActionUnit + "','" + status
//						+ "',sysdate)";
//				stmt1 = con.prepareStatement(query);
				stmt1.setInt(1, i);
				stmt1.setString(2, lAction);
				stmt1.setInt(3, lastActionTime);
				stmt1.setString(4, lastActionUnit);
				stmt1.setString(5, status);
				stmt1.addBatch();
				stmt1.clearParameters();
				if (i % 20 == 0)
					stmt1.executeBatch();
				// System.out.println(query);
				// stmt.executeUpdate(query);
				JSONObject personalStats = (JSONObject) JSONoutput.get("personalstats");
				String role = (String) JSONoutput.get("role");
				if (!role.contains("NPC")) {
					if (personalStats == null)
						continue;
					String statQuery = formQuery(personalStats, i, al);
					// stmt.executeUpdate(statQuery);
				}
			}

		}
		stmt1.executeBatch();
		stmt1.close();
		// con.close();
	}

	public static String formQuery(JSONObject personalStats, int user_id, ArrayList al) {
		//System.out.println("al.size = "+al.size());
		String query = "";
		int c = 0;
		try {

			ResultSet rs = statement1.executeQuery("select count(*) from torn_player_stats where userid = " + user_id);
			rs.next();
			int count = rs.getInt(1);
			if (count == 0) {
				Set keys = personalStats.keySet();
				Iterator j = keys.iterator();
				if(al.size()>22) 
					query = "insert into torn_player_stats(creationdate,userid,rank,USER_LEVEL,GENDER,PROPERTY,FRIENDS,ENEMIES,FORUM_POSTS,KARMA,AGE,ROLE,DONATOR,NAME,PROPERTY_ID,selling_illegal_products,theft,auto_theft,drug_deals,computer_crimes,murder,fraud_crimes,other_crimes,total_crimes,competition,score,team,attacks,";
				else
					query = "insert into torn_player_stats(creationdate,userid,rank,USER_LEVEL,GENDER,PROPERTY,FRIENDS,ENEMIES,FORUM_POSTS,KARMA,AGE,ROLE,DONATOR,NAME,PROPERTY_ID,selling_illegal_products,theft,auto_theft,drug_deals,computer_crimes,murder,fraud_crimes,other_crimes,total_crimes,";
				while (j.hasNext()) {
					String key = (String) j.next();
					query += key;
					c++;
					if (j.hasNext()) {
						query += ",";
					}
				}
				if(al.size()>22) 
					query += ") values (sysdate,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,";
				else
					query += ") values (sysdate,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,";
				for (int i = 0; i <= c; i++) {
					query += "?";
					if (!(i == c))
						query += ",";
				}
				query += ")";
				// System.out.println(query);
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
				personalStatsStmt.setLong(17, (Long) al.get(15));
				personalStatsStmt.setLong(18, (Long) al.get(16));
				personalStatsStmt.setLong(19, (Long) al.get(17));
				personalStatsStmt.setLong(20, (Long) al.get(18));
				personalStatsStmt.setLong(21, (Long) al.get(19));
				personalStatsStmt.setLong(22, (Long) al.get(20));
				personalStatsStmt.setLong(23, (Long) al.get(21));
				if(al.size()>22) {
					personalStatsStmt.setString(24, (String) al.get(22));
					if(al.get(23)!=null)
						personalStatsStmt.setLong(25, (Long) al.get(23));
					else
						personalStatsStmt.setNull(25, Types.NULL);
					if(al.get(24)!=null)
						personalStatsStmt.setString(26, (String) al.get(24));
					else
						personalStatsStmt.setNull(25, Types.NULL);
					if(al.get(25)!=null)
						personalStatsStmt.setLong(27, (Long) al.get(25));
					else
						personalStatsStmt.setNull(25, Types.NULL);
				}
				int i = 0;
				if(al.size()>22) {
					i = 28;
				}
				else
					i = 24;
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
					query += key + " = ?";// + personalStats.get(key);
					if (j.hasNext()) {
						query += ",";
					}
				}
				if(al.size()>22)
					query += ",rank=?,USER_LEVEL=?,GENDER=?,PROPERTY=?,FRIENDS=?,ENEMIES=?,FORUM_POSTS=?,KARMA=?,AGE=?,ROLE=?,DONATOR=?,NAME=?,PROPERTY_ID=?,selling_illegal_products=?,theft=?,auto_theft=?,drug_deals=?,computer_crimes=?,murder=?,fraud_crimes=?,other_crimes=?,total_crimes=?,competition=?,score=?,team=?,attacks=? where userid =?";// +
				else
					query += ",rank=?,USER_LEVEL=?,GENDER=?,PROPERTY=?,FRIENDS=?,ENEMIES=?,FORUM_POSTS=?,KARMA=?,AGE=?,ROLE=?,DONATOR=?,NAME=?,PROPERTY_ID=?,selling_illegal_products=?,theft=?,auto_theft=?,drug_deals=?,computer_crimes=?,murder=?,fraud_crimes=?,other_crimes=?,total_crimes=? where userid =?";// +
				personalStatsStmt = con.prepareStatement(query);
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
				personalStatsStmt.setLong(i + 15, (Long) al.get(15));
				personalStatsStmt.setLong(i + 16, (Long) al.get(16));
				personalStatsStmt.setLong(i + 17, (Long) al.get(17));
				personalStatsStmt.setLong(i + 18, (Long) al.get(18));
				personalStatsStmt.setLong(i + 19, (Long) al.get(19));
				personalStatsStmt.setLong(i + 20, (Long) al.get(20));
				personalStatsStmt.setLong(i + 21, (Long) al.get(21));
				if(al.size()>22) {
					personalStatsStmt.setString(i + 22, (String) al.get(22));
					if(al.get(23)!=null)
						personalStatsStmt.setLong(i + 23, (Long) al.get(23));
					else
						personalStatsStmt.setNull(i + 23, Types.NULL);
					if(al.get(24)!=null)
						personalStatsStmt.setString(i + 24, (String) al.get(24));
					else
						personalStatsStmt.setNull(i + 24, Types.NULL);
					if(al.get(25)!=null)
						personalStatsStmt.setLong(i + 25, (Long) al.get(25));
					else
						personalStatsStmt.setNull(i + 25, Types.NULL);
					
					personalStatsStmt.setInt(i + 26, user_id);

				}
				else
					personalStatsStmt.setInt(i + 22, user_id);
				personalStatsStmt.executeUpdate();
				personalStatsStmt.close();
			}
			// System.out.println(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return query;
	}

	public static void updateLastAction(int scanAll) throws Exception {
		al.clear();
		int i = 0;
		try {
			int count = 0;
			int count1 = 0;
			int loopCount = 0;
			String originalApiKey = apiKey;
			// ResultSet rs = statement.executeQuery("select user_id from torn_players where
			// creation_date < sysdate-5");
			ResultSet rs = statement.executeQuery(
					"select torn_players.user_id from TORN_PLAYERS where not (LAST_ACTION_UNIT = 'days' and LAST_ACTION_TIME>61) minus\n"
							+ "select user_id from torn_players where creation_date > SYSDATE - 2");
//			scanAll = 1;
//			if("tbQgiQjG3KMGQQI8".equals(apiKey))
//				rs = statement.executeQuery(
//						"select user_id from torn_players where creation_date < sysdate -61 and user_id <= 2342429");
//			else
//				rs = statement.executeQuery(
//						"select user_id from torn_players where creation_date < sysdate -61 and user_id >= 2321607");
			/*
			 * ResultSet rs = statement.
			 * executeQuery("select user_id from torn_players where user_id in (1517767,\n"
			 * + "2107082,\n" + "2124935,\n" + "2110398,\n" + "2110398,\n" + "2012366,\n" +
			 * "1007583,\n" + "1657096,\n" + "970300,\n" + "2119716,\n" + "2374710,\n" +
			 * "1458409,\n" + "2045217,\n" + "2214419,\n" + "1754243,\n" + "1623767,\n" +
			 * "2011520,\n" + "2011520,\n" + "2222379,\n" + "2034939,\n" + "2034939,\n" +
			 * "2132390)");
			 */

			// rs.next();
			// int userId = rs.getInt(1);
			startTime = System.currentTimeMillis();

			while (rs.next()) {
				al.add(rs.getInt(1));
				if (al.size() % 1000 == 0)
					System.out.println("Added " + al.size() + " players in "
							+ (System.currentTimeMillis() - startTime) / (1000) + " seconds");
			}
			rs.close();
			Collections.sort(al);
			sub1 = al;
			if(scanAll == 0) {
				int x = al.size() / 2;
				// System.out.println("x= "+x);
				if ("tbQgiQjG3KMGQQI8".equals(apiKey))
					sub1 = al.subList(x, al.size());
				else
					sub1 = al.subList(0, x);
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
			// for (int i = userId + 1; i < 2477321; i++) {
			// System.out.println("userid = " + userId);
			// for (int i = userId + 1; i <= maxUserId; i++) {
			String playerUpdateQuery = "update torn_players set last_action = ?, last_action_time =?, LAST_ACTION_UNIT = ?"
					+ ", CREATION_DATE = sysdate, status = ? where user_id = ?";
			
			while (it.hasNext()) {
				loopCount++;
				i = (Integer) it.next();
				if(playerUpdateStmt ==null || playerUpdateStmt.isClosed())
					playerUpdateStmt = con.prepareStatement(playerUpdateQuery);
				if (i % 200 == 0) {
					date = new Date();
					System.out.println("In update last action, Processing user i= " + i + " count= " + (count + count1)
							+ "  in " + (System.currentTimeMillis() - startTime) / (1000) + " seconds. Current time is "
							+ dtf.format(date));
				}
//				if (apiKey.equals("tbQgiQjG3KMGQQI8")) {
//					apiKey = "grxSMUkxonbgCTTg";
//					count1++;
//				} else {
//					apiKey = "tbQgiQjG3KMGQQI8";
//					count++;
//				}
				if (i == 2481851)
					apiKey = "tbQgiQjG3KMGQQI8";
				else if (i == 2398000)
					apiKey = "grxSMUkxonbgCTTg";
				else
					apiKey = originalApiKey;
				String output = null;
				while(output == null) {
					u = "https://api.torn.com/user/" + i + "?selections=profile,personalstats,crimes&key=" + apiKey;
					count++;
					// System.out.println(u);
					if (count % 80 == 0) {
						ct = (System.currentTimeMillis() - ot) / (1000);
						// System.out.println("count = "+count+" ct = "+ct);
						if (ct < 60) {
						//	 System.out.println("Sleeping for " + (60 - ct) + " seconds");
							Thread.sleep((60 - ct) * 1000);
						}
						ot = System.currentTimeMillis();
	
					}
	
	//				if (apiKey.equals("grxSMUkxonbgCTTg") && count1 % 85 == 0 && count1 != 0) {
	//					ct1 = (System.currentTimeMillis() - ot1) / (1000);
	//					// System.out.println("count1 = "+count1+" ct1 = "+ct1);
	//					if (ct1 < 60) {
	//						System.out.println("Sleeping for " + (60 - ct) + " seconds");
	//						Thread.sleep((60 - ct1) * 1000);
	//					}
	//					ot1 = System.currentTimeMillis();
	//
	//				}
	
					long starttime = System.currentTimeMillis();
					URL url = new URL(u);
	
					HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setRequestProperty("Accept", "application/json");
					conn.setRequestProperty("User-Agent",
							"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
	
					if (conn.getResponseCode() != 200) {
						System.out.println("Failed : HTTP error code : " + conn.getResponseCode());
						i--;
						continue;
					}
	
					BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	
					// System.out.println("Output from Server .... \n");
					output = br.readLine();
					if(output==null)
						Thread.sleep(1000);
				}
				if (output.contains("Incorrect ID"))
					continue;
				else if (output.contains("Too many requests")) {
					System.out.println("Too many reqs");
					Thread.sleep(1000 * 60 * 5);
				} else {
					JSONParser jsonParser = new JSONParser();
					JSONObject JSONoutput = (JSONObject) jsonParser.parse(output);
					JSONObject personalStats = (JSONObject) JSONoutput.get("personalstats");
					JSONObject criminalRecord = (JSONObject) JSONoutput.get("criminalrecord");
					JSONObject competition = (JSONObject) JSONoutput.get("competition");
					if(competition.get("score")==null)
						competition = null;
					ArrayList al = getProfileStats(JSONoutput,criminalRecord,competition);
					String role = (String) JSONoutput.get("role");
					if (role != null && role.contains("NPC"))
						continue;
					JSONObject lastAction = (JSONObject) JSONoutput.get("last_action");
					if (lastAction == null) {
						// i--;
						continue;
					}
					JSONObject statusObject = (JSONObject) JSONoutput.get("status");
					String action = (String) lastAction.get("relative");
					if ("No Last Action".equals(action))
						continue;
					String description = (String) statusObject.get("description");
//					String[] spl = action.split(" ago");
//					// if (spl.length < 2)
//					// continue;
//					String[] spl1 = spl[0].split(" ");
//					int size = spl1.length;
//
//					int lastActionTime = Integer.parseInt(spl1[size - 2]);
//					String lastActionUnit = spl1[size - 1];
//					String lAction = lastActionTime + " " + lastActionUnit + " ago";
//					String playerUpdateQuery = "update torn_players set last_action = '" + lAction
//							+ "', last_action_time =" + lastActionTime + ", LAST_ACTION_UNIT = '" + lastActionUnit
//							+ "', CREATION_DATE = sysdate, status = '" + description + "' where user_id = " + i;
//
//					// System.out.println(query);
//					stmt1.executeUpdate(playerUpdateQuery);
					executeLastAction(lastAction, i, description);
					//System.out.println("loopCount = "+loopCount);
					if (loopCount % 50 == 0) {
					//	System.out.println("Commiting...");
						playerUpdateStmt.executeBatch();
						playerUpdateStmt.close();
					}
					if (!role.contains("NPC")) {
						if (personalStats == null)
							continue;
						String statQuery = formQuery(personalStats, i, al);
						//stmt1.executeUpdate(statQuery);
					}
				}
				
			}
			playerUpdateStmt.executeBatch();
			playerUpdateStmt.close();
		} catch (Exception e) {
			System.out.println("For user: "+i);
			e.printStackTrace();
		}

	}

	public static void updateFactionDetails() throws Exception {
		int count = 0;
		int count1 = 0;
		int factionId = 0;
		int loopCount = 0;
		ResultSet rs = statement.executeQuery("select id from torn_factions");

		// rs.next();
		// companyId = rs.getInt(1);
		startTime = System.currentTimeMillis();
		/*
		 * while(rs.next()) { al.add(rs.getInt(1)); if(al.size()%1000==0)
		 * System.out.println("Added "+
		 * al.size()+" players in "+(System.currentTimeMillis()-startTime)/(1000)
		 * +" seconds"); } Collections.sort(al);
		 */

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
		query = "update torn_factions set creation_date = sysdate,name =?,leader = ?,coleader = ?"
				+ ",respect = ?, age = ?,best_chain=? where id =?";
		playerUpdateQuery = "update torn_players set last_action = ?, last_action_time =?, LAST_ACTION_UNIT = ?"
				+ ", CREATION_DATE = sysdate, status = ? where user_id = ?";
		
		int i = 0;
		try {

			// for (int i = 4209; i < 4210; i++) {//83301
			while (rs.next()) {
				loopCount++;
				i = rs.getInt(1);
				stmt1 = con.prepareStatement(query);
				stmt2 = con.prepareStatement("delete torn_faction_members where faction_id=?");
				stmt4 = con.prepareStatement("delete torn_factions where id = ?");
				playerUpdateStmt = con.prepareStatement(playerUpdateQuery);
				// System.out.println(i);
				if (i % 20 == 0) {
					date = new Date();
					System.out.println("In update faction details, Processing faction i= " + i + " count= "
							+ (count + count1) + " in " + (System.currentTimeMillis() - startTime) / (1000)
							+ " seconds. Current time is " + dtf.format(date));
				}
//				if (apiKey.equals("tbQgiQjG3KMGQQI8")) {
//					apiKey = "grxSMUkxonbgCTTg";
//					count1++;
//				} else {
//					apiKey = "tbQgiQjG3KMGQQI8";
//					count++;
//				}
				String output = null;
				while(output == null) {
					u = "https://api.torn.com/faction/" + i + "?selections=basic&key=" + apiKey;
					count++;
					if (count % 80 == 0) {
						ct = (System.currentTimeMillis() - ot) / (1000);
						// System.out.println("count = "+count+" ct = "+ct);
						if (ct < 60) {
							// System.out.println("Sleeping for " + (60 - ct) + " seconds");
							Thread.sleep((60 - ct) * 1000);
						}
						ot = System.currentTimeMillis();
	
					}
	
	//				if (apiKey.equals("grxSMUkxonbgCTTg") && count1 % 85 == 0 && count1 != 0) {
	//					ct1 = (System.currentTimeMillis() - ot1) / (1000);
	//					// System.out.println("count1 = "+count1+" ct1 = "+ct1);
	//					if (ct1 < 60) {
	//						System.out.println("Sleeping for " + (60 - ct) + " seconds");
	//						Thread.sleep((60 - ct1) * 1000);
	//					}
	//					ot1 = System.currentTimeMillis();
	//
	//				}
					// System.out.println(rs.getInt(1));
					long starttime = System.currentTimeMillis();
					URL url = new URL(u);
	
					HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setRequestProperty("Accept", "application/json");
					conn.setRequestProperty("User-Agent",
							"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
	
					if (conn.getResponseCode() != 200) {
						System.out.println("Failed : HTTP error code : " + conn.getResponseCode());
						// i--;
						continue;
					}
	
					BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	
					// System.out.println("Output from Server .... \n");
					output = br.readLine();
					if(output==null)
						Thread.sleep(1000);
				}
				if (output.contains("Incorrect ID"))
					continue;
				else if (output.contains("Too many requests")) {
					System.out.println("Too many reqs");
					Thread.sleep(1000 * 60 * 5);
				} else {
					// count++;
					JSONParser jsonParser = new JSONParser();
					JSONObject JSONoutput = (JSONObject) jsonParser.parse(output);
					JSONObject members = (JSONObject) JSONoutput.get("members");
					if (members.isEmpty()) {
						stmt4.setLong(1, i);
						stmt4.executeUpdate();
						stmt4.close();
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
//					query = "update torn_factions set creation_date = sysdate,name =?,leader = ?,coleader = ?"
//							+ ",respect = ?, age = ?,best_chain=? where id =?";
//					query = "update torn_factions set creation_date = sysdate,name ='"+name+"',leader = "+leader+",coleader = "
//							+ coleader +",respect = "+respect+", age = "+age+",best_chain = "+best_chain;
//					
//					query += " where id =" + i;
//					stmt1 = con.prepareStatement(query);
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
						stmt1.executeBatch();
						stmt1.close();
					}
					// System.out.println(rs.getInt(1));
//					stmt1 = con.prepareStatement("delete torn_faction_members where faction_id=?");//" + i);
					stmt2.setInt(1, i);
					stmt2.executeUpdate();
					stmt2.close();
					// System.out.println(rs.getInt(1));
					int userId = 0;
					long factionDays = 0;
					Set e = members.entrySet();
					Iterator ei = e.iterator();
					// System.out.println("set size ="+e.size());
					factionMembersQuery = "insert into torn_faction_members values(?,?,?,sysdate)";// + i + "," +
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
						if (status == null)
							continue;
						// String relative = (String) lastAction.get("relative");
						String description = (String) status.get("description");

						factionDays = (Long) memDetails.get("days_in_faction");
						// System.out.println("name = "+name+" position = "+position+" companyDays =
//						// "+companyDays+" id = "+id+" desc ="+description+" relative = "+relative) ;
//						String[] spl = relative.split(" ago");
//						// if (spl.length < 2)
//						// continue;
//						String[] spl1 = spl[0].split(" ");
//						int size = spl1.length;
//
//						int lastActionTime = Integer.parseInt(spl1[size - 2]);
//						String lastActionUnit = spl1[size - 1];
//						String lAction = lastActionTime + " " + lastActionUnit + " ago";
//						factionMembersQuery = "insert into torn_faction_members values(?,?,?,sysdate)";// + i + "," +
//																										// userId + ","
//						// + factionDays + ",sysdate)";
//						stmt1 = con.prepareStatement(factionMembersQuery);
						stmt3.setInt(1, i);
						stmt3.setInt(2, userId);
						stmt3.setLong(3, factionDays);

						stmt3.addBatch();
						stmt3.clearParameters();
						executeLastAction(lastAction, userId, description);
//						playerUpdateQuery = "update torn_players set last_action = '" + lAction
//								+ "', last_action_time =" + lastActionTime + ", LAST_ACTION_UNIT = '" + lastActionUnit
//								+ "', CREATION_DATE = sysdate, status = '" + description + "' where user_id = " + userId;
//						// System.out.println(companyEmployeesQuery);
						// System.out.println(playerUpdateQuery);

						// System.out.println(query);

//						stmt1.executeUpdate(factionMembersQuery);
//						stmt1.executeUpdate(playerUpdateQuery);
						// System.out.println(rs.getInt(1));
						
					}
					stmt3.executeBatch();
					playerUpdateStmt.executeBatch();
					stmt3.close();
					
				}
				playerUpdateStmt.close();
			}
			stmt1.executeBatch();
			playerUpdateStmt.close();
		} catch (Exception e) {
			System.out.println("For faction " + i);
			e.printStackTrace();

		} finally {
			if(stmt1 != null && !stmt1.isClosed())
				stmt1.close();
			if(stmt2 != null && !stmt2.isClosed())
				stmt2.close();
			if(stmt4 != null && !stmt4.isClosed())
				stmt4.close();
			if(playerUpdateStmt != null && !playerUpdateStmt.isClosed())
				playerUpdateStmt.close();
			if(stmt3 != null && !stmt3.isClosed())
				stmt3.close();
		}
	}

	public static void addFactionDetails() throws Exception {
		Thread.sleep(15 * 1000);
		int count = 0;
		int count1 = 0;
		int factionId = 0;
		int loopCount = 0;
		ResultSet rs = statement.executeQuery("select max(ID) from torn_factions");
		rs.next();
		factionId = rs.getInt(1);
		startTime = System.currentTimeMillis();
		/*
		 * while(rs.next()) { al.add(rs.getInt(1)); if(al.size()%1000==0)
		 * System.out.println("Added "+
		 * al.size()+" players in "+(System.currentTimeMillis()-startTime)/(1000)
		 * +" seconds"); } Collections.sort(al);
		 */

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
			playerUpdateQuery = "update torn_players set last_action = ?, last_action_time =?, LAST_ACTION_UNIT = ?"
					+ ", CREATION_DATE = sysdate, status = ? where user_id = ?";
			playerUpdateStmt = con.prepareStatement(playerUpdateQuery);
			query = "insert into torn_factions(id,name,leader,coleader,respect,age,best_chain,creation_date) values(?,?,?,?,?,?,?,sysdate)";
			// +
			// id+",'"+name+"',"+leader+","+coleader+","+respect+","+age+","+best_chain+",sysdate)";
			stmt1 = con.prepareStatement(query);
			for (int i = factionId + 1; i <= maxFactionId; i++) {// 83301
				if (i % 20 == 0) {
					date = new Date();
					System.out.println("In Add faction details, Processing faction i= " + i + " count= "
							+ (count + count1) + " in " + (System.currentTimeMillis() - startTime) / (1000)
							+ " seconds. Current time is " + dtf.format(date));
				}
//				if (apiKey.equals("tbQgiQjG3KMGQQI8")) {
//					apiKey = "grxSMUkxonbgCTTg";
//					count1++;
//				} else {
//					apiKey = "tbQgiQjG3KMGQQI8";
//					count++;
//				}
				String output = null;
				while(output == null) {
					u = "https://api.torn.com/faction/" + i + "?selections=basic&key=" + apiKey;
					count++;
					if (count % 80 == 0) {
						ct = (System.currentTimeMillis() - ot) / (1000);
						// System.out.println("count = "+count+" ct = "+ct);
						if (ct < 60) {
							System.out.println("Sleeping for " + (60 - ct) + " seconds");
							Thread.sleep((60 - ct) * 1000);
						}
						ot = System.currentTimeMillis();
	
					}
	
	//				if (apiKey.equals("grxSMUkxonbgCTTg") && count1 % 85 == 0 && count1 != 0) {
	//					ct1 = (System.currentTimeMillis() - ot1) / (1000);
	//					// System.out.println("count1 = "+count1+" ct1 = "+ct1);
	//					if (ct1 < 60) {
	//						System.out.println("Sleeping for " + (60 - ct) + " seconds");
	//						Thread.sleep((60 - ct1) * 1000);
	//					}
	//					ot1 = System.currentTimeMillis();
	//
	//				}
					long starttime = System.currentTimeMillis();
					URL url = new URL(u);
	
					HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setRequestProperty("Accept", "application/json");
					conn.setRequestProperty("User-Agent",
							"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
	
					if (conn.getResponseCode() != 200) {
						System.out.println("Failed : HTTP error code : " + conn.getResponseCode());
						i--;
						continue;
					}
	
					BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	
					// System.out.println("Output from Server .... \n");
					output = br.readLine();
					if(output==null)
						Thread.sleep(1000);
				}
				if (output.contains("Incorrect ID"))
					continue;
				else if (output.contains("Too many requests")) {
					System.out.println("Too many reqs");
					Thread.sleep(1000 * 60 * 5);
				} else {
					// count++;
					JSONParser jsonParser = new JSONParser();
					JSONObject JSONoutput = (JSONObject) jsonParser.parse(output);
					JSONObject members = (JSONObject) JSONoutput.get("members");
					Long id = (Long) JSONoutput.get("ID");
					if (id == null)
						continue;
					String name = (String) JSONoutput.get("name");
					name = replaceSpecialChars(name);
					long leader = (Long) JSONoutput.get("leader");
					long coleader = (Long) JSONoutput.get("co-leader");
					long respect = (Long) JSONoutput.get("respect");
					long age = (Long) JSONoutput.get("age");
					long best_chain = (Long) JSONoutput.get("best_chain");

//					query = "insert into torn_factions(id,name,leader,coleader,respect,age,best_chain,creation_date) values(?,?,?,?,?,?,?,sysdate)";
//					// +
//					// id+",'"+name+"',"+leader+","+coleader+","+respect+","+age+","+best_chain+",sysdate)";
//					stmt1 = con.prepareStatement(query);
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
					if (loopCount % 20 == 0)
						stmt1.executeBatch();
					// System.out.println(query);
					// stmt.executeUpdate(query);
					int userId = 0;
					long factionDays = 0;
					Set e = members.entrySet();
					Iterator ei = e.iterator();
					// System.out.println("set size ="+e.size());
					factionMembersQuery = "insert into torn_faction_members values(?,?,?,sysdate)";
					stmt2 = con.prepareStatement(factionMembersQuery);
					while (ei.hasNext()) {
						Map.Entry map = (Map.Entry) ei.next();
						// System.out.println(map.getKey());// +" = "+map.getValue());
						userId = Integer.parseInt((String) map.getKey());
						// System.out.println(userId);
						JSONObject memDetails = (JSONObject) members.get(map.getKey());
						JSONObject lastAction = (JSONObject) memDetails.get("last_action");
						JSONObject status = (JSONObject) memDetails.get("status");
						if (status == null)
							continue;
						String description = (String) status.get("description");

						factionDays = (Long) memDetails.get("days_in_faction");
						// System.out.println("name = "+name+" position = "+position+" companyDays =
						// "+companyDays+" id = "+id+" desc ="+description+" relative = "+relative) ;
//						String relative = (String) lastAction.get("relative");
//						
//						
//						String[] spl = relative.split(" ago");
//						// if (spl.length < 2)
//						// continue;
//						String[] spl1 = spl[0].split(" ");
//						int size = spl1.length;
//
//						int lastActionTime = Integer.parseInt(spl1[size - 2]);
//						String lastActionUnit = spl1[size - 1];
//						String lAction = lastActionTime + " " + lastActionUnit + " ago";
						// factionMembersQuery = "insert into torn_faction_members
						// values(?,?,?,sysdate)";// + id + "," +
						// userId + ","
						// + factionDays + ",sysdate)";
//						playerUpdateQuery = "update torn_players set last_action = '" + lAction
//								+ "', last_action_time =" + lastActionTime + ", LAST_ACTION_UNIT = '" + lastActionUnit
//								+ "', CREATION_DATE = sysdate, status = '" + description + "' where user_id = " + userId;
						// System.out.println(factionMembersQuery);
						// System.out.println(playerUpdateQuery);

						// System.out.println(query);
						// stmt1 = con.prepareStatement(factionMembersQuery);
						stmt2.setLong(1, id);
						stmt2.setInt(2, userId);
						stmt2.setLong(3, factionDays);
						stmt2.executeUpdate();
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

	public static void executeLastAction(JSONObject lastAction, int userId, String status) throws Exception {
		String relative = (String) lastAction.get("relative");
		if ("No Last Action".equals(relative))
			return;
		String[] spl = relative.split(" ago");
		// if (spl.length < 2)
		// continue;
		String[] spl1 = spl[0].split(" ");
		int size = spl1.length;

		int lastActionTime = Integer.parseInt(spl1[size - 2]);
		String lastActionUnit = spl1[size - 1];
		String lAction = lastActionTime + " " + lastActionUnit + " ago";
		// String playerUpdateQuery = "update torn_players set last_action = ?,
		// last_action_time =?, LAST_ACTION_UNIT = ?"
		// + ", CREATION_DATE = sysdate, status = ? where user_id = ?";
		// stmt1 = con.prepareStatement(playerUpdateQuery);
		playerUpdateStmt.setString(1, lAction);
		playerUpdateStmt.setInt(2, lastActionTime);
		playerUpdateStmt.setString(3, lastActionUnit);
		playerUpdateStmt.setString(4, status);
		playerUpdateStmt.setInt(5, userId);

		playerUpdateStmt.addBatch();
		
		
	}

	public static ArrayList getProfileStats(JSONObject JSONoutput, JSONObject criminalRecord, JSONObject competition) {
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
		
		if(criminalRecord != null) {
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
			
		}
		

		return al;
	}

}
