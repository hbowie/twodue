/*
 * ListPrinter.java
 *
 * Created on November 3, 2005, 8:19 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.powersurgepub.twodue;

  import com.powersurgepub.twodue.data.*;
  import javax.swing.*;
  import java.awt.print.*;
  import javax.print.*;
  import javax.print.event.*;
  import javax.print.attribute.*;
  import javax.print.attribute.standard.*;
  import java.io.*;

/**
 *
 * @author hbowie
 */
public class ListPrinter {
  
  /** Creates a new instance of ListPrinter */
  public ListPrinter() {
  }
  
  public void print (SortedItems list) {
    
    // Determine what "flavor" of output we will be using
    DocFlavor flavor = DocFlavor.INPUT_STREAM.TEXT_PLAIN_HOST;
    
    // Get a list of all printers that can handle this flavor
    PrintService[] services 
        = PrintServiceLookup.lookupPrintServices (flavor, null);
    
    // Set some printing attributes
    PrintRequestAttributeSet printAttributes
        = new HashPrintRequestAttributeSet();
    printAttributes.add (OrientationRequested.PORTRAIT);
    printAttributes.add (Chromaticity.MONOCHROME);
    
    // Display a dialog that allows the user to select one of the
    // available printers and to edit the default attributes
    PrintService service = ServiceUI.printDialog 
        (null, // GraphicsConfiguration
        100,   // x
        100,   // y
        services,
        null,  // default print service
        flavor,
        printAttributes);
    
    // If user selected a printer, then go ahead and print
    if (service != null) {
      /*
      
      // Create a Doc object to print from the inputStream and flavor
      Doc doc = new SimpleDoc (in, flavor, printAttributes);
      
      // Create a print job
      DocPrintJob job = service.createPrintJob();
      
      // Now print the document, catching errors
      try {
        job.print (doc, printAttributes);
      } catch (PrintException e) {
        System.out.println ("Printing error " + e);
      } // end if we caught an exception
       */
      
    } // end if user selected a printer
  } // end of print method
  
} // end of ListPrinter class
