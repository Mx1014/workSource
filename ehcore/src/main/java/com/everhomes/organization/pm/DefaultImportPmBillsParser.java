package com.everhomes.organization.pm;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;

public class DefaultImportPmBillsParser implements ImportPmBillsBaseParser{

	@Override
	public List<CommunityPmBill> parse(MultipartFile[] files) {
		ArrayList resultList = new ArrayList();
		try {
			resultList = PropMrgOwnerHandler.processorExcel(files[0].getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<CommunityPmBill> billList = new ArrayList<CommunityPmBill>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		if(resultList != null && !resultList.isEmpty()){
			int rowCount = resultList.size();
			int startRow = 1;

			for (int rowIndex = startRow; rowIndex < rowCount ; rowIndex++) {
				RowResult rowResult = (RowResult)resultList.get(rowIndex);
				if(rowResult.getB() == null)
					continue;

				//生成账单总信息
				CommunityPmBill bill = new CommunityPmBill();
				bill.setId(null);

				try {
					bill.setStartDate(new Date(format.parse(rowResult.getA()).getTime()));
					bill.setEndDate(new Date(format.parse(rowResult.getB()).getTime()));
					bill.setPayDate(new Date(format.parse(rowResult.getC()).getTime()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				bill.setAddress(rowResult.getD());
				bill.setDueAmount(rowResult.getE() == null ? null:new BigDecimal(rowResult.getE()));
				bill.setOweAmount(rowResult.getF() == null ? null:new BigDecimal(rowResult.getF()));
				bill.setDescription(rowResult.getG());

				billList.add(bill);
			}
		}

		return billList;
	}

}
