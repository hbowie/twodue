package com.powersurgepub.twodue.disk;

  import com.powersurgepub.psdatalib.tabdelim.*;
  import com.powersurgepub.psdatalib.psdata.*;
  import com.powersurgepub.psutils.*;
  import com.powersurgepub.twodue.*;
  import com.powersurgepub.twodue.data.*;
  import com.powersurgepub.xos2.*;
  import java.io.*;
  import javax.swing.*;
  import java.util.*;

/**
   A directory of disk stores known to this user. <p>
  
   This code is copyright (c) 2004 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 2004/01/03 - Originally written.
 */
  
public class TwoDueDiskDirectory {
      
  private static final String USER_NAME                   = "user.name";
  private static final String PRIMARY_DISK_STORE          = "primary.disk.store";
  private static final String RECENT_FILE_PREFIX          = "recent.file";
  private static final String RECENT_VIEW_PREFIX          = "recent.view";
  private static final String RECENT_TEMPLATE_PREFIX      = "recent.template";
  private static final String RECENT_PUBLISH_WHEN_PREFIX  = "recent.publish.when";
  
  private static final int    USER_PREFS_DISK_STORE_MAX   = 5;
  
  public  static final int    OPEN = 1;
  public  static final int    SAVE = 2;
  
  public  static final String TWO_DUE_FOLDER              = "TwoDue";
  private static final String TO_DO_FOLDER                = "to do list";
  
      
  private FileOpener              opener;
  private String                  userName;
  private UserPrefs               userPrefs;
  private String                  primaryFileName;
  private TwoDueDiskStore         primaryDiskStore;
  private TwoDueDiskStore         lastDiskStore;
  private ArrayList               dirList;
  private JMenu                   recentFilesMenu;
  
  private ViewList                views;
  
  private Logger                  log;
  
  /** 
    Creates a new instance of TwoDueDiskDirectory. 
   
    @param opener Object that implements the FileOpener interface, so
                  that it is capable of opening a to do file from a
                  passed disk store.
   
   */
  public TwoDueDiskDirectory (FileOpener opener, Logger log) {
    
    this.opener = opener;
    this.log = log;
    
    userName = System.getProperty (USER_NAME);
    
    dirList = new ArrayList();
    recentFilesMenu = new JMenu ("Open Recent");
    
    // If a primary file for this user is available from the user preferences,
    // then try to load the disk directory from this source; otherwise, try to 
    // load the disk directory directly from the user preferences.
    
    userPrefs = UserPrefs.getShared();
    boolean dirLoaded = false;
    TwoDueDiskStore nextStore;
    
    primaryFileName = userPrefs.getPref (userName + "." + PRIMARY_DISK_STORE);
    if (primaryFileName != null
        && primaryFileName.length() > 0) {
      primaryDiskStore = makeDiskStore (new File (primaryFileName));
      primaryDiskStore.setPrimary (true);
      // remember (primaryDiskStore);
      dirLoaded = loadDiskDir (primaryDiskStore);
    } // end if primary disk store available from user preferences
    
    // If we couldn't load the disk directory from the primary disk store,
    // then try to obtain the list directly from the user preferences
    if (! dirLoaded) {
      for (int i = 0; i < USER_PREFS_DISK_STORE_MAX; i++) {
        String recentFileName = getUserPrefFile (i);
        String recentFileView = getUserPrefView (i);
        String templateFileName = getUserPrefTemplate (i);
        int publishWhen = getUserPrefPublishWhen (i);
        if (recentFileName != null
            && recentFileName.length() > 0) {
          File recentFile = new File (recentFileName);
          nextStore = makeDiskStore (recentFile);
          nextStore.setCompareOption (recentFileView);
          if (templateFileName != null 
              && templateFileName.length() > 0) {
            File templateFile = new File (templateFileName);
            nextStore.setTemplate (templateFile);
            nextStore.setPublishWhen (publishWhen);
          }
          // remember (nextStore);
          if (i == 0) {
            lastDiskStore = nextStore;
          }
        } // end if we have a file name
      } // end for every recent file stored in user prefs
    } // end if directory not loaded from primary disk store
    
    views = new ViewList (this);
    if (primaryDiskStore == null) {
      views.setViewIndex 
          (0, TwoDueCommon.VIEW_TRIGGER_NEW_FILE);
    } else {
      views.setViewIndex 
          (primaryDiskStore.getViewIndex(), TwoDueCommon.VIEW_TRIGGER_SELECT_FILE);
    }
    
  } // end constructor
  
