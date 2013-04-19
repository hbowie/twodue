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

  import com.powersurgepub.psdatalib.ui.DateOwner;
	import com.powersurgepub.psutils.*;
  import com.powersurgepub.twodue.data.*;
  import java.awt.*;
  import java.awt.event.*;
  import java.io.*;
  import java.net.*;
  import javax.swing.*;
  import javax.swing.event.*;
  import java.text.*;
  import java.util.*;

/**
 *
 * @author  hbowie
 */
public class DatePanel 
    extends javax.swing.JPanel 
      implements DateOwner {
      
  private JFrame              frame;
  private ToDoItem            item;
  private boolean             done = false;
  // private GregorianCalendar   date = new GregorianCalendar();
  private boolean             modified = false;
  private com.powersurgepub.psdatalib.ui.DatePanel uiDatePanel;
  
  private ImageIcon       lateIcon;
  private ImageIcon       todayIcon;
  private ImageIcon       tomorrowIcon;
  private ImageIcon       futureIcon;
  
  /** Creates new form DatePanel */
  public DatePanel(JFrame frame) {
    this.frame = frame;
    uiDatePanel = new com.powersurgepub.psdatalib.ui.DatePanel (frame, this);
    initComponents();
    GridBagConstraints gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
    gridBagConstraints.anchor = GridBagConstraints.WEST;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    add(uiDatePanel, gridBagConstraints);
    // setBorder (BorderFactory.createLineBorder(Color.black));
  }
  
  public void setLateIcons 
      (ImageIcon late, ImageIcon today, ImageIcon tomorrow, ImageIcon future) {
    lateIcon = late;
    todayIcon = today;
    tomorrowIcon = tomorrow;
    futureIcon = future;
  }
  
  public void setItem (ToDoItem item) {
    this.item = item;
    /*
    if (item.isRecurring()) {
      recurButton.setEnabled (true);
      StringBuffer recursTip = new StringBuffer();
      recursTip.append("Every ");
      recursTip.append(String.valueOf (item.getRecursEvery()));
      recursTip.append (" ");
      switch (item.getRecursUnit()) {
        case (ToDoItem.DAYS):
          recursTip.append ("day");
          break;
        case (ToDoItem.WEEKS):
           recursTip.append ("week");
          break;
        case (ToDoItem.MONTHS):
          recursTip.append ("month");
          break;
        case (ToDoItem.YEARS):
          recursTip.append ("year");
          break;          
      } // end switch
      if (item.getRecursEvery() > 1) {
        recursTip.append ("s");
      }
      recurButton.setToolTipText (recursTip.toString());
    } else {
      recurButton.setEnabled (false);
    } // end if not recurring
    done = item.isDone();
    */
  }
  
  public void setDate (Date date) {
    uiDatePanel.setDate (date);
    modified = false;
    displayFields();
  }
  
  public void setDone (boolean done) {
    this.done = done;
    setLateLabel();
  }
  
  /**
   To be called whenever the date is modified by DatePanel.
   */
  public void dateModified (Date date) {
    displayFields();
  }
  
  /**
   Does this date have an associated rule for recurrence?
   */
  public boolean canRecur() {
    if (item == null) {
      return false;
    } else {
      return (item.isRecurring());
    }
  }
  
  /**
   Provide a text string describing the recurrence rule, that can
   be used as a tool tip.
   */
  public String getRecurrenceRule() {
    StringBuffer recursTip = new StringBuffer();
    if (item.isRecurring()) {
      recursTip.append("Every ");
      recursTip.append(String.valueOf (item.getRecursEvery()));
      recursTip.append (" ");
      switch (item.getRecursUnit()) {
        case (ToDoItem.DAYS):
          recursTip.append ("day");
          break;
        case (ToDoItem.WEEKS):
           recursTip.append ("week");
          break;
        case (ToDoItem.MONTHS):
          recursTip.append ("month");
          break;
        case (ToDoItem.YEARS):
          recursTip.append ("year");
          break;          
      } // end switch
      if (item.getRecursEvery() > 1) {
        recursTip.append ("s");
      }
    } 
    return recursTip.toString();
  }
  
  /**
   Apply the recurrence rule to the date.
   */
  public void recur (GregorianCalendar date) {
    item.recur (date);
  }
  
  private void displayFields() {
    setLateLabel();
  }
  
  private void setLateLabel () {
    int late = DateUtils.getLateCode(done, uiDatePanel.getDate());
    if (late < 0) {
      lateLabel.setIcon (lateIcon);
    }
    else
    if (late == 0) {
      lateLabel.setIcon (todayIcon);
    } 
    else
    if (late == 1) {
      lateLabel.setIcon (tomorrowIcon);
    }
    else {
      lateLabel.setIcon (futureIcon);
    }
  }
  
  public boolean wasModified () {
    return uiDatePanel.isModified();
  }
  
  public Date getDate () {
    return uiDatePanel.getDate();
  }
  
  public void editDate() {
    uiDatePanel.editDate();
  }
  
  /*
  public int getYear() {
    int year = 0;
    try {
      year = Integer.parseInt (yearText.getText());
      if (year < 100) {
        if (year < 50) {
          year = year + 2000;
        } else {
          year = year + 1900;
        }
        yearText.setText (String.valueOf (year));
      }
      date.set (Calendar.YEAR, year);
    } catch (NumberFormatException e) {
      year = date.get (Calendar.YEAR);
      yearText.setText (String.valueOf (year));
    }
    return year;
  }
  
  public int getMonth() {
    int month = 0;
    try {
      month = Integer.parseInt (monthText.getText());
      date.set (Calendar.MONTH, (month - 1));
    } catch (NumberFormatException e) {
      month = date.get (Calendar.DATE) + 1;
      monthText.setText (String.valueOf (month));
    }
    return month;
  }
  
  public int getDayOfMonth() {
    int day = 0;
    try {
      day = Integer.parseInt (dayText.getText());
      date.set (Calendar.DATE, day);
    } catch (NumberFormatException e) {
      day = date.get (Calendar.DATE);
      dayText.setText (String.valueOf (day));
    }
    return day;
  }
  */
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    lateLabel = new javax.swing.JLabel();

    setLayout(new java.awt.GridBagLayout());

    setMinimumSize(new java.awt.Dimension(250, 34));
    setPreferredSize(new java.awt.Dimension(250, 34));
    lateLabel.setIcon (futureIcon);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
    add(lateLabel, gridBagConstraints);

  }
  // </editor-fold>//GEN-END:initComponents
  
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel lateLabel;
  // End of variables declaration//GEN-END:variables
  
}
