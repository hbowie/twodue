/*
 * Copyright 2006 - 2013 Herb Bowie
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
