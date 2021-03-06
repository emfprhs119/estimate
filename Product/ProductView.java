package Product;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;

import FrameComponent.ViewManager;
import FrameComponent.WhitePanel;
import Main.Main;

//품목 화면
public class ProductView {
	private int maxPage = 1; // 전체 페이지
	private int currPage = 1; // 현재 페이지

	ProductList productList; // 상품 리스트

	WhitePanel frontTablePanel;
	WhitePanel backTablePanel;

	MyJTable frontTable; // 전면 테이블
	MyJTable backTable; // 후면 테이블

	private JTextField sumText; // 전면 합계,하단 합계
	private JTextField sumTextBottom;
	private JTextField sumLabelField; // 하단 합계 레이블 필드와 빈칸 필드
	private JTextField sumBlankField;
	private JLabel sumTextLabel; // 전면 합계금액 라벨

	int selx = 0, sely = 0; // 선택한 행렬
	long calcData = 0; // 계산된 합계금액
	String copyString = null; // 복사 붙여넣기를 위한 문자열

	String funcStr[] = { "행 추가 (ctrl+shift+a)", "행 제거 (ctrl+shift+d)", "행 복사 (ctrl+shift+c)", "행 잘라내기 (ctrl+shift+x)",
			"행 붙여넣기 (ctrl+shift+v)", "행 올리기 (ctrl+shift+up)", "행 내리기 (ctrl+shift+down)", "셀 복사 (ctrl+c)",
			"셀 잘라내기 (ctrl+x)", "셀 붙여넣기 (ctrl+v)" };
	Clipboard clipboard;
	ViewManager viewManager;

	public ProductView(ViewManager viewManager) {
		sumTextInit();
		this.viewManager = viewManager;
		productList = new ProductList();
		clipboard = new Clipboard();
		Object frontRow[][] = new Object[Main.FrontRow][8]; // 전면 테이블 행렬
		Object backRow[][] = new Object[Main.BackRow][8]; // 후면 테이블 행렬
		Object column[] = { "품목", "규격", "자재비", "가공비", "수량", "단가", "공급가액", "비고" };
		// 테이블 패널
		frontTablePanel = new WhitePanel();
		backTablePanel = new WhitePanel();

		frontTablePanel.setBounds(18, 290, 720, 635);
		backTablePanel.setBounds(18, 37, 720, 840);
		// 테이블 생성
		frontTable = new MyJTable((Object[][]) frontRow, column);
		backTable = new MyJTable((Object[][]) backRow, column);
		// 헤더 폰트
		frontTable.getTableHeader().setFont(new Font(Main.font, 0, Main.fontSize));
		backTable.getTableHeader().setFont(new Font(Main.font, 0, Main.fontSize));

		// 스크롤팬 조정
		JScrollPane frontScroll = new JScrollPane(frontTable);
		frontScroll.setBackground(Color.WHITE);
		JScrollPane backScroll = new JScrollPane(backTable);
		backScroll.setBackground(Color.WHITE);
		frontScroll.setBounds(0, 0, 720, 538);
		backScroll.setBounds(0, 0, 720, 791);
		// 테이블 초기화
		tableInit(frontTable);
		tableInit(backTable);
		// 팝업 초기화
		popupInit(frontTablePanel, backTablePanel);
		// 패널에 추가

		frontTablePanel.add(frontScroll);
		backTablePanel.add(backScroll);

		valueChangedUpdate(frontTable);
	}

