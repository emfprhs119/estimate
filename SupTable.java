import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

public class SupTable extends JPanel {

	static int Row = 22;
	static int RowMax = 33;
	List listC;
	String list[][];
	MyJTable table;
	MyJTable tableAdd;
	JTextField sumT,sumT2,sumF2,sumR2;
	JLabel sum = new JLabel("«’∞Ë±›æ◊ ");
	int index = 0;
	int selx = 0, sely = 0;
	boolean selkey = true;
	boolean eraseText = true;
	int sumDatas = 0;
	boolean init = false;
	static int flag = 1;
	static int curPage = 1;
	String tmpString = null;
	PopupMenu popup = new PopupMenu();
	PopupMenu popup2 = new PopupMenu();
	MenuItem addColumn,addColumn2;
	MenuItem removeColumn,removeColumn2;

	SupTable(JPanel back) {
		listC = new List(this);
		list = listC.init();

		sumT = new JTextField(10);
		sumR2 = new JTextField(10);
		sumF2 = new JTextField(10);
		sumT2 = new JTextField(10);
	
		sumT.setEditable(false);
		sumF2.setEditable(false);
		sumR2.setEditable(false);
		sumT2.setEditable(false);
		Object row[][] = new Object[Row][8];
		Object rowmax[][] = new Object[RowMax][8];

		Object column[] = { "«∞∏Ò", "±‘∞›", "¿⁄¿Á∫Ò", "∞°∞¯∫Ò", "ºˆ∑Æ", "¥‹∞°", "∞¯±ﬁ∞°æ◊", "∫Ò∞Ì" };

		table = new MyJTable((Object[][]) row, column);
		tableAdd = new MyJTable((Object[][]) rowmax, column);
		table.getTableHeader().setFont(new Font(Main.font, 0, Main.fontSize));
		tableAdd.getTableHeader().setFont(new Font(Main.font, 0, Main.fontSize));
		JScrollPane scroll = new JScrollPane(table);
		scroll.setBackground(Color.WHITE);
		JScrollPane scrollAdd = new JScrollPane(tableAdd);
		scrollAdd.setBackground(Color.WHITE);
		tableSet(table, Row);
		tableSet(tableAdd, RowMax);
		scroll.setPreferredSize(new Dimension(720, 538));
		scrollAdd.setPreferredSize(new Dimension(720, 814));

		sum.setBounds(15, 260, 400, 25);
		sum.setFont(new Font(Main.font, Font.BOLD, 25));
		sumT.setFont(new Font(Main.font, Font.BOLD, 25));
		sumT.setHorizontalAlignment(JTextField.RIGHT);
		sumT.setBackground(Main.color);
		sumT.setBounds(130, 255, 220, 35);
		
		sumR2.setBackground(Color.white);
		sumR2.setBounds(18+32, 830+31, 719, 35);
		
		sumF2.setText("«’∞Ë");
		sumF2.setFont(new Font(Main.font, Font.BOLD, Main.fontSize/2*3));
		sumF2.setHorizontalAlignment(JTextField.CENTER);
		sumF2.setBackground(Color.white);
		sumF2.setBounds(18+32,830+31,301, 35);
		
		sumT2.setFont(new Font(Main.font, Font.BOLD,  Main.fontSize));
		sumT2.setHorizontalAlignment(JTextField.RIGHT);
		sumT2.setBackground(Main.color);
		sumT2.setBounds(607+32,830+31,87, 35);
		addColumn = new MenuItem("«‡ √ﬂ∞°");
		removeColumn = new MenuItem("«‡ ¡¶∞≈");
		addColumn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listC.addColumn(table.getSelectedRow());
				listC.listRe(table);
				listC.listRe(tableAdd);
			}
		});
		removeColumn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listC.removeColumn(table.getSelectedRow());
				listC.listRe(table);
				//listC.listRe(tableAdd);
			}
		});
		
		addColumn2 = new MenuItem("«‡ √ﬂ∞°");
		removeColumn2 = new MenuItem("«‡ ¡¶∞≈");
		addColumn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listC.addColumn(tableAdd.getSelectedRow()+index);
				listC.listRe(tableAdd);
				listC.listRe(table);
			}
		});
		removeColumn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listC.removeColumn(tableAdd.getSelectedRow()+index);
				listC.listRe(tableAdd);
				listC.listRe(table);
			}
		});
		popup.add(addColumn);
		popup.add(removeColumn);
		popup2.add(addColumn2);
		popup2.add(removeColumn2);
		add(scroll);
		back.add(scrollAdd);
		this.setBackground(Color.WHITE);

		valueChangedSet(table, Row);
	}

	public void setFont(String font, int size) {
		OneCellRenderer centerRenderer = new OneCellRenderer(font, size);
		OneCellRenderer leftRenderer = new OneCellRenderer(font, size);
		OneCellRenderer rightRenderer = new OneCellRenderer(font, size);
		leftRenderer.setHorizontalAlignment(JLabel.LEFT);
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);// ??
		table.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
		table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		table.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		table.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
		table.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
		table.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);
		tableAdd.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
		tableAdd.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		tableAdd.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
		tableAdd.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		tableAdd.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		tableAdd.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
		tableAdd.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
		tableAdd.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);
	}

	public void valueChangedSet(JTable tableA, int rowMax) {
		int sumData = 0;
		int mixData = 0;

		if (selkey == true) {
			selx = tableA.getSelectedColumn();
			sely = tableA.getSelectedRow();
		}
		int max;
		/*
		if (flag == curPage)
			max = rowMax-1;
		else
		*/
			max = rowMax;
		if (flag > 1 && tableA.getRowCount() == Row) {
			max = rowMax;
		}
		int temp = sumDatas;
		sumDatas = 0;

		for (int i = 0; i < max; i++) {
			for (int j = 0; j < 8; j++) {
				if (tableA.getValueAt(i, j) != null) {
					if (!tableA.getValueAt(i, j).toString().matches(".*[a-z A-Z 0-9 §°-§æ§ø-§”∞°-∆R].*")) {
						tableA.setValueAt(null, i, j);
					}
				}
			}
			if (tableA.getValueAt(i, 2) != null)
				if (toStrFormat((String) tableA.getValueAt(i, 2)) != "non")
					tableA.setValueAt(toNumFormat(Integer.parseInt(toStrFormat((String) tableA.getValueAt(i, 2)))), i, 2);
			if (tableA.getValueAt(i, 3) != null)
				if (toStrFormat((String) tableA.getValueAt(i, 3)) != "non")
					tableA.setValueAt(toNumFormat(Integer.parseInt(toStrFormat((String) tableA.getValueAt(i, 3)))), i, 3);
			if (tableA.getValueAt(i, 4) != null)
				if (toStrFormat((String) tableA.getValueAt(i, 4)) != "non")
					tableA.setValueAt(toNumFormat(Integer.parseInt(toStrFormat((String) tableA.getValueAt(i, 4)))), i, 4);

			if ((toStrFormat((String) tableA.getValueAt(i, 2)) != null && toStrFormat((String) tableA.getValueAt(i, 3)) != null)
					&& toStrFormat((String) tableA.getValueAt(i, 2)) != "non" && toStrFormat((String) tableA.getValueAt(i, 3)) != "non")
				sumData = Integer.parseInt(toStrFormat((String) tableA.getValueAt(i, 2)))
						+ Integer.parseInt(toStrFormat((String) tableA.getValueAt(i, 3)));
			else if (toStrFormat((String) tableA.getValueAt(i, 2)) != "non" && tableA.getValueAt(i, 2) != null)
				sumData = Integer.parseInt(toStrFormat((String) tableA.getValueAt(i, 2)));
			else if (toStrFormat((String) tableA.getValueAt(i, 3)) != "non" && tableA.getValueAt(i, 3) != null)
				sumData = Integer.parseInt(toStrFormat((String) tableA.getValueAt(i, 3)));

			tableA.setValueAt(null, i, 5);
			if (!((toStrFormat((String) tableA.getValueAt(i, 2)) == null || toStrFormat((String) tableA.getValueAt(i, 2)) == "non") && (toStrFormat((String) tableA
					.getValueAt(i, 3)) == null || toStrFormat((String) tableA.getValueAt(i, 3)) == "non")))
				tableA.setValueAt(toNumFormat(sumData), i, 5);

			tableA.setValueAt(null, i, 6);
			if ((tableA.getValueAt(i, 4) != null) && (toStrFormat((String) tableA.getValueAt(i, 4)) != "non")
					&& (tableA.getValueAt(i, 5) != null)) {
				mixData = Integer.parseInt(toStrFormat((String) tableA.getValueAt(i, 4)))
						* Integer.parseInt(toStrFormat((String) tableA.getValueAt(i, 5)));
				tableA.setValueAt(toNumFormat(mixData), i, 6);
			} else
				mixData = 0;
		}

		init = false;
		if (curPage == 1) {
			listC.saveList(table, 0, Row);
		} else if (flag == curPage)
			listC.saveList(tableAdd, index, RowMax);
		else
			listC.saveList(tableAdd, index, RowMax);

		for (int i = 0; i < listC.maxSize; i++) {
			sumData = 0;
			
			if ((toStrFormat((String) list[i][2]) != null && toStrFormat((String) list[i][3]) != null)
					&& toStrFormat((String) list[i][2]) != "non" && toStrFormat((String) list[i][3]) != "non")
				sumData = Integer.parseInt(toStrFormat((String) list[i][2])) + Integer.parseInt(toStrFormat((String) list[i][3]));
			else if (toStrFormat((String) list[i][2]) != "non" && list[i][2] != null)
				sumData = Integer.parseInt(toStrFormat((String) list[i][2]));
			else if (toStrFormat((String) list[i][3]) != "non" && list[i][3] != null)
				sumData = Integer.parseInt(toStrFormat((String) list[i][3]));
			if (!((toStrFormat((String) list[i][2]) == null || toStrFormat((String) list[i][2]) == "non") && (toStrFormat((String) list[i][3]) == null || toStrFormat((String) list[i][3]) == "non")))
				if (list[i][4] != null)
					if (toStrFormat((String) list[i][4]) != "non")
						sumDatas += sumData * Integer.parseInt(toStrFormat((String) list[i][4]));
		}

		if (toNumFormat(sumDatas) == null) {
			sumT.setText(toNumFormat(temp) + "ø¯");
			sumT2.setText(toNumFormat(temp));
		} else {
			sumT.setText(toNumFormat(sumDatas) + "ø¯");
			sumT2.setText(toNumFormat(sumDatas));
		}
		/*
		if (curPage == flag) {
			if (flag == 1) {
				table.setValueAt("«’∞Ë", Row - 1, 5);
				table.setValueAt(toNumFormat(sumDatas), Row - 1, 6);
			} else if (tableAdd.getRowCount() == RowMax) {
				tableAdd.setValueAt("«’∞Ë", RowMax - 1, 5);
				tableAdd.setValueAt(toNumFormat(sumDatas), RowMax - 1, 6);
			}
		}
		 */
	}

	final public static String toNumFormat(int num) {
		if (num == 0)
			return "0";
		DecimalFormat df = new DecimalFormat("#,###");
		return df.format(num);
	}

	final static public String toStrFormat(String num) {

		if (num == null)
			return null;
		num = num.replaceAll("[^-0-9]", "");
		if (num.matches("^0-9+")) {
			return num;
		} else {
			return "non";
		}
	}

	public void setTableSize(String[] stn) {
		int pos = 0;
		table.getColumn("«∞∏Ò").setPreferredWidth(Integer.parseInt(stn[pos++]));
		table.getColumn("±‘∞›").setPreferredWidth(Integer.parseInt(stn[pos++]));
		table.getColumn("¿⁄¿Á∫Ò").setPreferredWidth(Integer.parseInt(stn[pos++]));
		table.getColumn("∞°∞¯∫Ò").setPreferredWidth(Integer.parseInt(stn[pos++]));
		table.getColumn("ºˆ∑Æ").setPreferredWidth(Integer.parseInt(stn[pos++]));
		table.getColumn("¥‹∞°").setPreferredWidth(Integer.parseInt(stn[pos++]));
		table.getColumn("∞¯±ﬁ∞°æ◊").setPreferredWidth(Integer.parseInt(stn[pos++]));
		table.getColumn("∫Ò∞Ì").setPreferredWidth(Integer.parseInt(stn[pos++]));
	}

	public void setPopup(SupTable supTable, JPanel paneAdd) {
		supTable.add(popup);
		paneAdd.add(popup2);
			table.addMouseListener(new MouseLis(supTable, table, popup));
			tableAdd.addMouseListener(new MouseLis(paneAdd,tableAdd, popup2));
	}
	public JTable tableSet(final MyJTable table, final int rowMax) {
		table.getColumn("«∞∏Ò").setPreferredWidth(Main.tableSize[0]);
		table.getColumn("±‘∞›").setPreferredWidth(Main.tableSize[1]);
		table.getColumn("¿⁄¿Á∫Ò").setPreferredWidth(Main.tableSize[2]);
		table.getColumn("∞°∞¯∫Ò").setPreferredWidth(Main.tableSize[3]);
		table.getColumn("ºˆ∑Æ").setPreferredWidth(Main.tableSize[4]);
		table.getColumn("¥‹∞°").setPreferredWidth(Main.tableSize[5]);
		table.getColumn("∞¯±ﬁ∞°æ◊").setPreferredWidth(Main.tableSize[6]);
		table.getColumn("∫Ò∞Ì").setPreferredWidth(Main.tableSize[7]);
		//table.setValueAt("«’∞Ë", rowMax - 1, 5);
		// Font f=new Font();
		table.setRowHeight(23);
		// table.setFont(new Font(null, 0, 50));
		OneCellRenderer centerRenderer = new OneCellRenderer(Main.font, Main.fontSize);
		OneCellRenderer leftRenderer = new OneCellRenderer(Main.font, Main.fontSize);
		OneCellRenderer rightRenderer = new OneCellRenderer(Main.font, Main.fontSize);
		leftRenderer.setHorizontalAlignment(JLabel.LEFT);
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);

		table.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
		table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		table.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		table.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
		table.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
		table.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);

		table.setCellSelectionEnabled(false);
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
		ListSelectionModel cellSelectionModel = table.getSelectionModel();
		table.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "selectNextColumnCell");
		table.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "selectNextColumnCell");
		table.addKeyListener(new KeyListener() {
			void copy() {
				tmpString = (String) table.getValueAt(table.getSelectedRow(), table.getSelectedColumn());
			}

			void paste() {
				table.setValueAt(tmpString, table.getSelectedRow(), table.getSelectedColumn());
			}

			public void keyPressed(KeyEvent e) {
				
				if ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0) {
					if (e.getKeyCode() == KeyEvent.VK_X) {
						copy();
						table.setValueAt("", table.getSelectedRow(), table.getSelectedColumn());
					}
					if (e.getKeyCode() == KeyEvent.VK_C)
						copy();
					if (e.getKeyCode() == KeyEvent.VK_V)
						paste();
				}
				switch (e.getKeyCode()) {
				case KeyEvent.VK_DELETE:
					table.setValueAt("", table.getSelectedRow(), table.getSelectedColumn());
					break;
				case KeyEvent.VK_ENTER:
					if (table.getSelectedColumn() == 4) {
						table.setColumnSelectionInterval(7, 7);
						table.setRowSelectionInterval(table.getSelectedRow(), table.getSelectedRow());
					}
					eraseText=true;
					break;
				case KeyEvent.VK_RIGHT:
					if (table.getSelectedColumn() == 4) {
						table.setColumnSelectionInterval(7, 7);
						table.setRowSelectionInterval(table.getSelectedRow(), table.getSelectedRow());
					}
					eraseText=true;
					break;
				
					/*
					System.out.println("ww");
					if (table.getValueAt(table.getSelectedRow(), table.getSelectedColumn()) != null) {
						int a = table.getValueAt(table.getSelectedRow(), table.getSelectedColumn()).toString().length() - 1;
						if (a <= 0)
							table.setValueAt(null, table.getSelectedRow(), table.getSelectedColumn());
						else {
									  table.setValueAt(table.getValueAt( table.getSelectedRow(),
									  table.getSelectedColumn()).toString().substring(0,a), table.getSelectedRow(),
									  table.getSelectedColumn()); System.out.println(a);
									 
						}

						Main.modify = true;
					}
					*/
				case KeyEvent.VK_UP:
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_LEFT:
					
					eraseText=true;
					break;
				default :
					if (eraseText){
						Character c;
						c = e.getKeyChar();
						
						if (c.toString().matches(".*[a-zA-Z0-9§°-§æ§ø-§”∞°-∆R`~!@#$%^&*()-_=+|{};:',.<>/].*")){
							eraseText=false;
							table.setValueAt("", table.getSelectedRow(), table.getSelectedColumn());
							System.out.println(c);
						}
						break;
					}
				}
				valueChangedSet(table, rowMax);
			}

			public void keyTyped(KeyEvent e) {
				
				Character c;
				c = e.getKeyChar();
				/*
				if (eraseText){
					eraseText=false;
					table.setValueAt("", table.getSelectedRow(), table.getSelectedColumn());
					valueChangedSet(table, rowMax);
					System.out.println(eraseText);
				}
				*/
				if (c.toString().matches(".*[^a-zA-Z0-9§°-§æ§ø-§”∞°-∆R`~!@#$%^&*()-_=+|{};:',.<>/??].*")) {
					System.out.println("∏Æ≈œ");
					valueChangedSet(table, rowMax);
					return;
				}

				else if (table.isSetCellEditable(table.getSelectedRow(), table.getSelectedColumn())) {
					keyPressed(e);
					if (!e.isControlDown()) {
						String s = (String) table.getValueAt(table.getSelectedRow(), table.getSelectedColumn());
						if (table.getSelectedColumn() == 5) {
							table.setColumnSelectionInterval(0, 0);
							table.setRowSelectionInterval(table.getSelectedRow() + 1, table.getSelectedRow() + 1);
						}
						if (table.getSelectedColumn() == 5 && table.getSelectedRow() == rowMax - 1) {
							table.setColumnSelectionInterval(0, 0);
							table.setRowSelectionInterval(table.getSelectedRow(), table.getSelectedRow());
						}
						if (s == null || s == "\r\n")
							s = "";
						table.setValueAt(s + e.getKeyChar(), table.getSelectedRow(), table.getSelectedColumn());
						Main.modify = true;
					};
				}
				valueChangedSet(table, rowMax);
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		});
		
		 cellSelectionModel.addListSelectionListener(new ListSelectionListener() { 
			 public void valueChanged(ListSelectionEvent e) { 
				 eraseText=true;
					valueChangedSet(table, rowMax);
				 }
			 }
		 );
		return table;
	}

	class OneCellRenderer extends DefaultTableCellRenderer {
		String font;
		int size;

		OneCellRenderer(String font, int size) {
			this.font = font;
			this.size = size;
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			comp.setBackground(Color.white);
			comp.setFont(new Font(font, Font.BOLD, size));
/*
			if ((row == table.getRowCount() - 1) && (column == 5)) {
				if (flag == 1) {
					if (table.getRowCount() == Row) {
						comp.setFont(new Font(font, Font.BOLD, size / 4 * 5));
						comp.setBackground(Color.getHSBColor((float) 0.160, (float) 0.42, (float) 0.98));
					}
				} else if (flag == curPage) {
					if (table.getRowCount() == RowMax) {
						comp.setFont(new Font(font, Font.BOLD, size / 4 * 5));
						comp.setBackground(Color.getHSBColor((float) 0.160, (float) 0.42, (float) 0.98));
					}
				}
			} else {
				*/
			
				if (column == 6) {
					comp.setBackground(Main.color);
				}
				comp.setFont(new Font(font, Font.BOLD, size));
			
			return comp;
		}
	}


}

