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

public class SupplyTable {
	static int FrontRow = 22; // Àü¸é ¸®½ºÆ® Çà¼ö
	static int BackRow = 33; // ÈÄ¸é ¸®½ºÆ® Çà¼ö
	static int maxPage = 1; // ÀüÃ¼ ÆäÀÌÁö
	static int currPage = 1; // ÇöÀç ÆäÀÌÁö

	TableList tableList; // Å×ÀÌºí ¸®½ºÆ®
	String strList[][]; // Å×ÀÌºí ¹®ÀÚ¿­

	MyJTable frontTable; // Àü¸é Å×ÀÌºí
	MyJTable backTable; // ÈÄ¸é Å×ÀÌºí

	JTextField sumText, sumTextBottom; // Àü¸é ÇÕ°è,ÇÏ´Ü ÇÕ°è
	JTextField sumLabelField, sumBlankField; // ÇÏ´Ü ÇÕ°è ·¹ÀÌºí ÇÊµå¿Í ºóÄ­ ÇÊµå
	JLabel sumTextLabel; // Àü¸é ÇÕ°è±Ý¾× ¶óº§

	int index = 0; // ÆäÀÌÁöº° Ã¹ Çà À§Ä¡
	int selx = 0, sely = 0; // ¼±ÅÃÇÑ Çà·Ä
	int calcData = 0; // °è»êµÈ ÇÕ°è±Ý¾×
	String copyString = null; // º¹»ç ºÙ¿©³Ö±â¸¦ À§ÇÑ ¹®ÀÚ¿­

	// ÆË¾÷ ¸Þ´º - Çà Ãß°¡,Á¦°Å

	SupplyTable(JPanel front, JPanel back) {

		tableList = new TableList(this);
		strList = tableList.init(); // string ¹è¿­ »ý¼º
		sumTextInit();

		Object frontRow[][] = new Object[FrontRow][8]; // Àü¸é Å×ÀÌºí Çà·Ä
		Object backRow[][] = new Object[BackRow][8]; // ÈÄ¸é Å×ÀÌºí Çà·Ä
		Object column[] = { "Ç°¸ñ", "±Ô°Ý", "ÀÚÀçºñ", "°¡°øºñ", "¼ö·®", "´Ü°¡", "°ø±Þ°¡¾×", "ºñ°í" };

		frontTable = new MyJTable((Object[][]) frontRow, column);
		backTable = new MyJTable((Object[][]) backRow, column);
		// Çì´õ ÆùÆ®
		frontTable.getTableHeader().setFont(new Font(Main.font, 0, Main.fontSize));
		backTable.getTableHeader().setFont(new Font(Main.font, 0, Main.fontSize));

		// ½ºÅ©·ÑÆÒ Á¶Á¤
		JScrollPane frontScroll = new JScrollPane(frontTable);
		frontScroll.setBackground(Color.WHITE);
		JScrollPane backScroll = new JScrollPane(backTable);
		backScroll.setBackground(Color.WHITE);
		frontScroll.setPreferredSize(new Dimension(720, 538));
		frontScroll.setBounds(0, 0, 720, 538);
		backScroll.setPreferredSize(new Dimension(720, 814));
		backScroll.setBounds(0, 0, 720, 814);
		// Å×ÀÌºí ÃÊ±âÈ­
		tableInit(frontTable);
		tableInit(backTable);
		// ÆË¾÷ ÃÊ±âÈ­
		popupInit(front, back);
		// ÆÐ³Î¿¡ Ãß°¡
		front.add(frontScroll);
		back.add(backScroll);

		valueChangedUpdate(frontTable);
	}

	class MenuItemActionListener implements ActionListener {
		JTable table;

		MenuItemActionListener(JTable table) {
			this.table = table;
		}

