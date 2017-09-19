//@formatter:off
package com.everhomes.asset;

import com.everhomes.asset.zjgkVOs.*;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.http.HttpUtils;
import com.everhomes.oauth2client.HttpResponseEntity;
import com.everhomes.oauth2client.handler.RestCallTemplate;
import com.everhomes.order.PayService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationOwner;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.pay.order.PaymentType;
import com.everhomes.recommend.RecommendationService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.asset.*;
import com.everhomes.rest.asset.BillDetailDTO;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.PaymentParamsDTO;
import com.everhomes.rest.order.PreOrderCommand;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.google.gson.Gson;
import org.elasticsearch.common.recycler.Recycler;
import org.elasticsearch.index.analysis.AnalysisSettingsRequired;
import org.springframework.context.ApplicationContext;
import org.apache.commons.lang.StringUtils;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import sun.java2d.pipe.SpanShapeRenderer;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.everhomes.util.SignatureHelper.computeSignature;

/**
 * Created by Wentian Wang on 2017/8/10.
 */

@Component(AssetVendorHandler.ASSET_VENDOR_PREFIX+"ZJGK")
public class ZhangjianggaokeAssetVendor implements AssetVendorHandler{

    private static final Logger LOGGER = LoggerFactory.getLogger(ZhangjianggaokeAssetVendor.class);

    @Autowired
    private AssetProvider assetProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private PayService payService;



    @Override
    public ShowBillForClientDTO showBillForClient(Long ownerId, String ownerType, String targetType, Long targetId, Long billGroupId,Byte isOwedBill,String contractNum) {
        ShowBillForClientDTO finalDto = new ShowBillForClientDTO();
        List<BillDetailDTO> dtos = new ArrayList<>();
        //找合计
        String postJson = "";
        Map<String, String> params=new HashMap<String, String> ();
        params.put("payFlag", "0");
        params.put("sdateFrom","");
        params.put("sdateTo","");
        check(contractNum,"合同编号");
        params.put("contractNum", contractNum);
        String json = generateJson(params);
        String url;
        if(targetType.equals("eh_organization")){
            url = ZjgkUrls.ENTERPRISE_BILLS_STAT;
        }else if(targetType.equals("eh_user")){
            url = ZjgkUrls.USER_BILLS_COUNT;
        }else{
            throw new RuntimeException("查询账单传递了不正确的客户类型"+targetType+",个人应该为eh_user，企业为eh_organization");
        }
        try {
            postJson = HttpUtils.postJson(url, json, 120, HTTP.UTF_8);
        } catch (IOException e) {
            LOGGER.error("Request ShenZhouShuMa Failed"+e);
            throw new RuntimeException("Request ShenZhouShuMa Failed"+e);
        }
        if(postJson!=null&&postJson.trim().length()>0){
            Gson gson = new Gson();
            BillCountResponse response = gson.fromJson(postJson, BillCountResponse.class);
            if(response.getErrorCode()==200){
                List<ContractBillsStatDTO> response1 = response.getResponse();
                if(response1!=null && response1.size()==1){
                    ContractBillsStatDTO res = response1.get(0);
                    finalDto.setBillPeriodMonths(res.getMonthsTotalOwed()==null?0:res.getMonthsTotalOwed());
                    finalDto.setAmountOwed(res.getAmountTotalOwed()==null?new BigDecimal("0"):new BigDecimal(res.getAmountTotalOwed()));
                }
            }else{
                LOGGER.error("Request ShenZhouShuMa Failed"+response.getErrorDescription()+","+response.getErrorDetails());
            }
        }
        //找列表
        postJson = "";
        params=new HashMap<String, String> ();
//        check(String.valueOf(ownerId),"ownerId");
//        String zjgk_communityIdentifier = assetProvider.findZjgkCommunityIdentifierById(ownerId);
        String payFlag = "";
        String dateStrEnd = "";
        if(isOwedBill==1){
            payFlag="0";
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            dateStrEnd = sdf.format(c.getTime());
        }else if(isOwedBill==0){
            payFlag="";
        }
        params.put("payFlag", payFlag);
        params.put("sdateFrom","");
        params.put("sdateTo",dateStrEnd);
        params.put("pageOffset","1");
        params.put("pageSize","999");
        params.put("contractNum", contractNum);
        json = generateJson(params);
        if(targetType.equals("eh_organization")){
            url = ZjgkUrls.ENTERPRISE_BILLS;
        }else if(targetType.equals("eh_user")){
            url = ZjgkUrls.USER_BILLS;
        }else{
            throw new RuntimeException("查询账单传递了不正确的客户类型"+targetType+",个人应该为eh_user，企业为eh_organization");
        }
        try {
            postJson = HttpUtils.postJson(url, json, 120, HTTP.UTF_8);
        } catch (IOException e) {
            LOGGER.error("调用神州数码失败"+e);
            throw new RuntimeException("调用神州数码失败"+e);
        }
        if(postJson!=null&&postJson.trim().length()>0){
            ContractBillResponse response = (ContractBillResponse)StringHelper.fromJsonString(postJson, ContractBillResponse.class);
            if(response.getErrorCode()==200){
                List<ContractBillsDTO> res = response.getResponse();
                for(int i = 0; i < res.size(); i++){
                    ContractBillsDTO sourceDto = res.get(i);
                    BillDetailDTO dto = new BillDetailDTO();
                    dto.setAmountOwed(sourceDto.getAmountOwed()==null?null:new BigDecimal(sourceDto.getAmountOwed()));
                    dto.setAmountReceviable(sourceDto.getAmountReceivable()==null?null:new BigDecimal(sourceDto.getAmountReceivable()));
                    dto.setBillId(sourceDto.getBillID());
                    dto.setDateStr(sourceDto.getBillDate());
                    dto.setStatus(sourceDto.getPayFlag());
                    String szsm_status = sourceDto.getStatus();
                    if(szsm_status.equals(PaymentStatus.SUSPEND)){
                        dto.setPayStatus(PaymentStatus.IN_PROCESS.getCode());
                    }
                    dtos.add(dto);
                }
            }else{
                LOGGER.error("Failed at request shenzhoushuma"+response.getErrorDescription()+","+response.getErrorDetails());
            }
        }
        finalDto.setBillDetailDTOList(dtos);
        return finalDto;
    }

