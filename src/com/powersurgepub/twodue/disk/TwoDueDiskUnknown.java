package com.powersurgepub.twodue.disk;

/**
   Holding tank for disk store yet to be identified.<p>
  
   This code is copyright (c) 2003 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 2003/12/31 - Originally written.
 */
public class TwoDueDiskUnknown 
    extends TwoDueDiskStore {
  
  /** Creates a new instance of TwoDueDiskUnidentified */
  public TwoDueDiskUnknown() {
    // System.out.println ("Constructing new TwoDueUnknown");
  }
  
  /**
    Indicates whether this is an instance of TwoDueUnknown.
   
    @return true if this is an instance of TwoDueUnknown.
   */
  public boolean isUnknown() {
    return true;
  }
  
  public boolean isAFile() {
    return false;
  }
  
  public boolean isAFolder() {
    return false;
  }
  
  public String getShortPath() {
    return "???/???";
  }
  
}
