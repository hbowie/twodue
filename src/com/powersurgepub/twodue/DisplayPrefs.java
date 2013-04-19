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

  import com.powersurgepub.psutils.*;
  import com.powersurgepub.twodue.data.*;
  import com.powersurgepub.twodue.disk.*;
  import com.powersurgepub.xos2.*;
  import java.awt.*;
  import javax.swing.*;

/**
 *
 * @author  hbowie
 */
public class DisplayPrefs extends javax.swing.JPanel {
  
  public static final String ROTATE_BACKGROUND_COLOR_KEY  = "rotatebackcolor";
  public static final String ROTATE_TEXT_COLOR_KEY        = "rotatetextcolor";
  public static final String ROTATE_NORMAL_FONT_SIZE_KEY  = "rotatefontsize";
  public static final String ROTATE_FONT_NAME_KEY         = "rotatefontname";
  public static final String ROTATE_METHOD_KEY            = "rotatemethod";
  public static final String ROTATE_SECONDS_KEY           = "rotateseconds";
  
  private TwoDueCommon      td;
  private PrefsWindow       prefsWindow;
  
  private XOS               xos = XOS.getShared();
  
  private ProgramVersion    programVersion = ProgramVersion.getShared();
  
  private boolean           setupComplete = false;
  
  private String[]          fontList;
  
  /** Creates new form GeneralPrefs */
  public DisplayPrefs(TwoDueCommon td, PrefsWindow prefsWindow) {
    this.td = td;
    this.prefsWindow = prefsWindow;
    initComponents();
    
    td.rotateTextColor = StringUtils.hexStringToColor 
        (td.userPrefs.getPref (ROTATE_TEXT_COLOR_KEY, "000000"));
    
    td.rotateBackgroundColor = StringUtils.hexStringToColor 
        (td.userPrefs.getPref (ROTATE_BACKGROUND_COLOR_KEY, "FFFFFF"));
    
    td.rotateNormalFontSize 
        = td.userPrefs.getPrefAsInt (ROTATE_NORMAL_FONT_SIZE_KEY,  3);
    rotateFontSizeSlider.setValue(td.rotateNormalFontSize);
    if (td.rotateNormalFontSize < 7) {
      td.rotateBigFontSize = td.rotateNormalFontSize + 1;
    } else {
      td.rotateBigFontSize = 7;
    }

    td.rotateFont = td.userPrefs.getPref (ROTATE_FONT_NAME_KEY, "Verdana");
    fontList 
        = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    for (int i = 0; i < fontList.length; i++) {
      rotateFontComboBox.addItem (fontList [i]);
      if (td.rotateFont.equals (fontList [i])) {
        rotateFontComboBox.setSelectedItem (fontList [i]);
      }
    }

    displayRotateSampleText();
    
    td.rotateList.setRotateMethod 
        (td.userPrefs.getPrefAsInt (ROTATE_METHOD_KEY, RotateList.ROTATE_LINEAR));
    if (td.rotateList.getRotateMethod() != RotateList.ROTATE_LINEAR
        && td.rotateList.getRotateMethod() != RotateList.ROTATE_RANDOM
        && td.rotateList.getRotateMethod() != RotateList.ROTATE_RANDOM_WEIGHTED) {
      td.rotateList.setRotateMethod (RotateList.ROTATE_LINEAR);
    }
    
    td.rotateSeconds
        = td.userPrefs.getPrefAsInt (ROTATE_SECONDS_KEY, 10);
    rotateSecondsText.setText (String.valueOf (td.rotateSeconds));
    
    setupComplete = true;
  }
  
  public void displayRotateSampleText () {
    StringBuffer text = new StringBuffer();
    text.append ("<html>");
    text.append ("<body bgcolor=\"#" 
        + StringUtils.colorToHexString(td.rotateBackgroundColor)
        + "\" text=\"#"
        + StringUtils.colorToHexString(td.rotateTextColor)
        + "\" link=\"#"
        + StringUtils.colorToHexString(td.rotateTextColor)
        + "\" alink=\"#"
        + StringUtils.colorToHexString(td.rotateTextColor)
        + "\" vlink=\"#"
        + StringUtils.colorToHexString(td.rotateTextColor)
        + "\">");
    
    text.append ("<p><font size="
        + String.valueOf (td.rotateNormalFontSize)
        + " face=\""
        + td.rotateFont
        + ", Verdana, Arial, sans-serif\">" 
        + "Sample Text"
        + "</font></p>");
    
    text.append ("</body>");
    text.append ("</html>");
    rotateTextSample.setText (text.toString());
  }
  
