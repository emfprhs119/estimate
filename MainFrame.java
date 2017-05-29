import java.awt.Button;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

class MainFrame extends JFrame {
	Est est;
	Func func;
	ActionButton action;
	Container contentPane;
	ContainerRect contentsPane;
	ContainerRect contentAddPane;
	MyPanel paneName, supplyPane, demandPane, backG, currPane;
	JLabel name, file;
	JLabel page;
	JPanel paneAdd;
	Demand demand;
	SupTable supTable;
	public static int curPage, flag;
	CardLayout card = new CardLayout();
	String displayFont, tableFont;
	int tableFontSize;
	Button button[] = new Button[9];

	public MainFrame() {
		super("견적서");
		setBounds(200, 0, 838, 1045);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.WHITE);
		contentPane = this.getContentPane();
		contentPane.setLayout(null);

		buttonInit();
		nameInit();
		contentsPane = new ContainerRect();
		contentAddPane = new ContainerRect();
		// contentsPane.setBounds(32, 30, 800, 1000);
		// contentAddPane.setBounds(32, 10000, 800, 1000);

		backG = new MyPanel();
		currPane = new MyPanel();
		currPane.setLayout(card);
		currPane.setBounds(32, 30, 800, 1000);

		Supply supply = new Supply();

		demand = new Demand();
		paneAdd = new JPanel();
		paneAdd.setBackground(Color.WHITE);
		supTable = new SupTable(paneAdd);
		Scanner scan;
		String str = null;
		func = new Func(this, supply, demand, supTable);
		action.func = func;
		demand.setFunc(func);

		backG.setBounds(0, 0, 2000, 2000);
		supTable.setBounds(18, 290, 720, 635);
		paneAdd.setBounds(18, 37, 720, 840);
		supply.setBounds(358, 90, 500, 200);
		demand.setBounds(0, 124, 350, 290);

		contentsPane.add(supTable.sum);
		contentsPane.add(supTable.sumT);
		/*contentsPane.add(supTable.sumF2);
		contentsPane.add(supTable.sumT2);
		contentsPane.add(supTable.sumR2);*/
		contentsPane.add(supTable);// 테이블
		contentsPane.add(demand);// 수요자
		contentsPane.add(supply);// 공급자
		contentsPane.add(paneName);// 견적서 텍스트

		contentAddPane.add(paneAdd);// 2장 이후 테이블

		supTable.setPopup(supTable,paneAdd);
		
		add(supTable.sumF2);
		add(supTable.sumT2);
		add(supTable.sumR2);

		currPane.add(contentsPane);
		currPane.add(contentAddPane);

