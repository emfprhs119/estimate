package Product;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
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

import FrameComponent.ViewManager;
import FrameComponent.WhitePanel;
import Main.Main;

public class ProductView {
	private int maxPage = 1; // ÀüÃ¼ ÆäÀÌÁö
	private int currPage = 1; // ÇöÀç ÆäÀÌÁö

	ProductList productList; // »óÇ° ¸®½ºÆ®

	WhitePanel frontTablePanel;
	WhitePanel backTablePanel;

	MyJTable frontTable; // Àü¸é Å×ÀÌºí
	MyJTable backTable; // ÈÄ¸é Å×ÀÌºí

	private JTextField sumText; // Àü¸é ÇÕ°è,ÇÏ´Ü ÇÕ°è
	private JTextField sumTextBottom;
	private JTextField sumLabelField; // ÇÏ´Ü ÇÕ°è ·¹ÀÌºí ÇÊµå¿Í ºóÄ­ ÇÊµå
	private JTextField sumBlankField;
	private JLabel sumTextLabel; // Àü¸é ÇÕ°è±Ý¾× ¶óº§

	int selx = 0, sely = 0; // ¼±ÅÃÇÑ Çà·Ä
	long calcData = 0; // °è»êµÈ ÇÕ°è±Ý¾×
	String copyString = null; // º¹»ç ºÙ¿©³Ö±â¸¦ À§ÇÑ ¹®ÀÚ¿­

	String funcStr[] = { "Çà Ãß°¡ (ctrl+shift+a)", "Çà Á¦°Å (ctrl+shift+d)", "Çà º¹»ç (ctrl+shift+c)", 
			"Çà Àß¶ó³»±â (ctrl+shift+x)", "Çà ºÙ¿©³Ö±â (ctrl+shift+v)","Çà ¿Ã¸®±â (ctrl+shift+up)", "Çà ³»¸®±â (ctrl+shift+down)",
			"¼¿ º¹»ç (ctrl+c)", "¼¿ Àß¶ó³»±â (ctrl+x)", "¼¿ ºÙ¿©³Ö±â (ctrl+v)" };
	Clipboard clipboard;
	public ProductView(ViewManager viewManager) {
		sumTextInit();
		productList = new ProductList();
		clipboard=new Clipboard();
		Object frontRow[][] = new Object[Main.FrontRow][8]; // Àü¸é Å×ÀÌºí Çà·Ä
		Object backRow[][] = new Object[Main.BackRow][8]; // ÈÄ¸é Å×ÀÌºí Çà·Ä
		Object column[] = { "Ç°¸ñ", "±Ô°Ý", "ÀÚÀçºñ", "°¡°øºñ", "¼ö·®", "´Ü°¡", "°ø±Þ°¡¾×", "ºñ°í" };
		// Å×ÀÌºí ÆÐ³Î
		frontTablePanel = new WhitePanel();
		backTablePanel = new WhitePanel();

		frontTablePanel.setBounds(18, 290, 720, 635);
		backTablePanel.setBounds(18, 37, 720, 840);
		// Å×ÀÌºí »ý¼º
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
		frontScroll.setBounds(0, 0, 720, 538);
		backScroll.setBounds(0, 0, 720, 791);
		// Å×ÀÌºí ÃÊ±âÈ­
		tableInit(frontTable);
		tableInit(backTable);
		// ÆË¾÷ ÃÊ±âÈ­
		popupInit(frontTablePanel, backTablePanel);
		// ÆÐ³Î¿¡ Ãß°¡

		frontTablePanel.add(frontScroll);
		backTablePanel.add(backScroll);

		valueChangedUpdate(frontTable);
	}
	private void popupInit(JPanel frontPanel, JPanel backPanel) {
		MenuItem menuItem;
		PopupMenu frontPopup = new PopupMenu();
		PopupMenu backPopup = new PopupMenu();

		MenuItemActionListener frontMenu = new MenuItemActionListener(this,frontTable);
		MenuItemActionListener backMenu = new MenuItemActionListener(this,backTable);
		
		for(int i=0;i<funcStr.length;i++){
		menuItem = new MenuItem(funcStr[i]);
		menuItem.addActionListener(frontMenu);
		frontPopup.add(menuItem);
		menuItem = new MenuItem(funcStr[i]);
		menuItem.addActionListener(backMenu);
		backPopup.add(menuItem);
		}
		setPopup(frontPanel, backPanel, frontPopup, backPopup);
	}

