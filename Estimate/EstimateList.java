package Estimate;
//견적서 리스트
class EstimateList {
	//검색 기능 차후 추가
	private LoadData[] loadDataArr;		// 로드한 데이터 배열
	private int maxSize;
	private int count;
	EstimateList() {
		maxSize = 100;
		loadDataArr = new LoadData[maxSize];
		count = 0;
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
		for (int i = 0; i < maxSize / 2; i++)
			temp[i] = loadDataArr[i];
		loadDataArr = temp;
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

	public void arrSort(int num,boolean decreasingFlag) {
		for(int i=0;i<count-1;i++){
			for(int j=0;j<count-1;j++){
				if(loadDataArr[j].compareTo(loadDataArr[j+1],num)>0){
					if (decreasingFlag)
						dataSwap(j,j+1);
				}else if (loadDataArr[j].compareTo(loadDataArr[j+1],num)<0){
					if (!decreasingFlag)
						dataSwap(j,j+1);
				}
			}
		}
	}
	public void dataSwap(int i,int j){
		LoadData tmp=loadDataArr[i];
		loadDataArr[i]=loadDataArr[j];
		loadDataArr[j]=tmp;
	}
	
}