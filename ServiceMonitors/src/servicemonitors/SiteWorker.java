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
import java.util.Map.Entry;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import javax.swing.SwingWorker;

/**
 *
 * @author Azuveike
 * This class encapsulates the siteSearcher class to allow it to work in the background.
 */
public class SiteWorker extends SwingWorker<Integer,String> {
    
    
      Vector<javax.swing.JLabel> labelcontainer;
    Vector<javax.swing.JButton> buttoncontainer;
    Vector<siteSearcher> searchcontainer ;
    ArrayList<Future<Integer>> searchers;
     Vector<Integer> siteresults;
     boolean ran;
     double total;
     int times_ran;
     int count;
     public long sleep;
    LinkedHashMap sitemap; // holds location for ping to relate to button
    
    public SiteWorker(Vector<javax.swing.JLabel> labelcontainer,Vector<javax.swing.JButton> buttoncontainer,
                Vector<siteSearcher> pingcontainer,    ArrayList<Future<Integer>> pingers,Vector<Integer> pingresults,
               LinkedHashMap sitemap )
    {
        count = 0;
        this.labelcontainer = labelcontainer;
        this.buttoncontainer = buttoncontainer;
        this.searchcontainer = pingcontainer;
        this.searchers = pingers;
        this.siteresults = pingresults;
        this.sitemap = sitemap;
        ran = false;
        total = 0;
        times_ran = 0;
        sleep = 10000;
       for(Iterator it = sitemap.entrySet().iterator(); it.hasNext();)
       {
            Entry<siteSearcher,Integer> item = (Entry<siteSearcher,Integer>) it.next();
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
          int y = 0;
         
          if(!this.isCancelled())
        for(Iterator it = sitemap.entrySet().iterator(); it.hasNext();)
        {
            Entry<siteSearcher,Integer> item = (Entry<siteSearcher,Integer>) it.next();
            //updateImage(siteresults.elementAt(y),labelcontainer.elementAt(item.getValue()));//blocks
            y++;
        }
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
      
       @Override
        protected  void process(List<String> chunks)
    {
        int y = 0;
         
          
        for(Iterator it = sitemap.entrySet().iterator(); it.hasNext();)
        {
            Entry<siteSearcher,Integer> item = (Entry<siteSearcher,Integer>) it.next();
            total+= siteresults.elementAt(y);
            // doesnt take into account maintenance. have maintenance add no service count.
           // updateImage(0+ (int)(Math.random() * ((4 ) + 1)),labelcontainer.elementAt(item.getValue()));//blocks
            updateImage(siteresults.elementAt(y),labelcontainer.elementAt(item.getValue()));//blocks
            y++;
        }
        
     //   System.out.println("updating site here.");
    }
     
     @Override
    protected Integer doInBackground() throws Exception {
        ran = true;
        while(ran)
        {
     //   System.out.println("proocessing site");
          for(Iterator it = sitemap.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry<siteSearcher,Integer> item = (Map.Entry<siteSearcher,Integer>) it.next();
            Callable<Integer> pingGen = item.getKey(); // make callable to use ping.call
            FutureTask<Integer> task = new FutureTask<Integer>(pingGen); // use future task to get results
            
            searchers.add(task); // add to our results computer
            
            Thread t = new Thread(task); // start the new thread
            t.start();
        }
        
         for(Future<Integer> results: searchers )
        {
            try {
                siteresults.add(results.get());
            } catch (InterruptedException ex) {
                siteresults.add(0);//Logger.getLogger(Monitor_Gui.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
               siteresults.add(0);// Logger.getLogger(Monitor_Gui.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
         // done();}
         times_ran++;
         publish();
         Thread.sleep(sleep);
         siteresults.clear();
         searchers.clear();
        }
         return 0;
       
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    
    }
       public double getRange()
    {
        return (total/times_ran)/ count;
    }
}
