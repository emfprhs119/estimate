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
	Estimate est;
	JPanel frontBackPanel;
	CardLayout cardLayout;
	JLabel file;
	JLabel page;
	EstimateLoad estimateLoad;
	PdfSave pdfSave;

	public Func(Estimate est,WhitePanel frontBackPanel,CardLayout cardLayout,JLabel file, JLabel page) {
		this.est = est;
		this.frontBackPanel = frontBackPanel;
		this.cardLayout = cardLayout;
		this.file = file;
		this.page = page;
		pdfSave = new PdfSave();
		//estimateLoad = new EstimateLoad(productView, demandView, file, page);
	}

	void pdfSave() {
		pdfSave.exportPDF(dataSave(),est);
	}

	String dataSave() {
		Demand demand = est.demand;
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
			//fw.write(productView.maxPage + "/");
			for(int i=0;i<8;i++){
				fw.write(est.tableWidth[0] + "/");
			}
			fw.write("\r\n");
			for (int i = 0; i < est.productList.maxSize; i++) {
				fw.write(est.productList.getString(i));
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


	void load() {// 불러오기
		if (Main.modify) {
			int choice = JOptionPane.showConfirmDialog(null, "변경 내용을 저장하시겠습니까?", "불러오기",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if (choice == 0) {
				dataSave();
				estimateLoad.loadList();
				estimateLoad.tableInit();
				estimateLoad.setVisible(true);
			} else if (choice == 1) {
				estimateLoad.loadList();
				estimateLoad.tableInit();
				estimateLoad.setVisible(true);
			}
		} else {
			estimateLoad.loadList();
			estimateLoad.tableInit();
			estimateLoad.setVisible(true);
		}
	}

	void leftPage() {
	}

	void rightPage() {
	}

	void addPage() {
	}

	void removePage() {
	}

	void after() {
		/*
		if (productView.currPage == 1) {
			cardLayout.first(frontBackPanel);
			productView.valueChangedUpdate(productView.frontTable);
		} else {
			cardLayout.last(frontBackPanel);
			productView.valueChangedUpdate(productView.backTable);
		}
		page.setText(new String("page" + productView.currPage + "/" + productView.maxPage));
		*/
		// repaint();
	}
	public void loadDemand() {
	}

}