  public void setLog (Logger log) {
    this.log = log;
  }
  
  public Logger getLog () {
    return log;
  }
  
  /**
    Method that returns an instance of TwoDueFile
    or TwoDueFolder, depending on whether the passed data record
    refers to a file or a directory.
   
    @return An instance of TwoDueFile if the input is a normal file,
            an instance of TwoDueFolder if the input is a directory,
            or null if the input is neither a normal file or a directory.
   
    @param  storeRec A data record defining a Two Due Disk Store.
   */
  public TwoDueDiskStore makeDiskStore (DataRecord storeRec) {
    
    TwoDueDiskStore store 
        = makeDiskStore (new File (storeRec.getFieldData (TwoDueDiskStore.FILE)));
    store.setMultiple (storeRec);
    return store;
    
  }
  
  /**
    Method that returns an instance of TwoDueFile
    or TwoDueFolder, depending on whether the passed file
    refers to a file or a directory.
   
    @return An instance of TwoDueFile if the input is a normal file,
            an instance of TwoDueFolder if the input is a directory,
            or null if the input is neither a normal file or a directory.
   
    @param  file A file or directory that has been selected by the user
                 as a location to use for storing TwoDue data.
   */
  public TwoDueDiskStore makeDiskStore (File file) {
    
    // System.out.println ("TwoDueDiskDirectory makeDiskStore " + file.toString());
    TwoDueDiskStore store;
    if (file == null) {
      store = new TwoDueDiskUnknown();
    }
    else
    if (file.exists()) {
      if (file.isFile()) {
        File folder = new File (file.getParent());
        String[] peers = folder.list();
        int hits = 0;
        for (int i = 0; i < peers.length; i++) {
          String peer = peers[i];
          if ((peer.equals (TwoDueFolder.TO_DO_FILE_NAME + "." 
                + TwoDueFolder.FILE_EXT))
              || (peer.equals (TwoDueFolder.TO_DO_FILE_NAME + "." 
                + TwoDueFolder.FILE_EXT_OLD))
              || (peer.equals (TwoDueFolder.TO_DO_FILE_NAME + "." 
                + TwoDueFolder.BACKUP_EXT))
              || (peer.equals (TwoDueFolder.DISK_DIR_NAME))
              || (peer.equals (TwoDueFolder.DISK_VIEWS_NAME))
              || (peer.endsWith (".tcz"))
              || (peer.endsWith (".html"))
              || (peer.endsWith (".htm"))
              || (peer.endsWith (".xml"))) {
            hits++;
          } // end if recognized file type
        } // end for every file in parent folder
        // System.out.println ("peers length = " + String.valueOf(peers.length));
        // System.out.println ("hits = " + String.valueOf(hits));
        if ((file.toString().endsWith (".tdu")
            || file.toString().endsWith (".txt"))
            && (peers.length - hits) <= 2) {
          store = new TwoDueFolder (folder);
        } else {
          store = new TwoDueFile (file);
        }
      }
      else
      if (file.isDirectory()) {
        store = new TwoDueFolder (file);
      } else {
        store = new TwoDueDiskUnknown();
      }
    } else {
      int extLength = file.getName().length() - file.getName().lastIndexOf ('.') - 1;
      if (extLength == 3 || extLength == 4) {
        store = new TwoDueFile (file);
      } else {
        store = new TwoDueFolder (file);
      }
    } // end if file not null but file/folder does not already exist
    store.setLog (log);
    if (! store.isUnknown()) {
      remember (store);
    }
    // System.out.println ("Two Due file = " + store.getToDoFile().toString());
    return store;
  } // end method
  
