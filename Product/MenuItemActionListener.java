package Product;

import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTable;

class MenuItemActionListener implements ActionListener {
	JTable table;
	ProductView productView;
	ProductList productList;
	Clipboard clipboard;

	MenuItemActionListener(ProductView productView, JTable currTable) {
		this.productView = productView;
		this.productList = productView.productList;
		this.table = currTable;

	}

	public void actionPerformed(ActionEvent e) {
		if (((MenuItem) e.getSource()).getLabel() == "행 추가 (ctrl+shift+a)") {
			productList.addRow(table.getSelectedRow());
		} else if (((MenuItem) e.getSource()).getLabel() == "행 제거 (ctrl+shift+d)") {
			productList.removeRow(table.getSelectedRow());
		} else if (((MenuItem) e.getSource()).getLabel() == "행 복사 (ctrl+shift+c)") {
			productList.copyRow(table.getSelectedRow());
		} else if (((MenuItem) e.getSource()).getLabel() == "행 잘라내기 (ctrl+shift+x)") {
			productList.copyRow(table.getSelectedRow());
			productList.removeRow(table.getSelectedRow());
		} else if (((MenuItem) e.getSource()).getLabel() == "행 붙여넣기 (ctrl+shift+v)") {
			productList.pasteRow(table.getSelectedRow());
		} else if (((MenuItem) e.getSource()).getLabel() == "행 올리기 (ctrl+shift+up)") {
			productList.shiftUpRow(table.getSelectedRow());
		} else if (((MenuItem) e.getSource()).getLabel() == "행 내리기 (ctrl+shift+down)") {
			productList.shiftDownRow(table.getSelectedRow());
		} else if (((MenuItem) e.getSource()).getLabel() == "셀 복사 (ctrl+c)") {
			productView.clipboardCopy(table);
		} else if (((MenuItem) e.getSource()).getLabel() == "셀 잘라내기 (ctrl+x)") {
			productView.clipboardCopy(table);
			if (table.getSelectedColumn() != 5 && table.getSelectedColumn() != 6) {
				table.setValueAt(null, table.getSelectedRow(), table.getSelectedColumn());
				productView.valueChangedUpdate(table);
			}
		} else if (((MenuItem) e.getSource()).getLabel() == "셀 붙여넣기 (ctrl+v)") {
			productView.clipboardPaste(table);
		}
		productView.tableUpdate(table);
	}
}