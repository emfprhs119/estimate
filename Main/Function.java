package Main;

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

import Demand.Demand;
import Estimate.Estimate;
import Estimate.EstimateLoad;
import FrameComponent.FrameLabel;
import FrameComponent.ViewManager;
import Output.PdfSave;

public class Function {
	ViewManager viewManager;
	FrameLabel frameLabel;
	EstimateLoad estimateLoad;
	PdfSave pdfSave;

	public Function(ViewManager viewManager, FrameLabel frameLabel) {
		this.viewManager = viewManager;
		this.frameLabel = frameLabel;
		this.estimateLoad = new EstimateLoad(viewManager,frameLabel);
		pdfSave = new PdfSave();
	}

	void pdfSave() {
		String fileName=estimateLoad.saveFile(frameLabel.getFileNameStr());
		pdfSave.exportPDF(fileName, viewManager.getEstimate());
	}

	public void save(){
		String fileName=estimateLoad.saveFile(frameLabel.getFileNameStr());
		viewManager.getDemandView().addDemand();
		frameLabel.setFileName(fileName);
	}

	public void load() {// 불러오기
		if (Main.modify) {
			int choice = JOptionPane.showConfirmDialog(null, "변경 내용을 저장하시겠습니까?", "불러오기",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if (choice == 0) {
				save();
				estimateLoad.setVisible(true);
			} else if (choice == 1) {
				estimateLoad.setVisible(true);
			}
		} else {
			estimateLoad.setVisible(true);
		}
	}

	void leftPage() {
		if (viewManager.getProductView().getCurrPage() > 1){
			if (viewManager.getProductView().getCurrPage() == 2)
				viewManager.swapPanel("front");
			viewManager.getProductView().setCurrPage(viewManager.getProductView().getCurrPage() - 1);
		}
		refresh();
	}

	void rightPage() {
		if (viewManager.getProductView().getCurrPage() < viewManager.getProductView().getMaxPage()){
			if (viewManager.getProductView().getCurrPage() == 1)
				viewManager.swapPanel("back");
			viewManager.getProductView().setCurrPage(viewManager.getProductView().getCurrPage() + 1);
		}
		refresh();
	}

	void addPage() {
		if (viewManager.getProductView().addPage())
			if (viewManager.getProductView().getCurrPage() == 2)
				viewManager.swapPanel("back");
		refresh();
	}

	void removePage() {
		if (viewManager.getProductView().removePage())
			if (viewManager.getProductView().getCurrPage() == 1)
				viewManager.swapPanel("front");
		refresh();
	}
	void refresh(){
		viewManager.getProductView().refresh();
		frameLabel.setPageText(viewManager.getProductView().getPageStr());
	}
}
