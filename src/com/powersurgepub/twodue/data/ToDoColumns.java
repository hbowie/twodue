package com.powersurgepub.twodue.data;

  import java.util.*;
  import javax.swing.*;

/**
   A collection of ToDoColumn objects. This can be used to specify the fields
   to be displayed in a JTable view of ToDoItem objects. <p>
  
   This code is copyright (c) 2004 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
      2004/06/06 - Originally written.
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 
      2004/06/06 - Originally written. 
 */
public class ToDoColumns {
  
  public final static int NUMBER_OF_SORT_FIELDS = 4;
  
  private List columns;
  
  /** 
    Creates a new instance of ToDoColumns. 
   
    @param lateIcon     Icon to flag late items.
    @param todayIcon    Icon to flag items due today.
    @param tomorrowIcon Icon to flag items due tomorrow.
    @param futureIcon   Icon to flag items due farther in the future.
   */
  public ToDoColumns(ImageIcon lateIcon, ImageIcon todayIcon, 
      ImageIcon tomorrowIcon, ImageIcon futureIcon) {
        
    columns = new ArrayList();
    for (int i = 0; i < ToDoItem.NUMBER_OF_COLUMNS; i++) {
      ToDoColumn column = new ToDoColumn (i, lateIcon, todayIcon, 
          tomorrowIcon, futureIcon);
      columns.add (column);
      // System.out.println ("Column " + String.valueOf (i) 
      //     + "-" + column.getDisplayName() + " created");
    }
  } // end constructor
  
  public void setComparator (ItemComparator comp) {
    for (int i = 0; i < ToDoItem.NUMBER_OF_COLUMNS; i++) {
      ToDoColumn column = (ToDoColumn) columns.get (i);
      column.setSortSequence (ToDoColumn.NULL_SORT_SEQUENCE);
      // System.out.println ("Column " + String.valueOf (i) 
      //     + " had sort sequence reset");
    }
    for (int i = 0; i < NUMBER_OF_SORT_FIELDS; i++) {
      String field = comp.getSortField (i);
      ToDoColumn column = getColumn (field);
      // System.out.println ("Column " + String.valueOf (i) + " " + field);
      if (column == null) {
        System.out.println ("Column is null");
      }
      column.setSortSequence (i);
      ToDoColumn column2;
      if (field.equals (ToDoItem.COLUMN_DISPLAY [ToDoItem.STATUS])) {
        column2 
					= getColumn (ToDoItem.COLUMN_DISPLAY [ToDoItem.DONE]);
        column2.setSortSequence (i);
      } // end if status is the sort key
      else
      if (field.equals (ToDoItem.COLUMN_DISPLAY [ToDoItem.DUE_DATE])) {
        column2
            = getColumn (ToDoItem.COLUMN_DISPLAY [ToDoItem.LATE]);
        column2.setSortSequence (i);
      } // end if due date is the sort key
    } // end for each sort field
  } // end setComparator method
  
  public void resetTimesUsed () {
    for (int i = 0; i < ToDoItem.NUMBER_OF_COLUMNS; i++) {
      ToDoColumn column = (ToDoColumn) columns.get (i);
      column.resetTimesUsed ();
      // System.out.println ("Column " + String.valueOf (i) 
      //     + " had times used reset");
    }
  }
  
  public void checkUsed (ToDoItem item) {
    for (int i = 0; i < ToDoItem.NUMBER_OF_COLUMNS; i++) {
      ToDoColumn column = (ToDoColumn) columns.get (i);
      column.checkUsed (item);
      // System.out.println ("Column " + String.valueOf (i) 
      //     + " had usage checked");
    }
  }
  
  public ToDoColumn getColumn (String displayName) {
    ToDoColumn column = null;
    boolean found = false;
    int i = 0;
    while ((! found) && (i < ToDoItem.NUMBER_OF_COLUMNS)) {
      column = (ToDoColumn) columns.get (i);
      // System.out.println ("Column " + column.toString() 
      //     + " at position " + String.valueOf (i)
      //     + " checked for name equal to " + displayName);
      // System.out.println ("Column " + String.valueOf (i) + " " + column.getDisplayName());
      if (displayName.equalsIgnoreCase (column.getDisplayName())) {
        found = true;
      } else {
        i++;
      } // end if not equal
    } // end while looking for a match
    if (found) {
      // System.out.println ("Column " + column.toString() 
      //     + " found at position " + String.valueOf (i));
      return column;
    } else {
      return null;
    }
  } // end getColumn method
  
  public ArrayList getDisplayColumns (int availableWidth) {
    
    // Build new list of columns
    ArrayList displayColumns = new ArrayList (columns);
    boolean swapped = true;
    
    // Put the most important ones first
    while (swapped) {
      swapped = false;
      // System.out.println ("Starting another bubble sort pass");
      for (int i = 0, j = 1; j < displayColumns.size(); i++, j++) {
        ToDoColumn columni = (ToDoColumn) displayColumns.get(i);
        ToDoColumn columnj = (ToDoColumn) displayColumns.get(j);
        // System.out.println ("Column " + columni.toString() 
        //   + " found at position " + String.valueOf (i));
        // System.out.println ("Column " + columnj.toString() 
        //   + " found at position " + String.valueOf (j));
        if (columni.compareTo (columnj) < 0) {
          displayColumns.set (i, columnj);
          displayColumns.set (j, columni);
          swapped = true;
          // System.out.println ("Swapped column " + columni.toString() 
          //     + " with column " + columnj.toString());
        } // end if two entries needed to be swapped
      } // end of one pass through display columns
    } // end while still swapping entries
    
    // Now prune columns that won't fit in the available space
    int usedWidth = 0;
    int i = 0;
    while (i < displayColumns.size()) {
      ToDoColumn column = (ToDoColumn) displayColumns.get(i);
      // System.out.println ("Checking Column " + column.toString() 
      //     + " found at position " + String.valueOf (i));
      usedWidth = usedWidth + column.getWidth();
      if (usedWidth > availableWidth || (! column.isUsed())) {
        displayColumns.remove (i);
        usedWidth = usedWidth - column.getWidth();
        // System.out.println ("  Column pruned");
      } else {
        i++;
        // System.out.println ("  Column retained");
      }
    } // end while counting column widths and pruning columns that won't fit
    
    return displayColumns;
  } // end getDisplayColumns method
  
} // end ToDoColumns class
