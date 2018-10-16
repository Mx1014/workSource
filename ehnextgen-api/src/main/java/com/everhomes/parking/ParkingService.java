// @formatter:off
package com.everhomes.parking;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.order.ListBizPayeeAccountDTO;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.parking.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

public interface ParkingService {
    List<ParkingCardDTO> listParkingCards(ListParkingCardsCommand cmd);

    List<ParkingCardDTO> getParkingCards(ListParkingCardsCommand cmd);

    List<ParkingLotDTO> listParkingLots(ListParkingLotsCommand cmd);

    List<ParkingRechargeRateDTO> listParkingRechargeRates(ListParkingRechargeRatesCommand cmd);

    ParkingRechargeRateDTO createParkingRechargeRate(CreateParkingRechargeRateCommand cmd);

    ParkingCardRequestDTO requestParkingCard(RequestParkingCardCommand cmd);

    ListParkingCardRequestResponse listParkingCardRequests(ListParkingCardRequestsCommand cmd);

    CommonOrderDTO createParkingRechargeOrder(CreateParkingRechargeOrderCommand cmd);

    PreOrderDTO createParkingRechargeOrderV2(CreateParkingRechargeOrderCommand cmd);

    PreOrderDTO createParkingTempOrderV2(CreateParkingTempOrderCommand cmd);

    ListParkingRechargeOrdersResponse listParkingRechargeOrders(ListParkingRechargeOrdersCommand cmd);

    boolean deleteParkingRechargeRate(DeleteParkingRechargeRateCommand cmd);

    ListParkingRechargeOrdersResponse searchParkingRechargeOrders(SearchParkingRechargeOrdersCommand cmd);

    ListParkingCardRequestResponse searchParkingCardRequests(SearchParkingCardRequestsCommand cmd);

    void exportParkingCardRequests(SearchParkingCardRequestsCommand cmd, HttpServletResponse response);

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

    void issueParkingCards(IssueParkingCardsCommand cmd);

    OpenCardInfoDTO getOpenCardInfo(GetOpenCardInfoCommand cmd);

    SurplusCardCountDTO getSurplusCardCount(GetParkingRequestCardConfigCommand cmd);

    ParkingRequestCardAgreementDTO getParkingRequestCardAgreement(GetParkingRequestCardAgreementCommand cmd);

    ParkingCardDTO getRechargeResult(GetRechargeResultCommand cmd);

    void synchronizedData(ListParkingCardRequestsCommand cmd);

    ParkingCarLockInfoDTO getParkingCarLockInfo(GetParkingCarLockInfoCommand cmd);

    void lockParkingCar(LockParkingCarCommand cmd);

    GetParkingCarNumsResponse getParkingCarNums(GetParkingCarNumsCommand cmd);

    ParkingRechargeOrderDTO updateParkingOrder(UpdateParkingOrderCommand cmd);

    void refundParkingOrder(RefundParkingOrderCommand cmd);

    DeferredResult getRechargeOrderResult(GetRechargeResultCommand cmd);

    ParkingFreeSpaceNumDTO getFreeSpaceNum(GetFreeSpaceNumCommand cmd);

    ParkingCarLocationDTO getCarLocation(GetCarLocationCommand cmd);

    List<ParkingCardRequestTypeDTO> listParkingCardRequestTypes(ListParkingCardRequestTypesCommand cmd);

    List<ParkingInvoiceTypeDTO> listParkingInvoiceTypes(ListParkingInvoiceTypesCommand cmd);

    ParkingCardType getParkingCardType(String ownerType, Long ownerId, Long parkingLotId, String cardTypeId);

    ParkingExpiredRechargeInfoDTO getExpiredRechargeInfo(GetExpiredRechargeInfoCommand cmd);

    SearchParkingCarVerificationResponse searchParkingCarVerifications(SearchParkingCarVerificationsCommand cmd);

    ListParkingCarVerificationsResponse listParkingCarVerifications(ListParkingCarVerificationsCommand cmd);

    ParkingCarVerificationDTO getParkingCarVerificationById(GetParkingCarVerificationByIdCommand cmd);

    ParkingCarVerificationDTO requestCarVerification(RequestCarVerificationCommand cmd);

    void deleteCarVerification(DeleteCarVerificationCommand cmd);

    ParkingSpaceDTO addParkingSpace(AddParkingSpaceCommand cmd);

    ParkingSpaceDTO updateParkingSpace(UpdateParkingSpaceCommand cmd);

    void updateParkingSpaceStatus(UpdateParkingSpaceStatusCommand cmd);

    void deleteParkingSpace(DeleteParkingSpaceCommand cmd);

    SearchParkingSpacesResponse searchParkingSpaces(SearchParkingSpacesCommand cmd);

    ListParkingSpaceLogsResponse listParkingSpaceLogs(ListParkingSpaceLogsCommand cmd);

    void raiseParkingSpaceLock(RaiseParkingSpaceLockCommand cmd);

    void downParkingSpaceLock(DownParkingSpaceLockCommand cmd);

    void raiseParkingSpaceLockForWeb(RaiseParkingSpaceLockCommand cmd);

    void downParkingSpaceLockForWeb(DownParkingSpaceLockCommand cmd);

    ListParkingSpaceLogsResponse exportParkingSpaceLogs(ListParkingSpaceLogsCommand cmd,HttpServletResponse response);

    void refreshToken(RefreshTokenCommand cmd);

    List<ListBizPayeeAccountDTO> listPayeeAccount(ListPayeeAccountCommand cmd);

    void createOrUpdateBusinessPayeeAccount(CreateOrUpdateBusinessPayeeAccountCommand cmd);

    ListBusinessPayeeAccountResponse listBusinessPayeeAccount(ListBusinessPayeeAccountCommand cmd);

    void delBusinessPayeeAccount(CreateOrUpdateBusinessPayeeAccountCommand cmd);

    void notifyParkingRechargeOrderPaymentV2(OrderPaymentNotificationCommand cmd);

    void initPayeeAccount(MultipartFile[] files);

    void rechargeOrderMigration();
    
    SearchParkingHubsResponse searchParkingHubs(SearchParkingHubsCommand cmd);

    ParkingHubDTO createOrUpdateParkingHub(CreateOrUpdateParkingHubCommand cmd);

    void deleteParkingHub(DeleteParkingHubCommand cmd);

    GetParkingSpaceLockFullStatusDTO getParkingSpaceLockFullStatus(DeleteParkingSpaceCommand cmd);

    ParkingLotDTO getParkingLotByToken(GetParkingLotByTokenCommand cmd);

    String transformToken(TransformTokenCommand cmd);

    Long createOrderNo(ParkingLot lot);

    void getWxParkingQrcode(GetWxParkingQrcodeCommand cmd, HttpServletResponse resp);

    GetParkingBussnessStatusResponse getParkingBussnessStatus(GetParkingBussnessStatusCommand cmd);

    void initFuncLists(GetParkingBussnessStatusCommand cmd);

    void exportParkingCarVerifications(SearchParkingCarVerificationsCommand cmd, HttpServletResponse resp);

    void updateParkingUserNotice(UpdateUserNoticeCommand cmd);

    void notifyParkingRechargeOrderPaymentWechat(WechatPayNotifyCommand cmd);
}
