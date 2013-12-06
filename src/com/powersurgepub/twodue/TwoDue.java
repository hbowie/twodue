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

  import com.powersurgepub.psfiles.*;
  import com.powersurgepub.psdatalib.elements.*;
  import com.powersurgepub.psdatalib.pstags.*;
  import com.powersurgepub.psutils.*;
  import com.powersurgepub.twodue.data.*;
  import com.powersurgepub.twodue.disk.*;
  import com.powersurgepub.psdatalib.ui.*;
  import com.powersurgepub.xos2.*;
  import java.awt.*;
  import java.awt.event.*;
  import java.io.*;
  import java.net.*;
  import javax.swing.*;
  import javax.swing.event.*;
  import java.text.*;
  import java.util.*;

/**
   A GUI To Do list. <p>
  
 */

public class TwoDue 
    extends javax.swing.JFrame 
      implements 
        com.powersurgepub.twodue.disk.FileOpener, 
        TagsChangeAgent,
        XHandler 
          {
  
  public static final String PROGRAM_NAME = "Two Due";
  public static final String PROGRAM_VERSION = "3.10";
  
  public static final int    CHILD_WINDOW_X_OFFSET = 60;
  public static final int    CHILD_WINDOW_Y_OFFSET = 60;
  
  private String              lastTextFound = "";
  
  private             String  country = "  ";
	private             String  language = "  ";
  
  Appster             appster;
  
  Home                home;
        
  XOS                 xos        = XOS.getShared();
        
  Logger              logger     = Logger.getShared();
        
  private static final String PROGRAM_HISTORY  
      = "versions.html";
  private static final String USER_GUIDE  
      = "twodue.html";
  private static final String HOME_PAGE   
      = "http://www.powersurgepub.com/products/twodue.html";
  public  static final String FIND = "Find";
  public  static final String FIND_AGAIN = "Again";
  private URL                 userGuideURL;
  private URL                 quickStartURL;
  private URL                 programHistoryURL;
  private int                 shortcutKeyMask;
  
  private TwoDueCommon   td;
  
  private DateFormat    longDateFormatter 
      = new SimpleDateFormat ("EEEE MMMM d, yyyy");
  
  // Hand-tailored GUI Elements
  private javax.swing.Timer autoSaveTimer;
  private JMenu knownFilesMenu;
  
  private File startingFile;
  private boolean fileOpenedByHandler = false;
  
  // Debugging
  private int printCount = 0;
  
  /** 
    Creates new form TwoDue.
   */
  public TwoDue(String[] args) {
    
    if (args.length >= 1) {
      logger.recordEvent (LogEvent.NORMAL, 
        "Application passed an argument of " + args[0],
        false);
      File argCheck = new File(args[0]);
      if (argCheck.exists() && argCheck.canRead()) {
        startingFile = argCheck;
      } 
    }
    
    // Perform Platform-Specific Initialization
    appster = new Appster
        ("powersurgepub", "com",
          PROGRAM_NAME, PROGRAM_VERSION,
          language, country,
          this, this);

    
    // Get shared objects
    home = Home.getShared();
    UserPrefs userPrefs = UserPrefs.getShared();
    
    initComponents();
    
    WindowMenuManager.getShared().addWindowMenu(windowMenu);
    
    td = new TwoDueCommon (this, mainSplitPane, tabs1, tabs2);

    // td.frame = this;
    td.autoSaveTimer = autoSaveTimer;
    try {
      userGuideURL = new URL (td.pageURL, USER_GUIDE);
    } catch (MalformedURLException e) {
    }
    try {
      quickStartURL = new URL (td.pageURL, td.QUICK_START);
    } catch (MalformedURLException e) {
    }
    try {
      programHistoryURL = new URL (td.pageURL, PROGRAM_HISTORY);
    } catch (MalformedURLException e) {
    }
    // userGuide = "file:/" + System.getProperty (GlobalConstants.USER_DIR) + USER_GUIDE;
    shortcutKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
    
    // navToolBar.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    // initComponents();
    setBounds (
        td.userPrefs.getPrefAsInt (td.LEFT, 40),
        td.userPrefs.getPrefAsInt (td.TOP,  40),
        td.userPrefs.getPrefAsInt (td.WIDTH, 620),
        td.userPrefs.getPrefAsInt (td.HEIGHT, 540));
    xos.setHelpMenuItem (helpUserGuideMenuItem);
    td.editMenu = editMenu;
    td.fileNameLabel = fileNameLabel;
    td.indexLabel = indexLabel;
    td.sizeLabel = sizeLabel;
    td.fullSizeLabel = fullSizeLabel;
    td.fileSaveButton = fileSaveButton;
    td.files = new TwoDueDiskDirectory(this, Logger.getShared());
    td.views = td.files.getViews();
    td.views.setTwoDueCommon (td);

    td.initComponents();
    td.longDateFormatter = longDateFormatter;
    td.todayLabel = todayLabel;
    
    // Set Open Known Menu Items
    
    fileMenu.add (td.files.getRecentFilesMenu()); 
    td.prefsWindow.setMenu (viewMenu);
    findText.grabFocus();
    
    // Open default disk store for user if one exists
    if (fileOpenedByHandler) {
      // do nothing -- file already opened
    }
    else
    if (startingFile != null) {
      handleOpenFile (startingFile);
    } else {
      td.fileOpenDefault();
      /*
      TwoDueDiskStore defaultStore = td.files.getDefaultStore();
      if (defaultStore == null) {
        td.fileNew();
      } else {
        handleOpenFile (defaultStore);
      }
      */
    }
    
    // Set About, Quit and other Handlers in platform-specific ways
    xos.setFileMenu (fileMenu);
    xos.setHelpMenu (helpMenu);
    xos.setXHandler (this);
    xos.setMainWindow (this);
    xos.enablePreferences();
    
    // Check to see if user is registered
  }
  
  public void logSysProps () {
    Properties p = System.getProperties();
    Enumeration seq = p.propertyNames();
    StringBuffer sysPropLine = new StringBuffer();
    while (seq.hasMoreElements()) {
      String propName = (String)seq.nextElement();
      String property = p.getProperty(propName);
      sysPropLine = new StringBuffer();
      if (propName.equals ("line.separator")) {
        sysPropLine.append (propName + ": ");
        for (int i = 0; i < property.length(); i++) {
          if (i > 0) {
            sysPropLine.append ("/");
          }
          if (property.charAt(i) == GlobalConstants.CARRIAGE_RETURN) {
            sysPropLine.append ("CR (ASCII 13)");
          } else
          if (property.charAt(i) == GlobalConstants.LINE_FEED) {
            sysPropLine.append ("LF (ASCII 10)");
          } else {
            sysPropLine.append (String.valueOf (property.charAt(i)));
          }
        }
      } else {
        sysPropLine.append (propName + ": " + property);
      }
      logger.recordEvent (LogEvent.NORMAL, 
          sysPropLine.toString(),
          false);
    }  // end while more system properties
  } // end method
  
  public void handleOpenApplication (File inFile) {
    logger.recordEvent (LogEvent.NORMAL, 
        "Open Application Handler Invoked with file " + inFile.toString(),
        false);
    td.fileOpen (inFile);
  }
  
  public void handleOpenApplication() {
      
  }
  
  /**      
    Standard way to respond to a document being passed to this application on a Mac.
   
    @param inFile File to be processed by this application, generally
                  as a result of a file or directory being dragged
                  onto the application icon.
   */
  public void handleOpenFile (File inFile) {
    logger.recordEvent (LogEvent.NORMAL, 
        "Open File Handler Invoked with file " + inFile.toString(),
        false);
    fileOpenedByHandler = true;
    td.fileOpen (inFile);
  }

  public void handleOpenURI(URI inURI) {
    
  }
  
  public void handlePrintFile (File printFile) {
    // not currently supported
  }

  public boolean preferencesAvailable() {
    return true;
  }
  
  public void handlePreferences () {
    td.displayPrefs();
  }
  
  /**      
    Opens a file and sets its view options.
   
    @param inFile File to be opened.
    @param inView View options to be used with this file.
   
  public void handleOpenFile (File inFile, String inView) {
    td.fileOpen (inFile, inView, null, 0);
  }
   */
  
  /**      
    Opens a file and sets its view options and Web template.
   
    @param inFile File to be opened.
    @param inView View options to be used with this file.
    @param inTemplate Template file to be used for Web publishing.
   
  public void handleOpenFile (File inFile, String inView, File inTemplate) {
    td.fileOpen (inFile, inView, inTemplate, 0);
  }
   */
  
  /**      
    Opens a file and sets its view options and Web template 
    and publishing frequence.
   
    @param inFile File to be opened.
    @param inView View options to be used with this file.
    @param inTemplate Template file to be used for Web publishing.
    @param inPublishWhen Indicator of how often file should be published.
   
  public void handleOpenFile (File inFile, String inView, 
      File inTemplate, int inPublishWhen) {
    td.fileOpen (inFile, inView, inTemplate, inPublishWhen);
  }
   */
  
  /**      
    Standard way to respond to a document being passed to this application on a Mac.
   
    @param inFStore Disk store to be processed by this application.
   */
  public void handleOpenFile (TwoDueDiskStore inStore) {
    td.fileOpen (inStore);
  }
  
  /**
     We're out of here!
   */
  public void handleQuit() {

    td.modIfChanged();
    td.fileClose();
    if (td.diskStore != null
        && (! td.diskStore.isUnknown())) {
      td.diskStore.setTemplate (td.webWindow.getTemplate());
      td.rememberLastFile ();
    }
    td.files.saveToDisk();
    td.views.saveViews();
    td.userPrefs.setPref (td.LEFT, td.frame.getX());
    td.userPrefs.setPref (td.TOP, td.frame.getY());
    td.userPrefs.setPref (td.WIDTH, td.frame.getWidth());
    td.userPrefs.setPref (td.HEIGHT, td.frame.getHeight());
    td.savePrefs();
    
    td.prefsWindow.savePrefs();
    td.prefsWindow.handleQuit();

    System.exit(0);
  }
  
  private void nextItem() {
    td.modIfChanged();
    td.navigator.nextItem();
    td.displayItem();
  }
  
  private void priorItem() {
    td.modIfChanged();
    td.navigator.priorItem();
    td.displayItem();
  }
  
  private void firstItem() {
    td.modIfChanged();
    td.navigator.firstItem();
    td.displayItem();
  }
  
  private void lastItem() {
    td.modIfChanged();
    td.navigator.lastItem();
    td.displayItem();
  }
  
  private void findItem () {
    String findString = findText.getText();
    if (findString != null) {
      if (findString.length() > 0) {
        if (findButton.getText().equals (FIND)) {
          td.findItem (findString);
          findButton.setText(FIND_AGAIN);
          lastTextFound = findString;
        } else {
          td.findAgain();
        } // end if not doing initial find
      } else {
        findText.grabFocus();
      }
    } else {
      findText.grabFocus();
    }
  }
  
  private void findAgain () {
    td.findAgain();
  }
  
  private void newItem() {
    td.modIfChanged();
    boolean ok = td.newItem();
    if (ok) {
      td.displayItem();
      td.activateItemTab();
    }
  }
  
  private void deleteItem() {
    if (! td.newItem) {
      boolean proceedWithDelete = true;
      if (td.prefsWindow.getCommonPrefs().confirmDeletes()) {
        int userOption = JOptionPane.showConfirmDialog(tabs1, "Really delete this record?", 
            "Delete Confirmation",
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE);
        proceedWithDelete = (userOption == JOptionPane.YES_OPTION); 
      }
      if (proceedWithDelete) {
        ToDoItem itemToDelete = td.getItem();
        if (itemToDelete.getWebPage().startsWith("file:/")) {
          FileName fileNameToDelete = new FileName(itemToDelete.getWebPage());
          File fileToDelete = fileNameToDelete.getFile();
          // System.out.println ("File to delete = " + fileToDelete.toString());
          if (fileToDelete.exists()) {
            int fileDeleteOption = JOptionPane.showConfirmDialog(tabs1, 
                "Delete file " + fileToDelete.toString() + "?",
                "File Delete Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            if (fileDeleteOption == JOptionPane.YES_OPTION) {
              boolean fileDeleteOK = fileToDelete.delete();
              if (! fileDeleteOK) {
                Trouble.getShared().report(this, 
                    "Could not delete " + fileToDelete.toString(), 
                    "Trouble Deleting File");
              }
            }
          } else {
            // System.out.println("File could not be found.");
          }
        }
        td.navigator.nextItem();
        td.items.remove (itemToDelete);
        td.setUnsavedChanges (true);
        td.displayItem();
      } // end if user confirmed delete
    } // end if new item not yet saved
  } // end method
  
  private void dueDateRecursion() {
    td.modIfChanged();
    td.getItem().recur();
    td.displayItem();
  }
  
  /**
     Standard way to respond to an About Menu Item Selection on a Mac.
   */
  public void handleAbout() {
    td.displayAbout();
  }
  
  private void replaceTags() {
    
    td.modIfChanged();
		TagsChangeScreen replaceScreen = new TagsChangeScreen
				(this, true, td.items.getTagsList(), this);
		replaceScreen.setLocation (
				this.getX() + CHILD_WINDOW_X_OFFSET,
				this.getY() + CHILD_WINDOW_Y_OFFSET);
		replaceScreen.setVisible (true);
		td.setUnsavedChanges (true);
  }
  
	/**
	 Called from TagsChangeScreen.
	 @param from The from String.
	 @param to	 The to String.
	 */
	public void changeAllTags (String from, String to) {

		td.modIfChanged();
		ToDoItem workItem = new ToDoItem ();
		int mods = 0;
		for (int workIndex = 0; workIndex < td.items.size(); workIndex++) {
			workItem = td.items.get (workIndex);
			// workItem.setIndex (workIndex);
			String before = workItem.getTags().toString();
			workItem.getTags().replace (from, to);
			if (! before.equals (workItem.getTags().toString())) {
				mods++;
				td.items.modify(workItem);
			}
		}

		JOptionPane.showMessageDialog(this,
			String.valueOf (mods)
					+ " tags changed",
			"Tags Replacement Results",
			JOptionPane.INFORMATION_MESSAGE);
		td.displayItem();
	}
  
  /**
   Display an auxiliary window with new window 
   tucked 60 pixels inside of upper left corner.
  
   @param window JFrame to be displayed.
   */
  public void displayAuxiliaryWindow(WindowToManage window) {
    window.setLocation(
        this.getX() + CHILD_WINDOW_X_OFFSET,
        this.getY() + CHILD_WINDOW_Y_OFFSET);
    WindowMenuManager.getShared().makeVisible(window);
  }
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    navToolBar = new javax.swing.JToolBar();
    itemDoneButton = new javax.swing.JButton();
    itemNewButton = new javax.swing.JButton();
    itemDeleteButton = new javax.swing.JButton();
    itemFirstButton = new javax.swing.JButton();
    itemPriorButton = new javax.swing.JButton();
    itemNextButton = new javax.swing.JButton();
    itemLastButton = new javax.swing.JButton();
    fileSaveButton = new javax.swing.JButton();
    itemLaunchButton = new javax.swing.JButton();
    findText = new javax.swing.JTextField();
    findButton = new javax.swing.JButton();
    mainSplitPane = new javax.swing.JSplitPane();
    tabs1 = new javax.swing.JTabbedPane();
    tabs2 = new javax.swing.JTabbedPane();
    footerPanel = new javax.swing.JPanel();
    fileNameLabel = new javax.swing.JLabel();
    dividerLabel1 = new javax.swing.JLabel();
    indexLabel = new javax.swing.JLabel();
    ofLabel = new javax.swing.JLabel();
    sizeLabel = new javax.swing.JLabel();
    fullSizeSpacerLabel = new javax.swing.JLabel();
    fullSizeLabel = new javax.swing.JLabel();
    dividerLabel2 = new javax.swing.JLabel();
    todayLabel = new javax.swing.JLabel();
    mainMenuBar = new javax.swing.JMenuBar();
    fileMenu = new javax.swing.JMenu();
    fileNewMenuItem = new javax.swing.JMenuItem();
    fileOpenMenuItem = new javax.swing.JMenuItem();
    jSeparator4 = new javax.swing.JPopupMenu.Separator();
    fileGetInfoMenuItem = new javax.swing.JMenuItem();
    folderSyncMenuItem = new javax.swing.JMenuItem();
    jSeparator3 = new javax.swing.JPopupMenu.Separator();
    fileImportMenuItem = new javax.swing.JMenuItem();
    fileSaveMenuItem = new javax.swing.JMenuItem();
    fileSaveAsMenuItem = new javax.swing.JMenuItem();
    jSeparator2 = new javax.swing.JPopupMenu.Separator();
    listPublishMenuItem = new javax.swing.JMenuItem();
    jSeparator5 = new javax.swing.JPopupMenu.Separator();
    fileBackupMenuItem = new javax.swing.JMenuItem();
    fileRevertMenuItem = new javax.swing.JMenuItem();
    jSeparator6 = new javax.swing.JPopupMenu.Separator();
    editMenu = new javax.swing.JMenu();
    listMenu = new javax.swing.JMenu();
    recordFindMenuItem = new javax.swing.JMenuItem();
    recordFindAgainMenuItem = new javax.swing.JMenuItem();
    jSeparator1 = new javax.swing.JPopupMenu.Separator();
    listReplaceCategoryMenuItem = new javax.swing.JMenuItem();
    listSchedule = new javax.swing.JMenuItem();
    listValidateURLs = new javax.swing.JMenuItem();
    listSlackMenuItem = new javax.swing.JMenuItem();
    listResetDueDatesMenuItem = new javax.swing.JMenuItem();
    listPurgeMenuItem = new javax.swing.JMenuItem();
    recordMenu = new javax.swing.JMenu();
    recordDeleteMenuItem = new javax.swing.JMenuItem();
    recordNewMenuItem = new javax.swing.JMenuItem();
    recordNextMenuItem = new javax.swing.JMenuItem();
    recordPriorMenuItem = new javax.swing.JMenuItem();
    recordCloseMenuItem = new javax.swing.JMenuItem();
    recordRecurMenuItem = new javax.swing.JMenuItem();
    recordLaunchMenuItem = new javax.swing.JMenuItem();
    recordEditDateMenuItem = new javax.swing.JMenuItem();
    recordCopyPasteMenuSep = new javax.swing.JSeparator();
    recordCopyMenuItem = new javax.swing.JMenuItem();
    recordPasteNewMenuItem = new javax.swing.JMenuItem();
    tabsMenu = new javax.swing.JMenu();
    tabsListMenuItem = new javax.swing.JMenuItem();
    tabsTreeMenuItem = new javax.swing.JMenuItem();
    tabsItemMenuItem = new javax.swing.JMenuItem();
    tabsTimeMenuItem = new javax.swing.JMenuItem();
    tabsRecursMenuItem = new javax.swing.JMenuItem();
    viewMenu = new javax.swing.JMenu();
    viewByDueDateMenuItem = new javax.swing.JMenuItem();
    viewByPriorityMenuItem = new javax.swing.JMenuItem();
    toolsMenu = new javax.swing.JMenu();
    optionsMenuItem = new javax.swing.JMenuItem();
    windowMenu = new javax.swing.JMenu();
    helpMenu = new javax.swing.JMenu();
    helpHistoryMenuItem = new javax.swing.JMenuItem();
    helpUserGuideMenuItem = new javax.swing.JMenuItem();
    helpWebSeparator = new javax.swing.JSeparator();
    helpHomePageMenuItem = new javax.swing.JMenuItem();
    helpMiscSeparator = new javax.swing.JSeparator();
    helpReduceWindowSizeMenuItem = new javax.swing.JMenuItem();

    setTitle("Two Due");
    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        exitForm(evt);
      }
    });

    itemDoneButton.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
    itemDoneButton.setText("OK");
    itemDoneButton.setToolTipText("Add new piece of wisdom");
    itemDoneButton.setMargin(new java.awt.Insets(0, 4, 4, 4));
    itemDoneButton.setMaximumSize(new java.awt.Dimension(60, 30));
    itemDoneButton.setMinimumSize(new java.awt.Dimension(30, 26));
    itemDoneButton.setPreferredSize(new java.awt.Dimension(40, 28));
    itemDoneButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        itemDoneButtonActionPerformed(evt);
      }
    });
    navToolBar.add(itemDoneButton);

    itemNewButton.setText("+");
    itemNewButton.setMaximumSize(new java.awt.Dimension(60, 30));
    itemNewButton.setMinimumSize(new java.awt.Dimension(30, 26));
    itemNewButton.setPreferredSize(new java.awt.Dimension(40, 28));
    itemNewButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        itemNewButtonActionPerformed(evt);
      }
    });
    navToolBar.add(itemNewButton);

    itemDeleteButton.setText("-");
    itemDeleteButton.setMaximumSize(new java.awt.Dimension(60, 30));
    itemDeleteButton.setMinimumSize(new java.awt.Dimension(30, 26));
    itemDeleteButton.setPreferredSize(new java.awt.Dimension(40, 28));
    itemDeleteButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        itemDeleteButtonActionPerformed(evt);
      }
    });
    navToolBar.add(itemDeleteButton);

    itemFirstButton.setText("<<");
    itemFirstButton.setMaximumSize(new java.awt.Dimension(60, 30));
    itemFirstButton.setMinimumSize(new java.awt.Dimension(48, 26));
    itemFirstButton.setPreferredSize(new java.awt.Dimension(40, 28));
    itemFirstButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        itemFirstButtonAction(evt);
      }
    });
    navToolBar.add(itemFirstButton);

    itemPriorButton.setText("<");
    itemPriorButton.setMaximumSize(new java.awt.Dimension(60, 30));
    itemPriorButton.setMinimumSize(new java.awt.Dimension(30, 26));
    itemPriorButton.setPreferredSize(new java.awt.Dimension(40, 28));
    itemPriorButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        itemPriorButtonAction(evt);
      }
    });
    navToolBar.add(itemPriorButton);

    itemNextButton.setText(">");
    itemNextButton.setMaximumSize(new java.awt.Dimension(60, 30));
    itemNextButton.setMinimumSize(new java.awt.Dimension(30, 26));
    itemNextButton.setPreferredSize(new java.awt.Dimension(40, 28));
    itemNextButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        itemNextButtonAction(evt);
      }
    });
    navToolBar.add(itemNextButton);

    itemLastButton.setText(">>");
    itemLastButton.setMaximumSize(new java.awt.Dimension(60, 30));
    itemLastButton.setMinimumSize(new java.awt.Dimension(30, 26));
    itemLastButton.setPreferredSize(new java.awt.Dimension(40, 28));
    itemLastButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        itemLastButtonAction(evt);
      }
    });
    navToolBar.add(itemLastButton);

    fileSaveButton.setText("Save");
    fileSaveButton.setEnabled(false);
    fileSaveButton.setMaximumSize(new java.awt.Dimension(72, 30));
    fileSaveButton.setMinimumSize(new java.awt.Dimension(48, 26));
    fileSaveButton.setPreferredSize(new java.awt.Dimension(60, 28));
    fileSaveButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        fileSaveButtonActionPerformed(evt);
      }
    });
    navToolBar.add(fileSaveButton);

    itemLaunchButton.setText("Launch");
    itemLaunchButton.setMaximumSize(new java.awt.Dimension(72, 30));
    itemLaunchButton.setMinimumSize(new java.awt.Dimension(48, 26));
    itemLaunchButton.setPreferredSize(new java.awt.Dimension(60, 28));
    itemLaunchButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        itemLaunchButtonActionPerformed(evt);
      }
    });
    navToolBar.add(itemLaunchButton);

    findText.setMargin(new java.awt.Insets(4, 4, 4, 4));
    findText.setMaximumSize(new java.awt.Dimension(240, 30));
    findText.setMinimumSize(new java.awt.Dimension(40, 26));
    findText.setPreferredSize(new java.awt.Dimension(120, 28));
    findText.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        findTextActionPerformed(evt);
      }
    });
    findText.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(java.awt.event.KeyEvent evt) {
        findTextKeyTyped(evt);
      }
    });
    navToolBar.add(findText);

    findButton.setText("Find");
    findButton.setToolTipText("Search for the text entered to the left");
    findButton.setMaximumSize(new java.awt.Dimension(72, 30));
    findButton.setMinimumSize(new java.awt.Dimension(48, 26));
    findButton.setPreferredSize(new java.awt.Dimension(60, 28));
    findButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        findButtonActionPerformed(evt);
      }
    });
    navToolBar.add(findButton);

    getContentPane().add(navToolBar, java.awt.BorderLayout.NORTH);

    mainSplitPane.setDividerLocation(320);
    mainSplitPane.setResizeWeight(0.5);
    mainSplitPane.setLeftComponent(tabs1);

    tabs2.setMinimumSize(new java.awt.Dimension(400, 400));
    tabs2.setPreferredSize(new java.awt.Dimension(339, 300));
    mainSplitPane.setRightComponent(tabs2);

    getContentPane().add(mainSplitPane, java.awt.BorderLayout.CENTER);

    footerPanel.setLayout(new java.awt.GridBagLayout());

    fileNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    fileNameLabel.setMaximumSize(new java.awt.Dimension(400, 40));
    fileNameLabel.setMinimumSize(new java.awt.Dimension(100, 14));
    fileNameLabel.setPreferredSize(new java.awt.Dimension(200, 18));
    todayLabel.setText (longDateFormatter.format (new Date()));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 0.5;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    footerPanel.add(fileNameLabel, gridBagConstraints);

    dividerLabel1.setText("|");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridy = 1;
    gridBagConstraints.weightx = 0.1;
    gridBagConstraints.insets = new java.awt.Insets(2, 8, 2, 8);
    footerPanel.add(dividerLabel1, gridBagConstraints);

    indexLabel.setText("1");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridy = 1;
    gridBagConstraints.weightx = 0.1;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    footerPanel.add(indexLabel, gridBagConstraints);

    ofLabel.setText("of");
    ofLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridy = 1;
    gridBagConstraints.weightx = 0.1;
    gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
    footerPanel.add(ofLabel, gridBagConstraints);

    sizeLabel.setText("1");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridy = 1;
    gridBagConstraints.weightx = 0.1;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    footerPanel.add(sizeLabel, gridBagConstraints);

    fullSizeSpacerLabel.setText("  ");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridy = 1;
    gridBagConstraints.weightx = 0.1;
    gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
    footerPanel.add(fullSizeSpacerLabel, gridBagConstraints);

    fullSizeLabel.setText("(2)");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridy = 1;
    gridBagConstraints.weightx = 0.1;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    footerPanel.add(fullSizeLabel, gridBagConstraints);

    dividerLabel2.setText("|");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridy = 1;
    gridBagConstraints.weightx = 0.1;
    gridBagConstraints.insets = new java.awt.Insets(2, 8, 2, 8);
    footerPanel.add(dividerLabel2, gridBagConstraints);

    todayLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    todayLabel.setMaximumSize(new java.awt.Dimension(400, 40));
    todayLabel.setMinimumSize(new java.awt.Dimension(100, 14));
    todayLabel.setPreferredSize(new java.awt.Dimension(200, 18));
    todayLabel.setText (longDateFormatter.format (new Date()));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.weightx = 0.2;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 22);
    footerPanel.add(todayLabel, gridBagConstraints);

    getContentPane().add(footerPanel, java.awt.BorderLayout.SOUTH);

    fileMenu.setText("File");

    fileNewMenuItem.setText("New");
    fileNewMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        fileNewMenuItemActionPerformed(evt);
      }
    });
    fileMenu.add(fileNewMenuItem);

    fileOpenMenuItem.setText("Open...");
    fileOpenMenuItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_O,
      Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
  fileOpenMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      fileOpenMenuItemActionPerformed(evt);
    }
  });
  fileMenu.add(fileOpenMenuItem);
  fileMenu.add(jSeparator4);

  fileGetInfoMenuItem.setText("Get Info");
  fileGetInfoMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      fileGetInfoMenuItemActionPerformed(evt);
    }
  });
  fileMenu.add(fileGetInfoMenuItem);

  folderSyncMenuItem.setText("Folder Sync...");
  folderSyncMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      folderSyncMenuItemActionPerformed(evt);
    }
  });
  fileMenu.add(folderSyncMenuItem);
  fileMenu.add(jSeparator3);

  fileImportMenuItem.setText("Import...");
  fileImportMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      fileImportMenuItemActionPerformed(evt);
    }
  });
  fileMenu.add(fileImportMenuItem);

  fileSaveMenuItem.setText("Save");
  fileSaveMenuItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_S,
    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
