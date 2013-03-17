/*
 * Field.java
 *
 * Created on May 14, 2006, 11:09 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.powersurgepub.twodue.data;

/**
 *
 * @author hbowie
 */
public class Field {
  
  public static final String COLUMN_NAME = "";
  public static final String COLUMN_DISPLAY_NAME = "";
  public static final String COLUMN_BRIEF_NAME = "";
  public static final int    COLUMN_WIDTH = 100;
  public static final int    COLUMN_CLASS_TYPE = 0;
  public static final int    COLUMN_DISPLAY_PRIORITY = 900;
  
  /** Creates a new instance of Field */
  public Field() {
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
