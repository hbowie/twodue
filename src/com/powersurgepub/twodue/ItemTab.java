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

  import com.powersurgepub.psdatalib.psdata.widgets.*;
  import com.powersurgepub.psdatalib.elements.*;
  import com.powersurgepub.psdatalib.ui.*;
	import com.powersurgepub.psutils.*;
  import com.powersurgepub.twodue.data.*;
  import com.powersurgepub.twodue.disk.*;
  import com.powersurgepub.xos2.*;
  import java.awt.*;
  import java.io.*;
  import java.net.*;
  import javax.swing.*;
  import java.util.*;

/**
   A panel to display most information about a To Do item. 
       
 */
public class ItemTab 
    extends javax.swing.JPanel 
      implements TwoDueTab {
        
  private static final  int DUE_DATE_ROW = 3;
        
  private TwoDueCommon  td;
  private Date          holdDueDate = new Date();
  private int           holdStatusIndex = 0;
  private int           closedStatus = ActionStatus.CLOSED;
  
  private DatePanel     dueDatePanel;
  
  private TextSelector  tagsTextSelector;
  
  private boolean       changed = false;
  
  /** Creates new form ItemTab */
  public ItemTab(TwoDueCommon td) {
    this.td = td;
    initComponents();
    
    dueDatePanel = new DatePanel (td.frame);
    GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = DUE_DATE_ROW;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    add(dueDatePanel, gridBagConstraints);
    
    // Use special text selector for the tags
    tagsTextSelector = new TextSelector();
    tagsTextSelector.setEditable(true);
    tagsTextSelector.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        tagsActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(tagsTextSelector, gridBagConstraints);
    tagsTextSelector.setValueList(td.items.getTagsList());
    
    EditMenuItemMaker editMenuItemMaker 
        = new EditMenuItemMaker (itemDescriptionText);
    editMenuItemMaker.addCutCopyPaste (td.editMenu);
  }
  
  private void tagsActionPerformed (java.awt.event.ActionEvent evt) {
    
  }
  
  public void setDueDateIcons 
      (ImageIcon late, ImageIcon today, ImageIcon tomorrow, ImageIcon future) {
    dueDatePanel.setLateIcons (late, today, tomorrow, future);
  }
  
  /**
    Prepares the tab for processing of newly opened file.
   
    @param store Disk Store object for the file.
   */
  public void filePrep (TwoDueDiskStore store) {
    // No file information used on the Item Tab
  }
  
  public void initItems() {
    td.actors.setComboBox (itemAssignedToComboBox);
    tagsTextSelector.setValueList(td.items.getTagsList());
  }
  
  /**
    Prepare to switch tabs and show this one.
   */
  private void showThisTab () {
    td.switchTabs();
  }
  
  public void displayItem() {
    ToDoItem item = td.getItem();
    // itemIDText.setText (item.getID());
    tagsTextSelector.setText (item.getTagsAsString());
    itemTitleText.setText (item.getTitle());
    holdDueDate = td.getItem().getDueDate ();
    dueDatePanel.setItem (item);
    dueDatePanel.setDate (holdDueDate);
    itemPrioritySlider.setValue (item.getPriority());
    int status = item.getStatus();
    switch (status) {
      case ActionStatus.OPEN:
        holdStatusIndex = 0;
        break;
      case ActionStatus.IN_WORK:
        holdStatusIndex = 1;
        break;
      case ActionStatus.PENDING:
        holdStatusIndex = 2;
        break;
      case ActionStatus.CANCELED:
        holdStatusIndex = 3;
        break;
      case ActionStatus.PENDING_RECURS:
        holdStatusIndex = 4;
        break;
      case ActionStatus.CLOSED:
        holdStatusIndex = 4;
        break;
      default:
        holdStatusIndex = 0;
        break;
    }
    itemStatusComboBox.setSelectedIndex (holdStatusIndex);
    closedStatus = ActionStatus.CLOSED;
    
    itemTypeComboBox.setSelectedIndex (item.getType());
    
    if (td.actors.size() > 0) {
      itemAssignedToComboBox.setSelectedIndex 
          (td.actors.lookupValue (item.getAssignedTo()));
    }
    itemDescriptionText.setText (item.getDescriptionSansHTML());
    itemSequenceText.setText (item.getSequence());
    itemOutcomeText.setText (item.getOutcomeSansHTML());
    itemWebPageText.setText (item.getWebPage());
    if (item.getFileLength() > 0) {
      fileLengthLabel.setText("File Size:");
      fileLengthText.setText(item.getFileLengthAsString() + " bytes");
      lastModDateLabel.setText("Last Mod Date:");
      lastModDateText.setText(item.getLastModDate(ToDoItem.COMPLETE_FORMAT));
    } else {
      fileLengthLabel.setText(" ");
      fileLengthText.setText(" ");
      lastModDateLabel.setText(" ");
      lastModDateText.setText(" ");
    }
    changed = false;
  } // end method
  
  /**
   Modifies the item if anything on the screen changed. 
   
   @return True if any item fields were modified. 
   */
  public boolean modIfChanged () {

    ToDoItem item = td.getItem();
    if (item != null) {
      
      // Check category
      if (! item.equalsTags (tagsTextSelector.getText())) {
        item.setTags (tagsTextSelector.getText());
        changed = true;
      }

      // Check title
      if (! itemTitleText.getText().equals (item.getTitle())) {
        changed = true;
        item.setTitle (itemTitleText.getText());
      }

      // Check due date
      if (! dueDatePanel.getDate().equals (holdDueDate)) {
        changed = true;
        item.setDueDate (dueDatePanel.getDate());
      }
      
      // Check Item Type
      if (itemTypeComboBox.getSelectedIndex() != item.getType()) {
        changed = true;
        item.setType (itemTypeComboBox.getSelectedIndex());
      }

      // Check Priority
      if (itemPrioritySlider.getValue() != item.getPriority()) {
        changed = true;
        item.setPriority (itemPrioritySlider.getValue());
      }

      // Check status
      if (itemStatusComboBox.getSelectedIndex() != holdStatusIndex) {
        changed = true;
        switch (itemStatusComboBox.getSelectedIndex()) {
          case 0:
            item.setStatus (ActionStatus.OPEN);
            break;
          case 1:
            item.setStatus (ActionStatus.IN_WORK);
            break;
          case 2:
            item.setStatus (ActionStatus.PENDING);
            break;
          case 3:
            item.setStatus (ActionStatus.CANCELED);
            break;
          case 4:
            // Allow CLOSED status to be set later, after cloning this record,
            // if appropriate.
            item.setStatus (closedStatus);
            break;
          default:
            item.setStatus (ActionStatus.OPEN);
            break;
        }
      }

      // Check assigned to
      modAssignedToIfChanged();
      
      // Check description
      if (! itemDescriptionText.getText().equals 
          (item.getDescriptionSansHTML())) {
        changed = true;
        item.setDescription (itemDescriptionText.getText());
      }
      
      // Check sequence
      if (! itemSequenceText.getText().equals (item.getSequence())) {
        changed = true;
        item.setSequence (itemSequenceText.getText());
      }
      
      // Check outcome
      if (! itemOutcomeText.getText().equals 
          (item.getOutcomeSansHTML())) {
        changed = true;
        item.setOutcome (itemOutcomeText.getText());
      }
      
      // Check Web page
      if (! itemWebPageText.getText().equals (item.getWebPage())) {
        changed = true;
        cleanWebPage();
        item.setWebPage (itemWebPageText.getText());
      }
      
      itemTitleText.requestFocus();
      
    } // end if item not null
    
    return changed;
    
  } // end method
  
  public void modAssignedToIfChanged () {
    ToDoItem item = td.getItem();
    if (item != null) {
      String itemAssignedTo = item.getAssignedTo();
      String guiAssignedTo = (String)(itemAssignedToComboBox.getEditor().getItem());
      if (guiAssignedTo != null) {
        if (! itemAssignedTo.equals (guiAssignedTo)) {
          changed = true;
          item.setAssignedTo (guiAssignedTo);
          td.actors.registerValue (guiAssignedTo);
        } // end if gui element has changed
      } // end if gui assigned to not null
    } // end if item not null
  } // end method
  
  private void cleanWebPage () {
    String in = itemWebPageText.getText();
    if (in.length() > 7) {
      String out = StringUtils.cleanURLString (in);
      if (! in.equals(out)) {
        itemWebPageText.setText (out);
      }
    }
  }
  
  public void editDueDate() {
    dueDatePanel.editDate();
  }
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        itemTitleLabel = new javax.swing.JLabel();
        itemTitleText = new javax.swing.JTextField();
        itemStatusLabel = new javax.swing.JLabel();
        itemStatusComboBox = new javax.swing.JComboBox();
        itemDateLabel = new javax.swing.JLabel();
        itemPriorityLabel = new javax.swing.JLabel();
        itemPrioritySlider = new javax.swing.JSlider();
        itemAssignedToLabel = new javax.swing.JLabel();
        itemAssignedToComboBox = new javax.swing.JComboBox();
        itemDescriptionLabel = new javax.swing.JLabel();
        itemDescriptionScrollPane = new javax.swing.JScrollPane();
        itemDescriptionText = new javax.swing.JTextArea();
        itemSequenceLabel = new javax.swing.JLabel();
        itemSequenceText = new javax.swing.JTextField();
        itemOutcomeScrollPane = new javax.swing.JScrollPane();
        itemOutcomeText = new javax.swing.JTextArea();
        itemOutcomeLabel = new javax.swing.JLabel();
        itemWebPageButton = new javax.swing.JButton();
        itemWebPageText = new javax.swing.JTextField();
        itemCategoryLabel = new javax.swing.JLabel();
        itemTypeLabel = new javax.swing.JLabel();
        itemTypeComboBox = new javax.swing.JComboBox();
        urlLaunchButton = new javax.swing.JButton();
        lastModDateLabel = new javax.swing.JLabel();
        lastModDateText = new javax.swing.JLabel();
        fileLengthLabel = new javax.swing.JLabel();
        fileLengthText = new javax.swing.JLabel();

        setNextFocusableComponent(itemAssignedToLabel);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                formComponentHidden(evt);
            }
        });
        setLayout(new java.awt.GridBagLayout());

        itemTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        itemTitleLabel.setText("Title: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(itemTitleLabel, gridBagConstraints);

        itemTitleText.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        itemTitleText.setMinimumSize(new java.awt.Dimension(450, 18));
        itemTitleText.setPreferredSize(new java.awt.Dimension(600, 18));
        itemTitleText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemTitleTextActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(itemTitleText, gridBagConstraints);

        itemStatusLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        itemStatusLabel.setText("Status: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(itemStatusLabel, gridBagConstraints);

        itemStatusComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Open", "In Work", "Pending", "Canceled", "Closed" }));
        itemStatusComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemStatusComboBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(itemStatusComboBox, gridBagConstraints);

        itemDateLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        itemDateLabel.setText("Due Date: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(12, 4, 4, 4);
        add(itemDateLabel, gridBagConstraints);

        itemPriorityLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        itemPriorityLabel.setText("Priority: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(itemPriorityLabel, gridBagConstraints);

        itemPrioritySlider.setMajorTickSpacing(1);
        itemPrioritySlider.setMaximum(5);
        itemPrioritySlider.setMinimum(1);
        itemPrioritySlider.setMinorTickSpacing(1);
        itemPrioritySlider.setPaintLabels(true);
        itemPrioritySlider.setPaintTicks(true);
        itemPrioritySlider.setSnapToTicks(true);
        itemPrioritySlider.setValue(3);
        itemPrioritySlider.setMinimumSize(new java.awt.Dimension(72, 40));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(itemPrioritySlider, gridBagConstraints);

        itemAssignedToLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        itemAssignedToLabel.setText("Assigned to: ");
        itemAssignedToLabel.setMaximumSize(new java.awt.Dimension(120, 17));
        itemAssignedToLabel.setMinimumSize(new java.awt.Dimension(90, 14));
        itemAssignedToLabel.setPreferredSize(new java.awt.Dimension(100, 17));
        itemAssignedToLabel.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(itemAssignedToLabel, gridBagConstraints);

        itemAssignedToComboBox.setEditable(true);
        itemAssignedToComboBox.setMinimumSize(new java.awt.Dimension(200, 23));
        itemAssignedToComboBox.setPreferredSize(new java.awt.Dimension(300, 26));
        itemAssignedToComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemAssignedToComboBoxActionPerformed(evt);
            }
        });
        itemAssignedToComboBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                itemAssignedToComboBoxFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(itemAssignedToComboBox, gridBagConstraints);

        itemDescriptionLabel.setText("Description:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(itemDescriptionLabel, gridBagConstraints);

        itemDescriptionScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        itemDescriptionText.setColumns(10);
        itemDescriptionText.setLineWrap(true);
        itemDescriptionText.setRows(4);
        itemDescriptionText.setTabSize(4);
        itemDescriptionText.setWrapStyleWord(true);
        itemDescriptionScrollPane.setViewportView(itemDescriptionText);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(itemDescriptionScrollPane, gridBagConstraints);

        itemSequenceLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        itemSequenceLabel.setText("Sequence:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(itemSequenceLabel, gridBagConstraints);

        itemSequenceText.setText("seq");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(itemSequenceText, gridBagConstraints);

        itemOutcomeScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        itemOutcomeText.setColumns(10);
        itemOutcomeText.setLineWrap(true);
        itemOutcomeText.setRows(4);
        itemOutcomeText.setTabSize(4);
        itemOutcomeText.setWrapStyleWord(true);
        itemOutcomeScrollPane.setViewportView(itemOutcomeText);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(itemOutcomeScrollPane, gridBagConstraints);

        itemOutcomeLabel.setText("Outcome: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(itemOutcomeLabel, gridBagConstraints);

        itemWebPageButton.setText("URL");
        itemWebPageButton.setToolTipText("Click here to select a local file to be launched.");
        itemWebPageButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        itemWebPageButton.setMaximumSize(new java.awt.Dimension(120, 24));
        itemWebPageButton.setMinimumSize(new java.awt.Dimension(70, 24));
        itemWebPageButton.setPreferredSize(new java.awt.Dimension(70, 24));
        itemWebPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemWebPageButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(itemWebPageButton, gridBagConstraints);

        itemWebPageText.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        itemWebPageText.setMinimumSize(new java.awt.Dimension(450, 18));
        itemWebPageText.setPreferredSize(new java.awt.Dimension(600, 18));
        itemWebPageText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemWebPageTextActionPerformed(evt);
            }
        });
        itemWebPageText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                itemWebPageTextFocusLost(evt);
            }
        });
        itemWebPageText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                itemWebPageTextKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(itemWebPageText, gridBagConstraints);

        itemCategoryLabel.setText("Tags:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(itemCategoryLabel, gridBagConstraints);

        itemTypeLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        itemTypeLabel.setText("Type: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(itemTypeLabel, gridBagConstraints);

        ItemType.initComboBox (itemTypeComboBox);
        itemTypeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemTypeComboBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(itemTypeComboBox, gridBagConstraints);

        urlLaunchButton.setBackground(new java.awt.Color(51, 51, 51));
        urlLaunchButton.setFont(new java.awt.Font("Zapf Dingbats", 0, 13)); // NOI18N
        urlLaunchButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/powersurgepub/twodue/right-arrow.png"))); // NOI18N
        urlLaunchButton.setToolTipText("Open the URL in your Web browser");
        urlLaunchButton.setBorderPainted(false);
        urlLaunchButton.setContentAreaFilled(false);
        urlLaunchButton.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        urlLaunchButton.setIconTextGap(0);
        urlLaunchButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        urlLaunchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                urlLaunchButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(urlLaunchButton, gridBagConstraints);

        lastModDateLabel.setText("Last Mod Date:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(lastModDateLabel, gridBagConstraints);

        lastModDateText.setText("01/01/1970");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(lastModDateText, gridBagConstraints);

        fileLengthLabel.setText("File Size:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(fileLengthLabel, gridBagConstraints);

        fileLengthText.setText("1,073 bytes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(fileLengthText, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

  private void itemTypeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemTypeComboBoxActionPerformed
// TODO add your handling code here:
  }//GEN-LAST:event_itemTypeComboBoxActionPerformed

  private void itemWebPageTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_itemWebPageTextFocusLost
    cleanWebPage();
  }//GEN-LAST:event_itemWebPageTextFocusLost

  private void itemWebPageTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemWebPageTextKeyTyped
    //
  }//GEN-LAST:event_itemWebPageTextKeyTyped

  private void itemWebPageTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemWebPageTextActionPerformed
    cleanWebPage();
  }//GEN-LAST:event_itemWebPageTextActionPerformed

  private void formComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentHidden

  }//GEN-LAST:event_formComponentHidden

  private void itemWebPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemWebPageButtonActionPerformed
    // cleanWebPage();
    // td.openURL (itemWebPageText.getText().trim());
    XFileChooser chooser = new XFileChooser ();
    chooser.setFileSelectionMode(XFileChooser.FILES_AND_DIRECTORIES);
    String syncFolderStr = null;
    File syncFolder = null;
    File homeDir = Home.getShared().getUserHome();
    if (td.folderSync != null) {
      syncFolderStr = td.folderSync.getSyncFolderAsString();
    }
    if (syncFolderStr != null
        && syncFolderStr.length() > 0) {
      syncFolder = new File (syncFolderStr);
    }
    if (syncFolder != null
        && syncFolder.exists()
        && syncFolder.canRead()) {
      chooser.setCurrentDirectory (syncFolder);
    } 
    else 
    if (homeDir != null) {
      chooser.setCurrentDirectory (homeDir);
    }
    File result = chooser.showOpenDialog (td.frame);
    if (result != null) {
      try {
        String webPage = result.toURI().toURL().toString();
        itemWebPageText.setText (webPage);
      } catch (MalformedURLException e) {
        // do nothing
      }
    }
  }//GEN-LAST:event_itemWebPageButtonActionPerformed

  private void itemStatusComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemStatusComboBoxActionPerformed
    int index = itemStatusComboBox.getSelectedIndex();
    if (index >= 0) {
      dueDatePanel.setDone 
          (itemStatusComboBox.getSelectedIndex() >= 3);
      if (index == 4) {
        closedStatus = td.verifyStatus (ActionStatus.PENDING_RECURS);
      }
    }
  }//GEN-LAST:event_itemStatusComboBoxActionPerformed

  private void itemAssignedToComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemAssignedToComboBoxActionPerformed
    modAssignedToIfChanged();
  }//GEN-LAST:event_itemAssignedToComboBoxActionPerformed

  private void itemAssignedToComboBoxFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_itemAssignedToComboBoxFocusLost

  }//GEN-LAST:event_itemAssignedToComboBoxFocusLost

  private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
    showThisTab();
    itemTitleText.requestFocus();
  }//GEN-LAST:event_formComponentShown

