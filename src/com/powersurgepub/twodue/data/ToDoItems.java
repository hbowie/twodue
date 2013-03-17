package com.powersurgepub.twodue.data;

import com.powersurgepub.psdatalib.pstags.TagsList;
import com.powersurgepub.psdatalib.pstags.TagsModel;
import com.powersurgepub.psdatalib.pstags.TaggableList;
import com.powersurgepub.psdatalib.txbio.XMLRecordWriter;
import com.powersurgepub.psdatalib.psdata.RecordDefinition;
import com.powersurgepub.psdatalib.psdata.XMLParser3;
import com.powersurgepub.psdatalib.psdata.DataDictionary;
import com.powersurgepub.psdatalib.psdata.DataRecord;
import com.powersurgepub.psdatalib.psdata.DataSource;
import com.powersurgepub.psdatalib.psdata.DataStore;
  import com.powersurgepub.psutils.*;
  import com.powersurgepub.regcodes.*;
  import com.powersurgepub.twodue.*;
  import com.powersurgepub.twodue.disk.*;
  import com.powersurgepub.xos2.*;
  import java.io.*;
  import java.text.*;
  import java.util.*;
  import javax.swing.*;
  import javax.swing.table.*;
  
/**
   A collection of to do items. New items are added to the end of the
   table. Deleted records are flagged as deleted, but are not removed
   from the table. An index pointing to an item in the table should always
   point to the same item (it should not move).<p>
  
   This code is copyright (c) 2003 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
    2003/11/09 - Originally written.
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 
    2004/06/27 - Added code to check item recurrence on an item add, 
                 in case the item is closed.
 */

