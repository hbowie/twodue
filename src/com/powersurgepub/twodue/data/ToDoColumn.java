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

package com.powersurgepub.twodue.data;

  import com.powersurgepub.psdatalib.elements.*;
  import java.text.*;
  import javax.swing.*;

/**
   An object representing one ToDoItem field as displayed in a user interface. 
 */
public class ToDoColumn {
  
  public final static   Boolean         BOOLEAN_CLASS     = new Boolean(true);
  
  public final static   String          STRING_CLASS      = "";
  
  public final static   ItemPriority    PRIORITY_CLASS    = new ItemPriority();
  
  public final static   ImageIcon       IMAGE_ICON_CLASS  = new ImageIcon();
  
  public final static   DateFormat      DATE_FORMAT     
      = new SimpleDateFormat ("MM/dd/yyyy");
  
  private               int             columnNumber;
  
  public  final static  int             NULL_SORT_SEQUENCE = 999;
  private               int             sortSequence = NULL_SORT_SEQUENCE;
  
  private               int             timesUsed = 0;
  
  private ImageIcon lateIcon;

  private ImageIcon todayIcon;

  private ImageIcon tomorrowIcon;

  private ImageIcon futureIcon;

  
  /** 
    Creates a new instance of ToDoColumn. 
   */
  public ToDoColumn(int columnNumber,
      ImageIcon lateIcon, ImageIcon todayIcon, 
      ImageIcon tomorrowIcon, ImageIcon futureIcon) {
        
    this.columnNumber = columnNumber;
    this.lateIcon = lateIcon;
    this.todayIcon = todayIcon;
    this.tomorrowIcon = tomorrowIcon;
    this.futureIcon = futureIcon;
    
  }
  
  /**
    Compare relative importance of two columns of ToDo information.
   
    @result < 0 if this column is less important than the second, or
              0 if the two columns are equally important, or 
            > 0 if this column is more important than the second.
    @param obj2 Second column to compare to this one. 
   */
  public int compareTo (Object object2)
      throws ClassCastException {
    ToDoColumn column2 = (ToDoColumn) object2;
    int c = 0;
    
    // First see whether one field is actually used and the other isn't
    // This takes precedence over all other conditions.
    if (this.isUsed() && (! column2.isUsed())) {
      c = 1;
    }
    else
    if ((! this.isUsed()) && column2.isUsed()) {
      c = -1;
    }
    else
      
    // Now see if one is higher in the sort hierarchy than the other
    if (this.getSortSequence() < column2.getSortSequence()) {
      c = 1;
    }
    else
    if (this.getSortSequence() > column2.getSortSequence()) {
      c = -1;
    }
    else
      
    // Now compare general display priorities for the two columns
    if (this.getDisplayPriority() < column2.getDisplayPriority()) {
      c = 1;
    } 
    else
    if (this.getDisplayPriority() > column2.getDisplayPriority()) {
      c = -1;
    }
    else
      
    // Finally, if all other fields and conditions are equal,
    // compare the two column numbers. 
    if (this.getColumnNumber() < column2.getColumnNumber()) {
      c = 1;
    } 
    else
    if (this.getColumnNumber() > column2.getColumnNumber()) {
      c = -1;
    }
    
    return c;
  }
  
  public int getColumnNumber () {
    return columnNumber;
  }
  
  public void setSortSequence (int sortSequence) {
    this.sortSequence = sortSequence;
  }
  
  public int getSortSequence () {
    return sortSequence;
  }
  
  public void resetTimesUsed () {
    // Always expect title to be used
    if (columnNumber == ToDoItem.TITLE) {
      timesUsed = 1;
    } else {
      timesUsed = 0;
    }
  }
  
  public void checkUsed (ToDoItem item) {
    Object value = getValue (item);
    Object nullOrDefault = getNullOrDefault();
    /*
    if (columnNumber == 5) {
      System.out.println ("For column " + this.toString()
           + " value = " + value.toString()
           + " and default = " + nullOrDefault.toString());
    } */
    if (! value.equals (nullOrDefault)) {
      timesUsed++;
    }
    switch (columnNumber) {
      case ToDoItem.STATUS:
        if (value.equals (ToDoItem.getStatusLabel (ActionStatus.CLOSED))
            || value.equals (ToDoItem.getStatusLabel (ActionStatus.PENDING_RECURS))) {
          timesUsed--;
        }
        break;
      case ToDoItem.RECURS_EVERY:
        if (value.equals ("-1")) {
          timesUsed--;
        }
        break;
      case ToDoItem.RECURS_DAY_OF_WEEK:
        if (value.equals ("0")) {
          timesUsed--;
        }
        break;
    } // end switch
    // System.out.println ("  Times used = " + String.valueOf (timesUsed));
  } // end checkUsed method
  
  public boolean isUsed () {
    return (timesUsed > 0);
  }
  
  public String getName () {
    return ToDoItem.COLUMN_NAME [columnNumber];
  }
  
  public String getDisplayName () {
    return ToDoItem.COLUMN_DISPLAY [columnNumber];
  }
  
  public String getBriefName () {
    return ToDoItem.COLUMN_BRIEF [columnNumber];
  }
  
  public int getWidth () {
    return ToDoItem.COLUMN_WIDTH [columnNumber];
  }
  
  public int getDisplayPriority () {
    return ToDoItem.COLUMN_DISPLAY_PRIORITY [columnNumber];
  }
  