    @Override
    public ShowBillDetailForClientResponse getBillDetailForClient(String billId,String targetType) {
        ShowBillDetailForClientResponse result = new ShowBillDetailForClientResponse();
        List<ShowBillDetailForClientDTO> list = new ArrayList<>();
        String postJson = "";
        Map<String, String> params=new HashMap<String, String> ();
        check(billId,"billId");
        params.put("billId", billId);
        String json = generateJson(params);
        String url;
        if(targetType.equals("eh_organization")){
            url = ZjgkUrls.ENTERPRISE_BILLS_DETAIL;
        }else if(targetType.equals("eh_user")){
            url = ZjgkUrls.USER_BILLS_DETAIL;
        }else{
            LOGGER.error("TargetType incorrect , the targetType given = {},the supported type are, for enterpriise={},for individual={}",targetType,AssetTargetType.ORGANIZATION.getCode(),AssetTargetType.USER.getCode());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "TargetType incorrect");
        }
        try {
            postJson = HttpUtils.postJson(url, json, 120, HTTP.UTF_8);
        } catch (IOException e) {
            LOGGER.error("Request ShenZhouShuMa Failed"+e);
            throw new RuntimeException("Request ShenZhouShuMa Failed"+e);
        }
        if(postJson!=null&&postJson.trim().length()>0) {
            BillDetailResponse response = new BillDetailResponse();
            response = (BillDetailResponse)StringHelper.fromJsonString(postJson, BillDetailResponse.class);
            if(response.getErrorCode()==200){
                List<com.everhomes.asset.zjgkVOs.BillDetailDTO> dtos = response.getResponse();
                BigDecimal amountOwed = new BigDecimal("0");
                BigDecimal amountReceivable = new BigDecimal("0");
                for(int i = 0 ; i < dtos.size(); i++){
                    com.everhomes.asset.zjgkVOs.BillDetailDTO sourceDto = dtos.get(i);
                    ShowBillDetailForClientDTO dto = new ShowBillDetailForClientDTO();
                    dto.setBillItemName(StringUtils.isEmpty(sourceDto.getFeeName())==true?"租金":sourceDto.getFeeName());
                    dto.setAmountOwed(sourceDto.getAmountOwed()==null?null:new BigDecimal(sourceDto.getAmountOwed()));
                    dto.setAmountReceivable(sourceDto.getAmountReceivable()==null?null:new BigDecimal(sourceDto.getAmountReceivable()));
                    amountOwed = amountOwed.add(sourceDto.getAmountOwed()==null?new BigDecimal("0"):new BigDecimal(sourceDto.getAmountOwed()));
                    amountReceivable = amountReceivable.add(sourceDto.getAmountReceivable()==null?new BigDecimal("0"):new BigDecimal(sourceDto.getAmountReceivable()));
                    List<CommunityAddressDTO> apartments = sourceDto.getApartments();
                    String buildingName = "";
                    String apartmentName = "";
                    if(apartments!=null&&apartments.size()>0){
                        buildingName = apartments.get(0).getBuildingName()==null?"":apartments.get(0).getBuildingName();
                        apartmentName = apartments.get(0).getApartmentName()==null?"":apartments.get(0).getApartmentName();
                    }
                    dto.setAddressName(buildingName+apartmentName);
                    dto.setPayStatus(sourceDto.getStatus()!=null?sourceDto.getStatus().equals(PaymentStatus.SUSPEND.getCode())?PaymentStatus.IN_PROCESS.getCode():null:null);
                    result.setDatestr(sourceDto.getBillDate());
                    list.add(dto);
                }
                result.setAmountOwed(amountOwed);
                result.setAmountReceivable(amountReceivable);
                result.setShowBillDetailForClientDTOList(list);
            }
        }
        return result;
    }

    @Override
    public ShowBillDetailForClientResponse listBillDetailOnDateChange(Byte billStatus, Long ownerId, String ownerType, String targetType, Long targetId, String dateStr,String contractId) {
        ShowBillDetailForClientResponse result = new ShowBillDetailForClientResponse();
        List<ShowBillDetailForClientDTO> list = new ArrayList<>();
        //找合计
        String postJson = "";
        Map<String, String> params=new HashMap<String, String> ();
        params.put("payFlag", "0");
        params.put("sdateFrom",dateStr);
        params.put("sdateTo",dateStr);
        check(contractId,"合同编号");
        params.put("contractNum", contractId);
        String json = generateJson(params);
        String url;
        if(targetType.equals("eh_organization")){
            url = ZjgkUrls.ENTERPRISE_BILLS_STAT;
        }else if(targetType.equals("eh_user")){
            url = ZjgkUrls.USER_BILLS_COUNT;
        }else{
            throw new RuntimeException("查询账单传递了不正确的客户类型"+targetType+",个人应该为eh_user，企业为eh_organization");
        }
        try {
            postJson = HttpUtils.postJson(url, json, 120, HTTP.UTF_8);
        } catch (IOException e) {
            LOGGER.error("Request ShenZhouShuMa Failed"+e);
            throw new RuntimeException("Request ShenZhouShuMa Failed"+e);
        }
        if(postJson!=null&&postJson.trim().length()>0){
            BillCountResponse response = (BillCountResponse)StringHelper.fromJsonString(postJson, BillCountResponse.class);
            if(response.getErrorCode()==200){
                List<ContractBillsStatDTO> response1 = response.getResponse();
                if(response1!=null && response1.size()>0){
                    ContractBillsStatDTO res = response1.get(0);
                    result.setAmountOwed(res.getAmountTotalOwed()==null?null:new BigDecimal(res.getAmountTotalOwed()));
                    result.setAmountReceivable(res.getAmountTotalOwed()==null?null:new BigDecimal(res.getAmountTotalOwed()));
                }
                result.setDatestr(dateStr);
            }else{
                LOGGER.error("GET result on date change failed"+response.getErrorDescription()+","+response.getErrorDetails());
            }
        }
        //找列表
        postJson = "";
        params=new HashMap<String, String> ();
        String zjgk_communityIdentifier = assetProvider.findZjgkCommunityIdentifierById(ownerId);
        String payFlag = "";
        if(billStatus==null){
            payFlag="";
        }else if(billStatus==1){
            payFlag="1";
        }else if(billStatus==0){
            payFlag="0";
        }else{
            payFlag = "";
        }
        params.put("payFlag", payFlag);
        check(dateStr,"切换的目标日期");
        params.put("sdateFrom",dateStr);
        params.put("sdateTo",dateStr);
        params.put("pageOffset","1");
        params.put("pageSize","999");
        check(contractId,"合同编号");
        params.put("contractNum", contractId);
        json = generateJson(params);
        if(targetType.equals("eh_organization")){
            url = ZjgkUrls.ENTERPRISE_BILLS;
        }else if(targetType.equals("eh_user")){
            url = ZjgkUrls.USER_BILLS;
        }else{
            throw new RuntimeException("查询账单传递了不正确的客户类型"+targetType+",个人应该为eh_user，企业为eh_organization");
        }
        try {
            postJson = HttpUtils.postJson(url, json, 120, HTTP.UTF_8);
        } catch (IOException e) {
            LOGGER.error("调用神州数码失败"+e);
            throw new RuntimeException("调用神州数码失败"+e);
        }
        if(postJson!=null&&postJson.trim().length()>0){
            ContractBillResponse response = (ContractBillResponse)StringHelper.fromJsonString(postJson, ContractBillResponse.class);
            if(response.getErrorCode()==200){
                List<ContractBillsDTO> res = response.getResponse();
                for(int i = 0; i < res.size(); i++){
                    ContractBillsDTO sourceDto = res.get(i);
                    ShowBillDetailForClientDTO dto = new ShowBillDetailForClientDTO();
                    dto.setAmountOwed(sourceDto.getAmountOwed()==null?null:new BigDecimal(sourceDto.getAmountOwed()));
                    dto.setAmountReceivable(sourceDto.getAmountReceivable()==null?null:new BigDecimal(sourceDto.getAmountReceivable()));
                    String buildingName = "";
                    String apartmentName = "";
                    if(sourceDto.getApartments()!=null&& sourceDto.getApartments().size()>0){
                        CommunityAddressDTO communityAddressDTO = sourceDto.getApartments().get(0);
                        buildingName = communityAddressDTO.getBuildingName();
                        apartmentName = communityAddressDTO.getApartmentName();
                    }
                    dto.setAddressName(buildingName+apartmentName);
                    dto.setBillItemName(sourceDto.getFeeName());
                    list.add(dto);
                }
            }else{
                LOGGER.error("调用张江高科searchEnterpriseBills失败"+response.getErrorDescription()+","+response.getErrorDetails());
            }
        }
        result.setShowBillDetailForClientDTOList(list);
        return result;

    }

    @Override
    public FindUserInfoForPaymentResponse findUserInfoForPayment(FindUserInfoForPaymentCommand cmd) {
        FindUserInfoForPaymentResponse result = new FindUserInfoForPaymentResponse();
        List<FindUserInfoForPaymentDTO> list = new ArrayList<>();
        String postJson = "";
        Map<String, String> params=new HashMap<String, String> ();
        String identifier;
        params.put("pageOffset", "1");
        params.put("pageSize", "2000");
        String url;
        String targetType= cmd.getTargetType();
        if(targetType!=null && targetType.equals(AssetTargetType.ORGANIZATION.getCode())){
            url = ZjgkUrls.ENTERPRISE_CONTRACT_LIST;
            Organization organization = organizationProvider.findOrganizationById(cmd.getTargetId());
            identifier = organization.getNamespaceOrganizationToken();
            if(identifier==null){
                LOGGER.error("Insufficient privilege, zjgkhandler organization_identifier is null");
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                        "Insufficient privilege");
            }
            params.put("enterpriseIdentifier",identifier);
        }else if(targetType!=null && targetType.equals(AssetTargetType.USER.getCode())){
            url = ZjgkUrls.USER_CONTRACT_LIST;
            identifier = assetProvider.findIdentifierByUid(UserContext.currentUserId());
            //测试
            if(identifier==null){
                LOGGER.error("Insufficient privilege, zjgkhandler userMobile is null");
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                        "Insufficient privilege");
            }
            params.put("userMobile",identifier);
        }else{
            throw new RuntimeException("查询账单传递了不正确的客户类型"+targetType+",个人应该为eh_user，企业为eh_organization");
        }
        String json = generateJson(params);
        try {
            postJson = HttpUtils.postJson(url, json, 120, HTTP.UTF_8);
        } catch (IOException e) {
            LOGGER.error("Request ShenZhouShuMa Failed"+e);
            throw new RuntimeException("Request ShenZhouShuMa Failed"+e);
        }
        if(postJson!=null&&postJson.trim().length()>0){
            ContractListResponse response =(ContractListResponse) StringHelper.fromJsonString(postJson, ContractListResponse.class);
            if(response.getErrorCode()==200){
                List<ContractDTO> dtos = response.getResponse();
                String customerName = "";
                BigDecimal areaSizeSum = new BigDecimal("0");
                List<String> addressNames = new ArrayList<>();
                for(int i = 0; i < dtos.size(); i++){
                    ContractDTO sourceDto = dtos.get(i);
                    FindUserInfoForPaymentDTO dto = new FindUserInfoForPaymentDTO();
                    dto.setContractId(sourceDto.getContractNum());
                    dto.setContractNum(sourceDto.getContractNum());
                    try{
                        if(cmd.getTargetType()!=null){
                            if(targetType.equals(AssetTargetType.ORGANIZATION.getCode())){
                                customerName = organizationProvider.findOrganizationById(cmd.getTargetId()).getName();
                            }else{
                                customerName = UserContext.current().getUser().getNickName();
                            }
                        }else{
                            customerName = UserContext.current().getUser().getNickName();
                        }
                    }catch(Exception e){
                        customerName = UserContext.current().getUser().getNickName();
                        LOGGER.error("findUserInfo targetType failed expects, targetType is {} ",targetType);
                    }
                    List<CommunityAddressDTO> apartments = sourceDto.getApartments();
                    if(apartments!=null){
                        for(int k = 0 ; k < apartments.size(); k ++){
                            CommunityAddressDTO communityAddressDTO = apartments.get(k);
                            addressNames.add(communityAddressDTO.getBuildingName()==null?"":communityAddressDTO.getBuildingName()+communityAddressDTO.getApartmentName());
                        }
                    }

                    areaSizeSum = areaSizeSum.add(sourceDto.getAreaSize()==null?new BigDecimal("0"):new BigDecimal(sourceDto.getAreaSize()));
                    list.add(dto);
                }
                result.setAreaSizesSum(areaSizeSum.toString());
                result.setAddressNames(addressNames);
                result.setContractList(list);
                result.setCustomerName(customerName);
            }else{
                LOGGER.error("Request ShenZhouShuMa Failed"+response.getErrorDescription());
                throw new RuntimeException("Request ShenZhouShuMa Failed"+response.getErrorDescription());
            }
        }
        return result;
    }

    @Override
    public GetAreaAndAddressByContractDTO getAreaAndAddressByContract(GetAreaAndAddressByContractCommand cmd) {
        GetAreaAndAddressByContractDTO result = new GetAreaAndAddressByContractDTO();
        String postJson = "";
        Map<String, String> params=new HashMap<String, String> ();
        String identifier;
        params.put("contractNum", cmd.getContractId());
        String json = generateJson(params);
        String url;
        String targetType= cmd.getTargetType();
        //PaymentStatus a = PaymentStatus.fromCode(targetType);
        //if(a != null && PaymentStatus.IN_PROCESS == a)
        if(targetType!=null && targetType.equals("eh_organization")){
            url = ZjgkUrls.ENTERPRISE_CONTRACT_DETAIL;
        }else if(targetType!=null && targetType.equals("eh_user")){
            url = ZjgkUrls.USER_CONTRACT_DETAIL;
        }else{
            LOGGER.error("Invalid target type, targetType={}, cmd={}, supportedTargetType={}", targetType, cmd, PaymentStatus.values());
            throw new RuntimeException("查询账单传递了不正确的客户类型"+targetType+",个人应该为eh_user，企业为eh_organization");
        }
        try {
            postJson = HttpUtils.postJson(url, json, 120, HTTP.UTF_8);
            LOGGER.info("Get contract from zjgk by area and address success, url={}, param={}, result={}", url, json, postJson);
        } catch (Exception e) {
            LOGGER.error("Failed to get contract from zjgk by area and address, url={}, param={}", url, json, e);
            throw new RuntimeException("Request ShenZhouShuMa Failed"+e);
        }
        if(postJson!=null&&postJson.trim().length()>0){
            Gson gson = new Gson();
            ContractDetailResponse response = gson.fromJson(postJson,ContractDetailResponse.class);
            if(response.getErrorCode()==200){
                List<ContractDTO> fakeSourceDtos = response.getResponse();
                if(fakeSourceDtos!=null&&fakeSourceDtos.size()<0){
                    return result;
                }
                ContractDTO sourceDto = fakeSourceDtos.get(0);
                String customerName = "";
                BigDecimal areaSizeSum = new BigDecimal("0");
                List<String> addressNames = new ArrayList<>();

                List<CommunityAddressDTO> apartments = sourceDto.getApartments();
                for(int i = 0; i < apartments.size(); i++){
                    CommunityAddressDTO communityAddressDTO = apartments.get(i);
                    addressNames.add(communityAddressDTO.getBuildingName()==null?"":communityAddressDTO.getBuildingName()+communityAddressDTO.getApartmentName());
                }
                result.setAreaSizesSum(sourceDto.getAreaSize());
                result.setAddressNames(addressNames);
            }else{
                LOGGER.error("Request ShenZhouShuMa Failed"+response.getErrorDescription());
                throw new RuntimeException("Request ShenZhouShuMa Failed"+response.getErrorDescription());
            }
        }
        return result;
    }

    @Override
    public ListBillDetailResponse listBillDetail(ListBillDetailCommand cmd) {
        LOGGER.error("Insufficient privilege, zjgkhandler listBillDetail");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public void deleteBill(String l) {
        LOGGER.error("Insufficient privilege, zjgkhandler deleteBill");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public void deleteBillItem(BillItemIdCommand cmd) {
        LOGGER.error("Insufficient privilege, zjgkhandler deleteBillItem");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public void deletExemptionItem(ExemptionItemIdCommand cmd) {
        LOGGER.error("Insufficient privilege, zjgkhandler deletExemptionItem");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public ShowCreateBillDTO showCreateBill(Long billGroupId) {
        LOGGER.error("Insufficient privilege, zjgkhandler showCreateBill");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public ListBillsDTO createBill(CreateBillCommand cmd) {
        LOGGER.error("Insufficient privilege, zjgkhandler createBill");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public void modifyBillStatus(BillIdCommand cmd) {
        LOGGER.error("Insufficient privilege, zjgkhandler modifyBillStatus");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public ListSettledBillExemptionItemsResponse listBillExemptionItems(listBillExemtionItemsCommand cmd) {
        LOGGER.error("Insufficient privilege, zjgkhandler listBillExemptionItems");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public List<BillStaticsDTO> listBillStatics(BillStaticsCommand cmd) {
        LOGGER.error("Insufficient privilege, zjgkhandler listBillExemptionItems");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public PaymentExpectanciesResponse listBillExpectanciesOnContract(ListBillExpectanciesOnContractCommand cmd) {
        LOGGER.error("Insufficient privilege, zjgkhandler listBillExpectanciesOnContract");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public void exportRentalExcelTemplate(HttpServletResponse response) {
        LOGGER.error("Insufficient privilege, zjgkhandler exportRentalExcelTemplate");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public void updateBillsToSettled(UpdateBillsToSettled cmd) {
        LOGGER.error("Insufficient privilege, zjgkhandler updateBillsToSettled");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public PreOrderDTO placeAnAssetOrder(PlaceAnAssetOrderCommand cmd) {
        //先进行检查是否重复下单,查询此传来bills是否有对应的order，如果有，那么检查订单的状态，如果订单已经完毕，则返回
        List<BillIdAndAmount> bills = cmd.getBills();
        List<String> billIds = new ArrayList<>();
        for(BillIdAndAmount billIdAndAmount : bills){
            billIds.add(billIdAndAmount.getBillId());
        }
        Long checkedOrderId = assetProvider.findAssetOrderByBillIds(billIds);
        if(checkedOrderId !=null){
            //重复下单的返回
            return null;
        }
        //如果账单为新的，则进行存储
        Long orderId  = assetProvider.saveAnOrderCopy(cmd.getPayerType(),cmd.getPayerId(),cmd.getAmountOwed(),cmd.getClientAppName(),cmd.getCommunityId(),cmd.getContactNum(),cmd.getOpenid(),cmd.getPayerName(),15l*60l*1000l);
        assetProvider.saveOrderBills(bills,orderId);
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 7; i++){
            sb.append(r.nextInt(10));
        }
        //请求支付模块的下预付单
        PreOrderCommand cmd2pay = new PreOrderCommand();
//        Long amount = 转成分(cmd.getAmountOwed());
        Long payerId = Long.parseLong(cmd.getPayerId());
        if(cmd.getPayerType().equals(AssetTargetType.USER.getCode())){
            if(Long.parseLong(cmd.getPayerId())==UserContext.currentUserId()){
                payerId = Long.parseLong(cmd.getPayerId());
            }else{
                LOGGER.error("individual make asset order failed, the given uid = {}, but the online uid is = {}",cmd.getPayerId(),UserContext.currentUserId());
                throw new RuntimeErrorException("individual make asset order failed");
            }
        }
        String amountOwed = cmd.getAmountOwed();
        Float var = Float.parseFloat(amountOwed);
        var = var*100f;
        Long amount1 = Long.parseLong(String.valueOf(var.intValue()));

        cmd2pay.setAmount(amount1);
        cmd2pay.setClientAppName(cmd.getClientAppName());
        cmd2pay.setExpiration(15l*60l);
        cmd2pay.setNamespaceId(UserContext.getCurrentNamespaceId());
        cmd2pay.setOpenid(cmd.getOpenid());
        cmd2pay.setOrderId(orderId);
        cmd2pay.setOrderType(OrderType.OrderTypeEnum.ZJGK_RENTAL_CODE.getPycode());
        cmd2pay.setPayerId(payerId);

        //没有返回paymethod，payparams
        cmd2pay.setPaymentType(PaymentType.WECHAT_APPPAY.getCode());
        PaymentParamsDTO paymentParamsDTO = new PaymentParamsDTO();
        paymentParamsDTO.setPayType("no_credit");
        User user = UserContext.current().getUser();
        paymentParamsDTO.setAcct(user.getNamespaceUserToken());
        cmd2pay.setPaymentParams(paymentParamsDTO);



        PreOrderDTO preOrder = payService.createPreOrder(cmd2pay);
//        response.setAmount(String.valueOf(preOrder.getAmount()));
//        response.setExpiredIntervalTime(15l*60l);
//        response.setOrderCommitNonce(preOrder.getOrderCommitNonce());
//        response.setOrderCommitTimestamp(preOrder.getOrderCommitTimestamp());
//        response.setOrderCommitToken(preOrder.getOrderCommitToken());
//        response.setOrderCommitUrl(preOrder.getOrderCommitUrl());
//        response.setPayMethod(preOrder.getPayMethod());

        return preOrder;
    }

    @Override
    public ListSimpleAssetBillsResponse listSimpleAssetBills(Long ownerId, String ownerType, Long targetId, String targetType, Long organizationId, Long addressId, String tenant, Byte status, Long startTime, Long endTime, Long pageAnchor, Integer pageSize) {
        return null;
    }

    @Override
    public AssetBillTemplateValueDTO findAssetBill(Long id, Long ownerId, String ownerType, Long targetId, String targetType, Long templateVersion, Long organizationId, String dateStr, Long tenantId, String tenantType, Long addressId) {
        return null;
    }

    @Override
    public AssetBillStatDTO getAssetBillStat(String tenantType, Long tenantId, Long addressId) {
        return null;
    }

    @Override
    public List<ListBillsDTO> listBills(String communityIdentifier,String contractNum,Integer currentNamespaceId, Long ownerId, String ownerType, String buildingName,String apartmentName, Long addressId, String billGroupName, Long billGroupId, Byte billStatus, String dateStrBegin, String dateStrEnd, Integer pageOffSet, Integer pageSize, String targetName, Byte status,String targetType,ListBillsResponse carrier) {
        if(status!=1){
            LOGGER.error("Insufficient privilege, zjgkhandler listNotSettledBills");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                    "Insufficient privilege");
        }
        List<ListBillsDTO> list = new ArrayList<>();
        String postJson = "";
        Map<String, String> params=new HashMap<String, String> ();
        String zjgk_communityIdentifier = assetProvider.findZjgkCommunityIdentifierById(ownerId);
        if(StringUtils.isEmpty(zjgk_communityIdentifier)){
            LOGGER.error("Zjgk community id is empty, ownerType={}, ownerId={}, result={}", ownerType, ownerId, zjgk_communityIdentifier);
            throw RuntimeErrorException.errorWith("zjgk", 9999712,
                    "该园区暂没有和系统对接，无法查询");
        } else {
            LOGGER.info("Find zjgk community id from db, ownerType={}, ownerId={}, result={}", ownerType, ownerId, zjgk_communityIdentifier);
        }
        params.put("customerName", StringUtils.isEmpty(targetName)==true?"":targetName);
        params.put("communityIdentifer", StringUtils.isEmpty(zjgk_communityIdentifier)==true?"":zjgk_communityIdentifier);
        params.put("buildingIdentifier", StringUtils.isEmpty(buildingName)==true?"":String.valueOf(buildingName));
        params.put("apartmentIdentifier", StringUtils.isEmpty(apartmentName)==true?"":String.valueOf(apartmentName));
        params.put("payFlag", billStatus==null?"":String.valueOf(billStatus));
        params.put("sdateFrom",StringUtils.isEmpty(dateStrBegin)==true?"":dateStrBegin);
        params.put("sdateTo",StringUtils.isEmpty(dateStrEnd)==true?"":dateStrEnd);
        params.put("pageOffset",pageOffSet==null?"":String.valueOf(pageOffSet));
        params.put("pageSize",pageSize==null?"":String.valueOf(pageSize));
        params.put("contractNum", StringUtils.isEmpty(contractNum)?"":contractNum);
        String json = generateJson(params);
        LOGGER.info("listBill"+json);
        String url;
        Boolean nextFLag = true;
        if(targetType==null||targetType.trim().equals("")){
            url = ZjgkUrls.SEARCH_USER_BILLS;
            listBillOnUrls(targetType, carrier, list, json, url,AssetTargetType.USER.getCode());
            if(carrier.getNextPageAnchor()==null){
                nextFLag = false;
            }
            if(list.size()>=pageSize){
                return list;
            }else{
                pageSize = pageSize-list.size();
            }
            params = new HashMap<>();
            params.put("customerName", StringUtils.isEmpty(targetName)==true?"":targetName);
            params.put("communityIdentifer", StringUtils.isEmpty(zjgk_communityIdentifier)==true?"":zjgk_communityIdentifier);
            params.put("buildingIdentifier", StringUtils.isEmpty(buildingName)==true?"":String.valueOf(buildingName));
            params.put("apartmentIdentifier", StringUtils.isEmpty(apartmentName)==true?"":String.valueOf(apartmentName));
            params.put("payFlag", billStatus==null?"":String.valueOf(billStatus));
            params.put("sdateFrom",StringUtils.isEmpty(dateStrBegin)==true?"":dateStrBegin);
            params.put("sdateTo",StringUtils.isEmpty(dateStrEnd)==true?"":dateStrEnd);
            params.put("pageOffset",pageOffSet==null?"":String.valueOf(pageOffSet));
            params.put("pageSize",pageSize==null?"":String.valueOf(pageSize));
            params.put("contractNum", StringUtils.isEmpty(contractNum)?"":contractNum);
            json = generateJson(params);
            LOGGER.info("listBill"+json);
            url = ZjgkUrls.SEARCH_ENTERPRISE_BILLS;
            listBillOnUrls(targetType, carrier, list, json, url,AssetTargetType.ORGANIZATION.getCode());
            if(nextFLag==false){
                if(carrier.getNextPageAnchor()==null){
                    carrier.setNextPageAnchor(null);
                }
            }else{
                Integer next = pageOffSet+1;
                carrier.setNextPageAnchor(next.longValue());
            }
            return list;
        }
        if(targetType.equals("eh_organization")){
            url = ZjgkUrls.SEARCH_ENTERPRISE_BILLS;
            listBillOnUrls(targetType, carrier, list, json, url,"eh_organization");
        }else if(targetType.equals("eh_user")){
            url = ZjgkUrls.SEARCH_USER_BILLS;
            listBillOnUrls(targetType, carrier, list, json, url,"eh_user");
        }else{
            throw new RuntimeException("查询账单传递了不正确的客户类型"+targetType+",个人应该为eh_user，企业为eh_organization");
        }
        return list;
    }

    private void listBillOnUrls(String targetType, ListBillsResponse carrier, List<ListBillsDTO> list, String json, String url,String RelTargetType) {
        String postJson;
        try {
            postJson = HttpUtils.postJson(url, json, 120, HTTP.UTF_8);
            LOGGER.info("Get bills from zjgk success, url={}, param={}, result={}", url, json, postJson);
        } catch (Exception e) {
            LOGGER.error("Get bills from zjgk success, url={}, param={}, ", url, json, e);
            throw new RuntimeException("Request ShenZhouShuMa Failed"+e);
        }
//        HashMap<CommunityFilter,CommunityAddressDTO> dump = new HashMap<>();
        if(postJson!=null&&postJson.trim().length()>0){
            Gson gson = new Gson();
            SearchBillsResponse response = gson.fromJson(postJson, SearchBillsResponse.class);
//            SearchBillsResponse response = (SearchBillsResponse) StringHelper.fromJsonString(postJson, SearchBillsResponse.class);
            if(response.getErrorCode()==200){
                List<SearchEnterpriseBillsDTO> res = response.getResponse();
                String nextPageOffset = response.getNextPageOffset();
                carrier.setNextPageAnchor(StringUtils.isEmpty(nextPageOffset)==true?null:Long.parseLong(nextPageOffset));
                for(int i = 0 ; i < res.size(); i++){
                    SearchEnterpriseBillsDTO sourceDto = res.get(i);
                    ListBillsDTO dto = new ListBillsDTO();
//                    dto.setContractId(sourceDto.getCont);
                    dto.setContractNum(sourceDto.getContractNum());
                    dto.setTargetId(sourceDto.getCustomerIdentifier());
                    dto.setTargetType(RelTargetType);
                    dto.setBillStatus(sourceDto.getPayFlag());
                    dto.setOwnerType("community");
                    List<String> noticeTel = new ArrayList<>();
                    String noticeTels = sourceDto.getNoticeTels();
                    if(noticeTels!=null && sourceDto.getNoticeTels().split(",").length>1){
                        for(String tel:sourceDto.getNoticeTels().split(",")){
                            noticeTel.add(tel);
                        }
                    }else if(noticeTels!=null && sourceDto.getNoticeTels().split("/").length>1){
                        for(String tel:sourceDto.getNoticeTels().split("/")){
                            noticeTel.add(tel);
                        }
                    }
                    else if(noticeTels!=null){
                        noticeTel.add(sourceDto.getNoticeTels());
                    }
                    StringBuilder phones = new StringBuilder();
                    forPhoneLoop:for(int k = 0; k < noticeTel.size(); k++){
                        if(k == noticeTel.size()-1){
                            phones.append(noticeTel.get(k));
                            break forPhoneLoop;
                        }
                        phones.append(noticeTel.get(k)+",");
                    }
                    dto.setNoticeTel(phones.toString());
                    dto.setBillId(sourceDto.getBillID());
                    dto.setBillGroupName(sourceDto.getFeeName()==null?"租金":sourceDto.getFeeName());
                    dto.setDateStr(sourceDto.getBillDate());
                    dto.setTargetType(RelTargetType);
                    dto.setTargetName(sourceDto.getCustomerName());
                    dto.setAmountOwed(new BigDecimal(sourceDto.getAmountOwed()));
                    dto.setAmountReceivable(new BigDecimal(sourceDto.getAmountReceivable()));
                    dto.setAmountReceived(new BigDecimal(sourceDto.getAmountReceived()));
                    list.add(dto);
                }
            }else{
                LOGGER.error("调用张江高科searchEnterpriseBills失败"+response.getErrorDescription()+","+response.getErrorDetails());
            }
        }
    }

    private String generateJson(Map<String,String> params){
        params.put("appKey", "ee4c8905-9aa4-4d45-973c-ede4cbb3cf21");
        params.put("nonce", "54256");
        params.put("timestamp", "1498097655000");
        params.put("crypto", "sssss");
        String SECRET_KEY = "2CQ7dgiGCIfdKyHfHzO772IltqC50e9w7fswbn6JezdEAZU+x4+VHsBE/RKQ5BCkz/irj0Kzg6te6Y9JLgAvbQ==";
        params.put("signature",computeSignature(params,SECRET_KEY));
        return StringHelper.toJsonString(params);
    }

    @Override
    public List<BillDTO> listBillItems(String targetType,String billId, String targetName, Integer pageOffSet, Integer pageSize) {
        List<BillDTO> list = new ArrayList<>();
        String postJson = "";
        Map<String, String> params=new HashMap<String, String> ();
        check(billId,"billId");
        params.put("billId", billId);
        String json = generateJson(params);
        LOGGER.info("Billitem,1 param"+json);
        String url;
        if(targetType.equals("eh_organization")){
            url = ZjgkUrls.ENTERPRISE_BILLS_DETAIL;
        }else if(targetType.equals("eh_user")){
            url = ZjgkUrls.USER_BILLS_DETAIL;
        }else{
            throw new RuntimeException("查询账单传递了不正确的客户类型"+targetType+",个人应该为eh_user，企业为eh_organization");
        }
        try {
            postJson = HttpUtils.postJson(url, json, 120, HTTP.UTF_8);
            LOGGER.info("Get bill items from zjgk success, url={}, param={}, result={}", url, json, postJson);
        } catch (Exception e) {
            LOGGER.error("Failed to get bill item from zjgk, url={}, param={}", url, json, e);
            throw new RuntimeException("Request ShenZhouShuMa Failed"+e);
        }
        if(postJson!=null && postJson.trim().length() > 0) {
            BillDetailResponse response = (BillDetailResponse)StringHelper.fromJsonString(postJson, BillDetailResponse.class);
//            BillDetailResponse response = gson.fromJson(postJson, BillDetailResponse.class);
            if(response.getErrorCode() == 200){
                List<com.everhomes.asset.zjgkVOs.BillDetailDTO> dtos = response.getResponse();
                for(int i = 0; i < dtos.size(); i++){
                    com.everhomes.asset.zjgkVOs.BillDetailDTO sourceDto = dtos.get(i);
                    String buildingName = "";
                    String apartmentName = "";
                    if(sourceDto.getApartments()!=null&& sourceDto.getApartments().size()>0){
                        CommunityAddressDTO communityAddressDTO = sourceDto.getApartments().get(0);
                        buildingName = communityAddressDTO.getBuildingName();
                        apartmentName = communityAddressDTO.getApartmentName();
                    }
                    BillDTO dto = new BillDTO();
                    dto.setBuildingName(buildingName);
                    dto.setApartmentName(apartmentName);
//                    dto.setBillItemId(billId);
                    dto.setAmountOwed(sourceDto.getAmountOwed()!=null?new BigDecimal(sourceDto.getAmountOwed()):null);
                    dto.setAmountReceivable(sourceDto.getAmountReceivable()!=null?new BigDecimal(sourceDto.getAmountReceivable()):null);
                    dto.setAmountReceived(sourceDto.getAmountReceived()!=null?new BigDecimal(sourceDto.getAmountReceived()):null);
                    dto.setBillItemName(sourceDto.getFeeName());
                    dto.setBillStatus(sourceDto.getPayFlag());
                    dto.setDateStr(sourceDto.getBillDate());
//                    dto.setDefaultOrder();
                    dto.setTargetId(sourceDto.getCustomerIdentifier());
                    dto.setTargetName(sourceDto.getCustomerName());
                    dto.setTargetType(targetType);
                    dto.setPayStatus(sourceDto.getStatus()!=null?sourceDto.getStatus().equals(PaymentStatus.SUSPEND.getCode())?PaymentStatus.IN_PROCESS.getCode():null:null);
                    list.add(dto);
                }
            }
        }
        return list;
    }

    @Override
    public List<NoticeInfo> listNoticeInfoByBillId(List<BillIdAndType> billIdAndTypes) {
        List<NoticeInfo> list = new ArrayList<>();
        String postJson = "";
        Map<String, String> params = new HashMap<String, String>();
        for (int i = 0; i < billIdAndTypes.size(); i++) {
            BillIdAndType idAndType = billIdAndTypes.get(i);
            String billId = idAndType.getBillId();
            check(billId, "billId");
            params.put("billId", billId);
            String json = generateJson(params);
            String url;
            String targetType = idAndType.getTargetType();
            check(targetType, "客户类型");
            if (targetType.equals("eh_organization")) {
                url = ZjgkUrls.ENTERPRISE_BILLS_DETAIL;
            } else if (targetType.equals("eh_user")) {
                url = ZjgkUrls.USER_BILLS_DETAIL;
            } else {
                throw new RuntimeException("查询账单传递了不正确的客户类型" + targetType + ",个人应该为eh_user，企业为eh_organization");
            }
            try {
                postJson = HttpUtils.postJson(url, json, 120, HTTP.UTF_8);
            } catch (IOException e) {
                LOGGER.error("Request ShenZhouShuMa Failed" + e);
                throw new RuntimeException("Request ShenZhouShuMa Failed" + e);
            }
            if (postJson != null && postJson.trim().length() > 0) {
                BillDetailResponse response = new BillDetailResponse();
                Gson gson = new Gson();
                response = gson.fromJson(postJson, BillDetailResponse.class);
                if (response.getErrorCode() == 200) {
                    List<com.everhomes.asset.zjgkVOs.BillDetailDTO> dtos = response.getResponse();
                    BigDecimal amountReceivable = new BigDecimal("0");
                    BigDecimal amountOwed = new BigDecimal("0");
                    if(dtos!=null&& dtos.size()>0){
                        for(int j = 0; j < dtos.size() ; j ++){
                            com.everhomes.asset.zjgkVOs.BillDetailDTO dto = dtos.get(j);
                            amountReceivable = amountReceivable.add(dto.getAmountReceivable()==null?new BigDecimal("0"):new BigDecimal(dto.getAmountReceivable()));
                            amountOwed = amountOwed.add(dto.getAmountOwed()==null?new BigDecimal("0"):new BigDecimal(dto.getAmountOwed()));
                        }
                        com.everhomes.asset.zjgkVOs.BillDetailDTO sourceDto = dtos.get(0);
                        NoticeInfo no = new NoticeInfo();
                        no.setTargetName(sourceDto.getCustomerName());
                        no.setTargetType(targetType);
                        Long targetId = assetProvider.findTargetIdByIdentifier(sourceDto.getCustomerIdentifier());
                        no.setTargetId(targetId);
                        String appName = assetProvider.findAppName(UserContext.getCurrentNamespaceId());
                        if (appName != null && appName.trim().length() > 0) {
                            no.setAppName(appName);
                        } else {
                            no.setAppName("张江高科推荐");
                        }
                        no.setAmountOwed(amountOwed);
                        no.setAmountRecevable(amountReceivable);
                        String noticeTels = sourceDto.getNoticeTels();
                        List<String> tempList = new ArrayList<>();
                        if(noticeTels!=null && sourceDto.getNoticeTels().split(",").length>1){
                            for(String tel:sourceDto.getNoticeTels().split(",")){
                                tempList.add(tel);
                            }
                        }else if(noticeTels!=null && sourceDto.getNoticeTels().split("/").length>1){
                            for(String tel:sourceDto.getNoticeTels().split("/")){
                                tempList.add(tel);
                            }
                        }
                        else if(noticeTels!=null){
                            tempList.add(sourceDto.getNoticeTels());
                        }
                        StringBuilder sb = new StringBuilder();
                        for(int l = 0; l < tempList.size(); l ++){
                            if(l == tempList.size()-1){
                                sb.append(tempList.get(l));
                                break;
                            }
                            sb.append(tempList.get(l)+",");
                        }
                        no.setPhoneNums(sb.toString());
                        no.setDateStr(sourceDto.getBillDate());
                        list.add(no);
                    }
                }
            }
        }
        return list;
    }
//    public HttpResponseEntity<?> postGo(String body, String url, Class T) {
//    HttpResponseEntity<?> entity = RestCallTemplate.url(url)
//            .body(body)
//            .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//            .respType(T)
//            .post();
//    return (HttpResponseEntity<?>)entity;
//    }

    private ZuolinAssetVendorHandler getZuolinHandler(){
        return applicationContext.getBean(ZuolinAssetVendorHandler.class);
    }
    private void check(String fieldValue, String fieldName){
        if(fieldValue==null||fieldValue.trim().length()<1){
            LOGGER.error(fieldName+":"+fieldValue+"为必填，不能为空");
            throw new RuntimeException(fieldName+":"+fieldValue+"为必填，不能为空");
        }
    }

}
