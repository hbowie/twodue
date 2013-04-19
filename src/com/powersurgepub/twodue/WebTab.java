/*
 * Copyright 2004 - 2013 Herb Bowie
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
  import com.powersurgepub.psdatalib.template.*;
  import com.powersurgepub.psutils.*;
  import com.powersurgepub.twodue.data.*;
  import com.powersurgepub.twodue.disk.*;
  import com.powersurgepub.xos2.*;
  import java.awt.*;
  import java.awt.event.*;
  import java.io.*;
  import javax.swing.*;

/**
   The Web Tab for Two Due, a GUI To Do list. This tab allows
   the user to publish his list to the Web.<p>
  
   This code is copyright (c) 2003-2004 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
      2004/12/26 - Originally written.
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 2004/12/26 - Originally written. 
 */
public class WebTab 
    extends javax.swing.JPanel 
        implements TwoDueTab {
  
  private TwoDueCommon      td;
  
  private File              templatesFolder;
  private File              templateFile;
  private File              webFile;
  
  /** Creates new form WebTab */
  public WebTab (TwoDueCommon td) {
    this.td = td;
    initComponents();
    templatesFolder = new File (td.appFolder, "templates");
    if (templatesFolder != null
        && templatesFolder.exists()
        && templatesFolder.isDirectory()
        && templatesFolder.canRead()) {
      File[] templates = templatesFolder.listFiles();
      if (templates != null) {
        // Bubble sort the array to get it into alphabetical order, ignoring case
        boolean swapped = true;
        while (swapped) {
          swapped = false;
          int i = 0;
          int j = 1;
          while (j < templates.length) {
            String lower = templates[i].getName();
            String higher = templates[j].getName();
            if (lower.compareToIgnoreCase(higher) > 0) {
              File hold = templates[i];
              templates[i] = templates[j];
              templates[j] = hold;
              swapped = true;
            } // end if we need to swap the two entries
            i++;
            j++;
          } // end one pass through the array of templates
        } // end while still swapping entries
        
        // Now load templates into drop-down menu
        int i = 0;
        while (i < templates.length) {
          if (templates[i].getName().length() > 0
              && templates[i].getName().charAt(0) != '.'
              && templates[i].exists()
              && templates[i].canRead()
              && templates[i].isFile()) {
            webTemplateComboBox.addItem (templates[i].getName());
          } // end if folder entry looks like a usable template
          i++;
        } // end while loading combo box with templates
      } // end if the templates folder had any contentes
    } // end if we found a valid templates folder
  } // end constructor
  
  /**
    Prepares the tab for processing of newly opened file.
   
    @param diskStore Disk Store object for the file.
   */
  public void filePrep (TwoDueDiskStore diskStore) {

    setTemplate (diskStore.getTemplate());
    setPublishWhen (diskStore.getPublishWhen());
    webTemplateComboBox.setSelectedIndex (0);
  }
  
  /**
   Sets the template file to be used.
   
   @parm template Template file to be used for Web publishing.
   */
  public void setTemplate (File template) {
    this.templateFile = template;
    if (template == null) {
      webTemplateText.setText("");
    } else {
      webTemplateText.setText(template.getAbsolutePath());
    }
    webViewButton.setEnabled (false);
  }
  
  /**
   Gets the template file last used.
   
   @return Template file last used for Web publishing.
   */
  public File getTemplate () {
    return templateFile;
  }
  
  /**
   Sets the publishing frequency.
   
   @param publishWhen Publishing frequency.
   */
  public void setPublishWhen (int publishWhen) {
    webPublishList.setSelectedIndex (publishWhen);
    td.diskStore.setPublishWhen (publishWhen);
  }
  
  /**
   Sets the Web page created from the template.
   
   @param webFile File created by the template.
   */
  public void setPublishedPage (File webFile) {
    this.webFile = webFile;
    if (webFile != null) {
      webViewButton.setEnabled (true);
    }
  }
  
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
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  private void initComponents() {//GEN-BEGIN:initComponents
    java.awt.GridBagConstraints gridBagConstraints;

    webGenerateTemplateLabel = new javax.swing.JLabel();
    webTemplateComboBox = new javax.swing.JComboBox();
    webTemplateButton = new javax.swing.JButton();
    webTemplateText = new javax.swing.JTextField();
    webPublishButton = new javax.swing.JButton();
    webPublishList = new javax.swing.JComboBox();
    webViewButton = new javax.swing.JButton();
    filler = new javax.swing.JLabel();

    setLayout(new java.awt.GridBagLayout());

    webGenerateTemplateLabel.setText("Generate Template");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(webGenerateTemplateLabel, gridBagConstraints);

    webTemplateComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-- none selected --" }));
    webTemplateComboBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        webTemplateComboBoxActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 0.9;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(webTemplateComboBox, gridBagConstraints);

    webTemplateButton.setText("Web Template");
    webTemplateButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        webTemplateButtonActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(webTemplateButton, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.weightx = 0.9;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(webTemplateText, gridBagConstraints);

    webPublishButton.setText("Publish");
    webPublishButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        webPublishButtonActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(webPublishButton, gridBagConstraints);

    webPublishList.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "On Demand", "On Close", "On Save" }));
    webPublishList.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        webPublishListActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 0.1;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(webPublishList, gridBagConstraints);

    webViewButton.setText("View Web Page");
    webViewButton.setEnabled(false);
    webViewButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        webViewButtonActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.weightx = 0.1;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(webViewButton, gridBagConstraints);

    filler.setText(" ");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 0.1;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(filler, gridBagConstraints);

  }//GEN-END:initComponents

  private void webTemplateComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_webTemplateComboBoxActionPerformed
    if (webTemplateComboBox.getSelectedIndex() > 0) {
      try {
        Template template = new Template (td.logger);
        String templateMakerName = (String)webTemplateComboBox.getSelectedItem();
        File templateMaker = new File (templatesFolder, templateMakerName);
        td.logEvent (LogEvent.NORMAL,
            "Setting template file from " + templateMaker.toString(),
            false);
        template.openTemplate (templateMaker);
        RecordDefinition metaRecDef = new RecordDefinition();
        DataSet metaData = new DataSet (metaRecDef);
        template.openData (metaData, "metadata");
        template.setTemplateFilePath (td.diskStore.getPath());
        template.generateOutput();
        File out = new File(template.getTextFileOut().getDestination());
        if (out != null) {
          setTemplate (out);
          td.logEvent (LogEvent.NORMAL,
              "Setting Template File to " + templateFile.toString(),
              false);
        }
      } catch (IOException e) {
        td.trouble.report ("Trouble setting template", "Template Trouble");
      }
    } // end if a template source file selected
  }//GEN-LAST:event_webTemplateComboBoxActionPerformed

  private void webPublishListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_webPublishListActionPerformed
    td.diskStore.setPublishWhen (webPublishList.getSelectedIndex());
  }//GEN-LAST:event_webPublishListActionPerformed

  private void webViewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_webViewButtonActionPerformed
    td.browseFile (webFile);
  }//GEN-LAST:event_webViewButtonActionPerformed

  private void webPublishButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_webPublishButtonActionPerformed
    if (templateFile != null) {
      td.publish (templateFile);
    }
  }//GEN-LAST:event_webPublishButtonActionPerformed

  private void webTemplateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_webTemplateButtonActionPerformed
    XFileChooser chooser = new XFileChooser ();
    chooser.setFileSelectionMode(XFileChooser.FILES_ONLY);
    if (td.diskStore != null
        && (! td.diskStore.isUnknown())) {
      chooser.setCurrentDirectory (td.diskStore.getFile());
    }
    File result = chooser.showOpenDialog (td.frame);
    if (result != null) {
      templateFile = chooser.getSelectedFile();
      webTemplateText.setText(templateFile.toString());
    }
    webViewButton.setEnabled (false);
  }//GEN-LAST:event_webTemplateButtonActionPerformed
  
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel filler;
  private javax.swing.JLabel webGenerateTemplateLabel;
  private javax.swing.JButton webPublishButton;
  private javax.swing.JComboBox webPublishList;
  private javax.swing.JButton webTemplateButton;
  private javax.swing.JComboBox webTemplateComboBox;
  private javax.swing.JTextField webTemplateText;
  private javax.swing.JButton webViewButton;
  // End of variables declaration//GEN-END:variables
  
}