  /**
    Prepares the tab for processing of newly opened file.
   
    @param store Disk Store object for the file.
   */
  public void filePrep (TwoDueDiskStore store) {
    // No file information used on the Prefs Tab
  }
  
  /**
    Prepare to switch tabs and show this one.
   */
  private void showThisTab () {
    prefsWindow.switchTabs();
  }
  
  public void displayItem() {
  }  
  
  public boolean modIfChanged() {
    return false;
  }  
  

  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    rotateBackgroundColorLabel = new javax.swing.JLabel();
    rotateBackgroundColorButton = new javax.swing.JButton();
    rotateTextColorLabel = new javax.swing.JLabel();
    rotateTextColorButton = new javax.swing.JButton();
    rotateFontSizeLabel = new javax.swing.JLabel();
    rotateFontSizeSlider = new javax.swing.JSlider();
    rotateTextSample = new javax.swing.JEditorPane();
    rotateFontLabel = new javax.swing.JLabel();
    rotateFontComboBox = new javax.swing.JComboBox();
    rotateSecondsLabel = new javax.swing.JLabel();
    rotateSecondsText = new javax.swing.JTextField();
    rotateSecondsLabel2 = new javax.swing.JLabel();
    rotateMethodLabel = new javax.swing.JLabel();
    rotateMethodComboBox = new javax.swing.JComboBox();

    setLayout(new java.awt.GridBagLayout());

    rotateBackgroundColorLabel.setText("Display Background:");
    rotateBackgroundColorLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 6;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(rotateBackgroundColorLabel, gridBagConstraints);

    rotateBackgroundColorButton.setText("Select...");
    rotateBackgroundColorButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        rotateBackgroundColorButtonActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 7;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(rotateBackgroundColorButton, gridBagConstraints);

    rotateTextColorLabel.setText("Display Text:");
    rotateTextColorLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 6;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(rotateTextColorLabel, gridBagConstraints);

    rotateTextColorButton.setText("Select...");
    rotateTextColorButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        rotateTextColorButtonActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 7;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(rotateTextColorButton, gridBagConstraints);

    rotateFontSizeLabel.setText("Display Font Size:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 6;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 2, 2, 2);
    add(rotateFontSizeLabel, gridBagConstraints);

    rotateFontSizeSlider.setMajorTickSpacing(1);
    rotateFontSizeSlider.setMaximum(7);
    rotateFontSizeSlider.setMinimum(1);
    rotateFontSizeSlider.setPaintLabels(true);
    rotateFontSizeSlider.setPaintTicks(true);
    rotateFontSizeSlider.setSnapToTicks(true);
    rotateFontSizeSlider.setValue(3);
    rotateFontSizeSlider.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        rotateFontSizeSliderStateChanged(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 7;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(rotateFontSizeSlider, gridBagConstraints);

    rotateTextSample.setEditable(false);
    rotateTextSample.setContentType("text/html");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 6;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.gridheight = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(rotateTextSample, gridBagConstraints);

    rotateFontLabel.setText("Display Font:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 6;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(rotateFontLabel, gridBagConstraints);

    rotateFontComboBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        rotateFontComboBoxActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 6;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(rotateFontComboBox, gridBagConstraints);

    rotateSecondsLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    rotateSecondsLabel.setText("Rotate Every:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 6;
    gridBagConstraints.gridy = 7;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(rotateSecondsLabel, gridBagConstraints);

    rotateSecondsText.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
    rotateSecondsText.setText("10");
    rotateSecondsText.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        rotateSecondsTextActionPerformed(evt);
      }
    });
    rotateSecondsText.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        rotateSecondsTextFocusLost(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 7;
    gridBagConstraints.gridy = 7;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 0.5;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(rotateSecondsText, gridBagConstraints);

    rotateSecondsLabel2.setText("seconds");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 8;
    gridBagConstraints.gridy = 7;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(rotateSecondsLabel2, gridBagConstraints);

    rotateMethodLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    rotateMethodLabel.setText("Rotate Method:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 6;
    gridBagConstraints.gridy = 8;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(rotateMethodLabel, gridBagConstraints);

    rotateMethodComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Linear", "Random", "Random, Weighted" }));
    rotateMethodComboBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        rotateMethodComboBoxActionPerformed(evt);
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 7;
    gridBagConstraints.gridy = 8;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(rotateMethodComboBox, gridBagConstraints);

  }// </editor-fold>//GEN-END:initComponents

  private void rotateMethodComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rotateMethodComboBoxActionPerformed
    td.rotateList.setRotateMethod(rotateMethodComboBox.getSelectedIndex());
    td.userPrefs.setPref(ROTATE_METHOD_KEY, td.rotateList.getRotateMethod());
  }//GEN-LAST:event_rotateMethodComboBoxActionPerformed

  private void rotateSecondsTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rotateSecondsTextFocusLost
    try {
      td.rotateSeconds = Integer.parseInt(rotateSecondsText.getText());
      td.userPrefs.setPref(ROTATE_SECONDS_KEY, td.rotateSeconds);
    } catch (NumberFormatException e) {
      rotateSecondsText.setText(String.valueOf(td.rotateSeconds));
    }
  }//GEN-LAST:event_rotateSecondsTextFocusLost

