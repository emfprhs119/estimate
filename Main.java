import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Scanner;


public class Main {
	final static Color color = Color.getHSBColor((float) 0.160, (float) 0.42, (float) 0.98);
	static String font = "Serif"; 	//default font
	static int fontSize=17;			//default fontSize
	static int tableSize[]={235,95,85,85,50,95,95,45}; //default tableSize
	static boolean modify;			//프로그램 수정여부(종료시 저장 여부 확인)
	static MainFrame mainFrame;
	public static void main(String[] args) {
		Scanner scan;
		modify=false;
		try {
			scan=new Scanner(new File("config.ini"));
			font=scan.next();
			fontSize=scan.nextInt();
			//첫줄 넘김
			scan.nextLine();	
			String str[]=scan.nextLine().split(":|,");
			for(int i=0;i<8;i++){
				//tableSize를 제외시키고 숫자들 매칭
				tableSize[i]=Integer.parseInt(str[i+1]);
			}
		} catch (FileNotFoundException ef) {
			try {
				//make config.ini file
				File file = new File("config.ini") ;
	            FileWriter fw = new FileWriter(file, true) ;
				fw.write(font+" "+fontSize+"\r\n");
				fw.write("tableSize:");
				for(int i=0;i<7;i++){
					fw.write(tableSize[i]+",");
				}
				fw.write(tableSize[7]+"");
	            fw.flush();
	            fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		mainFrame = new MainFrame(new Estimate());
	}
	
	public static String toNumFormat(long num) {
		if (num == 0)
			return "0";
		DecimalFormat df = new DecimalFormat("#,###");
		return df.format(num);
	}

	static public String toStrFormat(Object object) {
		if (object == null)
			return null;
		String num = (String) object;
		num = num.replaceAll("[^-0-9]", "");
		if (num.matches("^[0-9\\-]+")) {
			return num;
		} else {
			return null;
		}
	}

	static public long toLongFormat(Object obj) {
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
}
