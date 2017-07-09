package Product;

import java.awt.Toolkit;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class Clipboard implements ClipboardOwner {
	java.awt.datatransfer.Clipboard clipboard;
	Clipboard() { 
		clipboard = Toolkit.getDefaultToolkit().getSystemClipboard(); 
	}
	void copy(String str){
	    StringSelection contents = new StringSelection(str); 
	    clipboard.setContents(contents, null); 
	}
	String pasteData(){
		try {
			return ((String) clipboard.getContents(this).getTransferData(DataFlavor.stringFlavor)).trim();
		} catch (UnsupportedFlavorException | IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	@Override
	public void lostOwnership(java.awt.datatransfer.Clipboard clipboard, Transferable contents) {
		// TODO Auto-generated method stub
		
	} 
}