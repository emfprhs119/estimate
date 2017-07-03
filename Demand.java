
public class Demand {

	String date;
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
		this.name=name;
	}
	String name;
	String tel;
	String who;

	public Demand() {
	}

	public Demand(String name, String tel, String who) {
		this.date = null;
		this.name = name;
		this.tel = tel;
		this.who = who;
	}

	public boolean equals(Object obj) {
		if (name.equals(((Demand) obj).name) && tel.equals(((Demand) obj).tel) && who.equals(((Demand) obj).who))
			return true;
		else
			return false;
	}

	
}