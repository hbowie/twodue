package com.powersurgepub.twodue.data;

  import com.powersurgepub.psutils.*;
  import com.powersurgepub.twodue.*;
  import java.io.*;
  import java.net.*;

/**
   A web page with a URL and the ability to validate its existence. <p>
  
   This code is copyright (c) 2004 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
      2004/03/07 - Originally written.
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 
      2004/03/07 - Originally written.
 */
public class WebPage 
    extends Thread {
  
  private   Logger      log;
  private   ToDoItem    item;
  private   URL         url;
  private   int         status = UNKNOWN;
  public static final int UNKNOWN = 0;
  public static final int VALID   = 1;
  public static final int INVALID = 2;
  private   String      error = "";
  private   TwoDueCommon td;
  
  /** 
    Creates a new instance of WebPage 
   */
  public WebPage(ThreadGroup group, ToDoItem item, Logger log, TwoDueCommon td) {
    super (group, item.getWebPage());
    this.item = item;
    this.log = log;
    this.td = td;
    // log.recordEvent (LogEvent.NORMAL, "Created WebPage for URL " + address, false);
  }
  
  /**
    Validate this URL.
   */
  public void run() {
    // log.recordEvent (LogEvent.NORMAL, "Checking URL " + address, false);
    try {
      url = new URL (item.getWebPage());
      // log.recordEvent (LogEvent.NORMAL, "Well formed URL " + address, false);
    } catch (MalformedURLException e) {
      status = INVALID;
      error = "Malformed URL";
      // log.recordEvent (LogEvent.NORMAL, "Malformed URL " + address, false);
    }
    if (status == UNKNOWN) {
      /* try {
        Thread.sleep(20);
      } catch (InterruptedException e) {
      } */
      // log.recordEvent (LogEvent.NORMAL, 
      //     "Protocol for " + address + " = " + url.getProtocol(), false);
      try {
        URLConnection handle = url.openConnection();
        if (url.getProtocol().equals ("http")) {
          HttpURLConnection httpHandle = (HttpURLConnection)handle;
          int response = httpHandle.getResponseCode();
          if (response == HttpURLConnection.HTTP_OK
              || response == HttpURLConnection.HTTP_MOVED_TEMP
              || response == HttpURLConnection.HTTP_FORBIDDEN
              || response == HttpURLConnection.HTTP_INTERNAL_ERROR) {
            status = VALID;
          } else {
            status = INVALID;
            error = "HTTP Response " + String.valueOf (response)
                + httpHandle.getResponseMessage();
          }
        } 
        else
        if (url.getProtocol().equals ("file")) {
          InputStream file = handle.getInputStream();
          file.close();
          status = VALID;
        } else {
          status = VALID;
        }
      }
      catch (SocketException e) {
        status = INVALID;
        error = "SocketException";
      }
      catch (IOException e) {
        status = INVALID;
        error = "IOException";
      }
      catch (Exception e) {
        status = INVALID;
        error = "Exception";
      }
    } // end if status not yet determined
    // log.recordEvent (LogEvent.NORMAL, 
    //     item.getWebPage() 
    //     + ((status == VALID) ? " Valid " : " Invalid ") 
    //     + error, 
    //     false);
    td.validateURLPageDone (item, (status == VALID));
  } // end run method
  
  public int getItemNumber () {
    return item.getItemNumber();
  }
  
  public boolean isValidationComplete () {
    return (status > UNKNOWN);
  }
  
  public boolean isInvalidURL () {
    return (status == INVALID);
  }
  
  public String toString() {
    return item.getWebPage();
  }
  
}
