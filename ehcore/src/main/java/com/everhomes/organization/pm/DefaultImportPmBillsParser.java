package com.everhomes.organization.pm;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;

@Component("DefaultImportPmBillsParser")
public class DefaultImportPmBillsParser implements ImportPmBillsBaseParser{
	private static final Logger LOGGER = LoggerFactory.getLogger(PropertyMgrServiceImpl.class);
	
	@Override
	public List verifyFiles(MultipartFile[] files) {
		ArrayList list = new ArrayList();
		try {
			list = PropMrgOwnerHandler.processorExcel(files[0].getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(list == null || list.isEmpty()){
			LOGGER.error("file is empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"file is empty");
		}
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		int startRow = 1;
		int rowCount = list.size();
		for (int rowIndex = startRow; rowIndex < rowCount ; rowIndex++) {
			RowResult r = (RowResult)list.get(rowIndex);
			
			
		}
		
		
		return list;
	}

	@Override
	public List<CommunityPmBill> parse(List list) {
		List<CommunityPmBill> billList = new ArrayList<CommunityPmBill>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		if(list != null && !list.isEmpty()){
			int rowCount = list.size();
			int startRow = 1;

			for (int rowIndex = startRow; rowIndex < rowCount ; rowIndex++) {
				RowResult rowResult = (RowResult)list.get(rowIndex);
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
					LOGGER.error("date string format is wrong.must be yyyy-MM-dd format.");
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
							"date string format is wrong.must be yyyy-MM-dd format.");
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

	@Override
	public void createPmBills(List<CommunityPmBill> bills) {
		// TODO Auto-generated method stub
		
	}

}
