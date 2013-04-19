Version 3.10 (2013-04-18)
-------------------------

* Released as Open Source Software under the terms of the Apache 2.0 license.

Version 3.00 (2012-01-18)
-------------------------

* Updated to create a version that will run on OS X Lion
* The Folder Sync item on the File menu will now allow a Two Due list to be used to track the status of a folder of files.
* Converted Two Due from donationware back to shareware.
* The File menu now includes both Backup and Revert from Backup commands. Backup Preferences have also been added to the application Preferences, to allow either automatic backups, occasional (weekly) backup reminders, or completely manual backups.

Version 2.21 (2008-07-16)
-------------------------

* The most recent version retooled the user interface and, in doing so, lost any way for a Windows user to access the Preferences window. This may now be done via the Tools / Options menu.

Version 2.20 (2007-11-25)
-------------------------

* Converted TwoDue from shareware to donationware.
* When TwoDue is first launched, it will now default to a &quot;to do list&quot; folder within a &quot;TwoDue&quot; folder within the user's documents folder.
* Moved several panes that had been visible in a single row of tabs into separate windows. Updated the Toolbar to use symbols rather than words wherever possible, and to embed the search string field. Added an OK button that will reassuringly end your editing of an item and preserve your changes.

Version 2.1 (2006-07-21)
-------------------------

* Added a new Web Publishing template called Bookmarks.xbel. This will create an output file containing your categories, titles and Web URLs stored in the XBEL (XML Bookmark Exchange Language) format.
* The Accept Item on the Item menu now accepts unformatted as well as formatted text. The user may still create formatted text by using the Transfer item on the Item menu, but in addition, any block of text may now be added to a Two Due file with the Accept command.
* Added a Type field for each item, with a list of predefined values. Users may filter their views by selecting desired types to included.
* Corrected code that was incorrectly replacing a question mark.
* Added a Recur Button to the Item Tab so that the Due Date can be incremented by clicking on it, if a recurrence interval has been defined.
* Two Due will normally open with its last position and window size. In a case where a different monitor configuration is being used, this can result in the window being placed in an awkward position. Ctrl or Cmd W (or selecting Reduce Window Size from the Help menu) will now reposition and resize the window to make it fully accessible.
* When clicking on a date to edit it, if the date has not yet been set, then it will now default to today's date when the editor dialog opens.

Version 2.0.1 (2005-10-14)
-------------------------

* Received report from user running Windows XP Pro that the program was attempting to load its resource files from the user's home directory, rather than from the application's folder. Modified the code to fix this, so long as the program is installed in the standard location on Windows.
* Corrected a problem that prevented the Launch function from working successfully with URLs containing &quot;foreign&quot; characters.

Version 2.0 (2005-09-22)
-------------------------

