package FrameComponent;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;

import Main.Main;

public class FrameLabel {
	public String getFileNameStr() {
		return fileName.getText().replace("견적서_", "").replace("save\\", "").replace(".csv", "");
	}
	public void setFileName(String fileName) {
		fileName=fileName.replace("견적서_", "").replace("save\\", "").replace(".csv", "");
		this.fileName.setText("견적서_"+fileName);
		this.fileName.setBounds(390 - fileName.length() * 5, 5, 300, 20);
	}
	public JLabel getPage() {
		return page;
	}
	public void setPageText(String str) {
		this.page.setText(str);
	}
	public JLabel getTitle() {
		return title;
	}
	private JLabel title;
	private JLabel fileName;
	private JLabel page;
	FrameLabel(){
		title=nameInit();
		fileName=fileInit();
		page=pageInit();
	}
	private JLabel nameInit() {
		JLabel title = new JLabel("견적서");
		title.setFont(new Font(Main.font, Font.BOLD, 60));
		title.setBounds(283, 0, 270, 100);
		return title;
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
	public JLabel getFileName() {
		return fileName;
	}
}
