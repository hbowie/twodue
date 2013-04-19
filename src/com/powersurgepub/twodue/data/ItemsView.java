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
   An interface for any class that presents a view of the data stored
   in a ToDoItems list.
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
