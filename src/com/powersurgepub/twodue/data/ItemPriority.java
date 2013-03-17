package com.powersurgepub.twodue.data;

/**
   An object representing the priority of a To Do item. <p>
  
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
  
   @version 2004/06/08 - Added equals method. 
 */
public class ItemPriority {
  
  private int priority = 3;
  
  /** 
   Creates a new instance of ItemPriority. 
   */
  public ItemPriority() {
  }
  
  /** 
   Creates a new instance of ItemPriority. 
   */
  public ItemPriority(int priority) {
    setPriority (priority);
  }
  
  /** 
   Creates a new instance of ItemPriority. 
   */
  public ItemPriority(String priority) {
    setPriority (priority);
  }
  
  public void setPriority (String priorityString) {
    int priority = 3;
    try {
      priority = Integer.parseInt (priorityString);
    } catch (NumberFormatException e) {
    }
    setPriority (priority);
  }
  
  public void setPriority (int priority) {
    this.priority = priority;
  }
  
  public int getPriority () {
    return priority;
  }
  
  public boolean equals (Object object2) {
    ItemPriority priority2 = (ItemPriority) object2;
    return (priority == priority2.getPriority());
  }
  
  public String toString() {
    return String.valueOf(priority);
  }
  
}
