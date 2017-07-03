import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JOptionPane;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfSave {
	static Font formFont;
	static Font preFont;
	static Font font;
	static Font supfont;
	static Font font1;
	static Font txtFont,txtFont1;
	static BaseColor rgb;
	PdfSave() {
		rgb=new BaseColor(255,255,150);
		//rgb=rgb.brighter();
		try {
			BaseFont objFont = BaseFont.createFont("font/batang.ttc,0", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			formFont = new Font(objFont, 50);
			formFont.setStyle(Font.BOLD);
			font = new Font(objFont, 12);
			font.setStyle(Font.BOLD);
			supfont = new Font(objFont, 12);
			supfont.setStyle(Font.BOLD);
			preFont = new Font(objFont, 20);
			preFont.setStyle(Font.BOLD);
			font1 = new Font(objFont, 10);
			font1.setStyle(Font.BOLD);
			txtFont = new Font(objFont, 10);
			txtFont1 = new Font(objFont, 8);
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public void exportPDF(String fName,Estimate est) {
		Document document = new Document();
		document.setMargins(45, 45, 45, 45);
		String path="\\PDF";
		int curr = 0;
		try {
			File a = new File(path);
			if (a.exists() == false) {
				a.mkdirs();
			}
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path+"\\"+fName+".pdf"));
			document.open();
			Paragraph 견적서 = new Paragraph("견적서", formFont);
			견적서.setAlignment(Element.ALIGN_CENTER);
			견적서.setSpacingAfter(27f);
			document.add(견적서);
			
			String list[][];//=productView.strList;
			int sum,num;
			int all = 0;
			/*
			for (int k = 0; k < list.length; k++) {
				sum=0;
				num=0;
				if (Main.toStrFormat((String) list[k][2]) == "non" || list[k][2] == null ){
					if (Main.toStrFormat((String) list[k][3]) != "non" && list[k][3] != null ){
						sum=Integer.parseInt(Main.toStrFormat(list[k][3]));
						num = Integer.parseInt(Main.toStrFormat(list[k][4]));
					}
				}
				if (Main.toStrFormat((String) list[k][3]) == "non" || list[k][3] == null ){
					if (Main.toStrFormat((String) list[k][2]) != "non" && list[k][2] != null ){
						sum=Integer.parseInt(Main.toStrFormat(list[k][2]));
					num = Integer.parseInt(Main.toStrFormat(list[k][4]));
					}
				}
				if (Main.toStrFormat((String) list[k][2]) != "non"
					&& Main.toStrFormat((String) list[k][3]) != "non") {
					if (list[k][2] != null && list[k][3] != null) {
						sum = Integer.parseInt(Main.toStrFormat(list[k][2]))
								+ Integer.parseInt(Main.toStrFormat(list[k][3]));
						num = Integer.parseInt(Main.toStrFormat(list[k][4]));
					}
				}
				all+=sum*num;
			}
			*/
			String allSum=Main.toNumFormat(all);
			writeInit(document, est, allSum);
			document.add(headTable(est.tableWidth));
			document.add(createTable(est.tableWidth,est.productList, ProductView.FrontRow, curr, 1 == ProductView.maxPage,allSum));
			Paragraph para=new Paragraph("1");
			para.setAlignment(Element.ALIGN_CENTER);
			
			
			document.add(para);
			// 중간 빈칸
			for (int i = 1; i < ProductView.maxPage; i++) {
				if (i == 1)
					curr += ProductView.FrontRow;
				else
					curr += ProductView.BackRow;
				document.add(new Paragraph(" "));
				document.add(createTable(est.tableWidth,est.productList, ProductView.BackRow, curr, i == ProductView.maxPage - 1,allSum));
				para=new Paragraph(String.valueOf(i+1));
				para.setAlignment(Element.ALIGN_CENTER);
				document.add(para);
			}
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "pdf 파일을 닫고 다시 시도하세요.");
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "pdf 파일을 닫고 다시 시도하세요.");
			e.printStackTrace();
		}
		document.close();
		
	}

	private void writeInit(Document document,Estimate est, String allSum) {
		try {
			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(100f);
			table.setSpacingAfter(8);
			PdfPCell deCell = new PdfPCell(writeDemand(est.demand,allSum));
			deCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			deCell.setBorder(0);
			PdfPCell supCell = new PdfPCell(writeSupply(est.supply));
			supCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			supCell.setBorder(0);
			table.addCell(deCell);
			table.addCell(supCell);
			document.add(table);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// DemandF dmd=demand.getDemand();

	}

	private PdfPTable writeDemand(Demand demand, String allSum) {
		PdfPTable table = new PdfPTable(4);
		PdfPCell title[] = new PdfPCell[4];
		PdfPCell titleSub[] = new PdfPCell[4];
		table.setHorizontalAlignment(Element.ALIGN_LEFT);
		try {
			table.setWidths(new int[] { 6, 2, 13, 1 });
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		title[0] = new PdfPCell(new Paragraph(" 견 적 일 :", font));
		title[1] = new PdfPCell(new Paragraph(" 상     호 :", font));
		title[2] = new PdfPCell(new Paragraph(" 전화번호:", font));
		title[3] = new PdfPCell(new Paragraph(" 담 당 자 :", font));
		titleSub[0] = new PdfPCell(new Paragraph(demand.date, font1));
		titleSub[1] = new PdfPCell(new Paragraph(demand.name, font1));
		titleSub[2] = new PdfPCell(new Paragraph(demand.tel, font1));
		titleSub[3] = new PdfPCell(new Paragraph(demand.who, font1));
		PdfPCell tmp = new PdfPCell(new Paragraph(" "));
		tmp.setBorder(0);
		for (int i = 0; i < 4; i++) {
			title[i].setFixedHeight(21f);
			title[i].setHorizontalAlignment(Element.ALIGN_LEFT);
			title[i].setVerticalAlignment(Element.ALIGN_MIDDLE);
			title[i].setBorder(0);
			titleSub[i].setHorizontalAlignment(Element.ALIGN_LEFT);
			titleSub[i].setVerticalAlignment(Element.ALIGN_MIDDLE);
			titleSub[i].setColspan(2);
			titleSub[i].setBorder(0);
			table.addCell(title[i]);
			table.addCell(titleSub[i]);
			// table.addCell(tmp);
			table.addCell(tmp);
		}
		PdfPCell sumStr = new PdfPCell(new Paragraph("합계금액", preFont));
		PdfPCell sum = new PdfPCell(new Paragraph(allSum+"원", preFont));
		sumStr.setVerticalAlignment(Element.ALIGN_CENTER);
		sumStr.setColspan(2);
		sumStr.setBorder(0);
		sum.setVerticalAlignment(Element.ALIGN_CENTER);
		sum.setHorizontalAlignment(Element.ALIGN_RIGHT);
		sum.setBackgroundColor(rgb);
		tmp.setFixedHeight(4f);
		table.addCell(tmp);
		table.addCell(tmp);
		table.addCell(tmp);
		table.addCell(tmp);
		table.addCell(sumStr);
		table.addCell(sum);
		table.addCell(tmp);
		return table;
	}

	private PdfPTable writeSupply(Supply supply) {
		PdfPTable table = new PdfPTable(5);
		try {
			table.setWidths(new int[] { 8, 12, 30, 12, 30 });
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		table.setHorizontalAlignment(Element.ALIGN_RIGHT);

		table.setWidthPercentage(50f);
		PdfPCell title[] = new PdfPCell[9];

		title[0] = new PdfPCell(new Paragraph("공급자", preFont));
		title[1] = new PdfPCell(new Paragraph("등록번호", supfont));
		title[2] = new PdfPCell(new Paragraph("상호", font));
		title[3] = new PdfPCell(new Paragraph("성명", font));
		title[4] = new PdfPCell(new Paragraph("주소", font));
		title[5] = new PdfPCell(new Paragraph("업태", font));
		title[6] = new PdfPCell(new Paragraph("종목", font));
		title[7] = new PdfPCell(new Paragraph("전화", font));
		title[8] = new PdfPCell(new Paragraph("팩스", font));
		// 컬럼 바탕색

		for (PdfPCell tit : title) {
			tit.setFixedHeight(23f);
			tit.setHorizontalAlignment(Element.ALIGN_CENTER);
			tit.setVerticalAlignment(Element.ALIGN_MIDDLE);
		}
		title[0].setRowspan(5);
		table.addCell(title[0]);

		title[1].setFixedHeight(28f);
		table.addCell(title[1]);
		PdfPCell 등록번호 = new PdfPCell(new Paragraph(supply.num, preFont));
		등록번호.setVerticalAlignment(Element.ALIGN_MIDDLE);
		등록번호.setHorizontalAlignment(Element.ALIGN_CENTER);
		등록번호.setColspan(3);
		table.addCell(등록번호);

		table.addCell(title[2]);
		PdfPCell 상호 = new PdfPCell(new Paragraph(supply.company, font1));
		상호.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(상호);

		table.addCell(title[3]);
		PdfPCell 성명 = new PdfPCell(new Paragraph(supply.name, font1));
		성명.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(성명);

		table.addCell(title[4]);
		PdfPCell 주소 = new PdfPCell(new Paragraph(supply.address, font1));
		주소.setVerticalAlignment(Element.ALIGN_MIDDLE);
		주소.setColspan(3);
		table.addCell(주소);

		table.addCell(title[5]);
		PdfPCell 업태 = new PdfPCell(new Paragraph(supply.work, font1));
		업태.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(업태);

		table.addCell(title[6]);
		PdfPCell 종목 = new PdfPCell(new Paragraph(supply.work2, font1));
		종목.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(종목);

		table.addCell(title[7]);
		PdfPCell 전화 = new PdfPCell(new Paragraph(supply.tel, font1));
		전화.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(전화);

		table.addCell(title[8]);
		PdfPCell 팩스 = new PdfPCell(new Paragraph(supply.fax, font1));
		팩스.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(팩스);
		return table;
	}

	public static PdfPTable headTable(int tableWidth[]) {
		PdfPTable tableHead = new PdfPTable(8);
		tableHead.setWidthPercentage(100f);
		try {
			tableHead.setWidths(tableWidth);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		PdfPCell titleCell[] = new PdfPCell[8];
		titleCell[0] = new PdfPCell(new Paragraph("품목", font));
		titleCell[1] = new PdfPCell(new Paragraph("규격", font));
		titleCell[2] = new PdfPCell(new Paragraph("자재비", font));
		titleCell[3] = new PdfPCell(new Paragraph("가공비", font));
		titleCell[4] = new PdfPCell(new Paragraph("수량", font));
		titleCell[5] = new PdfPCell(new Paragraph("단가", font));
		titleCell[6] = new PdfPCell(new Paragraph("공급가액", font));
		titleCell[7] = new PdfPCell(new Paragraph("비고", font));
		// 컬럼 바탕색
		for (PdfPCell cell : titleCell) {
			cell.setFixedHeight(24f);
			cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			tableHead.addCell(cell);
		}
		return tableHead;
	}

	public static PdfPTable createTable(int tableWidth[],ProductList productList, int row, int curr, boolean end,String allSum) {
		PdfPTable table = new PdfPTable(8);
		int sum,num;
		String strSum, strSumNum;
		float oneRate;
		table.setWidthPercentage(100f);
		try {
			table.setWidths(tableWidth);
		} catch (DocumentException e) {
			JOptionPane.showMessageDialog(null, "PDF 저장 오류");
		}
		/*
		oneRate=(float)productView.frontTable.getColumn("품목").getPreferredWidth()/(
		productView.frontTable.getColumn("규격").getPreferredWidth()+
		productView.frontTable.getColumn("자재비").getPreferredWidth()+
		productView.frontTable.getColumn("가공비").getPreferredWidth()+
		productView.frontTable.getColumn("수량").getPreferredWidth()+
		productView.frontTable.getColumn("단가").getPreferredWidth()+
		productView.frontTable.getColumn("공급가액").getPreferredWidth()+
		productView.frontTable.getColumn("비고").getPreferredWidth());
		*/
		PdfPCell tit=null;
		for (int i = 0; i < row; i++) {
			sum=-1;
			num=-1;
			strSum = "";
			strSumNum = "";
			/*
			if (Main.toStrFormat((String) list[curr + i][2]) == "non" || list[curr + i][2] == null ){
				if (Main.toStrFormat((String) list[curr + i][3]) != "non" && list[curr + i][3] != null ){
					sum=Integer.parseInt(Main.toStrFormat(list[curr + i][3]));
					num = Integer.parseInt(Main.toStrFormat(list[curr + i][4]));
				}
			}
			if (Main.toStrFormat((String) list[curr + i][3]) == "non" || list[curr + i][3] == null ){
				if (Main.toStrFormat((String) list[curr + i][2]) != "non" && list[curr + i][2] != null ){
					sum=Integer.parseInt(Main.toStrFormat(list[curr + i][2]));
				num = Integer.parseInt(Main.toStrFormat(list[curr + i][4]));
				}
			}
			if (Main.toStrFormat((String) list[curr + i][2]) != "non"
				&& Main.toStrFormat((String) list[curr + i][3]) != "non") {
				if (list[curr + i][2] != null && list[curr + i][3] != null) {
					sum = Integer.parseInt(Main.toStrFormat(list[curr + i][2]))
							+ Integer.parseInt(Main.toStrFormat(list[curr + i][3]));
					num = Integer.parseInt(Main.toStrFormat(list[curr + i][4]));
				}
			}
			*/
			strSum = Main.toNumFormat(sum);
			strSumNum = Main.toNumFormat(sum * num);
			
			
			for (int j = 0; j < 8; j++) {
				switch(j){
				case 5:{
						if (sum==-1 )
							tit = new PdfPCell(new Paragraph("", txtFont));
						else
							tit = new PdfPCell(new Paragraph(strSum, txtFont));
					}break;
				case 6:
						if (sum==-1 || num==-1){
							tit = new PdfPCell(new Paragraph("", txtFont));
						}
						else
							tit = new PdfPCell(new Paragraph(strSumNum, txtFont));
						break;
				case 7:
					
					//tit = new PdfPCell(new Paragraph(list[curr + i][5], txtFont));
					break;
					/*
					default :
						if (list[curr + i][j]!=null && list[curr + i][j].length()>(oneRate/0.427)*14){
							
							System.out.println((oneRate/0.427)*14);
							tit = new PdfPCell(new Paragraph(list[curr + i][j], txtFont1));
						}
						else{
							tit = new PdfPCell(new Paragraph(list[curr + i][j], txtFont));
						}
						break;
						*/
						
				}
				tit.setFixedHeight(20f);
				if (j == 6)
					tit.setBackgroundColor(rgb);
				if (j == 0)
					tit.setHorizontalAlignment(Element.ALIGN_LEFT);
				else if (j==1||j==4)
					tit.setHorizontalAlignment(Element.ALIGN_CENTER);
				else
					tit.setHorizontalAlignment(Element.ALIGN_RIGHT);
				tit.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table.addCell(tit);
			}
		}
		if (end) 
			tit = new PdfPCell(new Paragraph("합계", font));
		else
			tit = new PdfPCell(new Paragraph("", txtFont));
		tit.setColspan(2);
		tit.setFixedHeight(30f);
		tit.setHorizontalAlignment(Element.ALIGN_CENTER);
		tit.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(tit);
		
		tit = new PdfPCell(new Paragraph("", txtFont));
		tit.setColspan(4);
		tit.setFixedHeight(30f);
		table.addCell(tit);
		

		if (end)
			tit = new PdfPCell(new Paragraph(allSum, txtFont));
			else
				tit = new PdfPCell(new Paragraph("", txtFont));
		tit.setFixedHeight(30f);
		tit.setBackgroundColor(rgb);
		tit.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tit.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(tit);
		
		tit = new PdfPCell(new Paragraph("", txtFont));
		tit.setFixedHeight(30f);
		table.addCell(tit);
		
		return table;
	}

}
