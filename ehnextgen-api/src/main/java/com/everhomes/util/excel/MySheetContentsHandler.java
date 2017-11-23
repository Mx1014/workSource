package com.everhomes.util.excel;

import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.usermodel.XSSFComment;

import java.util.ArrayList;

/**
 * @author jelly
 * 需要针对不同的excel模板写具体的处理类
 */
public class MySheetContentsHandler implements XSSFSheetXMLHandler.SheetContentsHandler {

	private ArrayList resultList=new ArrayList();
	private RowResult rowResult=null;

	/**
	 * 第1行不处理
	 */
	@Override
	public void startRow(int i) {
		if(i>=0)
		{
			rowResult=new RowResult();
			resultList.add(rowResult);
		}
	}



	@Override
	public void headerFooter(String s, boolean flag, String s1) {
	}

	public ArrayList getResultList()
	{
		return resultList;
	}



	@Override
	public void cell(String s, String s1, XSSFComment arg2) {
		if(rowResult!=null)
		{
			if(s.startsWith("A"))
			{
				rowResult.setA(s1);
			}
			else if(s.startsWith("B"))
			{
				rowResult.setB(s1);
			}
			else if(s.startsWith("C"))
			{
				rowResult.setC(s1);
			}
			else if(s.startsWith("D"))
			{
				rowResult.setD(s1);
			}
			else if(s.startsWith("E"))
			{
				rowResult.setE(s1);
			}
			else if(s.startsWith("F"))
			{
				rowResult.setF(s1);
			}
			else if(s.startsWith("G"))
			{
				rowResult.setG(s1);
			}
			else if(s.startsWith("H"))
			{
				rowResult.setH(s1);
			}
			else if(s.startsWith("I"))
			{
				rowResult.setI(s1);
			}
			else if(s.startsWith("J"))
			{
				rowResult.setJ(s1);
			}
			else if(s.startsWith("K"))
			{
				rowResult.setK(s1);
			}
			else if(s.startsWith("L"))
			{
				rowResult.setL(s1);
			}
			else if(s.startsWith("M"))
			{
				rowResult.setM(s1);
			}
			else if(s.startsWith("N"))
			{
				rowResult.setN(s1);
			}
			else if(s.startsWith("O"))
			{
				rowResult.setO(s1);
			}
			else if(s.startsWith("P"))
			{
				rowResult.setP(s1);
			}
			else if(s.startsWith("Q"))
			{
				rowResult.setQ(s1);
			}
			else if(s.startsWith("R"))
			{
				rowResult.setR(s1);
			}
			else if(s.startsWith("S"))
			{
				rowResult.setS(s1);
			}
			else if(s.startsWith("T"))
			{
				rowResult.setT(s1);
			}
			else if(s.startsWith("U"))
			{
				rowResult.setU(s1);
			}
			else if(s.startsWith("V"))
			{
				rowResult.setV(s1);
			}
			else if(s.startsWith("W"))
			{
				rowResult.setW(s1);
			}
			else if(s.startsWith("X"))
			{
				rowResult.setX(s1);
			}
			else if(s.startsWith("Y"))
			{
				rowResult.setY(s1);
			}
			else if(s.startsWith("Z"))
			{
				rowResult.setZ(s1);
			}
			rowResult.getCells().put(s.replace(String.valueOf(resultList.size()), ""), s1);
		}
	}



	@Override
	public void endRow(int arg0) {


	}


}
