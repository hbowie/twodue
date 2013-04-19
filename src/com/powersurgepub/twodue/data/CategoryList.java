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

  import com.powersurgepub.psdatalib.ui.ValueList;
  import com.powersurgepub.psutils.*;

/**
   A collection of values that are used as categories for to do items. 
   New values are added to the list. The
   list is maintained in alphabetical order. A JComboBox is maintained
   and kept synchronized with the list.
 */
public class CategoryList 
    extends ValueList 
        implements ItemsView {
  
  /** Creates a new instance of CategoryList */
  public CategoryList() {
  }
  
  public void add(ToDoItem item) {
    registerValue (item.getTags().toString());
  }
  
  public void modify(ToDoItem item) {
    registerValue (item.getTags().toString());
  }
  
  public void remove(ToDoItem item) {
    // No need to do anything
  }
  
  public void setItems(ToDoItems items) {
    // No need to save this for anything
  }
  
	/**
	   Returns the object in string form.
	  
	   @return Name of this class.
	 */
	public String toString() {
    return "CategoryList";
	}
  
}
