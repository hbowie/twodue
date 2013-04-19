Two Due User Guide
==================

<ul>
	<li><a href="#intro">Introduction</a></li>
  <li><a href="#sysrqmts">System Requirements</a></li>
  <li><a href="#rights">Rights</a></li>
  <li><a href="#installation">Installation</a></li>
  <li><a href="#save">Saving Your To Do List(s)</a></li>
  <li><a href="#backup">Backing Up Your To Do List(s)</a></li>
  <li><a href="#item">Creating and Modifying To Do Items</a></li>
  <li><a href="#sortlist">Viewing Your Items as a Sorted List</a></li>
  <li><a href="#tree">Managing Your Items with Tags</a></li>
  <li><a href="#recur">Using Recurring Items</a></li>
  <li><a href="#navigate">Navigating Your List</a></li>
  <li><a href="#closed">Dealing with Closed Items</a></li>
  <li><a href="#time">Using Time Fields </a></li>
  <li><a href="#foldersync">Using Folder Sync </a></li>
  <li><a href="#transfer">Transferring To Do Items </a></li>
  <li><a href="#publish">Publishing Your Lists to the Web </a></li>
  <li><a href="#creative">Creative Uses </a></li>
</ul>

<h2 id="intro">Introduction</h2>

Two Due is a powerful To Do List manager with many special features. It supports multiple lists, recurring To Do items, integration with your Web browser, multiple tags plus indented tags for each to do item. 

### Design Philosophy ###

Two Due's is something of a "contrarian" design: it intentionally does a lot of things that other To Do lists don't. The point of this is to give you, the user, some genuine choices, rather than just providing one more To Do list that looks very much like all the others out there. 

### Data Organization ###

Two Due allows you to maintain multiple lists of To Do items. You decide where you want to store each list. When you initially save a list, you must decide whether you want to save it as a stand-alone file, or as a folder of related files. Folders are recommended, since folders are more powerful than files.  

Whether you choose a folder or a file, your To Do items in that particular list will be saved in a tab-delimited text file. One of the advantages of using this format is that it is fairly universal: you can open or import such a file using many different programs, such as spreadsheet and database programs. 

### User Interface ###

Two Due has one primary window that includes multiple tabs. Most user interaction occurs within this main window. 

Two Due has a number of menus. Some of the menu items have shortcut keys, which are displayed when you select the menu. 

Two Due also has a toolbar with some common navigational controls. The initial position of the toolbar is at the top of the Two Due window, above the tabs, but you can move it to a docked position on any side of the window, or take it outside of the window altogether, by clicking on the toolbar's header and dragging it to another location. 

Two Due has a number of tabs. Each tab displays different data, and has a different function. When you first launch the program, you will find yourself on the Item tab, which has basic information about one To Do item. The List Tab will show you your entire To Do list. 

<h2 id="sysrqmts">System Requirements</h2>

Two Due is written in Java and can run on any reasonably modern operating system, including Mac OS X, Windows and Linux. Two Due requires a Java Runtime Environment (JRE), also known as a Java Virtual Machine (JVM). The version of this JRE/JVM must be at least 6. Visit [www.java.com][java] to download a recent version for most operating systems. Installation happens a bit differently under Mac OS X, but generally will occur fairly automatically when you try to launch a Java app for the first time.

Because Two Due may be run on multiple platforms, it may look slightly different on different operating systems, and will obey slightly different conventions (using the CMD key on a Mac, vs. an ALT key on a PC, for example). 

<h2 id="rights">Rights</h2>

Two Due Copyright &copy; 1999 - 2013 Herb Bowie

As of version 3.10, Two Due is [open source software][osd]. 

