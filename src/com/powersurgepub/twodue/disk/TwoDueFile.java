/*
 * Copyright 2003 - 2013 Herb Bowie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.powersurgepub.twodue.disk;

  import com.powersurgepub.psutils.*;
  import java.io.*;

/**
   Disk storage pointing to only a single Two Due file.
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
