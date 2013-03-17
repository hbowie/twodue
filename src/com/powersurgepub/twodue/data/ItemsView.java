package com.powersurgepub.twodue.data;

/**
   An interface for any class that presents a view of the data stored
   in a ToDoItems list.<p>
  
   This code is copyright (c) 2003 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 2003/11/10 - Originally written.
 */
public interface ItemsView {
  
  /**
    Save a pointer to the underlying ToDoItems collection.
   
    @param items Pointer to the ToDoItems collection.
   */
  void setItems(ToDoItems items);
  
  /**
    Process a new ToDoItem that has just been added to the ToDoItems collection.
   
    @param item   ToDoItem just added.
   */
  void add(ToDoItem item);
  
  /**
    Process a new ToDoItem that has just been modified within the ToDoItems collection.
   
    @param item   ToDoItem just modified.
   */
  void modify(ToDoItem item);
  
  /**
    Process a ToDoItem that has just been logically deleted from the ToDoItems collection.
   
    @param item   ToDoItem just logically deleted (by setting Deleted flag).
   */
  void remove(ToDoItem item);
  
}