fileSaveMenuItem.addActionListener(new java.awt.event.ActionListener() {
  public void actionPerformed(java.awt.event.ActionEvent evt) {
    fileSaveMenuItemActionPerformed(evt);
  }
  });
  fileMenu.add(fileSaveMenuItem);

  fileSaveAsMenuItem.setText("Save As...");
  fileSaveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      fileSaveAsMenuItemActionPerformed(evt);
    }
  });
  fileMenu.add(fileSaveAsMenuItem);
  fileMenu.add(jSeparator2);

  listPublishMenuItem.setText("Publish...");
  listPublishMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      listPublishMenuItemActionPerformed(evt);
    }
  });
  fileMenu.add(listPublishMenuItem);
  fileMenu.add(jSeparator5);

  fileBackupMenuItem.setText("Backup...");
  fileBackupMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      fileBackupMenuItemActionPerformed(evt);
    }
  });
  fileMenu.add(fileBackupMenuItem);

  fileRevertMenuItem.setText("Revert from Backup...");
  fileRevertMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      fileRevertMenuItemActionPerformed(evt);
    }
  });
  fileMenu.add(fileRevertMenuItem);
  fileMenu.add(jSeparator6);

  mainMenuBar.add(fileMenu);

  editMenu.setActionCommand("Edit");
  editMenu.setLabel("Edit");
  mainMenuBar.add(editMenu);

  listMenu.setText("List");
  listMenu.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      listMenuActionPerformed(evt);
    }
  });

  recordFindMenuItem.setText("Find");
  recordFindMenuItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_F,
    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