		public void actionPerformed(ActionEvent e) {
			if (((MenuItem) e.getSource()).getLabel() == "Çà Ãß°¡") {
				tableList.addColumn(table.getSelectedRow());
				tableUpdate(frontTable);
				tableUpdate(backTable);
			} else if (((MenuItem) e.getSource()).getLabel() == "Çà Á¦°Å") {
				tableList.removeColumn(table.getSelectedRow());
				tableUpdate(frontTable);
				tableUpdate(backTable);
			} else if (((MenuItem) e.getSource()).getLabel() == "º¹»ç") {
				copyString = (String) table.getValueAt(table.getSelectedRow(), table.getSelectedColumn());
			} else if (((MenuItem) e.getSource()).getLabel() == "Àß¶ó³»±â") {
				copyString = (String) table.getValueAt(table.getSelectedRow(), table.getSelectedColumn());
				table.setValueAt("", table.getSelectedRow(), table.getSelectedColumn());
				valueChangedUpdate(table);
			} else if (((MenuItem) e.getSource()).getLabel() == "ºÙ¿©³Ö±â") {
				if (table.getSelectedColumn() != 5 && table.getSelectedColumn() != 6)
					table.setValueAt(copyString, table.getSelectedRow(), table.getSelectedColumn());
			}
		}
	}

	private void popupInit(JPanel frontPanel, JPanel backPanel) {
		MenuItem menuItem;
		PopupMenu frontPopup = new PopupMenu();
		PopupMenu backPopup = new PopupMenu();

		MenuItemActionListener frontMenu = new MenuItemActionListener(frontTable);
		MenuItemActionListener backMenu = new MenuItemActionListener(backTable);

		menuItem = new MenuItem("Çà Ãß°¡");
		menuItem.addActionListener(frontMenu);
		frontPopup.add(menuItem);
		menuItem = new MenuItem("Çà Á¦°Å");
		menuItem.addActionListener(frontMenu);
		frontPopup.add(menuItem);
		menuItem = new MenuItem("º¹»ç");
		menuItem.addActionListener(frontMenu);
		frontPopup.add(menuItem);
		menuItem = new MenuItem("Àß¶ó³»±â");
		menuItem.addActionListener(frontMenu);
		frontPopup.add(menuItem);
		menuItem = new MenuItem("ºÙ¿©³Ö±â");
		menuItem.addActionListener(frontMenu);
		frontPopup.add(menuItem);
		menuItem = new MenuItem("Çà Ãß°¡");
		menuItem.addActionListener(backMenu);
		backPopup.add(menuItem);
		menuItem = new MenuItem("Çà Á¦°Å");
		menuItem.addActionListener(backMenu);
		backPopup.add(menuItem);
		menuItem = new MenuItem("º¹»ç");
		menuItem.addActionListener(backMenu);
		backPopup.add(menuItem);
		menuItem = new MenuItem("Àß¶ó³»±â");
		menuItem.addActionListener(backMenu);
		backPopup.add(menuItem);
		menuItem = new MenuItem("ºÙ¿©³Ö±â");
		menuItem.addActionListener(backMenu);
		backPopup.add(menuItem);

		setPopup(frontPanel, backPanel, frontPopup, backPopup);
	}

	private void setPopup(JPanel frontPanel, JPanel backPanel, PopupMenu frontPopup, PopupMenu backPopup) {
		frontPanel.add(frontPopup);
		backPanel.add(backPopup);
		frontTable.addMouseListener(new MouseLis(frontPanel, frontTable, frontPopup));
		backTable.addMouseListener(new MouseLis(backPanel, backTable, backPopup));
	}

