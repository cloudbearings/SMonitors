/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicemonitors;

import java.util.List;
import javax.swing.SwingWorker;

/**
 *
 * @author Azuveike
 * This class is run in the background for our Monitor_GUI.
 * Three separate threads are run in the background to update the gui simultaneously with ping,
 * site search, and service results;
 */
public class ParentWorker extends SwingWorker<Integer, String> {
    
   public PingWorkers pwork;
    public ServiceWorkers swork;
   public  SiteWorker sitework;
    boolean ran;
    javax.swing.JLabel label;
    
    ParentWorker( PingWorkers p, ServiceWorkers s, SiteWorker ss, javax.swing.JLabel lab)
    {
        pwork = p;
        swork = s;
        sitework = ss;
        ran = false;
        label = lab;
    }
    
     @Override
    protected Integer doInBackground() throws Exception {
    
         while(!ran)
         {
             publish();
             Thread.sleep(5000);
         }
         
         return 0;
}
     
     @Override
        protected  void process(List<String> chunks)
     {
         //the bug here is that ur dividing by 3 even when getrange returns 0.
         // make a counter check for each worker. if its greater than one, then do quotient++
         double code =0;
         double quotient = 0;
         if(pwork!= null)
         {
             if(pwork.getCount()>0)
             {
                 code = pwork.getRange();
                // System.out.println(code);
                 quotient++;
             }
         }
         if(swork!=null)
         {
             if(swork.getCount() > 0)
             {
                    code += swork.getRange();
         
                 quotient++;
             }
         }
         if(sitework!=null)
         {
             if(sitework.getCount() > 0)
             {
                code += sitework.getRange();
                 quotient++;
             }
         }
         code/= quotient;
         updateImage(code,label);
         
        // System.out.println(code);
     }
     
     
      public void updateImage(double code, javax.swing.JLabel j)
    {
        //System.out.println("code:" + code);
        if(code <.4)
        {
            //red
            
          j.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/Offline.JPG"))); // 

        }
        else if(code <.8 && code >= .4)
        {
            //yellow
                        j.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/Yellow Status.JPG"))); // 

        }
        else if ( code >= .8)
        {
            // green
                        j.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/Green Status.JPG"))); // 

        }
    }
}
