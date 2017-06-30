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

	public void exportPDF(String fName,Supply supply, Demand demand, SupplyTable supplyTable) {

		Document document = new Document();
		document.setMargins(45, 45, 45, 45);
		int curr = 0;
		try {
			File a = new File(System.getProperty("user.home")+"\\Desktop\\PDF��������");
			if (a.exists() == false) {
				a.mkdirs();
			}
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(System.getProperty("user.home")+"\\Desktop\\PDF��������\\"+fName+".pdf"));
			document.open();
			Paragraph ������ = new Paragraph("������", formFont);
			������.setAlignment(Element.ALIGN_CENTER);
			������.setSpacingAfter(27f);
			document.add(������);
			
			String list[][]=supplyTable.strList;
			int sum,num;
			int all = 0;
			for (int k = 0; k < list.length; k++) {
				sum=0;
				num=0;
				if (SupplyTable.toStrFormat((String) list[k][2]) == "non" || list[k][2] == null ){
					if (SupplyTable.toStrFormat((String) list[k][3]) != "non" && list[k][3] != null ){
						sum=Integer.parseInt(SupplyTable.toStrFormat(list[k][3]));
						num = Integer.parseInt(SupplyTable.toStrFormat(list[k][4]));
					}
				}
				if (SupplyTable.toStrFormat((String) list[k][3]) == "non" || list[k][3] == null ){
					if (SupplyTable.toStrFormat((String) list[k][2]) != "non" && list[k][2] != null ){
						sum=Integer.parseInt(SupplyTable.toStrFormat(list[k][2]));
					num = Integer.parseInt(SupplyTable.toStrFormat(list[k][4]));
					}
				}
				if (SupplyTable.toStrFormat((String) list[k][2]) != "non"
					&& SupplyTable.toStrFormat((String) list[k][3]) != "non") {
					if (list[k][2] != null && list[k][3] != null) {
						sum = Integer.parseInt(SupplyTable.toStrFormat(list[k][2]))
								+ Integer.parseInt(SupplyTable.toStrFormat(list[k][3]));
						num = Integer.parseInt(SupplyTable.toStrFormat(list[k][4]));
					}
				}
				all+=sum*num;
			}
			String allSum=SupplyTable.toNumFormat(all);
			createSupDem(document, supply, demand, allSum);
			document.add(headTable(supplyTable));
			if (1 == SupplyTable.maxPage)
				document.add(createTable(supplyTable, SupplyTable.FrontRow, curr, true,allSum));
			else
				document.add(createTable(supplyTable, SupplyTable.FrontRow, curr, false,allSum));
			Paragraph para=new Paragraph("1");
			para.setAlignment(Element.ALIGN_CENTER);
			
			
			document.add(para);
			// �߰� ��ĭ
			for (int i = 1; i < SupplyTable.maxPage; i++) {
				if (i == 1)
					curr += SupplyTable.FrontRow;
				else
					curr += SupplyTable.BackRow;
				document.add(new Paragraph(" "));
				if (i == SupplyTable.maxPage - 1)
					document.add(createTable(supplyTable, SupplyTable.BackRow, curr, true,allSum));
				else
					document.add(createTable(supplyTable, SupplyTable.BackRow, curr, false,allSum));
				para=new Paragraph(String.valueOf(i+1));
				para.setAlignment(Element.ALIGN_CENTER);
				document.add(para);
			}
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "pdf ������ �ݰ� �ٽ� �õ��ϼ���.");
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "pdf ������ �ݰ� �ٽ� �õ��ϼ���.");
			e.printStackTrace();
		}
		document.close();
		
	}

	private void createSupDem(Document document, Supply supply, Demand demand, String allSum) {
		try {
			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(100f);
			table.setSpacingAfter(8);
			PdfPCell deCell = new PdfPCell(creDemand(demand.getDemand(),allSum));
			deCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			deCell.setBorder(0);
			PdfPCell supCell = new PdfPCell(creSupply(supply));
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

	private PdfPTable creDemand(DemandF demand, String allSum) {
		PdfPTable table = new PdfPTable(4);
		PdfPCell title[] = new PdfPCell[4];
		PdfPCell titleSub[] = new PdfPCell[4];
		table.setHorizontalAlignment(Element.ALIGN_LEFT);
		try {
			table.setWidths(new int[] { 6, 2, 13, 1 });
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		title[0] = new PdfPCell(new Paragraph(" �� �� �� :", font));
		title[1] = new PdfPCell(new Paragraph(" ��     ȣ :", font));
		title[2] = new PdfPCell(new Paragraph(" ��ȭ��ȣ:", font));
		title[3] = new PdfPCell(new Paragraph(" �� �� �� :", font));
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
		PdfPCell sumStr = new PdfPCell(new Paragraph("�հ�ݾ�", preFont));
		PdfPCell sum = new PdfPCell(new Paragraph(allSum+"��", preFont));
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

	private PdfPTable creSupply(Supply supply) {
		PdfPTable table = new PdfPTable(5);
		SupplyF sup = supply.getSupply();
		try {
			table.setWidths(new int[] { 8, 12, 30, 12, 30 });
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		table.setHorizontalAlignment(Element.ALIGN_RIGHT);

		table.setWidthPercentage(50f);
		PdfPCell title[] = new PdfPCell[9];

		title[0] = new PdfPCell(new Paragraph("������", preFont));
		title[1] = new PdfPCell(new Paragraph("��Ϲ�ȣ", supfont));
		title[2] = new PdfPCell(new Paragraph("��ȣ", font));
		title[3] = new PdfPCell(new Paragraph("����", font));
		title[4] = new PdfPCell(new Paragraph("�ּ�", font));
		title[5] = new PdfPCell(new Paragraph("����", font));
		title[6] = new PdfPCell(new Paragraph("����", font));
		title[7] = new PdfPCell(new Paragraph("��ȭ", font));
		title[8] = new PdfPCell(new Paragraph("�ѽ�", font));
		// �÷� ������

		for (PdfPCell tit : title) {
			tit.setFixedHeight(23f);
			tit.setHorizontalAlignment(Element.ALIGN_CENTER);
			tit.setVerticalAlignment(Element.ALIGN_MIDDLE);
		}
		title[0].setRowspan(5);
		table.addCell(title[0]);

		title[1].setFixedHeight(28f);
		table.addCell(title[1]);
		PdfPCell ��Ϲ�ȣ = new PdfPCell(new Paragraph(sup.num, preFont));
		��Ϲ�ȣ.setVerticalAlignment(Element.ALIGN_MIDDLE);
		��Ϲ�ȣ.setHorizontalAlignment(Element.ALIGN_CENTER);
		��Ϲ�ȣ.setColspan(3);
		table.addCell(��Ϲ�ȣ);

		table.addCell(title[2]);
		PdfPCell ��ȣ = new PdfPCell(new Paragraph(sup.company, font1));
		��ȣ.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(��ȣ);

		table.addCell(title[3]);
		PdfPCell ���� = new PdfPCell(new Paragraph(sup.name, font1));
		����.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(����);

		table.addCell(title[4]);
		PdfPCell �ּ� = new PdfPCell(new Paragraph(sup.address, font1));
		�ּ�.setVerticalAlignment(Element.ALIGN_MIDDLE);
		�ּ�.setColspan(3);
		table.addCell(�ּ�);

		table.addCell(title[5]);
		PdfPCell ���� = new PdfPCell(new Paragraph(sup.work, font1));
		����.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(����);

		table.addCell(title[6]);
		PdfPCell ���� = new PdfPCell(new Paragraph(sup.work2, font1));
		����.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(����);

		table.addCell(title[7]);
		PdfPCell ��ȭ = new PdfPCell(new Paragraph(sup.tel, font1));
		��ȭ.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(��ȭ);

		table.addCell(title[8]);
		PdfPCell �ѽ� = new PdfPCell(new Paragraph(sup.fax, font1));
		�ѽ�.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(�ѽ�);
		return table;
	}

	public static PdfPTable headTable(SupplyTable supplyTable) {
		PdfPTable tableHead = new PdfPTable(8);
		tableHead.setWidthPercentage(100f);
		try {
			tableHead.setWidths(new int[] {supplyTable.frontTable.getColumn("ǰ��").getPreferredWidth(),
					supplyTable.frontTable.getColumn("�԰�").getPreferredWidth(),
					supplyTable.frontTable.getColumn("�����").getPreferredWidth(),
					supplyTable.frontTable.getColumn("������").getPreferredWidth(),
					supplyTable.frontTable.getColumn("����").getPreferredWidth(),
					supplyTable.frontTable.getColumn("�ܰ�").getPreferredWidth(),
					supplyTable.frontTable.getColumn("���ް���").getPreferredWidth(),
					supplyTable.frontTable.getColumn("���").getPreferredWidth() });
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PdfPCell title[] = new PdfPCell[8];
		title[0] = new PdfPCell(new Paragraph("ǰ��", font));
		title[1] = new PdfPCell(new Paragraph("�԰�", font));
		title[2] = new PdfPCell(new Paragraph("�����", font));
		title[3] = new PdfPCell(new Paragraph("������", font));
		title[4] = new PdfPCell(new Paragraph("����", font));
		title[5] = new PdfPCell(new Paragraph("�ܰ�", font));
		title[6] = new PdfPCell(new Paragraph("���ް���", font));
		title[7] = new PdfPCell(new Paragraph("���", font));
		// �÷� ������
		for (PdfPCell tit : title) {
			tit.setFixedHeight(24f);
			tit.setBackgroundColor(BaseColor.LIGHT_GRAY);
			tit.setHorizontalAlignment(Element.ALIGN_CENTER);
			tit.setVerticalAlignment(Element.ALIGN_MIDDLE);
			tableHead.addCell(tit);
		}
		return tableHead;
	}

	public static PdfPTable createTable(SupplyTable supplyTable, int row, int curr, boolean end,String allSum) {
		PdfPTable table = new PdfPTable(8);
		String list[][] = supplyTable.strList;
		int sum,num;
		String strSum, strSumNum;
		float oneRate;
		table.setWidthPercentage(100f);
		try {
			table.setWidths(new int[] { supplyTable.frontTable.getColumn("ǰ��").getPreferredWidth(),
					supplyTable.frontTable.getColumn("�԰�").getPreferredWidth(),
					supplyTable.frontTable.getColumn("�����").getPreferredWidth(),
					supplyTable.frontTable.getColumn("������").getPreferredWidth(),
					supplyTable.frontTable.getColumn("����").getPreferredWidth(),
					supplyTable.frontTable.getColumn("�ܰ�").getPreferredWidth(),
					supplyTable.frontTable.getColumn("���ް���").getPreferredWidth(),
					supplyTable.frontTable.getColumn("���").getPreferredWidth() });
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		oneRate=(float)supplyTable.frontTable.getColumn("ǰ��").getPreferredWidth()/(
		supplyTable.frontTable.getColumn("�԰�").getPreferredWidth()+
		supplyTable.frontTable.getColumn("�����").getPreferredWidth()+
		supplyTable.frontTable.getColumn("������").getPreferredWidth()+
		supplyTable.frontTable.getColumn("����").getPreferredWidth()+
		supplyTable.frontTable.getColumn("�ܰ�").getPreferredWidth()+
		supplyTable.frontTable.getColumn("���ް���").getPreferredWidth()+
		supplyTable.frontTable.getColumn("���").getPreferredWidth());
		PdfPCell tit;
		for (int i = 0; i < row; i++) {
			sum=-1;
			num=-1;
			strSum = "";
			strSumNum = "";
			
			if (SupplyTable.toStrFormat((String) list[curr + i][2]) == "non" || list[curr + i][2] == null ){
				if (SupplyTable.toStrFormat((String) list[curr + i][3]) != "non" && list[curr + i][3] != null ){
					sum=Integer.parseInt(SupplyTable.toStrFormat(list[curr + i][3]));
					num = Integer.parseInt(SupplyTable.toStrFormat(list[curr + i][4]));
				}
			}
			if (SupplyTable.toStrFormat((String) list[curr + i][3]) == "non" || list[curr + i][3] == null ){
				if (SupplyTable.toStrFormat((String) list[curr + i][2]) != "non" && list[curr + i][2] != null ){
					sum=Integer.parseInt(SupplyTable.toStrFormat(list[curr + i][2]));
				num = Integer.parseInt(SupplyTable.toStrFormat(list[curr + i][4]));
				}
			}
			if (SupplyTable.toStrFormat((String) list[curr + i][2]) != "non"
				&& SupplyTable.toStrFormat((String) list[curr + i][3]) != "non") {
				if (list[curr + i][2] != null && list[curr + i][3] != null) {
					sum = Integer.parseInt(SupplyTable.toStrFormat(list[curr + i][2]))
							+ Integer.parseInt(SupplyTable.toStrFormat(list[curr + i][3]));
					num = Integer.parseInt(SupplyTable.toStrFormat(list[curr + i][4]));
				}
			}
			strSum = SupplyTable.toNumFormat(sum);
			strSumNum = SupplyTable.toNumFormat(sum * num);
			
			
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
					tit = new PdfPCell(new Paragraph(list[curr + i][5], txtFont));
					break;
					default :
						if (list[curr + i][j]!=null && list[curr + i][j].length()>(oneRate/0.427)*14){
							
							System.out.println((oneRate/0.427)*14);
							tit = new PdfPCell(new Paragraph(list[curr + i][j], txtFont1));
						}
						else{
							tit = new PdfPCell(new Paragraph(list[curr + i][j], txtFont));
						}
						break;
						
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
			tit = new PdfPCell(new Paragraph("�հ�", font));
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