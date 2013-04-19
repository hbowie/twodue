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

/**
   An object representing the priority of a To Do item. 
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
