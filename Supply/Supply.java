package Supply;
// ������
public class Supply{
	private String num;	// ����� ��Ϲ�ȣ
	private String company;	// ��ȣ
	private String name;	// ��ǥ
	private String address;	// �ּ�
	private String work;	// ����
	private String work2;	// ����
	private String tel;	// ��ȭ
	private String fax;	// �ѽ�
	public static String supplyArr[] = {"����� ��Ϲ�ȣ","��ȣ","��ǥ","�ּ�","����","����","��ȭ","�ѽ�"}; 
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getWork() {
		return work;
	}
	public void setWork(String work) {
		this.work = work;
	}
	public String getWork2() {
		return work2;
	}
	public void setWork2(String work2) {
		this.work2 = work2;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
}
