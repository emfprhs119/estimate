package Demand;

import javax.swing.JOptionPane;

//거리처 리스트
class DemandList {
	private Demand[] demandArr;
	private Demand[] demandMatch;
	private String match;
	private int maxSize;
	private int count;
	private int matchCount;

	DemandList() {
		maxSize = 100;
		demandArr = new Demand[maxSize];
		demandMatch = new Demand[maxSize];
		count = 0;
		matchCount = 0;
		match = "";
	}
	public int getMatchCount() {
		matchCount = 0;
		for (int i = 0; i < count; i++) {
			if (demandArr[i].getName().matches(".*" + match + ".*")) {
				addMatch(demandArr[i]);
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
			JOptionPane.showMessageDialog(null, "상호를 적어주세요.");
			return false;
		}
		if (isHas(demand))
			return true;
		if (count == maxSize)
			resize();
		this.demandArr[count++] = demand;
		return true;
	}
	public void removeList(int selet) {
		for (int i=selet;i<count-1;i++){
			demandArr[i]=demandArr[i+1];
		}
		count--;
	}
	boolean isHas(Demand demand){
		for(int i=0;i<count;i++){
			if (demandArr[i].equals(demand))
				return true;
		}
		return false;
	}
	void resize() {
		maxSize *= 2;
		demandMatch = new Demand[maxSize];
		Demand temp[] = new Demand[maxSize];
		for (int i = 0; i < maxSize / 2; i++)
			temp[i] = demandArr[i];
		demandArr = temp;
	}



	public void setList(DemandList loadList) {
		this.demandArr=loadList.demandArr;
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
		return demandArr[index];
	}
	
	public void matchSort(int num,boolean decreasingFlag) {
		for(int i=0;i<matchCount-1;i++){
			for(int j=0;j<matchCount-1;j++){
				if(demandMatch[j].compareTo(demandMatch[j+1],num)>0){
					if (decreasingFlag)
						matchSwap(j,j+1);
				}else if (demandMatch[j].compareTo(demandMatch[j+1],num)<0){
					if (!decreasingFlag)
						matchSwap(j,j+1);
				}
			}
		}
	}
	public void matchSwap(int i,int j){
		Demand tmp=demandMatch[i];
		demandMatch[i]=demandMatch[j];
		demandMatch[j]=tmp;
	}
}