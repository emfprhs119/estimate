package FrameComponent;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;

import Main.MenuAction;

public class FrameMenuBar {
	private MenuBar menuBar;
	public FrameMenuBar(MenuAction action) {
		menuBar = new MenuBar(); // 메뉴바
		MenuItem menuItems[];
		Menu menus[] =new Menu[2];
		// 주메뉴
		menus[0] = new Menu("파일"); 
		menus[1] = new Menu("편집");
		//---------------------------------------
		menuItems = new MenuItem[6];
		menuItems[0] = new MenuItem("새 견적서"); 
		menuItems[1] = new MenuItem("불러오기");
		menuItems[2] = new MenuItem("저장하기");
		menuItems[3] = new MenuItem("공급자 수정");
		menuItems[4] = new MenuItem("Pdf 내보내기");
		menuItems[5] = new MenuItem("종료");
		
		for(MenuItem menuItem:menuItems){
			menuItem.addActionListener(action);
			menus[0].add(menuItem);
		}
		//---------------------------------------
		menuItems = new MenuItem[2];
		menuItems[0] = new MenuItem("거래처 목록 불러오기");
		menuItems[1] = new MenuItem("테이블 정렬하기");
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
