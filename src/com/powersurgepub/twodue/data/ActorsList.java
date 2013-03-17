package com.powersurgepub.twodue.data;

  import com.powersurgepub.psdatalib.ui.ValueList;

/**
   A collection of values that items are assigned to, or assigned by. 
   New values are added to the list. The
   list is maintained in alphabetical order. A JComboBox is maintained
   and kept synchronized with the list.<p>
  
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
public class ActorsList 
    extends ValueList 
        implements ItemsView {
  
  /** 
    Creates a new instance of ActorsList 
   */
  public ActorsList() {
    super();
  }
  
  public void add (ToDoItem item) {
    registerValue (item.getAssignedTo());
  }
  
  public void modify(ToDoItem item) {
    registerValue (item.getAssignedTo());
  }
  
  public void remove(ToDoItem item) {
    // No need to do anything
  }
  
  public void setItems(ToDoItems items) {
    // Do nothing -- we don't need to keep this for anything. 
  }
  
	/**
	   Returns the object in string form.
	  
	   @return Name of this class.
	 */
	public String toString() {
    return "ActorsList";
	}
  
}
