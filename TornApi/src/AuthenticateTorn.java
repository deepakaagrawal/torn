/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author deeagraw
 */


import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Properties;

public class AuthenticateTorn {
    
public static WebClient autoLogin(String loginUrl, String login, String password) throws FailingHttpStatusCodeException, MalformedURLException, IOException{
        
        WebClient client = new WebClient();
        
        //client.getOptions().setCssEnabled(false);
        //client.getOptions().setJavaScriptEnabled(false);
        ProxyConfig proxyConfig = new ProxyConfig("www-proxy.us.oracle.com", 80);
        client.getOptions().setProxyConfig(proxyConfig);

        HtmlPage page = client.getPage(loginUrl);

        HtmlInput inputPassword = page.getFirstByXPath("//input[@type='password']");
        //The first preceding input that is not hidden
        HtmlInput inputLogin = inputPassword.getFirstByXPath(".//preceding::input[not(@type='hidden')]");

        inputLogin.setValueAttribute(login);
        inputPassword.setValueAttribute(password);

        //get the enclosing form
        HtmlForm loginForm = inputPassword.getEnclosingForm() ;

        //submit the form
        page = client.getPage(loginForm.getWebRequest(null));
        HtmlPage a = client.getPage("http://www.torn.com/crimes.php");
        System.out.println(a.asText());
        //returns the cookie filled client :)
        return client;
    }

  public static void main(String[] args) throws Exception {
     
        
      String baseUrl = "http://torn.com" ;
        String loginUrl = baseUrl  ; 
        String login = "deepak.aagrawal@gmail.com";
        String password = "oneighty" ;

        try {
            System.out.println("Starting autoLogin on " + loginUrl);
            WebClient client = autoLogin(loginUrl, login, password);
            HtmlPage page = client.getPage(baseUrl) ;
            String output = page.asText();
            System.out.println(output);
            HtmlAnchor logoutLink = page.getFirstByXPath(String.format("//a[@href='/profiles.php?XID=%s']", login)) ;
            if(logoutLink != null ){
                System.out.println("Successfuly logged in !");
                // printing the cookies
                for(Cookie cookie : client.getCookieManager().getCookies()){
                    System.out.println(cookie.toString());
                }
            }else{
                System.err.println("Wrong credentials");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
     }

}

