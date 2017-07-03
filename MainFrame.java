import java.awt.Button;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

class MainFrame extends JFrame {
	private Container contentPane;
	private Func func;
	protected void frameInit(){
		super.frameInit();
		setBounds(200, 0, 838, 1045);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = this.getContentPane();
		contentPane.setLayout(null);
		contentPane.setBackground(Color.WHITE);
	}
	public MainFrame(Estimate est) {
		super("견적서");
		
		DemandView demandView;
		SupplyView supplyView;
		ProductView productView;
		CardLayout cardlayout;
		
		//이름,파일,페이지 레이블 초기화
		JLabel name=nameInit();
		JLabel file=fileInit();
		JLabel page=pageInit();
		
		WhitePanel masterPane = new WhitePanel();			//전후면 포함 패널
		WhiteRectPanel frontPanel= new WhiteRectPanel();	//전면 패널
		WhiteRectPanel backPanel = new WhiteRectPanel();	//후면 패널
		
		
		WhitePanel frontTablePane = new WhitePanel();	//전면 테이블 패널
		WhitePanel backTablePane = new WhitePanel();	//후면 테이블 패널
		
		demandView = new DemandView(est.demand);
		supplyView = new SupplyView(est.supply);
		productView = new ProductView(est.productList,frontTablePane,backTablePane);
		
		//레이아웃 조정
		cardlayout=new CardLayout();
		masterPane.setLayout(cardlayout);
		masterPane.setBounds(32, 30, 800, 1000);
		frontTablePane.setBounds(18, 290, 720, 635);
		backTablePane.setBounds(18, 37, 720, 840);
		
		//function 클래스 생성
		//func = new Func(supply, demand, masterPane,supplyTable,cardlayout,file,page);
		
		//-----전면 패널-------------------------------
		frontPanel.add(productView.sumTextLabel);	//전면에만 있는 합계금액 레이블
		frontPanel.add(productView.sumText);	//전면에만 있는 합계금액
		frontPanel.add(frontTablePane);// 전면테이블
		frontPanel.add(demandView);// 수요자
		frontPanel.add(supplyView);// 공급자
		frontPanel.add(name);// 견적서 텍스트
		//-----후면 패널-------------------------------
		backPanel.add(backTablePane);// 후면 테이블
		//-----마스터 패널------------------------------
		masterPane.add(frontPanel);
		masterPane.add(backPanel);
		//-----프레임 컨테이너--------------------------
		//하단 합계 추가
		contentPane.add(productView.sumTextBottom);
		contentPane.add(productView.sumLabelField);
		contentPane.add(productView.sumBlankField);
		//버튼 추가
		addButton(contentPane,func);
		//파일명,페이지명 레이블 추가
		contentPane.add(file);
		contentPane.add(page);
		//------------------------------------------
		contentPane.add(masterPane);
		
		//메뉴바
		menubarLayout();
		
		//창 닫기시 변경내용 저장여부 확인
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (Main.modify) {
					int choice = JOptionPane.showConfirmDialog(null, "변경 내용을 저장하시겠습니까?", "종료", JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.INFORMATION_MESSAGE);
					switch (choice) {
					case 0:
						func.dataSave();
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
	private JLabel nameInit() {
		JLabel name = new JLabel("견적서");
		name.setFont(new Font(Main.font, Font.BOLD, 60));
		name.setBounds(283, 0, 270, 100);
		return name;
	}
	private JLabel fileInit() {
		JLabel file = new JLabel("New Document");
		file.setFont(new Font(Main.font, Font.BOLD, 15));
		file.setBounds(419 - file.getText().length() * 6, 5, 300, 20);
		return file;
	}
	private JLabel pageInit() {
		JLabel page = new JLabel("page1/1");
		page.setFont(new Font("돋움", 0, 15));
		page.setBounds(43, 30, 80, 30);
		return page;
	}
	private void menubarLayout() {
		ManuAction menuAction = new ManuAction(func);
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
		menuBar.add(mnuFile); // 메뉴바에 주메뉴 등록
		menuBar.add(mnuModif); // 메뉴바에 주메뉴 등록
		// menuBar.add(mnuFont); // 메뉴바에 주메뉴 등록

		this.setMenuBar(menuBar); // frame에 메뉴바 등록
	}
	
	private void addButton(Container container,Func func) {
		Button button[] = new Button[7];
		ActionButton action = new ActionButton(func);
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
		for (int i = 0; i < 7; i++) {
			button[i].setFont(new Font(Main.font, Font.BOLD, 25));
			button[i].setVisible(true);
			button[i].addActionListener(action);
			container.add(button[i]);
		}
	}
	class ManuAction implements ActionListener {
		Func func;
		ManuAction(Func func){
			this.func=func;
		}
		public void actionPerformed(ActionEvent e) {
			String str = ((MenuItem) e.getSource()).getLabel();
			switch (str) {
			case "새 견적서":
				if (Main.modify) {
					int choice = JOptionPane.showConfirmDialog(null, "변경 내용을 저장하시겠습니까?", "종료", JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.INFORMATION_MESSAGE);
					switch (choice) {
					case 0:
						func.dataSave();
						new MainFrame(new Estimate());
						setVisible(false);
					case 1:
						new MainFrame(new Estimate());
						setVisible(false);
					case 2:
					}
				}
				else
				{
					new MainFrame(new Estimate());
					setVisible(false);
				}
				break;
			case "불러오기":
				func.load();
				break;
			case "저장하기":
				func.dataSave();
				break;
			case "Pdf 내보내기":
				func.pdfSave();
				break;
			case "종료":
				setVisible(false);
			case "거래처 목록 불러오기":
				func.loadDemand();
				break;
			default:
			}
			func.after();
		}
	}
}

class ActionButton implements ActionListener {
	Func func;
	ActionButton(Func func){
		this.func=func;
	}
	public void actionPerformed(ActionEvent e) {
		if (((Button) e.getSource()).getLabel().equals("저장")) {
			func.dataSave();
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
			//func.sort();
		}
		if (((Button) e.getSource()).getLabel().equals("목록")) {
			func.loadDemand();
		}
		func.after();

	}
}

//배경이 하얀색이고 layout이 null인 Container 외곽선 draw
class WhiteRectPanel extends WhitePanel {
	private static final long serialVersionUID = 6805380713240246261L;
	public void paint(Graphics g) {
		super.paint(g);
		g.drawRect(0, 0, 755,875);
	}
	
}

//배경이 하얀색이고 layout이 null인 panel
class WhitePanel extends JPanel {
	private static final long serialVersionUID = 5640668174921441140L;
	WhitePanel(){
		setBackground(Color.WHITE);
		setLayout(null);
	}
}

