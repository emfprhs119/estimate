public class Estimate {
	Supply supply;//공급자
	Demand demand;//수요자
	int tableWidth[]; 
	ProductList productList;//상품리스트
	Estimate(){
		supply = new Supply();
		demand = new Demand();
		productList = new ProductList();
	}
	public void setDemand(Demand demand) {
		this.demand=demand;
	}
}

