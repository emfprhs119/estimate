import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class ContainerRect extends Container {
	ContainerRect(){
		super();
		setLayout(null);
	}
	public void paint(Graphics g) {
		super.paint(g);
		g.drawRect(0, 0, 755,875);
	}
	
}
class MyPanel extends JPanel {
	MyPanel(){
		setBackground(Color.WHITE);
		setLayout(null);
	}
}



	// Color color=new Color((float) 128/255, (float)169/255, (float)1);
	// Color.getHSBColor((float) 0.620, (float)0.12, (float)0.98)

public class Main {
	final static Color color = Color.getHSBColor((float) 0.160, (float) 0.42, (float) 0.98);
	static String font = "Serif";
	static int size=17;
	static int tableSize[];
	static boolean modify;
	static MainFrame mainFrame;
	public static void main(String[] args) {
		Scanner scan;
		tableSize=new int[8];
		modify=false;
		try {
			scan=new Scanner(new File("config.ini"));
			font="Serif";
			scan.next();
			size=scan.nextInt();
			scan.nextLine();
			String str[]=scan.nextLine().split(":|,");

			for(int i=0;i<8;i++){
				tableSize[i]=Integer.parseInt(str[i+1]);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mainFrame = new MainFrame();
		//new PdfSave();
	}
}
