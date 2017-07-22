package FrameComponent;
import java.awt.BorderLayout;
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

import Demand.DemandView;
import Estimate.Estimate;
import Main.Function;
import Main.Main;
import Main.MenuAction;
import Product.ProductView;
import Supply.SupplyView;

public class MainFrame extends JFrame {
	private Container contentPane;
	private Function function;
	protected void frameInit(){
		super.frameInit();
		setBounds(200, 0, 838, 1045);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		contentPane = this.getContentPane();
		contentPane.setLayout(null);
		contentPane.setBackground(Color.WHITE);
	}
	public MainFrame() {
		super("견적서");
		//이름,파일,페이지 레이블 초기화
		FrameLabel frameLabel = new FrameLabel();
		
		WhitePanel masterPanel = new WhitePanel();			//전후면 포함 패널
		WhiteRectPanel frontPanel= new WhiteRectPanel();	//전면 패널
		WhiteRectPanel backPanel = new WhiteRectPanel();	//후면 패널
		
		ViewManager viewManager = new ViewManager(contentPane,masterPanel);	//뷰 관리
		//function 클래스 생성
		function = new Function(viewManager,frameLabel);
		MenuAction action;
		action = new MenuAction(this,function,true);
		FrameButton frameButton = new FrameButton(action);
		action = new MenuAction(this,function,false);
		FrameMenuBar menuBar =new FrameMenuBar(action);
		FrameToolBar toolBar =new FrameToolBar(action);
		//-----전면 패널-------------------------------
		frontPanel.add(viewManager.getProductView().getSumTextLabel());	//전면에만 있는 합계금액 레이블
		frontPanel.add(viewManager.getProductView().getSumText());	//전면에만 있는 합계금액
		frontPanel.add(viewManager.getProductView().getFrontTablePanel());// 전면테이블
		frontPanel.add(viewManager.getDemandView());// 수요자
		frontPanel.add(viewManager.getSupplyView());// 공급자
		frontPanel.add(frameLabel.getTitle());// 견적서 텍스트
		//-----후면 패널-------------------------------
		backPanel.add(viewManager.getProductView().getBackTablePanel());// 후면 테이블
		//-----마스터 패널------------------------------
		masterPanel.add(frontPanel,"front");
		masterPanel.add(backPanel,"back");
		//-----프레임 컨테이너--------------------------
		//하단 합계 추가
		contentPane.add(viewManager.getProductView().getSumTextBottom());
		contentPane.add(viewManager.getProductView().getSumLabelField());
		contentPane.add(viewManager.getProductView().getSumBlankField());
		//버튼 추가
		Button[] buttons=frameButton.getButtons();
		for(Button button:buttons){
			contentPane.add(button);
		}
		//파일명,페이지명 레이블 추가
		contentPane.add(frameLabel.getFileName());
		contentPane.add(frameLabel.getPage());
		//------------------------------------------
		contentPane.add(masterPanel);
		
		//메뉴바
		setMenuBar(menuBar.getMenuBar()); // frame에 메뉴바 등록
		//창 닫기시 변경내용 저장여부 확인
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (Main.modify) {
					int choice = JOptionPane.showConfirmDialog(null, "변경 내용을 저장하시겠습니까?", "종료", JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.INFORMATION_MESSAGE);
					switch (choice) {
					case 0:
						function.save();
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
	
	
	
	
}



