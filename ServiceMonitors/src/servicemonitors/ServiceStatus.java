/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicemonitors;


import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;

/**lll
 *
 * @author Azuveike
 * This Class checks for Services on different ports. It operates by opening up a Socket connection
 * and sending a message to a user inputted port number.
 */
public class ServiceStatus implements Callable<Integer>{
    
    public String hostname; // ip/url 
   public  int portnum;
   public boolean maintenance;
   public static double sleep;
   public String name; // name of the Service
    ServiceStatus(String hostname, int portnum)
    {
        this.hostname = hostname;
        this.portnum = portnum;
        maintenance = false;
        sleep = 10000;
    }
    
     ServiceStatus(String hostname, int portnum, String name)
    {
        this.hostname = hostname;
        this.portnum = portnum;
        maintenance = false;
        sleep = 10000;
        this.name = name;
    }
    
    ServiceStatus(String hostname, int portnum, boolean maint)
    {
        this.hostname = hostname;
        this.portnum = portnum;
        maintenance = maint;
    }
    
     ServiceStatus(String hostname, int portnum, boolean maint, String name)
    {
        this.hostname = hostname;
        this.portnum = portnum;
        maintenance = maint;
        this.name = name;
    }
    
    ServiceStatus(String hostname)
    {
        this.hostname = hostname;
        portnum = 7;
    }
    
    @Override
    public Integer call() throws IOException
    {
        
        if (maintenance)
            return 4; // blue
        try{
        if(ping())
            return 1; // green
        else
            return 0; 
        }catch(IOException s)
        {
            return 0;// edited
        }
        
    }
    
    
    boolean ping() throws IOException
    {
     /*   Socket test = new Socket(hostname,portnum);
         DataInputStream input = new DataInputStream(test.getInputStream());
        PrintStream ps = new PrintStream(test.getOutputStream());
        ps.println("testme");
     String str = input.readUTF();
     test.close();;
    if (str.equals("testme"))
      return true;
    else
      return false; */
  
        Socket socket = null;
boolean reachable = false;
try {
    socket = new Socket(hostname, portnum); // pings echo first.
    reachable = true;
}
catch( Exception e) // if echo fails, will attempt to ping port 80
{
     reachable = false;
}
finally {            
    if (socket != null) try { socket.close(); } catch(IOException e) {}
}
       return reachable;
    }
    
    /*  run function: call ping function. true = condition for green code. false = condition for down code
    
    Errors: any exception caught generates down condition*/
   
    
}
