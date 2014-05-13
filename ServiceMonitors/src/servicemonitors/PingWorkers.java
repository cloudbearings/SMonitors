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
 * This class encapsulates the PingStatus class and makes it work in the background
 * on a SwingWorker Thread. This allows us to seamlessly update the UI without freezing the gui.
 */
public class PingWorkers extends SwingWorker<Integer,String> {
    
    
    // constructor needs label to update, button to update
    // pass ping maps,pass ping arraylist, pass ping vector, label vector, button vector
    Vector<javax.swing.JLabel> labelcontainer; // synchronized, holds labels. Labels have our images drawn over them.
    Vector<javax.swing.JButton> buttoncontainer;// holds buttons. Update later to sync Arraylists
    Vector<PingStatus> pingcontainer ;// all Pings are put into the ssame Worker. This holds all our individual PingStatuses.
    ArrayList<Future<Integer>> pingers;// This holds the actual runnable(call)  ping processes.
     Vector<Integer> pingresults; // our results/
     boolean ran; // determines whether we start from the beginning
     double total; // used in our results when updating image
     int times_ran; // the number of times our pingworker has looped
     int count; // linked to services
     public long sleep;
    LinkedHashMap pingmap; // holds location for ping to relate to a  button
    public PingWorkers(Vector<javax.swing.JLabel> labelcontainer,Vector<javax.swing.JButton> buttoncontainer,
                Vector<PingStatus> pingcontainer,    ArrayList<Future<Integer>> pingers,Vector<Integer> pingresults,
               LinkedHashMap pingmap )
    {
        count = 0;
        this.labelcontainer = labelcontainer;
        this.buttoncontainer = buttoncontainer;
        this.pingcontainer = pingcontainer;
        this.pingers = pingers;
        this.pingresults = pingresults;
        this.pingmap = pingmap;
        ran = false;
       total = 0;
       times_ran = 0;
       sleep = 10000;
       
       for(Iterator it = pingmap.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry<PingStatus,Integer> item = (Map.Entry<PingStatus,Integer>) it.next();
         // updateImage(pingresults.elementAt(x),labelcontainer.elementAt(item.getValue()));//blocks
            count++;
        }
    }
    
    public int getCount()
    {
        // System.out.println("returning count");
         return count;
    }
      protected void done()
      {
         // System.out.println("updating here.");
          int x =0;
         for(Iterator it = pingmap.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry<PingStatus,Integer> item = (Map.Entry<PingStatus,Integer>) it.next();
         // updateImage(pingresults.elementAt(x),labelcontainer.elementAt(item.getValue()));//blocks
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
            Logger.getLogger(PingWorkers.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PingWorkers.class.getName()).log(Level.SEVERE, null, ex);
        }
      }

      
      
    @Override
    protected Integer doInBackground() throws Exception {
        ran = true;
        while(ran)
        {
      //  System.out.println("proocessing pioing here" );
           // total = 0;
          for(Iterator it = pingmap.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry<PingStatus,Integer> item = (Map.Entry<PingStatus,Integer>) it.next();
            Callable<Integer> pingGen = item.getKey(); // make callable to use ping.call
            FutureTask<Integer> task = new FutureTask<Integer>(pingGen); // use future task to get results
            
            pingers.add(task); // add to our results computer
            
            Thread t = new Thread(task); // start the new thread
            t.start();
        }
        
         for(Future<Integer> results: pingers )
        {
            try {
                pingresults.add(results.get());
            } catch (InterruptedException ex) {
                pingresults.add(0);//Logger.getLogger(Monitor_Gui.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
               pingresults.add(0);// Logger.getLogger(Monitor_Gui.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
         // done();}
         times_ran++;
         publish();
         Thread.sleep(sleep);
         pingresults.clear();
         pingers.clear();
        }
         return 0;
       
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    
    }
    
        @Override
        protected  void process(List<String> chunks)
    {
         int x =0;
         if(!this.isCancelled())
          for(Iterator it = pingmap.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry<PingStatus,Integer> item = (Map.Entry<PingStatus,Integer>) it.next();
            total+= pingresults.elementAt(x);
            //updateImage(0+ (int)(Math.random() * ((4 ) + 1)),labelcontainer.elementAt(item.getValue()));//blocks
            updateImage(pingresults.elementAt(x),labelcontainer.elementAt(item.getValue()));//blocks
            x++;
        }
         // System.out.println("updating ping  here.");
    }
    
        
        //protected  void publish(String chunks)
        //{
            
        //}
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
        return (total/times_ran)/count;
    }
}
