package Estimate;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import Demand.Demand;

class EstimateList {
	LoadData[] loadDatas;
	LoadData[] loadDataMatch;
	int maxSize;
	int count;
	int matchCount;

	String match, item;
	Date after, curr, before;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	EstimateList() {
		maxSize = 100;
		loadDatas = new LoadData[maxSize];
		loadDataMatch = new LoadData[maxSize];
		count = 0;
		match = "";
		item = "";
		matchCount = 0;
	}

	public void setDate(Date before, Date after) {
		this.before = before;
		this.after = after;
	}

	public void setMatchStr(String comp, String item) {
		this.match = comp;
		this.item = item;
	}

	void addList(String str) {
		String stn[] = str.split("_");
		if (stn.length != 4 || !stn[0].equals("°ßÀû¼­"))
			return;
		String name = stn[1];
		String date = stn[2];
		String no = stn[3].replaceAll(".csv", "");
		if (!name.equals("")) {
			if (count == maxSize)
				resize();
			loadDatas[count++] = new LoadData(date, name, no);
		}
	}

	void resize() {
		maxSize *= 2;
		LoadData temp[] = new LoadData[maxSize];
		loadDataMatch = new LoadData[maxSize];
		for (int i = 0; i < maxSize / 2; i++)
			temp[i] = loadDatas[i];
		loadDatas = temp;
	}

	public int getMatchCount() {
		matchCount = 0;
		for (int i = 0; i < count; i++) {
			try {
				curr = sdf.parse(loadDatas[i].date);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			if ((before == null || (curr.after(before))) && ((after == null) || curr.before(after))){
				if (loadDatas[i].name.matches(".*" + match + ".*")) {
					addMatch(loadDatas[i]);
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
		return loadDatas[index];
	}
}