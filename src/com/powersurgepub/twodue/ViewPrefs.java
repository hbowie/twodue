/*
 * ViewPrefs.java
 *
 * Created on November 21, 2007, 7:52 PM
 */

package com.powersurgepub.twodue;

  import com.powersurgepub.psdatalib.ui.*;
  import com.powersurgepub.psutils.*;
  import com.powersurgepub.twodue.data.*;
  import com.powersurgepub.twodue.disk.*;
  import java.awt.*;
  import javax.swing.*;

/**
 *
 * @author  hbowie
 */
public class ViewPrefs extends javax.swing.JPanel {
  
  public static final String ASCENDING  = "Ascending";
  public static final String DESCENDING = "Descending";
  
  public static final String[] SHOW_LIST 
      = { ItemSelector.SHOW_ALL_STR, 
          ItemSelector.SHOW_OPEN_STR, 
          ItemSelector.SHOW_MONTH_STR, 
          ItemSelector.SHOW_WEEK_STR, 
          ItemSelector.SHOW_TODAY_STR,
          ItemSelector.SHOW_DUE_IN_NEXT_STR,
          };
          
  private   String[] sortFieldNames 
       = {ToDoItem.COLUMN_DISPLAY [ToDoItem.STATUS], 
          ToDoItem.COLUMN_DISPLAY [ToDoItem.DUE_DATE], 
          ToDoItem.COLUMN_DISPLAY [ToDoItem.PRIORITY], 
          ToDoItem.COLUMN_DISPLAY [ToDoItem.SEQUENCE],
          ToDoItem.COLUMN_DISPLAY [ToDoItem.ASSIGNED_TO], 
          ToDoItem.COLUMN_DISPLAY [ToDoItem.TITLE],
          ToDoItem.COLUMN_DISPLAY [ToDoItem.TAGS],
          ItemType.COLUMN_DISPLAY_NAME,
          ToDoItem.COLUMN_DISPLAY [ToDoItem.FILE_LENGTH],
          ToDoItem.COLUMN_DISPLAY [ToDoItem.LAST_MOD_DATE]
          // , ToDoItem.COLUMN_DISPLAY [ToDoItem.ID]}
  };
          
  private   boolean[] sortFieldTaken 
      = {false, false, false, false, false, false, false, false, false, false};
      
  public static final int STATUS_INDEX      = 0;
  public static final int DUE_DATE_INDEX    = 1;
  public static final int PRIORITY_INDEX    = 2;
  public static final int SEQUENCE_INDEX    = 3;
  public static final int ASSIGNED_TO_INDEX = 4;
  public static final int TITLE_INDEX       = 5;
  public static final int CATEGORY_INDEX    = 6;
  public static final int TYPE_INDEX        = 7;
  public static final int FILE_LENGTH_INDEX = 8;
  public static final int LAST_MOD_DATE_INDEX = 9;
  // public static final int ID_INDEX          = 6;
  
  private ViewList    views;
  
  private String  sort5Field = ToDoItem.COLUMN_DISPLAY [ToDoItem.ASSIGNED_TO];
  private String  sort5Seq   = ASCENDING;
  
  private boolean cascade = true;
  
  private BinaryPanel sort1Seq = new BinaryPanel ("", ASCENDING, DESCENDING, true);
  private BinaryPanel sort2Seq = new BinaryPanel ("", ASCENDING, DESCENDING, true);
  private BinaryPanel sort3Seq = new BinaryPanel ("", ASCENDING, DESCENDING, true);
  private BinaryPanel sort4Seq = new BinaryPanel ("", ASCENDING, DESCENDING, true);
  
  private TwoDueCommon       td;
  private PrefsWindow        prefsWindow;
  
  /** Creates new form ViewPrefs */
  public ViewPrefs(TwoDueCommon td, PrefsWindow prefsWindow) {
    this.td = td;
    views = td.views;
    this.prefsWindow = prefsWindow;
    initComponents();
    views.setComboBox (viewNameComboBox);
    
    dueInDaysTextField.setText("");
    
    for (int i = 0; i < ItemType.size(); i++) {
      JCheckBox checkBox = new JCheckBox (ItemType.getName(i));
      checkTypesPanel.add (checkBox);
    }
    
    int row = 4;
    GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridwidth = 5;
    gridBagConstraints.gridy = row;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(sort1Seq, gridBagConstraints);
    
    row++;
    gridBagConstraints.gridy = row;
    add(sort2Seq, gridBagConstraints);
    
    row++;
    gridBagConstraints.gridy = row;
    add(sort3Seq, gridBagConstraints);
    
    row++;
    gridBagConstraints.gridy = row;
    add(sort4Seq, gridBagConstraints);
    
    configureSortLists();
  }
  
