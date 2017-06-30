import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Func {
	Supply supply;
	Demand demand;
	SupplyTable supplyTable;
	JPanel masterPane;
	CardLayout cardLayout;
	JLabel file;
	JLabel page;
	Load load;
	PdfSave pdfSave;

	public Func(Supply supply, Demand demand, WhitePanel masterPane, SupplyTable supplyTable, CardLayout cardLayout,
			JLabel file, JLabel page) {
		this.supply = supply;
		this.demand = demand;
		this.masterPane = masterPane;
		this.supplyTable = supplyTable;
		this.cardLayout = cardLayout;
		this.file = file;
		this.page = page;
		demand.setFunc(this);
		pdfSave = new PdfSave();
		load = new Load(supplyTable, demand, file, page);
	}

	void pdfSave() {
		pdfSave.exportPDF(save(), supply, demand, supplyTable);
		supplyTable.currPage = 1;
	}

	String save() {

		DemandF demand = this.demand.getDemand();
		addDemand(demand);
		if (supplyTable.currPage == 1) {
			supplyTable.tableList.saveList(supplyTable.frontTable, supplyTable.index, supplyTable.FrontRow);
		} else {
			supplyTable.tableList.saveList(supplyTable.backTable, supplyTable.index, supplyTable.BackRow);
		}
		File a = new File("save");
		if (a.exists() == false) {
			a.mkdirs();
		}
		int number = 0;
		String fileName, temp;
		// 새 파일이 아니면 무조건 저장 그러므로 다른이름으로 저장같은거 없음.
		if (!file.getText().equals("New Document")) {
			fileName = new String("save\\" + file.getText() + ".save");
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
			fw.write(supplyTable.maxPage + "/");
			fw.write(supplyTable.frontTable.getColumn("품목").getPreferredWidth() + "/"
					+ supplyTable.frontTable.getColumn("규격").getPreferredWidth() + "/"
					+ supplyTable.frontTable.getColumn("자재비").getPreferredWidth() + "/"
					+ supplyTable.frontTable.getColumn("가공비").getPreferredWidth() + "/"
					+ supplyTable.frontTable.getColumn("수량").getPreferredWidth() + "/"
					+ supplyTable.frontTable.getColumn("단가").getPreferredWidth() + "/"
					+ supplyTable.frontTable.getColumn("공급가액").getPreferredWidth() + "/"
					+ supplyTable.frontTable.getColumn("비고").getPreferredWidth());
			fw.write("\r\n");
			for (int i = 0; i < supplyTable.tableList.maxSize; i++) {
				for (int j = 0; j < 6; j++) {

					if (supplyTable.strList[i][j] != null && !supplyTable.strList[i][j].equals("")
							&& !supplyTable.strList[i][j].equals("\r\n") && !supplyTable.strList[i][j].equals("\r")
							&& !supplyTable.strList[i][j].equals("\n"))
						fw.write(supplyTable.strList[i][j] + "/");
					else
						fw.write(" /");
				}
				fw.write("\r\n");
			}
			fileName = fileName.replace("save\\", "");
			fileName = fileName.replace(".save", "");
			file.setText(fileName);
			file.setBounds(419 - file.getText().length() * 5, 5, 300, 20);
			fw.flush();
			fw.close();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		Main.modify = false;
		return fileName;
	}

	void addDemand(DemandF dmd) {

		String name, tel, who;
		demand.loadList();
		String fileName;
		fileName = new String("demandList.list");
		BufferedWriter fw;
		try {
			fw = new BufferedWriter(new FileWriter(fileName));
			for (int i = 0; i < demand.manageLi.top; i++) {
				if (!demand.manageLi.demand[i].equals(dmd)) {
					name = demand.manageLi.demand[i].name;
					tel = demand.manageLi.demand[i].tel;
					who = demand.manageLi.demand[i].who;
					if (name == "" || name == null || name.length() == 0)
						continue;
					if (tel == "" || tel == null || tel.length() == 0) {
						tel = " ";
					}
					if (who == "" || who == null || who.length() == 0)
						who = " ";
					fw.write(name + "/" + tel + "/" + who + "/\r\n");
					/*
					 * fw.write(demand.manageLi.demand[i].name + "/" +
					 * demand.manageLi.demand[i].tel + "/" +
					 * demand.manageLi.demand[i].who );
					 */
				}
			}
			name = dmd.name;
			tel = dmd.tel;
			who = dmd.who;
			if (name == "" || name == null || name.length() == 0) {
				fw.flush();
				fw.close();
				return;
			}
			if (tel == "" || tel == null || tel.length() == 0) {
				tel = " ";
			}
			if (who == "" || who == null || who.length() == 0)
				who = " ";
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
			int choice = JOptionPane.showConfirmDialog(null, "변경 내용을 저장하시겠습니까?", "불러오기",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if (choice == 0) {
				save();
				load.loadList();
				load.tableInit();
				load.setVisible(true);
			} else if (choice == 1) {
				load.loadList();
				load.tableInit();
				load.setVisible(true);
			}
		} else {
			load.loadList();
			load.tableInit();
			load.setVisible(true);
		}
	}

	void leftPage() {
		if (supplyTable.currPage != 1) {
			supplyTable.frontTable.getColumn("품목")
					.setPreferredWidth(supplyTable.backTable.getColumn("품목").getPreferredWidth());
			supplyTable.frontTable.getColumn("규격")
					.setPreferredWidth(supplyTable.backTable.getColumn("규격").getPreferredWidth());
			supplyTable.frontTable.getColumn("자재비")
					.setPreferredWidth(supplyTable.backTable.getColumn("자재비").getPreferredWidth());
			supplyTable.frontTable.getColumn("가공비")
					.setPreferredWidth(supplyTable.backTable.getColumn("가공비").getPreferredWidth());
			supplyTable.frontTable.getColumn("수량")
					.setPreferredWidth(supplyTable.backTable.getColumn("수량").getPreferredWidth());
			supplyTable.frontTable.getColumn("단가")
					.setPreferredWidth(supplyTable.backTable.getColumn("단가").getPreferredWidth());
			supplyTable.frontTable.getColumn("공급가액")
					.setPreferredWidth(supplyTable.backTable.getColumn("공급가액").getPreferredWidth());
			supplyTable.frontTable.getColumn("비고")
					.setPreferredWidth(supplyTable.backTable.getColumn("비고").getPreferredWidth());
		}
		if (supplyTable.currPage == 1) {
		} else if (supplyTable.currPage == 2) {
			supplyTable.tableList.saveList(supplyTable.backTable, supplyTable.index, supplyTable.BackRow);
			supplyTable.index -= supplyTable.FrontRow;
			supplyTable.currPage--;
		} else {
			supplyTable.tableList.saveList(supplyTable.backTable, supplyTable.index, supplyTable.BackRow);
			supplyTable.index -= supplyTable.BackRow;
			supplyTable.currPage--;
			supplyTable.tableList.listRe(supplyTable.backTable);
		}
		for (int i = 0; i < supplyTable.backTable.getRowCount(); i++) {
			for (int j = 5; j < supplyTable.backTable.getColumnCount(); j++) {
				supplyTable.backTable.setValueAt(null, i, j);
			}
		}
		supplyTable.tableList.listRe(supplyTable.backTable);

	}

	void rightPage() {
		if (supplyTable.currPage == 1) {
			supplyTable.backTable.getColumn("품목")
					.setPreferredWidth(supplyTable.frontTable.getColumn("품목").getPreferredWidth());
			supplyTable.backTable.getColumn("규격")
					.setPreferredWidth(supplyTable.frontTable.getColumn("규격").getPreferredWidth());
			supplyTable.backTable.getColumn("자재비")
					.setPreferredWidth(supplyTable.frontTable.getColumn("자재비").getPreferredWidth());
			supplyTable.backTable.getColumn("가공비")
					.setPreferredWidth(supplyTable.frontTable.getColumn("가공비").getPreferredWidth());
			supplyTable.backTable.getColumn("수량")
					.setPreferredWidth(supplyTable.frontTable.getColumn("수량").getPreferredWidth());
			supplyTable.backTable.getColumn("단가")
					.setPreferredWidth(supplyTable.frontTable.getColumn("단가").getPreferredWidth());
			supplyTable.backTable.getColumn("공급가액")
					.setPreferredWidth(supplyTable.frontTable.getColumn("공급가액").getPreferredWidth());
			supplyTable.backTable.getColumn("비고")
					.setPreferredWidth(supplyTable.frontTable.getColumn("비고").getPreferredWidth());
		} else {
			supplyTable.frontTable.getColumn("품목")
					.setPreferredWidth(supplyTable.backTable.getColumn("품목").getPreferredWidth());
			supplyTable.frontTable.getColumn("규격")
					.setPreferredWidth(supplyTable.backTable.getColumn("규격").getPreferredWidth());
			supplyTable.frontTable.getColumn("자재비")
					.setPreferredWidth(supplyTable.backTable.getColumn("자재비").getPreferredWidth());
			supplyTable.frontTable.getColumn("가공비")
					.setPreferredWidth(supplyTable.backTable.getColumn("가공비").getPreferredWidth());
			supplyTable.frontTable.getColumn("수량")
					.setPreferredWidth(supplyTable.backTable.getColumn("수량").getPreferredWidth());
			supplyTable.frontTable.getColumn("단가")
					.setPreferredWidth(supplyTable.backTable.getColumn("단가").getPreferredWidth());
			supplyTable.frontTable.getColumn("공급가액")
					.setPreferredWidth(supplyTable.backTable.getColumn("공급가액").getPreferredWidth());
			supplyTable.frontTable.getColumn("비고")
					.setPreferredWidth(supplyTable.backTable.getColumn("비고").getPreferredWidth());
		}
		if (supplyTable.currPage < supplyTable.maxPage) {
			if ((supplyTable.index + supplyTable.BackRow) > supplyTable.tableList.maxSize)
				supplyTable.tableList.resize();
			if (supplyTable.currPage == 1) {
				supplyTable.tableList.saveList(supplyTable.frontTable, supplyTable.index, supplyTable.FrontRow);
				supplyTable.index += supplyTable.FrontRow;
			} else {
				supplyTable.tableList.saveList(supplyTable.backTable, supplyTable.index, supplyTable.BackRow);
				supplyTable.index += supplyTable.BackRow;
			}
			if (supplyTable.maxPage != supplyTable.currPage) {
				supplyTable.currPage++;
			}
			for (int i = 0; i < supplyTable.backTable.getRowCount(); i++) {
				for (int j = 5; j < supplyTable.backTable.getColumnCount(); j++) {
					supplyTable.backTable.setValueAt(null, i, j);
				}
			}
			supplyTable.tableList.listRe(supplyTable.backTable);
			if (supplyTable.currPage == supplyTable.maxPage)
				supplyTable.backTable.setValueAt("합계", supplyTable.BackRow - 1, 5);// ///////////////////////
		}

	}

	void addPage() {
		if (supplyTable.currPage == 1) {
			supplyTable.backTable.getColumn("품목")
					.setPreferredWidth(supplyTable.frontTable.getColumn("품목").getPreferredWidth());
			supplyTable.backTable.getColumn("규격")
					.setPreferredWidth(supplyTable.frontTable.getColumn("규격").getPreferredWidth());
			supplyTable.backTable.getColumn("자재비")
					.setPreferredWidth(supplyTable.frontTable.getColumn("자재비").getPreferredWidth());
			supplyTable.backTable.getColumn("가공비")
					.setPreferredWidth(supplyTable.frontTable.getColumn("가공비").getPreferredWidth());
			supplyTable.backTable.getColumn("수량")
					.setPreferredWidth(supplyTable.frontTable.getColumn("수량").getPreferredWidth());
			supplyTable.backTable.getColumn("단가")
					.setPreferredWidth(supplyTable.frontTable.getColumn("단가").getPreferredWidth());
			supplyTable.backTable.getColumn("공급가액")
					.setPreferredWidth(supplyTable.frontTable.getColumn("공급가액").getPreferredWidth());
			supplyTable.backTable.getColumn("비고")
					.setPreferredWidth(supplyTable.frontTable.getColumn("비고").getPreferredWidth());
		} else {
			supplyTable.frontTable.getColumn("품목")
					.setPreferredWidth(supplyTable.backTable.getColumn("품목").getPreferredWidth());
			supplyTable.frontTable.getColumn("규격")
					.setPreferredWidth(supplyTable.backTable.getColumn("규격").getPreferredWidth());
			supplyTable.frontTable.getColumn("자재비")
					.setPreferredWidth(supplyTable.backTable.getColumn("자재비").getPreferredWidth());
			supplyTable.frontTable.getColumn("가공비")
					.setPreferredWidth(supplyTable.backTable.getColumn("가공비").getPreferredWidth());
			supplyTable.frontTable.getColumn("수량")
					.setPreferredWidth(supplyTable.backTable.getColumn("수량").getPreferredWidth());
			supplyTable.frontTable.getColumn("단가")
					.setPreferredWidth(supplyTable.backTable.getColumn("단가").getPreferredWidth());
			supplyTable.frontTable.getColumn("공급가액")
					.setPreferredWidth(supplyTable.backTable.getColumn("공급가액").getPreferredWidth());
			supplyTable.frontTable.getColumn("비고")
					.setPreferredWidth(supplyTable.backTable.getColumn("비고").getPreferredWidth());
		}
		/*
		if (supplyTable.init == false) {
			supplyTable.backTable.setEnabled(true);
			supplyTable.init = true;
		}
		*/
		if ((supplyTable.index + supplyTable.BackRow) > supplyTable.tableList.maxSize)
			supplyTable.tableList.resize();
		supplyTable.maxPage++;
		for (int i = 0; i < supplyTable.backTable.getRowCount(); i++) {
			for (int j = 5; j < supplyTable.backTable.getColumnCount(); j++) {
				supplyTable.backTable.setValueAt(null, i, j);
			}
		}
		supplyTable.tableList.listRe(supplyTable.backTable);
		supplyTable.tableList.listRe(supplyTable.frontTable);
	}

	void removePage() {
		if (supplyTable.maxPage == 1) {
			for (int i = 0; i < supplyTable.FrontRow - 1; i++)
				for (int j = 0; j < 6; j++)
					supplyTable.strList[i][j] = null;
			for (int i = 0; i < supplyTable.FrontRow - 1; i++) {
				for (int j = 0; j < 6; j++) {
					supplyTable.frontTable.setValueAt(null, i, j);
					supplyTable.strList[supplyTable.frontTable.getRowCount()][j] = null;
				}
			}
			for (int j = 0; j < 8; j++)
				supplyTable.frontTable.setValueAt(null, supplyTable.frontTable.getRowCount() - 1, j);
			supplyTable.tableList.listRe(supplyTable.frontTable);
		} else if (supplyTable.currPage == supplyTable.maxPage) {

			for (int i = supplyTable.index; i < supplyTable.index + supplyTable.BackRow - 1; i++)
				for (int j = 0; j < 6; j++) {
					supplyTable.backTable.setValueAt(null, i - supplyTable.index, j);
					supplyTable.strList[i][j] = null;
				}
			if (supplyTable.currPage == 1) {

			} else if (supplyTable.currPage == 2) {
				supplyTable.tableList.saveList(supplyTable.backTable, supplyTable.index, supplyTable.BackRow);
				supplyTable.index -= supplyTable.FrontRow;
				supplyTable.currPage--;
			} else {
				supplyTable.tableList.saveList(supplyTable.backTable, supplyTable.index, supplyTable.BackRow);
				supplyTable.index -= supplyTable.BackRow;
				supplyTable.currPage--;
				supplyTable.tableList.listRe(supplyTable.backTable);
			}
			for (int i = 0; i < supplyTable.backTable.getRowCount(); i++) {
				for (int j = 5; j < supplyTable.backTable.getColumnCount(); j++) {
					supplyTable.backTable.setValueAt(null, i, j);
				}
			}
			for (int j = 0; j < 6; j++)
				supplyTable.strList[supplyTable.index + supplyTable.BackRow - 1][j] = null;

			supplyTable.tableList.listRe(supplyTable.backTable);
			supplyTable.maxPage--;
			supplyTable.backTable.setValueAt("합계", supplyTable.BackRow - 1, 5);
			if (supplyTable.maxPage == 1) {
				for (int j = 0; j < 8; j++) {
					supplyTable.frontTable.setValueAt(null, supplyTable.frontTable.getRowCount() - 1, j);
				}
				for (int j = 0; j < 6; j++) {
					supplyTable.frontTable.setValueAt(null, supplyTable.frontTable.getRowCount() - 1, j);
					supplyTable.strList[supplyTable.frontTable.getRowCount() - 1][j] = null;
				}
				supplyTable.tableList.listRe(supplyTable.frontTable);
			}
		}
	}

	void after() {
		if (supplyTable.currPage == 1) {
			cardLayout.first(masterPane);
			supplyTable.valueChangedUpdate(supplyTable.frontTable);
		} else {
			cardLayout.last(masterPane);
			supplyTable.valueChangedUpdate(supplyTable.backTable);
		}
		page.setText(new String("page" + supplyTable.currPage + "/" + supplyTable.maxPage));
		// repaint();
	}

	public BufferedImage createComponentCapture(Component comp) {
		BufferedImage image = new BufferedImage(comp.getWidth() * 72 / 100, comp.getHeight() * 72 / 100,
				BufferedImage.TYPE_INT_RGB);
		BufferedImage img = new BufferedImage(comp.getWidth() * 72 / 100, comp.getHeight() * 72 / 100,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = (Graphics2D) image.getGraphics();
		g2d.scale(0.72, 0.72);
		comp.paint(g2d); // 여기서 이미지에 그려넣습니다.

		// g2d.drawImage(image, null, 0, 0);
		return image;
	}

	public void sort() {
		TableList listC = supplyTable.tableList;
		Est est = new Est();
		for (int i = 0; i < listC.maxSize; i++) {
			est.addProduct(listC.strList[i][0], listC.strList[i][1], listC.strList[i][2], listC.strList[i][3],
					listC.strList[i][4], listC.strList[i][5]);
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
