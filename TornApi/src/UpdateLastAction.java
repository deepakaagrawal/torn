import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.net.ssl.HttpsURLConnection;
import java.net.URL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class UpdateLastAction implements Runnable{
    static Connection con = null;
    static Statement stmt = null;
    static int activeProfiles = 0;
    static long startTime = 0;
    static long currentTime = 0;
    static int apiHit = 0;
    static String apiKey = "JdyE2o33m3tl05IL";
    static int userProcessed = 0;
    int startUserId = 0;
    int endUserId = 0;  
    static ArrayList al = new ArrayList(); 
    static ArrayList errored = new ArrayList();
    static ArrayList multi = new ArrayList();
    static final String hostname = "mumbai51408.bom2.fusionappsdbom1.oraclevcn.com";
	static final int port = 1522;
	static final String sid = "mumbai51";
    
    //  static QuantityTreeManager qtam = null;
    
    public UpdateLastAction(int start, int end){
        this.startUserId = start;
        this.endUserId = end;
    }
    public void run() {
        try {
            int userId = ((Integer)al.get(startUserId)).intValue();

            while (startUserId <= endUserId) {
            //    Thread.sleep(220);
                if(al.contains(userId)){
                boolean active = parseProfile(userId, "profile", apiKey,"Update");
                if (userId > 999 && userId % 100 == 0) {
                    long currentTime = System.currentTimeMillis();
                    long diff = currentTime - startTime;
                    diff = diff / 1000;
                    int day = (int)TimeUnit.SECONDS.toDays(diff);
                    long hours = TimeUnit.SECONDS.toHours(diff) - (day * 24);
                    long minute = TimeUnit.SECONDS.toMinutes(diff) - (TimeUnit.SECONDS.toHours(diff) * 60);
                    long second = TimeUnit.SECONDS.toSeconds(diff) - (TimeUnit.SECONDS.toMinutes(diff) * 60);
                    System.out.println("...." + userProcessed + " users processed..." + activeProfiles + " active users found..." + "api hit = " + apiHit + "..." + day + " Days " + hours +
                                       " Hours " + minute + " minute " + second + " Seconds elapsed, user id =" + userId);
                }
            }
                userId = ((Integer)al.get(++startUserId)).intValue();

            }
            System.out.println("Thread "+Thread.currentThread().getName()+" completed**********************************************************");
        } catch (Exception e) {
            e.printStackTrace();
        }
      
    }

    public static void main(String[] args) {
        // TODO code application logic here
        try {
        	Properties systemProperties = System.getProperties();
			systemProperties.setProperty("https.proxyHost", "www-proxy.us.oracle.com");
			systemProperties.setProperty("https.proxyPort", "80");
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection("jdbc:oracle:thin:@"+hostname+":"+port+":"+sid, "fusion","fusion");
			stmt = con.createStatement();
            ResultSet rs=stmt.executeQuery("select user_id from TORN_PLAYERS where CREATION_DATE < SYSDATE - 1");
            startTime = System.currentTimeMillis();
            
            while(rs.next()) {
                al.add(rs.getInt(1));
                if(al.size()%100000==0)
                   System.out.println("Added "+ al.size()+" players in "+(System.currentTimeMillis()-startTime)/(1000)+" seconds");
            }
            Collections.sort(al);
            int x = al.size()/100;
            

    //            OnhandUtil obj = new OnhandUtil();
    //            Thread t = new Thread(obj);
//    Runnable        r1      =       new     TornUtil(1      ,       20000);
//    Runnable        r2      =       new     TornUtil(20001  ,       40000);
//    Runnable        r3      =       new     TornUtil(40001  ,       60000);
//    Runnable        r4      =       new     TornUtil(60001  ,       80000);
//    Runnable        r5      =       new     TornUtil(80001  ,       100000);
//    Runnable        r6      =       new     TornUtil(100001 ,       120000);
//    Runnable        r7      =       new     TornUtil(120001 ,       140000);
//    Runnable        r8      =       new     TornUtil(140001 ,       160000);
//    Runnable        r9      =       new     TornUtil(160001 ,       180000);
//    Runnable        r10     =       new     TornUtil(180001 ,       200000);
//    Runnable        r11     =       new     TornUtil(200001 ,       220000);
//    Runnable        r12     =       new     TornUtil(220001 ,       240000);
//    Runnable        r13     =       new     TornUtil(240001 ,       260000);
//    Runnable        r14     =       new     TornUtil(260001 ,       280000);
//    Runnable        r15     =       new     TornUtil(280001 ,       300000);
//    Runnable        r16     =       new     TornUtil(300001 ,       320000);
//    Runnable        r17     =       new     TornUtil(320001 ,       340000);
//    Runnable        r18     =       new     TornUtil(340001 ,       360000);
//    Runnable        r19     =       new     TornUtil(360001 ,       380000);
//    Runnable        r20     =       new     TornUtil(380001 ,       400000);
//    Runnable        r21     =       new     TornUtil(400001 ,       420000);
//    Runnable        r22     =       new     TornUtil(420001 ,       440000);
//    Runnable        r23     =       new     TornUtil(440001 ,       460000);
//    Runnable        r24     =       new     TornUtil(460001 ,       480000);
//    Runnable        r25     =       new     TornUtil(480001 ,       500000);
//    Runnable        r26     =       new     TornUtil(500001 ,       520000);
//    Runnable        r27     =       new     TornUtil(520001 ,       540000);
//    Runnable        r28     =       new     TornUtil(540001 ,       560000);
//    Runnable        r29     =       new     TornUtil(560001 ,       580000);
//    Runnable        r30     =       new     TornUtil(580001 ,       600000);
//    Runnable        r31     =       new     TornUtil(600001 ,       620000);
//    Runnable        r32     =       new     TornUtil(620001 ,       640000);
//    Runnable        r33     =       new     TornUtil(640001 ,       660000);
//    Runnable        r34     =       new     TornUtil(660001 ,       680000);
//    Runnable        r35     =       new     TornUtil(680001 ,       700000);
//    Runnable        r36     =       new     TornUtil(700001 ,       720000);
//    Runnable        r37     =       new     TornUtil(720001 ,       740000);
//    Runnable        r38     =       new     TornUtil(740001 ,       760000);
//    Runnable        r39     =       new     TornUtil(760001 ,       780000);
//    Runnable        r40     =       new     TornUtil(780001 ,       800000);
//    Runnable        r41     =       new     TornUtil(800001 ,       820000);
//    Runnable        r42     =       new     TornUtil(820001 ,       840000);
//    Runnable        r43     =       new     TornUtil(840001 ,       860000);
//    Runnable        r44     =       new     TornUtil(860001 ,       880000);
//    Runnable        r45     =       new     TornUtil(880001 ,       900000);
//    Runnable        r46     =       new     TornUtil(900001 ,       920000);
//    Runnable        r47     =       new     TornUtil(920001 ,       940000);
//    Runnable        r48     =       new     TornUtil(940001 ,       960000);
//    Runnable        r49     =       new     TornUtil(960001 ,       980000);
//    Runnable        r50     =       new     TornUtil(980001 ,       1000000);
//    Runnable        r51     =       new     TornUtil(1000001        ,       1020000);
//    Runnable        r52     =       new     TornUtil(1020001        ,       1040000);
//    Runnable        r53     =       new     TornUtil(1040001        ,       1060000);
//    Runnable        r54     =       new     TornUtil(1060001        ,       1080000);
//  //  Runnable        r54     =       new     TornUtil(1076377        ,       1080000);
//    Runnable        r55     =       new     TornUtil(1080001        ,       1100000);
//    Runnable        r56     =       new     TornUtil(1100001        ,       1120000);
//    Runnable        r57     =       new     TornUtil(1120001        ,       1140000);
//    Runnable        r58     =       new     TornUtil(1140001        ,       1160000);
//    Runnable        r59     =       new     TornUtil(1160001        ,       1180000);
//    Runnable        r60     =       new     TornUtil(1180001        ,       1200000);
//    Runnable        r61     =       new     TornUtil(1200001        ,       1220000);
//    Runnable        r62     =       new     TornUtil(1220001        ,       1240000);
//    Runnable        r63     =       new     TornUtil(1240001        ,       1260000);
//    Runnable        r64     =       new     TornUtil(1260001        ,       1280000);
//    Runnable        r65     =       new     TornUtil(1280001        ,       1300000);
//    Runnable        r66     =       new     TornUtil(1300001        ,       1320000);
//    Runnable        r67     =       new     TornUtil(1320001        ,       1340000);
//    Runnable        r68     =       new     TornUtil(1340001        ,       1360000);
//    Runnable        r69     =       new     TornUtil(1360001        ,       1380000);
//    Runnable        r70     =       new     TornUtil(1380001        ,       1400000);
//    Runnable        r71     =       new     TornUtil(1400001        ,       1420000);
//    Runnable        r72     =       new     TornUtil(1420001        ,       1440000);
//    Runnable        r73     =       new     TornUtil(1440001        ,       1460000);
//    Runnable        r74     =       new     TornUtil(1460001        ,       1480000);
//    Runnable        r75     =       new     TornUtil(1480001        ,       1500000);
//    Runnable        r76     =       new     TornUtil(1500001        ,       1520000);
//    Runnable        r77     =       new     TornUtil(1520001        ,       1540000);
//    Runnable        r78     =       new     TornUtil(1540001        ,       1560000);
//    Runnable        r79     =       new     TornUtil(1560001        ,       1580000);
//    Runnable        r80     =       new     TornUtil(1580001        ,       1600000);
//    Runnable        r81     =       new     TornUtil(1600001        ,       1620000);
//    Runnable        r82     =       new     TornUtil(1620001        ,       1640000);
//    Runnable        r83     =       new     TornUtil(1640001        ,       1660000);
//    Runnable        r84     =       new     TornUtil(1660001        ,       1680000);
//    Runnable        r85     =       new     TornUtil(1680001        ,       1700000);
//    Runnable        r86     =       new     TornUtil(1700001        ,       1720000);
//    Runnable        r87     =       new     TornUtil(1720001        ,       1740000);
//    Runnable        r88     =       new     TornUtil(1740001        ,       1760000);
//    Runnable        r89     =       new     TornUtil(1760001        ,       1780000);
//    Runnable        r90     =       new     TornUtil(1780001        ,       1800000);
//    Runnable        r91     =       new     TornUtil(1800001        ,       1820000);
//    Runnable        r92     =       new     TornUtil(1820001        ,       1840000);
//    Runnable        r93     =       new     TornUtil(1840001        ,       1860000);
//    Runnable        r94     =       new     TornUtil(1860001        ,       1880000);
//    Runnable        r95     =       new     TornUtil(1880001        ,       1900000);
//    Runnable        r96     =       new     TornUtil(1900001        ,       1920000);
//    Runnable        r97     =       new     TornUtil(1920001        ,       1940000);
//    Runnable        r98     =       new     TornUtil(1940001        ,       1960000);
//    Runnable        r99     =       new     TornUtil(1960001        ,       1980000);
//    Runnable        r100    =       new     TornUtil(1980001        ,       2000000);
  Runnable r1 = new UpdateLastAction ( 0   , x   ) ;
Runnable r2 = new UpdateLastAction ( x   , x * 2 ) ;
Runnable r3 = new UpdateLastAction ( x * 2 , x * 3 ) ;
Runnable r4 = new UpdateLastAction ( x * 3 , x * 4 ) ;
Runnable r5 = new UpdateLastAction ( x * 4 , x * 5 ) ;
Runnable r6 = new UpdateLastAction ( x * 5 , x * 6 ) ;
Runnable r7 = new UpdateLastAction ( x * 6 , x * 7 ) ;
Runnable r8 = new UpdateLastAction ( x * 7 , x * 8 ) ;
Runnable r9 = new UpdateLastAction ( x * 8 , x * 9 ) ;
Runnable r10 = new UpdateLastAction ( x * 9 , x * 10 ) ;
Runnable r11 = new UpdateLastAction ( x * 10 , x * 11 ) ;
Runnable r12 = new UpdateLastAction ( x * 11 , x * 12 ) ;
Runnable r13 = new UpdateLastAction ( x * 12 , x * 13 ) ;
Runnable r14 = new UpdateLastAction ( x * 13 , x * 14 ) ;
Runnable r15 = new UpdateLastAction ( x * 14 , x * 15 ) ;
Runnable r16 = new UpdateLastAction ( x * 15 , x * 16 ) ;
Runnable r17 = new UpdateLastAction ( x * 16 , x * 17 ) ;
Runnable r18 = new UpdateLastAction ( x * 17 , x * 18 ) ;
Runnable r19 = new UpdateLastAction ( x * 18 , x * 19 ) ;
Runnable r20 = new UpdateLastAction ( x * 19 , x * 20 ) ;
Runnable r21 = new UpdateLastAction ( x * 20 , x * 21 ) ;
Runnable r22 = new UpdateLastAction ( x * 21 , x * 22 ) ;
Runnable r23 = new UpdateLastAction ( x * 22 , x * 23 ) ;
Runnable r24 = new UpdateLastAction ( x * 23 , x * 24 ) ;
Runnable r25 = new UpdateLastAction ( x * 24 , x * 25 ) ;
Runnable r26 = new UpdateLastAction ( x * 25 , x * 26 ) ;
Runnable r27 = new UpdateLastAction ( x * 26 , x * 27 ) ;
Runnable r28 = new UpdateLastAction ( x * 27 , x * 28 ) ;
Runnable r29 = new UpdateLastAction ( x * 28 , x * 29 ) ;
Runnable r30 = new UpdateLastAction ( x * 29 , x * 30 ) ;
Runnable r31 = new UpdateLastAction ( x * 30 , x * 31 ) ;
Runnable r32 = new UpdateLastAction ( x * 31 , x * 32 ) ;
Runnable r33 = new UpdateLastAction ( x * 32 , x * 33 ) ;
Runnable r34 = new UpdateLastAction ( x * 33 , x * 34 ) ;
Runnable r35 = new UpdateLastAction ( x * 34 , x * 35 ) ;
Runnable r36 = new UpdateLastAction ( x * 35 , x * 36 ) ;
Runnable r37 = new UpdateLastAction ( x * 36 , x * 37 ) ;
Runnable r38 = new UpdateLastAction ( x * 37 , x * 38 ) ;
Runnable r39 = new UpdateLastAction ( x * 38 , x * 39 ) ;
Runnable r40 = new UpdateLastAction ( x * 39 , x * 40 ) ;
Runnable r41 = new UpdateLastAction ( x * 40 , x * 41 ) ;
Runnable r42 = new UpdateLastAction ( x * 41 , x * 42 ) ;
Runnable r43 = new UpdateLastAction ( x * 42 , x * 43 ) ;
Runnable r44 = new UpdateLastAction ( x * 43 , x * 44 ) ;
Runnable r45 = new UpdateLastAction ( x * 44 , x * 45 ) ;
Runnable r46 = new UpdateLastAction ( x * 45 , x * 46 ) ;
Runnable r47 = new UpdateLastAction ( x * 46 , x * 47 ) ;
Runnable r48 = new UpdateLastAction ( x * 47 , x * 48 ) ;
Runnable r49 = new UpdateLastAction ( x * 48 , x * 49 ) ;
Runnable r50 = new UpdateLastAction ( x * 49 , x * 50 ) ;
Runnable r51 = new UpdateLastAction ( x * 50 , x * 51 ) ;
Runnable r52 = new UpdateLastAction ( x * 51 , x * 52 ) ;
Runnable r53 = new UpdateLastAction ( x * 52 , x * 53 ) ;
Runnable r54 = new UpdateLastAction ( x * 53 , x * 54 ) ;
Runnable r55 = new UpdateLastAction ( x * 54 , x * 55 ) ;
Runnable r56 = new UpdateLastAction ( x * 55 , x * 56 ) ;
Runnable r57 = new UpdateLastAction ( x * 56 , x * 57 ) ;
Runnable r58 = new UpdateLastAction ( x * 57 , x * 58 ) ;
Runnable r59 = new UpdateLastAction ( x * 58 , x * 59 ) ;
Runnable r60 = new UpdateLastAction ( x * 59 , x * 60 ) ;
Runnable r61 = new UpdateLastAction ( x * 60 , x * 61 ) ;
Runnable r62 = new UpdateLastAction ( x * 61 , x * 62 ) ;
Runnable r63 = new UpdateLastAction ( x * 62 , x * 63 ) ;
Runnable r64 = new UpdateLastAction ( x * 63 , x * 64 ) ;
Runnable r65 = new UpdateLastAction ( x * 64 , x * 65 ) ;
Runnable r66 = new UpdateLastAction ( x * 65 , x * 66 ) ;
Runnable r67 = new UpdateLastAction ( x * 66 , x * 67 ) ;
Runnable r68 = new UpdateLastAction ( x * 67 , x * 68 ) ;
Runnable r69 = new UpdateLastAction ( x * 68 , x * 69 ) ;
Runnable r70 = new UpdateLastAction ( x * 69 , x * 70 ) ;
Runnable r71 = new UpdateLastAction ( x * 70 , x * 71 ) ;
Runnable r72 = new UpdateLastAction ( x * 71 , x * 72 ) ;
Runnable r73 = new UpdateLastAction ( x * 72 , x * 73 ) ;
Runnable r74 = new UpdateLastAction ( x * 73 , x * 74 ) ;
Runnable r75 = new UpdateLastAction ( x * 74 , x * 75 ) ;
Runnable r76 = new UpdateLastAction ( x * 75 , x * 76 ) ;
Runnable r77 = new UpdateLastAction ( x * 76 , x * 77 ) ;
Runnable r78 = new UpdateLastAction ( x * 77 , x * 78 ) ;
Runnable r79 = new UpdateLastAction ( x * 78 , x * 79 ) ;
Runnable r80 = new UpdateLastAction ( x * 79 , x * 80 ) ;
Runnable r81 = new UpdateLastAction ( x * 80 , x * 81 ) ;
Runnable r82 = new UpdateLastAction ( x * 81 , x * 82 ) ;
Runnable r83 = new UpdateLastAction ( x * 82 , x * 83 ) ;
Runnable r84 = new UpdateLastAction ( x * 83 , x * 84 ) ;
Runnable r85 = new UpdateLastAction ( x * 84 , x * 85 ) ;
Runnable r86 = new UpdateLastAction ( x * 85 , x * 86 ) ;
Runnable r87 = new UpdateLastAction ( x * 86 , x * 87 ) ;
Runnable r88 = new UpdateLastAction ( x * 87 , x * 88 ) ;
Runnable r89 = new UpdateLastAction ( x * 88 , x * 89 ) ;
Runnable r90 = new UpdateLastAction ( x * 89 , x * 90 ) ;
Runnable r91 = new UpdateLastAction ( x * 90 , x * 91 ) ;
Runnable r92 = new UpdateLastAction ( x * 91 , x * 92 ) ;
Runnable r93 = new UpdateLastAction ( x * 92 , x * 93 ) ;
Runnable r94 = new UpdateLastAction ( x * 93 , x * 94 ) ;
Runnable r95 = new UpdateLastAction ( x * 94 , x * 95 ) ;
Runnable r96 = new UpdateLastAction ( x * 95 , x * 96 ) ;
Runnable r97 = new UpdateLastAction ( x * 96 , x * 97 ) ;
Runnable r98 = new UpdateLastAction ( x * 97 , x * 98 ) ;
Runnable r99 = new UpdateLastAction ( x * 98 , x * 99 ) ;
Runnable r100 = new UpdateLastAction ( x * 99 , x * 100 ) ;

            startTime = System.currentTimeMillis();
        Thread  t1      =       new     Thread(r1);     
        Thread  t2      =       new     Thread(r2);     
        Thread  t3      =       new     Thread(r3);     
        Thread  t4      =       new     Thread(r4);     
        Thread  t5      =       new     Thread(r5);     
        Thread  t6      =       new     Thread(r6);     
        Thread  t7      =       new     Thread(r7);     
        Thread  t8      =       new     Thread(r8);     
        Thread  t9      =       new     Thread(r9);     
        Thread  t10     =       new     Thread(r10);    
        Thread  t11     =       new     Thread(r11);    
        Thread  t12     =       new     Thread(r12);    
        Thread  t13     =       new     Thread(r13);    
        Thread  t14     =       new     Thread(r14);    
        Thread  t15     =       new     Thread(r15);    
        Thread  t16     =       new     Thread(r16);    
        Thread  t17     =       new     Thread(r17);    
        Thread  t18     =       new     Thread(r18);    
        Thread  t19     =       new     Thread(r19);    
        Thread  t20     =       new     Thread(r20);    
        Thread  t21     =       new     Thread(r21);    
        Thread  t22     =       new     Thread(r22);    
        Thread  t23     =       new     Thread(r23);    
        Thread  t24     =       new     Thread(r24);    
        Thread  t25     =       new     Thread(r25);    
        Thread  t26     =       new     Thread(r26);    
        Thread  t27     =       new     Thread(r27);    
        Thread  t28     =       new     Thread(r28);    
        Thread  t29     =       new     Thread(r29);    
        Thread  t30     =       new     Thread(r30);    
        Thread  t31     =       new     Thread(r31);    
        Thread  t32     =       new     Thread(r32);    
        Thread  t33     =       new     Thread(r33);    
        Thread  t34     =       new     Thread(r34);    
        Thread  t35     =       new     Thread(r35);    
        Thread  t36     =       new     Thread(r36);    
        Thread  t37     =       new     Thread(r37);    
        Thread  t38     =       new     Thread(r38);    
        Thread  t39     =       new     Thread(r39);    
        Thread  t40     =       new     Thread(r40);    
        Thread  t41     =       new     Thread(r41);    
        Thread  t42     =       new     Thread(r42);    
        Thread  t43     =       new     Thread(r43);    
        Thread  t44     =       new     Thread(r44);    
        Thread  t45     =       new     Thread(r45);    
        Thread  t46     =       new     Thread(r46);    
        Thread  t47     =       new     Thread(r47);    
        Thread  t48     =       new     Thread(r48);    
        Thread  t49     =       new     Thread(r49);    
        Thread  t50     =       new     Thread(r50);    
        Thread  t51     =       new     Thread(r51);    
        Thread  t52     =       new     Thread(r52);    
        Thread  t53     =       new     Thread(r53);    
        Thread  t54     =       new     Thread(r54);    
        Thread  t55     =       new     Thread(r55);    
        Thread  t56     =       new     Thread(r56);    
        Thread  t57     =       new     Thread(r57);    
        Thread  t58     =       new     Thread(r58);    
        Thread  t59     =       new     Thread(r59);    
        Thread  t60     =       new     Thread(r60);    
        Thread  t61     =       new     Thread(r61);    
        Thread  t62     =       new     Thread(r62);    
        Thread  t63     =       new     Thread(r63);    
        Thread  t64     =       new     Thread(r64);    
        Thread  t65     =       new     Thread(r65);    
        Thread  t66     =       new     Thread(r66);    
        Thread  t67     =       new     Thread(r67);    
        Thread  t68     =       new     Thread(r68);    
        Thread  t69     =       new     Thread(r69);    
        Thread  t70     =       new     Thread(r70);    
        Thread  t71     =       new     Thread(r71);    
        Thread  t72     =       new     Thread(r72);    
        Thread  t73     =       new     Thread(r73);    
        Thread  t74     =       new     Thread(r74);    
        Thread  t75     =       new     Thread(r75);    
        Thread  t76     =       new     Thread(r76);    
        Thread  t77     =       new     Thread(r77);    
        Thread  t78     =       new     Thread(r78);    
        Thread  t79     =       new     Thread(r79);    
        Thread  t80     =       new     Thread(r80);    
        Thread  t81     =       new     Thread(r81);    
        Thread  t82     =       new     Thread(r82);    
        Thread  t83     =       new     Thread(r83);    
        Thread  t84     =       new     Thread(r84);    
        Thread  t85     =       new     Thread(r85);    
        Thread  t86     =       new     Thread(r86);    
        Thread  t87     =       new     Thread(r87);    
        Thread  t88     =       new     Thread(r88);    
        Thread  t89     =       new     Thread(r89);    
        Thread  t90     =       new     Thread(r90);    
        Thread  t91     =       new     Thread(r91);    
        Thread  t92     =       new     Thread(r92);    
        Thread  t93     =       new     Thread(r93);    
        Thread  t94     =       new     Thread(r94);    
        Thread  t95     =       new     Thread(r95);    
        Thread  t96     =       new     Thread(r96);    
        Thread  t97     =       new     Thread(r97);    
        Thread  t98     =       new     Thread(r98);    
        Thread  t99     =       new     Thread(r99);    
        Thread  t100    =       new     Thread(r100);   
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
            t16.setName("16");
            t17.setName("17");
            t18.setName("18");
            t19.setName("19");
            t20.setName("20");
            t21.setName("21");
            t22.setName("22");
            t23.setName("23");
            t24.setName("24");
            t25.setName("25");
            t26.setName("26");
            t27.setName("27");
            t28.setName("28");
            t29.setName("29");
            t30.setName("30");
            t31.setName("31");
            t32.setName("32");
            t33.setName("33");
            t34.setName("34");
            t35.setName("35");
            t36.setName("36");
            t37.setName("37");
            t38.setName("38");
            t39.setName("39");
            t40.setName("40");
            t41.setName("41");
            t42.setName("42");
            t43.setName("43");
            t44.setName("44");
            t45.setName("45");
            t46.setName("46");
            t47.setName("47");
            t48.setName("48");
            t49.setName("49");
            t50.setName("50");
            t51.setName("51");
            t52.setName("52");
            t53.setName("53");
            t54.setName("54");
            t55.setName("55");
            t56.setName("56");
            t57.setName("57");
            t58.setName("58");
            t59.setName("59");
            t60.setName("60");
            t61.setName("61");
            t62.setName("62");
            t63.setName("63");
            t64.setName("64");
            t65.setName("65");
            t66.setName("66");
            t67.setName("67");
            t68.setName("68");
            t69.setName("69");
            t70.setName("70");
            t71.setName("71");
            t72.setName("72");
            t73.setName("73");
            t74.setName("74");
            t75.setName("75");
            t76.setName("76");
            t77.setName("77");
            t78.setName("78");
            t79.setName("79");
            t80.setName("80");
            t81.setName("81");
            t82.setName("82");
            t83.setName("83");
            t84.setName("84");
            t85.setName("85");
            t86.setName("86");
            t87.setName("87");
            t88.setName("88");
            t89.setName("89");
            t90.setName("90");
            t91.setName("91");
            t92.setName("92");
            t93.setName("93");
            t94.setName("94");
            t95.setName("95");
            t96.setName("96");
            t97.setName("97");
            t98.setName("98");
            t99.setName("99");
            t100.setName("100");
            
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
            t16.start();
            t17.start();
            t18.start();
            t19.start();
            t20.start();
            t21.start();
            t22.start();
            t23.start();
            t24.start();
            t25.start();
            t26.start();
            t27.start();
            t28.start();
            t29.start();
            t30.start();
            t31.start();
            t32.start();
            t33.start();
            t34.start();
            t35.start();
            t36.start();
            t37.start();
            t38.start();
            t39.start();
            t40.start();
            t41.start();
            t42.start();
            t43.start();
            t44.start();
            t45.start();
            t46.start();
            t47.start();
            t48.start();
            t49.start();
            t50.start();
            t51.start();
            t52.start();
            t53.start();
            t54.start();
            t55.start();
            t56.start();
            t57.start();
            t58.start();
            t59.start();
            t60.start();
            t61.start();
            t62.start();
            t63.start();
            t64.start();
            t65.start();
            t66.start();
            t67.start();
            t68.start();
            t69.start();
            t70.start();
            t71.start();
            t72.start();
            t73.start();
            t74.start();
            t75.start();
            t76.start();
            t77.start();
            t78.start();
            t79.start();
            t80.start();
            t81.start();
            t82.start();
            t83.start();
            t84.start();
            t85.start();
            t86.start();
            t87.start();
            t88.start();
            t89.start();
            t90.start();
            t91.start();
            t92.start();
            t93.start();
            t94.start();
            t95.start();
            t96.start();
            t97.start();
            t98.start();
            t99.start();
            t100.start();
    
  
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  
    
    public static boolean parseProfile(int userId, String selection, String apiKey, String mode) throws Exception {
        String u = "https://www.torn.com/profiles.php?XID=" + userId;

        URL url = new URL(u);
        
        HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
         
        conn.setRequestMethod("GET");
      //  conn.setRequestProperty("Accept", "application/json");
       // conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36");
        conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36");

        // conn.addRequestProperty("cookie", "<cookie value from the browser, from the header on a successful request>");
        	
        conn.setConnectTimeout(2000);
        conn.setInstanceFollowRedirects(false);
        conn.setReadTimeout(10000);
        conn.addRequestProperty("cookie", "fc3ea40db37c33ed85206cc703f01b877a5f9254-1626199352-0-150");
       // conn.addRequestProperty("Referer", u);
		Thread.sleep(6000);
        //     conn.setRequestProperty("Accept", "application/json");
        if (conn.getResponseCode() != 200) {
        	System.out.println(conn.getResponseCode());
            System.out.println("*******************************Sleeping for 10 mins... " + userId);
            Thread.sleep(600000);
            //   parseProfile(userId, selection, apiKey);
            conn = (HttpsURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            //throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }
        synchronized (u) {
            userProcessed++;
        }
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        StringBuilder input = new StringBuilder();
        
        String inputLine;
      //  long startTime1 = System.currentTimeMillis();
        while ((inputLine = br.readLine()) != null)
            input.append(inputLine);
      //  System.out.println(System.currentTimeMillis()-startTime1);  
        String output = input.toString();
        if (output.contains("User could not be found"))
            return false;
        
        if("Update".equals(mode)){
            output.replaceAll(" ","");
            String status = "Okay";                
            if(output.contains("Account marked for deletion"))
                status = "Account marked for deletion";
            String[] spl = output.split(" ago ");
            if(spl.length<2)
                return false;
            String[] spl1 = spl[spl.length-2].split(" ");
            int size = spl1.length;
            try{
            int lastActionTime  = Integer.parseInt(spl1[size-2]);
            String lastActionUnit = spl1[size-1];
            String lastAction = lastActionTime +" "+ lastActionUnit +" ago";
            synchronized (u) {
                activeProfiles++;
            }
      
            String query = "update torn_players set last_action = '" + lastAction + "',last_action_time = " + lastActionTime + ", last_action_unit = '" + lastActionUnit + "', creation_date = sysdate, status ='"+status+"' where user_id = "+userId;
            //    System.out.println(query);
            stmt.executeUpdate(query);
             
            //       // userId++;
            if (userId % 10000 == 0)
                conn.disconnect();
            }
            catch(NumberFormatException ne) {
                System.out.println("Number Format Exception******************************** for "+userId);
                errored.add(userId);
            }
            catch(Exception e){
                System.out.println("Exception=========================================for "+userId);
                errored.add(userId);
            }
            
        }
     
        
        return true;
    }

}
