package Main;
import java.awt.Color;
import java.text.DecimalFormat;
import FrameComponent.MainFrame;


public class Main {
	public final static Color YELLOW = Color.getHSBColor((float) 0.160, (float) 0.42, (float) 0.98);
	public static String version = "v1.1";
	public static String font = "Serif"; 	//default font
	public static int fontSize=17;			//default fontSize
	public static int tableSize[]={235,95,85,85,50,95,95,45}; //default tableSize
	public static int FrontRow=22; // ���� ����Ʈ ���
	public static int BackRow = 33; // �ĸ� ����Ʈ ���
	public static boolean modify=false; //���α׷� ��������(����� ���� ���� Ȯ��)
	public static void main(String[] args) {
		new MainFrame();
	}
	// long to #,### transform
	public static String longToMoneyString(long num) {
		if (num == 0)
			return "0";
		DecimalFormat df = new DecimalFormat("#,###");
		return df.format(num);
	}
	// String to longString format transform
	public static String stringToLongString(Object object) {
		if (object == null)
			return null;
		String num = (String) object;
		num = num.replaceAll("[^-0-9]", "");
		if (num.matches("^[0-9\\-]+")) {
			return num;
		} else {
			return "0";
		}
	}
	// String to long
	public static long StringToLong(Object obj) {
		if (obj == null)
			return 0;
		String num = (String) obj;
		num = num.replaceAll("[^-0-9]", "");
		if (num.matches("^[0-9\\-]+")) {
			return Long.parseLong(num);
		} else {
			return 0;
		}
	}
	// if null to '-'  if contain , to both side " " 
	public static String checkString(String str){
		if (str==null || str.replace(" ","").equals(""))
			return "-";
		else if (str.contains(","))
			return "\""+str+"\"";
		else
			return str;
	}
}
