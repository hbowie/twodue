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

  import com.powersurgepub.twodue.data.*;
  import com.powersurgepub.twodue.disk.*;
  import javax.swing.*;

/**
   A panel to display information about the program's execution.  
 */
public class LogTab 
    extends javax.swing.JPanel 
      implements TwoDueTab {
      
  private TwoDueCommon td;
  
  /** Creates new form LogTab */
  public LogTab(TwoDueCommon td) {
    this.td = td;
    initComponents();
  }
  
  /**
    Prepare to switch tabs and show this one.
   */
  private void showThisTab () {
    td.switchTabs();
  }
  
  public JTextArea getTextArea () {
    return logTextArea;
  }
  
  /**
    Prepares the tab for processing of newly opened file.
   
    @param store Disk Store object for the file.
   */
  public void filePrep (TwoDueDiskStore store) {
    // No file information used on the About Tab
  }
  
  public void displayItem() {
    // No item info to display on this tab
  }  
  
  /**
   Modifies the td.item if anything on the screen changed. 
   
   @return True if any of the data changed on this tab. 
   */
  public boolean modIfChanged () {
    return false;
  } // end method
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  private void initComponents() {//GEN-BEGIN:initComponents
    logScrollPane = new javax.swing.JScrollPane();
    logTextArea = new javax.swing.JTextArea();

    setLayout(new java.awt.BorderLayout());

    addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentShown(java.awt.event.ComponentEvent evt) {
        formComponentShown(evt);
      }
    });

    logScrollPane.setViewportView(logTextArea);

    add(logScrollPane, java.awt.BorderLayout.CENTER);

  }//GEN-END:initComponents

  private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
    showThisTab();
    logScrollPane.requestFocus();
  }//GEN-LAST:event_formComponentShown
  
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JScrollPane logScrollPane;
  private javax.swing.JTextArea logTextArea;
  // End of variables declaration//GEN-END:variables
  
}