		contentPane.add(file);
		contentPane.add(page);
		contentPane.add(currPane);
		// contentPane.add(contentAddPane);
		add(backG);
		menuLayout();
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (Main.modify) {
					int choice = JOptionPane.showConfirmDialog(null, "변경 내용을 저장하시겠습니까?", "종료", JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.INFORMATION_MESSAGE);
					switch (choice) {
					case 0:
						func.save();
						System.exit(0);
					case 1:
						System.exit(0);
					case 2:
					}
				}
				else
					System.exit(0);
			}
		});
		setVisible(true);

	}

	private void menuLayout() {
		ManuAction menuAction = new ManuAction();
		MenuBar menuBar = new MenuBar(); // 메뉴바
		Menu mnuFile = new Menu("파일"); // 주메뉴
		MenuItem mnuNew = new MenuItem("새 견적서"); // 부메뉴..
		MenuItem mnuOpen = new MenuItem("불러오기");
		MenuItem mnuSave = new MenuItem("저장하기");
		MenuItem mnuSup = new MenuItem("공급자 수정");
		MenuItem mnuExport = new MenuItem("Pdf 내보내기");
		MenuItem mnuExit = new MenuItem("종료");
		mnuNew.addActionListener(menuAction);
		mnuSave.addActionListener(menuAction);
		mnuSup.addActionListener(menuAction);
		mnuOpen.addActionListener(menuAction);
		mnuExport.addActionListener(menuAction);
		mnuExit.addActionListener(menuAction);
		mnuFile.add(mnuNew);
		mnuFile.add(mnuOpen);
		mnuFile.add(mnuSave);
		// mnuFile.add(mnuSup);//////////////////////차후 추가
		mnuFile.add(mnuExport);
		mnuFile.addSeparator(); // 구분선
		mnuFile.add(mnuExit);

		Menu mnuModif = new Menu("편집");
		MenuItem mnuGetDemend = new MenuItem("거래처 목록 불러오기");
		MenuItem mnuTableSort = new MenuItem("테이블 정렬하기");
		mnuGetDemend.addActionListener(menuAction);
		mnuTableSort.addActionListener(menuAction);
		mnuModif.add(mnuGetDemend);
		//mnuModif.add(mnuTableSort);
		
		/*
		 * Menu mnuFont = new Menu("서식"); Menu mnuViewFont = new Menu("화면 글꼴"); Menu mnuTableFont = new
		 * Menu("테이블 글꼴"); Menu mnuTableFontSize = new Menu("테이블 글꼴 크기");
		 * 
		 * GraphicsEnvironment ge = null; ge = GraphicsEnvironment.getLocalGraphicsEnvironment(); Font[]
		 * fonts = ge.getAllFonts(); String fontName; MenuItem item;
		 * 
		 * for(int i=0; i<fonts.length;i++){ if(fonts[i].canDisplay('한')){ item = new
		 * MenuItem(fonts[i].getFontName()); item.addActionListener(menuAction); mnuViewFont.add(item);
		 * item = new MenuItem(fonts[i].getFontName()); item.addActionListener(menuAction);
		 * mnuTableFont.add(item); } } for(int i=10; i<31;i++){ item = new MenuItem(String.valueOf(i));
		 * item.addActionListener(menuAction); mnuTableFontSize.add(item);
		 * 
		 * } mnuFont.add(mnuViewFont); mnuFont.add(mnuTableFont); mnuFont.add(mnuTableFontSize);
		 */
		menuBar.add(mnuFile); // 메뉴바에 주메뉴 등록
		menuBar.add(mnuModif); // 메뉴바에 주메뉴 등록
		// menuBar.add(mnuFont); // 메뉴바에 주메뉴 등록

		this.setMenuBar(menuBar); // frame에 메뉴바 등록
	}

	class ManuAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			String str = ((MenuItem) e.getSource()).getLabel();
			switch (str) {
			case "새 견적서":
				if (Main.modify) {
					int choice = JOptionPane.showConfirmDialog(null, "변경 내용을 저장하시겠습니까?", "종료", JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.INFORMATION_MESSAGE);
					switch (choice) {
					case 0:
						func.save();
						new MainFrame();
						setVisible(false);
					case 1:
						new MainFrame();
						setVisible(false);
					case 2:
					}
				}
				else
				{
					new MainFrame();
					setVisible(false);
				}
				break;
			case "불러오기":
				func.load();
				break;
			case "저장하기":
				func.save();
				break;
			case "Pdf 내보내기":
				func.pdfSave();
				break;
			case "종료":
				setVisible(false);
			case "거래처 목록 불러오기":
				func.loadDemand();
				break;
			case "테이블 정렬하기":
				func.sort();
				break;
			default:
				if (((Menu) ((MenuItem) e.getSource()).getParent()).getLabel().equals("화면 글꼴")) {
					displayFont = str;
					setDisplayFont();
				} else if (((Menu) ((MenuItem) e.getSource()).getParent()).getLabel().equals("테이블 글꼴")) {
					tableFont = str;
					setTableFont(tableFont, tableFontSize);
				} else if (((Menu) ((MenuItem) e.getSource()).getParent()).getLabel().equals("테이블 글꼴 크기")) {
					tableFontSize = Integer.parseInt(str);
					setTableFont(tableFont, tableFontSize);
				}
			}
			func.after();
		}
	}

	public void setDisplayFont() {
		name.setFont(new Font(displayFont, 0, 60));
		file.setFont(new Font(displayFont, 0, 15));
		contentsPane.repaint();
		repaint();
	}

	public void setTableFont(String font, int size) {
		supTable.setFont(font, size);
		contentsPane.repaint();
		repaint();
	}

	public void nameInit() {
		name = new JLabel("견적서");
		name.setFont(new Font(Main.font, Font.BOLD, 60));
		name.setBounds(275, 0, 270, 100);
		file = new JLabel("New Document");
		file.setFont(new Font(Main.font, Font.BOLD, 15));
		file.setBounds(419 - file.getText().length() * 6, 5, 300, 20);
		page = new JLabel("page1/1");
		page.setFont(new Font("돋움", 0, 15));
		page.setBounds(43, 30, 80, 30);
		paneName = new MyPanel();
		paneName.setBounds(8, 0, 500, 290);
		paneName.add(name);
	}

	public void buttonInit() {
		action = new ActionButton();

		button[0] = new Button("내보내기");
		button[0].setBounds(620, 925, 140, 45);
		button[1] = new Button("저장");
		button[1].setBounds(480, 925, 120, 45);
		button[2] = new Button("불러오기");
		button[2].setBounds(310, 925, 150, 45);
		button[3] = new Button("◀");
		button[3].setBounds(60, 925, 70, 45);
		button[4] = new Button("▶");
		button[4].setBounds(150, 925, 70, 45);
		button[5] = new Button("추가");
		button[5].setBounds(230, 925, 70, 20);
		button[6] = new Button("제거");
		button[6].setBounds(230, 925 + 30, 70, 20);
		/*
		 * button[7] = new Button("정렬"); button[7].setBounds(5, 330, 35, 27); button[8] = new
		 * Button("목록"); button[8].setBounds(5, 190, 35, 27);
		 */
		for (int i = 0; i < 7; i++) {
			button[i].setFont(new Font(Main.font, Font.BOLD, 25));
			button[i].setVisible(true);
			button[i].addActionListener(action);
			add(button[i]);
			// Adding ActionListener on the Button
		}
		/*
		 * button[7].setFont(new Font(Main.font, Font.BOLD, 15)); button[8].setFont(new Font(Main.font,
		 * Font.BOLD, 15));
		 */
	}
}

class ActionButton implements ActionListener {
	Func func;

	public void actionPerformed(ActionEvent e) {
		if (((Button) e.getSource()).getLabel().equals("저장")) {
			func.save();
		}
		if (((Button) e.getSource()).getLabel().equals("내보내기")) {
			func.pdfSave();
		}
		if (((Button) e.getSource()).getLabel().equals("불러오기")) {
			func.load();
		}
		if (((Button) e.getSource()).getLabel().equals("◀")) {
			func.leftPage();
		}
		if (((Button) e.getSource()).getLabel().equals("▶")) {
			func.rightPage();
		}
		if (((Button) e.getSource()).getLabel().equals("추가")) {
			func.addPage();
		}
		if (((Button) e.getSource()).getLabel().equals("제거")) {
			func.removePage();
		}
		if (((Button) e.getSource()).getLabel().equals("정렬")) {
			func.sort();
		}
		if (((Button) e.getSource()).getLabel().equals("목록")) {
			func.loadDemand();
		}
		func.after();

	}
}
