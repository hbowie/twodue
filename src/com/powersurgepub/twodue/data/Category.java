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

  import com.powersurgepub.psutils.*;
  import java.util.*;

/**
   An object representing the category of a To Do item. A 
   category may consist of multiple levels, with the first
   level being the primary category and subsequent levels being
   sub-categories. 
 */
public class Category {
  
  /** Preferred separator character. */
  public char     PREFERRED_SEPARATOR = '.';
  
  /** Alternate separator character. */
  public char     ALTERNATE_SEPARATOR = '/';
  
  /** Maximum allowable number of category levels. */
  public int      LEVEL_MAX = 10;
  
  /** 
    The normalized representation of zero or more nested categories,
    with periods separating each level, and with no spaces surrounding
    the periods.
   */
  private String category = "";
  
  /** The number of levels in the category string. */
  private int levels = 0;
  
  /** The starting position in the category string for each level. */
  private ArrayList levelStart;
  
  /** 
    Creates a new instance of Category with null values. 
   */
  public Category() {
    set ("");
  }
  
  /** 
    Creates a new instance of Category with a particular value.
   
    @param category A string containing one or more nested categories.
                    Levels may be separated by periods or slashes, 
                    and spaces may separate the periords or slashes from
                    the words.
   */
  public Category (String category) {
    set (category);
  }
  
  /** 
    Sets the category to a particular value.
   
    @param inCat    A string containing one or more nested categories.
                    Levels may be separated by periods or slashes, 
                    and spaces may separate the periords or slashes from
                    the words.
   */
  public void set(String inCat) {
    levels = 0;
    levelStart = new ArrayList();
    String category = StringUtils.purify(inCat).trim();
    StringBuffer workCat = new StringBuffer();
    int i = 0;
    int j = 0;
    while (i < category.length()) {

      // skip leading spaces
      while (i < category.length() 
          && category.charAt(i) == ' ') {
        i++;
      } // end while next char is a space
      
      if (i < category.length()) {
        levels++;
        if (j > 0) {
          workCat.append (PREFERRED_SEPARATOR);
          j++;
        }
        levelStart.add (new Integer(j));
      }

      // extract next category level
      while (i < category.length() 
          && category.charAt(i) != ALTERNATE_SEPARATOR
          && category.charAt(i) != PREFERRED_SEPARATOR) {
        workCat.append (category.charAt(i));
        i++;
        j++;
      } // end of next category level

      // eliminate any trailing spaces
      while (workCat.length() > 0
          && workCat.charAt (workCat.length() - 1) == ' ') {
        workCat.deleteCharAt (workCat.length() - 1);
        j--;
      }

      // Position pointer beyond delimiter that stopped our scan
      if (i < category.length()) {
        i++;
      }

    } // end while more category levels to extract
    
    this.category = workCat.toString();
    
    setClosingLevelStart();
  }
  
  /**
    Set one level. Note that this is only allowed if the
    level to be set is a higher number than any previously
    set levels. The bottom line is that this method may be used
    to set one level at a time, but only when the category is initially
    being populated, and only when the levels are set in ascending
    sequence by level number. 
   
    @param  inSubCat Category string at given level.
   
    @param  level    Level at which category is to be set, with
                     zero indicating the first level.
   */
  public void setLevel (String inSubCat, int level) {
    String subCat = StringUtils.purify(inSubCat).trim();
    if (level >= levels
        && level >= 0
        && level < LEVEL_MAX
        && subCat.length() > 0) {
      StringBuffer workCat = new StringBuffer(category);
      while (levels <= level) {
        levels++;
        if (level > 0) {
          workCat.append (PREFERRED_SEPARATOR);
        }
        Integer lastLevelStart = (Integer)levelStart.get(levelStart.size() - 1);
        if (workCat.length() > lastLevelStart.intValue()) {
          levelStart.add (new Integer(workCat.length()));
        }
      }
      workCat.append (subCat);
      category = workCat.toString();
      setClosingLevelStart();
    }
  }
  
  /**
    Set one final level start to give us an ending point
   */
  private void setClosingLevelStart() {
    int sepLength = 0;
    if (this.category.length() > 0) {
      sepLength = 1;
    }
    levelStart.add (new Integer(this.category.length() + sepLength));
    
  } // end set method
  
  /**
    Return the number of levels in the category.
   
    @return Number of levels in this category.
   */
  public int getLevels() {
    return levels;
  }
  
  /**
    Return the category string for a particular level.
   
    @return Category string at given level.
   
    @param  level Level at which category is desired, with
                  zero indicating the first level, or an empty string
                  if the requested level is invalid.
   */
  public String getLevel (int level) {
    if (level < 0 || level >= levels) {
      return "";
    } else {
      Integer start = (Integer)levelStart.get(level);
      int s = start.intValue();
      Integer end = (Integer)levelStart.get(level + 1);
      int e = end.intValue() - 1;
      if ((e - s - 1) > 0) {
        return category.substring (s, e);
      } else {
        return "";
      }
    }
  }
  
  /**
   Return all levels through the specified level, concatenated,
   with periods separating the levels.
   
   @return Concatenated categories through the specified level. 
           An empthy string will be returned if the number of levels
           requested is less than zero.
   
   @param level The highest level for which categories are requested,
                with zero indicating the first.
   */
  public String getLevelsThru (int level) {
    if (level < 0 ) {
      return "";
    }
    else
    if (level >= levels) {
      return category;
    } 
    else {
      Integer end = (Integer)levelStart.get(level + 1);
      int e = end.intValue();
      if ((e - 1) > 0) {
        return category.substring (0, e);
      } else {
        return "";
      } // end if length is null
    } // end if valid levels
  } // end method
  
  /**
    Determines if this category is essentially equal to another
    category. The two categories may be different objects.
   
    @return True if the two category strings are equal, ignoring upper-
            or lower- case considerations.
   
    @param  cat2 Second Category to be compared to this one.
   */
  public boolean equals (Category cat2) {
    return (compareTo(cat2) == 0);
  }
  
  /**
    Compares this category to another category, using a non-case-sensitive
    comparison of their two normalized category strings as the basis
    for the comparison. 
   
    @return Zero if the two categories are equal; 
            < 0 if this category is less than the second category; or 
            > 0 if this category is greater than the second category.
   
    @param  cat2 Second Category to be compared to this one.
   */
  public int compareTo (Category cat2) {
    return category.toString().compareToIgnoreCase (cat2.toString());
  }
  
  /**
    Return the normalized category string for this object.
   
    @return The normalized category string, with periods separating levels,
            and with no spaces surrounding the periods.
   */
  public String toString() {
    return category;
  }
  
}