class MouseLis extends MouseAdapter {
	JPanel panel;
	JTable table;
	PopupMenu popup;

	MouseLis(JPanel panel, JTable table, PopupMenu popup) {
		this.panel = panel;
		this.table = table;
		this.popup = popup;
	}

	public void mouseClicked(MouseEvent e) {
		int row = table.rowAtPoint(e.getPoint());
		int col = table.columnAtPoint(e.getPoint());
		table.setRowSelectionInterval(row, row);
		table.setColumnSelectionInterval(col, col);
		if (e.getButton() == MouseEvent.BUTTON3){
			popup.show(panel, e.getX() + 1, e.getY() + 35);
		}
	}
}

class MyJTable extends JTable {
	public MyJTable(Object[][] row, Object[] column) {
		super(row, column);
	}

	public boolean isSetCellEditable(int row, int column) {
		if (column == 5 || column == 6) {
			return false;
		}
		/*
		if (SupTable.curPage == SupTable.flag) {
			if (SupTable.curPage == 1) {
				if (row == SupTable.Row - 1) {
					return false;
				}
			} else {
				if (row == SupTable.RowMax - 1) {
					return false;
				}
			}
		}
		*/
		return true;
	}

	public boolean isCellEditable(int row, int column) {
		if (column == 5 || column == 6) {
			return false;
		}
		/*
		if (SupTable.curPage == SupTable.flag) {
			if (SupTable.curPage == 1) {
				if (row == SupTable.Row - 1) {
					return false;
				}
			} else {
				if (row == SupTable.RowMax - 1) {
					return false;
				}
			}
		}
		*/
		return super.isCellEditable(row, column); // or maybe simply "true"

	}
}