recordFindMenuItem.addActionListener(new java.awt.event.ActionListener() {
  public void actionPerformed(java.awt.event.ActionEvent evt) {
    recordFindMenuItemActionPerformed(evt);
  }
  });
  listMenu.add(recordFindMenuItem);

  recordFindAgainMenuItem.setText("Find Again");
  recordFindAgainMenuItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_G,
    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
recordFindAgainMenuItem.addActionListener(new java.awt.event.ActionListener() {
  public void actionPerformed(java.awt.event.ActionEvent evt) {
    recordFindAgainMenuItemActionPerformed(evt);
  }
  });
  listMenu.add(recordFindAgainMenuItem);
  listMenu.add(jSeparator1);

  listReplaceCategoryMenuItem.setText("Replace Tags...");
  recordFindAgainMenuItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_G,
    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
listReplaceCategoryMenuItem.addActionListener(new java.awt.event.ActionListener() {
  public void actionPerformed(java.awt.event.ActionEvent evt) {
    listReplaceCategoryMenuItemActionPerformed(evt);
  }
  });
  listMenu.add(listReplaceCategoryMenuItem);

  listSchedule.setText("Schedule Start Times");
  listSchedule.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      listScheduleActionPerformed(evt);
    }
  });
  listMenu.add(listSchedule);

  listValidateURLs.setText("Validate Web Pages...");
  listValidateURLs.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      listValidateURLsActionPerformed(evt);
    }
  });
  listMenu.add(listValidateURLs);

  listSlackMenuItem.setText("Cut Some Slack...");
  listSlackMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      listSlackMenuItemActionPerformed(evt);
    }
  });
  listMenu.add(listSlackMenuItem);

  listResetDueDatesMenuItem.setText("Reset Due Dates...");
  listResetDueDatesMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      listResetDueDatesMenuItemActionPerformed(evt);
    }
  });
  listMenu.add(listResetDueDatesMenuItem);

  listPurgeMenuItem.setText("Purge...");
  listPurgeMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      listPurgeMenuItemActionPerformed(evt);
    }
  });
  listMenu.add(listPurgeMenuItem);

  mainMenuBar.add(listMenu);

  recordMenu.setText("Item");

  recordDeleteMenuItem.setText("Delete");
  recordDeleteMenuItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_D,
    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
