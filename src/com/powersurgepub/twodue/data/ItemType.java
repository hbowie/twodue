/*
 * ItemType.java
 *
 * Created on May 14, 2006, 11:09 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.powersurgepub.twodue.data;

import javax.swing.*;

/**
 *
 * @author hbowie
 */
public class ItemType
    extends Field {
  
  public  final static String COLUMN_NAME         = "type";
  public  final static String COLUMN_DISPLAY_NAME = "Type";
  public  final static String COLUMN_BRIEF_NAME   = "Type";
  public  final static int    COLUMN_WIDTH        = 100;
  public  final static int    COLUMN_CLASS_TYPE   = 0;
  public  final static int    COLUMN_DISPLAY_PRIORITY = 35;
  
  public  final static String[] TYPE_NAME = {
    "Action",
    "Affirmation",
    "Agenda",
    "Discussion",
    "Info",
    "Input",
    "Link",
    "Principle",
    "Quotation",
    "Review",
    "Strategy"
  };
  
  private               int         type          = 0;
  
  
  
  /** Creates a new instance of ItemType */
  public ItemType() {
    set ("Action");
  }
  
  public ItemType (int type) {
    set (type);
  }
  
  public void set (int type) {
    if (isValidIndex(type)) {
      this.type = type;
    }
  }
  
  public int set (String typeName) {
    int i = getIndex (typeName);
    if (isValidIndex (i)) {
      type = i;
    }
    return i;
  }
  
  public static int getIndex (String typeName) {
    int i = 0;
    boolean found = false;
    while (isValidIndex(i) && (! found)) {
      found = (typeName.equalsIgnoreCase (TYPE_NAME [i]));
      if (! found) {
        i++;
      }
    }
    if (! found) {
      i = -1;
    }
    return i;    
  }
  
  public int get () {
    return type;
  }
  
  public String getName () {
    return (getName (type));
  }
  
  public static String getName (int i) {
    if (isValidIndex (i)) {
      return (TYPE_NAME [i]);
    } else {
      return "** unknown **";
    }
  }
  
  public static boolean isValidIndex (int i) {
    return (i >= 0 & i < TYPE_NAME.length);
  }
  
  public static int size() {
    return TYPE_NAME.length;
  }
  
  public static void initComboBox (JComboBox cb) {
    for (int i = 0; isValidIndex (i); i++) {
      cb.addItem (getName (i));
    }
  }
  
  public static String getColumnName () {
    return COLUMN_NAME;
  }
  
  public static String getColumnDisplayName () {
    return COLUMN_DISPLAY_NAME;
  }
  
  public static String getColumnBriefName () {
    return COLUMN_BRIEF_NAME;
  }
  
  public static int getColumnWidth() {
    return COLUMN_WIDTH;
  }
  
  public static int getColumnClassType () {
    return COLUMN_CLASS_TYPE;
  }
  
  public static int getColumnDisplayPriority () {
    return COLUMN_DISPLAY_PRIORITY;
  }
  
}
