import javax.swing.JTable;

public class ProductList {
	//테이블 리스트 관리
	int maxSize;
	Product[] products;
	//초기화
	ProductList() {
		maxSize = 23;//front page count
		products = new Product[maxSize];
		for(int i=0;i<maxSize;i++){
			products[i]=new Product();
		}
	}
	//리사이즈
	void resize() {
		maxSize += 33;//back page count
		Product temp[] = new Product[maxSize];
		for (int i = 0; i < maxSize / 2; i++)
			temp[i].copy(products[i]);
		products = temp;
	}
	//table to list
	void tableToData(JTable table, int index) {
		for (int i = index; i < index + table.getRowCount(); i++) {
			products[i].name = (String) table.getValueAt(i - index, 0);
			products[i].standard = (String) table.getValueAt(i - index, 1);
			products[i].materialCost = (String) table.getValueAt(i - index, 2);
			products[i].processedCost = (String) table.getValueAt(i - index, 3);
			products[i].count = (String) table.getValueAt(i - index, 4);
			products[i].etc = (String) table.getValueAt(i - index, 7);
		}
	}
	//data to table
	void dataToTable(JTable table, int index) {
		for (int i = 0; i < table.getRowCount(); i++) {
			table.setValueAt(products[i+index].name,i, 0);
			table.setValueAt(products[i+index].standard,i, 1);
			table.setValueAt(products[i+index].materialCost,i, 2);
			table.setValueAt(products[i+index].processedCost,i, 3);
			table.setValueAt(products[i+index].count,i, 4);
			table.setValueAt(products[i+index].etc,i, 7);
		}
	}
	//사이즈 크기조절
	void isFull(int size){
		if (size>maxSize-1)
			resize();
	}
	//파일 로드
	/*
	public void addLoadStr(String[] stn) {
		products[i].name = stn[0].replaceAll("!@#", "");
		products[i].standard = stn[1].replaceAll("!@#", "");
		products[i].materialCost = stn[2].replaceAll("!@#", "");
		products[i].processedCost = stn[3].replaceAll("!@#", "");
		products[i].count = stn[4].replaceAll("!@#", "");
		products[i].etc = stn[5].replaceAll("!@#", "");
		
	}
	*/
	//파일 저장에 쓰일 문자열 변환
	public String getString(int n) {
		String str="";
		// 슬래쉬를 저장에 사용할경우 / 문자를 저장하지 못하기 때문에 변경필요.
		str=str+products[n].name+"/";
		str=str+products[n].standard+"/";
		str=str+products[n].materialCost+"/";
		str=str+products[n].processedCost+"/";
		str=str+products[n].count+"/";
		str=str+products[n].etc+"/";
		return str;
	}
	//합계 금액
	public long getSumMoney() {
		long sumData;
		long calcData=0;
		for (int i = 0; i < maxSize; i++) {
			sumData = Main.toLongFormat(products[i].materialCost) + Main.toLongFormat(products[i].processedCost);
			calcData += sumData * Main.toLongFormat(products[i].count);
		}
		return calcData;
	}
	//--------리스트 조작-----------------------
	public void addRow(int pos) {
		for (int i = maxSize - 1; i > pos; i--) {
			products[i]=products[i-1];
		}
		products[pos]=new Product();
	}

	public void removeRow(int pos) {
		for (int i = pos; i < maxSize - 1; i++) {
			products[i]=products[i+1];
		}
	}
	
	
	
	
}


