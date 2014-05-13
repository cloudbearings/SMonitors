/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicemonitors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

/**
 *
 * @author Azuveike
 * This is the class for the Ping Service. It implements callable to return a code which
 * we use as a state to display images. This class pings by opening up an external ping process
 * and feeding it an ip.
 */
public class PingStatus implements Callable<Integer> {
   public String hostname; // the ip we are pinging.
   public String info; // we return the result of the ping process to this and parse it for data.
   public boolean maintenance; // determines whether or not this status is active
   public String name; // The name of the status
   public static double sleep; // determines the waiting period between pings;
    PingStatus(String host){ hostname = host; maintenance = false; sleep = 10000;}
    PingStatus(String host, String name){ hostname = host; maintenance = false; sleep = 10000;this.name = name;}
     PingStatus(String host, boolean maint){ hostname = host; maintenance = maint;}
      PingStatus(String host, boolean maint, String n){ hostname = host; maintenance = maint;name = n;}
     
    @Override
    public Integer call() throws IOException
    {
        if(maintenance)
            return 4; // code for maintenance
        String result ="";
        int latency = 9999;
        result = pingHost();
        if(timeout())
            return 3; // code for red/offline
        else
        {
            latency = getLatency();
            if(latency <100)
                return 1; // code for green
            else if(latency > 100 && latency < 501)
                return 2; // code for yellow
            else
                return 3;
        }
            
    }
    String pingHost() throws IOException
    {
       // System.out.println("Testing ping...");
        try {
            // websites typically block pings so it might be unresponsive
            Process ping = Runtime.getRuntime().exec("ping " + hostname); // start the ping process
            BufferedReader br = new BufferedReader(new InputStreamReader(ping.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null)
            {
             sb.append(line);
             sb.append("\n"); // append all output to string
            }
            info = sb.toString();
            return (sb.toString());
            }
         catch (IOException e) 
        {
        //e.printStackTrace();
            
        }
        return "9999";
    }
    
    boolean timeout()
    {
        if(info.contains("Request timed out."))
            return true;
        else
            return false;
    }
    
    int getLatency()
    {
        // retrieves the average latency
        String start = "Average = ";
        String end = "ms";
        String returnv = "";
        //Integer retval = Integer.getInteger((giveMeTextBetween(info, start,end)));
        returnv =   (giveMeTextBetween(info, start,end)); //Integer.getInteger
        return Integer.parseInt(returnv);
    }
    
     String giveMeTextBetween(String s, String before, String after)
     {
        String found = "";
        int start = s.indexOf(before);    // Find the index of the beginning tag
         if (start == - 1) return"";       // If we don't find anything, send back a blank String
         start += before.length();         // Move to the end of the beginning tag
         int end = s.indexOf(after,start); // Find the index of the end tag
        if (end == -1) return"";          // If we don't find the end tag, send back a blank String
        return s.substring(start,end);    // Return the text in between
    }
     
     /* run function: calls pinghost which updates host member. calls timeout. if timeout is false
        return code for down/bad. else get latency. check latency against 3 thresholds. 
        return code based upon this value.
     
     Error checking:invalid host? throw exception at pinghost. return bad string. give code for down.*/
}

