package com.powersurgepub.twodue;

  import com.powersurgepub.twodue.data.*;
  import com.powersurgepub.twodue.disk.*;

/**
  An interface that must be enforced for all Tabs that are part of the 
  Two Due GUI. <p>
  
   This code is copyright (c) 2003-2004 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
      2003/11/01 - Originally written.
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 2003/11/01 - Originally written. 
 */
public interface TwoDueTab {
  
  /**
    Prepares the tab for processing of newly opened file.
   
    @param store Disk Store object for the file.
   */
  public void filePrep (TwoDueDiskStore store);
  
  /**
   Displays the item at the curent index.
   */
  public void displayItem() ;
  
  /**
   Modifies the td.item if anything on the screen changed. 
   
   @return True if any of the data changed on this tab. 
   */
  public boolean modIfChanged ();
  
}