* Display and editing of the Due date have been reworked to make them easier to use. Dates that have not yet been set now appear as &quot;N/A&quot;. The Due Date display on the Item Tab is now a single button labeled with the date. To change the date, click on the button to bring up a dialog that has a full calendar for the month. You may enter a year and month directly, or click on increment/decrement buttons to change them gradually. Click on the desired day of the month to set that. There is also a &quot;Today&quot; button to set the date to the current date, another button to set the date value to a null value or &quot;N/A&quot;, plus a Recur button for recurring dates.
* When a Two Due primary data file is saved, the file extension now defaults to .tdu. The associated Apple Creator Code is 2Due. Opening a file within the Finder on Mac OS X will now cause it to be opened with Two Due. Existing files with extensions of .txt will be saved with extensions of .tdu the first time they are accessed.
* The &quot;Web Page&quot; button on the Item Tab has now become the &quot;Web URL&quot; button, and performs a different function. Pushing this button now allows the user to select a local file or folder to be specified in the following URL field. There is now a separate Launch function, accessible either via the new Launch button on the Toolbar, or from the new Launch menu item on the Item menu, or via the shortcut key ctrl/cmd L. Whatever address is in the Web URL field will be passed to your Web browser. If this is a local file or folder, it should result in the file or folder being opened for you by your Web browser. The exact behavior may vary somewhat based on OS and on Web Browser.
* When copying an item using the Item Transfer/Accept commands, the Sequence field was being dropped. This has now been corrected.
* Multiple items may now be transferred between lists by Transferring one at a time to a contiguous block of text (in an e-mail, for example), then copying the entire text range and performing a single Accept. <br><br>When Accepting items, a message will now be displayed telling you how many items were found on the clipboard.
* When PSTextMerge is invoked to publish a web template, some additional informational messages will now be displayed on the log, including the amount of available memory and information about the Java Virtual Machine on which the application is running.
* Added IFNEWLIST and IFENDLIST commands, that indicate the beginning and ending of a list of records, all having the same group identifier at the specified level. So whereas IFNEWGROUP indicates a new value in the group identifier field for a particular level, IFNEWLIST indicates the start of a new sequence of values. In general, IFNEWLIST and IFENDLIST can be used to insert begin list and end list tags when generating HTML.
* Added an INCLUDE Command to Template processing. This command allows you to include text from another file into the output stream being generated by the template. The included text is not processed in any way, but is simply copied to the output file(s) being generated. This does allow output from a previous step in a script to be included in the output generated by a later step. If an include file is not found, then it will simply be skipped and processing will continue, with a log message to note the event.
* Added new Rotate Tab, with accompanying controls on Prefs Tab, that will allow selected To Do items to rotate regularly from one item to the next. This is an alternative way to increase visibility of your open items.
* Added logic that will update today's date at or around midnight, for users who leave Two Due running overnight.
* Corrected some conflicts when running PSTextMerge (aka TDFCzar) from within Two Due.

Version 1.9 (2005-01-09)
-------------------------

* Added a new menu item to the List Menu to Cut Some Slack, by making all past due items due today, tomorrow, or next week.
* Added a three-character abbreviation of the month to the date display on the Item tab, to make the data a bit easier to read.
* Modified to clean up the Web Page field in the following ways: trim leading and trailing spaces; eliminate surrounding &quot;&lt;>&quot; characters; insert &quot;http://&quot; at beginning of field if missing.
* Fixed bug that caused user to be prompted to save the to do list even though it had already been saved.
* Modified to use a default location of a folder named &quot;Two Due&quot; within the user's primary documents folder.
* The Web template fields have been moved from the View tab to a new Web tab. A new drop-down menu has been added to allow the user to select a standard template to be used to generate a Web page.
* When creating a new file as an unregistered user, the program will now automatically create a To Do item with a reminder to register Two Due.
* Modified to display a confirmation dialog before creating a new occurrence of a recurring item, when closing an old one, to ensure the user really wants a new one created.
* Added Save button to Toolbar. This not only allows a convenient way to save your file, it also provides a visual indicator on non-Mac platforms when you have unsaved changes.
* Modified sort order on Tree Tab to list to do items before sub-category nodes.

Version 1.8 (2004-10-30)
-------------------------

* Reduced the number of Save warnings produced when no data seemed to actually have changed -- especially after having done an explicit save and when trying to quit Two Due.
* Fixed a bug that prevented Web publishing actions from appearing on the Log tab.
* Fixed a bug that sometimes caused Web publishing to run in demo mode, and therefore only processing the first 10 items in a list.
* Added a variable on the View tab to allow any number of days to be specified as the number of days in the future for which you want to see to do items. This is invoked by selecting &quot;Due in Next&quot; from the Show menu, and then entering the desired number of look-ahead days.
* A view is now a separate named entity. A view is defined as a set of selection and sorting criteria. A name may be assigned to a view on the View tab. Views may also be added and deleted on the View tab. A different view can be selected from either the View tab, or from a new View menu. The bottom line is that you may now define any number of different views, and easily switch between views using the View menu.

Version 1.7 (2004-09-04)
-------------------------

