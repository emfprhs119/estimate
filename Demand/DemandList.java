package Demand;

import javax.swing.JOptionPane;

class DemandList {
	private Demand[] demands;
	private Demand[] demandMatch;
	private String match;
	private int maxSize;
	private int count;
	private int matchCount;

	DemandList() {
		maxSize = 100;
		demands = new Demand[maxSize];
		demandMatch = new Demand[maxSize];
		count = 0;
		matchCount = 0;
		match = "";
	}
	public int getMatchCount() {
		matchCount = 0;
		for (int i = 0; i < count; i++) {
			if (demands[i].getName().matches(".*" + match + ".*")) {
				addMatch(demands[i]);
			}
		}
		return matchCount;
	}

	private void addMatch(Demand Demand) {
		demandMatch[matchCount++] = Demand;
	}

	public void setMatchStr(String text) {
		match = text;
	}

	boolean addList(Demand demand) {
		if (demand.getName().equals(null) || demand.getName().trim().equals("")){
			JOptionPane.showMessageDialog(null, "이름을 적어주세요.");
			return false;
		}
		if (isHas(demand))
			return true;
		if (count == maxSize)
			resize();
		this.demands[count++] = demand;
		return true;
	}
	public void removeList(int selet) {
		for (int i=selet;i<count-1;i++){
			demands[i]=demands[i+1];
		}
		count--;
	}
	boolean isHas(Demand demand){
		for(int i=0;i<count;i++){
			if (demands[i].equals(demand))
				return true;
		}
		return false;
	}
	void resize() {
		maxSize *= 2;
		demandMatch = new Demand[maxSize];
		Demand temp[] = new Demand[maxSize];
		for (int i = 0; i < maxSize / 2; i++)
			temp[i] = demands[i];
		demands = temp;
	}



	public void setList(DemandList loadList) {
		this.demands=loadList.demands;
		this.maxSize=loadList.maxSize;
		this.count=loadList.count;
	}
	public Demand getMatch(int index) {
		return demandMatch[index];
	}
	public int getCount() {
		return count;
	}
	public Demand getDemand(int index) {
		return demands[index];
	}
}