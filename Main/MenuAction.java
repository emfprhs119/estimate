package Main;

import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import FrameComponent.MainFrame;

public class MenuAction extends MouseAdapter implements ActionListener {
	JFrame frame;
	Function function;
	boolean isButton;
	public MenuAction(JFrame frame,Function function,boolean isButton){
		this.frame=frame;
		this.function=function;
		this.isButton=isButton;
	}
	public void actionPerformed(ActionEvent e) {
		String str;
		// 버튼 일경우 버튼의 라벨 메뉴 일경우 메뉴의 라벨 
		if (isButton)
			str= ((JButton) e.getSource()).getToolTipText();
		else
			str = ((MenuItem) e.getSource()).getLabel();
		// 이름에 따른 함수 호출
		switch (str) {
		case "새 견적서":
			if (Main.modify) {
				int choice = JOptionPane.showConfirmDialog(null, "변경 내용을 저장하시겠습니까?", "종료", JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.INFORMATION_MESSAGE);
				switch (choice) {
				case 0:
					function.save();
					new MainFrame(frame.getX(),frame.getY());
					frame.setVisible(false);
				case 1:
					new MainFrame(frame.getX(),frame.getY());
					frame.setVisible(false);
				case 2:
				}
			}
			else
			{
				new MainFrame(frame.getX(),frame.getY());
				frame.setVisible(false);
			}
			break;
		case "불러오기":
			function.load();
			break;
		case "저장하기":
			function.save();
			break;
		case "◀ 이전":
			function.leftPage();
			break;
		case "다음 ▶":
			function.rightPage();
			break;
		case "Pdf 내보내기":
		case "내보내기":
			function.pdfSave();
			break;
		case "종료":
			System.exit(0);
			break;
		case "도움말":
			((MainFrame) frame).helpPopup(true);
			break;
		case "About 견적서":
			JOptionPane.showMessageDialog(frame, "Version : v1.0\nEmail : emfprhs119@gmail.com", "About 견적서",1);
			break;
		default:
		}
	}
	@Override
	public void mouseExited(MouseEvent e) {
		((MainFrame) frame).helpPopup(false);
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		((MainFrame) frame).helpPopup(true);
	}
}