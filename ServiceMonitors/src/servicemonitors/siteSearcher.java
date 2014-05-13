/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicemonitors;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.Callable;
/**
 *
 * @author Azuveike
 * This class searches for specific text from a url. Can be fed http,www, and xml
 */
public class siteSearcher implements Callable<Integer> {
    
   public  String url;
    public String find;
    public boolean maintenance;
    public static double sleep;
    public String name;
    siteSearcher(String url, String find)
    {
        this.url = url;
        this.find = find;
        sleep = 10000;
    }
     siteSearcher(String url, String find, String name)
    {
        this.url = url;
        this.find = find;
        sleep = 10000;
        this.name = name;
    }
    siteSearcher(String url, String find, boolean maint)
    {
        this.url = url;
        this.find = find;
        maintenance = maint;
    }
    siteSearcher(String url, String find, boolean maint, String name)
    {
        this.url = url;
        this.find = find;
        maintenance = maint;
        this.name = name;
    }
    
    
    
    @Override
    public Integer call()
    {
        if(maintenance)
            return 4;
        boolean found = false;;
        try {
            found = search();
        } catch (IOException ex) {
           // Logger.getLogger(siteSearcher.class.getName()).log(Level.SEVERE, null, ex);
            found = false;
        }
        if(found)
            return 1;
        else
            return 0;
    }
    
    boolean search( ) throws MalformedURLException, IOException
    {
        String out = new Scanner(new URL(url).openStream(), "UTF-8").useDelimiter("\\A").next();
        if(out.contains(find))
            return true;
        else
            return false;
    }
    
    
    /*  if true, generate green code. else if false red code. if exception, generate bad code.*/ 
}
