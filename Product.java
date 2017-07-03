class Product{
	String name;
	String standard;
	String materialCost;
	String processedCost;
	String count;
	String etc;

	public Product(String name, String standard, String materialCost, String processedCost, String count, String etc) {
		this.name = name;
		this.standard = standard;
		this.materialCost = materialCost;
		this.processedCost = processedCost;
		this.count = count;
		this.etc = etc;
	}
	
	public Product(){
		this.name = new String();
		this.standard = new String();
		this.materialCost = new String();
		this.processedCost = new String();
		this.count = new String();
		this.etc = new String();
	}

	public void copy(Product product) {
		this.name = product.name;
		this.standard = product.standard;
		this.materialCost = product.materialCost;
		this.processedCost = product.processedCost;
		this.count = product.count;
		this.etc = product.etc;
	}
}