  public void setMenu (JMenu viewMenu) {
    views.setMenu (viewMenu);
  }
  
  /**
    Prepares the tab for processing of newly opened file.
   
    @param diskStore Disk Store object for the file.
   */
  public void filePrep (TwoDueDiskStore diskStore) {

  }
  
  public void setOptions (ItemComparator comp) {
    setOptions (comp.getSelectString(),
        comp.getSortField (0), comp.getSortSeq (0),
        comp.getSortField (1), comp.getSortSeq (1),
        comp.getSortField (2), comp.getSortSeq (2),
        comp.getSortField (3), comp.getSortSeq (3),
        comp.getSortField (4), comp.getSortSeq (4),
        comp.getUndatedString());
    for (int i = 0; i < ItemType.size(); i++) {
      JCheckBox checkBox = (JCheckBox)checkTypesPanel.getComponent (i);
      checkBox.setSelected (comp.getSelector().isTypeSelected(i));
    }
  }
  
  /**
    Store latest data entered by user on the View tab.
   */
  public void updateViewFromForm () {
    String select = ((String)showList.getSelectedItem());
    if (select.equals (ItemSelector.SHOW_DUE_IN_NEXT_STR)) {
      String days = dueInDaysTextField.getText();
      if (days != null
          && days.length() > 0) {
        select = select + " " + days;
      } // end if days not null
    } // end if user entered days
    td.views.getSelector().setSelectOption (select);
    td.views.getComparator().setSortFields 
        (select,
         (String)sort1List.getSelectedItem(), sort1Seq.getDecisionString(),
         (String)sort2List.getSelectedItem(), sort2Seq.getDecisionString(),
         (String)sort3List.getSelectedItem(), sort3Seq.getDecisionString(),
         (String)sort4List.getSelectedItem(), sort4Seq.getDecisionString(),
         sort5Field, sort5Seq, getUndatedString());
    for (int i = 0; i < ItemType.size(); i++) {
      JCheckBox checkBox = (JCheckBox)checkTypesPanel.getComponent (i);
      td.views.getSelector().setSelectType (i, checkBox.isSelected());
    }
  }
  
  private void setOptions (String select, 
      String sort1, String seq1, 
      String sort2, String seq2,
      String sort3, String seq3, 
      String sort4, String seq4, 
      String sort5, String seq5,
      String undatedString) {
    
    cascade = false;
    
    dueInDaysTextField.setText("");
    if (select.startsWith (ItemSelector.SHOW_DUE_IN_NEXT_STR)) {
      showList.setSelectedItem (ItemSelector.SHOW_DUE_IN_NEXT_STR);
      StringScanner opt = new StringScanner (select);
      int days = opt.extractInteger(9999);
      dueInDaysTextField.setText (String.valueOf (days));
    } else {
      showList.setSelectedItem (select);
    }
    
    configureSort1List();
    sort1List.setSelectedItem (sort1);
    sort1Seq.setDecision (seq1);
    configureSort2List();
    sort2List.setSelectedItem (sort2);
    sort2Seq.setDecision (seq2);
    configureSort3List();
    sort3List.setSelectedItem (sort3);
    sort3Seq.setDecision (seq3);
    configureSort4List();
    sort4List.setSelectedItem (sort4);
    sort4Seq.setDecision (seq4);
    sort5Field = sort5;
    sort5Seq = seq5;
    cascade = true;
    setUndated (undatedString);
  }
  
  public void setUndated (String undatedString) {
    if (undatedString.equalsIgnoreCase (ItemComparator.HIGH)) {
      sortUndatedCheckBox.setSelected (true);
    }
    else
    if (undatedString.equalsIgnoreCase (ItemComparator.LOW)) {
      sortUndatedCheckBox.setSelected (false);
    }
  }
  
  public String getUndatedString () {
    if (sortUndatedCheckBox.isSelected()) {
      return ItemComparator.HIGH;
    } else {
      return ItemComparator.LOW;
    }
  }
  
