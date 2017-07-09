package Main;

public class CsvPasser {
	public static String[] csvSplit(String str) {
		String[] resultStr = null;
		String result = "";

		String[] a = str.split(",");
		int cnt = 0;
		String temp = "";
		for (int i = 0; i < a.length; i++) {
			if (a[i].indexOf("\"") == 0) { // "문자로 시작
				if (a[i].lastIndexOf("\"") == a[i].length() - 1) {	// ex) "text",
					result += a[i].replaceAll("\"", "");
				} else {
					cnt++;
					temp += a[i].replaceAll("\"", "");
				}
			} else if (a[i].lastIndexOf("\"") == a[i].length() - 1) {	// ex) text",
				if (cnt > 0) {
					result += temp + "," + a[i].replaceAll("\"", "");
					cnt = 0;
					temp = "";
				}
			} else {
				if (cnt > 0) {
					cnt++;
					temp += "," + a[i].replaceAll("\"", "");
				} else {
					result += a[i];
				}
			}
			if (i != a.length - 1 && cnt == 0)
				result += "|,|";//최종 split 문자
		}
		resultStr = result.split("\\|,\\|");

		return resultStr;
	}
}
