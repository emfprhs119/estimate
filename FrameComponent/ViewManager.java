package FrameComponent;
import java.awt.CardLayout;
import java.awt.Container;

import Demand.Demand;
import Demand.DemandView;
import Estimate.Estimate;
import Product.ProductView;
import Supply.SupplyView;

public class ViewManager {
	public DemandView getDemandView() {
		return demandView;
	}
	protected SupplyView getSupplyView() {
		return supplyView;
	}
	public ProductView getProductView() {
		return productView;
	}
	private DemandView demandView;
	private SupplyView supplyView;
	private ProductView productView;
	
	Container contentPane;
	private WhitePanel masterPanel;
	private CardLayout cardLayout;
	public ViewManager(Container contentPane, WhitePanel masterPanel){
		this.contentPane=contentPane;
		cardLayout=new CardLayout();
		masterPanel.setLayout(cardLayout);
		masterPanel.setBounds(32, 30, 800, 1000);
		this.masterPanel=masterPanel;
		demandView = new DemandView(this);
		supplyView = new SupplyView(this);
		productView = new ProductView(this);
	}
	public void setEstimate(Estimate est){
		productView.setTableWidth(est.getTableWidth());
		demandView.setDemand(est.getDemand());
		supplyView.setSupply(est.getSupply());
		productView.setProductList(est.getProductList());
		productView.refresh();
	}
	
	public Estimate getEstimate(){
		Estimate est = new Estimate();
		est.setTableWidth(productView.getTableWidth());
		est.setDemand(demandView.getDemand());
		est.setSupply(supplyView.getSupply());
		est.setProductList(productView.getProductList());
		return est;
	}
	public void setDemand(Demand demand) {
		demandView.setDemand(demand);
	}
	public void setTableWidth(int[] tableWidth) {
		productView.setTableWidth(tableWidth);
	}
	public void swapPanel(String str) {
		//front or back
		cardLayout.show(masterPanel, str);
		contentPane.repaint();
	}
}
