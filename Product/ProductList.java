package Product;

import javax.swing.JTable;

import Main.Main;

public class ProductList {
	// 테이블 리스트 관리
	private int maxSize;
	Product[] products;
	private int size = 0;
	private Product copyProduct;
	// 초기화
	public ProductList() {
		maxSize = Main.FrontRow+1;// front page count
		products = new Product[maxSize];
		for (int i = 0; i < maxSize; i++) {
			products[i] = new Product();
		}
	}

	// 리사이즈
	void resize(int size) {
		int prevSize = maxSize;
		maxSize = size;
		Product temp[] = new Product[maxSize];
		for (int i = 0; i < maxSize; i++) {
			if (i < (maxSize < prevSize ? maxSize : prevSize))
				temp[i] = products[i];
			else
				temp[i] = new Product();
		}
		products = temp;
	}

	// table to list
	void tableToData(JTable table, int index) {
		for (int i = index; i < index + table.getRowCount(); i++) {
			products[i].setName((String) table.getValueAt(i - index, 0));
			products[i].setStandard((String) table.getValueAt(i - index, 1));
			products[i].setMaterialCost((String) table.getValueAt(i - index, 2));
			products[i].setProcessedCost((String)table.getValueAt(i - index, 3));
			products[i].setCount((String)table.getValueAt(i - index, 4));
			products[i].setEtc((String) table.getValueAt(i - index, 7));
		}
	}

	// data to table
	void dataToTable(JTable table, int index) {
		for (int i = 0; i < table.getRowCount(); i++) {
			table.setValueAt(products[i + index].getName(), i, 0);
			table.setValueAt(products[i + index].getStandard(), i, 1);
			table.setValueAt(products[i + index].getMaterialCost(), i, 2);
			table.setValueAt(products[i + index].getProcessedCost(), i, 3);
			table.setValueAt(products[i + index].getCount(), i, 4);
			table.setValueAt(products[i + index].getEtc(), i, 7);
		}
	}

	// 합계 금액
	public long getSumMoney() {
		long sumMoney = 0;
		for (int i = 0; i < maxSize; i++) {
			sumMoney += products[i].getSumMoney();
		}
		return sumMoney;
	}

	// --------리스트 조작-----------------------
	public void addPage() {
		resize(maxSize + Main.BackRow);
	}

	public void removePage() {
		if (maxSize == Main.FrontRow + 1) {
			products = new Product[maxSize];
			for (int i = 0; i < maxSize; i++) {
				products[i] = new Product();
			}
		} else
			resize(maxSize - Main.BackRow);

	}

	public void addRow(int index) {
		for (int i = maxSize - 1; i > index; i--) {
			products[i] = products[i - 1];
		}
		products[index] = new Product();
	}

	public void removeRow(int index) {
		for (int i = index; i < maxSize - 1; i++) {
			products[i] = products[i + 1];
		}
	}
	public void copyRow(int index) {
		this.copyProduct=products[index];
	}
	public void pasteRow(int index) {
		addRow(index);
		products[index].setProduct(copyProduct);
	}
	public void shiftUpRow(int index){
		if(index>1)
			swapProductRow(index-1,index);
	}
	public void shiftDownRow(int index){
		if(index<maxSize-1)
			swapProductRow(index,index+1);
	}
	private void swapProductRow(int index1, int index2) {
		Product product=products[index1];
		products[index1]=products[index2];
		products[index2]=product;
	}

	// ----------------------------------------

	public void addProduct(String[] stn) {
		if (size>=maxSize){
			resize(maxSize+Main.BackRow);
		}
		products[size]=new Product(stn);
		
		size++;
	}

	public int getMaxSize() {
		return maxSize;
	}
	public Product getProduct(int index){
		return products[index];
	}

	public void setData(String paste, int selectedRow, int selectedColumn) {
		switch(selectedColumn){
		case 0:products[selectedRow].setName(paste);break;
		case 1:products[selectedRow].setStandard(paste);break;
		case 2:products[selectedRow].setMaterialCost(paste);break;
		case 3:products[selectedRow].setProcessedCost(paste);break;
		case 4:products[selectedRow].setCount(paste);break;
		case 7:products[selectedRow].setEtc(paste);break;
		}
	}

	
}