recordDeleteMenuItem.addActionListener(new java.awt.event.ActionListener() {
  public void actionPerformed(java.awt.event.ActionEvent evt) {
    recordDeleteMenuItemActionPerformed(evt);
  }
  });
  recordMenu.add(recordDeleteMenuItem);

  recordNewMenuItem.setText("New");
  recordNewMenuItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_N,
    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
recordNewMenuItem.addActionListener(new java.awt.event.ActionListener() {
  public void actionPerformed(java.awt.event.ActionEvent evt) {
    recordNewMenuItemActionPerformed(evt);
  }
  });
  recordMenu.add(recordNewMenuItem);

  recordNextMenuItem.setText("Next");
  recordNextMenuItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_CLOSE_BRACKET,
    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
recordNextMenuItem.addActionListener(new java.awt.event.ActionListener() {
  public void actionPerformed(java.awt.event.ActionEvent evt) {
    recordNextMenuItemActionPerformed(evt);
  }
  });
  recordMenu.add(recordNextMenuItem);

  recordPriorMenuItem.setText("Prior");
  recordPriorMenuItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_OPEN_BRACKET,
    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
recordPriorMenuItem.addActionListener(new java.awt.event.ActionListener() {
  public void actionPerformed(java.awt.event.ActionEvent evt) {
    recordPriorMenuItemActionPerformed(evt);
  }
  });
  recordMenu.add(recordPriorMenuItem);

  recordCloseMenuItem.setText("Close");
  recordCloseMenuItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_K,
    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
