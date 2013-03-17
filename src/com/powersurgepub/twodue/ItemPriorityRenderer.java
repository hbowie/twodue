package com.powersurgepub.twodue;

  import java.awt.*;
  import javax.swing.*;

/**
   An object capable of rendering the priority of a To Do item
   in a JTable cell. <p>
  
   This code is copyright (c) 2003 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
      2003/11/02 - Originally written.
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 2003/11/02 - Originally written. 
 */
public class ItemPriorityRenderer 
    extends javax.swing.table.DefaultTableCellRenderer 
        implements javax.swing.table.TableCellRenderer {
          
  public static final Color PRIORITY_1_COLOR = new Color (204, 0, 0);
  public static final Color PRIORITY_2_COLOR = new Color (102, 102, 204);
  public static final Color PRIORITY_3_COLOR = new Color (0, 0, 255);
  public static final Color PRIORITY_4_COLOR = new Color (102, 102, 0);
  public static final Color PRIORITY_5_COLOR = new Color (0, 153, 51);
  public static final Color[] COLORS = { PRIORITY_1_COLOR, PRIORITY_2_COLOR,
      PRIORITY_3_COLOR, PRIORITY_4_COLOR, PRIORITY_5_COLOR };
  
  /** Creates a new instance of ItemPriorityRenderer */
  public ItemPriorityRenderer() {
    this.setHorizontalAlignment (JLabel.CENTER);
  }
  
  public void setText (String text) {
    super.setText(text);
    int priorityIndex;
    try {
      priorityIndex = Integer.parseInt(text);
      priorityIndex--;
      if (priorityIndex >= 0 && priorityIndex <= 4) {
        this.setForeground (COLORS [priorityIndex]);
      }
    } catch (NumberFormatException e) {
    }
  }
  
  /* public java.awt.Component getTableCellRendererComponent
      (javax.swing.JTable jTable, ItemPriority priorityObject, 
      boolean param, boolean param3, int param4, int param5) {
    java.awt.Component component 
        = super.getTableCellRendererComponent
            (jTable, priorityObject, param, param3, param4, param5);
    System.out.println ("ItemPriorityRenderer working...");
    component.setForeground (COLORS [priorityObject.getPriority() - 1]);
    // try {
      JLabel label = (JLabel)component;
      label.setHorizontalAlignment (JLabel.CENTER);
    // } catch (
      
    return component;
  } */
  
}
