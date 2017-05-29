import javax.swing.JTable;

class Est {
	DemandF demand;
	Product[] product;
	int maxSize;
	int top;
	public int flag;
	Product productOne;

	Est(DemandF demand) {
		maxSize = 100;
		product = new Product[maxSize];
		this.demand = demand;
		flag = 1;
		top = 0;
	}

	Est() {
		maxSize = 100;
		product = new Product[maxSize];
		flag = 1;
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
		return Integer.parseInt(SupTable.toStrFormat(mat));
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

public class List {
	int maxSize;
	String list[][];
	SupTable supTable;

	List(SupTable supTable) {
		maxSize = 100;
		this.supTable = supTable;
	}

	String[][] init() {
		list = new String[100][6];
		for (int i = 0; i < 100; i++) {
			for (int j = 0; j < 6; j++) {
				list[i][j] = new String();
			}
		}
		return list;
	}

	void saveList(JTable table, int index, int rowMax) {
		for (int i = index; i < index + rowMax; i++) {
			list[i][0] = (String) table.getValueAt(i - index, 0);
			list[i][1] = (String) table.getValueAt(i - index, 1);
			list[i][2] = (String) table.getValueAt(i - index, 2);
			list[i][3] = (String) table.getValueAt(i - index, 3);
			list[i][4] = (String) table.getValueAt(i - index, 4);
			list[i][5] = (String) table.getValueAt(i - index, 7);
		}
	}

	void resize() {
		maxSize *= 2;
		String temp[][] = new String[maxSize][6];
		for (int i = 0; i < maxSize / 2; i++)
			for (int j = 0; j < 6; j++)
				temp[i][j] = list[i][j];
		list = temp;
		supTable.list = list;
	}

	void listRe(JTable table) {
		int i = supTable.index;
		int count = supTable.index;
		if (table.getRowCount() == supTable.Row) {
			i = 0;
			count = 0;
		}
		if (supTable.index + table.getRowCount() > maxSize)
			resize();
		for (int z = 0; z < table.getRowCount(); z++) {
			for (int j = 0; j < table.getColumnCount(); j++) {
				table.setValueAt(null, z, j);
			}
		}
		for (; i < count + table.getRowCount(); i++) {
			for (int j = 0; j < 5; j++) {
				table.setValueAt(list[i][j], i - count, j);
			}
			table.setValueAt(list[i][5], i - count, 7);
			// table.setValueAt("t", i - count, 6);
		}
		supTable.valueChangedSet(table, supTable.Row);
	}

	void setList(Est est, int flag) {
		int i = 0;
		for (; i < est.top; i++) {
			if (i == list.length)
				resize();
			list[i][0] = est.product[i].name;
			list[i][1] = est.product[i].size;
			list[i][2] = est.product[i].mat;
			list[i][3] = est.product[i].mCost;
			list[i][4] = est.product[i].num;
			list[i][5] = est.product[i].etc;
		}
		for (; i < maxSize; i++) {
			list[i][0] = null;
			list[i][1] = null;
			list[i][2] = null;
			list[i][3] = null;
			list[i][4] = null;
			list[i][5] = null;
		}
		flag = (supTable.Row - 1 - est.top) / supTable.RowMax + 1;
		listRe(supTable.table);
	}

	public void addColumn(int pos) {
		System.out.println(pos);
		for (int i = maxSize - 2; i >= pos; i--) {

			for (int j = 0; j < 6; j++) {
				System.out.println(list[i][j]);
				if (i  == pos)
					list[i ][j] = null;
				else {
					list[i][j] = list[i-1][j];
					if (supTable.flag == 1) {
						if (list[i][j] != null && i == supTable.Row) {
							Main.mainFrame.func.addPage();
							Main.mainFrame.func.after();
						}
					} else {
						if (list[i][j] != null && i == supTable.Row + supTable.RowMax * (supTable.flag - 1)) {
							Main.mainFrame.func.addPage();
							Main.mainFrame.func.after();
						}
					}
				}
			}
		}
	}

	public void removeColumn(int pos) {
		for (int i = pos; i < maxSize - 1; i++) {
			for (int j = 0; j < 6; j++) {
				list[i][j] = list[i + 1][j];
			}
		}
	}
}
