// @formatter:off
package com.everhomes.parking;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.everhomes.rest.flow.ListFlowBriefResponse;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.rest.parking.*;

public interface ParkingService {
	List<ParkingCardDTO> listParkingCards(ListParkingCardsCommand cmd);

    GetParkingCardsResponse getParkingCards(GetParkingCardsCommand cmd);

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
    
    HttpServletResponse exportParkingRechargeOrders(SearchParkingRechargeOrdersCommand cmd,
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
    
    void synchronizedData(ListParkingCardRequestsCommand cmd);

    ParkingCarLockInfoDTO getParkingCarLockInfo(GetParkingCarLockInfoCommand cmd);

    void lockParkingCar(LockParkingCarCommand cmd);

	GetParkingCarNumsResponse getParkingCarNums(GetParkingCarNumsCommand cmd);

    UpdateParkingOrderDTO updateParkingOrder(UpdateParkingOrderCommand cmd);
}
