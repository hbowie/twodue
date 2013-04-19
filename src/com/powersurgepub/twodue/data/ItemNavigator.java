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

  import com.powersurgepub.twodue.*;

/**
   An interface for any class that allows navigation through an ordered
   list of to do items.
 */
public interface ItemNavigator {
  
  /**
    Save a pointer to the underlying ToDoItems collection.
   
    @param items Pointer to the ToDoItems collection.
   */
  void setItems(ToDoItems items);
  
  /**
    Save a pointer to the TwoDueCommon object.
   
    @param common Pointer to the TwoDueCommon object.
   */
  void setCommon (TwoDueCommon common);
  
  void firstItem();
  
  void nextItem();
  
  void priorItem();
  
  void lastItem();
  
  void selectItem(ToDoItem item);
  
  /**
    Get the item's position within this collection.
   
    @return Current item's position within this collection, where zero points
            to the first.
   */
  public int getItemNumber ();
  
  /**
    Returns the size of the collection.
    
    @return Size of the collection.
   */
  public int size ();
  
}
