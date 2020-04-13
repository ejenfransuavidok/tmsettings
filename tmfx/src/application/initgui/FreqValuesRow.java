package application.initgui;

public class FreqValuesRow {
	private String f1;
	private String f2;
	private String f3;
	private String f4;
	private String f5;
	private String f6;
	private String f7;
	private String f8;
	private String f9;
	private String f10;
	private String f11;
	private String f12;
	
	public FreqValuesRow() {
		
	}
	
	public FreqValuesRow(String f1, String f2, String f3, String f4, String f5, String f6, String f7, String f8,
			String f9, String f10, String f11, String f12) {
		this.f1 = f1;
		this.f2 = f2;
		this.f3 = f3;
		this.f4 = f4;
		this.f5 = f5;
		this.f6 = f6;
		this.f7 = f7;
		this.f8 = f8;
		this.f9 = f9;
		this.f10 = f10;
		this.f11 = f11;
		this.f12 = f12;
	}
	
	public String getByName(String name) {
		switch (name.toLowerCase()) {
		case "f1":
			return getF1();
		case "f2":
			return getF2();
		case "f3":
			return getF3();
		case "f4":
			return getF4();
		case "f5":
			return getF5();
		case "f6":
			return getF6();
		case "f7":
			return getF7();
		case "f8":
			return getF8();
		case "f9":
			return getF9();
		case "f10":
			return getF10();
		case "f11":
			return getF11();
		case "f12":
			return getF12();	
		}
		return "";
	}
	
	public void setByName(String name, String value) {
		switch (name.toLowerCase()) {
		case "f1":
			setF1(value);
			break;
		case "f2":
			setF2(value);
			break;
		case "f3":
			setF3(value);
			break;
		case "f4":
			setF4(value);
			break;
		case "f5":
			setF5(value);
			break;
		case "f6":
			setF6(value);
			break;
		case "f7":
			setF7(value);
			break;
		case "f8":
			setF8(value);
			break;
		case "f9":
			setF9(value);
			break;
		case "f10":
			setF10(value);
			break;
		case "f11":
			setF11(value);
			break;
		case "f12":
			setF12(value);
			break;
		}
	}
	
	public String getF1() {
		return f1;
	}
	public void setF1(String f1) {
		this.f1 = f1;
	}
	public String getF2() {
		return f2;
	}
	public void setF2(String f2) {
		this.f2 = f2;
	}
	public String getF3() {
		return f3;
	}
	public void setF3(String f3) {
		this.f3 = f3;
	}
	public String getF4() {
		return f4;
	}
	public void setF4(String f4) {
		this.f4 = f4;
	}
	public String getF5() {
		return f5;
	}
	public void setF5(String f5) {
		this.f5 = f5;
	}
	public String getF6() {
		return f6;
	}
	public void setF6(String f6) {
		this.f6 = f6;
	}
	public String getF7() {
		return f7;
	}
	public void setF7(String f7) {
		this.f7 = f7;
	}
	public String getF8() {
		return f8;
	}
	public void setF8(String f8) {
		this.f8 = f8;
	}
	public String getF9() {
		return f9;
	}
	public void setF9(String f9) {
		this.f9 = f9;
	}
	public String getF10() {
		return f10;
	}
	public void setF10(String f10) {
		this.f10 = f10;
	}
	public String getF11() {
		return f11;
	}
	public void setF11(String f11) {
		this.f11 = f11;
	}
	public String getF12() {
		return f12;
	}
	public void setF12(String f12) {
		this.f12 = f12;
	}
}