  public void configureSortLists() {
    cascade = false;
    configureSort1List();
    configureSort2List();
    configureSort3List();
    configureSort4List();
    cascade = true;
  }
  
  private void configureSort1List () {

    String showOption = (String)showList.getSelectedItem();
    for (int i = 0; i < sortFieldTaken.length; i++) {
      sortFieldTaken [i] = false;
    }
    if (! showOption.equals (ItemSelector.SHOW_ALL_STR)) {
      sortFieldTaken [STATUS_INDEX] = true;
    }
    configureSortList (sort1List);
  }
  
  private void configureSort2List () {
    takeSortField (sort1List);
    configureSortList (sort2List);
  }
  
  private void configureSort3List () {
    takeSortField (sort2List);
    configureSortList (sort3List);
  }
  
  private void configureSort4List () {
    takeSortField (sort3List);
    configureSortList (sort4List);
    sort5Field = "";
    for (int i = 0; i < sortFieldTaken.length; i++) {
      if (! sortFieldTaken [i]) {
        sort5Field = sortFieldNames [i];
      } // end if this sort field not already taken
    } // end for all sort fields
  }
  
  private void takeSortField (JComboBox sortList) {
    String sortField = (String)sortList.getSelectedItem();
    if (sortField == null) {
      sortField = "";
    }
    boolean found = false;
    int i = 0;
    while ((i < sortFieldTaken.length)
        && (! found)) {
      if (sortField.equals (sortFieldNames [i])) {
        sortFieldTaken [i] = true;
        found = true;
      } // end if we found the sort field we were looking for
      i++;
    } // end while searching through sort field names
  } // end method
  
  private void configureSortList (JComboBox sortList) {
    String selected = (String)sortList.getSelectedItem();
    if (selected == null) {
      selected = "";
    }
    sortList.removeAllItems();
    boolean selectedSet = false;
    for (int i = 0; i < sortFieldTaken.length; i++) {
      if (! sortFieldTaken [i]) {
        sortList.addItem (sortFieldNames [i]);
        if (selected.equals (sortFieldNames [i])) {
          selectedSet = true;
        } // end if this item was previously selected
      } // end if this sort field not already taken
    } // end for all sort fields
    if (sortList.getItemCount() > 0) {
      if (selectedSet) {
        sortList.setSelectedItem (selected);
      } else {
        sortList.setSelectedIndex (0);
      }
    }
  } // end method
  
  /**
   Displays the item at the curent index.
   */
  public void displayItem() {
    
  }
  
  /**
   Modifies the td.item if anything on the screen changed. 
   
   @return True if any of the data changed on this tab. 
   */
  public boolean modIfChanged () {
    return false;
  }
  
  private void processDueInDays () {
    String days = dueInDaysTextField.getText();
    if (days != null
        && days.length() > 0) {
      try {
        int d = Integer.parseInt (days);
        showList.setSelectedItem (ItemSelector.SHOW_DUE_IN_NEXT_STR);
      } catch (NumberFormatException e) {
        dueInDaysTextField.setText ("");
      }
    } // end if user entered days
  }
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    showLabel = new javax.swing.JLabel();
    showList = new javax.swing.JComboBox();
    sort1Label = new javax.swing.JLabel();
    sort1List = new javax.swing.JComboBox();
    sort2Label = new javax.swing.JLabel();
    sort2List = new javax.swing.JComboBox();
    sort3Label = new javax.swing.JLabel();
    sort3List = new javax.swing.JComboBox();
    sort4Label = new javax.swing.JLabel();
    sort4List = new javax.swing.JComboBox();
    sortUndatedCheckBox = new javax.swing.JCheckBox();
    sortUndatedLabel = new javax.swing.JLabel();
    viewNameLabel = new javax.swing.JLabel();
    viewNameComboBox = new javax.swing.JComboBox();
    dueInDaysLabel = new javax.swing.JLabel();
    viewNameAddButton = new javax.swing.JButton();
    viewNameDeleteButton = new javax.swing.JButton();
    dueInDaysTextField = new javax.swing.JTextField();
    viewNameFiller = new javax.swing.JLabel();
    filler = new javax.swing.JLabel();
    checkTypesPanel = new javax.swing.JPanel();

    setLayout(new java.awt.GridBagLayout());

    showLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    showLabel.setText("Show: ");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(showLabel, gridBagConstraints);

