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

package com.powersurgepub.twodue;

  import com.powersurgepub.psdatalib.elements.*;
  import com.powersurgepub.psdatalib.ui.*;
  import com.powersurgepub.psdatalib.txbio.*;
  import com.powersurgepub.psdatalib.tabdelim.*;
  import com.powersurgepub.psdatalib.pstextio.*;
  import com.powersurgepub.psdatalib.psdata.*;
  import com.powersurgepub.psdatalib.textmerge.*;
  import com.powersurgepub.psfiles.*;
  import com.powersurgepub.pstextmerge.*;
  import com.powersurgepub.psutils.*;
  import com.powersurgepub.twodue.data.*;
  import com.powersurgepub.twodue.disk.*;
  import com.powersurgepub.xos2.*;
  import java.awt.*;
  import java.awt.datatransfer.*;
  import java.awt.event.*;
  import java.io.*;
  import java.net.*;
  // import javax.mail.*;
  import javax.swing.*;
  import javax.swing.event.*;
  import java.text.*;
  import java.util.*;

/**
   An object accessible to most classes within the twodue package,
   that intentionally exposes its data for direct access. <p>
  
 */
public class TwoDueCommon 
    implements 
      ActionListener, 
      AppToBackup,
      ChainLink, 
      ClipboardOwner { 
  
  static final int LIST_TAB_INDEX     = 0;
  static final int TREE_TAB_INDEX     = 1;
  static final int ITEM_TAB_INDEX     = 0;
  static final int TIME_TAB_INDEX     = 1;
  static final int RECURS_TAB_INDEX   = 2;
  static final int ROTATE_TAB_INDEX   = 3;
  static final int VIEW_TAB_INDEX     = 4;
  static final int WEB_TAB_INDEX      = 5;
  static final int PURGE_TAB_INDEX    = 6;
  
  static final String LAST_FILE       = "last.file";
  
  static final int DEMO_MAX_RECS = -1;
  
  static final String SELECT_OPTION = "selectoption";
  static final String SORT_FIELD_1  = "sortfield1";
  static final String SORT_FIELD_2  = "sortfield2";
  static final String SORT_FIELD_3  = "sortfield3";
  static final String SORT_FIELD_4  = "sortfield4";
  static final String SORT_FIELD_5  = "sortfield5";
  static final String AUTO_SAVE     = "autosave";
  
  public static final int VIEW_TRIGGER_PROGRAM_START              = 0;
  public static final int VIEW_TRIGGER_NEW_FILE                   = 1;
  public static final int VIEW_TRIGGER_SELECT_FILE                = 2;
  public static final int VIEW_TRIGGER_LEAVING_VIEW_TAB           = 3;
  public static final int VIEW_TRIGGER_SELECT_VIEW_FROM_MENU      = 4;
  public static final int VIEW_TRIGGER_SELECT_VIEW_FROM_COMBO_BOX = 5;
  public static final int VIEW_TRIGGER_VIEW_NEW                   = 6;
  public static final int VIEW_TRIGGER_VIEW_REMOVE                = 7;
  public static final int VIEW_TRIGGER_ARCHIVING                  = 8;
  public static final int VIEW_TRIGGER_DONE_ARCHIVING             = 9;
  
  XOS                 xos           = XOS.getShared();
  
  static final String QUICK_START  
      = "userguide/products/twodue/quickstart.html";
  
  static final int    ONE_SECOND    = 1000;
  static final int    ONE_MINUTE    = ONE_SECOND * 60;
  static final int    ONE_HOUR      = ONE_MINUTE * 60;
  
  static final String LEFT          = "left";
  static final String TOP           = "top";
  static final String WIDTH         = "width";
  static final String HEIGHT        = "height";
  
  
  /** Various system properties. */
  String              userName;
  String              userDirString;
  String              fileSeparatorString;
  
  // Global variables
  Trouble             trouble;
  Home                home;
  UserPrefs           userPrefs;
  File                appFolder;
  TwoDueDiskDirectory files;
  ViewList            views;
  
  boolean             selItemTab = true;
  
  public static final String APP_ICON_PATH = "images/twodue_128.gif";
  
  // File stuff
  // File                tdfFile;
  // TabDelimFile        tdf;
  // boolean             fileNowOpen = false;
  TwoDueDiskStore     diskStore = new TwoDueDiskUnknown();
  File                importFile;
  // Template            template;
  // int                 publishWhen = 0;
  // static final int    PUBLISH_ON_CLOSE = 1;
  // static final int    PUBLISH_ON_SAVE  = 2;
  
  // GUI stuff
  
  JFrame              frame;
  JMenu               editMenu;
  JButton             fileSaveButton;
  public JTabbedPane  tabs1;
  public JTabbedPane  tabs2;
  private JSplitPane  split;
  
  javax.swing.Timer         autoSaveTimer;
  private int               autoSaveInterval = 0;
  private JDialog     saveDialog;
  private JProgressBar saveProgress;
  
  javax.swing.Timer   progressTimer;
  javax.swing.Timer   validateURLTimer;
  javax.swing.Timer   midnightTimer;
  javax.swing.Timer   rotateTimer;
  
  ListTab               listTab;
  CategoryTab           treeTab;
  ItemTab               itemTab;
  TimeTab               timeTab;
  RecursTab             recursTab;
  RotateTab             rotateTab;
  WebWindow             webWindow;
  PurgeWindow           purgeWindow;
  PrefsWindow           prefsWindow;
  LogWindow             logWindow;
  AboutWindow           aboutWindow;
  FileInfoWindow        filePropertiesWindow;
  
  FolderSync            folderSync;
  boolean               tabSetupComplete = false;
  
  // Tips                tips = null;
  
  JLabel              fileNameLabel;
  JLabel              indexLabel;
  JLabel              sizeLabel;
  JLabel              fullSizeLabel;
  
  ImageIcon           lateIcon;
  ImageIcon           todayIcon;
  ImageIcon           tomorrowIcon;
  ImageIcon           futureIcon;
  
  DateFormat          longDateFormatter;
  JLabel              todayLabel;
  DateFormat          dateFormatter = new SimpleDateFormat ("MM/dd/yyyy");
  // private boolean     unsavedChanges = false;
  
  // ItemComparator      comp = new ItemComparator();
  // ItemSelector        select = new ItemSelector();
  // IDListHeader        header        = new IDListHeader();
  ToDoItems           items         = new ToDoItems(this);
  ActorsList          actors        = new ActorsList();
  CategoryList        categories    = new CategoryList();
  SortedItems         sorted        = new SortedItems(this);
  ItemNavigator       navigator     = sorted;
  AuxList             auxList       = new AuxList(this);
  RotateList          rotateList    = new RotateList(this);
  
  private String      selectSave    = ItemSelector.SHOW_ALL_STR;
  
  ToDoItem            item;
  boolean             newItem = false;
  private boolean     changed = false;
  private String      originalTitle = "";
  
  URL                 pageURL;
  
  Color               rotateBackgroundColor   = new Color (255, 255, 255);
  Color               rotateTextColor         = new Color (0, 0, 0);
  String              rotateFont              = "Verdana";
  int                 rotateNormalFontSize    = 3;
  int                 rotateBigFontSize       = 4;
  int                 rotateSeconds           = 10;
  
  Logger              logger     = Logger.getShared();
  LogOutput           logOutput;
  
  // Fields used to validate Web Page URLs
  private  ThreadGroup webPageGroup;
  private  ArrayList  pages;
  private  ProgressMonitor progressDialog;
  private  int        progressMax = 0;
  private  int        progress = 0;
  private  int        badPages = 0;
  
  // System ClipBoard fields
  boolean             clipBoardOwned = false;
  Clipboard           clipBoard = null;
  Transferable        clipContents = null;
  
  private     ChainLink    idParent = null;
  private     ChainLink    idNext   = null;
  private     ChainLink    idPrior  = null;
  private     ChainLink    idFirstChild = null;
  private     ChainLink    idLastChild  = null;
  
  GregorianCalendar today = DateUtils.getToday();
  
  private DateFormat  backupDateFormatter 
      = new SimpleDateFormat ("yyyy-MM-dd-HH-mm");
  
  public ChainLink getLastChildLink () {
    return idLastChild;
  }
  
  public void setNextLink (ChainLink link) {
    // Should not be any next links for the top link
  }
  
  public void addChildLink (ChainLink child) {
    if (idLastChild == null) {
      idFirstChild = child;
    } else {
      idLastChild.setNextLink (child);
    }
    idLastChild = child;
  }
  
  /** 
    Creates a new instance of TwoDueCommon 
   */
  public TwoDueCommon(JFrame frame, JSplitPane split, JTabbedPane tabs1, JTabbedPane tabs2) {
    this.frame = frame;
    this.split = split;
    this.tabs1 = tabs1;
    this.tabs2 = tabs2;
    trouble = Trouble.getShared();
    trouble.setParent (tabs1);
    home = Home.getShared();
    appFolder = home.getAppFolder();
    userPrefs = UserPrefs.getShared(); 
    File iconFile = new File (appFolder, APP_ICON_PATH); 
    // userPrefs.setProgramName (PROGRAM_NAME);
    // comp.setSelector (select);
    logWindow = new LogWindow ();
    logOutput = new LogOutputText(logWindow.getTextArea());
    Logger.getShared().setLog (logOutput);
    Logger.getShared().setLogAllData (false);
    Logger.getShared().setLogThreshold (LogEvent.NORMAL);
    WindowMenuManager.getShared().add(logWindow);
    
    // Set up registration window
    // RegisterWindow.getShared().setStatusBar (statusBar);
    
    // Get System Properties
    userName = System.getProperty ("user.name");
    userDirString = System.getProperty (GlobalConstants.USER_DIR);
    Logger.getShared().recordEvent (LogEvent.NORMAL, 
      "User Directory = " + userDirString,
      false);
    Logger.getShared().recordEvent (LogEvent.NORMAL, 
      "App Folder = " + appFolder.toString(),
      false);
    Logger.getShared().recordEvent (LogEvent.NORMAL,
        "Java Virtual Machine = " + System.getProperty("java.vm.name") + 
        " version " + System.getProperty("java.vm.version") +
        " from " + StringUtils.removeQuotes(System.getProperty("java.vm.vendor")),
        false);
    if (xos.isRunningOnMacOS()) {
      Logger.getShared().recordEvent (LogEvent.NORMAL,
          "Mac Runtime for Java = " + System.getProperty("mrj.version"),
          false);      
    }
    Runtime runtime = Runtime.getRuntime();
    runtime.gc();
    NumberFormat numberFormat = NumberFormat.getInstance();
    Logger.getShared().recordEvent (LogEvent.NORMAL,
        "Available Memory = " + numberFormat.format (Runtime.getRuntime().freeMemory()),
        false);
    fileSeparatorString = System.getProperty (GlobalConstants.FILE_SEPARATOR, "\\");
    try {
      pageURL = appFolder.toURI().toURL(); 
    } catch (MalformedURLException e) {
      trouble.report ("Trouble forming pageURL from " + appFolder.toString(), 
          "URL Problem");
    }
    
    // load image icons
    try {
      lateIcon = new ImageIcon (new URL (pageURL, "images/late.gif"));
      todayIcon = new ImageIcon (new URL (pageURL, "images/today.gif"));
      tomorrowIcon = new ImageIcon (new URL (pageURL, "images/tomorrow.gif"));
      futureIcon = new ImageIcon (new URL (pageURL, "images/future.gif"));
    } catch (MalformedURLException e) {
      trouble.report ("Trouble forming image URLs", "URL Problem");
    }
    if (lateIcon.getImageLoadStatus() != MediaTracker.COMPLETE
        || todayIcon.getImageLoadStatus() != MediaTracker.COMPLETE
        || tomorrowIcon.getImageLoadStatus() != MediaTracker.COMPLETE
        || futureIcon.getImageLoadStatus() != MediaTracker.COMPLETE) {
      trouble.report ("Could not load icon images","Image Problem");
    }
    items.setDueDateIcons (lateIcon, todayIcon, tomorrowIcon, futureIcon);
    sorted.setDueDateIcons (lateIcon, todayIcon, tomorrowIcon, futureIcon);
    sorted.setCommon (this);
    
    // set refresh timer
    midnightTimer = new javax.swing.Timer (ONE_HOUR, this);
    midnightTimer.start();
    
    // Check to see if program has been registered
    // registration = RegistrationCode.getShared();
    // regUser = registration.getUser();
    // regCode = registration.getCode();
  }
  
  public void initComponents() {
    listTab = new ListTab (this);
    tabs1.addTab("List", listTab);
    
    treeTab = new CategoryTab (this);
    tabs1.addTab("Tree", treeTab);
    
    itemTab = new ItemTab (this);
    itemTab.setDueDateIcons (lateIcon, todayIcon, tomorrowIcon, futureIcon);
    tabs2.addTab("Item", itemTab);
    
    timeTab = new TimeTab (this);
    tabs2.addTab ("Time", timeTab);
    
    recursTab = new RecursTab (this);
    tabs2.addTab("Recurs", recursTab);
    
    rotateTab = new RotateTab (this);
    tabs2.addTab ("Rotate", rotateTab);
    
    // String select = userPrefs.getPref (SELECT_OPTION);
    // if (select != null && (! select.equals (""))) {
      // setSelectOption (views.getComparator().getSelectString());
      // String seq = ViewTab.ASCENDING;
      
    /*
      setSortFields (views.getComparator().getSelectString(),
          views.getComparator().getSortField(0), 
            views.getComparator().getSortSeq(0),
          views.getComparator().getSortField(0), 
            views.getComparator().getSortSeq(0),
          views.getComparator().getSortField(0), 
            views.getComparator().getSortSeq(0),
          views.getComparator().getSortField(0), 
            views.getComparator().getSortSeq(0),
          views.getComparator().getSortField(0), 
            views.getComparator().getSortSeq(0),
          views.getComparator().getUndatedString());
      viewTab.setOptions (views.getComparator().getSelectString(),
          views.getComparator().getSortField(0), 
            views.getComparator().getSortSeq(0),
          views.getComparator().getSortField(0), 
            views.getComparator().getSortSeq(0),
          views.getComparator().getSortField(0), 
            views.getComparator().getSortSeq(0),
          views.getComparator().getSortField(0), 
            views.getComparator().getSortSeq(0),
          views.getComparator().getSortField(0), 
            views.getComparator().getSortSeq(0),
          views.getComparator().getUndatedString()); */
      /* setSortFields (select, 
          userPrefs.getPref (SORT_FIELD_1), seq,
          userPrefs.getPref (SORT_FIELD_2), seq,
          userPrefs.getPref (SORT_FIELD_3), seq,
          userPrefs.getPref (SORT_FIELD_4), seq,
          userPrefs.getPref (SORT_FIELD_5), seq,
          ItemComparator.LOW);
      viewTab.setOptions (select,
          userPrefs.getPref (SORT_FIELD_1), seq,
          userPrefs.getPref (SORT_FIELD_2), seq,
          userPrefs.getPref (SORT_FIELD_3), seq,
          userPrefs.getPref (SORT_FIELD_4), seq,
          userPrefs.getPref (SORT_FIELD_5), seq,
          ItemComparator.LOW); */
    // }
    
    webWindow = new WebWindow (this);
    
    purgeWindow = new PurgeWindow (this);
    
    prefsWindow = new PrefsWindow (this);
    prefsWindow.getCommonPrefs().enableAutoSave(true);
    filePropertiesWindow = new FileInfoWindow (this);
    modifyView (-1, TwoDueCommon.VIEW_TRIGGER_PROGRAM_START);
    newUserPrefs();
    
    aboutWindow = new AboutWindow (
      false,   // loadFromDisk
      true,    // browserLauncher2Used
      true,    // jxlUsed
      true,    // pegdownUsed
      true,    // xerces used
      true,    // saxon used
      "1999"); // copyRightYearFrom));
    
    folderSync = new FolderSync(this);
    // WindowMenuManager.getShared().add(folderSync);
    
    prefsWindow.getCommonPrefs().setSplitPane(split);
    
    tabSetupComplete = true;
  }
  
  /*
  public void showTips () {
     
    if (tips == null) {
      tips = new Tips ();
    }
    tips.setVisible (true);
    tips.toFront();
  } */
  
  public void savePrefs () {
    // if (tips != null) {
    //   tips.savePrefs();
    // }
  }
  
  public void setSplit (boolean splitPaneHorizontal) {
    int splitOrientation = JSplitPane.VERTICAL_SPLIT;
    if (splitPaneHorizontal) {
      splitOrientation = JSplitPane.HORIZONTAL_SPLIT;
    }
    split.setOrientation (splitOrientation);
  }
  
  /**
   Initialize a new file.
   */
  protected void fileNew() {
    fileClose();
    diskStore = new TwoDueDiskUnknown();
    resetComparator();
    // diskStore.setComparator (comp);
    filePrep();
    newItems();
    initItems();
    modifyView (0, VIEW_TRIGGER_NEW_FILE);
    fileNameLabel.setText("");
    fileNameLabel.setToolTipText("");
    addDefaultItem();
    /* if (! registeredUser) {
      /*
      newItem();
      item.setTitle ("Read Two Due Quick Start");
      item.setDescription ("Read the Two Due Quick Start Guide by pressing"
          + " the Web Page Button below.");
      try {
        URL quickStartURL = new URL (pageURL, QUICK_START);
        item.setWebPage (quickStartURL);
      } catch (MalformedURLException e) {
      }
      item.setDueDateToday();
      try {
        int i = items.add (item);
      } catch (RegistrationException reg) {
        handleRegistrationException (reg);
      }
      
      addDefaultItem();
      modifyView (0, VIEW_TRIGGER_LEAVING_VIEW_TAB);
    } */
    setUnsavedChanges (true);
    displayItem();
    activateItemTab();
  }
  
  /**
   Allow various windows to perform initialization after the load of a new
   list of items.
  */
  private void initItems() {
    items.setDueDateIcons (lateIcon, todayIcon, tomorrowIcon, futureIcon);
    listTab.initItems();
    treeTab.initItems();
    itemTab.initItems();
    rotateTab.initItems();
    folderSync.initItems(items);
  }
  
  public void fileOpenDefault () {
    TwoDueDiskStore defaultStore = files.getDefaultStore();
    if (defaultStore == null || defaultStore.isInvalid()) {
      fileNew();
      TwoDueDiskStore store = files.getUsualLocation();
      store.setLog (Logger.getShared());
      if (store.isToDoFileValidOutput()) {
        diskStore = store;
        try {
          diskStore.save (items);
          setUnsavedChanges (false);
          displayFile();
          diskStore.setTemplate (webWindow.getTemplate());
          rememberLastFile (diskStore);
        } catch (IOException e) {
          trouble.report ("Two Due data could not be saved",
              "File Save As Error");
        }
      } else {
        trouble.report ("Trouble Saving to Default Location",
            "Default Location Error");
      }
      boolean prefsOK = userPrefs.savePrefs();
    } else {
      fileOpen (defaultStore);
    }
  }
  
  public void addDefaultItem () {    
    newItem();
    item.setTitle ("Add your to do items");
    item.setDescription ("Add your items that need to be done. ");
    // item.setWebPage (UnregisteredWindow.STORE);
    item.setDueDateToday();
    int i = items.add (item);
  }
  
  /**
    Open an existing file, allowing the user to specify the file.
   */
  protected void fileOpen() {
    modIfChanged();
    fileClose();
    TwoDueDiskStore store = files.chooseDiskStore
        (files.OPEN, frame, diskStore);
    store.setLog (Logger.getShared());
    if (store.isToDoFileValidInput()) {
      fileOpenFresh (store, false);
    }
  }
  
  /**
    Open an existing file, passing the file identifier in.
   */
  /*
  protected void fileOpen(File inFile, String inView, 
      File template, int publishWhen) {
    modIfChanged();
    fileClose();
    TwoDueDiskStore store = TwoDueDiskStore.makeDiskStore (inFile);
    store.setComparator (inView);
    store.setTemplate (template);
    store.setPublishWhen (publishWhen);
    store.setLog (Logger.getShared());
    fileOpen (store);
  }
   */
  /**
    Open an existing file, given a File identifier.
   */
  public void fileOpen (File openFile) {
    TwoDueDiskStore openStore = files.get (openFile);
    if (openStore == null) {
      openStore = files.makeDiskStore (openFile);
    }
    fileOpen (openStore);
  }
  
  /**
    Open an existing file, passing the disk store in.
   */
  protected void fileOpen(TwoDueDiskStore diskStore) {
    fileOpen (diskStore, false);
  }
  
  /**
    Open an existing archive, passing the disk store in.
   
    @param diskStore The disk store whose archive is to be opened.
   */
  protected void archiveOpen (TwoDueDiskStore diskStore) {
    fileOpen (diskStore, true);
  }
  
  /**
    Open an existing to do file, or its archive, 
    passing the disk store in along with an archive flag.
   
    @param diskStore Disk Store whose file or archive is to be opened.
    @param processingArchive True if we are to process the archive instead
                             of the current items.
   */
  protected void fileOpen 
      (TwoDueDiskStore diskStore, boolean processingArchive) {
    modIfChanged();
    fileClose();
    fileOpenFresh(diskStore, processingArchive);
  }
  
  /**
    Open an existing to do file, or its archive, 
    passing the disk store in along with an archive flag. Any necessary
    closure and cleanup from a previous file is presumed complete.
   
    @param diskStore Disk Store whose file or archive is to be opened.
    @param processingArchive True if we are to process the archive instead
                             of the current items.
   */
  protected void fileOpenFresh 
      (TwoDueDiskStore diskStore, boolean processingArchive) {
    
    boolean openOK = true;;
    this.diskStore = diskStore;

    if (diskStore.isPrimary()) {
      userPrefs.setAltParmsFolder (diskStore.getFile());
      newUserPrefs();
    }

    // setActionMsg ("Opening " + diskStore.getShortPath() 
    //     + (processingArchive ? TwoDueFolder.ARCHIVE_FILE_NAME : "")
    //     + " ... ");
    // if (diskStore.comparatorIsAvailable()) {
    //   comp = diskStore.getComparator();
    // } else {
    //   resetComparator();
    //   diskStore.setComparator (comp);
    // }
    diskStore.setProcessingArchive (processingArchive);
    filePrep();
    newItems();
    try {
      diskStore.populate (items, dateFormatter);
      rememberLastFile (diskStore);
    } catch (IOException e) {
      openOK = false;
      if (processingArchive) {
        trouble.report 
            ("Trouble Reading Archive File "
              + TwoDueFolder.ARCHIVE_FILE_NAME,
              "Archive Problem");
      } else {
        Object[] options = { "Remember it for later", "Forget about it" };
        int userOption = JOptionPane.showOptionDialog(tabs1, "File is not available", 
            "File Open Error",
            JOptionPane.DEFAULT_OPTION, 
            JOptionPane.WARNING_MESSAGE,
            null, options, options[0]);
        switch (userOption) {
          case 0:
            // Remember it for later: no action necessary
            break;
          case 1:
            // Forget about it
            diskStore.setForgettable (true);
            files.forget (diskStore);
            break;
          default:
            // Do nothing
            break;
        } // end switch
      } // end if not processing archive
    } // end catch IOException
    if (openOK) {
      displayFile();
      // clearActionMsg();
      initItems();
      if (folderSync.getAutoSync()) {
        folderSync.syncWithFolder();
      }
      navigator.firstItem();
      displayItem();
      activateListTab();
      modifyView (diskStore.getViewIndex(), VIEW_TRIGGER_SELECT_FILE);
      // switchTabs();
      // listTab.showThisTab();
    } else {
      fileNew();
    }
  }
  
  /**
    Pass the new disk store to any tabs that care, for initialization.
   */
  private void filePrep () {
    prefsWindow.filePrep (diskStore);
    webWindow.filePrep (diskStore);
    purgeWindow.filePrep (diskStore);
    timeTab.filePrep (diskStore);
    filePropertiesWindow.filePrep (diskStore);
    folderSync.filePrep(diskStore);
  }
  
  /**
    Create a new items collection and associated objects and 
    link the whole mess together.
   */
  private void newItems () {
    // header = new IDListHeader();
    items = new ToDoItems(this);
    items.setDueDateIcons (lateIcon, todayIcon, tomorrowIcon, futureIcon);
    
    actors = new ActorsList();
    actors.registerValue("");
    items.addView (actors);
    
    categories = new CategoryList();
    categories.registerValue("");
    items.addView (categories);
    
    sorted = new SortedItems 
        (items, diskStore.getComparator(), diskStore.getSelector());
    sorted.setDueDateIcons (lateIcon, todayIcon, tomorrowIcon, futureIcon);
    sorted.setCommon (this);
    navigator = sorted;
    items.addView (sorted);
    sorted.firstItem();
    
    // tree = new TagsModel(diskStore.getFile());
    // tree.setCommon (this);
    // items.addView (tree);
    
    // diskStore = new TwoDueDiskUnknown();
    setUnsavedChanges (false);
  }
  
  /**
    Import additional records into the current data store from a disk
    file specified by the user. 
   */
  public void fileImport() {
    
    modIfChanged();
    
    // FilePrefs.getShared().handleMajorEvent();
    
    XFileChooser chooser = new XFileChooser ();
    chooser.setFileSelectionMode(XFileChooser.FILES_ONLY); 
    if (importFile != null) {
      chooser.setCurrentDirectory (importFile); 
    } 
    else
    if (diskStore != null
        && (! diskStore.isUnknown())) {
      chooser.setCurrentDirectory (diskStore.getToDoFile());
    }
    importFile = chooser.showOpenDialog (frame);
    
    if (importFile != null) {
      // importFile = chooser.getSelectedFile();
      String importName = importFile.toString();
      DataSource importer;
      if (importName.endsWith (".htm")
          || importName.endsWith (".html")) {
        importer = new HTMLFile (importFile);
        HTMLFile htmlImporter = (HTMLFile)importer;
        htmlImporter.useHeadingsAndLists();
      } else {
        importer = new TabDelimFile (importFile);
      }
      importer.setLog (Logger.getShared());
      // setActionMsg ("Importing " + importName + " ... ");
      int before = items.size();
      try {
        items.addAll (dateFormatter, importer); 
      } catch (IOException e) {
        trouble.report 
            ("Trouble Reading File "
            + importFile.toString(),
            "File I/O Problem");
      }
      // clearActionMsg();
      int added = (items.size() - before);
      if (added > 0) {
        setUnsavedChanges (true);
        selectItem (items.get (before));
      }
      JOptionPane.showMessageDialog(tabs1,
          String.valueOf (added) 
              + " Items Imported",
          "Import Results",
          JOptionPane.INFORMATION_MESSAGE);
      
    } // end if user specified a file to import

  } // end method fileImport
  
  public void resetComparator() {
    diskStore.resetComparator();
  }
  
  /**
    Save the current file if there are currently any unsaved changes in memory.
   */
  protected void fileClose() {
    items.shutDown();
    if (! diskStore.isUnknown()) {
      diskStore.setTemplate (webWindow.getTemplate());
      rememberLastFile (diskStore);
    }
    if (getUnsavedChanges()) {
      Object[] options = { "Save", "Save As", "Discard Changes" };
      int userOption = JOptionPane.showOptionDialog(tabs1, "Save your changes?", 
          "Unsaved Changes",
          JOptionPane.DEFAULT_OPTION, 
          JOptionPane.WARNING_MESSAGE,
          null, options, options[0]);
      switch (userOption) {
        case 1:
          fileSaveAs();
          break;
        case 2:
          // do nothing
          break;
        default:
          fileSave();
          break;
      } // end switch
    } // end if unsaved changes
    if (diskStore.getPublishWhen() == TwoDueDiskStore.PUBLISH_ON_CLOSE) {
      publish (webWindow.getTemplate());
    }
  } // end method
  
  /**
    Save the current file from memory to disk.
   */
  protected void fileSave() {
    modIfChanged();
    if (diskStore.isFileNowOpen()) {
      try {
        diskStore.save (items);
        setUnsavedChanges (false);
        diskStore.setTemplate (webWindow.getTemplate());
        rememberLastFile (diskStore);
        boolean prefsOK = userPrefs.savePrefs();
      } catch (IOException e) {
          trouble.report ("Two Due data could not be saved",
              "File Save Error");
      } // end catch
      // clearActionMsg();
      if ((! diskStore.getProcessingArchive()) 
          && diskStore.getPublishWhen() == TwoDueDiskStore.PUBLISH_ON_SAVE) {
        publish (webWindow.getTemplate());
      }
    } else {
      fileSaveAs();
    }
  } // end method
  
  /**
    Save the file to disk, allowing the user to specify a new
    name for the disk file.
   */
  protected void fileSaveAs() {
    TwoDueDiskStore store = files.chooseDiskStore
        (files.SAVE, frame, diskStore);
    store.setLog (Logger.getShared());
    if (store.isToDoFileValidOutput()) {
      diskStore = store;
      // setActionMsg ("Saving " + diskStore.getShortPath() + "... ");
      try {
        diskStore.save (items);
        setUnsavedChanges (false);
        displayFile();
        diskStore.setTemplate (webWindow.getTemplate());
        rememberLastFile (diskStore);
        // System.out.println("TwoDueCommon.fileSaveAs filePrep");
        filePrep();
      } catch (IOException e) {
        trouble.report ("Two Due data could not be saved",
            "File Save As Error");
      }
    } else {
      trouble.report ("Valid Output Destination Not Specified",
          "File Save As Error");
    }
    boolean prefsOK = userPrefs.savePrefs();
    // clearActionMsg();
    if (diskStore.getPublishWhen() == TwoDueDiskStore.PUBLISH_ON_SAVE) {
      publish (webWindow.getTemplate());
    }
  }
  
  /**
   Backup without prompting the user. 
  
   @return True if backup was successful. 
  */
  public boolean backupWithoutPrompt() {
    
    boolean ok = true;
    
    File backupFolder = getBackupFolder();
    String backupFileName = getBackupFileName();
    File backupFile = new File 
        (backupFolder, backupFileName);
    ok = backup (backupFile);

    return ok;
  }
  
  /**
    Perform a backup of the Two Due list that is currently open. 
  
    @return True if backup was performed successfully.
  */
  public boolean promptForBackup () {
    
    boolean ok = false;
    
    XFileChooser chooser = new XFileChooser (); 
    chooser.setFileSelectionMode(XFileChooser.FILES_ONLY); 
    
    // Get the default folder to use for backups
    File backupFolder = getBackupFolder();
    chooser.setCurrentDirectory (backupFolder);
    
    // Now build default file name
    String backupFileName = getBackupFileName();
    File defaultBackupFile = new File 
        (backupFolder, backupFileName);
    
    chooser.setFile (defaultBackupFile);
    chooser.setDialogTitle("Create a Backup of your Two Due List");
   
    File result = chooser.showSaveDialog (frame); 
    
    File backupFile = null;
    if (result != null) { 
      backupFile = chooser.getSelectedFile();
      ok = backup (backupFile);
      if (ok) {
        JOptionPane.showMessageDialog(frame,
            "Backup completed successfully",
            "Backup Results",
            JOptionPane.INFORMATION_MESSAGE,
            Home.getShared().getIcon());
        setBackupFolder(backupFile.getParentFile());
      } 
    } // end if result file was selected
    return ok;
  }
  
  /**
   Get the default folder to be used for backups. 
  
   @return Last folder used, or default folder for collection. 
  */
  public File getBackupFolder() {
    return diskStore.getBackupFolder();
  }
  
  /**
   Set the default folder to be used for backups. 
  
   */
  public void setBackupFolder(File backupFolder) {
    diskStore.setBackupFolder(backupFolder);
    setUnsavedChanges(true);
  }
  
  public void setBackupFolder(String backupPath) {
    diskStore.setBackupFolder(backupPath);
    setUnsavedChanges(true);
  }
  
  /**
   Get the default file name to be used for backups. 
  
   @return A default file name to be used for backups. 
  */
  public String getBackupFileName() {
    StringBuilder backupFileName = new StringBuilder ();
    FileName name = new FileName (diskStore.getFile());
    int numberOfFolders = name.getNumberOfFolders();
    int i = numberOfFolders - 1;
    if (i < 0) {
      i = 0;
    }
    while (i <= numberOfFolders) {
      if (backupFileName.length() > 0) {
        backupFileName.append (' ');
      }
      backupFileName.append (name.getFolder (i));
      i++;
    }
    backupFileName.append (" backup ");
    backupFileName.append (backupDateFormatter.format (new Date()));
    backupFileName.append (".xml");
    return backupFileName.toString();
  }
  
  /**
   Backup the collection to the passed file. 
  
   @param backupFile The backup file to be created.
  
   @return True if backup was created successfully.
  */
  public boolean backup (File backupFile) {
    
    boolean ok = true;
    FileName backupName = new FileName (backupFile, FileName.FILE_TYPE);
    FileName backupTwoDueName 
        = new FileName (backupName.getPath() + backupName.replaceExt 
          (TwoDueDiskStore.FILE_EXT_XML), FileName.FILE_TYPE);
    
    XMLRecordWriter xmlWriter = diskStore.constructToDoRecsXMLOut
        (backupTwoDueName.getFile());

    try {
      diskStore.saveToXML(items, xmlWriter);
    } 
    catch (java.io.IOException e) {
      ok = false;
    }
    if (ok) {
      Logger.getShared().recordEvent (LogEvent.NORMAL,
        "List backed up to " + backupTwoDueName.toString(),
          false);
      setBackupFolder(backupTwoDueName.getPath());
      // FilePrefs.getShared(this).saveLastBackupDate();
    } else {
        trouble.report ("iWisdom data could not be backed up",
            "File Backup Error");
    }
    return ok;
  }
  
  /**
   Revert to a backup. Allow the user to select the backup file, delete all 
   the current items, and import the items contained in the backup file. 
   */
  public boolean fileRevert () {
    
    boolean ok = true;
    modIfChanged();
    
    // Ask the user to choose the backup file
    XFileChooser chooser = new XFileChooser ();
    chooser.setFileSelectionMode(XFileChooser.FILES_ONLY); 
    
    // Get the default folder to use for backups
    File backupFolder = diskStore.getBackupFolder();
    chooser.setCurrentDirectory (backupFolder);
    chooser.setDialogTitle("Select Previous Backup File to Revert to");
    File backupFile = chooser.showOpenDialog (frame);
    if (backupFile != null) {
      newItems();
      Logger.getShared().recordEvent (LogEvent.NORMAL, 
        "Reverting from "  + backupFile.toString(),
        false);
      try {
        diskStore.populate(items, dateFormatter, backupFile);
        int added = items.size();
        JOptionPane.showMessageDialog(frame,
          String.valueOf (added) 
              + " Items Restored from Backup",
          "Revert Results",
          JOptionPane.INFORMATION_MESSAGE);
        Logger.getShared().recordEvent (LogEvent.NORMAL,
            String.valueOf (added)
              + " Items restored from backup",
            false);
      } 
      catch (IOException e) {
        trouble.report ("Two Due backup file could not be read",
              "File Revert From Backup Error");
        ok = false;
      }
    }
    return ok;
  }
  
  public void filePrint() {
    ListPrinter printer = new ListPrinter();
    printer.print (sorted);
  }
  
  /**
   Saves information about last To Do file accessed.
   
   @param lastFile Last file opened or saved with new name.
   
  public void rememberLastFile (File lastFile, ItemComparator compare, File template) {
    
    if (lastFile != null) {
      recentFiles.remember 
          (lastFile, compare.toString(), template, diskStore.getPublishWhen());
    }
    
  } // end method */
  
  public void setPrimary (boolean primary) {
    diskStore.setPrimary (primary);
    rememberLastFile();
    displayFile();
  }
  
  /**
   Saves information about last To Do file accessed.
   
   @param lastFile Last file opened or saved with new name.
   */
  public void rememberLastFile (TwoDueDiskStore lastStore) {
    
    if (lastStore != null
        && (! lastStore.isUnknown())) {
      files.remember (lastStore);
    }
    
  } // end method
  
  /**
    Saves information about last To Do file accessed.
   */
  public void rememberLastFile () {
    
    if (diskStore != null
        && (! diskStore.isUnknown())) {
      files.remember (diskStore);
    }
    
  } // end method
  
  /**
    Schedule start times for this list of to do items.
   */
  public void scheduleStartTimes() {
    // setActionMsg ("Scheduling Start Times...");
    int mods = sorted.scheduleStartTimes (diskStore.getDefaultStartTime());
    if (mods > 0) {
      displayItem();
      setUnsavedChanges (true);
      sorted.fireTableDataChanged();
    }
    // clearActionMsg();
  }
  
  public void slack () {
    // Make sure user is ready to proceed
    Object[] options = { "Cancel", "Due Next Week", "Due Tomorrow", "Due Today" };
    int userOption = JOptionPane.showOptionDialog(tabs1, 
        "Make Past Due Items:", 
        "Cut You Some Slack",
        JOptionPane.DEFAULT_OPTION, 
        JOptionPane.QUESTION_MESSAGE,
        null, options, options[0]);
    
    int mods = 0;
    
    // If User is ready, then proceed
    if (userOption > 0) {
      
      // Prepare Auxiliary List to track invalid URLs
      GregorianCalendar slackDate = new GregorianCalendar ();
      int slack = 0;
      switch (userOption) {
        case 1:
          slack = 7;
          break;
        case 2:
          slack = 1;
          break;
      }
      if (slack > 0) {
        slackDate.add (GregorianCalendar.DATE, slack);
      }
      Date newDate = new Date();
      newDate = slackDate.getTime();

      // Go through sorted items looking for past due items
      ToDoItem next;
      for (int i = 0; i < sorted.size(); i++) {
        next = sorted.get(i);
        if (next.isLate()) {
          next.setDueDate (newDate);
          mods++;
        } 
      } // end of sorted items
    } // end if user requested slack
    
    if (mods > 0) {
      displayItem();
      setUnsavedChanges (true);
      sorted.sort();
      sorted.fireTableDataChanged();
      listTab.setColumnWidths();
      items.sortTagsModel();
    }
  
  } // end slack method
  
  public void resetDueDates() {

    // Make sure user is ready to proceed
    int userOption = JOptionPane.showConfirmDialog(tabs1, 
        "Reset All Due Dates?", 
        "Due Date Reset",
        JOptionPane.YES_NO_OPTION, 
        JOptionPane.QUESTION_MESSAGE);
    
    // If User is ready, then proceed
    if (userOption == JOptionPane.YES_OPTION) {

      // Go through sorted items looking for each item
      ToDoItem next;
      for (int i = 0; i < sorted.size(); i++) {
        next = sorted.get(i);
        next.setDueDateDefault();
      } // end of sorted items
    } // end if user requested slack
    
    displayItem();
    setUnsavedChanges (true);
    sorted.sort();
    sorted.fireTableDataChanged();
    listTab.setColumnWidths();
    items.sortTagsModel();
  }
  
  /**
    Validate URLs for the currently visible list of to do items.
   */
  public void validateURLs () {
    
    // Make sure user is ready to proceed
    Object[] options = { "Continue", "Cancel" };
    int userOption = JOptionPane.showOptionDialog(tabs1, 
        "Please ensure your Internet connection is active", 
        "Validate Web Pages",
        JOptionPane.DEFAULT_OPTION, 
        JOptionPane.WARNING_MESSAGE,
        null, options, options[0]);
    
    // If User is ready, then proceed
    if (userOption == 0) {
      
      // Prepare Auxiliary List to track invalid URLs
      webPageGroup = new ThreadGroup("WebPage threads");
      pages = new ArrayList();

      // Go through sorted items looking for Web Pages
      ToDoItem next;
      String address;
      WebPage page;
      for (int i = 0; i < sorted.size(); i++) {
        next = sorted.get(i);
        address = next.getWebPage();
        if (address.length() > 0) {
          page = new WebPage (webPageGroup, next, Logger.getShared(), this);
          pages.add (page);
        } 
      } // end of sorted items
      
      // Prepare dialog to show validation progress
      progress = 0;
      progressMax = pages.size();
      progressDialog = new ProgressMonitor (tabs1,
          "Validating "
              + String.valueOf (progressMax)
              + " Web Page URLs...",
          "                                                  ", // Status Note
          0,              // lower bound of range
          progressMax     // upper bound of range
          );
      progressDialog.setProgress(0);
      progressDialog.setMillisToDecideToPopup(500);
      progressDialog.setMillisToPopup(500);
      
      // Now start threads to check Web pages
      badPages = 0;
      for (int i = 0; i < pages.size(); i++) {
        page = (WebPage)pages.get(i);
        page.start();
      } // end for each page being validated 
      
      // Start timer to give the user a chance to cancel
      if (validateURLTimer == null) {
        validateURLTimer = new javax.swing.Timer (ONE_SECOND, this);
      } else {
        validateURLTimer.setDelay (ONE_SECOND);
      }
      validateURLTimer.start();
    } // continue rather than cancel
  } // end validateURLs method
  
  /**
    Record the results each time a WebPage checks in to report that
    its URL validation process has been complete. 
   
    @param item   The ToDoItem whose Web Page URL was being validated.
    @param valid  True if the URL was found to be valid. 
   */
  public synchronized void validateURLPageDone (ToDoItem item, boolean valid) {
    progress++;
    progressDialog.setProgress (progress);
    progressDialog.setNote ("Validation complete for "
        + String.valueOf (progress));
    if (! valid) {
      if (badPages == 0) {
        auxList.setItems (items);
        auxList.setSorted (sorted);
        auxList.initList();
      }
      badPages++;
      auxList.add (item.getItemNumber());
    }
    if (progress >= progressMax) {
      validateURLAllDone();
    } // end if all pages checked
  } // end method validateURLPageDone
  
  /**
    Handle GUI events, including the firing of various timers.
   
    @param event The GUI event that fired the action.
   */
  public void actionPerformed (ActionEvent event) {
    Object source = event.getSource();
    
    // AutoSave Timer
    if (source == autoSaveTimer) {
      if (getUnsavedChanges()) {
        if (prefsWindow.getAutoSave() == autoSaveInterval) {
          fileSave();
        } else
        if (prefsWindow.getAutoSave() > 0 
            && prefsWindow.getAutoSave() < autoSaveInterval) {
          fileSave();
          checkAutoSave();
        } else {
          checkAutoSave();
        }
      } // end if unsaved changes
    } // end if autosave timer
    else
      
    // URL Validation Timer
    if (source == validateURLTimer) {
      if (progressDialog.isCanceled()) {
        WebPage page;
        for (int i = 0; i < pages.size(); i++) {
          page = (WebPage)pages.get(i);
          if (! page.isValidationComplete()) {
            logEvent (LogEvent.MEDIUM, 
                "URL Validation incomplete for "
                + page.toString(),
                false);
          }
        } // end for each page being validated 
        webPageGroup.interrupt(); 
        validateURLAllDone();
      }
    }
    else
      
    // Progress Timer -- Unused??
    if (source == progressTimer) {
      saveProgress.setValue (items.getItemIndex());
    }
    else
      
    // Midnight Timer
    if (source == midnightTimer) {
      DateUtils.refresh();
      if (DateUtils.differentDate (today, DateUtils.getToday())) {
        today = DateUtils.getToday();
      }
      todayLabel.setText (longDateFormatter.format (new Date()));
      sorted.fireTableDataChanged();
    }
    else
      
    // Rotation Timer
    if (source == rotateTimer) {
      modIfChanged();
      navigator.nextItem();
      displayItem();
    }
     // else
      
    // Action Message Timer
    // if (source == actionMsgTimer) {
    //   actionMsg.setText ("          ");
    // }
  } // end method
  
  /**
    Shut down the URL Validation process and report the results.
   */
  private void validateURLAllDone () {
    if (validateURLTimer != null
        && validateURLTimer.isRunning()) {
      validateURLTimer.stop();
    }
    progressDialog.close();
    JOptionPane.showMessageDialog(tabs1,
      String.valueOf (badPages) 
          + " Invalid Web Page(s) Found out of " 
          + String.valueOf (pages.size()),
      "Web Page Validation Results",
      JOptionPane.INFORMATION_MESSAGE);
    if (badPages > 0) {
      navigator = auxList;
      auxList.firstItem();
      displayItem();
      activateItemTab();
    } // end if any bad pages found
  } // end method
  
  public void startRotation () {
    
    if (rotateTimer == null) {
      rotateTimer = new javax.swing.Timer (rotateSeconds * ONE_SECOND, this);
    } else {
      rotateTimer.setDelay (rotateSeconds * ONE_SECOND);
    }
    rotateTimer.start();
    
    rotateList.setItems (items);
    rotateList.setSorted (sorted);
    rotateList.start();
    if (rotateList.isRotating()) {
      navigator = rotateList;
      rotateList.firstItem();
      displayItem();
    }
  }
  
  public void endRotation () {
    if (rotateList.isRotating()) {
      rotateTimer.stop();
      rotateList.stop();
      navigator = sorted;
    }
  }
  
  public int replaceCategory (String find, String replace) {
    int mods = 0;
    ToDoItem next;
    for (int i = 0; i < items.size(); i++) {
      next = items.get(i);
      if (next.getTags().toString().equalsIgnoreCase (find)) {
        next.setTags (replace);
        mods++;
        setUnsavedChanges (true);
        items.modify (next);
      } 
    } // end of  items
    displayItem();
    return mods;
  }
  
  /**
    Clear the action message.
   */
  /*
  public void clearActionMsg () {
    if (actionMsgTimer == null) {
      actionMsgTimer = new javax.swing.Timer (5000, this);
    } else {
      if (actionMsgTimer.isRunning()) {
        actionMsgTimer.stop();
      }
      actionMsgTimer.setDelay (5000);
    }
    actionMsgTimer.setRepeats (false);
    actionMsgTimer.start();
  } */
  
  /**
    Set the action message.
   */
  /*
  public void setActionMsg (String message) {
    if (actionMsgTimer != null
        && actionMsgTimer.isRunning()) {
      actionMsgTimer.stop();
    }
    actionMsg.setText (message); 
  }
   */
  
  /**
    Make the file name visible to the user.
   */
  protected void displayFile() {
    fileNameLabel.setText(diskStore.getShortPath());
    fileNameLabel.setToolTipText(diskStore.getPath());
    items.getTagsModel().setSource(diskStore.getFile());
  }
  
  /**
    Changes the active tab to the tab displaying a list of items.
   */
  public void activateListTab () {
    tabs1.setSelectedIndex (LIST_TAB_INDEX);
    listTab.grabFocus();
  }
  
  /**
    Changes the active tab to the tab displaying a tree of items 
    organized by category.
   */
  public void activateTreeTab () {
    tabs1.setSelectedIndex (TREE_TAB_INDEX);
  }
  
  /**
    Changes the active tab to the tab displaying an individual item.
   */
  public void activateItemTab () {
    tabs2.setSelectedIndex (ITEM_TAB_INDEX);
  }
  
  /**
    Changes the active tab to the tab displaying time information for
    an individual item.
   */
  public void activateTimeTab () {
    tabs2.setSelectedIndex (TIME_TAB_INDEX);
  }
  
  /**
    Changes the active tab to the tab displaying recurs information for
    an individual item.
   */
  public void activateRecursTab () {
    tabs2.setSelectedIndex (RECURS_TAB_INDEX);
  }
  
  /**
    Changes the active tab to the tab displaying a rotating selection of
    items.
   */
  public void activateRotateTab () {
    tabs2.setSelectedIndex (ROTATE_TAB_INDEX);
  }
  
  /**
    Changes the active tab to the About tab.
   */
  public void displayAbout () {
    aboutWindow.setVisible (true);
  }
  
  /**
    Changes the active tab to the Prefs tab.
   */
  public void displayPrefs () {
    prefsWindow.setVisible (true);
  }
  
  public void switchTabs () {
    if (tabSetupComplete) {
      modIfChanged();
      displayItem();
    }
    endRotation();
  }
  
  /**
    Purge closed items from list.
   
    @param archive Indicates whether items purged from the main list 
                   should be written to an archive file. 
   */
  public void purge (boolean archive) {
    
    // If we're archiving, then make sure all items are visible 
    // on the sorted list. 
    
    // diskStore.display ("purge pre");
    boolean archiving = false;
    int oldDeletes = 0;
    String selectSave = diskStore.getSelectString();
    if (archive && diskStore.archiveIsAvailable()) {
      archiving = true;
      modifyView (-1, VIEW_TRIGGER_ARCHIVING);
    }
    
    // If we're archiving, then add items from the archive back to the
    // current list and re-archive them, in order to keep the archive
    // in sorted order, for Web publication and viewing. 
    
    RecordDefinition recDef = ToDoItem.getRecDef();
    File archiveFile = diskStore.getArchiveFile();
    TabDelimFile archiveTDF = diskStore.getArchiveTDF();
    File archiveNewFile = diskStore.getArchiveNewFile();
    TabDelimFile archiveNewTDF = new TabDelimFile (archiveNewFile);
    File archiveBackupFile = diskStore.getArchiveBackupFile();
    if (archiving 
        && archiveFile.exists()
        && archiveFile.canRead()) {
      try {       
        DataRecord next;
        int addedAt = 0;
        archiveTDF.openForInput();
        while (! archiveTDF.isAtEnd()) {
          next = archiveTDF.nextRecordIn();
          if (next != null) {
            ToDoItem newItem = new ToDoItem (dateFormatter, next);
            newItem.setDeleted (false);
            addedAt = items.add (newItem);
            oldDeletes++;
          } // end if next data rec not null
        } // end while more items in old archive
      } catch (IOException e) {
        trouble.report 
            ("Trouble Reading Prior Archive File "
              + TwoDueFolder.ARCHIVE_FILE_NAME,
              "Archive Problem");
      } // end catch IOException
    }
    
    // If we're archiving, then write purged records to an archive file.
    
    if (archiving) {
      try {
        archiveNewTDF.openForOutput (recDef);
      } catch (IOException e) {
        trouble.report 
            ("Trouble Opening Archive File "
              + archiveNewFile.toString()
              + " for Output",
              "Archive Problem");
        archiving = false;
      }
    }
    
    // Go through sorted list looking for closed or canceled items.
    
    ToDoItem nextItem;
    int i = 0;
    int deletes = 0;
    while (i < sorted.size()) {
      nextItem = sorted.get (i);
      if (nextItem.isDone()) {
        if (archiving) {
          try {
            archiveNewTDF.nextRecordOut (nextItem.getDataRec(recDef));
          } catch (IOException e) {
            trouble.report ("Trouble writing to Archive File "
                + archiveNewFile.toString(), "Archive Problem");
          }
        }
        items.remove(nextItem);
        deletes++;
      } else {
        // Item not removed
        i++;
      }
    } // end while loop
    
    // Finish up
    setUnsavedChanges (true);
    fileSave();
    fileOpen (diskStore);
    if (archiving) {
      modifyView (-1, VIEW_TRIGGER_DONE_ARCHIVING);
      try {
        archiveNewTDF.close();
        diskStore.backupRenameArchive();
      } catch (IOException e) {
        trouble.report ("Trouble closing and renaming Archive File", 
            "Archive Problem");
      }
      publishArchive();
    }
    // diskStore.display ("purge post");
    navigator.firstItem();
    displayItem();
    // setActionMsg ((archiving ? "Archived " : "Purged ") 
    //     + String.valueOf(deletes - oldDeletes) + " closed items");
  } // end purge method
  
  /**
    Publish archived items using archive web template.
   
   */
  public void publishArchive () {
    if (diskStore.archiveTemplateIsAvailable()) {
      // setActionMsg ("Publishing Archive with Web template... ");
      try {
        File archiveWebPage = diskStore.publishArchive();
      } catch (java.io.IOException e) {
        trouble.report ("Trouble generating Archive Web Template", "Template Problem");
      }
      // clearActionMsg();
    }
  }
  
  public void findItem (String findString) {
    auxList.setItems (items);
    auxList.setSorted (sorted);
    auxList.find (findString);
    if (auxList.isAuxActive()) {
      navigator = auxList;
      auxList.firstItem();
      displayItem();
      activateItemTab();
    }
  }
  
  public void findAgain () {
    if (auxList.isAuxActive()) {
      auxList.nextItem();
      displayItem();
      activateItemTab();
    }
  }
  
  /**
    Gets the ToDoItem currently being displayed/modified. 
   
    @return The ToDoItem currently selected.
   */
  public ToDoItem getItem() {
    return item;
  }
  
  /**
    Sets the ToDoItem currently being displayed/modified. 
   
    @param item The ToDoItem to be stored as the current one.
   */
  public void setItem (ToDoItem item) {
    this.item = item;
    newItem = false;
    displayItemNumber();
  }
  
  /**
    Allocates a new ToDoItem.
   */
  public boolean newItem() {
    
    boolean ok = true;

    item = new ToDoItem ();
    item.setStartTime (diskStore.getDefaultStartTime());
    // item.setID ("");
    newItem = true;
    displayItemNumber();
    return ok;

  }
  
  /**
    Displays the item number currently being worked on.
   */
  private void displayItemNumber() {
    if (indexLabel != null) {
      indexLabel.setText (String.valueOf (navigator.getItemNumber() + 1));
    }
  }
  
  /**
    Selectes the passed item and displays it.
   
    @param item Item being selected.
   */
  public void selectItem (ToDoItem item) {
    this.item = item;
    navigator.selectItem (item);
    displayItem();
    // activateItemTab();
  }
  
  /**
   Displays the item at the curent index.
   */
  public void displayItem() {
    originalTitle = item.getTitle();
    itemTab.displayItem();
    timeTab.displayItem();
    recursTab.displayItem();
    rotateTab.displayItem();
    displayRecordCounts();
    changed = false;
  }
  
  public void displayRecordCounts() {    
    if (sizeLabel != null) {
      sizeLabel.setText (String.valueOf (navigator.size()));
    }
    if (fullSizeLabel != null) {
      if (items.size() > navigator.size()) {
        fullSizeLabel.setText ("(" + String.valueOf (items.size()) + ")");
      } else {
        fullSizeLabel.setText("");
      } // end if no invisibile items
    } // end if label already initialized
  } // end method
  
  public void setChanged () {
    changed = true;
  }
  
  /**
    Modifies the item if anything on the screen changed.  
   */
  public void modIfChanged () {
    checkAutoSave();
    if (item != null) {

      if (itemTab.modIfChanged()) {
        changed = true;
      } 
      
      if (timeTab.modIfChanged()) {
        changed = true;
      }
      
      if (recursTab.modIfChanged()) {
        changed = true;
      }

      if (changed) {    
        if (newItem) {
          int i = items.add (item);
          newItem = false;
        } else {
          items.modify (item);
          if (! item.getTitle().equalsIgnoreCase(originalTitle)) {
            checkForFileRenames(item, originalTitle);
          }
        }
        // Shouldn't need to do this here, because SortedItems is doing
        // it internally when it is called by ToDoItems for a modify or add
        // sorted.fireTableDataChanged();
        // listTab.doLayout();
        setUnsavedChanges (true);
        // Following line added on 30-Oct-2004 -- Hope it works!
        changed = false;
      } // end if changed
    } // end if item not null
  } // end method
  
  /**
   If the title of an item has changed, then check to see any similarly 
   named files should be renamed as well. 
  
   @param modItem    The to do item that has had its title modified. 
   @param priorTitle The title of the item prior to modification.
   @return           The number of files renamed successfully.
  */
  private int checkForFileRenames(ToDoItem modItem, String priorTitle) {
    
    int renames = 0;
    
    // See if the to do item has a file associated with it
    if (modItem.getWebPage().startsWith("file:/")) {
      FileName webFileName = new FileName(modItem.getWebPage());
      
      // If so, then see if the file name matches the prior title
      // System.out.println("TwoDueCommon.checkForFileRenames");
      // System.out.println("  Prior Title  = " + priorTitle);
      // System.out.println("  English Name = " 
      //     + webFileName.getFileOrFolderNameEnglish());
      if (priorTitle.equals(webFileName.getFileOrFolderNameEnglish())) {
        File fileToRename = webFileName.getFile();
        // System.out.println ("File to rename = " + fileToRename.toString());
        if (fileToRename.exists()) {
          int fileRenameOption = JOptionPane.showConfirmDialog(tabs1, 
              "Rename similarly named file(s)?",
              "File Rename Confirmation",
              JOptionPane.YES_NO_OPTION,
              JOptionPane.QUESTION_MESSAGE);
          if (fileRenameOption == JOptionPane.YES_OPTION) {
            File newFile = renameFileWithTitle
                (fileToRename, webFileName, modItem.getTitle());
            if (newFile != null) {
              renames++;
              try {
                String webPage = newFile.toURI().toURL().toString();
                modItem.setWebPage(webPage);
              } catch (MalformedURLException e) {
                // leave as-is
              }
              
              renames = renames + 
                  checkForMoreMatchingNames(
                    fileToRename.getParentFile(),
                    priorTitle,
                    modItem.getTitle());
            }
          }
        } else {
          Trouble.getShared().report(tabs1, 
              "File to rename could not be found.", "Rename Error");
        }      
      }
    }
    return renames;
  }
  
  private int checkForMoreMatchingNames(
      File parentFolder,
      String priorTitle,
      String newTitle) {
    int matches = 0;
    
    DirectoryReader directoryReader = new DirectoryReader (parentFolder);
    directoryReader.setLog (Logger.getShared());
    try {
      directoryReader.openForInput();
      while (! directoryReader.isAtEnd()) {
        File nextFile = directoryReader.nextFileIn();
        if ((nextFile != null) 
            && (! nextFile.getName().startsWith ("."))
            && (nextFile.exists())
            && (nextFile.canRead())
            && (! nextFile.getName().startsWith(TwoDueFolder.TO_DO_FILE_NAME))
            && (! nextFile.getName().equals(TwoDueFolder.DISK_DIR_NAME))
            && (! nextFile.getName().equals(TwoDueDiskDirectory.TWO_DUE_FOLDER))) {
          FileName nextFileName = new FileName(nextFile);
          String nextFileEnglishName = nextFileName.getFileOrFolderNameEnglish();
          if (priorTitle.equals(nextFileName.getFileOrFolderNameEnglish())) {
            File newFile = renameFileWithTitle
                (nextFile, nextFileName, newTitle);
            if (newFile != null) {
              matches++;
              if (newFile.isDirectory()) {
                checkForMoreMatchingNames(newFile, priorTitle, newTitle);
              }
            }
          } // end if matching file
        } // end if file exists, can be read, etc.
      } // end while more files in specified folder
    } catch (IOException ioe) {
      Trouble.getShared().report(tabs1, 
          "Trouble reading folder: " + parentFolder.toString(), 
          "Folder access problems");
    } // end if caught I/O Error
    directoryReader.close();
    
    return matches;
  }
  
  /**
   Rename a file whose corresponding to do item has changed. 
  
   @param fileToRename     The file to be renamed. 
   @param fileNameToRename The file name of the same file to be renamed. 
   @param newTitle         The new title of the to do item. 
   @return                 The new file, if renamed successfully, or a null
                           value, if not.
  */
  private File renameFileWithTitle (
      File fileToRename, 
      FileName fileNameToRename, 
      String newTitle) {
    boolean renamed = false;
    File parentFolder = fileToRename.getParentFile();
    File newFile = null;
    if (fileToRename.isDirectory()) {
      newFile = new File (parentFolder, newTitle);
      renamed = fileToRename.renameTo(newFile);
    }
    else
    if (fileToRename.isFile()) {
      newFile = new File (parentFolder, newTitle + "." + fileNameToRename.getExt());
      renamed = fileToRename.renameTo(newFile);
    }
    if (renamed) {
      Logger.getShared().recordEvent(LogEvent.NORMAL, 
          fileToRename + 
          " renamed to " 
          + newFile.toString(), 
          false);
    } else {
      Trouble.getShared().report(tabs1, 
        "Could not rename " + fileToRename.toString() +
        " to " + newFile.toString(), 
        "Trouble Renaming File");
    }
    if (renamed) {
      return newFile;
    } else {
      return null;
    }
  }
  
  public void setUnsavedChanges (boolean unsaved) {
    xos.setUnsavedChanges (unsaved);
    fileSaveButton.setEnabled (unsaved);
    // unsavedChanges = unsaved;
  }
  
  public boolean getUnsavedChanges() {
    return xos.getUnsavedChanges();
  }
  
  public void newUserPrefs() {
    // registration.loadFromUserPrefs();
    int autoSaveInterval = 0;
    try {
      autoSaveInterval = Integer.parseInt (userPrefs.getPref (AUTO_SAVE));
    } catch (NumberFormatException e) {
      // no action necessary
    }
    setAutoSave (autoSaveInterval);
  }
  
  /**
   Check to see if auto save interval has been modified by the user. 
  */
  private void checkAutoSave() {
    if (autoSaveInterval != prefsWindow.getAutoSave()) {
      setAutoSave(prefsWindow.getAutoSave());
    }
  }
  
  public void setAutoSave (int autoSaveInterval) {
    if (autoSaveTimer != null
        && autoSaveTimer.isRunning()) {
      autoSaveTimer.stop();
    }
    if (autoSaveInterval > 0) {
      if (autoSaveTimer == null) {
        autoSaveTimer = new javax.swing.Timer ((autoSaveInterval * 60 * 1000), this);
      } else {
        autoSaveTimer.setDelay (autoSaveInterval * 60 * 1000);
      }
      autoSaveTimer.start();
    }
    userPrefs.setPref (AUTO_SAVE, String.valueOf (autoSaveInterval)); 
    if (autoSaveInterval != prefsWindow.getAutoSave()) {
      prefsWindow.setAutoSave (autoSaveInterval);
    }
    this.autoSaveInterval = autoSaveInterval;
  }
  
  public void setSelItemTab (boolean selItemTab) {
    this.selItemTab = selItemTab;
  }
  
  /**
    Controller code for modification of view settings.
   
    @param inIndex New value for view index, if applicable. Value less than zero
                 will be ignored. 
    @param trigger Indicator of what event is triggering the modifications. This
           should have one of the following values.
        VIEW_TRIGGER_PROGRAM_START (0) = start of program.
        VIEW_TRIGGER_NEW_FILE (1) = new, empty, file being created.
        VIEW_TRIGGER_SELECT_FILE (2) = different file being selected.
        VIEW_TRIGGER_LEAVING_VIEW_TAB (3) = user is leaving the view tab.
        VIEW_TRIGGER_SELECT_VIEW_FROM_MENU (4) = user selected a different view
                                                 from the View menu.
        VIEW_TRIGGER_SELECT_VIEW_FROM_COMBO_BOX (5) = user selected a different
                                                      view from the View tab
                                                      combo box.
        VIEW_TRIGGER_VIEW_NEW (6) = User created a new view.
        VIEW_TRIGGER_VIEW_REMOVE (7) = User is deleting a view.
        VIEW_TRIGGER_ARCHIVING (8) = User is archiving, so we need to
                                     temporarily ensure that all items are
                                     being selected.
        VIEW_TRIGGER_DONE_ARCHIVING (9) = User is done archiving, so we can 
                                          restore the prior select options.
   */
  public void modifyView (int inIndex, int trigger) {
    
    int index = inIndex;
    
    // Make sure we have captured any user updates to the current view
    if (trigger > VIEW_TRIGGER_PROGRAM_START
        && trigger != VIEW_TRIGGER_DONE_ARCHIVING) {
      prefsWindow.updateViewFromForm();
    }
    
    // If we are archiving, we need to temporarily select all items
    if (trigger == VIEW_TRIGGER_ARCHIVING) {
      selectSave = views.getSelector().toString();
      views.getSelector().setSelectOption (ItemSelector.SHOW_ALL_STR);
    }
    
    if (trigger == VIEW_TRIGGER_DONE_ARCHIVING) {
      views.getSelector().setSelectOption (selectSave);
    }
    
    // Handle a request for a new view
    if (trigger == VIEW_TRIGGER_VIEW_NEW) {
      views.newView();
      index = views.getViewIndex();
    }
    
    // Handle a request to delete the current view
    if (trigger == VIEW_TRIGGER_VIEW_REMOVE) {
      views.removeView();
      index = views.getViewIndex();
    }
    
    // If we were passed a view index, then use it
    if (index >= 0) {
      views.setViewIndex (index, trigger);
    }
    
    // Update the file info if necessary
    if ((diskStore != null) 
        && (index >= 0)
        && (trigger > VIEW_TRIGGER_SELECT_FILE)) {
      diskStore.setViewIndex (index);
      diskStore.setComparator (views.getComparator());
    }
    
    // Set the GUI fields on the View Tab
    prefsWindow.setOptions (views.getComparator());
    
    // Make sure the list and tree views are using the latest view info
    sorted.setComparator (views.getComparator());
    sorted.sort();
    sorted.fireTableDataChanged();
    items.sortTagsModel();
    
    // Display current record counts, including records selected
    displayRecordCounts();
    
    // recalculate column content and widths on the List Tab
    listTab.setColumnWidths();

    // Make sure we retain information about the current file
    rememberLastFile();
  }
  
  public void launchWebPage() {
    if (item != null) {
      String webPage = item.getWebPage();
      if (webPage.length() > 7) {
        openURL (webPage);
      }
    }
  }
  
  public void browseFile (File webFile) {
    URI uri = webFile.toURI ();
    URL url;
    try {
      url = uri.toURL();
      openURL (url);
    } catch (MalformedURLException e) {
      trouble.report ("Trouble opening Web page " + uri.toString(), "Web Browser Problem");
    }
    // String url = "file://" + webFile.getAbsolutePath();
  }
  
  public boolean openURL (URL url) {
    return home.openURL(url);
  }
  
  public boolean openURL (String url) {
    return home.openURL(url);
  }
  
  /**
    Publish a to do list using a Web template or PSTextMerge script file.
   
    @param templateFile A File pointing to the file to be used for Web
                        publication.
   */
  public void publish (File templateFile) {
    if (templateFile == null
        || (! templateFile.exists())
        || (! templateFile.canRead())) {
      trouble.report ("Web Template " + templateFile.toString() 
          + " Not Accessible", "Template Problem");
    } 
    else
    if (templateFile.getName().endsWith ("." + TextMergeScript.SCRIPT_EXT)) {
      if (getUnsavedChanges()) {
        fileSave();
      }
      if (diskStore.isAFolder()) {
        try {
        items.getAllForEachTag(diskStore.getSplitTagsTDF());
        } catch (java.io.IOException e) {
          trouble.report 
              ("Trouble writing split tags file", "Split Tags Problem");
        }
      }
      // setActionMsg ("Publishing with PSTextMerge Script");
      diskStore.setTemplate (templateFile);
      PSTextMerge.execScript (templateFile.getAbsolutePath(), Logger.getShared());
      webWindow.setPublishedPage (null);
      // clearActionMsg();
    }
    else {
      // setActionMsg ("Publishing with Web template... ");
      diskStore.setTemplate (templateFile);
      try {
        File webPage = diskStore.publish (sorted);
        logEvent (LogEvent.NORMAL, 
            "Publishing using Web template " + templateFile.toString(),
            false);
        webWindow.setPublishedPage (webPage);
        logEvent (LogEvent.NORMAL, 
            "Created Web Page " + webPage.toString(),
            false);
      } catch (java.io.IOException e) {
        trouble.report ("Trouble generating Web Template", "Template Problem");
      }
      // clearActionMsg();
    } // end if templateFile not null
  } // end method
  
  public void logEvent (int severity, String msg, boolean dataRelated) {
    Logger.getShared().recordEvent (severity, msg, dataRelated);
  }
  
  public int getFrameWidth () {
    return frame.getWidth();
  }
  
  public void reportFolderTrouble () {

    trouble.report 
        ("Your data store must be opened" + GlobalConstants.LINE_FEED
        + "as a folder to perform this function.", "Save As a Folder First");
    
  }
  
  public void setLookAndFeel (String className) {
    try {
      javax.swing.UIManager.setLookAndFeel (className);
      if (frame != null) {
        SwingUtilities.updateComponentTreeUI (frame);
      }
    } catch (Exception e) {
      trouble.report ("Problems Setting New Look and Feel", "UI Problem");
    }
  }
  
  public void itemCopy () {
    if (item == null) {
      trouble.report ("Select an Item before trying to copy it", 
          "No Item Selected");
    } else {
      StringSelection block = new StringSelection (item.getTextBlock());
      clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
      clipBoard.setContents(block, this);
    }
  }
  
  public void itemPasteNew () {
    modIfChanged();
    int itemsAdded = 0;
    String blockText = "";
    clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
    Transferable contents = clipBoard.getContents(null);
    boolean hasTransferableText = (contents != null) 
        && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
    if (hasTransferableText) {
      try {
        blockText = (String)contents.getTransferData(DataFlavor.stringFlavor);
        if (TextBlock.isTextBlockFormat (blockText, ToDoItem.OBJECT_NAME)) {
          TextBlock block = new TextBlock (blockText);
          while (! block.endOfText()) {
            ToDoItem priorItem = item;
            item = new ToDoItem ();
            int numberOfFields = item.setFromTextBlock (block);
            if (numberOfFields > 0) {
              int i = items.add (item);
              itemsAdded++;
            } else { // end if number of fields > 0
              item = priorItem;
            }
          } // end while block has more text
        } else {
          // unformatted text
          if (blockText.length() > 0) {
            item = new ToDoItem ();
            item.setDescription (blockText);
            item.setDefaultTitle();
            item.setType ("Action");
            int i = items.add (item);
            itemsAdded++;
          } // end if adding from unformatted text
        }
        if (itemsAdded == 0) {
          JOptionPane.showMessageDialog(tabs1,
              "No items found",
              "Accept Results",
              JOptionPane.WARNING_MESSAGE);
        }
        else
        if (itemsAdded == 1) {
          setUnsavedChanges (true);
          JOptionPane.showMessageDialog(tabs1,
              "One item added",
              "Accept Results",
              JOptionPane.INFORMATION_MESSAGE);
             // newItem = true;
          displayItemNumber();
          activateItemTab();
          displayItem();
        }
        else
        if (itemsAdded > 1) {
          setUnsavedChanges (true);
          JOptionPane.showMessageDialog(tabs1,
              String.valueOf(itemsAdded) + " items added",
              "Accept Results",
              JOptionPane.INFORMATION_MESSAGE);
          activateListTab();
        }
      } // end trying to get text from clipboard
      catch (UnsupportedFlavorException ex){
        //highly unlikely since we are using a standard DataFlavor
      }
      catch (IOException ex) {
        trouble.report ("Trouble getting data from Clipboard",
            "Clipboard Problem");
      } // end catch io exception
    } // end if we've got anything to paste
    else {
      trouble.report ("Trouble getting data from Clipboard",
          "Clipboard Empty");
    }
  } // end method itemPasteNew
  
  public void lostOwnership (Clipboard clipBoard, Transferable contents) {
    clipBoardOwned = false;
  }
  
  public int verifyStatus (int newStat) {
    if (newStat == ActionStatus.PENDING_RECURS) {
      if (item.isDone()) {
        return ActionStatus.CLOSED;
      } else {
        if (item.isRecurring()) {
          Object[] options = { "Recur", "Close" };
          int userOption = JOptionPane.showOptionDialog(tabs1, "Create another recurrence?", 
              "Recur Option",
              JOptionPane.DEFAULT_OPTION, 
              JOptionPane.QUESTION_MESSAGE,
              null, options, options[0]);
          switch (userOption) {
            case 0:
              return newStat;
            case 1:
              return ActionStatus.CLOSED;
            default:
              break;
          } // end switch        
        } else {
          return ActionStatus.CLOSED;
        }
      } // end if item open
    } else { // not pending recurs
      return newStat;
    }
    return newStat;
  }
  
  public void editDueDate() {
    if (itemTab != null
        && item != null) {
      if (tabs2.getSelectedIndex() != ITEM_TAB_INDEX) {
        activateItemTab();
      }
      itemTab.editDueDate();
    }
  }
  
  public UserPrefs getUserPrefs () {
    return userPrefs;
  }
  
} // end class TwoDueCommon
