
import java.io.BufferedWriter;
import java.io.FileWriter;

class DemandList {
	Demand[] demands;
	Demand[] demandMatch;
	String match;
	int maxSize;
	int top;
	int mattop;

	DemandList() {
		maxSize = 100;
		demands = new Demand[maxSize];
		demandMatch = new Demand[maxSize];
		top = 0;
		mattop = 0;
		match = "";
		// num = 0;
	}

	public void removeMat(int sel) {
		String name, tel, who;
		String fileName;
		fileName = new String("demandList.list");
		BufferedWriter fw;
		try {
			fw = new BufferedWriter(new FileWriter(fileName));
			for (int i = 0; i < top; i++) {
				if (demands[i] == demandMatch[sel])
					continue;
				name = demands[i].name;
				tel = demands[i].tel;
				who = demands[i].who;
				if (name == "" || name == null || name.length() == 0)
					continue;
				if (tel == "" || tel == null || tel.length() == 0) {
					tel = " ";
				}
				if (who == "" || who == null || who.length() == 0)
					who = " ";
				fw.write(name + "/" + tel + "/" + who + "/\r\n");
			}
			fw.flush();
			fw.close();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
	}

	public int getMatchCount() {
		mattop = 0;
		for (int i = 0; i < top; i++) {
			if (demands[i].name.matches(".*" + match + ".*")) {
				addMatch(demands[i]);
			}
		}
		return mattop;
	}

	private void addMatch(Demand Demand) {
		demandMatch[mattop++] = Demand;
	}

	public void setMatchStr(String text) {
		match = text;
	}

	void addList(Demand demand) {
		if (top == maxSize)
			resize();
		this.demands[top++] = demand;
	}

	void resize() {
		maxSize *= 2;
		demandMatch = new Demand[maxSize];
		Demand temp[] = new Demand[maxSize];
		for (int i = 0; i < maxSize / 2; i++)
			temp[i] = demands[i];
		demands = temp;
	}
}