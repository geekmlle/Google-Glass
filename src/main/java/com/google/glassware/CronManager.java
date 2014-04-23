package com.google.glassware;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import java.util.Date;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;


public class CronManager {

	private Scheduler sched = null;
	private boolean running = false; 
	private int hour;
	private int minute;
	private int day;
	private int month;
	private String UserID;
	
	public CronManager(int hour, int minute, int day, int month, String UserID){
		this.hour = hour;
		this.minute = minute;
		this.day = day;
		this.month = month;
		this.UserID = UserID;
	}
	
	public void run() throws Exception {
        
        SchedulerFactory sf = new StdSchedulerFactory();
        sched = sf.getScheduler();

        JobDetail job = newJob(GlassTimer.class)
            .withIdentity("job1", "group1")
            .build();
        
        // Seconds, Minutes, Hours, Day, Month, Year.
        String cronScheduler;
        if(day==0 && month == 0){
        	cronScheduler = "0 "+minute+" "+hour+" * * ?";
        }else{
        	cronScheduler = "0 "+minute+" "+hour+" "+day+" "+month+" ?";
        }
        
        CronTrigger trigger = newTrigger()
            .withIdentity("trigger1", "group1")
            .withSchedule(cronSchedule(cronScheduler))
            .build();
        
        job.getJobDataMap().put(GlassTimer.USERID, UserID);
        
        Date ft = sched.scheduleJob(job, trigger);

        sched.start();

       /* try {
            // wait five minutes to show jobs
            Thread.sleep(300L * 1000L);
            // executing...
        } catch (Exception e) {
        }*/

        running = true;
        
    }
	
	public void stopNotifications(){
        try {
			sched.shutdown(true);
	        running = false;
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isRunning(){
		return running;
	}
	
}
