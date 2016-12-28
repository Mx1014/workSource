// @formatter:off
package com.everhomes.parking;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.everhomes.rest.flow.ListFlowBriefResponse;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.rest.parking.CreateParkingRechargeOrderCommand;
import com.everhomes.rest.parking.CreateParkingRechargeRateCommand;
import com.everhomes.rest.parking.CreateParkingTempOrderCommand;
import com.everhomes.rest.parking.DeleteParkingRechargeOrderCommand;
import com.everhomes.rest.parking.DeleteParkingRechargeRateCommand;
import com.everhomes.rest.parking.GetOpenCardInfoCommand;
import com.everhomes.rest.parking.GetParkingActivityCommand;
import com.everhomes.rest.parking.GetParkingTempFeeCommand;
import com.everhomes.rest.parking.GetRechargeResultCommand;
import com.everhomes.rest.parking.GetRequestParkingCardDetailCommand;
import com.everhomes.rest.parking.GetParkingRequestCardConfigCommand;
import com.everhomes.rest.parking.IssueParkingCardsCommand;
import com.everhomes.rest.parking.ListCardTypeCommand;
import com.everhomes.rest.parking.ListCardTypeResponse;
import com.everhomes.rest.parking.ListParkingCarSeriesCommand;
import com.everhomes.rest.parking.ListParkingCarSeriesResponse;
import com.everhomes.rest.parking.ListParkingCardRequestResponse;
import com.everhomes.rest.parking.ListParkingCardRequestsCommand;
import com.everhomes.rest.parking.ListParkingCardsCommand;
import com.everhomes.rest.parking.ListParkingLotsCommand;
import com.everhomes.rest.parking.ListParkingRechargeOrdersCommand;
import com.everhomes.rest.parking.ListParkingRechargeOrdersResponse;
import com.everhomes.rest.parking.ListParkingRechargeRatesCommand;
import com.everhomes.rest.parking.OpenCardInfoDTO;
import com.everhomes.rest.parking.ParkingActivityDTO;
import com.everhomes.rest.parking.ParkingCardDTO;
import com.everhomes.rest.parking.ParkingCardRequestDTO;
import com.everhomes.rest.parking.ParkingLotDTO;
import com.everhomes.rest.parking.ParkingRechargeRateDTO;
import com.everhomes.rest.parking.ParkingRequestCardAgreementDTO;
import com.everhomes.rest.parking.ParkingRequestCardConfigDTO;
import com.everhomes.rest.parking.ParkingTempFeeDTO;
import com.everhomes.rest.parking.RequestParkingCardCommand;
import com.everhomes.rest.parking.SearchParkingCardRequestsCommand;
import com.everhomes.rest.parking.SearchParkingRechargeOrdersCommand;
import com.everhomes.rest.parking.SetParkingActivityCommand;
import com.everhomes.rest.parking.SetParkingCardIssueFlagCommand;
import com.everhomes.rest.parking.SetParkingLotConfigCommand;
import com.everhomes.rest.parking.SetParkingRequestCardConfigCommand;
import com.everhomes.rest.parking.SurplusCardCountDTO;
import com.everhomes.rest.parking.GetParkingRequestCardAgreementCommand;

public interface ParkingService {
	List<ParkingCardDTO> listParkingCards(ListParkingCardsCommand cmd);

    List<ParkingLotDTO> listParkingLots(ListParkingLotsCommand cmd);
    
    List<ParkingRechargeRateDTO> listParkingRechargeRates(ListParkingRechargeRatesCommand cmd);
    
    ParkingRechargeRateDTO createParkingRechargeRate(CreateParkingRechargeRateCommand cmd);
    
    ParkingCardRequestDTO requestParkingCard(RequestParkingCardCommand cmd);
    
    ListParkingCardRequestResponse listParkingCardRequests(ListParkingCardRequestsCommand cmd);
    
    CommonOrderDTO createParkingRechargeOrder(CreateParkingRechargeOrderCommand cmd);
    
    ListParkingRechargeOrdersResponse listParkingRechargeOrders(ListParkingRechargeOrdersCommand cmd);
    
    boolean deleteParkingRechargeRate(DeleteParkingRechargeRateCommand cmd);
    
    ListParkingRechargeOrdersResponse searchParkingRechargeOrders(SearchParkingRechargeOrdersCommand cmd);
    
    ListParkingCardRequestResponse searchParkingCardRequests(SearchParkingCardRequestsCommand cmd);
    
    void setParkingLotConfig(SetParkingLotConfigCommand cmd);
    
    void notifyParkingRechargeOrderPayment(PayCallbackCommand cmd);
    
    ParkingActivityDTO setParkingActivity(SetParkingActivityCommand cmd);
    
    ParkingActivityDTO getParkingActivity(GetParkingActivityCommand cmd);
    
    HttpServletResponse exportParkingRechageOrders(SearchParkingRechargeOrdersCommand cmd,
    		HttpServletResponse response);
    
    void deleteParkingRechargeOrder(DeleteParkingRechargeOrderCommand cmd);
    
    ListCardTypeResponse listCardType(ListCardTypeCommand cmd);
    
    ParkingTempFeeDTO getParkingTempFee(GetParkingTempFeeCommand cmd);
    
    CommonOrderDTO createParkingTempOrder(CreateParkingTempOrderCommand cmd);
    
    ListParkingCarSeriesResponse listParkingCarSeries(ListParkingCarSeriesCommand cmd);
    
    ParkingRequestCardConfigDTO getParkingRequestCardConfig(HttpServletRequest request, GetParkingRequestCardConfigCommand cmd);
    
    void setParkingRequestCardConfig(SetParkingRequestCardConfigCommand cmd);
    
    ParkingCardRequestDTO getRequestParkingCardDetail(GetRequestParkingCardDetailCommand cmd);
    
    void setParkingCardIssueFlag(SetParkingCardIssueFlagCommand cmd);
    
    void issueParkingCards(IssueParkingCardsCommand cmd);
    
    OpenCardInfoDTO getOpenCardInfo(GetOpenCardInfoCommand cmd);
    
    SurplusCardCountDTO getSurplusCardCount(GetParkingRequestCardConfigCommand cmd);
    
    ParkingRequestCardAgreementDTO getParkingRequestCardAgreement(GetParkingRequestCardAgreementCommand cmd);
    
    ParkingCardDTO getRechargeResult(GetRechargeResultCommand cmd);
}
