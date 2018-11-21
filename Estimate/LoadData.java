package Estimate;

public class LoadData {
	public String getDate() {
		return date;
	}

	public String getName() {
		return name;
	}

	public String getNo() {
		return no;
	}

	private String date;
	private String name;
	private String no;
	private String ext;

	LoadData(String date, String name,String no,String ext) {
		this.date = date;
		this.name = name;
		this.no = no;
		this.ext = ext;
	}

	public String getPath() {
		return "save\\견적서_"+name+"_"+date+"_"+no+"."+ext;
	}
	public String getString() {
		return "견적서_"+name+"_"+date+"_"+no;
	}

	public int compareTo(LoadData loadData, int flag) {
		switch(flag){
		case 0:return this.date.compareTo(loadData.date);
		case 1:return this.name.compareTo(loadData.name);
		case 2:return this.no.compareTo(loadData.no);
		}
		
		return 0;
	}
}
