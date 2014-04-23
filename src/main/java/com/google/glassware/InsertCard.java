package com.google.glassware;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.mirror.model.MenuItem;
import com.google.api.services.mirror.model.NotificationConfig;
import com.google.api.services.mirror.model.TimelineItem;

public class InsertCard {

	public InsertCard(){
		
	}
	
	  public void sendBirthdayCard(Credential credential){
		  Date date = new Date();
		  String html =  "<article class='auto-paginate'>"
			  		      + "<h2 class='blue text-large'>"+date.toString()+"</h2>"
			  		      + "<p>This is the card! "
			  		      + "</p><br/>"
			  		      + "</article>";
	  	
	  
	      TimelineItem timelineItem = new TimelineItem();
	      timelineItem.setHtml(html);

	      List<MenuItem> menuItemList = new ArrayList<MenuItem>();
	      menuItemList.add(new MenuItem().setAction("OPEN_URI").setPayload(
	          "https://www.google.com/search?q=cat+maintenance+tips"));
	      timelineItem.setMenuItems(menuItemList);

	      // Triggers an audible tone when the timeline item is received
	      timelineItem.setNotification(new NotificationConfig().setLevel("DEFAULT"));
	      
	      try{
	      MirrorClient.insertTimelineItem(credential, timelineItem);
	      }
	      catch(Exception e){
	  
	      }
	  }
	
}
