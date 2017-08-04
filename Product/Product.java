package Product;

import Main.Main;
//Ç°¸ñ
public class Product {
	public String getName() {
		return name;
	}

	public String getStandard() {
		return standard;
	}

	public String getMaterialCost() {
		if (materialCost==0)
			return "";
		else
			return Main.longToMoneyString(materialCost);
	}

	public String getProcessedCost() {
		if (processedCost==0)
			return "";
		else
			return Main.longToMoneyString(processedCost);
	}

	public String getCount() {
		if (count==0)
			return "";
		else
			return Main.longToMoneyString(count);
	}

	public String getEtc() {
		return etc;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public void setMaterialCost(String materialCost) {
		if (materialCost != null)
			this.materialCost = Long.parseLong(materialCost.replace(",", ""));
	}

	public void setProcessedCost(String processedCost) {
		if (processedCost != null)
			this.processedCost = Long.parseLong(processedCost.replace(",", ""));
	}

	public void setCount(String count) {
		if (count != null)
			this.count = Long.parseLong(count.replace(",", ""));
	}

	public void setEtc(String etc) {
		this.etc = etc;
	}
	
	public String[] getStrings() {
		String stn[] = new String[6];
		stn[0]= name;
		stn[1] =standard;
		stn[2] =getMaterialCost() ;
		stn[3] =getProcessedCost();
		stn[4] =getCount();
		stn[5] =etc;
		return stn;
	}

	private String name;
	private String standard;
	private long materialCost;
	private long processedCost;
	private long count;
	private String etc;

	/*
	 * public Product(String name, String standard, String materialCost, String
	 * processedCost, String count, String etc) { this.name = name;
	 * this.standard = standard; this.materialCost = materialCost;
	 * this.processedCost = processedCost; this.count = count; this.etc = etc; }
	 */
	public Product() {
		this.name = new String();
		this.standard = new String();
		this.materialCost = 0;
		this.processedCost = 0;
		this.count = 0;
		this.etc = new String();
	}

	public Product(String[] stn) {
		name = stn[0];
		standard = stn[1];
		materialCost = Long.parseLong(stn[2]==""?"0":Main.stringToLongString(stn[2]));
		processedCost = Long.parseLong(stn[3]==""?"0":Main.stringToLongString(stn[3]));
		count = Long.parseLong(stn[4]==""?"0":Main.stringToLongString(stn[4]));
		etc = stn[5];
	}

	public void setProduct(Product product) {
		this.name = product.name;
		this.standard = product.standard;
		this.materialCost = product.materialCost;
		this.processedCost = product.processedCost;
		this.count = product.count;
		this.etc = product.etc;
	}

	public String getCost() {
		if (materialCost + processedCost==0)
			return "";
		else
			
			return Main.longToMoneyString(materialCost + processedCost);
	}

	public long getSumMoney() {
		return (materialCost + processedCost) * count;
	}

	public String getSumMoneyString() {
		if ((materialCost + processedCost) * count==0)
			return "";
		else
			return Main.longToMoneyString((materialCost + processedCost) * count);
	}

	public boolean isNull() {
		if(name==null || name.equals("")){
			return true;
		}else
			return false;
	}
}