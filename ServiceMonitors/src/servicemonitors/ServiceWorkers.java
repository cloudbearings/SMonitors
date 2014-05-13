/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicemonitors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;

/**
 *
 * @author Azuveike
 * This class is identical to the PingWorkers Class except it encapsulates ServiceStatuses
 */
public class ServiceWorkers extends SwingWorker<Integer,String>{
    
      Vector<javax.swing.JLabel> labelcontainer;
    Vector<javax.swing.JButton> buttoncontainer;
    Vector<ServiceStatus> servcontainer ;
    ArrayList<Future<Integer>> servers;
     Vector<Integer> servresults;
     boolean ran;
     double total;
     int times_ran;
     int count;
     public long sleep;
    LinkedHashMap servmap; // holds location for a Service to relate to button
    
    public ServiceWorkers(Vector<javax.swing.JLabel> labelcontainer,Vector<javax.swing.JButton> buttoncontainer,
                Vector<ServiceStatus> pingcontainer,    ArrayList<Future<Integer>> pingers,Vector<Integer> pingresults,
               LinkedHashMap pingmap )
    {
        count = 0;
        this.labelcontainer = labelcontainer;
        this.buttoncontainer = buttoncontainer;
        this.servcontainer = pingcontainer;
        this.servers = pingers;
        this.servresults = pingresults;
        this.servmap = pingmap;
        ran = false;
        total = 0;
        times_ran = 0;
        sleep = 10000;
       for(Iterator it = servmap.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry<ServiceStatus,Integer> item = (Map.Entry<ServiceStatus,Integer>) it.next();
        count++;
        }
    }
    
    public int getCount()
    {
        return count;
    }
    
    @Override
    protected void done()
      {
         // System.out.println("updating here.");
          int x =0;
          for(Iterator it = servmap.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry<ServiceStatus,Integer> item = (Map.Entry<ServiceStatus,Integer>) it.next();
           // updateImage(servresults.elementAt(x),labelcontainer.elementAt(item.getValue()));//blocks
            x++;
        }
          //System.out.println("updating here.");
        try {
            Thread.sleep(0); // sleep for 10 seconds
           // pingers.clear();
     //  pingresults.clear();
       //System.out.println("restarting");
           //doInBackground();
        } catch (InterruptedException ex) {
            Logger.getLogger(ServiceWorkers.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ServiceWorkers.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    
    @Override
    protected Integer doInBackground() throws Exception {
        ran = true;
        while(ran)
        {
      //  System.out.println("proocessing serv");
          for(Iterator it = servmap.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry<ServiceStatus,Integer> item = (Map.Entry<ServiceStatus,Integer>) it.next();
            Callable<Integer> pingGen = item.getKey(); // make callable to use ping.call
            FutureTask<Integer> task = new FutureTask<Integer>(pingGen); // use future task to get results
            
            servers.add(task); // add to our results computer
            
            Thread t = new Thread(task); // start the new thread
            t.start();
        }
        
         for(Future<Integer> results: servers )
        {
            try {
                servresults.add(results.get());
            } catch (InterruptedException ex) {
                servresults.add(0);//Logger.getLogger(Monitor_Gui.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
               servresults.add(0);// Logger.getLogger(Monitor_Gui.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
         // done();}
         times_ran++;
         publish();
         Thread.sleep(sleep);
         servresults.clear();
         servers.clear();
        }
         return 0;
       
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    
    }
    
    @Override
        protected  void process(List<String> chunks)
    {
         int x =0;
         if(!this.isCancelled())
          for(Iterator it = servmap.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry<ServiceStatus,Integer> item = (Map.Entry<ServiceStatus,Integer>) it.next();
            
            total+= servresults.elementAt(x);
          // updateImage(0+ (int)(Math.random() * ((4 ) + 1)),labelcontainer.elementAt(item.getValue()));//blocks
            updateImage(servresults.elementAt(x),labelcontainer.elementAt(item.getValue()));//blocks
            x++;
        }
       //  System.out.println("updating serv here.");
    }
    
     public void updateImage(int code, javax.swing.JLabel j)
    {
        if(code==0)
            j.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/Offline.JPG"))); // );
        //jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/Green Status.JPG"))); // NOI18N
       // jLabel1.setVisible(false);
        else if(code==1)
            j.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/Green Status.JPG"))); // 
        else if (code ==2)
            j.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/Yellow Status.JPG"))); // 
        else if(code==3)
            j.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/Offline.JPG"))); // 
        else if(code==4)
            j.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/Maintenance Staus.JPG"))); // 
        else
            j.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/Maintenance Staus.JPG"))); // 
    }
     
      public double getRange()
    {
        return (total/times_ran);
    }
}