recordCloseMenuItem.addActionListener(new java.awt.event.ActionListener() {
  public void actionPerformed(java.awt.event.ActionEvent evt) {
    recordCloseMenuItemActionPerformed(evt);
  }
  });
  recordMenu.add(recordCloseMenuItem);

  recordRecurMenuItem.setText("Recur");
  recordRecurMenuItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_R,
    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
recordRecurMenuItem.addActionListener(new java.awt.event.ActionListener() {
  public void actionPerformed(java.awt.event.ActionEvent evt) {
    recordRecurMenuItemActionPerformed(evt);
  }
  });
  recordMenu.add(recordRecurMenuItem);

  recordLaunchMenuItem.setText("Launch");
  recordLaunchMenuItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_L,
    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
recordLaunchMenuItem.addActionListener(new java.awt.event.ActionListener() {
  public void actionPerformed(java.awt.event.ActionEvent evt) {
    recordLaunchMenuItemActionPerformed(evt);
  }
  });
  recordMenu.add(recordLaunchMenuItem);

  recordEditDateMenuItem.setText("Edit Due Date");
  recordEditDateMenuItem.setActionCommand("EditDate");
  recordEditDateMenuItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_E,
    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
recordEditDateMenuItem.addActionListener(new java.awt.event.ActionListener() {
  public void actionPerformed(java.awt.event.ActionEvent evt) {
    recordEditDateMenuItemActionPerformed(evt);
  }
  });
  recordMenu.add(recordEditDateMenuItem);
  recordEditDateMenuItem.getAccessibleContext().setAccessibleName("EditDate");

  recordMenu.add(recordCopyPasteMenuSep);

  recordCopyMenuItem.setText("Transfer");
  recordCopyMenuItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_T,
    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
