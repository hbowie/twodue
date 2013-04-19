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

  import com.powersurgepub.psdatalib.psdata.*;
  import com.powersurgepub.psdatalib.txbio.*;
  import com.powersurgepub.psutils.*;
  import com.powersurgepub.twodue.data.*;
  import com.powersurgepub.twodue.disk.*;
  import com.powersurgepub.xos2.*;
  import java.io.*;
  import java.net.*;
  import java.util.*;
  import javax.swing.*;

/**
 Allows the user to sync a to do list with the files/folders
 contained in a folder. 

 @author Herb Bowie
 */
public class FolderSync 
    extends javax.swing.JFrame
      implements WindowToManage {
  
  private static final String FOLDER_SYNC = "FolderSync";
  private static final String UNDERLINES  = "==========";
  private static final String FILE_SIZE   = "File Size: ";
  private static final String MOD_DATE    = "Mod Date:  ";
  private static final String NEW_LINE    = "\n";
  
  private TwoDueCommon      td          = null;
  private TwoDueDiskStore   diskStore   = null;
  private ToDoItems         items       = null;

  /** Creates new form FolderSync */
  public FolderSync(TwoDueCommon td) {
    this.td = td;
    initComponents();
  }
  
  /**
   Prepare for a new disk store. 
  
   @param diskStore The new disk store being opened. 
  */
  public void filePrep (TwoDueDiskStore diskStore) {
    this.diskStore = diskStore;
    setSyncFolder("");
    setAutoSync(false);
    setDeleteUnsynced(false);
    msgs.setText("");
    /*if (diskStore.isAFolder()) {
      setSyncFolder (diskStore.getFile());
    }
    else
    if (diskStore.isAFile()) {
      setSyncFolder (diskStore.getFile().getParentFile());
    } */
    
    resetDoneButton();
  }
  
  public void initItems(ToDoItems items) {
    this.items = items;
    // See if we have a sync folder
    String lastSyncFolderStr = diskStore.getSyncFolder();
    if (lastSyncFolderStr.length() > 0) {
      File lastSyncFolder = new File (lastSyncFolderStr);
      if (lastSyncFolder.exists()
          && (lastSyncFolder.canRead())) {
        setSyncFolder (lastSyncFolder);
        setAutoSync (diskStore.getAutoSync());
        setDeleteUnsynced (diskStore.getDeleteUnsynced());
      }
    }
    resetDoneButton();
  }
  
  private void setSyncFolder (File folder) {
    try {
      String folderString = folder.getCanonicalPath();
      setSyncFolder(folderString);
    } catch (java.io.IOException e) {
      setSyncFolder(folder.toString());
    }
    
    resetDoneButton();
  }
  
  private void setSyncFolder (String folderString) {
    syncFolderTextField.setText(folderString);
  }
  
  public String getSyncFolderAsString() {
    return syncFolderTextField.getText();
  }
  
  private void setAutoSync (boolean autoSync) {
    autoSyncCheckBox.setSelected(autoSync);
  }
  
  public boolean getAutoSync() {
    return autoSyncCheckBox.isSelected();
  }
  
  public String getAutoSyncAsString() {
    return String.valueOf(autoSyncCheckBox.isSelected());
  }
  
  private void setDeleteUnsynced (boolean deleteUnsynced) {
    deleteUnsyncedCheckBox.setSelected(deleteUnsynced);
  }
  
  public String getDeleteUnsyncedAsString() {
    return String.valueOf(deleteUnsyncedCheckBox.isSelected());
  }
  
  public boolean getDeleteUnsynced() {
    return deleteUnsyncedCheckBox.isSelected();
  }
  
  private void resetDoneButton() {
    doneButton.setText("Cancel");
  }
  
  /**
   Sync the contents of the two due list with the contents of the sync folder. 
  
   @return True if everything went OK. 
  */
  public boolean syncWithFolder () {
    
    boolean ok = true;
    
    File syncFolder = new File (syncFolderTextField.getText());
    
    // Make sure we've got the necessary info to work with
    if (syncFolder.exists()
        && syncFolder.isDirectory()
        && syncFolder.canRead()) {
      ok = true;
    } else {
      Trouble.getShared().report(
          this, 
          "Trouble reading from folder: " + syncFolder.toString(), 
          "Problem with Sync Folder");
      ok = false;
    }
    
    if (items == null) {
      Trouble.getShared().report(
          this, 
          "Items not available for " + diskStore.getFile().toString(), 
          "Problem with program logic");
      ok = false;
    }
    
    int synced = 0;
    int added = 0;
    int deleted = 0;
    
    if (ok) {
      
      // Now go through the items on the list and mark them all as unsynced
      ToDoItem next;
      int i = 0;
      
      boolean found = false;
      for (i = 0; i < items.size(); i++) {
        next = items.get(i);
        next.setSynced(false);
        next.setWebPage("");
        next.setCommonTitle();
      } // end of sorted items

      // Now match directory entries in the folder with items on the list
      int startingSize = items.size();
      DirectoryReader directoryReader = new DirectoryReader (syncFolder);
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
              && (! nextFile.getName().equals(TwoDueDiskDirectory.TWO_DUE_FOLDER))
              && (! nextFile.getName().equals(TwoDueFolder.DISK_VIEWS_NAME))) {
            // System.out.println(" ");
            // System.out.println("FolderSync   file name = " + nextFile.getName());
            FileName nextFileName = new FileName(nextFile);
            String fileNameBase;
            if (nextFile.isDirectory()) {
              fileNameBase = nextFile.getName();
              // System.out.println("FolderSync folder name = " + fileNameBase);
              int dotIndex = fileNameBase.lastIndexOf('.');
              if (dotIndex > 0
                  && ((dotIndex + 4) >= fileNameBase.length())) {
                fileNameBase = fileNameBase.substring(0, dotIndex);
              }
            } else {
              fileNameBase = nextFileName.getBase();
            }
            String commonFileName 
                = StringUtils.commonName(fileNameBase);
            // System.out.println("FolderSync common name = " + commonFileName);
            String nextFileEnglishName 
                = nextFileName.getFileOrFolderNameEnglish();
            URL nextUrl = nextFile.toURI().toURL();
            String nextTitle = "";
            String nextCommonTitle = "";
            boolean markdown = MetaMarkdownReader.isInterestedIn(nextFile);
            if (markdown) {
              MetaMarkdownReader mdReader 
                  = new MetaMarkdownReader
                    (nextFile, MetaMarkdownReader.MARKDOWN_TYPE);
              mdReader.openForInput();
              nextTitle = mdReader.getTitle();
              nextCommonTitle = StringUtils.commonName(nextTitle);
            }
            
            i = 0;
            found = false;
            while (i < items.size() 
                // && i < startingSize
                && (! found)) {
              next = items.get(i);
              found = (next.getCommonTitle().equals(commonFileName)
                  || next.getCommonTitle().equals(nextCommonTitle));
              if (found) {
                // System.out.println("FolderSync       match = " + next.getTitle());
                next.setSynced(true);
                String nextWebPage = next.getWebPage();
                if (nextWebPage.length() == 0 
                    || nextWebPage.endsWith(".html")
                    || markdown) {
                  if (! next.getWebPage().equals(nextUrl.toString())) {
                    next.setWebPage(nextUrl);
                    // System.out.println("FolderSync     new url = " + nextUrl.toString());
                  }
                  boolean changedDescription = setDescription(next, nextFile);
                  // Date lastDueDate = next.getDueDate();
                  Date lastModDate = new Date (nextFile.lastModified());
                  if (lastModDate.compareTo(next.getLastModDate()) != 0) {
                    next.setLastModDate(lastModDate);
                  }
                  if (nextFile.length() != next.getFileLength()) {
                    next.setFileLength(nextFile.length());
                  }
                  if (nextTitle.length() > 0
                      && (! next.getTitle().equals(nextTitle))) {
                    next.setTitle(nextTitle);
                  }
                  // if ((! next.hasDueDate())
                  //     || lastModDate.after(lastDueDate)) {
                  //   next.setDueDate(lastModDate);
                  // }
                }
                if (i < startingSize) {
                  synced++;
                }
              } else {
                i++;
              }
            } // end while looking for a matching to do entry
            if ((! found) 
                && (nextFileEnglishName.length() > 0)) {
              ToDoItem newItem = new ToDoItem();
              newItem.setTitle(nextFileEnglishName);
              if (nextTitle.length() > 0) {
                newItem.setTitle(nextTitle);
              }
              newItem.setSynced(true);
              newItem.setWebPage(nextUrl);
              setDescription(newItem, nextFile);
              Date lastModDate = new Date (nextFile.lastModified());
              newItem.setLastModDate(lastModDate);
              newItem.setFileLength(nextFile.length());
              i = items.add (newItem);
              added++;
              msgs.append("Added " + newItem.getTitle() + "\n");
              // System.out.println ("FolderSync       added = " + newItem.getTitle());
            }
          } // end if file exists, can be read, etc.
        } // end while more files in specified folder
      } catch (IOException ioe) {
        Trouble.getShared().report(this, 
            "Trouble reading folder: " + syncFolder.toString(), 
            "Folder access problems");
        ok = false;
      } // end if caught I/O Error
      directoryReader.close();
      
      msgs.append(String.valueOf(added) + " "
          + StringUtils.pluralize("item", added)
          + " added\n");
      
      msgs.append(String.valueOf(synced)  + " existing "
          + StringUtils.pluralize("item", synced)
          + " synced\n");
      
      
      // Now delete items that weren't synced
      
      /*
      int toDelete = JOptionPane.showConfirmDialog(this, 
          "Delete Unsynced Items?", 
          "Delete Confirmation", 
          JOptionPane.YES_NO_OPTION, 
          JOptionPane.QUESTION_MESSAGE);
      
      if (toDelete == JOptionPane.YES_OPTION) 
       */
      if (deleteUnsyncedCheckBox.isSelected()) {
        
        for (i = 0; i < startingSize; i++) {
          next = items.get(i);
          if (! next.wasSynced()) {
            // System.out.println("FolderSync item not synced");
            msgs.append("Deleted " + next.getTitle() + "\n");
            items.remove (next);
            deleted++;
          }
          next.setSynced(false);
        } // end of sorted items

        msgs.append(String.valueOf(deleted) + " "
            + StringUtils.pluralize("item", deleted)
            + " deleted\n");
      }
      
      msgs.append("Folder Sync Completed!\n");
      
      diskStore.setSyncFolder(syncFolder.toString());
      diskStore.setAutoSync(autoSyncCheckBox.isSelected());
      diskStore.setDeleteUnsynced(deleteUnsyncedCheckBox.isSelected());
      
      td.fileSave();
      doneButton.setText("OK");
    }
    
    return ok;
      
  }
  
  private boolean setDescription (ToDoItem item, File file) {

    boolean changed = false;
    String oldDescription = item.getDescription();
    int i = 0;
    StringBuilder newDescription = new StringBuilder();
    while (i < oldDescription.length()) {
      // Check each line in existing description
      int lineStart = i;
      // Scan for end of line
      while (i < oldDescription.length()
          && oldDescription.charAt(i) != GlobalConstants.CARRIAGE_RETURN
          && oldDescription.charAt(i) != GlobalConstants.LINE_FEED) {
        i++;
      }
      int lineEnd = i;
      if (i >= oldDescription.length()) {
        lineEnd--;
      }
      else
      if ((i + 1) >= oldDescription.length()) {
        // leave well enough alone
      } else {
        char nextChar = oldDescription.charAt(i + 1);
        if (nextChar != oldDescription.charAt(i)
            && (nextChar == GlobalConstants.CARRIAGE_RETURN
              || nextChar == GlobalConstants.LINE_FEED)) {
          lineEnd++;
          i++;
        } // end if line feed / carriage return combo
      } // end if another character available
      
      String nextLine = oldDescription.substring(lineStart, lineEnd + 1);
      if (nextLine.startsWith (FOLDER_SYNC)
          || nextLine.startsWith (UNDERLINES)
          || nextLine.startsWith (FILE_SIZE)
          || nextLine.startsWith (MOD_DATE)) {
        // Drop it
      } else {
        newDescription.append(nextLine);
      }
      i++;
    } // end of all lines in description field
    
    if (! item.getDescription().equals(newDescription.toString())) {
      item.setDescription(newDescription.toString());
      changed = true;
    }
    return changed;
  }
  
  public void hideMe() {
    WindowMenuManager.getShared().hide(this);
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        folderLabel = new javax.swing.JLabel();
        folderBrowseButton = new javax.swing.JButton();
        syncFolderTextField = new javax.swing.JTextField();
        syncButton = new javax.swing.JButton();
        msgsScrollPane = new javax.swing.JScrollPane();
        msgs = new javax.swing.JTextArea();
        doneButton = new javax.swing.JButton();
        deleteUnsyncedCheckBox = new javax.swing.JCheckBox();
        autoSyncCheckBox = new javax.swing.JCheckBox();

        setTitle("Folder Sync");
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                formComponentHidden(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        folderLabel.setText("Folder:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 4);
        getContentPane().add(folderLabel, gridBagConstraints);

        folderBrowseButton.setText("Browse...");
        folderBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                folderBrowseButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(folderBrowseButton, gridBagConstraints);

        syncFolderTextField.setColumns(60);
        syncFolderTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                syncFolderTextFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(syncFolderTextField, gridBagConstraints);

        syncButton.setText("Sync");
        syncButton.setMaximumSize(new java.awt.Dimension(140, 35));
        syncButton.setMinimumSize(new java.awt.Dimension(100, 29));
        syncButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                syncButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(syncButton, gridBagConstraints);

        msgs.setColumns(20);
        msgs.setRows(5);
        msgsScrollPane.setViewportView(msgs);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(msgsScrollPane, gridBagConstraints);

        doneButton.setText("Cancel");
        doneButton.setMinimumSize(new java.awt.Dimension(100, 29));
        doneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doneButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(doneButton, gridBagConstraints);

        deleteUnsyncedCheckBox.setText("Delete Unsynced Items?");
        deleteUnsyncedCheckBox.setToolTipText("Delete items from the list when they no longer have any similarly named files in the sync folder?");
        deleteUnsyncedCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteUnsyncedCheckBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(deleteUnsyncedCheckBox, gridBagConstraints);

        autoSyncCheckBox.setText("Auto Sync?");
        autoSyncCheckBox.setToolTipText("Automatically sync with designated folder in the future?");
        autoSyncCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoSyncCheckBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(autoSyncCheckBox, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void syncFolderTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_syncFolderTextFieldActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_syncFolderTextFieldActionPerformed

private void folderBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_folderBrowseButtonActionPerformed
  XFileChooser chooser = new XFileChooser();
  chooser.setDialogTitle("Specify Target Folder for Synchronization");
  chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
  File chosen = chooser.showOpenDialog(this);
  if (chosen != null) {
    setSyncFolder(chosen);
  } // end if user chose a folder
}//GEN-LAST:event_folderBrowseButtonActionPerformed

private void syncButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_syncButtonActionPerformed
  
  syncWithFolder();
}//GEN-LAST:event_syncButtonActionPerformed

private void formComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentHidden
  hideMe();
}//GEN-LAST:event_formComponentHidden

private void doneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doneButtonActionPerformed
  hideMe();
}//GEN-LAST:event_doneButtonActionPerformed

private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
  resetDoneButton();
}//GEN-LAST:event_formComponentShown

  private void deleteUnsyncedCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteUnsyncedCheckBoxActionPerformed
    td.setUnsavedChanges(true);
    if (diskStore != null) {
      diskStore.setDeleteUnsynced(deleteUnsyncedCheckBox.isSelected());
    }
  }//GEN-LAST:event_deleteUnsyncedCheckBoxActionPerformed

  private void autoSyncCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoSyncCheckBoxActionPerformed
    td.setUnsavedChanges(true);
    if (diskStore != null) {
      diskStore.setAutoSync(autoSyncCheckBox.isSelected());
    }
  }//GEN-LAST:event_autoSyncCheckBoxActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox autoSyncCheckBox;
    private javax.swing.JCheckBox deleteUnsyncedCheckBox;
    private javax.swing.JButton doneButton;
    private javax.swing.JButton folderBrowseButton;
    private javax.swing.JLabel folderLabel;
    private javax.swing.JTextArea msgs;
    private javax.swing.JScrollPane msgsScrollPane;
    private javax.swing.JButton syncButton;
    private javax.swing.JTextField syncFolderTextField;
    // End of variables declaration//GEN-END:variables
}