Licensed under the Apache License, Version 2.0 (the &#8220;License&#8221;); you may not use this file except in compliance with the License. You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an &#8220;AS IS&#8221; BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Two Due also incorporates or adapts the following open source software libraries. 

* BrowserLauncher2 &#8212; Copyright 2004 - 2007 Markus Gebhard, Jeff Chapman, used under the terms of the [GNU General Public License][gnu]. 

* JExcelAPI &#8212; Copyright 2002 Andrew Khan, used under the terms of the [GNU General Public License][gnu]. 

* parboiled  &#8212; Copyright 2009-2011 Mathias Doenitz, used under the terms of the [Apache License, Version 2.0][apache]. 
	
* pegdown &#8212; Copyright 2010-2011 Mathias Doenitz, used under the terms of the [Apache License, Version 2.0][apache].

* Xerces &#8212; Copyright 1999-2012 The Apache Software Foundation, used under the terms of the [Apache License, Version 2.0][apache].

* Saxon &#8212; Copyright Michael H. Kay, used under the terms of the [Mozilla Public License, Version 1.0][mozilla].

<h2 id="installation">Installation</h2>

Download the latest version from <a href="http://www.powersurgepub.com/downloads.html#twodue">PowerSurgePub.com</a>. Decompress the downloaded file. Drag the resulting file or folder into the location where you normally store your applications.

<h2 id="save">Saving Your To Do List(s)</h2>

When you launch Two Due for the first time, it will open with an empty list of To Do Items, and will save it in a "to do list" folder within a "TwoDue" folder within your "Documents" folder ("My Documents" on Windows). 

You may select New from the File menu to create a new list. 

To save your list to a location other than the default, take the following actions. 

* Select Save As from the File menu. 
* You will see a dialog box asking you whether you want to save as a file or a folder. Click on the Folder button. 
* You will see a File chooser dialog. Navigate to the location where you want your Two Due folder to be stored. Normally this would be somewhere in your documents folder. Click the Choose button when you have highlighted the existing folder in which your to do list will be saved. 
* You will see another dialog box asking you to name your Two Due folder. Enter the name you want to use to identify your primary To Do list -- or leave the name blank to use the folder already selected above -- then click the OK button. 

Two Due will identify one of your to do lists as your primary list, and will open this for you automatically when you launch Two Due. The first list you create will be your primary list, by default.  

If you create multiple To Do lists, then you can use the File / Open Known menu item to easily switch between them. Your primary data store will be identified by a "(P)" on the Open Known list. 

If you later wish to make a different file your primary file, then open that file, select Get Info from the File menu, and check the box that says "Make this my Primary List". 

Note that changes and additions to a list are not automatically saved to disk as they are made. You may explicitly choose the Save item from the File menu in order to save your data to disk. You may also wish to turn on the AutoSave option, by visiting the General tab within application Preferences and setting the AutoSave interval. 

<h2 id="backup">Backing Up Your To Do List(s)</h2>

Use the Backup menu item on the File menu to create a backup file containing your To Do List. A file name identifying your list and the current date and time will be suggested for you. After picking a location for your backups, that same location will be suggested for subsequent backups. 

Use the Revert from Backup menu item on the File menu to restore your list from a prior backup. 

Use the Backups tab within the application Preferences to select the degree of assistance you would like with backups. The Automatic option will completely automate the process for you. The Manual Only option will not provide any automated assistance. The Occasional Suggestions option will suggest a backup on roughly a weekly basis. 

<h2 id="item">Creating and Modifying To Do Items</h2>

Use the Item tab to create new To Do items, and to modify the basic information about an existing To Do item.  

The Item tab has the following fields that you may enter. All the fields are optional (you may leave them blank or with their default values if you wish), with the exception of the Title field, which is always required. 

<dl>
  <dt>Tags </dt>
    <dd>You may enter multiple tags, separated by commas, and each tag may contain multiple levels, separated by periods. For example "Books.Mysteries" would be a two-level tag. Once you enter a tag for one item, you may select that same tag for another item by using the drop-down menu. The Tree tab will show the items on your list, grouped by tags.  </dd>

  <dt>Title </dt>
    <dd>Enter a brief description or name for the item.  </dd>

  <dt>Due Date </dt>
    <dd>The components of the date are specified in Year/Month/Day order. The initial date is a default, meaningless date far in the future. If your item doesn't have a particular due date, then leave this field as is. (This will allow undated items to be sorted by more meaningful fields, such as priority.) Clicking on the Today button will cause today's date to be entered. You may then use the various increment and decrement buttons to increment the date components until you see the date you want. Or, you can always type the date in yourself.</dd>

  <dt>Status </dt>
    <dd>Use the drop-down menu to select a status, if you want to assign a status other than Open. You may only wish to use Open and Closed, but other values are available in case you are interested. The following meanings are suggested for the available values. 

			<ul>
			  <li>Open -- The item still needs to be done. </li>
			  <li>In Work -- The item has been started, but not yet completed. </li>
			  <li>Pending -- The item can't progress any further until someone else completes some action you have no control over. </li>
			  <li>Canceled -- The item was not completed, but no longer needs to be done. </li>
			  <li>Closed -- The item has been completed and needs no further attention. </li>
			</ul>
    </dd>

  <dt>Priority </dt>
    <dd>Use the slider to select a priority, if you wish to distinguish between high- and low-priority items. 1 is the highest priority, and 5 the lowest. </dd>

  <dt>Assigned To </dt>
    <dd>If you are only using Two Due for items that you personally need to do, then you probably won't need this field. If you are using Two Due to manage a team, or to track action items for other people, then you may type a person's name in here. You may use the drop-down menu to select a name that was previously entered. </dd>

  <dt>Sequence </dt>
    <dd>This field can be handy if you want your items to be sorted by some value other than any of the other ones available for an item. </dd>

  <dt>Description </dt>
    <dd>Use this if want to enter a longer description of the item than would fit in the Title field. </dd>

  <dt>Outcome </dt>
    <dd>In some cases, you may want to maintain a record of completed items, with a description of how they turned out. In these cases, this field can be used to document some details of how the task was completed. </dd>

  <dt>URL </dt>
    <dd>If you have a Web Page or local file associated with this item, then you may enter the URL in this field. Note that "URL" is a button, rather than an ordinary label. Pressing on the button will allow you to select a local file on your computer to be linked to the item. Note also that the List menu has an item called Validate Web Pages. This function will attempt to validate all the Web Page links in the currently open To Do list, identifying any dead or invalid links. Of course, you need a currently active Internet connection for this to work. Pressing the Launch button on the toolbar, or the arrow to the left of the URL, will cause Two Due to pass the entered URL to your preferred Web browser. </dd>

</dl>

<h2 id="sortlist">Viewing Your Items as a Sorted List</h2>

### Sorting Your Items ###

Go to the Views tab of the application Preferences to set your desired filter and sort parameters. 

Select the named View you want to modify by selecting it from the View Name drop-down menu. Alternatively you may press the "+" button to the right to create a new named View. In either case, you may change the name of the View by typing a new name in the View Name Combo Box. 

Select the desired Show value from the drop-down menu: All, Open and In-Work only, Due in Next 30 Days, Due in Next 7 Days, Due Today or Due in Next with a variable number of "look-ahead" days. 

Then select up to four fields that you wish to use to sort your list. 

All of the values on the View tab will be remembered for this particular View Name, and the selected View Name will be associated with the current to do list, and will be re-established automatically the next time you open the list. 

### Viewing a List of Your Items ###

Visit the List tab to see the sorted, filtered list of your To Do items. 

Note that the selection and sequence of fields shown on the List tab is dynamic, depending on the fields actually used in the currently open file, and on the sort fields in effect. Going to the Views Preferences and then back to the List tab will always cause this dynamic display to be recalibrated based on your current data. 

You may change the filtering and sort criteria without leaving the List Tab, by selecting a different named View from the View menu. 

<h2 id="tree">Managing Your Items with Tags</h2>

You may enter a tag and optional sub-tags for each item, on the Item tab. 

You may view your items grouped into their respective tags on the Tree tab. This is presented as an indented tree, on which you can open and close groups of items by clicking on the left-most icon in a category's row.  

Note that the List menu has an item called Replace Tags. This function provides a handy way to change all items with a given tag (and possible sub-tags) to a new set of tags. 

<h2 id="recur">Using Recurring Items</h2>

Many items, thankfully, are done once and then they are done forever. But other items may need to be repeated on a recurring basis. For the latter, you may use Two Due to repeat items on a regular schedule. 

The first step in setting up a recurring item is to create the new item on the Item tab. Set the due date to the first occurrence in the future. 

Now visit the Recurs tab. Here you can specify how often the item is to recur, in terms of a number and a unit of measurement (weeks, months, etc.). If you always want the item to occur on a certain day of the week, then you may also specify that on the Recurs tab. And finally, for monthly items, if you have specified a day of the week, you may specify which occurrence of that day, within each month (first, second, last, etc.) you want to be used. 

A recurrence may be triggered in one of two fashions. Both are triggered from the Item tab.  

1. Pressing the Recur button, to the right of the due date and below the Today button, will cause the date of the current item to be immediately set to the next scheduled occurrence. (Holding your cursor over the Recur button will cause a Tool Tip to appear, reminding you of the recurrence schedule for the current item.) 

2. Closing a recurring item will cause a new item to be created, with an Open Status, with the next scheduled due date, and with all of the other attributes of the current item. Use this option if for some reason you want to keep a record of the closed item, in addition to scheduling the new one. 

<h2 id="navigate">Navigating Your List</h2>

There are several ways to navigate through a list of To Do items. 

* You may advance to the next item in your list by pressing the Next button on the toolbar, selecting Next from the Item Menu, or by pressing CMD/ALT ] (right bracket) on the keyboard. 
* You may go back to the prior item in your list by pressing the Prior button on the toolbar, selecting Prior from the Item menu, or pressing CMD/ALT [ (left bracket) on the keyboard.  
* From the List menu, clicking on an item will take you to the Item tab for that item. 
* From the Tree menu, clicking on an item will take you to the Item tab for that item. 
* Selecting Find from the List menu, or simply clicking in the Find box on the toolbar, will allow you to specify a string to search for, and then take you to the first item that contains the specified string. From there, selecting Find Again from the List menu will take you successively to each following item in the list of found items. 

<h2 id="closed">Dealing with Closed Items</h2>

The number of open items on your list at any one time will tend to be relatively constant. As time passes, however, the number of closed items on your list will tend to grow. Two Due offers several ways of dealing with these.  

* You can always delete closed items when you have completed them instead of closing them. Of course, then you lose any record of what you have done (and deprive yourself of the satisfaction of reviewing your long list of closed items with a feeling of accomplishment). Also, you increase your chances that you will accidentally delete the wrong item. 
* You can use the Show drop-down menu on the View tab to request that you only see open and in-work items (or an even more restrictive option). This way completed items will disappear from view (but still be carried on your To Do file). 
* You may select Purge from the List menu to permanently remove closed items from your To Do file. Using the Purge Closed Items button will remove them from your To Do file and hurl them into oblivion. 
* Using the Archive Closed Items button (also available after selecting Purge from the List menu) will remove these same items from your To Do file, but transfer them into a special archive file that only contains closed and canceled items. If you need to preserve some sort of record of completed items (to use as meeting minutes, for example), then this may be your best bet. The Purge window also contains buttons that allow you to open the file of Purged items and to publish it to a Web page. 

<h2 id="time">Using Time Fields</h2>

Many To Do items need only a due date, and do not need to be identified with a time of day. However, Two Due has several fields and functions for dealing with start times when they become important. Here are the ones you should be aware of. 

* The Get Info window available from the File menu has a field at the bottom that allows you to specify a Default Start Time for a particular list. If you specify a value here, then all new items on this list will receive the default start time that you specify. This can be especially useful if you are using a list to keep track of a meeting agenda, for example (more information on this below). 
* The Time tab has several time-related fields that may be specified for each item. Duration is the first, and can be used to specify how long an item is expected to take, in hours and minutes. Again, this is especially useful for agenda items.
* The Time tab also has a Start Time field. For new items, this will default to the value specified on the View tab. Again, this is useful for meeting agendas. 
* The final field on the Time tab is the Alert Prior field. Setting a non-zero value here will cause visible and audible alerts to be issued by Two Due at the set number of minutes prior to the scheduled Start Time for an item. 
* Finally, on the List menu, there is an item called Schedule Start Times. This is especially handy to schedule meeting agendas. To use this, set up a separate To Do list for each meeting. On the View tab, specify the Default Start Time as the starting time for the meeting. As you enter each agenda item, specify its expected or desired duration, in hours and minutes. The Category, Seq and/or Priority fields, all on the Item tab, may be used to specify the desired sequence of the agenda items. Be sure to specify the desired sort sequence for the list on the View tab. Due Date would normally be first, with some combination of Category, Seq and/or Priority following. Once you have done all this, now execute the Schedule Start Times function from the List menu. Two Due will then use all the data you have entered to modify the start time for each open item on the list, with the first item on each date receiving the default start time, and following items being adjusted based on the duration(s) of the prior item(s). 

<h2 id="foldersync">Using Folder Sync </h2>

Two Due can be used to track to do information about a folder of files. Metadata about the files (last modification date and file size) will be kept up-to-date automatically. Two Due can then be used to track other information about each file, such as its status, tags used to categorize the file, the priority of its completion, etc. 

Start by creating a new list. You can use the folder of files to be tracked as the location for your Two Due folder, or you can use another folder located elsewhere on the same disk drive. 

Next select Folder Sync from the File menu to see the Folder Sync window. Click on the Browse button in the upper right corner and select the folder of files to be tracked. 

Now click the Sync button. At this point Two Due will create a matching to do item on your list for each file or folder in the file to be tracked. The title of each item will be taken from the corresponding file or folder name, dropping any file extension (which means that multiple files with the same name but different extensions will be represented by a single item on your Two Due list). If an item with a matching title already exists, then that item will be updated, rather than creating a new item. Each corresponding item will have four fields created/updated, based on the latest information about the file:

* Title, as described above. 
* URL, to point to the matching file. 
* Last Mod Date, indicating the last date on which the file was modified. 
* File Size, indicating the size of the file in bytes. 

Note that Last Mod Date and File Size can be used as sort keys when creating Views. 

When multiple files have the same name with different extensions, then files with extensions of .txt, .markdown or .mkdown will take precedence over others when deciding which file will have its URL, Last Mod Date and File Size updated to match the file's info. 

Check the Delete Unsynced Items box to have the sync operation delete any items on your list that no longer have any matching files in the sync folder. 

Check the Auto Sync box to have a sync performed automatically whenever this list is opened. 

The text window below the Sync buttons will show the results of the sync operation. 

Note that your Two Due list will be saved to disk at the end of every Sync operation. 

The Cancel button can be used to close the Folder Sync window when done. 

<h2 id="transfer">Transferring To Do Items</h2>

After selecting a particular To Do item, you may select the Transfer menu item from the Item menu. This will copy your to do item to the system clipboard in a plain text format. You may then paste this text into an e-mail, as one possible example. The recipient of the e-mail could then read the to do item and optionally paste it into their to do list by copying the text to the clipboard, then using the Two Due Accept command on the Item menu. 

<h2 id="publish">Publishing Your Lists to the Web</h2>

Two Due has a powerful ability to publish your To Do lists as Web pages, or as a series of related Web pages.   

PowerSurge Publishing has a complete system for publishing tab-delimited files of any kind to the Web.  This is available from a separate product, called PSTextMerge. However, most of PSTextMerge's capabilities are made available within Two Due for To Do lists. 

In order to publish a To Do list to the Web, you need to use a template file. The template file is a combination of normal HTML tags and special PSTextMerge commands. Several standard templates are provided with the Two Due distribution. You may either select one of these standard templates from the Publish window or create a custom Web template and specify it from the Publish window. 

See the <a href="http://www.powersurgepub.com/products/pstextmerge/opguide/template.html">Template page</a> on the PowerSurge Publishing Web site for a complete introduction to creating Web templates.   

A Web template is normally stored in the same folder that contains your twodue.tdu file. After selecting a standard or custom template on the Publish window, you may then publish your list as a Web page by pressing the Publish button on the Publish window. You may also pick from the following drop-down menu to have Two Due automatically re-publish your Web page whenever you save your To Do file, or whenever you close it. You may also specify a complete PSTextMerge script as a Web Template, which can then cause a whole series of related templates to be used.  

Note that on the Purge window, you may specify a separate template to publish your archived To Do items. This can be used to publish meeting minutes, for example. 

<h2 id="creative">Creative Uses </h2>

Two Due's fields and features can be combined in a variety of creative ways. Following are some examples. 

* Meeting Agendas and Minutes

	Use Two Due to track agenda items. Use the recurrence feature for standing agenda items. Use the Due Date for the date of the meeting, and use the Sequence field to place your agenda items in the desired order. Use the Start Time, Duration and Default Start Time fields, along with the Schedule Start Times menu item on the Lists menu, to schedule planned start times for each agenda item. Use the Outcome field to track the results from each agenda item, then archive closed items to a separate file, which then become your meeting minutes. Publish both files using Web Templates, so that other participants can easily access the agendas for upcoming meetings and minutes from past meetings. 
	
* Change Requests

	Use Two Due to track change requests. Use the Sequence field to track the version number that incorporated the changes. Sort the list by Status and Sequence (Descending), so that the open changes sort to the top of the list, and completed changes follow. Use a Web template to publish a version history for your product. 

[java]:  http://www.java.com/

[pspub]:     http://www.powersurgepub.com/
[downloads]: http://www.powersurgepub.com/downloads.html
[store]:     http://www.powersurgepub.com/store.html

[markdown]:  http://daringfireball.net/projects/markdown/
[pegdown]:   https://github.com/sirthias/pegdown/blob/master/LICENSE
[parboiled]: https://github.com/sirthias/parboiled/blob/master/LICENSE
[Mathias]:   https://github.com/sirthias

[club]:         clubplanner.html
[filedir]:      filedir.html
[metamarkdown]: metamarkdown.html
[template]:     template.html

[osd]:				http://opensource.org/osd
[gnu]:        http://www.gnu.org/licenses/
[apache]:			http://www.apache.org/licenses/LICENSE-2.0.html
[mozilla]: http://www.mozilla.org/MPL/2.0/