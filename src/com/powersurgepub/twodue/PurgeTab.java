package com.powersurgepub.twodue;

  import com.powersurgepub.twodue.data.*;
  import com.powersurgepub.twodue.disk.*;

/**
   A panel to allow the user to purge or archive closed items. <p>
  
   This code is copyright (c) 2003 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
      2003/10/06 - Originally written.
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 2003/10/06 - Originally written.
 */
public class PurgeTab 
    extends javax.swing.JPanel 
      implements TwoDueTab {
        
  private TwoDueDiskStore         diskStore;
        
  private TwoDueCommon      td;
  
  /** Creates new form PurgeTab */
  public PurgeTab(TwoDueCommon td) {
    this.td = td;
    initComponents();
    purgeButton.setEnabled (false);
    archiveButton.setEnabled (false);
    openArchiveButton.setEnabled (false);
    webPublishButton.setEnabled (false);
    openCurrentButton.setEnabled (false);
    webViewButton.setEnabled (false);
  }
  
  /**
    Prepares the tab for processing of newly opened file.
   
    @param store Disk Store object for the file.
   */
  public void filePrep (TwoDueDiskStore diskStore) {
    this.diskStore = diskStore;
    // diskStore.display ("PurgeTab filePrep");
    allButtonsSetEnabled();
  }
  
  /**
    Prepare to switch tabs and show this one.
   */
  private void showThisTab () {
    td.switchTabs();
  }
  
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
  private void initComponents() {//GEN-BEGIN:initComponents
    java.awt.GridBagConstraints gridBagConstraints;

    purgeButton = new javax.swing.JButton();
    archiveSeparator = new javax.swing.JSeparator();
    archiveButton = new javax.swing.JButton();
    webPublishButton = new javax.swing.JButton();
    webViewButton = new javax.swing.JButton();
    openArchiveButton = new javax.swing.JButton();
    openCurrentButton = new javax.swing.JButton();

    setLayout(new java.awt.GridBagLayout());

    addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentShown(java.awt.event.ComponentEvent evt) {
        formComponentShown(evt);
      }
    });

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
    add(purgeButton, gridBagConstraints);

    archiveSeparator.setMinimumSize(new java.awt.Dimension(20, 1));
    archiveSeparator.setPreferredSize(new java.awt.Dimension(20, 1));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(archiveSeparator, gridBagConstraints);

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
    add(archiveButton, gridBagConstraints);

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
    add(webPublishButton, gridBagConstraints);

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
    add(webViewButton, gridBagConstraints);

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
    add(openArchiveButton, gridBagConstraints);

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
    add(openCurrentButton, gridBagConstraints);

  }//GEN-END:initComponents

  private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
    showThisTab();
    archiveButton.requestFocus();
  }//GEN-LAST:event_formComponentShown

  private void openCurrentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openCurrentButtonActionPerformed
    td.fileOpen (diskStore);
    allButtonsSetEnabled();
  }//GEN-LAST:event_openCurrentButtonActionPerformed

  private void openArchiveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openArchiveButtonActionPerformed
    if (diskStore.isAFolder()) {
      if (diskStore.isArchiveFileValidInput()) {
        td.archiveOpen (diskStore);
        openArchiveButton.setEnabled (false);
        openCurrentButton.setEnabled (true);
        archiveButton.setEnabled (false);
        purgeButton.setEnabled (false);
        webPublishButton.setEnabled (false);
        webViewButton.setEnabled (false);
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
      td.browseFile (diskStore.getArchivePublishedPage());
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
      webViewButton.setEnabled (true);
    } else {
      webViewButton.setEnabled (false);
    }
  }//GEN-LAST:event_webPublishButtonActionPerformed

  private void archiveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_archiveButtonActionPerformed
    if (diskStore.isAFolder()) {
      td.purge (true);
      openArchiveButton.setEnabled (true);
    } else {
      td.reportFolderTrouble();
    }
    if (diskStore.isArchivePublishedPageAvailable()) {
      webViewButton.setEnabled (true);
    } else {
      webViewButton.setEnabled (false);
    }
  }//GEN-LAST:event_archiveButtonActionPerformed

  private void purgeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purgeButtonActionPerformed
    td.purge(false);
  }//GEN-LAST:event_purgeButtonActionPerformed

  public void displayItem() {
  }  
  
  public boolean modIfChanged() {
    return false;
  }  
  
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
