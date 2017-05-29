import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import javax.swing.JOptionPane;

public class Func {
	Supply supply;
	Demand demand;
	SupTable supTable;
	MainFrame mainFrame;
	Load load;
	PdfSave pdfSave;

	public Func(MainFrame mainFrame, Supply supply, Demand demand, SupTable supTable) {
		this.mainFrame = mainFrame;
		this.supply = supply;
		this.demand = demand;
		this.supTable = supTable;
		pdfSave = new PdfSave();
		load = new Load(mainFrame);
	}

	void pdfSave() {
		pdfSave.exportPDF(save(), supply, demand, supTable);
		supTable.curPage = 1;
	}

	String save() {
		
		DemandF demand = mainFrame.demand.getDemand();
		addDemand(demand);
		if (supTable.curPage == 1) {
			supTable.listC.saveList(supTable.table, supTable.index, supTable.Row);
		} else {
			supTable.listC.saveList(supTable.tableAdd, supTable.index, supTable.RowMax);
		}
		File a = new File("save");
		if (a.exists() == false) {
			a.mkdirs();
		}
		int number = 0;
		String fileName, temp;
		//새 파일이 아니면 무조건 저장  그러므로 다른이름으로 저장같은거 없음.
		if (!mainFrame.file.getText().equals("New Document")) {
			fileName = new String("save\\" + mainFrame.file.getText() + ".save");
			a = new File(fileName);
			a.delete();
		}
		while (true) {
			fileName = new String("save\\" + "견적서_" + demand.name + "_" + demand.date + "_" + number + ".save");
			a = new File(fileName);
			if (a.exists() == true) {
				number++;
				continue;
			}
			break;
		}
		try {
			BufferedWriter fw;
			fw = new BufferedWriter(new FileWriter(fileName));
			fw.write(demand.date + "/" + demand.name + "/" + demand.tel + " /" + demand.who + " /");
			fw.write(supTable.flag + "/");
			fw.write(supTable.table.getColumn("품목").getPreferredWidth()+"/"+
			supTable.table.getColumn("규격").getPreferredWidth()+"/"+
			supTable.table.getColumn("자재비").getPreferredWidth()+"/"+
			supTable.table.getColumn("가공비").getPreferredWidth()+"/"+
			supTable.table.getColumn("수량").getPreferredWidth()+"/"+
			supTable.table.getColumn("단가").getPreferredWidth()+"/"+
			supTable.table.getColumn("공급가액").getPreferredWidth()+"/"+
			supTable.table.getColumn("비고").getPreferredWidth());
			fw.write("\r\n");
			for (int i = 0; i < mainFrame.supTable.listC.maxSize; i++) {
				for (int j = 0; j < 6; j++) {

					if (supTable.list[i][j] != null && !supTable.list[i][j].equals("")&&!supTable.list[i][j].equals("\r\n") 
							&&!supTable.list[i][j].equals("\r")&&!supTable.list[i][j].equals("\n"))
						fw.write(supTable.list[i][j] + "/");
					else
						fw.write(" /");
				}
				fw.write("\r\n");
			}
			fileName = fileName.replace("save\\", "");
			fileName = fileName.replace(".save", "");
			mainFrame.file.setText(fileName);
			mainFrame.file.setBounds(419 - mainFrame.file.getText().length() * 5, 5, 300, 20);
			fw.flush();
			fw.close();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		Main.modify=false;
		return fileName;
	}

	void addDemand(DemandF dmd) {
		
		String name,tel,who;
		demand.loadList();
		String fileName;
		fileName = new String("demandList.list");
		BufferedWriter fw;
		try {
			fw = new BufferedWriter(new FileWriter(fileName));
			for (int i = 0; i < demand.manageLi.top; i++) {
				if (!demand.manageLi.demand[i].equals(dmd)){
					name=demand.manageLi.demand[i].name;
					tel=demand.manageLi.demand[i].tel;
					who=demand.manageLi.demand[i].who;
					if (name=="" || name==null||name.length()==0)
						continue;
					if (tel=="" || tel==null||tel.length()==0){
						tel=" ";
					}
					if (who=="" || who==null||who.length()==0)
						who=" ";
					fw.write(name + "/" + tel + "/" + who + "/\r\n");
					/*fw.write(demand.manageLi.demand[i].name + "/" + demand.manageLi.demand[i].tel + "/" + demand.manageLi.demand[i].who
							);*/
				}
			}
			name=dmd.name;
			tel=dmd.tel;
			who=dmd.who;
			if (name=="" || name==null||name.length()==0){
				fw.flush();
				fw.close();
				return;
			}
			if (tel=="" || tel==null ||tel.length()==0 ){
				tel=" ";
			}
			if (who=="" || who==null||who.length()==0)
				who=" ";
			fw.write(name + "/" + tel + "/" + who + "/");
			fw.write("\r\n");
			fw.flush();
			fw.close();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
	}

	void load() {// 불러오기
		if (Main.modify) {
			int choice = JOptionPane.showConfirmDialog(null, "변경 내용을 저장하시겠습니까?", "불러오기", JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.INFORMATION_MESSAGE);
			if (choice==0){
				save();
			load.loadList();
			load.tableInit();
			load.setVisible(true);
			}
			else if (choice==1){
				load.loadList();
				load.tableInit();
				load.setVisible(true);
			}
		}
		else{
			load.loadList();
			load.tableInit();
			load.setVisible(true);
		}
	}

	void leftPage() {
		if (supTable.curPage != 1){
			supTable.table.getColumn("품목").setPreferredWidth(supTable.tableAdd.getColumn("품목").getPreferredWidth());
			supTable.table.getColumn("규격").setPreferredWidth(supTable.tableAdd.getColumn("규격").getPreferredWidth());
			supTable.table.getColumn("자재비").setPreferredWidth(supTable.tableAdd.getColumn("자재비").getPreferredWidth());
			supTable.table.getColumn("가공비").setPreferredWidth(supTable.tableAdd.getColumn("가공비").getPreferredWidth());
			supTable.table.getColumn("수량").setPreferredWidth(supTable.tableAdd.getColumn("수량").getPreferredWidth());
			supTable.table.getColumn("단가").setPreferredWidth(supTable.tableAdd.getColumn("단가").getPreferredWidth());
			supTable.table.getColumn("공급가액").setPreferredWidth(supTable.tableAdd.getColumn("공급가액").getPreferredWidth());
			supTable.table.getColumn("비고").setPreferredWidth(supTable.tableAdd.getColumn("비고").getPreferredWidth());
		}
		if (supTable.curPage == 1) {
		} else if (supTable.curPage == 2) {
			supTable.listC.saveList(supTable.tableAdd, supTable.index, supTable.RowMax);
			supTable.index -= supTable.Row;
			supTable.curPage--;
		} else {
			supTable.listC.saveList(supTable.tableAdd, supTable.index, supTable.RowMax);
			supTable.index -= supTable.RowMax;
			supTable.curPage--;
			supTable.listC.listRe(supTable.tableAdd);
		}
		for (int i = 0; i < supTable.tableAdd.getRowCount(); i++) {
			for (int j = 5; j < supTable.tableAdd.getColumnCount(); j++) {
				supTable.tableAdd.setValueAt(null, i, j);
			}
		}
		supTable.listC.listRe(supTable.tableAdd);

	}

	void rightPage() {
		if (supTable.curPage == 1){
			supTable.tableAdd.getColumn("품목").setPreferredWidth(supTable.table.getColumn("품목").getPreferredWidth());
			supTable.tableAdd.getColumn("규격").setPreferredWidth(supTable.table.getColumn("규격").getPreferredWidth());
			supTable.tableAdd.getColumn("자재비").setPreferredWidth(supTable.table.getColumn("자재비").getPreferredWidth());
			supTable.tableAdd.getColumn("가공비").setPreferredWidth(supTable.table.getColumn("가공비").getPreferredWidth());
			supTable.tableAdd.getColumn("수량").setPreferredWidth(supTable.table.getColumn("수량").getPreferredWidth());
			supTable.tableAdd.getColumn("단가").setPreferredWidth(supTable.table.getColumn("단가").getPreferredWidth());
			supTable.tableAdd.getColumn("공급가액").setPreferredWidth(supTable.table.getColumn("공급가액").getPreferredWidth());
			supTable.tableAdd.getColumn("비고").setPreferredWidth(supTable.table.getColumn("비고").getPreferredWidth());
		}
		else
		{
				supTable.table.getColumn("품목").setPreferredWidth(supTable.tableAdd.getColumn("품목").getPreferredWidth());
				supTable.table.getColumn("규격").setPreferredWidth(supTable.tableAdd.getColumn("규격").getPreferredWidth());
				supTable.table.getColumn("자재비").setPreferredWidth(supTable.tableAdd.getColumn("자재비").getPreferredWidth());
				supTable.table.getColumn("가공비").setPreferredWidth(supTable.tableAdd.getColumn("가공비").getPreferredWidth());
				supTable.table.getColumn("수량").setPreferredWidth(supTable.tableAdd.getColumn("수량").getPreferredWidth());
				supTable.table.getColumn("단가").setPreferredWidth(supTable.tableAdd.getColumn("단가").getPreferredWidth());
				supTable.table.getColumn("공급가액").setPreferredWidth(supTable.tableAdd.getColumn("공급가액").getPreferredWidth());
				supTable.table.getColumn("비고").setPreferredWidth(supTable.tableAdd.getColumn("비고").getPreferredWidth());
		}
		if (supTable.curPage < supTable.flag) {
			if ((supTable.index + supTable.RowMax) > supTable.listC.maxSize)
				supTable.listC.resize();
			if (supTable.curPage == 1) {
				supTable.listC.saveList(supTable.table, supTable.index, supTable.Row);
				supTable.index += supTable.Row;
			} else {
				supTable.listC.saveList(supTable.tableAdd, supTable.index, supTable.RowMax);
				supTable.index += supTable.RowMax;
			}
			if (supTable.flag != supTable.curPage) {
				supTable.curPage++;
			}
			for (int i = 0; i < supTable.tableAdd.getRowCount(); i++) {
				for (int j = 5; j < supTable.tableAdd.getColumnCount(); j++) {
					supTable.tableAdd.setValueAt(null, i, j);
				}
			}
			supTable.listC.listRe(supTable.tableAdd);
			if (supTable.curPage == supTable.flag)
				supTable.tableAdd.setValueAt("합계", supTable.RowMax - 1, 5);// ///////////////////////
		}

	}

	void addPage() {
		if (supTable.curPage == 1){
			supTable.tableAdd.getColumn("품목").setPreferredWidth(supTable.table.getColumn("품목").getPreferredWidth());
			supTable.tableAdd.getColumn("규격").setPreferredWidth(supTable.table.getColumn("규격").getPreferredWidth());
			supTable.tableAdd.getColumn("자재비").setPreferredWidth(supTable.table.getColumn("자재비").getPreferredWidth());
			supTable.tableAdd.getColumn("가공비").setPreferredWidth(supTable.table.getColumn("가공비").getPreferredWidth());
			supTable.tableAdd.getColumn("수량").setPreferredWidth(supTable.table.getColumn("수량").getPreferredWidth());
			supTable.tableAdd.getColumn("단가").setPreferredWidth(supTable.table.getColumn("단가").getPreferredWidth());
			supTable.tableAdd.getColumn("공급가액").setPreferredWidth(supTable.table.getColumn("공급가액").getPreferredWidth());
			supTable.tableAdd.getColumn("비고").setPreferredWidth(supTable.table.getColumn("비고").getPreferredWidth());
		}
		else
		{
				supTable.table.getColumn("품목").setPreferredWidth(supTable.tableAdd.getColumn("품목").getPreferredWidth());
				supTable.table.getColumn("규격").setPreferredWidth(supTable.tableAdd.getColumn("규격").getPreferredWidth());
				supTable.table.getColumn("자재비").setPreferredWidth(supTable.tableAdd.getColumn("자재비").getPreferredWidth());
				supTable.table.getColumn("가공비").setPreferredWidth(supTable.tableAdd.getColumn("가공비").getPreferredWidth());
				supTable.table.getColumn("수량").setPreferredWidth(supTable.tableAdd.getColumn("수량").getPreferredWidth());
				supTable.table.getColumn("단가").setPreferredWidth(supTable.tableAdd.getColumn("단가").getPreferredWidth());
				supTable.table.getColumn("공급가액").setPreferredWidth(supTable.tableAdd.getColumn("공급가액").getPreferredWidth());
				supTable.table.getColumn("비고").setPreferredWidth(supTable.tableAdd.getColumn("비고").getPreferredWidth());
		}
		if (supTable.init == false) {
			supTable.tableAdd.setEnabled(true);
			supTable.init = true;
		}
			if ((supTable.index + supTable.RowMax) > supTable.listC.maxSize)
				supTable.listC.resize();
			supTable.flag++;
			for (int i = 0; i < supTable.tableAdd.getRowCount(); i++) {
				for (int j = 5; j < supTable.tableAdd.getColumnCount(); j++) {
					supTable.tableAdd.setValueAt(null, i, j);
				}
			}
			supTable.listC.listRe(supTable.tableAdd);
			supTable.listC.listRe(supTable.table);
	}

	void removePage() {
		if (supTable.flag == 1) {
			for (int i = 0; i < supTable.Row - 1; i++)
				for (int j = 0; j < 6; j++)
					supTable.list[i][j] = null;
			for (int i = 0; i < supTable.Row - 1; i++) {
				for (int j = 0; j < 6; j++) {
					supTable.table.setValueAt(null, i, j);
					supTable.list[supTable.table.getRowCount()][j] = null;
				}
			}
			for (int j = 0; j < 8; j++)
				supTable.table.setValueAt(null, supTable.table.getRowCount() - 1, j);
			supTable.listC.listRe(supTable.table);
		} else if (supTable.curPage == supTable.flag) {

			for (int i = supTable.index; i < supTable.index + supTable.RowMax - 1; i++)
				for (int j = 0; j < 6; j++) {
					supTable.tableAdd.setValueAt(null, i - supTable.index, j);
					supTable.list[i][j] = null;
				}
			if (supTable.curPage == 1) {

			} else if (supTable.curPage == 2) {
				supTable.listC.saveList(supTable.tableAdd, supTable.index, supTable.RowMax);
				supTable.index -= supTable.Row;
				supTable.curPage--;
			} else {
				supTable.listC.saveList(supTable.tableAdd, supTable.index, supTable.RowMax);
				supTable.index -= supTable.RowMax;
				supTable.curPage--;
				supTable.listC.listRe(supTable.tableAdd);
			}
			for (int i = 0; i < supTable.tableAdd.getRowCount(); i++) {
				for (int j = 5; j < supTable.tableAdd.getColumnCount(); j++) {
					supTable.tableAdd.setValueAt(null, i, j);
				}
			}
			for (int j = 0; j < 6; j++)
				supTable.list[supTable.index + supTable.RowMax - 1][j] = null;

			supTable.listC.listRe(supTable.tableAdd);
			supTable.flag--;
			supTable.tableAdd.setValueAt("합계", supTable.RowMax - 1, 5);
			if (supTable.flag == 1) {
				for (int j = 0; j < 8; j++) {
					supTable.table.setValueAt(null, supTable.table.getRowCount() - 1, j);
				}
				for (int j = 0; j < 6; j++) {
					supTable.table.setValueAt(null, supTable.table.getRowCount() - 1, j);
					supTable.list[supTable.table.getRowCount() - 1][j] = null;
				}
				supTable.listC.listRe(supTable.table);
			}
		}
	}

	void after() {
		if (supTable.curPage == 1) {
			mainFrame.card.first(mainFrame.currPane);
			supTable.valueChangedSet(supTable.table, supTable.Row);
		} else {
			mainFrame.card.last(mainFrame.currPane);
			supTable.valueChangedSet(supTable.tableAdd, supTable.RowMax);
		}
		mainFrame.page.setText(new String("page" + supTable.curPage + "/" + supTable.flag));
		mainFrame.repaint();
	}

	public BufferedImage createComponentCapture(Component comp) {
		BufferedImage image = new BufferedImage(comp.getWidth() * 72 / 100, comp.getHeight() * 72 / 100, BufferedImage.TYPE_INT_RGB);
		BufferedImage img = new BufferedImage(comp.getWidth() * 72 / 100, comp.getHeight() * 72 / 100, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = (Graphics2D) image.getGraphics();
		g2d.scale(0.72, 0.72);
		comp.paint(g2d); // 여기서 이미지에 그려넣습니다.

		// g2d.drawImage(image, null, 0, 0);
		return image;
	}

	public void sort() {
		List listC = supTable.listC;
		Est est = new Est();
		for (int i = 0; i < listC.maxSize; i++) {
				est.addProduct(listC.list[i][0], listC.list[i][1], listC.list[i][2], listC.list[i][3], listC.list[i][4], listC.list[i][5]);
		}
		est.sort();
		listC.setList(est, 1);
	}

	public void loadDemand() {
		
		demand.loadList();
		demand.tableInit();
		demand.frame.setVisible(true);

	}

}