    showList.setModel(new javax.swing.DefaultComboBoxModel(SHOW_LIST));
    showList.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        showListActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 0.1;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(showList, gridBagConstraints);

    sort1Label.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    sort1Label.setText("Sort Field 1: ");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(sort1Label, gridBagConstraints);

    sort1List.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        sort1ListActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 0.1;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(sort1List, gridBagConstraints);

    sort2Label.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    sort2Label.setText("Sort Field 2: ");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(sort2Label, gridBagConstraints);

    sort2List.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        sort2ListActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 0.1;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(sort2List, gridBagConstraints);

    sort3Label.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    sort3Label.setText("Sort Field 3: ");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(sort3Label, gridBagConstraints);

    sort3List.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        sort3ListActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 0.1;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(sort3List, gridBagConstraints);

    sort4Label.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    sort4Label.setText("Sort Field 4: ");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 7;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(sort4Label, gridBagConstraints);

    sort4List.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        sort4ListActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 7;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 0.1;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(sort4List, gridBagConstraints);

    sortUndatedCheckBox.setText("Sort Undated Items After?");
    sortUndatedCheckBox.setToolTipText("Check this box to cause your undated items to sort after dated ones, as if they had later dates.");
    sortUndatedCheckBox.setActionCommand("sortundated");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 0.1;
    gridBagConstraints.insets = new java.awt.Insets(2, 6, 2, 2);
    add(sortUndatedCheckBox, gridBagConstraints);

