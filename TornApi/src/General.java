

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import javax.net.ssl.HttpsURLConnection;
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
public class General {
     static String apiKey = "e8StmbbT";
    public static void main(String[] args) throws Exception{
        HashSet<Long> hashSet = new HashSet<Long>();
        HashMap map = new HashMap();
        BigDecimal qty = new BigDecimal(1);
        BigDecimal eQty = (BigDecimal)map.get(1);
        if(eQty==null)
             map.put(1,qty);
        else{
            if(eQty.compareTo(qty)==1)
                map.put(1,qty);
        }
        
        map.put(1, 5);
        
        System.out.println(map.get(1));
    hashSet.add(new Long("9"));
    hashSet.add(new Long("4"));
    hashSet.add(new Long("2"));
    hashSet.add(new Long("2"));
    hashSet.add(new Long("3"));

    Iterator it = hashSet.iterator();
    while(it.hasNext())
    {
        Object obj = it.next();
      //  System.out.println(obj);
                
                
    }
    //System.out.println("Minimum Element of HashSet is : " + obj);
    }
}
