package com.powersurgepub.twodue.disk;

  import com.powersurgepub.psutils.*;
  import java.io.*;

/**
   Disk storage pointing to only a single Two Due file.<p>
  
   This code is copyright (c) 2003 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 2003/12/21 - Originally written.
 */
public class TwoDueFile 
    extends TwoDueDiskStore {
  
  /** 
    Creates a new instance of TwoDueFile.
   
    @param file The location of the Two Due file. 
   */
  public TwoDueFile(File file) {

    this.file = file;
    toDoFile = file;
    if (toDoFile != null) {
      constructToDoTDF();
      toDoFileXML = new File (file.getParent(), 
          TO_DO_FILE_NAME + "." + FILE_EXT_XML);
      if (toDoFileXML != null) {
        constructToDoRecsXML();
      }
    }
    
  }
  
  /**
    Indicates whether this is an instance of TwoDueUnknown.
   
    @return true if this is an instance of TwoDueUnknown.
   */
  public boolean isUnknown() {
    return false;
  }
  
  /**
    Indicates whether this is an instance of TwoDueFile or TwoDueFolder.
   
    @return true if this is an instance of TwoDueFile.
   */
  public boolean isAFile() {
    return true;
  }
  
  /**
    Indicates whether this is an instance of TwoDueFile or TwoDueFolder.
   
    @return true if this is an instance of TwoDueFolder.
   */
  public boolean isAFolder() {
    return false;
  }
  
  /**
    Return an abbreviated path to the disk store, to visually
    identify it to the user. 
   
    @return A string identifying the disk store to the user. 
   */
  public String getShortPath() {
    FileName name = new FileName (file);
    return (name.getFolder() + "/" + file.getName()); 
  }
  
  /**
    Returns the file of To Do Items.
   
    @return The file of To Do Items.
   */
  public File getToDoFile() {
    return file;
  }
  
}