    sortUndatedLabel.setText("Undated Position:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(sortUndatedLabel, gridBagConstraints);

    viewNameLabel.setText("View Name:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(viewNameLabel, gridBagConstraints);

    viewNameComboBox.setEditable(true);
    viewNameComboBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        viewNameComboBoxActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(viewNameComboBox, gridBagConstraints);

    dueInDaysLabel.setText("Days");
    dueInDaysLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 0.1;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(dueInDaysLabel, gridBagConstraints);

    viewNameAddButton.setText("+");
    viewNameAddButton.setToolTipText("Add a new View");
    viewNameAddButton.setActionCommand("addView");
    viewNameAddButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    viewNameAddButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
    viewNameAddButton.setMaximumSize(new java.awt.Dimension(40, 29));
    viewNameAddButton.setMinimumSize(new java.awt.Dimension(40, 29));
    viewNameAddButton.setPreferredSize(new java.awt.Dimension(40, 29));
    viewNameAddButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        viewNameAddButtonActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 0.1;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(viewNameAddButton, gridBagConstraints);

    viewNameDeleteButton.setText("-");
    viewNameDeleteButton.setToolTipText("Delete this View");
    viewNameDeleteButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    viewNameDeleteButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
    viewNameDeleteButton.setMaximumSize(new java.awt.Dimension(40, 29));
    viewNameDeleteButton.setMinimumSize(new java.awt.Dimension(40, 29));
    viewNameDeleteButton.setPreferredSize(new java.awt.Dimension(40, 29));
    viewNameDeleteButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        viewNameDeleteButtonActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 4;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 0.1;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(viewNameDeleteButton, gridBagConstraints);

    dueInDaysTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
    dueInDaysTextField.setPreferredSize(new java.awt.Dimension(30, 22));
    dueInDaysTextField.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        dueInDaysTextFieldActionPerformed(evt);
      }
    });
    dueInDaysTextField.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        dueInDaysTextFieldFocusLost(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 0.1;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(dueInDaysTextField, gridBagConstraints);

    viewNameFiller.setText("   ");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 5;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 0.2;
    add(viewNameFiller, gridBagConstraints);

    filler.setText(" ");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 13;
    gridBagConstraints.gridwidth = 6;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(filler, gridBagConstraints);

    checkTypesPanel.setLayout(new java.awt.GridLayout(0, 4, 4, 4));

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridwidth = 5;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(checkTypesPanel, gridBagConstraints);

  }// </editor-fold>//GEN-END:initComponents

  private void dueInDaysTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_dueInDaysTextFieldFocusLost
    processDueInDays();
  }//GEN-LAST:event_dueInDaysTextFieldFocusLost

  private void dueInDaysTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dueInDaysTextFieldActionPerformed
    processDueInDays();
  }//GEN-LAST:event_dueInDaysTextFieldActionPerformed

  private void viewNameDeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewNameDeleteButtonActionPerformed
    int userOption = JOptionPane.showConfirmDialog(
        this,
        "Really delete this view?",
        "View Delete Confirmation",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE);
    if (userOption == JOptionPane.YES_OPTION) {
      td.modifyView(-1, TwoDueCommon.VIEW_TRIGGER_VIEW_REMOVE);
    } // end if user confirmed delete
  }//GEN-LAST:event_viewNameDeleteButtonActionPerformed

  private void viewNameAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewNameAddButtonActionPerformed
    td.modifyView(-1, TwoDueCommon.VIEW_TRIGGER_VIEW_NEW);
  }//GEN-LAST:event_viewNameAddButtonActionPerformed

  private void viewNameComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewNameComboBoxActionPerformed
    int index = viewNameComboBox.getSelectedIndex();
    String viewName = td.views.getViewName(index);
    String guiViewName = (String)(viewNameComboBox.getEditor().getItem());
    
    if (guiViewName != null) {
      if (index < 0) {
        views.setViewName(views.getViewIndex(), guiViewName);
        td.modifyView(index,
            TwoDueCommon.VIEW_TRIGGER_SELECT_VIEW_FROM_COMBO_BOX);
      } // end if user has typed in a new name
      else {
        td.modifyView(index,
            TwoDueCommon.VIEW_TRIGGER_SELECT_VIEW_FROM_COMBO_BOX);
      }
    } // end if gui assigned to not null
  }//GEN-LAST:event_viewNameComboBoxActionPerformed

  private void sort4ListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sort4ListActionPerformed
    if (sort4List.getSelectedIndex() >= 0 && cascade) {
      // nothing yet
    }
  }//GEN-LAST:event_sort4ListActionPerformed

  private void sort3ListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sort3ListActionPerformed
    if (sort3List.getSelectedIndex() >= 0 && cascade) {
      configureSortLists();
    }
  }//GEN-LAST:event_sort3ListActionPerformed

  private void sort2ListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sort2ListActionPerformed
    if (sort2List.getSelectedIndex() >= 0 && cascade) {
      configureSortLists();
    }
  }//GEN-LAST:event_sort2ListActionPerformed

  private void sort1ListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sort1ListActionPerformed
    if (sort1List.getSelectedIndex() >= 0 && cascade) {
      configureSortLists();
    }
  }//GEN-LAST:event_sort1ListActionPerformed

  private void showListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showListActionPerformed
    if (showList.getSelectedIndex() >= 0 && cascade) {
      String select = (String)showList.getSelectedItem();
      if (select.equals(ItemSelector.SHOW_ALL_STR)) {
        dueInDaysTextField.setText("");
      } else
        if (select.equals(ItemSelector.SHOW_OPEN_STR)) {
        dueInDaysTextField.setText("");
        } else
          if (select.equals(ItemSelector.SHOW_MONTH_STR)) {
        dueInDaysTextField.setText("30");
          } else
            if (select.equals(ItemSelector.SHOW_WEEK_STR)) {
        dueInDaysTextField.setText("7");
            } else
              if (select.equals(ItemSelector.SHOW_TODAY_STR)) {
        dueInDaysTextField.setText("1");
              }
      configureSortLists();
    }
  }//GEN-LAST:event_showListActionPerformed
  
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JPanel checkTypesPanel;
  private javax.swing.JLabel dueInDaysLabel;
  private javax.swing.JTextField dueInDaysTextField;
  private javax.swing.JLabel filler;
  private javax.swing.JLabel showLabel;
  private javax.swing.JComboBox showList;
  private javax.swing.JLabel sort1Label;
  private javax.swing.JComboBox sort1List;
  private javax.swing.JLabel sort2Label;
  private javax.swing.JComboBox sort2List;
  private javax.swing.JLabel sort3Label;
  private javax.swing.JComboBox sort3List;
  private javax.swing.JLabel sort4Label;
  private javax.swing.JComboBox sort4List;
  private javax.swing.JCheckBox sortUndatedCheckBox;
  private javax.swing.JLabel sortUndatedLabel;
  private javax.swing.JButton viewNameAddButton;
  private javax.swing.JComboBox viewNameComboBox;
  private javax.swing.JButton viewNameDeleteButton;
  private javax.swing.JLabel viewNameFiller;
  private javax.swing.JLabel viewNameLabel;
  // End of variables declaration//GEN-END:variables
  
}