recordCopyMenuItem.addActionListener(new java.awt.event.ActionListener() {
  public void actionPerformed(java.awt.event.ActionEvent evt) {
    recordCopyMenuItemActionPerformed(evt);
  }
  });
  recordMenu.add(recordCopyMenuItem);

  recordPasteNewMenuItem.setText("Accept");
  recordPasteNewMenuItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_P,
    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
recordPasteNewMenuItem.addActionListener(new java.awt.event.ActionListener() {
  public void actionPerformed(java.awt.event.ActionEvent evt) {
    recordPasteNewMenuItemActionPerformed(evt);
  }
  });
  recordMenu.add(recordPasteNewMenuItem);

  mainMenuBar.add(recordMenu);

  tabsMenu.setText("Tabs");

  tabsListMenuItem.setText("List");
  tabsListMenuItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_1,
    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
tabsListMenuItem.addActionListener(new java.awt.event.ActionListener() {
  public void actionPerformed(java.awt.event.ActionEvent evt) {
    tabsListMenuItemActionPerformed(evt);
  }
  });
  tabsMenu.add(tabsListMenuItem);

  tabsTreeMenuItem.setText("Tree");
  tabsTreeMenuItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_2,
    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
tabsTreeMenuItem.addActionListener(new java.awt.event.ActionListener() {
  public void actionPerformed(java.awt.event.ActionEvent evt) {
    tabsTreeMenuItemActionPerformed(evt);
  }
  });
  tabsMenu.add(tabsTreeMenuItem);

  tabsItemMenuItem.setText("Item");
  tabsItemMenuItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_3,
    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
tabsItemMenuItem.addActionListener(new java.awt.event.ActionListener() {
  public void actionPerformed(java.awt.event.ActionEvent evt) {
    tabsItemMenuItemActionPerformed(evt);
  }
  });
  tabsMenu.add(tabsItemMenuItem);

  tabsTimeMenuItem.setText("Time");
  tabsTimeMenuItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_4,
    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
tabsTimeMenuItem.addActionListener(new java.awt.event.ActionListener() {
  public void actionPerformed(java.awt.event.ActionEvent evt) {
    tabsTimeMenuItemActionPerformed(evt);
  }
  });
  tabsMenu.add(tabsTimeMenuItem);

  tabsRecursMenuItem.setText("Recurs");
  tabsRecursMenuItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_5,
    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