	private void sumTextInit() {
		sumTextLabel = new JLabel("ÇÕ°è±Ý¾× ");
		sumText = new JTextField(10);
		sumTextBottom = new JTextField(10);
		sumBlankField = new JTextField(10);
		sumLabelField = new JTextField(10);

		sumText.setEditable(false);
		sumTextBottom.setEditable(false);
		sumLabelField.setEditable(false);
		sumBlankField.setEditable(false);

		sumTextLabel.setBounds(15, 260, 400, 25);
		sumTextLabel.setFont(new Font(Main.font, Font.BOLD, 25));
		sumText.setFont(new Font(Main.font, Font.BOLD, 25));
		sumText.setHorizontalAlignment(JTextField.RIGHT);
		sumText.setBackground(Main.color);
		sumText.setBounds(132, 257, 220, 30);

		sumBlankField.setBackground(Color.white);
		sumBlankField.setBounds(50, 861, 719, 35);

		sumLabelField.setText("ÇÕ°è");
		sumLabelField.setFont(new Font(Main.font, Font.BOLD, Main.fontSize / 2 * 3));
		sumLabelField.setHorizontalAlignment(JTextField.CENTER);
		sumLabelField.setBackground(Color.white);
		sumLabelField.setBounds(50, 861, 301, 35);

		sumTextBottom.setFont(new Font(Main.font, Font.BOLD, Main.fontSize));
		sumTextBottom.setHorizontalAlignment(JTextField.RIGHT);
		sumTextBottom.setBackground(Main.color);
		sumTextBottom.setBounds(639, 861, 87, 35);
	}
	
	void tableUpdate(JTable table) {
		tableList.isFull(index + table.getRowCount());
		//null·Î Å×ÀÌºí ÃÊ±âÈ­
		for (int i = 0; i < table.getRowCount(); i++) {
			for (int j = 0; j < table.getColumnCount(); j++) {
				table.setValueAt(null, i, j);
			}
		}
		//Å×ÀÌºí¿¡ µ¥ÀÌÅÍ ÀÔ·Â
		for (int i = index; i < index + table.getRowCount(); i++) {
			for (int j = 0; j < 5; j++) {
				table.setValueAt(strList[i][j], i - index, j);
			}
			table.setValueAt(strList[i][5], i - index, 7);
		}
		valueChangedUpdate(table);
	}

	protected void valueChangedUpdate(JTable table) {
		//µ¥ÀÌÅÍ º¯°æ¿¡ µû¸¥ Å×ÀÌºí ¾÷µ¥ÀÌÆ®
		int sumData = 0;
		int mulData = 0;
		int max = table.getRowCount();
		selx = table.getSelectedColumn();
		sely = table.getSelectedRow();

		// Àß¸øµÈ µ¥ÀÌÅÍ¸¦ °É·¯³»°í ´Ü°¡¿Í °ø±Þ°¡¾× ÀÔ·Â
		for (int i = 0; i < max; i++) {
			sumData = 0;
			mulData = 0;
			for (int j = 0; j < 8; j++) {
				if (table.getValueAt(i, j) != null) {
					if (!table.getValueAt(i, j).toString()
							.matches("[a-zA-Z0-9¤¡-¤¾¤¿-¤Ó°¡-ÆR`~!@#$%^&*()-_=+|{};:',.<>/]+")) {
						table.setValueAt(null, i, j);
					}
					if (j >= 2 && j <= 4) {
						if (toStrFormat(table.getValueAt(i, j)) == null)
							table.setValueAt(null, i, j);
						else {
							table.setValueAt(toNumFormat(toIntFormat(table.getValueAt(i, j))), i, j);
							if (j <= 3)
								sumData += toIntFormat(table.getValueAt(i, j));
							else
								mulData = sumData * toIntFormat(table.getValueAt(i, j));
						}
					}
				}
				if (j == 5) {
					if (sumData != 0)
						table.setValueAt(toNumFormat(sumData), i, 5);
					else
						table.setValueAt(null, i, 5);
				} else if (j == 6) {
					if (mulData != 0)
						table.setValueAt(toNumFormat(mulData), i, 6);
					else
						table.setValueAt(null, i, 6);
				}
			}
		}
		//ÃÑ ±Ý¾× ¾÷µ¥ÀÌÆ®
		calcSumDataUpdate();
	}