	private void popupInit(JPanel frontPanel, JPanel backPanel) {
		MenuItem menuItem;
		PopupMenu frontPopup = new PopupMenu();
		PopupMenu backPopup = new PopupMenu();

		MenuItemActionListener frontMenu = new MenuItemActionListener(this, frontTable);
		MenuItemActionListener backMenu = new MenuItemActionListener(this, backTable);

		for (int i = 0; i < funcStr.length; i++) {
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
		setSumTextLabel(new JLabel("합계금액 "));
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
		getSumText().setBackground(Main.YELLOW);
		getSumText().setBounds(132, 257, 220, 30);

		getSumBlankField().setBackground(Color.white);
		getSumBlankField().setBounds(50, 861, 719, 35);

		getSumLabelField().setText("합계");
		getSumLabelField().setFont(new Font(Main.font, Font.BOLD, Main.fontSize / 2 * 3));
		getSumLabelField().setHorizontalAlignment(JTextField.CENTER);
		getSumLabelField().setBackground(Color.white);
		getSumLabelField().setBounds(50, 861, 301, 35);

		getSumTextBottom().setFont(new Font(Main.font, Font.BOLD, Main.fontSize));
		getSumTextBottom().setHorizontalAlignment(JTextField.RIGHT);
		getSumTextBottom().setBackground(Main.YELLOW);
		getSumTextBottom().setBounds(639, 861, 87, 35);
	}

	void tableUpdate(JTable table) {
		// null로 테이블 초기화
		for (int i = 0; i < table.getRowCount(); i++) {
			for (int j = 0; j < table.getColumnCount(); j++) {
				table.setValueAt(null, i, j);
			}
		}
		// 테이블에 데이터 입력
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
		// 데이터 변경에 따른 테이블 업데이트
		long sumData = 0;
		long mulData = 0;
		int max = table.getRowCount();
		selx = table.getSelectedColumn();
		sely = table.getSelectedRow();

		// 잘못된 데이터를 걸러내고 단가와 공급가액 입력
		for (int i = 0; i < max; i++) {
			sumData = 0;
			mulData = 0;
			for (int j = 0; j < 8; j++) {
				if (table.getValueAt(i, j) != null) {
					if (!table.getValueAt(i, j).toString()
							.matches("[ a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣`~!@#$%&*()-_=+|{};:',.<>/]+")) {
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
		// 총 금액 업데이트
		calcSumDataUpdate(table);
	}

	void calcSumDataUpdate(JTable table) {
		// 데이터 리스트에 테이블 데이터 저장
		productList.tableToData(table, getIndex());
		// 합계 계산
		calcData = productList.getSumMoney();
		if (calcData > 999999999999L) {
			getSumText().setText("NaN");
			getSumTextBottom().setText("NaN");
			return;
		}
		// bottomTextField 조절
		int len = Main.longToMoneyString(calcData).length() > 10 ? (Main.longToMoneyString(calcData).length() - 10) * 10
				: 0;
		getSumText().setText(Main.longToMoneyString(calcData) + "원");
		getSumTextBottom().setText(Main.longToMoneyString(calcData));
		getSumTextBottom().setBounds(639 - len, 861, 87 + len, 35);
	}

	void clipboardCopy(JTable table) { // 복사
		clipboard.copy((String) table.getValueAt(table.getSelectedRow(), table.getSelectedColumn()));
	}

	void clipboardPaste(JTable table) { // 붙여넣기
		if (table.getSelectedColumn() != 5 && table.getSelectedColumn() != 6) {
			productList.setData(clipboard.pasteData(), table.getSelectedRow(), table.getSelectedColumn());
		}
	}

	private void tableInit(final MyJTable table) {
		// 테이블 가로크기
		table.getColumn("품목").setPreferredWidth(Main.tableSize[0]);
		table.getColumn("규격").setPreferredWidth(Main.tableSize[1]);
		table.getColumn("자재비").setPreferredWidth(Main.tableSize[2]);
		table.getColumn("가공비").setPreferredWidth(Main.tableSize[3]);
		table.getColumn("수량").setPreferredWidth(Main.tableSize[4]);
		table.getColumn("단가").setPreferredWidth(Main.tableSize[5]);
		table.getColumn("공급가액").setPreferredWidth(Main.tableSize[6]);
		table.getColumn("비고").setPreferredWidth(Main.tableSize[7]);
		// 테이블 세로크기
		table.setRowHeight(23);
		// 렌더링과 텍스트 정렬방식
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

		table.getTableHeader().setReorderingAllowed(false); // 헤더 재배치 불가
		table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN); // 가로 크기 조절 가능
		table.setDragEnabled(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 한 셀만 선택
																		// 가능
		table.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "selectNextColumnCell");
		table.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "selectNextColumnCell");

		table.addKeyListener(new KeyListener() {
			// 키 세팅
			public void keyPressed(KeyEvent e) {
				if ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0) {
					if (e.getKeyCode() == KeyEvent.VK_X) { // ctrl + x 잘라내기
						clipboardCopy(table);
						table.setValueAt(null, table.getSelectedRow(), table.getSelectedColumn());
					}
					if (e.getKeyCode() == KeyEvent.VK_C) { // ctrl + c 복사
						clipboardCopy(table);
					}
					if (e.getKeyCode() == KeyEvent.VK_V) { // ctrl + v 붙여넣기
						clipboardPaste(table);
						tableUpdate(table);
						
					}
					// ctrl + shift function
					if ((e.getModifiers() & KeyEvent.SHIFT_MASK) != 0) {
						if (e.getKeyCode() == KeyEvent.VK_A) {
							productList.addRow(table.getSelectedRow() + getIndex());
						}
						if (e.getKeyCode() == KeyEvent.VK_D) {
							productList.removeRow(table.getSelectedRow() + getIndex());
						}
						if (e.getKeyCode() == KeyEvent.VK_X) {
							productList.copyRow(table.getSelectedRow() + getIndex());
							productList.removeRow(table.getSelectedRow() + getIndex());
						}
						if (e.getKeyCode() == KeyEvent.VK_C) {
							productList.copyRow(table.getSelectedRow() + getIndex());
						}
						if (e.getKeyCode() == KeyEvent.VK_V) {
							productList.pasteRow(table.getSelectedRow() + getIndex());
						}
						if (e.getKeyCode() == KeyEvent.VK_UP)
							productList.shiftUpRow(table.getSelectedRow() + getIndex());
						if (e.getKeyCode() == KeyEvent.VK_DOWN)
							productList.shiftDownRow(table.getSelectedRow() + getIndex());
						tableUpdate(table);
						pageRefresh();
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
					break;

				case KeyEvent.VK_UP:
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_LEFT:
					break;
				default:
				}
				valueChangedUpdate(table);
			}

			public void keyTyped(KeyEvent e) {
				Character c = e.getKeyChar();
				// 허용 문자
				if (c.toString().matches("[^a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣`~!@#$%^&*()-_=+|{};:',.<>?]+")) {
					valueChangedUpdate(table);
					return;
				} else if (table.isSetCellEditable(table.getSelectedRow(), table.getSelectedColumn())) {
					keyPressed(e);
					if (!e.isControlDown()) {
						// System.out.println(table.get.length);
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
	}

	// 테이블 크기 조정 load data
	public void setTableWidth(String[] stn) {
		int index = 0;
		frontTable.getColumn("품목").setPreferredWidth(Integer.parseInt(stn[index++]));
		frontTable.getColumn("규격").setPreferredWidth(Integer.parseInt(stn[index++]));
		frontTable.getColumn("자재비").setPreferredWidth(Integer.parseInt(stn[index++]));
		frontTable.getColumn("가공비").setPreferredWidth(Integer.parseInt(stn[index++]));
		frontTable.getColumn("수량").setPreferredWidth(Integer.parseInt(stn[index++]));
		frontTable.getColumn("단가").setPreferredWidth(Integer.parseInt(stn[index++]));
		frontTable.getColumn("공급가액").setPreferredWidth(Integer.parseInt(stn[index++]));
		frontTable.getColumn("비고").setPreferredWidth(Integer.parseInt(stn[index++]));
	}

	public void setTableWidth(int[] tableWidth) {
		int index = 0;
		frontTable.getColumn("품목").setPreferredWidth(tableWidth[index++]);
		frontTable.getColumn("규격").setPreferredWidth(tableWidth[index++]);
		frontTable.getColumn("자재비").setPreferredWidth(tableWidth[index++]);
		frontTable.getColumn("가공비").setPreferredWidth(tableWidth[index++]);
		frontTable.getColumn("수량").setPreferredWidth(tableWidth[index++]);
		frontTable.getColumn("단가").setPreferredWidth(tableWidth[index++]);
		frontTable.getColumn("공급가액").setPreferredWidth(tableWidth[index++]);
		frontTable.getColumn("비고").setPreferredWidth(tableWidth[index++]);
	}

	public int[] getTableWidth() {
		int width[] = new int[8];
		int index = 0;
		width[index++] = frontTable.getColumn("품목").getPreferredWidth();
		width[index++] = frontTable.getColumn("규격").getPreferredWidth();
		width[index++] = frontTable.getColumn("자재비").getPreferredWidth();
		width[index++] = frontTable.getColumn("가공비").getPreferredWidth();
		width[index++] = frontTable.getColumn("수량").getPreferredWidth();
		width[index++] = frontTable.getColumn("단가").getPreferredWidth();
		width[index++] = frontTable.getColumn("공급가액").getPreferredWidth();
		width[index++] = frontTable.getColumn("비고").getPreferredWidth();
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

	// 현재 페이지
	public int getCurrPage() {
		return currPage;
	}

	// 페이지 이동
	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}

	// 최대 페이지
	public int getMaxPage() {
		return maxPage;
	}

	// 최대 페이지 설정
	public void setMaxPage(int maxPage) {
		this.maxPage = maxPage;
	}

	public WhitePanel getFrontTablePanel() {
		return frontTablePanel;
	}

	public WhitePanel getBackTablePanel() {
		return backTablePanel;
	}

	// 새로고침
	public void refresh() {
		if (currPage == 1)
			tableUpdate(frontTable);
		else
			tableUpdate(backTable);
	}

	public void pageRefresh() {
		if (productList.getMaxSize() > Main.FrontRow + (maxPage - 1) * Main.BackRow) {
			maxPage++;
			viewManager.getFrameLabel().setPageText(getPageStr());
		} else if (productList.getMaxSize() < Main.FrontRow + (maxPage - 1) * Main.BackRow) {
			maxPage--;
			viewManager.getFrameLabel().setPageText(getPageStr());
		}
	}

	public ProductList getProductList() {
		return productList;
	}

	public void removeLastPage() {
		if (currPage == maxPage && currPage > 1 && productList.isBlankLastPage()) {
			productList.removePage();
			maxPage--;
		}
	}

	public void addLastPage() {
		if (currPage == maxPage) {
			productList.addPage();
			maxPage++;
		}
	}

	public String getPageStr() {
		return "page" + currPage + "/" + maxPage;
	}

	public void setProductList(ProductList productList) {
		this.productList = productList;
		productList.dataToTable(frontTable, 0);
		maxPage = (productList.getMaxSize() - Main.FrontRow) / Main.BackRow + 1;
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
		if (column == 6) { // 공급가액
			comp.setBackground(Main.YELLOW);
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
		// 단가 공급가액 편집 불가
		if (column == 5 || column == 6) {
			return false;
		}
		return true;
	}

	public boolean isCellEditable(int row, int column) {
		// 단가 공급가액 편집 불가
		if (column == 5 || column == 6) {
			return false;
		}
		return true;
	}
}