package com.powersurgepub.twodue.data;

  import com.powersurgepub.psdatalib.psdata.RecordDefinition;
  import com.powersurgepub.twodue.*;

/**
   A comparator for a To Do item. Given a set of sort fields,
   will determine whether a particular To Do item is less than, greater than,
   or equal to, another To Do item. <p>
  
   This code is copyright (c) 2003 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
      2003/11/09 - Added Category field. <li>
      2003/08/31 - Originally written.
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 2004/07/01 - Added Undated High/Low option. 
 */
public class ItemComparator
      implements java.util.Comparator {
        
  public static final String SHOW           = "show";
  public static final String FIELD1         = "field1";
  public static final String SEQ1           = "seq1";
  public static final String FIELD2         = "field2";
  public static final String SEQ2           = "seq2";
  public static final String FIELD3         = "field3";
  public static final String SEQ3           = "seq3";
  public static final String FIELD4         = "field4";
  public static final String SEQ4           = "seq4";
  public static final String FIELD5         = "field5";
  public static final String SEQ5           = "seq5";
  public static final String UNDATED        = "undated";
        
  public static final String ASCENDING  = "ascending";
  public static final String DESCENDING = "descending";
  public static final String HIGH       = "high";
  public static final String LOW        = "low";
        
  private ToDoItems     items;
        
  private ItemSelector  select = new ItemSelector();
  
  private String[]      sortFields    
      = { ToDoItem.COLUMN_DISPLAY [ToDoItem.STATUS], 
          ToDoItem.COLUMN_DISPLAY [ToDoItem.DUE_DATE], 
          ToDoItem.COLUMN_DISPLAY [ToDoItem.PRIORITY], 
          ToDoItem.COLUMN_DISPLAY [ToDoItem.ASSIGNED_TO], 
          ToDoItem.COLUMN_DISPLAY [ToDoItem.FILE_LENGTH],
          ToDoItem.COLUMN_DISPLAY [ToDoItem.LAST_MOD_DATE],
          ToDoItem.COLUMN_DISPLAY [ToDoItem.SEQUENCE],
          ItemType.COLUMN_DISPLAY_NAME,
          ToDoItem.COLUMN_DISPLAY [ToDoItem.TITLE], 
          ToDoItem.COLUMN_DISPLAY [ToDoItem.TAGS],
          ToDoItem.COLUMN_DISPLAY [ToDoItem.START_TIME],
          ToDoItem.COLUMN_DISPLAY [ToDoItem.DESCRIPTION],
          ToDoItem.COLUMN_DISPLAY [ToDoItem.OUTCOME],
          ToDoItem.COLUMN_DISPLAY [ToDoItem.WEB_PAGE],
          ToDoItem.COLUMN_DISPLAY [ToDoItem.RECURS_EVERY],
          ToDoItem.COLUMN_DISPLAY [ToDoItem.RECURS_UNIT],
          ToDoItem.COLUMN_DISPLAY [ToDoItem.RECURS_DAY_OF_WEEK],
          ToDoItem.COLUMN_DISPLAY [ToDoItem.RECURS_WITHIN_MONTH],
          ToDoItem.COLUMN_DISPLAY [ToDoItem.ID]};
          
  private boolean       undatedHigh = false;
          
  private int[]         sortSeqs = 
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
          
  private int index = 0;
  
  private int nextIndex = -1;
  
  /** Creates a new instance of ItemComparator */
  public ItemComparator() {
    
  }
  
  /*
  public void setSelector (ItemSelector select) {
    this.select = select;
  }
   */
  
  /**
    Sets the underlying collection of ToDoItems to be used by this object.
   
    @param items Underlying collection of ToDoItems (unsorted).
   */
  public void setItems(ToDoItems items) {
    this.items = items;
  }
  
  /**
    Set all of the comparator's options from a single string.
   
    @param bundle All of this comparator's options, bundled into a single string,
                  using commas to separate fields, and enclosed in parentheses.
   */
  public void set (String bundle) {
    nextIndex = -1;
    setSortFields 
       (nextField (bundle),
        nextField (bundle),
        nextField (bundle),
        nextField (bundle),
        nextField (bundle),
        nextField (bundle),
        nextField (bundle),
        nextField (bundle),
        nextField (bundle),
        nextField (bundle),
        nextField (bundle),
        nextField (bundle));
    while (moreChars (bundle)) {
      String type = nextField (bundle);
      select.setSelectType (type);
    }
  }
  
  /**
    Extract the next value from the bundle. 
   
    @param bundle A single string containing all the options for this comparator. 
   */
  private String nextField (String bundle) {
    char c = nextChar (bundle);
    while (moreChars (bundle)
        && (c == ',' || c == '(' || c == ' ')) {
      c = nextChar (bundle);
    }
    StringBuffer field = new StringBuffer();
    while (moreChars (bundle)
        && c != ',' && c != ')') {
      field.append (c);
      c = nextChar (bundle);
    }
    return field.toString();
  }
  
  /**
    Extract the next character from the bundle of options.
   
    @param bundle A single string containing all the options for this comparator.
   */
  private char nextChar (String bundle) {
    char c = ')';
    nextIndex++;
    if (moreChars (bundle)) {
      c = bundle.charAt (nextIndex);
    } 
    return c;
  }
  
  /**
    Are there more characters left in the bundle?
   
    @param bundle A single string containing all the options for this comparator.
   */
  private boolean moreChars (String bundle) {
    return (nextIndex < bundle.length());
  }
  
  public void setSortFields 
      (String showOption,
       String sort1Field, String sort1Seq,
       String sort2Field, String sort2Seq,
       String sort3Field, String sort3Seq,
       String sort4Field, String sort4Seq,
       String sort5Field, String sort5Seq,
       String undatedHigh) {
    select.setSelectOption (showOption);
    index = 0;
    // if (! showOption.equals ("All")) {
    //   setNextSortField (ToDoItem.COLUMN_DISPLAY [ToDoItem.STATUS], 
    //       ViewTab.ASCENDING);
    // } 
    for (int i = 0; i < sortFields.length; i++) {
      sortSeqs [i] = 1;
    }
    setNextSortField (sort1Field, sort1Seq);
    setNextSortField (sort2Field, sort2Seq);
    setNextSortField (sort3Field, sort3Seq);
    setNextSortField (sort4Field, sort4Seq);
    setNextSortField (sort5Field, sort5Seq);
    setUndated (undatedHigh);
  }
  
  private void setUndated (String undatedHighString) {
    if (undatedHighString.equalsIgnoreCase (HIGH)) {
      undatedHigh = true;
    }
    else
    if (undatedHighString.equalsIgnoreCase (LOW)) {
      undatedHigh = false;
    }
  }
  
  private void setNextSortField (String sortField, String seq) {
    
    // Ignoring fields already set, look for desired field in table
    int i = index;
    while (i < sortFields.length
        && (! sortField.equals (sortFields [i]))) {
      i++;
    }
    
    // If we found it, and it is currently lower on list, move it up
    if (i < sortFields.length) {
      while (i > index) {
        sortFields [i] = sortFields [i - 1];
        i--;
      }
      sortFields [index] = sortField;
      if (seq.substring(0,1).equalsIgnoreCase("d")) {
        sortSeqs [index] = -1;
      } else {
        sortSeqs [index] = 1;
      }
      index++;
    } // end if sortField found
  }
  
  /**
    Compare two ToDoItem objects and determine which is lower in a sort sequence,
    based on sort fields supplied by the user.
   
    @result <0 if the first item is less than the second, or
             0 if the two items are equal on all significant keys, or 
            >0 if the first item is great than the second.
    @param obj  First to do item to be compared.
    @param obj1 Second to do item to be compared. 
   */
  public int compare(Object obj, Object obj1) 
      throws ClassCastException {
    ToDoItem item1 = (ToDoItem)obj;
    ToDoItem item2 = (ToDoItem)obj1;

    int c = 0;
    if (select.selected (item1)
        && (! select.selected (item2))) {
      c = -1;
    }
    else
    if (select.selected (item2)
        && (! select.selected (item1))) {
      c = 1;
    }

    int i = 0;
    String sortField;
    int sortSeq;
    while (c == 0 && i < sortFields.length) {
      sortField = sortFields [i];
      sortSeq = sortSeqs [i];
      if (sortField.equals (ToDoItem.COLUMN_DISPLAY [ToDoItem.STATUS])) {
        // Open items sort before closed
        if ((item1.isNotDone()) && item2.isDone()) {
          c = -1;
        } 
        else 
        if (item1.isDone() && (item2.isNotDone())) {
          c = 1;
        }
      }
      else
      if (sortField.equals (ToDoItem.COLUMN_DISPLAY [ToDoItem.DUE_DATE])) {
        c = item1.getDueDateYMD(undatedHigh).compareTo 
            (item2.getDueDateYMD(undatedHigh));
      }
      else 
      if (sortField.equals (ToDoItem.COLUMN_DISPLAY [ToDoItem.PRIORITY])) {
        if (item1.getPriority() < item2.getPriority()) {
          c = -1;
        }
        else
        if (item1.getPriority() > item2.getPriority()) {
          c = 1;
        }
      }
      else
      if (sortField.equals (ToDoItem.COLUMN_DISPLAY [ToDoItem.SEQUENCE])) {
        c = item1.getSequence().compareToIgnoreCase (item2.getSequence());
      }
      else
      if (sortField.equals (ToDoItem.COLUMN_DISPLAY [ToDoItem.TITLE])) {
        c = item1.getTitle().compareToIgnoreCase (item2.getTitle());
      }
      else
      if (sortField.equals (ToDoItem.COLUMN_DISPLAY [ToDoItem.TAGS])) {
        c = item1.getTags().compareTo (item2.getTags());
      }
      else
      if (sortField.equals (ToDoItem.COLUMN_DISPLAY [ToDoItem.START_TIME])) {
        c = item1.getStartTime().compareTo (item2.getStartTime());
      }
      else
      if (sortField.equals (ToDoItem.COLUMN_DISPLAY [ToDoItem.DESCRIPTION])) {
        c = item1.getDescription().compareToIgnoreCase (item2.getDescription());
      }
      else
      if (sortField.equals (ToDoItem.COLUMN_DISPLAY [ToDoItem.OUTCOME])) {
        c = item1.getOutcome().compareToIgnoreCase (item2.getOutcome());
      }
      else
      if (sortField.equals (ToDoItem.COLUMN_DISPLAY [ToDoItem.WEB_PAGE])) {
        c = item1.getWebPage().compareToIgnoreCase (item2.getWebPage());
      }
      else
      if (sortField.equals (ToDoItem.COLUMN_DISPLAY [ToDoItem.ASSIGNED_TO])) {
        c = item1.getAssignedTo().compareToIgnoreCase (item2.getAssignedTo());
      }
      else
      if (sortField.equals (ToDoItem.COLUMN_DISPLAY [ToDoItem.FILE_LENGTH])) {
        if (item1.getFileLength() < item2.getFileLength()) {
          c = -1;
        }
        else
        if (item1.getFileLength() > item2.getFileLength()) {
          c = 1;
        }
      }
      else
      if (sortField.equals (ToDoItem.COLUMN_DISPLAY [ToDoItem.LAST_MOD_DATE])) {
        c = item1.getLastModDate().compareTo (item2.getLastModDate());
      }
      else
      if (sortField.equals (ToDoItem.COLUMN_DISPLAY [ToDoItem.RECURS_EVERY])) {
        if (item1.getRecursEvery() < item2.getRecursEvery()) {
          c = -1;
        }
        else
        if (item1.getRecursEvery() > item2.getRecursEvery()) {
          c = 1;
        }
      }
      else
      if (sortField.equals (ToDoItem.COLUMN_DISPLAY [ToDoItem.RECURS_UNIT])) {
        if (item1.getRecursUnit() < item2.getRecursUnit()) {
          c = -1;
        }
        else
        if (item1.getRecursUnit() > item2.getRecursUnit()) {
          c = 1;
        }
      }
      else
      if (sortField.equals (ToDoItem.COLUMN_DISPLAY [ToDoItem.RECURS_DAY_OF_WEEK])) {
        c = compareInts (item1.getRecursDayOfWeek(), item2.getRecursDayOfWeek());
      }
      else
      if (sortField.equals (ToDoItem.COLUMN_DISPLAY [ToDoItem.RECURS_WITHIN_MONTH])) {
        c = compareInts (item1.getRecursWithinMonth(), item2.getRecursWithinMonth());
      }
      else
      if (sortField.equals (ItemType.COLUMN_DISPLAY_NAME)) {
        c = compareInts (item1.getType(), item2.getType());
      }
      /*
      else
      if (sortField.equals (ToDoItem.COLUMN_DISPLAY [ToDoItem.ID])) {
        int j = 0;
        while ((c == 0) && (j < item1.getIDComponentsSize())) {
          c = item1.getIDComponentAsInteger(j).compareTo 
              (item2.getIDComponentAsInteger(j));
          j++;
        }
        if (c == 0) {
          if (item1.getIDComponentsSize() < item2.getIDComponentsSize()) {
            c = -1;
          }
          else
          if (item2.getIDComponentsSize() < item1.getIDComponentsSize()) {
            c = 1;
          }
        }
      }
      */
      if ((c != 0) && (sortSeq < 1)) {
        c = c * sortSeq;
      }
      i++;
    } // end while still equal and more fields to go
    return c;
  }
  
  public int compareInts (int int1, int int2) {
    if (int1 < int2) {
      return -1;
    }
    else
    if (int1 > int2) {
      return +1;
    } else {
      return 0;
    }
  }
  
  /**
    Add column names for the fields defined by this class.
   
    @param recDef Record Definition to have column names added to it.
   */
  public static void addColumnNames (RecordDefinition recDef) {
    recDef.addColumn (SHOW);
    recDef.addColumn (FIELD1);
    recDef.addColumn (SEQ1);
    recDef.addColumn (FIELD2);
    recDef.addColumn (SEQ2);
    recDef.addColumn (FIELD3);
    recDef.addColumn (SEQ3);
    recDef.addColumn (FIELD4);
    recDef.addColumn (SEQ4);
    recDef.addColumn (FIELD5);
    recDef.addColumn (SEQ5);
    recDef.addColumn (UNDATED);
  }
  
  /**
    Return the options for this comparator as a single String.
   
    @return Options for this comparator bundled into a single String. The entire 
            bundle is enclosed in parentheses. Eleven fields are enclosed, with
            commas separating the fields. The first field is the Selection option.
            The remaining ten fields consist of five value pairs, one pair for each
            sort field, from most significant to least. Within each pair, the first
            value identifies the field, and the second identifies the sort sequence.
   */
  public String toString() {
    return ("(" + select.toString() + ","
        + sortFields [0] + "," + getSortSeq (0) + ","
        + sortFields [1] + "," + getSortSeq (1) + ","
        + sortFields [2] + "," + getSortSeq (2) + ","
        + sortFields [3] + "," + getSortSeq (3) + ","
        + sortFields [4] + "," + getSortSeq (4) + ","
        + getUndatedString() + ","
        + select.getTypeSelectors()
        + ")");
  }
  
  public void setSelectOption (String selectOption) {
    select.setSelectOption (selectOption);
  }
  
  public ItemSelector getSelector () {
    return select;
  }
  
  public String getSelectString() {
    return select.toString();
  }
  
  public boolean getUndated () {
    return undatedHigh;
  }
  
  public String getUndatedString () {
    return (undatedHigh ? HIGH : LOW);
  }
  
  public String getSortField (int i) {
    return sortFields [i];
  }
  
  public String getSortSeq (int i) {
    if (sortSeqs [i] > 0) {
      return ViewPrefs.ASCENDING;
    } else {
      return ViewPrefs.DESCENDING;
    }
  }
  
  /**
    Compare this comparator to another one.
   
    @return True if the two comparators have equal values.
    @param  obj2 A second comparator to compare to this one.
   */
  public boolean equals(Object obj2) {
    boolean match = true;
    ItemComparator comp2 = null;
    try {
      comp2 = (ItemComparator)obj2;
    } catch (ClassCastException e) {
      match = false;
    }
    if (! match) {
      return match;
    }
    match = (comp2.getSelector().equals (this.getSelector()));
    if (! match) {
      return match;
    }
    match = (comp2.undatedHigh == this.undatedHigh);
    if (! match) {
      return match;
    }
    for (int i = 0; ((i < sortFields.length) && (match)); i++) {
      match = ((comp2.sortFields[i].equals (this.sortFields[i]))
          && (comp2.sortSeqs[i] == this.sortSeqs[i]));
    }
    return match;
  }
  
}
