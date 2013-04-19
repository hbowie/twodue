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

  import com.powersurgepub.twodue.disk.*;
  
public class PurgeWindow extends javax.swing.JFrame {
  
  private TwoDueDiskStore         diskStore;
  
  private TwoDueCommon            td;
  
  /**
   * Creates new form WebWindow
   */
  public PurgeWindow(TwoDueCommon td) {
    this.td = td;
    initComponents();
    purgeButton.setEnabled (false);
    archiveButton.setEnabled (false);
    openArchiveButton.setEnabled (false);
    webPublishButton.setEnabled (false);
    openCurrentButton.setEnabled (false);
    webViewButton.setEnabled (false);
  }
  
  public void filePrep (TwoDueDiskStore diskStore) {
    this.diskStore = diskStore;
    allButtonsSetEnabled();
  }
  
  public void initItems() {
    // td.categories.setComboBox (itemCategoryComboBox);
  }
  
  /**
    Prepare to switch tabs and show this one.
   */
  private void showThisTab () {
    td.switchTabs();
  }
  
  public void displayItem() {

  } // end method
  
  /**
   Modifies the item if anything on the screen changed. 
   
   @return True if any item fields were modified. 
   */
  public boolean modIfChanged () {
    
    return false;
    
  } // end method
  
  private void allButtonsSetEnabled () {
    purgeButton.setEnabled (true);
    if (diskStore.isAFolder()) {
      archiveButton.setEnabled (true);
      if (diskStore.isArchiveFileValidInput()) {
        openArchiveButton.setEnabled (true);
        webPublishButton.setEnabled (true);
      } else {
        openArchiveButton.setEnabled (false);
        webPublishButton.setEnabled (false);
      }
      openCurrentButton.setEnabled (false);
      webViewButton.setEnabled (false);
    } else {
      archiveButton.setEnabled (false);
      openArchiveButton.setEnabled (false);
      webPublishButton.setEnabled (false);
      openCurrentButton.setEnabled (false);
      webViewButton.setEnabled (false);
    }
  }
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    purgeButton = new javax.swing.JButton();
    archiveSeparator = new javax.swing.JSeparator();
    archiveButton = new javax.swing.JButton();
    webPublishButton = new javax.swing.JButton();
    webViewButton = new javax.swing.JButton();
    openArchiveButton = new javax.swing.JButton();
    openCurrentButton = new javax.swing.JButton();

    getContentPane().setLayout(new java.awt.GridBagLayout());

    purgeButton.setText("Purge Closed Items");
    purgeButton.setToolTipText("Delete closed and canceled items from list");
    purgeButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    purgeButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        purgeButtonActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(purgeButton, gridBagConstraints);

    archiveSeparator.setMinimumSize(new java.awt.Dimension(20, 1));
    archiveSeparator.setPreferredSize(new java.awt.Dimension(20, 1));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(archiveSeparator, gridBagConstraints);

    archiveButton.setText("Archive Closed Items");
    archiveButton.setToolTipText("Archive closed and canceled items");
    archiveButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    archiveButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        archiveButtonActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(archiveButton, gridBagConstraints);

    webPublishButton.setText("Publish to Web");
    webPublishButton.setToolTipText("");
    webPublishButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    webPublishButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        webPublishButtonActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(webPublishButton, gridBagConstraints);

    webViewButton.setText("View Web Archive");
    webViewButton.setToolTipText("");
    webViewButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    webViewButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        webViewButtonActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(webViewButton, gridBagConstraints);

    openArchiveButton.setText("Open Archive");
    openArchiveButton.setToolTipText("");
    openArchiveButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    openArchiveButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        openArchiveButtonActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(openArchiveButton, gridBagConstraints);

    openCurrentButton.setText("Open Current");
    openCurrentButton.setToolTipText("");
    openCurrentButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    openCurrentButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        openCurrentButtonActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(openCurrentButton, gridBagConstraints);

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void openCurrentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openCurrentButtonActionPerformed
    td.fileOpen(diskStore);
    allButtonsSetEnabled();
  }//GEN-LAST:event_openCurrentButtonActionPerformed

  private void openArchiveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openArchiveButtonActionPerformed
    if (diskStore.isAFolder()) {
      if (diskStore.isArchiveFileValidInput()) {
        td.archiveOpen(diskStore);
        openArchiveButton.setEnabled(false);
        openCurrentButton.setEnabled(true);
        archiveButton.setEnabled(false);
        purgeButton.setEnabled(false);
        webPublishButton.setEnabled(false);
        webViewButton.setEnabled(false);
      } else {
        td.trouble.report
            ("Archive File "
            + TwoDueFolder.ARCHIVE_FILE_NAME
            + " is not available", "Archive Problem");
      }
    } else {
      td.reportFolderTrouble();
    }
  }//GEN-LAST:event_openArchiveButtonActionPerformed

  private void webViewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_webViewButtonActionPerformed
    if (diskStore.isArchivePublishedPageAvailable()) {
      td.browseFile(diskStore.getArchivePublishedPage());
    } else {
      td.trouble.report
          ("Archive Web Page is not Available", "Archive Web Page Problem");
    }
  }//GEN-LAST:event_webViewButtonActionPerformed

  private void webPublishButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_webPublishButtonActionPerformed
    if (diskStore.isAFolder()) {
      if (diskStore.archiveTemplateIsAvailable()) {
        td.publishArchive();
      } else {
        td.trouble.report
            ("Archive Web Page Template "
            + TwoDueFolder.ARCHIVE_TEMPLATE_FILE_NAME
            + " could not be found", "Template Problem");
      }
    } else {
      td.reportFolderTrouble();
    }
    
    if (diskStore.isArchivePublishedPageAvailable()) {
      webViewButton.setEnabled(true);
    } else {
      webViewButton.setEnabled(false);
    }
  }//GEN-LAST:event_webPublishButtonActionPerformed

  private void archiveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_archiveButtonActionPerformed
    if (diskStore.isAFolder()) {
      td.purge(true);
      openArchiveButton.setEnabled(true);
    } else {
      td.reportFolderTrouble();
    }
    if (diskStore.isArchivePublishedPageAvailable()) {
      webViewButton.setEnabled(true);
    } else {
      webViewButton.setEnabled(false);
    }
  }//GEN-LAST:event_archiveButtonActionPerformed

  private void purgeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purgeButtonActionPerformed
    td.purge(false);
  }//GEN-LAST:event_purgeButtonActionPerformed
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton archiveButton;
  private javax.swing.JSeparator archiveSeparator;
  private javax.swing.JButton openArchiveButton;
  private javax.swing.JButton openCurrentButton;
  private javax.swing.JButton purgeButton;
  private javax.swing.JButton webPublishButton;
  private javax.swing.JButton webViewButton;
  // End of variables declaration//GEN-END:variables
  
}
