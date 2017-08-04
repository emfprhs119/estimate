package FrameComponent;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;

import Main.MenuAction;

public class FrameMenuBar {
	private MenuBar menuBar;
	public FrameMenuBar(MenuAction action) {
		menuBar = new MenuBar(); // �޴���
		MenuItem menuItems[];
		Menu menus[] =new Menu[2];
		// �ָ޴�
		menus[0] = new Menu("����"); 
		menus[1] = new Menu("����");
		//---------------------------------------
		menuItems = new MenuItem[5];
		menuItems[0] = new MenuItem("�� ������"); 
		menuItems[1] = new MenuItem("�ҷ�����");
		menuItems[2] = new MenuItem("�����ϱ�");
		//menuItems[3] = new MenuItem("������ ����");
		menuItems[3] = new MenuItem("Pdf ��������");
		menuItems[4] = new MenuItem("����");
		
		for(MenuItem menuItem:menuItems){
			menuItem.addActionListener(action);
			menus[0].add(menuItem);
		}
		//---------------------------------------
		
		menuItems = new MenuItem[2];
		menuItems[0] = new MenuItem("����");
		menuItems[1] = new MenuItem("About ������");
		for(MenuItem menuItem:menuItems){
			menuItem.addActionListener(action);
			menus[1].add(menuItem);
		}
		
		//---------------------------------------
		for(Menu menu:menus){
			menuBar.add(menu);
		}
	}
	public MenuBar getMenuBar() {
		return menuBar;
	}
	
}