	void calcSumDataUpdate() {
		int sumData;
		int temp = calcData;
		// µ¥ÀÌÅÍ ¸®½ºÆ®¿¡ Å×ÀÌºí µ¥ÀÌÅÍ ÀúÀå
		if (currPage == 1) {
			tableList.saveList(frontTable, 0, FrontRow);
		} else
			tableList.saveList(backTable, index, BackRow);

		// ÇÕ°è °è»ê
		calcData = 0;
		for (int i = 0; i < tableList.maxSize; i++) {
			sumData = toIntFormat(strList[i][2]) + toIntFormat(strList[i][3]);
			calcData += sumData * toIntFormat(strList[i][4]);
		}

		// Å×ÀÌºí¿¡ Àß¸øµÈ µ¥ÀÌÅÍ°¡ ÀÖÀ» °æ¿ì ÀÌÀüµ¥ÀÌÅÍ »ç¿ë
		if (toNumFormat(calcData) == null) {
			sumText.setText(toNumFormat(temp) + "¿ø");
			sumTextBottom.setText(toNumFormat(temp));
			sumTextBottom.setBounds(639, 861, 87, 35);
			calcData = temp;
		} else {
			// bottomTextField Á¶Àý
			int len = toNumFormat(temp).length() > 10 ? (toNumFormat(temp).length() - 10) * 10 : 0;
			sumText.setText(toNumFormat(calcData) + "¿ø");
			sumTextBottom.setText(toNumFormat(calcData));
			sumTextBottom.setBounds(639 - len, 861, 87 + len, 35);
		}
	}

	final public static String toNumFormat(int num) {
		if (num == 0)
			return "0";
		DecimalFormat df = new DecimalFormat("#,###");
		return df.format(num);
	}

	final static public String toStrFormat(Object object) {
		if (object == null)
			return null;
		String num = (String) object;
		num = num.replaceAll("[^-0-9]", "");
		if (num.matches("^[0-9\\-]+")) {
			return num;
		} else {
			return null;
		}
	}

	final static public int toIntFormat(Object obj) {
		if (obj == null)
			return 0;
		String num = (String) obj;
		num = num.replaceAll("[^-0-9]", "");
		if (num.matches("^[0-9\\-]+")) {
			return Integer.parseInt(num);
		} else {
			return 0;
		}
	}