  private void rotateSecondsTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rotateSecondsTextActionPerformed
    try {
      td.rotateSeconds = Integer.parseInt(rotateSecondsText.getText());
      td.userPrefs.setPref(ROTATE_SECONDS_KEY, td.rotateSeconds);
    } catch (NumberFormatException e) {
      rotateSecondsText.setText(String.valueOf(td.rotateSeconds));
    }
  }//GEN-LAST:event_rotateSecondsTextActionPerformed

  private void rotateFontComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rotateFontComboBoxActionPerformed
    if (setupComplete) {
      td.rotateFont = (String)rotateFontComboBox.getSelectedItem();
      td.userPrefs.setPref(ROTATE_FONT_NAME_KEY, td.rotateFont);
      displayRotateSampleText();
    }
  }//GEN-LAST:event_rotateFontComboBoxActionPerformed

  private void rotateFontSizeSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rotateFontSizeSliderStateChanged
    if (! rotateFontSizeSlider.getValueIsAdjusting()) {
      td.rotateNormalFontSize = rotateFontSizeSlider.getValue();
      td.userPrefs.setPref(ROTATE_NORMAL_FONT_SIZE_KEY, td.rotateNormalFontSize);
      displayRotateSampleText();
    }
  }//GEN-LAST:event_rotateFontSizeSliderStateChanged

  private void rotateTextColorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rotateTextColorButtonActionPerformed
    Color oldColor = td.rotateTextColor;
    td.rotateTextColor = JColorChooser.showDialog(
        this,
        "Choose Text Color for Rotate Tab",
        oldColor);
    td.userPrefs.setPref(ROTATE_TEXT_COLOR_KEY,
        StringUtils.colorToHexString(td.rotateTextColor));
    displayRotateSampleText();
  }//GEN-LAST:event_rotateTextColorButtonActionPerformed

  private void rotateBackgroundColorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rotateBackgroundColorButtonActionPerformed
    Color oldColor = td.rotateBackgroundColor;
    td.rotateBackgroundColor = JColorChooser.showDialog(
        this,
        "Choose Background Color for Rotate Tab",
        oldColor);
    td.userPrefs.setPref(ROTATE_BACKGROUND_COLOR_KEY,
        StringUtils.colorToHexString(td.rotateBackgroundColor));
    displayRotateSampleText();
  }//GEN-LAST:event_rotateBackgroundColorButtonActionPerformed
  
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton rotateBackgroundColorButton;
  private javax.swing.JLabel rotateBackgroundColorLabel;
  private javax.swing.JComboBox rotateFontComboBox;
  private javax.swing.JLabel rotateFontLabel;
  private javax.swing.JLabel rotateFontSizeLabel;
  private javax.swing.JSlider rotateFontSizeSlider;
  private javax.swing.JComboBox rotateMethodComboBox;
  private javax.swing.JLabel rotateMethodLabel;
  private javax.swing.JLabel rotateSecondsLabel;
  private javax.swing.JLabel rotateSecondsLabel2;
  private javax.swing.JTextField rotateSecondsText;
  private javax.swing.JButton rotateTextColorButton;
  private javax.swing.JLabel rotateTextColorLabel;
  private javax.swing.JEditorPane rotateTextSample;
  // End of variables declaration//GEN-END:variables
  
}