* Default Due Date has been changed to 12/01/2050, from 12/31/2050, to avoid the problem of a month entry being incremented because the default day of the month is greater than the number of days in the month. Items with the old default dates should be converted when they are opened.
* Date formats in templates may now be used for any mm/dd/yy date. Web templates previously offered a very flexible way to format dates, but this formatting could only be used to format today's date. The same date formats may now be used for any date in month/day/year format.
* A variable modifier of x will now cause a variable to have applicable characters translated to XML entities. This is especially useful if using Two Due to generate an RSS feed.
* Added menu items to Transfer and Accept to do items. Transfer will copy the information for the selected to do item to the System clipboard. From there, you can paste it into an e-mail, for example. The recipient of the e-mail could then select the body of the e-mail, and paste it to their clipboard. Once on the clipboard, selecting Accept from within Two Due will add the item to the current list.
* Added several options on the Prefs tab to modify the application's look and feel, especially when running on a Mac.

Version 1.6.2 (2004-07-23)
-------------------------

* Inadvertently omitted a module from the Mac OS X build, effectively preventing the execution of Web generation scripts. This version corrects that problem.

Version 1.6.1 (2004-07-23)
-------------------------

* Version 1.6 introduced a small bug that could cause a crash at startup under certain conditions (apparently if the new version did not end normally the first time it was run). This has now been corrected.

Version 1.6 (2004-07-11)
-------------------------

* Modified to preserve line breaks in the description and outcome fields by converting them internally to HTML br tags.
* Added a check box to the View tab that allows the user to specify that undated items should be sorted high (as if they had dates higher, or farther in the future, than all others).
* The program version history is now easily accessible from Two Due's Help Menu.
* A Quick Start guide is now available. It can be accessed from the powersurgepub.com Web site, and is also available from the Help Menu of Two Due.
* Corrected a problem that sometimes occurred when user tried to create a new item with an initial status of closed.
* Added standard Edit Menu containing Cut, Copy and Paste menu items that work on any text field.
* Added another column to the tab-delimited text file used to store To Do items. Due Date YMD is the due date of the item, but in a year/month/day format. This can be used to sort the items in a file in due date sequence when using a TDF Czar script file (.tcz) for Web publishing.
* A Java JVM/JRE of 1.4.1 or higher is now required to run Two Due. The latest JVM available under Mac OS X satisfies this requirement. Other users may download the latest version available for your platform from www.java.com. If you are unsure of your current version, you may check it by viewing the bottom of the Two Due About tab.
* User preferences are now stored using the Java API, which places them in the proper location for preferences per the operating system standard. A copy of the preferences is also maintained in the Primary folder file, as backup.
* Two Due will now open at the same position and with the same window size as when you last closed it.
* Columns on the List View are now dynamic, depending on which columns are in use when a file is opened, depending on the sort sequence for the view, and depending on the size of the application window.

Version 1.5 (2004-04-05)
-------------------------

* For those who like keyboard shortcuts, Alt/Cmd 1 - 5 now take you to the List through Recurs tabs, respectively. These are visible, and also available, from the new Tabs menu.
* Added a new item status value of Pending, to indicate that the task is waiting on someone else to take some action.
* Added an import function that will open an existing file and add its contents to the current list of items. File names ending in .htm or .html will be recognized as HTML files, and will be treated as bookmark files, for the purpose of importing. Files with other extensions will be treated as standard Two Due tab-delimited files.
* Added a Validate Web Pages function, available from the List Menu. When selected, all non-blank web pages in the current list will be validated to be active and available URL references. The command will identify the number of dead links found, and will allow you to cycle through these by using the Next and Prior commands.
* Renamed the Record menu to the Item menu, and moved it to the right of the List menu, so that menus now go consistently from most general to most specific, when read from left to right.
* The List menu now includes a Replace Category function. This will change the category of all items having the specified find value to the specified replacement value.
* Modified to ignore any errors when attempting to save user preferences, if the program has not yet been registered. This supports potential users executing the program from the disk image, before moving it to their hard drive.
* Added the ability to perform an entire Web publishing script, as an alternative to using a single template. The script format supports filtering and sorting as well as template expansion. The script format is the same as the one used by TDF Czar. Script file names are entered in place of Web Templates, and script file names must end with '.tcz'.
* Added logging tab so as to provide a place to display background messages about what the program is doing. Useful for debugging or to figure out what the program is doing when this may otherwise not be obvious.
* The item number and item count fields displayed at the bottom of the screen now show information from the active view. A view may be activated by selecting an item from its Tab (List and Tree have their own views) or by executing a function that selects a subset of the records (such as Find). In the case of a Find, for example, the numbers at the bottom of the screen will show you the item number and item count for the found records, until the user selects the List or Tree view to activate a different view.
* Modified Next and Prior actions to work properly when coming from the Tree Tab, so that they take you to the next/prior items as sorted by category.
* Added a Find function that will search for a text string in any text field, ignoring case, take you to the first found item, and then allow you to use Next and Prior to cycle through the list of found items. For ease of use, and consistency with the Mac interface, also added Find Again, which takes you to the next found record.