private void urlLaunchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_urlLaunchButtonActionPerformed
    td.modIfChanged();
    td.launchWebPage();
}//GEN-LAST:event_urlLaunchButtonActionPerformed

  private void itemTitleTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemTitleTextActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_itemTitleTextActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel fileLengthLabel;
    private javax.swing.JLabel fileLengthText;
    private javax.swing.JComboBox itemAssignedToComboBox;
    private javax.swing.JLabel itemAssignedToLabel;
    private javax.swing.JLabel itemCategoryLabel;
    private javax.swing.JLabel itemDateLabel;
    private javax.swing.JLabel itemDescriptionLabel;
    private javax.swing.JScrollPane itemDescriptionScrollPane;
    private javax.swing.JTextArea itemDescriptionText;
    private javax.swing.JLabel itemOutcomeLabel;
    private javax.swing.JScrollPane itemOutcomeScrollPane;
    private javax.swing.JTextArea itemOutcomeText;
    private javax.swing.JLabel itemPriorityLabel;
    private javax.swing.JSlider itemPrioritySlider;
    private javax.swing.JLabel itemSequenceLabel;
    private javax.swing.JTextField itemSequenceText;
    private javax.swing.JComboBox itemStatusComboBox;
    private javax.swing.JLabel itemStatusLabel;
    private javax.swing.JLabel itemTitleLabel;
    private javax.swing.JTextField itemTitleText;
    private javax.swing.JComboBox itemTypeComboBox;
    private javax.swing.JLabel itemTypeLabel;
    private javax.swing.JButton itemWebPageButton;
    private javax.swing.JTextField itemWebPageText;
    private javax.swing.JLabel lastModDateLabel;
    private javax.swing.JLabel lastModDateText;
    private javax.swing.JButton urlLaunchButton;
    // End of variables declaration//GEN-END:variables
  
}
