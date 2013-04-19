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

package com.powersurgepub.twodue.disk;

/**
   Holding tank for disk store yet to be identified.
 */
public class TwoDueDiskUnknown 
    extends TwoDueDiskStore {
  
  /** Creates a new instance of TwoDueDiskUnidentified */
  public TwoDueDiskUnknown() {
    // System.out.println ("Constructing new TwoDueUnknown");
  }
  
  /**
    Indicates whether this is an instance of TwoDueUnknown.
   
    @return true if this is an instance of TwoDueUnknown.
   */
  public boolean isUnknown() {
    return true;
  }
  
  public boolean isAFile() {
    return false;
  }
  
  public boolean isAFolder() {
    return false;
  }
  
  public String getShortPath() {
    return "???/???";
  }
  
}