	private JTable tableInit(final MyJTable table) {
		// Å×ÀÌºí °¡·ÎÅ©±â
		table.getColumn("Ç°¸ñ").setPreferredWidth(Main.tableSize[0]);
		table.getColumn("±Ô°Ý").setPreferredWidth(Main.tableSize[1]);
		table.getColumn("ÀÚÀçºñ").setPreferredWidth(Main.tableSize[2]);
		table.getColumn("°¡°øºñ").setPreferredWidth(Main.tableSize[3]);
		table.getColumn("¼ö·®").setPreferredWidth(Main.tableSize[4]);
		table.getColumn("´Ü°¡").setPreferredWidth(Main.tableSize[5]);
		table.getColumn("°ø±Þ°¡¾×").setPreferredWidth(Main.tableSize[6]);
		table.getColumn("ºñ°í").setPreferredWidth(Main.tableSize[7]);
		// Å×ÀÌºí ¼¼·ÎÅ©±â
		table.setRowHeight(23);
		// ·»´õ¸µ°ú ÅØ½ºÆ® Á¤·Ä¹æ½Ä
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

		// table.setCellSelectionEnabled(true); //¼¿ ¼±ÅÃ Çã¿ë
		table.getTableHeader().setReorderingAllowed(false); // Çì´õ Àç¹èÄ¡ ºÒ°¡
		table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN); // °¡·Î Å©±â Á¶Àý °¡´É
		ListSelectionModel cellSelectionModel = table.getSelectionModel(); // ¼¿
																			// ¸ðµ¨
		table.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "selectNextColumnCell");
		table.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "selectNextColumnCell");
		table.addKeyListener(new KeyListener() {
			void copy() { // º¹»ç
				copyString = (String) table.getValueAt(table.getSelectedRow(), table.getSelectedColumn());
			}

			void paste() { // ºÙ¿©³Ö±â
				if (table.getSelectedColumn() != 5 && table.getSelectedColumn() != 6)
					table.setValueAt(copyString, table.getSelectedRow(), table.getSelectedColumn());
			}

			// Å° ¼¼ÆÃ
			public void keyPressed(KeyEvent e) {
				if ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0) {
					if (e.getKeyCode() == KeyEvent.VK_X) { // ctrl + x Àß¶ó³»±â
						copy();
						table.setValueAt("", table.getSelectedRow(), table.getSelectedColumn());
					}
					if (e.getKeyCode() == KeyEvent.VK_C) // ctrl + c º¹»ç
						copy();
					if (e.getKeyCode() == KeyEvent.VK_V) // ctrl + v ºÙ¿©³Ö±â
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
					break;
				case KeyEvent.VK_RIGHT:
					if (table.getSelectedColumn() == 4) {
						table.setColumnSelectionInterval(7, 7);
						table.setRowSelectionInterval(table.getSelectedRow(), table.getSelectedRow());
					}
				case KeyEvent.VK_UP:
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_LEFT:
					break;
				default:
				}
				valueChangedUpdate(table);
			}

			public void keyTyped(KeyEvent e) {
				Character c;
				c = e.getKeyChar();
				if (c.toString().matches("[^a-zA-Z0-9¤¡-¤¾¤¿-¤Ó°¡-ÆR`~!@#$%^&*()-_=+|{};:',.<>/??]+")) {
					valueChangedUpdate(table);
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
						if (table.getSelectedColumn() == 5 && (table.getSelectedRow() == table.getRowCount() - 1)) {
							table.setColumnSelectionInterval(0, 0);
							table.setRowSelectionInterval(table.getSelectedRow(), table.getSelectedRow());
						}
						if (s == null || s == "\r\n")
							s = "";
						table.setValueAt(s + e.getKeyChar(), table.getSelectedRow(), table.getSelectedColumn());
						Main.modify = true;
					}
				}
				valueChangedUpdate(table);
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		});

		cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				valueChangedUpdate(table);
			}
		});
		return table;
	}

	// Å×ÀÌºí Å©±â Á¶Á¤ load data
	public void setTableSize(String[] stn) {
		int pos = 0;
		frontTable.getColumn("Ç°¸ñ").setPreferredWidth(Integer.parseInt(stn[pos++]));
		frontTable.getColumn("±Ô°Ý").setPreferredWidth(Integer.parseInt(stn[pos++]));
		frontTable.getColumn("ÀÚÀçºñ").setPreferredWidth(Integer.parseInt(stn[pos++]));
		frontTable.getColumn("°¡°øºñ").setPreferredWidth(Integer.parseInt(stn[pos++]));
		frontTable.getColumn("¼ö·®").setPreferredWidth(Integer.parseInt(stn[pos++]));
		frontTable.getColumn("´Ü°¡").setPreferredWidth(Integer.parseInt(stn[pos++]));
		frontTable.getColumn("°ø±Þ°¡¾×").setPreferredWidth(Integer.parseInt(stn[pos++]));
		frontTable.getColumn("ºñ°í").setPreferredWidth(Integer.parseInt(stn[pos++]));
	}
}

class OneCellRenderer extends DefaultTableCellRenderer {
	String font;
	int size;

	OneCellRenderer(String font, int size) {
		this.font = font;
		this.size = size;
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		comp.setBackground(Color.white);
		comp.setFont(new Font(font, Font.BOLD, size));
		if (column == 6) { // °ø±Þ°¡¾×
			comp.setBackground(Main.color);
		}
		comp.setFont(new Font(font, Font.BOLD, size));
		return comp;
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
		if (e.getButton() == MouseEvent.BUTTON3) {
			popup.show(panel, e.getX(), e.getY());// 13,41
		}
	}
}

class MyJTable extends JTable {
	public MyJTable(Object[][] row, Object[] column) {
		super(row, column);
	}

	public boolean isSetCellEditable(int row, int column) {
		// ´Ü°¡ °ø±Þ°¡¾× ÆíÁý ºÒ°¡
		if (column == 5 || column == 6) {
			return false;
		}
		return true;
	}

	public boolean isCellEditable(int row, int column) {
		// ´Ü°¡ °ø±Þ°¡¾× ÆíÁý ºÒ°¡
		if (column == 5 || column == 6) {
			return false;
		}
		return true;
	}
}