    /**
   Returns the Class of a particular column to be displayed in a JTable view.
   
   @return The Class of the objects that will be returned for this column.
   @param column Column number of desired column.
   */
  public Class getColumnClass() {
    Class columnClass;
    int classType = ToDoItem.COLUMN_CLASS_TYPE [columnNumber];
    switch (classType) {
      case 1:
        // Is task done? (Display as check box)
        columnClass = BOOLEAN_CLASS.getClass();
        break;
      case 2:
        // Is due date in the past, or today? (Display graphic)
        columnClass = IMAGE_ICON_CLASS.getClass();
        break;
      case 3:
        // Priority
        columnClass = PRIORITY_CLASS.getClass();
        break;
      default:
        // Everything else is a string
        columnClass = STRING_CLASS.getClass();
        break;
    } // end switch
    return columnClass;
  } // end method
  
  /**
    Gets a field from the ToDoItem record, based on the field number.
   
    @return The requested field.
    @param  ToDoItem containing the desired data.
    */
  public Object getValue (ToDoItem item) {
    switch (columnNumber) {
      case ToDoItem.DELETED:
        return new Boolean (item.isDeleted());
      case ToDoItem.TAGS:
        return item.getTags().toString();
      case ToDoItem.DUE_DATE:
        return item.getDueDate (DATE_FORMAT);
      case ToDoItem.LATE:
        int late = item.getLateCode();
        if (late < 0) {
          return lateIcon;
        }
        else
        if (late == 0) {
          return todayIcon;
        } 
        else
        if (late == 1) {
          return tomorrowIcon;
        }
        else {
          return futureIcon;
        }
      case ToDoItem.PRIORITY:
        return new ItemPriority (item.getPriority ());
      case ToDoItem.STATUS:
        return item.getStatusLabel();
      case ToDoItem.DONE:
        return new Boolean (item.isDone());
      case ToDoItem.ASSIGNED_TO:
        return item.getAssignedTo();
      case ToDoItem.SEQUENCE:
        return item.getSequence();
      case ToDoItem.TITLE:
        return item.getTitle ();
      case ToDoItem.DESCRIPTION:
        return item.getDescription ();
      case ToDoItem.OUTCOME:
        return item.getOutcome ();
      case ToDoItem.WEB_PAGE:
        return item.getWebPage ();
      case ToDoItem.START_TIME:
        return item.getStartTimeAsString();
      case ToDoItem.DURATION:
        return item.getDurationAsString();
      case ToDoItem.ALERT_PRIOR:
        return item.getAlertPriorAsString();
      case ToDoItem.RECURS_EVERY:
        return String.valueOf (item.getRecursEvery ());
      case ToDoItem.RECURS_UNIT:
        return item.getRecursUnitAsString ();
      case ToDoItem.RECURS_DAY_OF_WEEK:
        return String.valueOf (item.getRecursDayOfWeek ());
      case ToDoItem.RECURS_WITHIN_MONTH:
        return String.valueOf (item.getRecursWithinMonth ());
      /*
      case ToDoItem.ID:
        return item.getID();
      */
      case ToDoItem.TYPE:
        return item.getTypeName();
      case ToDoItem.FILE_LENGTH:
        return item.getFileLengthAsString();
      case ToDoItem.LAST_MOD_DATE:
        return item.getLastModDate (DATE_FORMAT);
      default:
        return "";
    }
   
  } // end method getValue
  
  /**
    Gets a field from the ToDoItem record, based on the field number.
   
    @return The requested field.
    @param  ToDoItem containing the desired data.
    */
  public Object getNullOrDefault () {
    switch (columnNumber) {
      case ToDoItem.DELETED:
        return new Boolean (false);
      case ToDoItem.TAGS:
        return "";
      case ToDoItem.DUE_DATE:
        return " ";
        // return DATE_FORMAT.format (ToDoItem.DEFAULT_DATE.getTime());
      case ToDoItem.LATE:
        return futureIcon;
      case ToDoItem.PRIORITY:
        return new ItemPriority (3);
      case ToDoItem.STATUS:
        return ToDoItem.getStatusLabel (ActionStatus.OPEN);
      case ToDoItem.DONE:
        return new Boolean (false);
      case ToDoItem.ASSIGNED_TO:
        return "";
      case ToDoItem.SEQUENCE:
        return "";
      case ToDoItem.TITLE:
        return "";
      case ToDoItem.DESCRIPTION:
        return "";
      case ToDoItem.OUTCOME:
        return "";
      case ToDoItem.WEB_PAGE:
        return "";
      case ToDoItem.START_TIME:
        return ToDoItem.DEFAULT_START_TIME;
      case ToDoItem.DURATION:
        return "N/A";
      case ToDoItem.ALERT_PRIOR:
        return "N/A";
      case ToDoItem.RECURS_EVERY:
        return "0";
      case ToDoItem.RECURS_UNIT:
        return "NA";
      case ToDoItem.RECURS_DAY_OF_WEEK:
        return "-1";
      case ToDoItem.RECURS_WITHIN_MONTH:
        return "-1";
      case ToDoItem.ID:
        return "";
      case ToDoItem.TYPE:
        return "";
      case ToDoItem.FILE_LENGTH:
        return "";
      case ToDoItem.LAST_MOD_DATE:
        return "";
      default:
        return "";
    }
   
  } // end method getNullOrDefault
  
  /**
    Return columns as some kind of string.
   
    @return Name of column.
   */
  public String toString () {
    return getDisplayName();
  }
  
} // end class ToDoColumn
