package Main;

import javax.swing.JOptionPane;
import Estimate.EstimateLoad;
import FrameComponent.FrameLabel;
import FrameComponent.ViewManager;
import Output.PdfSave;
// �ܺ� �۵� ��ư �Լ�
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
	// .pdf
	void pdfSave() {
		String fileName=estimateLoad.saveFile(frameLabel.getFileNameStr());
		pdfSave.exportPDF(fileName, viewManager.getEstimate());
	}
	// .csv
	public void save(){
		String fileName=estimateLoad.saveFile(frameLabel.getFileNameStr());
		viewManager.getDemandView().addDemand();
		frameLabel.setFileName(fileName);
	}

	public void load() {// �ҷ�����
		if (Main.modify) {
			int choice = JOptionPane.showConfirmDialog(null, "���� ������ �����Ͻðڽ��ϱ�?", "�ҷ�����",
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
	// ���� ������
	void leftPage() {
		if (viewManager.getProductView().getCurrPage() > 1){
			if (viewManager.getProductView().getCurrPage() == 2)
				viewManager.swapPanel("front");
			viewManager.getProductView().removeLastPage();
			viewManager.getProductView().setCurrPage(viewManager.getProductView().getCurrPage() - 1);
			
		}
		refresh();
	}
	// �� ������
	void rightPage() {

		viewManager.getProductView().addLastPage();
		if (viewManager.getProductView().getCurrPage() < viewManager.getProductView().getMaxPage()){
			if (viewManager.getProductView().getCurrPage() == 1)
				viewManager.swapPanel("back");
			viewManager.getProductView().setCurrPage(viewManager.getProductView().getCurrPage() + 1);
		}
		refresh();
	}
	public void supplyEdit() {
		viewManager.getSupplyView().editSupply();
	}
	/*
	// ������ �߰�
	void addPage() {
		if (viewManager.getProductView().addLastPage())
			if (viewManager.getProductView().getCurrPage() == 2)
				viewManager.swapPanel("back");
		refresh();
	}
	// ������ ����
	void removePage() {
		if (viewManager.getProductView().removePage())
			if (viewManager.getProductView().getCurrPage() == 1)
				viewManager.swapPanel("front");
		refresh();
	}
	*/
	// ȭ�� ������Ʈ
	void refresh(){
		viewManager.getProductView().refresh();
		frameLabel.setPageText(viewManager.getProductView().getPageStr());
	}
}
