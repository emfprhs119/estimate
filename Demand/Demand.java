package Demand;

public class Demand {

	public String getDate() {
		return date;
	}

	public String getName() {
		return name;
	}

	public String getTel() {
		return tel;
	}

	public String getWho() {
		return who;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public void setWho(String who) {
		this.who = who;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getStrings() {
		String stn[] = new String[4];
		stn[0] = date;
		stn[1] = name;
		stn[2] = tel;
		stn[3] = who;
		return stn;
	}

	private String date;
	private String name;
	private String tel;
	private String who;

	public Demand() {
	}

	public Demand(String date, String name, String tel, String who) {
		this.date = date;
		this.name = name;
		this.tel = tel;
		this.who = who;
	}

	public Demand(String name, String tel, String who) {
		this.name = name;
		this.tel = tel;
		this.who = who;
	}

	public Demand(String[] stn) {
		int n=0;
		if (stn.length==4)
			this.date=stn[n++];
		this.name = stn[n++];
		this.tel = stn[n++];
		this.who = stn[n++];
	}

	public boolean equals(Object obj) {
		if (name.equals(((Demand) obj).name) && tel.equals(((Demand) obj).tel) && who.equals(((Demand) obj).who))
			return true;
		else
			return false;
	}

	public void setDemand(Demand demand) {
		this.name = demand.name;
		this.tel = demand.tel;
		this.who = demand.who;
	}

	public int compareTo(Demand demand, int flag) {
		switch(flag){
		case 0:return this.name.compareTo(demand.name);
		case 1:return this.who.compareTo(demand.who);
		case 2:return this.tel.compareTo(demand.tel);
		}
		
		return 0;
	}

}