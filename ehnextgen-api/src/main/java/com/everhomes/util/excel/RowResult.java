package com.everhomes.util.excel;

/**
 * 针对每个excel模板中的一行自定义的对象
 * @author jelly
 *
 */
public class RowResult {
	private String A;
	private String B;
	private String C;
	private String D;
	private String E;
	private String F;
	private String G;
	private String H;
	private String I;
	private String J;
	private String K;
	private String L;
	private String M;
	private String N;
	private String O;
	private String P;
	public String getA() {
		return A;
	}
	public void setA(String a) {
		A = a;
	}
	public String getB() {
		return B;
	}
	public void setB(String b) {
		B = b;
	}
	public String getC() {
		return C;
	}
	public void setC(String c) {
		C = c;
	}
	public String getD() {
		return D;
	}
	public void setD(String d) {
		D = d;
	}
	
	public String getE()
	{
		return E;
	}
	public void setE(String e)
	{
		E = e;
	}
	public String getF()
	{
		return F;
	}
	public void setF(String f)
	{
		F = f;
	}
	public String getG()
	{
		return G;
	}
	public void setG(String g)
	{
		G = g;
	}
	public String getH()
	{
		return H;
	}
	public void setH(String h)
	{
		H = h;
	}
	public String getI()
	{
		return I;
	}
	public void setI(String i)
	{
		I = i;
	}
	public String getJ()
	{
		return J;
	}
	public void setJ(String j)
	{
		J = j;
	}
	public String getK()
	{
		return K;
	}
	public void setK(String k)
	{
		K = k;
	}
	public String getL()
	{
		return L;
	}
	public void setL(String l)
	{
		L = l;
	}
	public String getM()
	{
		return M;
	}
	public void setM(String m)
	{
		M = m;
	}
	public String getN()
	{
		return N;
	}
	public void setN(String n)
	{
		N = n;
	}
	public String getO()
	{
		return O;
	}
	public void setO(String o)
	{
		O = o;
	}
	public String getP()
	{
		return P;
	}
	public void setP(String p)
	{
		P = p;
	}
	@Override
	public String toString() {
		
		return "RowResult [A=" + A + ", B=" + B + ", C=" + C + ", D=" + D + ", E="+ E + ", F=" + F + ", G=" + G + ", H=" + H + 
				", I=" +I + ", J=" + J + ", K=" + K + ", L="+ L + ", M=" + M + ", N=" + N + ", O=" + O +", P=" + P +"]";
	}
	
	public static String trimString(String str){
		if(str != null && str.length() > 0)
		{
			return str.trim();
		}
		else
		{
			return str;
		}
	}
	
}
