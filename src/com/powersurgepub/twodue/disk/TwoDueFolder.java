package com.powersurgepub.twodue.disk;

  import com.powersurgepub.psdatalib.tabdelim.*;
  import com.powersurgepub.psutils.*;
  import java.io.*;

/**
   Disk storage pointing to a folder containing a collection of files.<p>
  
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
public class TwoDueFolder 
    extends TwoDueDiskStore {
      
  public static final String TEMPLATE_NAME               = "template.html";
  public static final String DISK_DIR_NAME               = "files.txt";
  public static final String DISK_VIEWS_NAME             = "views.txt";
  public static final String ARCHIVE_FILE_NAME           = "archive";
  public static final String ARCHIVE_TEMPLATE_FILE_NAME  = "archive_template.html";
  public static final String UPDATE_LOG_FOLDER_NAME      = "updatelogs";
  
  /** 
    Creates a new instance of TwoDueFolder.
   
    @param file The location of the Two Due folder. 
   */
  public TwoDueFolder (File file) {

    this.file = file;
    if (file != null) {
      toDoFile = new File (file, TO_DO_FILE_NAME + "." + FILE_EXT);
      if (! toDoFile.exists()) {
        File toDoFile2 = new File (file, TO_DO_FILE_NAME + "." + FILE_EXT_OLD);
        if (toDoFile2.exists()) {
          toDoFile = toDoFile2;
        }
      }
      if (toDoFile != null) {
        constructToDoTDF();
      } // end if To Do data file not null
      
      toDoFileXML = new File (file, TO_DO_FILE_NAME + "." + FILE_EXT_XML);
      if (toDoFileXML != null) {
        constructToDoRecsXML();
      }
      templateFile = new File (file, TEMPLATE_NAME);
      diskDirFile = new File (file, DISK_DIR_NAME);
      if (diskDirFile != null) {
        diskDirTDF = new TabDelimFile (diskDirFile);
        diskDirTDF.setLog (log);
      }
      diskViewsFile = new File (file, DISK_VIEWS_NAME);
      if (diskViewsFile != null) {
        diskViewsTDF = new TabDelimFile (diskViewsFile);
        diskViewsTDF.setLog (log);
      }
      archiveFile = new File (file, ARCHIVE_FILE_NAME + "." + FILE_EXT);
      if (! archiveFile.exists()) {
        File archiveFile2 = new File (file, ARCHIVE_FILE_NAME + "." + FILE_EXT_OLD);
        if (archiveFile2.exists()) {
          archiveFile = archiveFile2;
        }
      }
      if (archiveFile != null) {
        constructArchiveTDF();
      }
      archiveNewFile = new File (file, ARCHIVE_FILE_NAME + "." + NEW_EXT);
      archiveBackupFile = new File (file, ARCHIVE_FILE_NAME + "." + BACKUP_EXT);
      archiveTemplateFile = new File (file, ARCHIVE_TEMPLATE_FILE_NAME);
      updateLogFolder = new File (file, UPDATE_LOG_FOLDER_NAME);
    } // end if parameter not null
  } // end constructor
  
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
    return false;
  }
  
  /**
    Indicates whether this is an instance of TwoDueFile or TwoDueFolder.
   
    @return true if this is an instance of TwoDueFolder.
   */
  public boolean isAFolder() {
    return true;
  }
  
  /**
    Return an abbreviated path to the disk store, to visually
    identify it to the user. 
   
    @return A string identifying the disk store to the user. 
   */
  public String getShortPath() {
    FileName name = new FileName (file);
    StringBuffer shortPath = new StringBuffer();
    int numberOfFolders = name.getNumberOfFolders();
    int i = numberOfFolders - 1;
    if (i < 0) {
      i = 0;
    }
    while (i <= numberOfFolders) {
      if (shortPath.length() > 0) {
        shortPath.append ('/');
      }
      shortPath.append (name.getFolder (i));
      i++;
    }
    if (isPrimary()) {
      shortPath.append (" (P)");
    }
    return (shortPath.toString()); 
  }
  
  /*
  public File getArchiveFile () {
    return new File (file, ARCHIVE_FILE_NAME + "." + FILE_EXT);
  }
  
  public File getArchiveNewFile () {
    return new File (file, ARCHIVE_FILE_NAME + "." + NEW_EXT);
  }
  
  public File getArchiveBackupFile () {
    return new File (file, ARCHIVE_FILE_NAME + "." + BACKUP_EXT);
  } */
  
}