public class ToDoItems 
  implements 
    DataSource, 
    TaggableList {
      
  private ImageIcon       lateIcon = new ImageIcon();
  private ImageIcon       todayIcon;
  private ImageIcon       tomorrowIcon;
  private ImageIcon       futureIcon;
      
  private ArrayList       items;
  
  private int             itemIndex = 0;
  
  private RecordDefinition recDef;
  
  private int             dsIndex = 0;
  
  private Debug           debug   = new Debug(false);
  
  private Logger          logger     = new Logger();
  
  private ArrayList       views;
  
  private TagsList        tagsList = new TagsList();
  
	private TagsModel       tagsModel = new TagsModel();
  
  private TwoDueCommon    td;
  
  private RegisterWindow  registerWindow = null;

	/**
	   Constructor with minimal arguments.
   
     @param td    Instance of TwoDueCommon that can be used 
                  to anchor alert dialogs.
	 */
	public ToDoItems (TwoDueCommon td) {
    this.td = td;
    recDef = ToDoItem.getRecDef();
		items = new ArrayList();
    views = new ArrayList();
    tagsList.registerValue("");
    registerWindow = RegisterWindow.getShared();
	}
  
	/**
	   Constructor with data source argument.
     
     @param fmt   A DateFormat instance to be used to parse date strings.
     @param td    Instance of TwoDueCommon that can be used 
                  to anchor alert dialogs.
     @param items A collection of items to be added to this collection. 
	 */
	public ToDoItems (IDListHeader header, DateFormat fmt, TwoDueCommon td, 
      DataSource items) 
        throws IOException, RegistrationException {
    this.td = td;
    recDef = ToDoItem.getRecDef();
    this.items = new ArrayList();
    views = new ArrayList();
		addAll (fmt, items);
	}
  
  /**
    Add another view of the data to be kept synchronized with the 
    underlying collection of ToDoItems.
   
    @param view Another ItemsView to be added.
   */
  public void addView (ItemsView view) {
    boolean ok = views.add (view);
    view.setItems (this);
    for (int i = 0; i < items.size(); i++) {
      view.add ((ToDoItem)items.get(i));
    }
  }
  
  public void setDueDateIcons 
      (ImageIcon late, ImageIcon today, ImageIcon tomorrow, ImageIcon future) {
    lateIcon = late;
    todayIcon = today;
    tomorrowIcon = tomorrow;
    futureIcon = future;
  }
  
  /**
    Adds another collection of items to this collection.
    
    @return True if the items were added successfully.
    @param  fmt   A DateFormat instance to be used to parse dates.
    @param  items A collection of items to be added to this collection.
   */
  public boolean addAll (DateFormat fmt, DataSource items) 
      throws IOException, RegistrationException {
    boolean ok = true;
    DataRecord next;
    int added = 0;
    int addedAt;
    items.openForInput();
    XMLParser3 xmlParser = null;
    Class dataSourceClass = items.getClass();
    if (dataSourceClass.getSimpleName().equals("XMLParser3")) {
      xmlParser = (XMLParser3)items;
    } 
    
    while ((! items.isAtEnd())
        && (roomForMore())) {
      next = items.nextRecordIn();
      // System.out.println ("ToDoItems.addAll size = "
      //     + String.valueOf(size())
      //     + " roomForMore = " + String.valueOf(roomForMore()));
      if (next != null
          && (xmlParser == null
              || xmlParser.getRecTag().equals(TwoDueDiskStore.ITEM_TAG))) {
        ToDoItem newItem = new ToDoItem (fmt, next);
        if (newItem.isDueToday()
            || newItem.isLate()) {
          // ??
        }
        addedAt = add (newItem);
        if (addedAt < 0) {
          ok = false;
        } else {
          added++;
        }
      } // end if next data rec not null
    } // end while more items in data source
    if (! items.isAtEnd()) {
      next = items.nextRecordIn();
      if (next != null) {
        throw new RegistrationException ("Some Records Skipped");
      }
    }
    Logger.getShared().recordEvent(LogEvent.NORMAL, 
        "Added " + String.valueOf(added) + " items", false);
    return ok;
  } // end addAll method

  /**
    Adds another to do item to the collection.
    
    @return Index position of item after add.
    
    @param item The item to be added to the collection.
   */
  public int add (ToDoItem item) 
      throws RegistrationException {
    // System.out.println ("ToDoItems.add size = "
    //       + String.valueOf(size())
    //       + " roomForMore = " + String.valueOf(roomForMore()));
    if (item.getTitle().length() == 0) {
      System.out.println("Tried to add item with blank title");
      return -1;
    } else {
      if (roomForMore()) {
        // Don't need to actually add the newItem, but need to go 
        // through the motions in order to get a valid status in
        // case the user is adding an item with a closed status.
        ToDoItem newItem = item.recurIfClosed();
        boolean ok = items.add (item);
        if (ok) {
          int index = items.size() - 1;
          item.setItemNumber (index);
          item.setCommon (td);

          tagsList.add  (item);
          tagsModel.add (item);

          // Update Views
          for (int v = 0; v < views.size(); v++) {
            ItemsView view = (ItemsView)views.get(v);
            view.add (item);
          }
          return index;
        } else {
          // Collection add failed
          return -1;
        }
      } else {
        // Too many recs for unregistered user
        throw new RegistrationException ("Maximum Number of Records Reached");
      }
    }
  } // end method
  
  /**
   Can more records/items be added without exceeding the demo limitation?

   @return     True if more can be added, false if we've hit the ceiling.
   */
  public boolean roomForMore() {
    return registerWindow.roomForMore(size());
  }
  
  /**
    Indicates that an item in the list has been modified.
    
    @param item The item being modified.
   */
  public void modify (ToDoItem item) 
      throws RegistrationException {
    ToDoItem newItem = item.recurIfClosed();
    tagsList.modify  (item);
		tagsModel.modify (item);
    for (int v = 0; v < views.size(); v++) {
      ItemsView view = (ItemsView)views.get(v);
      view.modify (item);
    }
    if (newItem != null) {
      int addedAt = add (newItem);
    }
  } // end method
  
  public void remove (ToDoItem item) {
    
    item.setDeleted();
    tagsModel.remove (item);
		tagsList.remove (item);
    for (int v = 0; v < views.size(); v++) {
      ItemsView view = (ItemsView)views.get(v);
      view.remove (item);
    }
  }
  
  /**
    Gets this collection of items and passes it to a data store, dropping
    deleted items.
    
    @return True if the items were stored successfully.
    @param items A data store to receive the collection.
   */
  public boolean getAll (DataStore dataOut) 
      throws IOException {
        
    boolean ok = true;
        
    // Build the record definition
    try {
      dataOut.openForOutput (recDef);
    } catch (IOException e) {
      ok = false;
    }
    
    if (ok) {
      if (dataOut.getClass().getSimpleName().equals("XMLRecordWriter")) {
        XMLRecordWriter xmlWriter = (XMLRecordWriter)dataOut;
        xmlWriter.setRecTag(TwoDueDiskStore.ITEM_TAG);
      }
      // Create a date formatter
      DateFormat fmt = new SimpleDateFormat ("MM/dd/yyyy"); 
    
      ToDoItem nextItem;
      for (itemIndex = 0; itemIndex < size(); itemIndex++) {
        nextItem = get (itemIndex);
        if (! nextItem.isDeleted()) {
          DataRecord nextRec = nextItem.getDataRec (recDef);
          try {
            dataOut.nextRecordOut (nextRec);
          } catch (IOException e2) {
            ok = false;
          }
        } // end if not deleted
      } // end for loop
    } // end if open ok
    dataOut.close();
    return ok;
  } // end getAll method
  
  /**
    Gets this collection of items and passes it to a data store, dropping
    deleted items, and splitting tags.
    
    @return True if the items were stored successfully.
    @param items A data store to receive the collection.
   */
  public boolean getAllForEachTag (DataStore dataOut) 
      throws IOException {
        
    boolean ok = true;
        
    try {
      dataOut.openForOutput (recDef);
    } catch (IOException e) {
      ok = false;
    }
    
    if (ok) {
      if (dataOut.getClass().getSimpleName().equals("XMLRecordWriter")) {
        XMLRecordWriter xmlWriter = (XMLRecordWriter)dataOut;
        xmlWriter.setRecTag(TwoDueDiskStore.ITEM_TAG);
      }
      // Create a date formatter
      DateFormat fmt = new SimpleDateFormat ("MM/dd/yyyy"); 
    
      ToDoItem nextItem;
      for (itemIndex = 0; itemIndex < size(); itemIndex++) {
        nextItem = get (itemIndex);
        if (! nextItem.isDeleted()) {
          int tagIndex = 0;
          DataRecord nextRec = nextItem.getDataRec (recDef, tagIndex);
          while (nextRec != null) {
            try {
              dataOut.nextRecordOut (nextRec);
            } catch (IOException e2) {
              ok = false;
            }
            tagIndex++;
            nextRec = nextItem.getDataRec (recDef, tagIndex);
          } // end while nextRec not null
        } // end if not deleted
      } // end for loop
    } // end if open ok
    dataOut.close();
    return ok;
  } // end getAll method
  
  public int getItemIndex() {
    return itemIndex;
  }
  
   /**
    Gets a to do item from the collection.
    
    @return To do item.
    
    @param index The index to the desired position in the collection.
   */
  public ToDoItem get (int index) {
    return (ToDoItem)items.get (index);
  }
  
   /**
    Returns the size of the collection.
    
    @return Size of the collection.
   */
  public int size () {
    return items.size();
  }
  
	public TagsList getTagsList () {
		return tagsList;
	}

	public TagsModel getTagsModel () {
		return tagsModel;
	}
  
  public void sortTagsModel() {
    tagsModel.sort(this);
  }
	
	/**
	   Returns the object in string form.
	  
	   @return object formatted as a string
	 */
	public String toString() {
    return "ToDoItems";
	}
  
  /*
    The following methods allow an object of this class to act
    as a DataSource.
   */
  
  /**
     Opens the reader for input.
    
     @param inDict A data dictionary to use.
    
     @throws IOException If there is trouble opening a disk file.
   */
  public void openForInput (DataDictionary inDict)
      throws IOException {
    dsIndex = 0;
  }
      
  /**
     Opens the reader for input.
    
     @param inRecDef A record definition to use.
    
     @throws IOException If there is trouble opening a disk file.
   */
  public void openForInput (RecordDefinition inRecDef)
      throws IOException {
    dsIndex = 0;
  }
  
  /**
     Opens the reader for input.
    
     @throws IOException If there is trouble opening a disk file.
   */
  public void openForInput ()
      throws IOException {
    dsIndex = 0;
  }
  
  /**
     Returns the next input data record.
    
     @return Next data record.
    
     @throws IOException If reading from a source that might generate
                         these.
   */
  public DataRecord nextRecordIn () 
      throws IOException {
    if (isAtEnd()) {
      return null;
    } else {
      ToDoItem item;
      boolean goodRec = false;
      do {
        item = (ToDoItem)items.get (dsIndex);
        if (! item.isDeleted()) {
          goodRec = true;
        }
        dsIndex++;
      } while ((! isAtEnd()) && (item.isDeleted()));
      if (goodRec) {
        return item.getDataRec(recDef);
      }
    }
    return null;
  }
    
  /**
     Returns the record definition for the reader.
    
     @return Record definition.
   */
  public RecordDefinition getRecDef () {
    return recDef;
  }
  
  /**
     Returns the sequential record number of the last record returned.
    
     @return Sequential record number of the last record returned via 
             nextRecordIn, where 1 identifies the first record.
   */
  public int getRecordNumber () {
    return dsIndex;  
  }

  /**
     Indicates whether there are more records to return.
    
     @return True if no more records to return.
   */
  public boolean isAtEnd() {
    return (dsIndex >= items.size());
  }
  
  /**
     Closes the reader.
    
     @throws IOException If there is trouble closing the file.
   */
  public void close () 
      throws IOException {
  }
    
  /**
     Sets a log to be used by the reader to record events.
    
     @param  logger A logger object to use.
   */
  public void setLog (Logger logger) {
    this.logger = logger;
  }
  
  /**
     Sets the debug instance to the passed value.
    
     @param debug Debug instance. 
   */
  public void setDebug (Debug debug) {
    this.debug = debug;
  }
  
  /**
     Indicates whether all data records are to be logged.
    
     @param  dataLogging True if all data records are to be logged.
   */
  public void setDataLogging (boolean dataLogging) {
    logger.setLogAllData (dataLogging);
  }
  
  /**
     Sets a file ID to be used to identify this reader in the log.
    
     @param  fileId An identifier for this reader.
   */
  public void setFileId (String fileId) {
    
  }
  
  /**
     Sets the maximum directory explosion depth.
    
     @param maxDepth Desired directory/sub-directory explosion depth.
   */
  public void setMaxDepth (int maxDepth) {
  }
  
  /**
     Retrieves the path to the original source file (if any).
    
     @return Path to the original source file (if any).
   */
  public String getDataParent () {
    return "";
  }
  
  /**
    Close up shop for this collection. Stops any alerts that may have been
    set.

   */
  public void shutDown () {
        
    ToDoItem nextItem;
    for (itemIndex = 0; itemIndex < size(); itemIndex++) {
      nextItem = get (itemIndex);
      nextItem.stopAlert();
    } // end for loop

  } // end shutDown method
  
} // end of class