	private void setPopup(JPanel frontPanel, JPanel backPanel, PopupMenu frontPopup, PopupMenu backPopup) {
		frontPanel.add(frontPopup);
		backPanel.add(backPopup);
		frontTable.addMouseListener(new PopupListner(frontPanel, frontTable, frontPopup));
		backTable.addMouseListener(new PopupListner(backPanel, backTable, backPopup));
	}

	private void sumTextInit() {
		setSumTextLabel(new JLabel("ÇÕ°è±Ý¾× "));
		setSumText(new JTextField(10));
		setSumTextBottom(new JTextField(10));
		setSumBlankField(new JTextField(10));
		setSumLabelField(new JTextField(10));

		getSumText().setEditable(false);
		getSumTextBottom().setEditable(false);
		getSumLabelField().setEditable(false);
		getSumBlankField().setEditable(false);

		getSumTextLabel().setBounds(15, 260, 400, 25);
		getSumTextLabel().setFont(new Font(Main.font, Font.BOLD, 25));
		getSumText().setFont(new Font(Main.font, Font.BOLD, 25));
		getSumText().setHorizontalAlignment(JTextField.RIGHT);
		getSumText().setBackground(Main.color);
		getSumText().setBounds(132, 257, 220, 30);

		getSumBlankField().setBackground(Color.white);
		getSumBlankField().setBounds(50, 861, 719, 35);

		getSumLabelField().setText("ÇÕ°è");
		getSumLabelField().setFont(new Font(Main.font, Font.BOLD, Main.fontSize / 2 * 3));
		getSumLabelField().setHorizontalAlignment(JTextField.CENTER);
		getSumLabelField().setBackground(Color.white);
		getSumLabelField().setBounds(50, 861, 301, 35);

		getSumTextBottom().setFont(new Font(Main.font, Font.BOLD, Main.fontSize));
		getSumTextBottom().setHorizontalAlignment(JTextField.RIGHT);
		getSumTextBottom().setBackground(Main.color);
		getSumTextBottom().setBounds(639, 861, 87, 35);
	}

	void tableUpdate(JTable table) {
		// null·Î Å×ÀÌºí ÃÊ±âÈ­
		for (int i = 0; i < table.getRowCount(); i++) {
			for (int j = 0; j < table.getColumnCount(); j++) {
				table.setValueAt(null, i, j);
			}
		}
		// Å×ÀÌºí¿¡ µ¥ÀÌÅÍ ÀÔ·Â
		productList.dataToTable(table, getIndex());
		valueChangedUpdate(table);
	}

	private int getIndex() {
		int index = 0;
		if (currPage != 1)
			index += Main.FrontRow;
		if (currPage > 2)
			index += (currPage - 2) * Main.BackRow;
		return index;
	}

