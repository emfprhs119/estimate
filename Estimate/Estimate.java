package Estimate;
import Demand.Demand;
import Product.ProductList;
import Supply.Supply;
//견적서 데이터
public class Estimate {
	private Supply supply;	//공급자
	private Demand demand;	//수요자
	private ProductList productList;	//상품리스트
	private int tableWidth[];	// 상품 테이블 열 가로크기
	public Estimate(){
		supply = new Supply();
		demand = new Demand();
		productList = new ProductList();
	}
	public Supply getSupply() {
		return supply;
	}
	public void setSupply(Supply supply) {
		this.supply = supply;
	}
	public int[] getTableWidth() {
		return tableWidth;
	}
	public void setTableWidth(int[] tableWidth) {
		this.tableWidth = tableWidth;
	}
	public ProductList getProductList() {
		return productList;
	}
	public void setProductList(ProductList productList) {
		this.productList = productList;
	}
	public void setDemand(Demand demand) {
		this.demand=demand;
	}
	public Demand getDemand() {
		return demand;
	}
}

