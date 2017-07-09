package FrameComponent;

import java.awt.Button;
import java.awt.Container;
import java.awt.Font;

import Main.Main;

public class FrameButton {
	private Button button[];
	public FrameButton(MenuAction action) {
		button = new Button[7];
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
		}
	}
	public Button[] getButtons() {
		return button;
	}
}