	protected void valueChangedUpdate(JTable table) {
		// µ¥ÀÌÅÍ º¯°æ¿¡ µû¸¥ Å×ÀÌºí ¾÷µ¥ÀÌÆ®
		long sumData = 0;
		long mulData = 0;
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
						if (Main.stringToLongString(table.getValueAt(i, j)) == null)
							table.setValueAt(null, i, j);
						else {
							table.setValueAt(Main.longToMoneyString(Main.StringToLong(table.getValueAt(i, j))), i, j);
							if (j <= 3)
								sumData += Main.StringToLong(table.getValueAt(i, j));
							else
								mulData = sumData * Main.StringToLong(table.getValueAt(i, j));
						}
					}
				}
				if (j == 5) {
					if (sumData != 0)
						table.setValueAt(Main.longToMoneyString(sumData), i, 5);
					else
						table.setValueAt(null, i, 5);
				} else if (j == 6) {
					if (mulData != 0)
						table.setValueAt(Main.longToMoneyString(mulData), i, 6);
					else
						table.setValueAt(null, i, 6);
				}
			}
		}
		// ÃÑ ±Ý¾× ¾÷µ¥ÀÌÆ®
		calcSumDataUpdate(table);
	}

	void calcSumDataUpdate(JTable table) {
		// µ¥ÀÌÅÍ ¸®½ºÆ®¿¡ Å×ÀÌºí µ¥ÀÌÅÍ ÀúÀå
		productList.tableToData(table, getIndex());
		// ÇÕ°è °è»ê
		calcData = productList.getSumMoney();
		if (calcData > 999999999999L) {
			getSumText().setText("NaN");
			getSumTextBottom().setText("NaN");
			return;
		}
		// bottomTextField Á¶Àý
		int len = Main.longToMoneyString(calcData).length() > 10 ? (Main.longToMoneyString(calcData).length() - 10) * 10 : 0;
		getSumText().setText(Main.longToMoneyString(calcData) + "¿ø");
		getSumTextBottom().setText(Main.longToMoneyString(calcData));
		getSumTextBottom().setBounds(639 - len, 861, 87 + len, 35);
	}
	void clipboardCopy(JTable table) { // º¹»ç
		clipboard.copy((String) table.getValueAt(table.getSelectedRow(), table.getSelectedColumn()));
	}
	void clipboardPaste(JTable table) { // ºÙ¿©³Ö±â
		if (table.getSelectedColumn() != 5 && table.getSelectedColumn() != 6){
			productList.setData(clipboard.pasteData(), table.getSelectedRow(), table.getSelectedColumn());
		}
	}
	private void tableInit(final MyJTable table) {
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

		table.getTableHeader().setReorderingAllowed(false); // Çì´õ Àç¹èÄ¡ ºÒ°¡
		table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN); // °¡·Î Å©±â Á¶Àý °¡´É
		table.setDragEnabled(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // ÇÑ ¼¿¸¸ ¼±ÅÃ °¡´É
		table.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "selectNextColumnCell");
		table.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "selectNextColumnCell");
		

		table.addKeyListener(new KeyListener() {
			// Å° ¼¼ÆÃ
			public void keyPressed(KeyEvent e) {
				if ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0) {
					if (e.getKeyCode() == KeyEvent.VK_X) { // ctrl + x Àß¶ó³»±â
						//clipboardCopy(table);
						table.setValueAt(null, table.getSelectedRow(), table.getSelectedColumn());
					}
					if (e.getKeyCode() == KeyEvent.VK_C){ // ctrl + c º¹»ç
						//clipboardCopy(table);
					}
					if (e.getKeyCode() == KeyEvent.VK_V){ // ctrl + v ºÙ¿©³Ö±â
						clipboardPaste(table);
						tableUpdate(table);
					}
					if ((e.getModifiers() & KeyEvent.SHIFT_MASK) != 0) {
						if (e.getKeyCode() == KeyEvent.VK_A) { 
							productList.addRow(table.getSelectedRow());
						}
						if (e.getKeyCode() == KeyEvent.VK_D) { 
							productList.removeRow(table.getSelectedRow());
						}
						if (e.getKeyCode() == KeyEvent.VK_X) { 
							productList.copyRow(table.getSelectedRow());
							productList.removeRow(table.getSelectedRow());
						}
						if (e.getKeyCode() == KeyEvent.VK_C){
							productList.copyRow(table.getSelectedRow());
						}
						if (e.getKeyCode() == KeyEvent.VK_V){
							productList.pasteRow(table.getSelectedRow());
						}
						if (e.getKeyCode() == KeyEvent.VK_UP)
							productList.shiftUpRow(table.getSelectedRow());
						if (e.getKeyCode() == KeyEvent.VK_DOWN)
							productList.shiftDownRow(table.getSelectedRow());
						tableUpdate(table);
					}
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
				/*
				table.editCellAt(table.getSelectedRow(), table.getSelectedColumn());
				Component editor = table.getEditorComponent();
		        editor.requestFocusInWindow();
				 */
				Character c = e.getKeyChar();
				// Çã¿ë ¹®ÀÚ
				if (c.toString().matches("[^a-zA-Z0-9¤¡-¤¾¤¿-¤Ó°¡-ÆR`~!@#$%^&*()-_=+|{};:',.<>?]+")) {
					valueChangedUpdate(table);
					return;
				} else if (table.isSetCellEditable(table.getSelectedRow(), table.getSelectedColumn())) {
					keyPressed(e);
					if (!e.isControlDown()) {
						//System.out.println(table.get.length);
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
/*
		cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				valueChangedUpdate(table);
			}
		});
		*/
	}

	// Å×ÀÌºí Å©±â Á¶Á¤ load data
	public void setTableWidth(String[] stn) {
		int index = 0;
		frontTable.getColumn("Ç°¸ñ").setPreferredWidth(Integer.parseInt(stn[index++]));
		frontTable.getColumn("±Ô°Ý").setPreferredWidth(Integer.parseInt(stn[index++]));
		frontTable.getColumn("ÀÚÀçºñ").setPreferredWidth(Integer.parseInt(stn[index++]));
		frontTable.getColumn("°¡°øºñ").setPreferredWidth(Integer.parseInt(stn[index++]));
		frontTable.getColumn("¼ö·®").setPreferredWidth(Integer.parseInt(stn[index++]));
		frontTable.getColumn("´Ü°¡").setPreferredWidth(Integer.parseInt(stn[index++]));
		frontTable.getColumn("°ø±Þ°¡¾×").setPreferredWidth(Integer.parseInt(stn[index++]));
		frontTable.getColumn("ºñ°í").setPreferredWidth(Integer.parseInt(stn[index++]));
	}

	public void setTableWidth(int[] tableWidth) {
		int index = 0;
		frontTable.getColumn("Ç°¸ñ").setPreferredWidth(tableWidth[index++]);
		frontTable.getColumn("±Ô°Ý").setPreferredWidth(tableWidth[index++]);
		frontTable.getColumn("ÀÚÀçºñ").setPreferredWidth(tableWidth[index++]);
		frontTable.getColumn("°¡°øºñ").setPreferredWidth(tableWidth[index++]);
		frontTable.getColumn("¼ö·®").setPreferredWidth(tableWidth[index++]);
		frontTable.getColumn("´Ü°¡").setPreferredWidth(tableWidth[index++]);
		frontTable.getColumn("°ø±Þ°¡¾×").setPreferredWidth(tableWidth[index++]);
		frontTable.getColumn("ºñ°í").setPreferredWidth(tableWidth[index++]);
	}

	public int[] getTableWidth() {
		int width[] = new int[8];
		int index = 0;
		width[index++] = frontTable.getColumn("Ç°¸ñ").getPreferredWidth();
		width[index++] = frontTable.getColumn("±Ô°Ý").getPreferredWidth();
		width[index++] = frontTable.getColumn("ÀÚÀçºñ").getPreferredWidth();
		width[index++] = frontTable.getColumn("°¡°øºñ").getPreferredWidth();
		width[index++] = frontTable.getColumn("¼ö·®").getPreferredWidth();
		width[index++] = frontTable.getColumn("´Ü°¡").getPreferredWidth();
		width[index++] = frontTable.getColumn("°ø±Þ°¡¾×").getPreferredWidth();
		width[index++] = frontTable.getColumn("ºñ°í").getPreferredWidth();
		return width;
	}

	public JLabel getSumTextLabel() {
		return sumTextLabel;
	}

	public void setSumTextLabel(JLabel sumTextLabel) {
		this.sumTextLabel = sumTextLabel;
	}

	public JTextField getSumText() {
		return sumText;
	}

	public void setSumText(JTextField sumText) {
		this.sumText = sumText;
	}

	public JTextField getSumTextBottom() {
		return sumTextBottom;
	}

	public void setSumTextBottom(JTextField sumTextBottom) {
		this.sumTextBottom = sumTextBottom;
	}

	public JTextField getSumLabelField() {
		return sumLabelField;
	}

	public void setSumLabelField(JTextField sumLabelField) {
		this.sumLabelField = sumLabelField;
	}

	public JTextField getSumBlankField() {
		return sumBlankField;
	}

	public void setSumBlankField(JTextField sumBlankField) {
		this.sumBlankField = sumBlankField;
	}

	public int getCurrPage() {
		return currPage;
	}

	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}

	public int getMaxPage() {
		return maxPage;
	}

	public void setMaxPage(int maxPage) {
		this.maxPage = maxPage;
	}

	public WhitePanel getFrontTablePanel() {
		return frontTablePanel;
	}

	public WhitePanel getBackTablePanel() {
		return backTablePanel;
	}

	public void refresh() {
		if (currPage == 1)
			tableUpdate(frontTable);
		else
			tableUpdate(backTable);
	}

	public ProductList getProductList() {
		return productList;
	}

	public boolean addPage() {
		if (currPage == maxPage) {

			productList.addPage();
			currPage++;
			maxPage++;
			return true;
		}
		return false;
	}

	public boolean removePage() {
		if (currPage == maxPage) {
			productList.removePage();
			if (currPage != 1) {
				currPage--;
				maxPage--;
			}
			return true;
		}
		return false;
	}

	public String getPageStr() {
		return "page" + currPage + "/" + maxPage;
	}

	public void setProductList(ProductList productList) {
		this.productList = productList;
		productList.dataToTable(frontTable, 0);
		maxPage = (productList.getMaxSize() - Main.FrontRow - 1) / Main.BackRow + 1;
		currPage = 1;
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

class PopupListner extends MouseAdapter {
	JPanel panel;
	JTable table;
	PopupMenu popup;

	PopupListner(JPanel panel, JTable table, PopupMenu popup) {
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
			popup.show(panel, e.getX(), e.getY() + 29);
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