package com.google.glassware;


import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class GlassTimer implements Job{


   public static final String USERID = "user id";
	
   public void execute(JobExecutionContext context)
	        throws JobExecutionException {
	   
	   //JobDataMap data = context.getJobDetail().getJobDataMap();
       //CredentialSaver CS = data.get("CredentialSaver");
       
	  JobDataMap data = context.getJobDetail().getJobDataMap();
	  String userId = data.getString(USERID);
	  
	  try{
		  InsertCard IC = new InsertCard();
		  IC.sendBirthdayCard(AuthUtil.newAuthorizationCodeFlow().loadCredential(userId));
		  
	  }
	  catch(Exception e){
		  
	  }
	  
   }
   
}
	

