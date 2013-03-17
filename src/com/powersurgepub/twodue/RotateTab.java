/*
 * RotateTab.java
 *
 * Created on July 3, 2005, 3:35 PM
 */

package com.powersurgepub.twodue;

	import com.powersurgepub.psdatalib.elements.*;
  import com.powersurgepub.psutils.*;
  import com.powersurgepub.twodue.data.*;
  import com.powersurgepub.twodue.disk.*;
  import javax.swing.event.*;
  import java.text.*;

/**
   A panel to display a rotating selection of current to do items. <p>
  
   This code is copyright (c) 2005 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
      2003/08/31 - Originally written.
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 2003/11/09 - Added Category field. 
 */
public class RotateTab 
    extends javax.swing.JPanel 
      implements HyperlinkListener, TwoDueTab {
  
  private Trouble trouble = Trouble.getShared();
  
  private TwoDueCommon td;
  
  private StringBuffer text;
  
  private DateFormat dateFormat = new SimpleDateFormat ("EEEE MMMM d, yyyy");
  
  /** Creates new form RotateTab */
  public RotateTab(TwoDueCommon td) {
    this.td = td;
    initComponents();
    rotateEditorPane.addHyperlinkListener (this);
  }
  
  /**
    Prepares the tab for processing of newly opened file.
   
    @param store Disk Store object for the file.
   */
  public void filePrep (TwoDueDiskStore store) {
    // No file information used on the About Tab
  }
  
  public void initItems() {

  }
  
  public void displayItem() {
    text = new StringBuffer();
    text.append ("<html>");
    text.append ("<body bgcolor=\"#" 
        + StringUtils.colorToHexString(td.rotateBackgroundColor)
        + "\" text=\"#"
        + StringUtils.colorToHexString(td.rotateTextColor)
        + "\" link=\"#"
        + StringUtils.colorToHexString(td.rotateTextColor)
        + "\" alink=\"#"
        + StringUtils.colorToHexString(td.rotateTextColor)
        + "\" vlink=\"#"
        + StringUtils.colorToHexString(td.rotateTextColor)
        + "\">");
    text.append ("<table border=0><tr>");
    text.append ("<td>&nbsp;</td>");
    text.append ("<td>");
    ToDoItem item = td.getItem();
    // itemIDText.setText (item.getID());
    
    // Display Category in italics
    String category = "";
    if (td.categories.size() > 0) {
      category = item.getTags().toString();
    }
    appendParagraph ("em", 0, "", "", category);
    
    // Display Title in bold and increased size
    appendParagraph ("b", 1, "", "", item.getTitle());
    
    // Display Description, if there is any
    String description = item.getDescription();
    if (description.length() > 0) {
      appendParagraph ("", 0, "", "", item.getDescription());
    }
    
    // Display Outome, if any
    String outcome = item.getOutcome();
    if (outcome.length() > 0) {
      appendParagraph ("", 0, "", "Outcome", item.getOutcome());
    }
    
    // Display status if anything unusual
    int status = item.getStatus();
    if (status != ActionStatus.OPEN) {
      String statusLabel = item.getStatusLabel();
      appendParagraph ("", 0, "", "Status", statusLabel);
    }
    
    // Display due date, with comment if past due, due today or due tomorrow
    String dueDate = item.getDueDate (dateFormat);
    String dueDateComment;
    if (item.isLate()) {
      dueDateComment = " <b><em>Past Due!!!</em></b>";
    }
    else
    if (item.isDueToday()) {
      dueDateComment = " Due Today";
    }
    else
    if (item.isDueTomorrow()) {
      dueDateComment = " Due Tomorrow";
    } else {
      dueDateComment = "";
    }
    if (dueDate.length() > 3) {
      appendParagraph ("", 0, "", "Due", dueDate + dueDateComment);
    }
    
    // Display priority if anything unusual
    int priority = item.getPriority();
    if (priority != 3) {
      appendParagraph ("", 0, "", "Priority", 
          String.valueOf (priority) + " " + item.getPriorityLabel());
    }

    // Display Web Page, if there is one
    String url = item.getWebPage();
    if (url.length() > 0) {
      appendParagraph ("", 0, url, "", url);
    }
    
    String assignedTo = item.getAssignedTo();
    if (assignedTo.length() > 0) {
      appendParagraph ("", 0, "", "Assigned to", assignedTo);
    }

    /*

    int status = item.getStatus();
    switch (status) {
      case ToDoItem.OPEN:
        holdStatusIndex = 0;
        break;
      case ToDoItem.IN_WORK:
        holdStatusIndex = 1;
        break;
      case ToDoItem.PENDING:
        holdStatusIndex = 2;
        break;
      case ToDoItem.CANCELED:
        holdStatusIndex = 3;
        break;
      case ToDoItem.PENDING_RECURS:
        holdStatusIndex = 4;
        break;
      case ToDoItem.CLOSED:
        holdStatusIndex = 4;
        break;
      default:
        holdStatusIndex = 0;
        break;
    }
    itemStatusComboBox.setSelectedIndex (holdStatusIndex);
    closedStatus = ToDoItem.CLOSED;
    
    if (td.actors.size() > 0) {
      itemAssignedToComboBox.setSelectedIndex 
          (td.actors.lookupValue (item.getAssignedTo()));
    }
    
    itemSequenceText.setText (item.getSequence());
    
    
     */
    text.append ("</td>");
    text.append ("<td>&nbsp;</td>");
    text.append ("</tr></table>");
    text.append ("</body>");
    text.append ("</html>");
    rotateEditorPane.setText (text.toString());
  }  
  
  private String startCategoryParagraph 
      = "<p><font size=3 face=\"Lucida Grande, Verdana, Arial, sans-serif\"><em>";
  private String horizontalRule = "<hr>";
  private String startTitleParagraph 
      = "<p><font size=4 face=\"Lucida Grande, Verdana, Arial, sans-serif\"><b>";
  private String startNormalParagraph 
      = "<p><font size=3 face=\"Lucida Grande, Verdana, Arial, sans-serif\">";
  private String endCategoryParagraph
      = "</em></font></p>";
  private String endTitleParagraph 
      = "</b></font></p>";
  private String endNormalParagraph
      = "</font></p>";
  
  public void appendParagraph (
      String specialTag, 
      int fontVariance, 
      String href,
      String label, 
      String body) {
    int fontSize = td.rotateNormalFontSize + fontVariance;
    if (fontSize < 1) {
      fontSize = 1;
    } 
    if (fontSize > 7) {
      fontSize = 7;
    }
    String startSpecialTag = "";
    String endSpecialTag = "";
    if (specialTag.length() > 0) {
      startSpecialTag = "<" + specialTag + ">";
      endSpecialTag = "</" + specialTag + ">";
    }
    String intro = "";
    if (label.length() > 0) {
      intro = label + ": ";
    }
    String startLink = "";
    String endLink = ""; 
    if (href.length() > 0) {
      startLink = "<a href=\"" + href + "\">";
      endLink = "</a>";
    }
    text.append (
        "<p><font size=" +
        String.valueOf (fontSize) +
        " face=\"" +
        td.rotateFont +
        ", Verdana, Arial, sans-serif\">" +
        startSpecialTag +
        intro +
        startLink +
        body +
        endLink +
        endSpecialTag +
        "</font></p>");
  }
  
  public void hyperlinkUpdate (HyperlinkEvent e) {
    HyperlinkEvent.EventType type = e.getEventType();
    if (type == HyperlinkEvent.EventType.ACTIVATED) {
      td.openURL (e.getURL());
    }
  }
  
  /**
    Prepare to switch tabs and show this one.
   */
  private void showThisTab () {
    td.switchTabs();
  }
  
  /**
   Modifies the td.item if anything on the screen changed. 
   
   @return True if any of the data changed on this tab. 
   */
  public boolean modIfChanged () {
    return false;
  } // end method
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    rotateScrollPane = new javax.swing.JScrollPane();
    rotateEditorPane = new javax.swing.JEditorPane();

    setLayout(new java.awt.GridBagLayout());

    addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentShown(java.awt.event.ComponentEvent evt) {
        formComponentShown(evt);
      }
    });
    addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        formFocusLost(evt);
      }
    });

    rotateScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    rotateScrollPane.setViewportBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
    rotateEditorPane.setEditable(false);
    rotateEditorPane.setContentType("text/html");
    rotateEditorPane.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        rotateEditorPaneMouseClicked(evt);
      }
    });

    rotateScrollPane.setViewportView(rotateEditorPane);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(rotateScrollPane, gridBagConstraints);

  }
  // </editor-fold>//GEN-END:initComponents

  private void formFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusLost
    // Does nothing useful
  }//GEN-LAST:event_formFocusLost

  private void rotateEditorPaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rotateEditorPaneMouseClicked
    td.activateItemTab();
  }//GEN-LAST:event_rotateEditorPaneMouseClicked

  private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
    showThisTab();
    td.startRotation();
    // aboutJavaText.requestFocus();
  }//GEN-LAST:event_formComponentShown
  
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JEditorPane rotateEditorPane;
  private javax.swing.JScrollPane rotateScrollPane;
  // End of variables declaration//GEN-END:variables
  
}
