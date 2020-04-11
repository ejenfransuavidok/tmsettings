package application.initgui;

import java.util.ArrayList;
import java.util.List;

public class Row {
	private String a;
	private String b;
	private String c;
	private String d;
	private String e;
	private String f;
	private String g;
	private String h;
	private String i;
	private String j;
	private String k;
	private String l;
	private String m;
	private String n;
	private String o;
	private String p;
	private String q;
	private String r;
	private String s;
	private String t;
	private String u;
	private String v;
	private String w;
	private String x;
	private String y;
	private String z;
	
	private List<String> letters = new ArrayList<>();
	
	public Row(String a, String b, String c, String d, String e, String f, String g, String h, String i, String j,
			String k, String l, String m, String n, String o, String p, String q, String r, String s, String t,
			String u, String v, String w, String x, String y, String z) {
		super();
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.e = e;
		this.f = f;
		this.g = g;
		this.h = h;
		this.i = i;
		this.j = j;
		this.k = k;
		this.l = l;
		this.m = m;
		this.n = n;
		this.o = o;
		this.p = p;
		this.q = q;
		this.r = r;
		this.s = s;
		this.t = t;
		this.u = u;
		this.v = v;
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
		letters.add("a");
		letters.add("b");
		letters.add("c");
		letters.add("d");
		letters.add("e");
		letters.add("f");
		letters.add("g");
		letters.add("h");
		letters.add("i");
		letters.add("j");
		letters.add("k");
		letters.add("l");
		letters.add("m");
		letters.add("n");
		letters.add("o");
		letters.add("p");
		letters.add("q");
		letters.add("r");
		letters.add("s");
		letters.add("t");
		letters.add("u");
		letters.add("v");
		letters.add("w");
		letters.add("x");
		letters.add("y");
		letters.add("z");
	}
	
	public void setByName(String name, String value) {
		switch (name.toLowerCase()) {
		case "a":
			setA(value);
			break;
		case "b":
			setB(value);
			break;
		case "c":
			setC(value);
			break;
		case "d":
			setD(value);
			break;
		case "e":
			setE(value);
			break;
		case "f":
			setF(value);
			break;
		case "g":
			setG(value);
			break;
		case "h":
			setH(value);
			break;
		case "i":
			setI(value);
			break;
		case "j":
			setJ(value);
			break;
		case "k":
			setK(value);
			break;
		case "l":
			setL(value);
			break;
		case "m":
			setM(value);
			break;
		case "n":
			setN(value);
			break;
		case "o":
			setO(value);
			break;
		case "p":
			setP(value);
			break;
		case "q":
			setQ(value);
			break;
		case "r":
			setR(value);
			break;
		case "s":
			setS(value);
			break;
		case "t":
			setT(value);
			break;
		case "u":
			setU(value);
			break;
		case "v":
			setV(value);
			break;
		case "w":
			setW(value);
			break;
		case "x":
			setX(value);
			break;
		case "y":
			setY(value);
			break;
		case "z":
			setZ(value);
			break;
		}
	}
	
	public String getByIndex(int index) {
		if (letters.size() > index) {
			return getByName(letters.get(index));
		}
		return null;
	}
	
	public String getByName(String name) {
		switch (name.toLowerCase()) {
		case "a":
			return getA();
		case "b":
			return getB();
		case "c":
			return getC();
		case "d":
			return getD();
		case "e":
			return getE();
		case "f":
			return getF();
		case "g":
			return getG();
		case "h":
			return getH();
		case "i":
			return getI();
		case "j":
			return getJ();
		case "k":
			return getK();
		case "l":
			return getL();
		case "m":
			return getM();
		case "n":
			return getN();
		case "o":
			return getO();
		case "p":
			return getP();
		case "q":
			return getQ();
		case "r":
			return getR();
		case "s":
			return getS();
		case "t":
			return getT();
		case "u":
			return getU();
		case "v":
			return getV();
		case "w":
			return getW();
		case "x":
			return getX();
		case "y":
			return getY();
		case "z":
			return getZ();
		}
		return "";
	}
	
	public String getA() {
		return a;
	}
	public void setA(String a) {
		this.a = a;
	}
	public String getB() {
		return b;
	}
	public void setB(String b) {
		this.b = b;
	}
	public String getC() {
		return c;
	}
	public void setC(String c) {
		this.c = c;
	}
	public String getD() {
		return d;
	}
	public void setD(String d) {
		this.d = d;
	}
	public String getE() {
		return e;
	}
	public void setE(String e) {
		this.e = e;
	}
	public String getF() {
		return f;
	}
	public void setF(String f) {
		this.f = f;
	}
	public String getG() {
		return g;
	}
	public void setG(String g) {
		this.g = g;
	}
	public String getH() {
		return h;
	}
	public void setH(String h) {
		this.h = h;
	}
	public String getI() {
		return i;
	}
	public void setI(String i) {
		this.i = i;
	}
	public String getJ() {
		return j;
	}
	public void setJ(String j) {
		this.j = j;
	}
	public String getK() {
		return k;
	}
	public void setK(String k) {
		this.k = k;
	}
	public String getL() {
		return l;
	}
	public void setL(String l) {
		this.l = l;
	}
	public String getM() {
		return m;
	}
	public void setM(String m) {
		this.m = m;
	}
	public String getN() {
		return n;
	}
	public void setN(String n) {
		this.n = n;
	}
	public String getO() {
		return o;
	}
	public void setO(String o) {
		this.o = o;
	}
	public String getP() {
		return p;
	}
	public void setP(String p) {
		this.p = p;
	}
	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
	}
	public String getR() {
		return r;
	}
	public void setR(String r) {
		this.r = r;
	}
	public String getS() {
		return s;
	}
	public void setS(String s) {
		this.s = s;
	}
	public String getT() {
		return t;
	}
	public void setT(String t) {
		this.t = t;
	}
	public String getU() {
		return u;
	}
	public void setU(String u) {
		this.u = u;
	}
	public String getV() {
		return v;
	}
	public void setV(String v) {
		this.v = v;
	}
	public String getW() {
		return w;
	}
	public void setW(String w) {
		this.w = w;
	}
	public String getX() {
		return x;
	}
	public void setX(String x) {
		this.x = x;
	}
	public String getY() {
		return y;
	}
	public void setY(String y) {
		this.y = y;
	}
	public String getZ() {
		return z;
	}
	public void setZ(String z) {
		this.z = z;
	}
	public List<String> getLetters() {
		return letters;
	}
}