Version 1.4 (2004-02-20)
-------------------------

* Now only set H as a shortcut key for Help when not running on a Mac, since Cmd H on a Mac should hide the application.
* For greater consistency with Mac GUI guidelines, the keyboard shortcut for Next Record has been changed to use the closing bracket character (rather than the right arrow), and the Prior Record shortcut has been changed to use the opening bracket character (instead of the left arrow).
* Corrected some bugs affecting updating of Categories and the Tree Tab.
* Modified the View Web Page function to replace spaces in the URL with %20 placeholders, which should make this function more reliable.
* The process for saving data has been modified so that the data is first written to a file with a .tmp extension. If this is successful, then the prior data file is renamed with a .bak extension, and the new file is finally renamed to have its normal .txt file extension. This new procedure reduces the risk of accidental data loss.
* There is now a new Time Tab with information about the time that a particular item is expected to occur. This Tab has three fields: Start Time, Duration, and Alert Prior. There is also a default Start Time on the View Tab. Whatever Start Time you enter here (00:00 or midnight, by default) will be assigned to each new To Do Item within that particular data store.
* By setting a start time for an item and by setting alert prior to something other than N/A (both fields appearing on the new Time Tab), an alert will be triggerred at the specified time. Your system will beep and a dialog box will come up, reminding you of the item. You may dismiss the box with no change, or request that the alerting item be brought into view.
* If you set a default start time for a particular file (on the View Tab), and set durations for items in that file, then a new menu item List --> Schedule Start Times will automatically schedule each item with a start time by adding the prior item's duration to its start time. The first item scheduled for each Due Date will be assumed to start at the default time, with following items scheduled successively. This function can be used to generate meeting agendas, for example.
* Added separate fields, on input and output, for Category 1 through Category 5. This allows files to be input that have sub-categories defined as separate fields. It also allows each of the first 5 category levels to be accessed independently in Web templates. One benefit of this change is that it allows Two Due to be used as a Bookmark organizer. Also provided is a sample Web template that shows how to use these fields to simulate the tree view.
* Open Known replaces Open Recent on the File menu. The number of files that may appear here is no longer limited to five. Any file or folder opened or saved will be added to this sub-menu, as with Open Recent. Files or Folders may be removed by telling Two Due to forget about them (on the View Tab, or when opening a disk store that cannot be found).
* From the Purge Tab, Closed and Canceled Items may now be archived as well as purged, for TwoDue disk stores specified as folders. Archived Items will be removed from the current file and added to a new file in the same folder, named archive. txt. Successive archive operations will continue adding items to the same file. The archive file may be opened and modified, and may also be published using its own Web template, which should be named archive_template.html.
* Although individual files may still be opened and saved, it is now preferred to specify folders as your Two Due storage locations. Two Due will then recognize multiple files, file types, and file names for different purposes within each folder. The primary file will be named twodue.txt. The default template file will be template.html. This design change will enable many new enhancements in this and coming releases.
* You now need to specify a primary folder. You can do this on the View Tab. You may only have one Primary Folder. This is where Two Due will store information about all of your known files. It will also be used for other purposes in future enhancements. This should be your primary list of to do items.

Version 1.3 (2003-12-08)
-------------------------

