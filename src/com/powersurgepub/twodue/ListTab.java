package com.powersurgepub.twodue;

  import com.powersurgepub.twodue.data.*;
  import com.powersurgepub.twodue.disk.*;
  import javax.swing.*;
  import javax.swing.event.*;


/**
 *
 * @author  hbowie
 */
public class ListTab 
    extends javax.swing.JPanel 
      implements TwoDueTab {
        
  private TwoDueCommon   td;
  
  /** Creates new form ListTab */
  public ListTab(TwoDueCommon td) {
    this.td = td;
    initComponents();
  }
  
  /**
    Prepares the tab for processing of newly opened file.
   
    @param store Disk Store object for the file.
   */
  public void filePrep (TwoDueDiskStore store) {
    // No file information used on the List Tab
  }
  
  public void initItems() {
    td.sorted.getDisplayColumns();
    listTable.setModel (td.sorted);
    setColumnWidths ();
    
    // Make different priority values different colors
    ItemPriorityRenderer ipr = new ItemPriorityRenderer();
    listTable.setDefaultRenderer (ItemPriority.class, ipr);
    int priorityColumn = td.sorted.getPriorityColumn();
    if (priorityColumn >= 0) {
      listTable.getColumn(td.sorted.getColumnName(priorityColumn)).setCellRenderer(ipr);
    }
    td.sorted.fireTableDataChanged();
    listTable.doLayout();
  }
  
  /* public void doLayout() {
    listTable.doLayout();
  } */
  
  /**
    Set column widths.
   */
  public void setColumnWidths() {
    td.sorted.setColumnWidths (listTable);
  }
  
  /**
    Prepare to switch tabs and show this one.
   */
  private void showThisTab () {
    td.switchTabs();
  }
  
  public void displayItem() {
  }  
  
  public boolean modIfChanged() {
    return false;
  }  
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
  private void initComponents() {
    listScrollPane = new javax.swing.JScrollPane();
    listTable = new javax.swing.JTable();

    setLayout(new java.awt.BorderLayout());

    addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentShown(java.awt.event.ComponentEvent evt) {
        formComponentShown(evt);
      }
    });

    listTable.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {
        {null, null, null, null},
        {null, null, null, null},
        {null, null, null, null},
        {null, null, null, null}
      },
      new String [] {
        "Title 1", "Title 2", "Title 3", "Title 4"
      }
    ));
    listTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    listTable.setModel (td.sorted);
    listTable.setSelectionMode
    (ListSelectionModel.SINGLE_SELECTION);
    ListSelectionModel rowSM = listTable.getSelectionModel();
    rowSM.addListSelectionListener (new ListSelectionListener() {
      public void valueChanged (ListSelectionEvent e) {
        ListSelectionModel lsm = (ListSelectionModel)e.getSource();
        if (lsm.isSelectionEmpty()) {
          // nothing selected
        } else {
          itemSelected (lsm.getMinSelectionIndex());
        }
      }
    });
    listScrollPane.setViewportView(listTable);

    add(listScrollPane, java.awt.BorderLayout.CENTER);

  }
  // </editor-fold>//GEN-END:initComponents

  private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
    showThisTab();
    listScrollPane.requestFocus();
  }//GEN-LAST:event_formComponentShown

  private void itemSelected(int sortedIndex) {
    showThisTab();
    td.navigator = td.sorted;
    td.sorted.setIndex (sortedIndex);
    td.selectItem (td.sorted.get(sortedIndex));
    td.displayItem();
    if (td.selItemTab) {
      td.activateItemTab();
    } 
  }
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JScrollPane listScrollPane;
  private javax.swing.JTable listTable;
  // End of variables declaration//GEN-END:variables
  
}