tabsRecursMenuItem.addActionListener(new java.awt.event.ActionListener() {
  public void actionPerformed(java.awt.event.ActionEvent evt) {
    tabsRecursMenuItemActionPerformed(evt);
  }
  });
  tabsMenu.add(tabsRecursMenuItem);

  mainMenuBar.add(tabsMenu);

  viewMenu.setText("View");

  viewByDueDateMenuItem.setText("By Due Date");
  viewMenu.add(viewByDueDateMenuItem);

  viewByPriorityMenuItem.setText("By Priority");
  viewMenu.add(viewByPriorityMenuItem);

  mainMenuBar.add(viewMenu);

  toolsMenu.setText("Tools");

  optionsMenuItem.setText("Options");
  optionsMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      optionsMenuItemActionPerformed(evt);
    }
  });
  toolsMenu.add(optionsMenuItem);

  mainMenuBar.add(toolsMenu);

  windowMenu.setText("Window");
  mainMenuBar.add(windowMenu);

  helpMenu.setText("Help");

  helpHistoryMenuItem.setText("Program History");
  helpHistoryMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      helpHistoryMenuItemActionPerformed(evt);
    }
  });
  helpMenu.add(helpHistoryMenuItem);

  helpUserGuideMenuItem.setText("User Guide");
  helpUserGuideMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      helpUserGuideMenuItemActionPerformed(evt);
    }
  });
  helpMenu.add(helpUserGuideMenuItem);
  helpMenu.add(helpWebSeparator);

  helpHomePageMenuItem.setText("Two Due Home Page");
  helpHomePageMenuItem.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      helpHomePageMenuItemActionPerformed(evt);
    }
  });
  helpMenu.add(helpHomePageMenuItem);
  helpMenu.add(helpMiscSeparator);

  helpReduceWindowSizeMenuItem.setText("Reduce Window Size");
  helpReduceWindowSizeMenuItem.setAccelerator (KeyStroke.getKeyStroke (KeyEvent.VK_W,
    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
helpReduceWindowSizeMenuItem.addActionListener(new java.awt.event.ActionListener() {
  public void actionPerformed(java.awt.event.ActionEvent evt) {
    helpReduceWindowSizeMenuItemActionPerformed(evt);
  }
  });
  helpMenu.add(helpReduceWindowSizeMenuItem);

  mainMenuBar.add(helpMenu);

  setJMenuBar(mainMenuBar);

  setSize(new java.awt.Dimension(675, 550));
  setLocationRelativeTo(null);
  }// </editor-fold>//GEN-END:initComponents

  private void listPurgeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listPurgeMenuItemActionPerformed
    td.purgeWindow.setVisible (true);
  }//GEN-LAST:event_listPurgeMenuItemActionPerformed

  private void listPublishMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listPublishMenuItemActionPerformed
    td.webWindow.setVisible(true);
  }//GEN-LAST:event_listPublishMenuItemActionPerformed

  private void itemDoneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemDoneButtonActionPerformed
    td.modIfChanged();
    td.displayItem();
  }//GEN-LAST:event_itemDoneButtonActionPerformed

  private void fileGetInfoMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileGetInfoMenuItemActionPerformed
    td.filePropertiesWindow.setVisible(true);
  }//GEN-LAST:event_fileGetInfoMenuItemActionPerformed

  private void findButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findButtonActionPerformed
    findItem();
  }//GEN-LAST:event_findButtonActionPerformed

  private void findTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_findTextKeyTyped
    if (! findText.getText().equals(lastTextFound)) {
      findButton.setText(FIND);
    }
  }//GEN-LAST:event_findTextKeyTyped

  private void findTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findTextActionPerformed
    findItem();
  }//GEN-LAST:event_findTextActionPerformed

    private void helpReduceWindowSizeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpReduceWindowSizeMenuItemActionPerformed
      setBounds (40, 40, 400, 540);
      pack();
    }//GEN-LAST:event_helpReduceWindowSizeMenuItemActionPerformed

  private void recordEditDateMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recordEditDateMenuItemActionPerformed
    td.editDueDate();
  }//GEN-LAST:event_recordEditDateMenuItemActionPerformed

  private void recordLaunchMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recordLaunchMenuItemActionPerformed
    td.modIfChanged();
    td.launchWebPage();
  }//GEN-LAST:event_recordLaunchMenuItemActionPerformed

  private void itemLaunchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemLaunchButtonActionPerformed
    td.modIfChanged();
    td.launchWebPage();
  }//GEN-LAST:event_itemLaunchButtonActionPerformed

  private void listSlackMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listSlackMenuItemActionPerformed
    td.slack();
  }//GEN-LAST:event_listSlackMenuItemActionPerformed

  private void fileSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileSaveButtonActionPerformed
    td.fileSave();
  }//GEN-LAST:event_fileSaveButtonActionPerformed

  private void recordPasteNewMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recordPasteNewMenuItemActionPerformed
    td.itemPasteNew();
  }//GEN-LAST:event_recordPasteNewMenuItemActionPerformed

  private void recordCopyMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recordCopyMenuItemActionPerformed
    td.itemCopy();
  }//GEN-LAST:event_recordCopyMenuItemActionPerformed

  private void helpHistoryMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpHistoryMenuItemActionPerformed
    td.openURL (programHistoryURL);
  }//GEN-LAST:event_helpHistoryMenuItemActionPerformed

  private void tabsRecursMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tabsRecursMenuItemActionPerformed
    td.activateRecursTab();
  }//GEN-LAST:event_tabsRecursMenuItemActionPerformed

  private void tabsTimeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tabsTimeMenuItemActionPerformed
    td.activateTimeTab();
  }//GEN-LAST:event_tabsTimeMenuItemActionPerformed

  private void tabsItemMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tabsItemMenuItemActionPerformed
    td.activateItemTab();
  }//GEN-LAST:event_tabsItemMenuItemActionPerformed

  private void tabsTreeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tabsTreeMenuItemActionPerformed
    td.activateTreeTab();
  }//GEN-LAST:event_tabsTreeMenuItemActionPerformed

  private void tabsListMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tabsListMenuItemActionPerformed
    td.activateListTab();
  }//GEN-LAST:event_tabsListMenuItemActionPerformed

  private void fileImportMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileImportMenuItemActionPerformed
    if (td.items.size() > 1) {
      // BackupPrefs.getShared().handleMajorEvent();
    }
    td.fileImport();
  }//GEN-LAST:event_fileImportMenuItemActionPerformed

  private void listReplaceCategoryMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listReplaceCategoryMenuItemActionPerformed
    replaceTags();
  }//GEN-LAST:event_listReplaceCategoryMenuItemActionPerformed

  private void listValidateURLsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listValidateURLsActionPerformed
    td.validateURLs();
  }//GEN-LAST:event_listValidateURLsActionPerformed

  private void recordFindAgainMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recordFindAgainMenuItemActionPerformed
    findAgain();
  }//GEN-LAST:event_recordFindAgainMenuItemActionPerformed

  private void recordRecurMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recordRecurMenuItemActionPerformed
    if (td.item != null) {
      if (tabs2.getSelectedIndex() != td.ITEM_TAB_INDEX) {
        td.activateItemTab();
      }
      td.item.recur();
      td.setChanged();
      td.modIfChanged();
      td.displayItem();
    }
  }//GEN-LAST:event_recordRecurMenuItemActionPerformed

  private void recordCloseMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recordCloseMenuItemActionPerformed
    if (td.item != null) {
      if (tabs2.getSelectedIndex() != td.ITEM_TAB_INDEX) {
        td.activateItemTab();
      }
      td.item.setStatus (td.verifyStatus (ActionStatus.PENDING_RECURS));
      td.setChanged();
      td.modIfChanged();
      td.displayItem();
    }
  }//GEN-LAST:event_recordCloseMenuItemActionPerformed

  private void recordFindMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recordFindMenuItemActionPerformed
    findItem();
  }//GEN-LAST:event_recordFindMenuItemActionPerformed

  private void listScheduleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listScheduleActionPerformed
    td.scheduleStartTimes();
  }//GEN-LAST:event_listScheduleActionPerformed

  private void listMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listMenuActionPerformed
    // Add your handling code here:
  }//GEN-LAST:event_listMenuActionPerformed

  private void recordDeleteMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recordDeleteMenuItemActionPerformed
    deleteItem();
  }//GEN-LAST:event_recordDeleteMenuItemActionPerformed

  private void recordPriorMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recordPriorMenuItemActionPerformed
    priorItem();
  }//GEN-LAST:event_recordPriorMenuItemActionPerformed

  private void recordNextMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recordNextMenuItemActionPerformed
    nextItem();
  }//GEN-LAST:event_recordNextMenuItemActionPerformed

  private void recordNewMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recordNewMenuItemActionPerformed
    newItem();
  }//GEN-LAST:event_recordNewMenuItemActionPerformed

  private void helpUserGuideMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpUserGuideMenuItemActionPerformed
    td.openURL (userGuideURL);
  }//GEN-LAST:event_helpUserGuideMenuItemActionPerformed

  private void helpHomePageMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpHomePageMenuItemActionPerformed
    td.openURL (HOME_PAGE);
  }//GEN-LAST:event_helpHomePageMenuItemActionPerformed

  private void itemDeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemDeleteButtonActionPerformed
    deleteItem();
  }//GEN-LAST:event_itemDeleteButtonActionPerformed

  private void fileNewMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileNewMenuItemActionPerformed
    td.fileNew();
  }//GEN-LAST:event_fileNewMenuItemActionPerformed

  private void itemNewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemNewButtonActionPerformed
    newItem();
  }//GEN-LAST:event_itemNewButtonActionPerformed

  private void fileSaveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileSaveMenuItemActionPerformed
    td.fileSave();
  }//GEN-LAST:event_fileSaveMenuItemActionPerformed

  private void fileSaveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileSaveAsMenuItemActionPerformed
    td.fileSaveAs();
  }//GEN-LAST:event_fileSaveAsMenuItemActionPerformed

  private void fileOpenMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileOpenMenuItemActionPerformed
    td.fileOpen();
  }//GEN-LAST:event_fileOpenMenuItemActionPerformed

  private void itemLastButtonAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemLastButtonAction
    lastItem();
  }//GEN-LAST:event_itemLastButtonAction

  private void itemNextButtonAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemNextButtonAction
    nextItem();
  }//GEN-LAST:event_itemNextButtonAction

  private void itemPriorButtonAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemPriorButtonAction
    priorItem();
  }//GEN-LAST:event_itemPriorButtonAction

  private void itemFirstButtonAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemFirstButtonAction
    firstItem();
  }//GEN-LAST:event_itemFirstButtonAction
  
  /** Exit the Application */
  private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
    handleQuit();
  }//GEN-LAST:event_exitForm

