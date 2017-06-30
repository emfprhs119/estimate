import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class Main {
	final static Color color = Color.getHSBColor((float) 0.160, (float) 0.42, (float) 0.98);
	static String font = "Serif"; 	//default font
	static int fontSize=17;			//default fontSize
	static int tableSize[]={235,95,85,85,50,95,95,45}; //default tableSize
	static boolean modify;			//���α׷� ��������(����� ���� ���� Ȯ��)
	static MainFrame mainFrame;
	public static void main(String[] args) {
		Scanner scan;
		modify=false;
		try {
			//initialize font,fontSize and tableSize data
			scan=new Scanner(new File("config.ini"));
			font=scan.next();
			fontSize=scan.nextInt();
			//ù�� �ѱ�
			scan.nextLine();	
			String str[]=scan.nextLine().split(":|,");
			for(int i=0;i<8;i++){
				//tableSize�� ���ܽ�Ű�� ���ڵ� ��Ī
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
		mainFrame = new MainFrame();
	}
}