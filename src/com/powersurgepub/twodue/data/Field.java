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
