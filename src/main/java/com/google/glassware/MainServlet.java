/*
 * Copyright (C) 2013 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.glassware;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpHeaders;
import com.google.api.services.mirror.model.Command;
import com.google.api.services.mirror.model.Contact;
import com.google.api.services.mirror.model.MenuItem;
import com.google.api.services.mirror.model.MenuValue;
import com.google.api.services.mirror.model.NotificationConfig;
import com.google.api.services.mirror.model.TimelineItem;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles POST requests from index.jsp
 *
 * @author Jenny Murphy - http://google.com/+JennyMurphy
 * @author Modified by Diana Melara - http://github.com/geekmlle
 */
 
public class MainServlet extends HttpServlet {

  /**
   * Private class to process batch request results.
   * <p/>
   * For more information, see
   * https://code.google.com/p/google-api-java-client/wiki/Batch.
   */
   
  private final class BatchCallback extends JsonBatchCallback<TimelineItem> {
    private int success = 0;
    private int failure = 0;

    @Override
    public void onSuccess(TimelineItem item, HttpHeaders headers) throws IOException {
      ++success;
    }

    @Override
    public void onFailure(GoogleJsonError error, HttpHeaders headers) throws IOException {
      ++failure;
      LOG.info("Failed to insert item: " + error.getMessage());
    }
  }

  private static final Logger LOG = Logger.getLogger(MainServlet.class.getSimpleName());
  public static final String CONTACT_ID = "com.google.glassware.contact.java-quick-start";
  public static final String CONTACT_NAME = "Java Quick Start";
  private CronManager cm = null;
  
 
  /**
   * Do stuff when buttons on index.jsp are clicked
   */
   
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

    String userId = AuthUtil.getUserId(req);
    //Credential credential = AuthUtil.newAuthorizationCodeFlow().loadCredential(userId);
    String message = "";
 	
	 if (req.getParameter("operation").equals("sendBirthday")) {
		 
		 int minute = Integer.parseInt(req.getParameter("selectMinute")); 
		 int hour = Integer.parseInt(req.getParameter("selectHour"));  
		 boolean dateCheckbox = req.getParameter("InputBirthdate") != null;
		 int month = Integer.parseInt(req.getParameter("bdayMonth"));
		 int day = Integer.parseInt(req.getParameter("bdayDay"));
		 
		 if(cm==null){
			 
			if(dateCheckbox==true){
				 cm = new CronManager(hour,minute,day,month,userId);
			 }else{
				 cm = new CronManager(hour,minute,0,0,userId);
			 }
			 
			 try {
				cm.run();
		     } catch (Exception e) {
				message = "Something bad happened. Couldn't start the timer.";		
				e.printStackTrace();
		     }
			 message = "Timer started for notifications at "+hour+":"+minute+" (24HR Time)";
		 }
		 else{
			 message = "Timer has already started. Please stop notifications to start again.";
		 }
		 
	} 
	else
	if (req.getParameter("operation").equals("stopNotifications")) {
	 
		if(cm!=null && cm.isRunning()){
			cm.stopNotifications();
			message = "Timer Stopped.";
			cm = null;
		}
		else{
			message = "Can't stop timer. Timer hasn't started.";
		}
		
	}
	else {
      String operation = req.getParameter("operation");
      LOG.warning("Unknown operation specified " + operation);
      message = "I don't know how to do that";
      
    }
    WebUtil.setFlash(req, message);
    res.sendRedirect(WebUtil.buildUrl(req, "/"));
  }
  
  
  
}