  /**
    Method to ask the user to choose a disk location (file or folder) 
    for a list of to do items.
   
    @return A disk storage location.
   
    @param option Indicates whether an existing file/folder is to be opened,
                  or a new one created. 
   
    @param frame  Frame providing the context for the file chooser. 
   
    @param store  The existing disk storage location to use as a starting
                  point for the user's navigation, and for existing
                  comparator and selector options. 
   */
  public TwoDueDiskStore chooseDiskStore 
      (int option, JFrame frame, TwoDueDiskStore store) {

    if (option == OPEN) {
      return openDiskStore (frame, store);
    }
    else
    if (option == SAVE) {
      return saveDiskStore (frame, store);
    } else {
      return new TwoDueDiskUnknown();
    }
  } // end method
  
  /**
   Return a standard, default location for a starting Two Due folder.
   */
  public TwoDueDiskStore getUsualLocation () {
        
    Trouble trouble = Trouble.getShared();
        
    TwoDueDiskStore newStore = new TwoDueDiskUnknown();
    File twoDue = new File (Home.getShared().getUserDocs(), TWO_DUE_FOLDER);
    if (! twoDue.exists()) {
      try {
        boolean ok = twoDue.mkdir();
        if (! ok) {
          trouble.report ("Trouble creating new folder " + twoDue.toString(), "Mkdir Problem");
          return newStore;
        }
      } catch (SecurityException sx) {
        trouble.report ("Access Denied trying to create new folder " + twoDue.toString(), 
            "Security Problem");
        return newStore;
      }
    }
    
    File todo = new File (twoDue, TO_DO_FOLDER);
    if (! todo.exists()) {
      try {
        boolean ok = todo.mkdir();
        if (! ok) {
          trouble.report ("Trouble creating new folder " + todo.toString(), "Mkdir Problem");
          return newStore;
        }
      } catch (SecurityException sx) {
        trouble.report ("Access Denied trying to create new folder " + todo.toString(), 
            "Security Problem");
        return newStore;
      }
    }

    newStore = makeDiskStore (todo);
    
    return newStore;
    
  } // end method
  
