package Estimate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
//견적서 리스트
class EstimateList {
	private LoadData[] loadDataArr;
	private LoadData[] loadDataMatch;
	private int maxSize;
	private int count;
	private int matchCount;

	private String match;
	private Date after, curr, before;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	EstimateList() {
		maxSize = 100;
		loadDataArr = new LoadData[maxSize];
		loadDataMatch = new LoadData[maxSize];
		count = 0;
		match = "";
		matchCount = 0;
	}

	public void setDate(Date before, Date after) {
		this.before = before;
		this.after = after;
	}

	public void setMatchStr(String comp, String item) {
		this.match = comp;
	}

	boolean addList(LoadData loadData) {
		if (loadData.equals(null) || loadData.getName().trim().equals("")) {
			return false;
		}
		if (count == maxSize)
			resize();
		this.loadDataArr[count++] = loadData;
		return true;
	}

	void resize() {
		maxSize *= 2;
		LoadData temp[] = new LoadData[maxSize];
		loadDataMatch = new LoadData[maxSize];
		for (int i = 0; i < maxSize / 2; i++)
			temp[i] = loadDataArr[i];
		loadDataArr = temp;
	}

	public int getMatchCount() {
		matchCount = 0;
		for (int i = 0; i < count; i++) {
			try {
				curr = sdf.parse(loadDataArr[i].getDate());
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			if ((before == null || (curr.after(before))) && ((after == null) || curr.before(after))) {
				if (loadDataArr[i].getName().matches(".*" + match + ".*")) {
					addMatch(loadDataArr[i]);
				}
			}
		}
		return matchCount;
	}

	private void addMatch(LoadData loadData) {
		loadDataMatch[matchCount++] = loadData;
	}

	public LoadData getMatch(int index) {
		return loadDataMatch[index];
	}

	public int getCount() {
		return count;
	}

	public LoadData getLoadData(int index) {
		return loadDataArr[index];
	}

	public void setList(EstimateList estimateList) {
		this.loadDataArr = estimateList.loadDataArr;
		this.maxSize = estimateList.maxSize;
		this.count = estimateList.count;
	}

	public void matchSort(int num,boolean decreasingFlag) {
		for(int i=0;i<matchCount-1;i++){
			for(int j=0;j<matchCount-1;j++){
				if(loadDataMatch[j].compareTo(loadDataMatch[j+1],num)>0){
					if (decreasingFlag)
						matchSwap(j,j+1);
				}else if (loadDataMatch[j].compareTo(loadDataMatch[j+1],num)<0){
					if (!decreasingFlag)
						matchSwap(j,j+1);
				}
			}
		}
	}
	public void matchSwap(int i,int j){
		LoadData tmp=loadDataMatch[i];
		loadDataMatch[i]=loadDataMatch[j];
		loadDataMatch[j]=tmp;
	}
	
}