/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicemonitors;

import java.io.IOException;

// code 0 = offline, code 1 = green, code 2 = yellow, code 3 = failed, code 4 = maintenance
/**
 * since each service sleeps for separate amounts of time, 
 * make three different containers. one for pings, site text, service
 * every 10-15 seconds, block and get results of ping then update display.
 * every 5 minutes get result of service, and also site text.
 * update these, clean memory, then reinitialize container threads. Do this in an
 * infinite loop.
 *
 * @author Azuveike
 * used for initial testing. Disregard this file.
 */
public class ServiceMonitors {

    /**
     * @param args the command line arguments
     */
    public static void main3(String[] args) throws IOException {
        // TODO code application logic here
        
        siteSearcher test = new siteSearcher("http://www.gamefaqs.com", "sirloinedstake");
        System.out.println(test.search());
        ServiceStatus pingtest = new ServiceStatus("192.168.2.1",7);
        System.out.println(pingtest.ping());
         PingStatus pingme = new PingStatus("192.168.2.1");
         System.out.println(pingme.pingHost());
          System.out.println(pingme.getLatency());
    }
    
}