  /**
    Method to ask the user to choose an existing disk location (file or folder) 
    to open.
   
    @return A disk storage location.
   
    @param frame  Frame providing the context for the file chooser. 
   
    @param store  The existing disk storage location to use as a starting
                  point for the user's navigation, and for existing
                  comparator and selector options. 
   */
  public TwoDueDiskStore openDiskStore 
      (JFrame frame, TwoDueDiskStore store) {
    XFileChooser chooser = new XFileChooser ();
    chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); 
    if (store != null
        && (! store.isUnknown())) {
      chooser.setCurrentDirectory (store.getToDoFile());
    }
    File result = chooser.showOpenDialog (frame);
    if (result != null) {
      File openFile = chooser.getSelectedFile();
      TwoDueDiskStore openStore = get (openFile);
      if (openStore == null) {
        openStore = makeDiskStore (openFile);
        if (openStore.isPrimary()) {
          loadDiskDir (openStore);
        }
      }
      return openStore;
    }
    return new TwoDueDiskUnknown();
  } // end method
  
  /**
    Loads the directory of disk files from a previously stored disk file.
   
    @return True if the passed Disk Store's disk directory was read
            successfully.
   
    @param  The disk store from which the saved disk directory is to be read.
   */
  private boolean loadDiskDir (TwoDueDiskStore diskStore) {
    boolean dirLoaded = false;
    TwoDueDiskStore nextStore;
    if (diskStore.isDiskDirFileValidInput()) {
      dirLoaded = true;
      TabDelimFile dir = diskStore.getDiskDirTDF();
      log.recordEvent (LogEvent.NORMAL, 
          "Known files source = " + dir.toString(),
          false);
      dir.setLog (log);
      DataRecord next = null;
      File nextFile = null;
      try {
        dir.openForInput();
      } catch (IOException e) {
        System.out.println ("Trouble opening Disk Directory for "
            + diskStore.toString());
        dirLoaded = false;
      }
      while (! dir.isAtEnd()) {
        try {
          next = dir.nextRecordIn();
        } catch (IOException e) {
          System.out.println ("Trouble reading Disk Directory for "
              + diskStore.toString());
          dirLoaded = false;
        }
        if (next != null) {
          nextFile = new File (next.getFieldData (TwoDueDiskStore.FILE));
          if (primaryDiskStore.equals (nextFile)) {
            primaryDiskStore.setMultiple (next);
          } else {
            nextStore = makeDiskStore (next);
            // remember (nextStore);
          }
        } // end if next data rec not null
      } // end while more items in data source
    } // end if disk directory file available
    return dirLoaded;
  } // end method loadDiskDir
  
  /**
    Method to ask the user to choose a new disk location (file or folder) 
    for a list of to do items.
   
    @return A new disk storage location.
   
    @param frame  Frame providing the context for the file chooser. 
   
    @param store  The existing disk storage location to use as a starting
                  point for the user's navigation, and for existing
                  comparator and selector options. 
   */
  public TwoDueDiskStore saveDiskStore 
      (JFrame frame, TwoDueDiskStore store) {
        
    Trouble trouble = Trouble.getShared();
        
    String newFolderDefault = "";
    File folder = new File(System.getProperty (GlobalConstants.USER_DIR));
    File home = new File(System.getProperty (GlobalConstants.USER_HOME)); 
    File docs = new File(home, "My Documents");
    if (docs == null 
        || (! docs.exists())) {
      docs = new File(home, "Documents");
    }
    TwoDueDiskStore newStore = new TwoDueDiskUnknown();;

    XFileChooser chooser = new XFileChooser ();
    chooser.setFileSelectionMode(XFileChooser.FILES_AND_DIRECTORIES); 
    if (store != null
        && (! store.isUnknown())) {
      chooser.setCurrentDirectory (store.getToDoFile());
    } 
    else
    if (docs != null
        && docs.exists()
        && docs.isDirectory()
        && docs.canRead()) {
      chooser.setCurrentDirectory (docs);
      newFolderDefault = TWO_DUE_FOLDER;
    } else {
      chooser.setCurrentDirectory (home);
      newFolderDefault = TWO_DUE_FOLDER;
    }
    int progress = 1;
    int fileFolder = 0;
    int result = 0;
    
    while (progress > 0 && progress < 9) {
      switch (progress) {
        
        case 1:
          // See if user wants to save as a file or a folder
          Object[] fileFolderOptions = { "Folder", "File", "Cancel" };
          fileFolder = JOptionPane.showOptionDialog
              (frame, 
              "Save As a File or a Folder?"
              + GlobalConstants.LINE_FEED
              + GlobalConstants.LINE_FEED
              + "(Folders are recommended)", 
              "Save As / Step 1",
              JOptionPane.YES_NO_CANCEL_OPTION, 
              JOptionPane.QUESTION_MESSAGE,
              null, fileFolderOptions, fileFolderOptions[0]);
          switch (fileFolder) {
            case 0:
              // Save as folder
              progress = 2;
              break;
            case 1:
              // Save as file
              progress = 8;
              break;
            case 2:
              // Cancel 
              progress = 0;
              break;
          }
          break;
          
        case 2:
          // Save as a folder -- let user choose closest parent
          // or actual target
          chooser.setFileSelectionMode (XFileChooser.DIRECTORIES_ONLY);
          chooser.setDialogTitle ("Save As / Step 2 of 3 / Specify Parent or Target Folder");
          folder = chooser.showOpenDialog (frame);
          if (folder != null) {
            progress = 3;
          } else {
            progress = 1;
          }
          break;
          
        case 3:
          // See if user wants to create a new folder
          File testFolder = null;
          String startingDefaultFolder = newFolderDefault;
          String suffix = "";
          int counter = 0;
          do {
            newFolderDefault = startingDefaultFolder + suffix;
            testFolder = new File (folder, newFolderDefault);
            counter++;
            suffix = String.valueOf(counter).trim();
          } while (testFolder.exists());
          String newFolderString = (String)JOptionPane.showInputDialog (
              frame,
              "Specify new folder, or leave blank to use as is:"
              + GlobalConstants.LINE_FEED
              + folder.toString(),
              // "Save As / Step 3 of 3 / Optionally Specify New Folder",
              // JOptionPane.QUESTION_MESSAGE
              newFolderDefault
              );
          if (newFolderString == null) {
            progress--;
          } 
          else 
          if (newFolderString.length() > 0) {
            File newFolder = new File (folder, newFolderString);
            try {
              if (newFolder.isDirectory()) {
                // use as is
                folder = newFolder;
                progress = 9;
              } else {
                boolean ok = newFolder.mkdir();
                if (ok) {
                  folder = newFolder;
                  progress = 9;
                } else {
                  trouble.report ("Trouble creating new folder", "Mkdir Problem");
                  progress--;
                }
              }
            } catch (SecurityException sx) {
              trouble.report ("Access Denied", "Security Problem");
              progress--;
            }
          } else {
            // Use existing folder as specified
            progress = 9;
          }
          break;
          
        case 8:
          // Save as a file
          chooser.setFileSelectionMode (XFileChooser.FILES_ONLY);
          chooser.setDialogTitle ("Save As / Step 2 of 2 / Specify New Output File");
          File file = chooser.showSaveDialog (frame);
          if (file != null) {
            FileName fileName = new FileName (file);
            if (fileName.getExt().length() == 0) {
              file = new File (fileName.getPath() 
                + "/" + fileName.replaceExt (TwoDueDiskStore.FILE_EXT));
            }
            newStore = makeDiskStore (chooser.getSelectedFile());
            progress = 9;
          } else {
            progress = 1;
          }
          break;
      } // end primary switch
    } // end while loop
    
    if (progress > 0) {
      if (fileFolder == 0) {
        newStore = makeDiskStore (folder);
      }
      newStore.setCompareOption (store.getCompareString());
      newStore.setSelectOption  (store.getSelectString());
      newStore.setPublishWhen (store.getPublishWhen());
      newStore.setTemplate (store.getTemplate());
    }
    
    return newStore;
    
  } // end method
  
  /**
    Get a JMenu object of all known files (files in the directory list).
   
    @return Menu of all known files.
   */
  public JMenu getRecentFilesMenu () {
    return recentFilesMenu;
  }

  /**
    Make sure this disk store
    is saved in the directory list. 
   
    @param store Disk store to be added, or used to update an existing 
                 disk store already in the list.
  */
  public void remember (TwoDueDiskStore store) {
    if (store.isForgettable()) {
      // don't remember it if we said to forget about it
    } else {
      if (store.isAFolder()
          && primaryDiskStore == null) {
        store.setPrimary (true);
      }
      int i = 0;
      while (i < dirList.size()
         && (store.compareTo(get(i)) > 0)) {
       i++;
      }
      if (i >= dirList.size()) {
        dirList.add (store);
        recentFilesMenu.add (makeMenuItem (store));
      }
      else
      if (store.compareTo(get(i)) < 0) {
        dirList.add (i, store);
        recentFilesMenu.insert (makeMenuItem (store), i);
      } 
      else {
        TwoDueDiskStore old = get(i);
        old.setPrimary(store.isPrimary());
      }
      if (store.isPrimary()) {
        if (primaryDiskStore == null) {
          primaryDiskStore = store;
        }
        else
        if (! primaryDiskStore.equals (store)) {
          primaryDiskStore.setPrimary (false);
          primaryDiskStore = store;
          views.saveViews();
          for (i = 0; i < dirList.size(); i++) {
            TwoDueDiskStore nextStore = (TwoDueDiskStore)dirList.get(i);
            JMenuItem nextMenuItem = (JMenuItem)recentFilesMenu.getMenuComponent(i);
            nextMenuItem.setText(nextStore.getShortPath());
          }
        } // end if primary disk store points to a different disk location
      } // end if this store claims to be primary
    } // end if this disk store not forgettable
  } // end method remember
   
  /**
    Make a new Menu item for a new disk store.
   
    @param  store Definition of recent disk store accessed.
   */
  private JMenuItem makeMenuItem (TwoDueDiskStore store) {
    
    JMenuItem storeMenuItem = new JMenuItem(store.getShortPath());
    storeMenuItem.setActionCommand (store.getPath());
    storeMenuItem.setToolTipText (store.getPath());
    storeMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        fileRecentMenuItemActionPerformed(evt);
      }
    });
    return storeMenuItem;   
  } // end method
   
   /**
    Drop this disk store from the directory list. 
   
    @param store Disk store to be added, or used to update an existing 
                 disk store already in the list.
   */
   public void forget (TwoDueDiskStore store) {
     int i = 0;
     while (i < dirList.size()
        && (store.compareTo(get(i)) > 0)) {
      i++;
     }
     if (i < dirList.size()
        && store.equals (get(i))) {
       dirList.remove (i);
       recentFilesMenu.remove (i);
     }

     if ((primaryDiskStore != null)
           && (primaryDiskStore.equals (store))) {
         primaryDiskStore = null;
     } // end if this store was primary
   } // end method forget
  
   /**
      Save disk directory to disk (either as a tab-delimited file, or
      in user prefs).
    */
  public void saveToDisk () {

    if (primaryDiskStore == null) {
      for (int i = 0; i < USER_PREFS_DISK_STORE_MAX; i++) {
        if (i < dirList.size()
            && (! get(i).isForgettable())) {
          setUserPrefFile (get (i), i);
        } // end if disk store is available
      } // end for each disk store slot available in user prefs
      userPrefs.savePrefs();
    } else { // primary disk store is available
      TwoDueDiskStore nextStore;
      DataRecord nextRec;
      RecordDefinition recDef = TwoDueDiskStore.getRecDef();
      boolean ok = true;
      TabDelimFile dir = primaryDiskStore.getDiskDirTDF();
      try {
        dir.openForOutput (recDef);
      } catch (IOException e) {
        ok = false;
      }
      if (ok) {
        for (int i = 0; i < dirList.size(); i++) {
          nextStore = get (i);
          if (! get(i).isForgettable()) {
            nextRec = nextStore.getDataRec (recDef);
            try {
              dir.nextRecordOut (nextRec);
            } catch (IOException e2) {
              ok = false;
            }
          }
        } // end for loop
      } // end if open ok
      try {
        dir.close();
      } catch (IOException e) {
      }
      if (ok) {
        userPrefs.setPref (
          userName + "." + PRIMARY_DISK_STORE, 
          primaryDiskStore.getPath());
      }
      
      views.saveViews();
      
    } // end if primary disk store is available
    
  } // end saveToDisk method
  
  /**
    Get default data store -- primary, if one is identified, or first one 
    loaded from user prefs, or first file in list.
   
    @return Default data store to be opened automatically at startup.
   */
  public TwoDueDiskStore getDefaultStore() {
    if (primaryDiskStore != null) {
      return primaryDiskStore;
    }
    else
    if (lastDiskStore != null) {
      return lastDiskStore;
    }
    else
    if (dirList.size() > 0) {
      return get(0);
    } else {
      return null;
    }
  }
  
  /**
    Get primary file.
   
    @return Primary to do disk store for this user, or null if none available.
   */
  public TwoDueDiskStore getPrimaryStore() {
    return primaryDiskStore;
  }
  
  /**
    Get disk store, given a File identifier.
   
    @return The disk store with this path in the directory list, or 
            null if no known disk store has this path.
   
    @param  file Pointer to the disk store location.
   */
  public TwoDueDiskStore get (File file) {
    int i = 0;
    while (i < dirList.size()
        && (! (get(i).equals(file)))) {
      i++;
    }
    if (i < dirList.size()) {
      return get(i);
    } else {
      return null;
    }
  }
  
  /**
    Get disk store, given a disk store path.
   
    @return The disk store with this path in the directory list, or 
            null if no known disk store has this path.
   
    @param  path Path to the disk store location.
   */
  public TwoDueDiskStore get (String path) {
    int i = 0;
    while (i < dirList.size()
        && (! ((get(i)).getPath().equals(path)))) {
      i++;
    }
    if (i < dirList.size()) {
      return get(i);
    } else {
      return null;
    }
  }
  
  /**
    Get disk store, given an index value.
   
    @return The disk store at this location in the directory list, or 
            null if the index does not point to a valid location.
   
    @param  index Index value pointing to a location in the directory list.
   */
  public TwoDueDiskStore get (int index) {
    if (index < 0 || index >= dirList.size()) {
      return null;
    } else {
      return (TwoDueDiskStore)dirList.get(index);
    }
  }
  
  /**
    Get size of directory (number of disk stores).
   
    @return Number of disk stores in the directory.
   */
  public int size() {
    return dirList.size();
  }
  
  /** 
    Get recent file from user prefs, given an index value.
   
    @return Name of file from user prefs.
    @param  index Pointer to an entry on recent files list. 
   */
  private String getUserPrefFile (int index) {
    String digit = String.valueOf(index).trim();
    return userPrefs.getPref (RECENT_FILE_PREFIX + digit);
  }
  
  /** 
    Get recent view options from user prefs, given an index value.
   
    @return View options, from user prefs.
    @param  index Pointer to an entry on recent files list. 
   */
  private String getUserPrefView (int index) {
    String digit = String.valueOf(index).trim();
    return userPrefs.getPref (RECENT_VIEW_PREFIX + digit);
  }
  
  public ViewList getViews () {
    return views;
  }
  
  /** 
    Get template for recent file from user prefs, given an index value.
   
    @return Name of template file from user prefs.
    @param  index Pointer to an entry on recent files list. 
   */
  private String getUserPrefTemplate (int index) {
    String digit = String.valueOf(index).trim();
    return userPrefs.getPref (RECENT_TEMPLATE_PREFIX + digit);
  }
  
  /** 
    Get publishing frequency for recent file from user prefs, 
    given an index value.
   
    @return Indicator of publishing frequency from user prefs.
    @param  index Pointer to an entry on recent files list. 
   */
  private int getUserPrefPublishWhen (int index) {
    String digit = String.valueOf(index).trim();
    String pubStr = userPrefs.getPref (RECENT_PUBLISH_WHEN_PREFIX + digit);
    int pub;
    try {
      pub = Integer.parseInt (pubStr);
    } catch (NumberFormatException e) {
      pub = 0;
    }
    return pub;
  }
  
  /**
    Set the contents of the user preferences, based on the contents
    of a disk store object.
   
    @param store Disk Store to be saved in user preferences.
    @param index Index position within user prefs to be used
                 for storage.
   */
  private void setUserPrefFile (TwoDueDiskStore store, int index) {
    setUserPrefFile (store.getFile(), index);
    setUserPrefView (store.getComparator().toString(), index);
    setUserPrefTemplate (store.getTemplate(), index);
    setUserPrefPublishWhen (store.getPublishWhen(), index);
  }
  
  /** 
    Store recent file in user prefs, given an index value.
   
    @param  file  File definition of recent file accessed.
    @param  index Pointer to an entry on recent files list. 
   */
  private void setUserPrefFile (File file, int index) {
    String digit = String.valueOf(index).trim();
    userPrefs.setPref (RECENT_FILE_PREFIX + digit, file.getAbsolutePath());
  }
  
  /** 
    Store recent view options in user prefs, given an index value.
   
    @param  view  View options for recent file accessed.
    @param  index Pointer to an entry on recent files list. 
   */
  private void setUserPrefView (String view, int index) {
    String digit = String.valueOf(index).trim();
    userPrefs.setPref (RECENT_VIEW_PREFIX + digit, view);
  }
  
  /** 
    Store recent template in user prefs, given an index value.
   
    @param  template  File definition of recent template accessed.
    @param  index Pointer to an entry on recent files list. 
   */
  private void setUserPrefTemplate (File template, int index) {
    String digit = String.valueOf(index).trim();
    String templateName;
    if (template == null) {
      templateName = "";
    } else {
      templateName = template.getAbsolutePath();
    }
    userPrefs.setPref (RECENT_TEMPLATE_PREFIX + digit, templateName);
  }
  
  /** 
    Store recent publication frequency in user prefs, 
    given an index value.
   
    @param  publishWhen  Indicator of publishing frequency 
                         for recently accessed file.
    @param  index Pointer to an entry on recent files list. 
   */
  private void setUserPrefPublishWhen (int publishWhen, int index) {
    String digit = String.valueOf(index).trim();
    userPrefs.setPref (RECENT_PUBLISH_WHEN_PREFIX + digit, 
        String.valueOf(publishWhen));
  }
  
  /**
    Action listener for recent file menu items.
   
    @param evt = Action event.
   */
  private void fileRecentMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    String path = evt.getActionCommand();
    TwoDueDiskStore store = get (path);
    opener.handleOpenFile (store);
  } // end method
  
} // end class