private void optionsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionsMenuItemActionPerformed
    handlePreferences();
}//GEN-LAST:event_optionsMenuItemActionPerformed

private void folderSyncMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_folderSyncMenuItemActionPerformed
  displayAuxiliaryWindow(td.folderSync);
}//GEN-LAST:event_folderSyncMenuItemActionPerformed

  private void listResetDueDatesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listResetDueDatesMenuItemActionPerformed
    td.resetDueDates();
  }//GEN-LAST:event_listResetDueDatesMenuItemActionPerformed

  private void fileBackupMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileBackupMenuItemActionPerformed
    td.promptForBackup();
  }//GEN-LAST:event_fileBackupMenuItemActionPerformed

  private void fileRevertMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileRevertMenuItemActionPerformed
    td.fileRevert();
  }//GEN-LAST:event_fileRevertMenuItemActionPerformed
  
  /**
   * @param args the command line arguments
   */
  public static void main(String args[]) {
    
    new TwoDue(args).setVisible(true);
  }
  
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel dividerLabel1;
  private javax.swing.JLabel dividerLabel2;
  private javax.swing.JMenu editMenu;
  private javax.swing.JMenuItem fileBackupMenuItem;
  private javax.swing.JMenuItem fileGetInfoMenuItem;
  private javax.swing.JMenuItem fileImportMenuItem;
  private javax.swing.JMenu fileMenu;
  private javax.swing.JLabel fileNameLabel;
  private javax.swing.JMenuItem fileNewMenuItem;
  private javax.swing.JMenuItem fileOpenMenuItem;
  private javax.swing.JMenuItem fileRevertMenuItem;
  private javax.swing.JMenuItem fileSaveAsMenuItem;
  private javax.swing.JButton fileSaveButton;
  private javax.swing.JMenuItem fileSaveMenuItem;
  private javax.swing.JButton findButton;
  private javax.swing.JTextField findText;
  private javax.swing.JMenuItem folderSyncMenuItem;
  private javax.swing.JPanel footerPanel;
  private javax.swing.JLabel fullSizeLabel;
  private javax.swing.JLabel fullSizeSpacerLabel;
  private javax.swing.JMenuItem helpHistoryMenuItem;
  private javax.swing.JMenuItem helpHomePageMenuItem;
  private javax.swing.JMenu helpMenu;
  private javax.swing.JSeparator helpMiscSeparator;
  private javax.swing.JMenuItem helpReduceWindowSizeMenuItem;
  private javax.swing.JMenuItem helpUserGuideMenuItem;
  private javax.swing.JSeparator helpWebSeparator;
  private javax.swing.JLabel indexLabel;
  private javax.swing.JButton itemDeleteButton;
  private javax.swing.JButton itemDoneButton;
  private javax.swing.JButton itemFirstButton;
  private javax.swing.JButton itemLastButton;
  private javax.swing.JButton itemLaunchButton;
  private javax.swing.JButton itemNewButton;
  private javax.swing.JButton itemNextButton;
  private javax.swing.JButton itemPriorButton;
  private javax.swing.JPopupMenu.Separator jSeparator1;
  private javax.swing.JPopupMenu.Separator jSeparator2;
  private javax.swing.JPopupMenu.Separator jSeparator3;
  private javax.swing.JPopupMenu.Separator jSeparator4;
  private javax.swing.JPopupMenu.Separator jSeparator5;
  private javax.swing.JPopupMenu.Separator jSeparator6;
  private javax.swing.JMenu listMenu;
  private javax.swing.JMenuItem listPublishMenuItem;
  private javax.swing.JMenuItem listPurgeMenuItem;
  private javax.swing.JMenuItem listReplaceCategoryMenuItem;
  private javax.swing.JMenuItem listResetDueDatesMenuItem;
  private javax.swing.JMenuItem listSchedule;
  private javax.swing.JMenuItem listSlackMenuItem;
  private javax.swing.JMenuItem listValidateURLs;
  private javax.swing.JMenuBar mainMenuBar;
  private javax.swing.JSplitPane mainSplitPane;
  private javax.swing.JToolBar navToolBar;
  private javax.swing.JLabel ofLabel;
  private javax.swing.JMenuItem optionsMenuItem;
  private javax.swing.JMenuItem recordCloseMenuItem;
  private javax.swing.JMenuItem recordCopyMenuItem;
  private javax.swing.JSeparator recordCopyPasteMenuSep;
  private javax.swing.JMenuItem recordDeleteMenuItem;
  private javax.swing.JMenuItem recordEditDateMenuItem;
  private javax.swing.JMenuItem recordFindAgainMenuItem;
  private javax.swing.JMenuItem recordFindMenuItem;
  private javax.swing.JMenuItem recordLaunchMenuItem;
  private javax.swing.JMenu recordMenu;
  private javax.swing.JMenuItem recordNewMenuItem;
  private javax.swing.JMenuItem recordNextMenuItem;
  private javax.swing.JMenuItem recordPasteNewMenuItem;
  private javax.swing.JMenuItem recordPriorMenuItem;
  private javax.swing.JMenuItem recordRecurMenuItem;
  private javax.swing.JLabel sizeLabel;
  private javax.swing.JTabbedPane tabs1;
  private javax.swing.JTabbedPane tabs2;
  private javax.swing.JMenuItem tabsItemMenuItem;
  private javax.swing.JMenuItem tabsListMenuItem;
  private javax.swing.JMenu tabsMenu;
  private javax.swing.JMenuItem tabsRecursMenuItem;
  private javax.swing.JMenuItem tabsTimeMenuItem;
  private javax.swing.JMenuItem tabsTreeMenuItem;
  private javax.swing.JLabel todayLabel;
  private javax.swing.JMenu toolsMenu;
  private javax.swing.JMenuItem viewByDueDateMenuItem;
  private javax.swing.JMenuItem viewByPriorityMenuItem;
  private javax.swing.JMenu viewMenu;
  private javax.swing.JMenu windowMenu;
  // End of variables declaration//GEN-END:variables
  
}
