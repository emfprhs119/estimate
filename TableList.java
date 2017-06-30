import javax.swing.JTable;

class Est {
	DemandF demand;
	Product[] product;
	int maxSize;
	int top;
	public int maxPage;
	Product productOne;

	Est(DemandF demand) {
		maxSize = 100;
		product = new Product[maxSize];
		this.demand = demand;
		maxPage = 1;
		top = 0;
	}

	Est() {
		maxSize = 100;
		product = new Product[maxSize];
		maxPage = 1;
		top = 0;
	}

	void addProduct(String name, String size, String mat, String mCost, String num, String etc) {
		if (top == maxSize)
			resize();
		productOne = new Product(name, size, mat, mCost, num, etc);
		if (!productOne.isNull)
			product[top++] = productOne;
	}

	void resize() {
		maxSize *= 2;
		Product temp[] = new Product[maxSize];
		for (int i = 0; i < maxSize / 2; i++)
			temp[i] = product[i];
		product = temp;
	}

	public void sort() {
		for (int i = 0; i < top - 1; i++) {
			for (int j = 0; j < top - 1; j++) {
				if (product[j].compareTo(product[j + 1]) > 0) {
					swap(j, j + 1);
				}
			}
		}
	}

	private void swap(int i, int j) {
		Product temp = product[i];
		product[i] = product[i + 1];
		product[i + 1] = temp;
	}
}

class Product implements Comparable {
	String name;
	String size;
	String mat;
	String mCost;
	String num;
	String etc;
	boolean isNull = false;

	public Product(String name, String size, String mat, String mCost, String num, String etc) {
		this.name = name;
		this.size = size;
		this.mat = mat;
		this.mCost = mCost;
		this.num = num;
		this.etc = etc;
		nulls();
	}

	public void nulls() {
		if ((name == null || name == "") && (size == null || size == "") && (mat == null || mat == "")
				&& (mCost == null || mCost == "") && (num == null || num == "") && (etc == null || etc == ""))
			isNull = true;
		if (name == null)
			name = "";
		if (size == null)
			size = "";
		if (mat == null)
			mat = "";
		if (mCost == null)
			mCost = "";
		if (num == null)
			num = "";
		if (etc == null)
			etc = "";
	}

	public int oneCost() {
		return (toInt(mat) + toInt(mCost));
	}

	public int cost() {
		return (toInt(mat) + toInt(mCost)) * toInt(num);
	}

	public int toInt(String mat) {
		return Integer.parseInt(SupplyTable.toStrFormat(mat));
	}

	public int compareTo(Object arg0) {
		if (isNull) {
			return -10;
		}
		int num;
		num = name.compareTo(((Product) arg0).name);

		if (num == 0) {
			num = size.compareTo(((Product) arg0).size);
			if (num == 0) {
				num = etc.compareTo(((Product) arg0).etc);
			}
		}

		return num;
	}
}

public class TableList {
	//테이블 리스트 관리
	int maxSize;
	String strList[][];
	SupplyTable supplyTable;

	TableList(SupplyTable supplyTable) {
		maxSize = 100;
		this.supplyTable = supplyTable;
	}

	String[][] init() {
		strList = new String[maxSize][6];
		for (int i = 0; i < maxSize; i++) {
			for (int j = 0; j < 6; j++) {
				strList[i][j] = new String();
			}
		}
		return strList;
	}

	void resize() {
		maxSize *= 2;
		String temp[][] = new String[maxSize][6];
		for (int i = 0; i < maxSize / 2; i++)
			for (int j = 0; j < 6; j++)
				temp[i][j] = strList[i][j];
		strList = temp;
		supplyTable.strList = strList;
	}
	
	void saveList(JTable table, int index, int rowMax) {
		for (int i = index; i < index + rowMax; i++) {
			strList[i][0] = (String) table.getValueAt(i - index, 0);
			strList[i][1] = (String) table.getValueAt(i - index, 1);
			strList[i][2] = (String) table.getValueAt(i - index, 2);
			strList[i][3] = (String) table.getValueAt(i - index, 3);
			strList[i][4] = (String) table.getValueAt(i - index, 4);
			strList[i][5] = (String) table.getValueAt(i - index, 7);
		}
	}

	void isFull(int size){
		if (size>maxSize)
			resize();
	}

	void setList(Est est, int flag) {
		int i = 0;
		for (; i < est.top; i++) {
			if (i == strList.length)
				resize();
			strList[i][0] = est.product[i].name;
			strList[i][1] = est.product[i].size;
			strList[i][2] = est.product[i].mat;
			strList[i][3] = est.product[i].mCost;
			strList[i][4] = est.product[i].num;
			strList[i][5] = est.product[i].etc;
		}
		for (; i < maxSize; i++) {
			strList[i][0] = null;
			strList[i][1] = null;
			strList[i][2] = null;
			strList[i][3] = null;
			strList[i][4] = null;
			strList[i][5] = null;
		}
		flag = (supplyTable.FrontRow - 1 - est.top) / supplyTable.BackRow + 1;
	}

	public void addColumn(int pos) {
		System.out.println(pos);
		for (int i = maxSize - 2; i >= pos; i--) {

			for (int j = 0; j < 6; j++) {
				if (i  == pos)
					strList[i ][j] = null;
				else {
					strList[i][j] = strList[i-1][j];
					//if (supplyTable.flag == 1) {
						//if (list[i][j] != null && i == supplyTable.Row) {
							//Main.mainFrame.func.addPage();
							//Main.mainFrame.func.after();
						//}
					//} else {
						//if (list[i][j] != null && i == supplyTable.Row + supplyTable.RowMax * (supplyTable.flag - 1)) {
							//Main.mainFrame.func.addPage();
							//Main.mainFrame.func.after();
						//}
					//}
				}
			}
		}
	}

	public void removeColumn(int pos) {
		for (int i = pos; i < maxSize - 1; i++) {
			for (int j = 0; j < 6; j++) {
				strList[i][j] = strList[i + 1][j];
			}
		}
	}
}
