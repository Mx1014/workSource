package com.everhomes.organization.pm.handler;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.organization.pm.CommunityPmBill;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.excel.RowResult;

@Component(ImportPmBillsBaseHandler.IMPORT_PM_BILLS_HANDLER_PREFIX+ImportPmBillsBaseHandler.HANDLER_1)
public class ImportPmBillsHandler1 extends ImportPmBillsBaseHandler{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ImportPmBillsHandler1.class);

	@SuppressWarnings("rawtypes")
	@Override
	public List verifyFiles(MultipartFile[] files) {
		List list = super.verifyFiles(files);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		int startRow = 1;
		int rowCount = list.size();
		Set<String> addressSet = new HashSet<String>();
		
		for (int rowIndex = startRow; rowIndex < rowCount ; rowIndex++) {
			RowResult r = (RowResult)list.get(rowIndex);
			//verify A,B,C,D,E column not be null
			if(r.getA() == null || r.getA().isEmpty() || r.getB() == null || r.getB().isEmpty() ||
					r.getC() == null || r.getC().isEmpty() || r.getD() == null || r.getD().isEmpty() ||
					r.getE() == null || r.getE().isEmpty()){
				LOGGER.error("Error happend in row " + (rowIndex+1) + ".Column A,B,C,D,E could not be null or empty.");
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
						"Error happend in row " + (rowIndex+1) + ".Column A,B,C,D,E could not be null or empty.");
			}
			//verify date in A,B,C column
			try {
				format.parse(r.getA());
				format.parse(r.getB());
				format.parse(r.getC());
			} catch (ParseException e) {
				LOGGER.error("Error happend in row "+ (rowIndex+1) + ".Column A,B,C must be date format : yyyy-MM-dd.");
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
						"Error happend in row "+ (rowIndex+1) + ".Column A,B,C must be date format : yyyy-MM-dd.");
			}
			//verify E,F column must be bigDecimal
			try{
				Double.valueOf(r.getE());
				if(r.getF() != null && !r.getF().isEmpty())
					Double.valueOf(r.getF());
			}
			catch(NumberFormatException e){
				LOGGER.error("Error happend in row "+ (rowIndex+1) + ".Column E,F must be float,otherwise F column can be empty.");
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
						"Error happend in row "+ (rowIndex+1) + ".Column E,F must be float,otherwise F column can be empty.");
			}
			//verify D column.address not repeat.
			addressSet.add(r.getD());
		}
		//verify D column.address not repeat.
		if(addressSet != null && addressSet.size() != list.size()-1){
			LOGGER.error("address could not repeat");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"address could not repeat");
		}
			
		return list;
	}

	@Override
	public List<CommunityPmBill> parse(List list) {
		List<CommunityPmBill> bills = new ArrayList<CommunityPmBill>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		if(list != null && !list.isEmpty()){
			int rowCount = list.size();
			int startRow = 1;

			for (int rowIndex = startRow; rowIndex < rowCount ; rowIndex++) {
				RowResult rowResult = (RowResult)list.get(rowIndex);
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
				bill.setDueAmount(rowResult.getE() == null ? BigDecimal.ZERO:new BigDecimal(rowResult.getE()));
				bill.setOweAmount(rowResult.getF() == null ? null:new BigDecimal(rowResult.getF()));
				bill.setDescription(rowResult.getG());
				bills.add(bill);
			}
		}

		return bills;
	}

}