* Corrected bug that allowed carriage returns, line feeds and tab characters to be entered in text fields, but then misinterpreted these characters when a file was re-opened, causing fields to be misaligned, effectively corrupting the data file.
* Added a registration reminder that will prompt the user to register the program when he or she tries to load or add more than 10 records.
* Fixed a bug that caused the late flag to be inaccurate, especially on the last day of the month.
* Reconfigured the layout of date fields on the Item Tab to make them easier to use and more intuitive.
* Can now set a recurrence to happen on the last day of week of a month (in addition to the first, second, third or fourth).
* Added a category field. Multiple category levels can be separated by a forward slash ('/') or a period ('.'). The new Tree Tab allows you to see your items organized by categories.
* Prior logic threw away remembered file names if they were inaccessible at launch. These are now retained and reported as inaccessible when you attempt to access them.
* Fixed a bug in logic that checked your registration code. If the first digit after the last dash was a zero, then the registration check would fail.

Version 1.2 (2003-11-06)
-------------------------

* Added access to Two Due User Guide from Help menu.
* Changed the display of Priority on the List Tab to center the number within its column, and to display different values in different colors, to provide better visual cues.
* Added a Record Menu, with items to create a New Record, move to the Next Record, and move to the Prior Record. All three have keyboard shortcuts (Cmnd/Ctrl N is now used to create a New Record, rather than a New File).
* Added information about the current Java Run-time to the bottom of the About Tab.
* Added a green plus sign icon to flag items due tomorrow, in addition to other icons already used to indicate overdue or due today.
* Added a Web Page field for each To Do item, along with a button to launch the Web page in your Web browser.
* Added access to Kagi Registration page from Help menu.
* Added access to Two Due Home Page from Help menu.
* Added Web Publishing fields to View Tab, so that a To Do list can be easily published as a Web page using an HTML template.

Version 1.1 ()
-------------------------

* Added an Open Recent sub-menu to the File menu. This will allow the user to select any of the last five To Do files opened to be conveniently selected and re-opened. The View options (selection and sort criteria) last used for each file will also be reset when a file is opened via this sub-menu.
* Added a Description field. This is a multi-line text field that allows a more complete description of an item than will fit in the Title field.
* Added a Sequence field, which can be used to store a version number (for software change requests) or an agenda sequence for meeting agenda items. This new Sequence field was also added as a candidate field for Sorting on the View tab.
* Added an AutoSave option on the Prefs tab that allows the user to specify how often they want an open file with unsaved changes to be automatically saved.
* Added an Outcome field. This can be used to document the outcome, or the results, of a To Do item. For software change requests, as an example, this field can be used to record the resolution of the request.
* Added new Show options that allow user to see only open items due today, due in next 7 days, or due in next 30 days.
* Added Ascending/Descending radio buttons for each sort field on the View tab, so that fields may be sorted in descending order as well as the default ascending sequence.
* Fixed the following bug: When a new record was added, and only a subset of the entire list was being displayed, the last visible record on the list dropped off, keeping the number of visible records constant.
* Modified the operation of the Next button so that, when an item has changed its position in the list due to a key change, the Next button should take you to the next item after the modified item's original position, rather than the next item after the modified item's new position.
* View settings (selection and sort criteria) are now saved between executions, for the last 5 files accessed.
* Added two new fields that together allow a recurring item to be scheduled for the nth day of week within a month (the 3rd Wednesday of the month, for example). The new fields are the desired day of the week, and the desired occurrence of that day of week within the month.

Version 1.0 ()
-------------------------

* Added buttons to the Item Tab to allow the date to be incremented or decremented one week at a time.
* Added ability to purge closed items.
* Added tool tips to the date increment and decrement buttons to explain their operation.
* Corrected bug that caused sort to only look at visible records.
* Modified to turn late flag off when item is closed.
* Modified to adjust late flag as date is modified.
* Added a tool tip to the Recur Button, on the Item Tab, to indicate the frequency of recurrence.
* Modified today button to set year, month and day but not set current time, so that time of day will not influence sort order.
* Created a User Guide and published it on the PowerSurgePub Web site.

Version 0.9 ()
-------------------------

* First pre-release of program for beta testing.

