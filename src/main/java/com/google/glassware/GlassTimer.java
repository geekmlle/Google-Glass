package com.google.glassware;

import java.util.*;

import com.google.api.client.auth.oauth2.Credential;


public class GlassTimer extends TimerTask{

	private int hr;
	private int min;
	private MainServlet MS;
	private Credential credential;
	
	public GlassTimer(int hour, int min, MainServlet MS, Credential credential){
		this.hr = hour;
		this.min = min;
		this.MS = MS;
		this.credential = credential;
	}
  
   public void run(){

       Calendar cal = Calendar.getInstance(); 
       //this is the method you should use, not the Date(), because it is desperated.
       int hour = cal.get(Calendar.HOUR_OF_DAY);
       int minute = cal.get(Calendar.MINUTE);
       //get the hour number of the day, from 0 to 23
       if(hour == hr && minute == min){
    	   MS.sendBirthdayCard(credential);
       }
   }
}
	

