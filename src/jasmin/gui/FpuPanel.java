/*
 * FpuPanel.java
 *
 * Created on 13. Mai 2006, 17:56
 */

package jasmin.gui;

import jasmin.core.Fpu;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;

/**
 * @author kai
 */
public class FpuPanel extends javax.swing.JPanel {
	
	private static final long serialVersionUID = -5008641259698677910L;
	
	Fpu fpu = null;
	JasDocument doc = null;
	
	/** Creates new form FpuPanel */
	public FpuPanel(Fpu fpu, JasDocument doc) {
		initComponents();
		this.doc = doc;
		this.fpu = fpu;
		jTable1.setModel(new FpuStackTableModel(fpu, doc));
		jTable1.setDefaultRenderer(String.class, new TableCellRenderer() {
			
			/**
			 * @param isSelected
			 * @param hasFocus
			 * @param column
			 */
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				String s = table.getModel().getValueAt(row, 0) + "";
				JLabel label = new JLabel();
				if (s.startsWith("ST0")) {
					label.setFont(label.getFont().deriveFont(Font.BOLD));
				} else {
					label.setFont(label.getFont().deriveFont(Font.PLAIN));
				}
				label.setText(value + "");
				return label;
			}
		});
	}
	
	public void update() {
		jTable1.setModel(new FpuStackTableModel(fpu, doc));
	}
	
	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
	 * content of this method is always regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
	private void initComponents() {
		jScrollPane1 = new javax.swing.JScrollPane();
		jTable1 = new javax.swing.JTable();
		
		setLayout(new java.awt.BorderLayout());
		
		setBorder(javax.swing.BorderFactory.createTitledBorder("FPU Registers"));
		jScrollPane1.setViewportView(jTable1);
		
		add(jScrollPane1, java.awt.BorderLayout.CENTER);
		
	}// </editor-fold>//GEN-END:initComponents
	
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTable jTable1;
	// End of variables declaration//GEN-END:variables
	
}
