package com.everhomes.techpark.rental;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.everhomes.rest.techpark.rental.AddRentalBillCommand;
import com.everhomes.rest.techpark.rental.AddRentalBillItemCommand;
import com.everhomes.rest.techpark.rental.AddRentalBillItemCommandResponse;
import com.everhomes.rest.techpark.rental.AddRentalSiteCommand;
import com.everhomes.rest.techpark.rental.AddRentalSiteItemsCommand;
import com.everhomes.rest.techpark.rental.AddRentalSiteRulesCommand;
import com.everhomes.rest.techpark.rental.AddRentalSiteSimpleRulesCommand;
import com.everhomes.rest.techpark.rental.BatchCompleteBillCommand;
import com.everhomes.rest.techpark.rental.BatchCompleteBillCommandResponse;
import com.everhomes.rest.techpark.rental.BatchIncompleteBillCommand;
import com.everhomes.rest.techpark.rental.CancelRentalBillCommand;
import com.everhomes.rest.techpark.rental.CompleteBillCommand;
import com.everhomes.rest.techpark.rental.ConfirmBillCommand;
import com.everhomes.rest.techpark.rental.DeleteRentalBillCommand;
import com.everhomes.rest.techpark.rental.DeleteRentalSiteCommand;
import com.everhomes.rest.techpark.rental.DeleteRentalSiteItemCommand;
import com.everhomes.rest.techpark.rental.DeleteRentalSiteRulesCommand;
import com.everhomes.rest.techpark.rental.DisableRentalSiteCommand;
import com.everhomes.rest.techpark.rental.EnableRentalSiteCommand;
import com.everhomes.rest.techpark.rental.FindRentalBillsCommand;
import com.everhomes.rest.techpark.rental.FindRentalBillsCommandResponse;
import com.everhomes.rest.techpark.rental.FindRentalSiteItemsCommand;
import com.everhomes.rest.techpark.rental.FindRentalSiteItemsCommandResponse;
import com.everhomes.rest.techpark.rental.FindRentalSiteRulesCommand;
import com.everhomes.rest.techpark.rental.FindRentalSiteRulesCommandResponse;
import com.everhomes.rest.techpark.rental.FindRentalSiteWeekStatusCommand;
import com.everhomes.rest.techpark.rental.FindRentalSiteWeekStatusCommandResponse;
import com.everhomes.rest.techpark.rental.FindRentalSitesCommand;
import com.everhomes.rest.techpark.rental.FindRentalSitesCommandResponse;
import com.everhomes.rest.techpark.rental.FindRentalSitesStatusCommand;
import com.everhomes.rest.techpark.rental.FindRentalSitesStatusCommandResponse;
import com.everhomes.rest.techpark.rental.GetRentalSiteTypeResponse;
import com.everhomes.rest.techpark.rental.GetRentalTypeRuleCommand;
import com.everhomes.rest.techpark.rental.GetRentalTypeRuleCommandResponse;
import com.everhomes.rest.techpark.rental.IncompleteBillCommand;
import com.everhomes.rest.techpark.rental.ListRentalBillCountCommand;
import com.everhomes.rest.techpark.rental.ListRentalBillCountCommandResponse;
import com.everhomes.rest.techpark.rental.ListRentalBillsCommand;
import com.everhomes.rest.techpark.rental.ListRentalBillsCommandResponse;
import com.everhomes.rest.techpark.rental.ListRentalSiteItemsCommand;
import com.everhomes.rest.techpark.rental.ListRentalSiteItemsCommandResponse;
import com.everhomes.rest.techpark.rental.OnlinePayCallbackCommand;
import com.everhomes.rest.techpark.rental.OnlinePayCallbackCommandResponse;
import com.everhomes.rest.techpark.rental.RentalBillDTO;
import com.everhomes.rest.techpark.rental.UpdateRentalRuleCommand;
import com.everhomes.rest.techpark.rental.UpdateRentalSiteCommand;
import com.everhomes.rest.techpark.rental.VerifyRentalBillCommandResponse;

public interface RentalService {

	void updateRentalRule(UpdateRentalRuleCommand cmd);

	Long addRentalSite(AddRentalSiteCommand cmd);

	void addRentalSiteItems(AddRentalSiteItemsCommand cmd);

	void addRentalSiteRules(AddRentalSiteRulesCommand cmd);

	FindRentalSitesStatusCommandResponse findRentalSiteDayStatus(
			FindRentalSitesStatusCommand cmd);

	GetRentalSiteTypeResponse findRentalSiteTypes();

	FindRentalSiteItemsCommandResponse findRentalSiteItems(
			FindRentalSiteItemsCommand cmd);

	FindRentalSitesCommandResponse findRentalSites(FindRentalSitesCommand cmd);

	FindRentalSiteRulesCommandResponse findRentalSiteRules(
			FindRentalSiteRulesCommand cmd);

	RentalBillDTO addRentalBill(AddRentalBillCommand cmd);

	FindRentalBillsCommandResponse findRentalBills(FindRentalBillsCommand cmd);

	GetRentalTypeRuleCommandResponse getRentalTypeRule(
			GetRentalTypeRuleCommand cmd);

	void addRentalSiteSimpleRules(AddRentalSiteSimpleRulesCommand cmd);
 
	void deleteRentalSiteRules(DeleteRentalSiteRulesCommand cmd);

	void cancelRentalBill(CancelRentalBillCommand cmd);

	void updateRentalSite(UpdateRentalSiteCommand cmd);

	void deleteRentalSite(DeleteRentalSiteCommand cmd);

	void deleteRentalSiteItem(DeleteRentalSiteItemCommand cmd);

	ListRentalSiteItemsCommandResponse listRentalSiteItems(
			ListRentalSiteItemsCommand cmd);

	AddRentalBillItemCommandResponse addRentalItemBill(AddRentalBillItemCommand cmd);

	ListRentalBillsCommandResponse listRentalBills(ListRentalBillsCommand cmd);

	void deleteRentalBill(DeleteRentalBillCommand cmd);
 
	OnlinePayCallbackCommandResponse onlinePayCallback(
			OnlinePayCallbackCommand cmd);

	FindRentalSiteWeekStatusCommandResponse findRentalSiteWeekStatus(
			FindRentalSiteWeekStatusCommand cmd);

	RentalBillDTO confirmBill(ConfirmBillCommand cmd);

	RentalBillDTO completeBill(CompleteBillCommand cmd);

	RentalBillDTO incompleteBill(IncompleteBillCommand cmd);

	void disableRentalSite(DisableRentalSiteCommand cmd);

	void enableRentalSite(EnableRentalSiteCommand cmd);
 

	BatchCompleteBillCommandResponse batchIncompleteBill(
			BatchIncompleteBillCommand cmd);

	BatchCompleteBillCommandResponse batchCompleteBill(
			BatchCompleteBillCommand cmd);

	ListRentalBillCountCommandResponse listRentalBillCount(
			ListRentalBillCountCommand cmd);

	VerifyRentalBillCommandResponse VerifyRentalBill(AddRentalBillCommand cmd);
	
	public HttpServletResponse exportRentalBills(@Valid ListRentalBillsCommand cmd,HttpServletResponse response);

}
