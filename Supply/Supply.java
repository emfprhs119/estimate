package Supply;
// 공급자
public class Supply{
	private String num;	// 사업자 등록번호
	private String company;	// 상호
	private String name;	// 대표
	private String address;	// 주소
	private String work;	// 업태
	private String work2;	// 종목
	private String tel;	// 전화
	private String fax;	// 팩스
	
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
