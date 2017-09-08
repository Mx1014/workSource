//@formatter:off
package com.everhomes.asset;

import com.everhomes.asset.zjgkVOs.*;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.http.HttpUtils;
import com.everhomes.oauth2client.HttpResponseEntity;
import com.everhomes.oauth2client.handler.RestCallTemplate;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.recommend.RecommendationService;
import com.everhomes.rest.asset.*;
import com.everhomes.rest.asset.BillDetailDTO;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.StringHelper;
import org.elasticsearch.index.analysis.AnalysisSettingsRequired;
import org.springframework.context.ApplicationContext;
import org.apache.commons.lang.StringUtils;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
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
            LOGGER.error("调用张江高科失败"+e);
            throw new RuntimeException("调用张江高科失败"+e);
        }
        if(postJson!=null&&postJson.trim().length()>0){
            BillCountResponse response = (BillCountResponse)StringHelper.fromJsonString(postJson, BillCountResponse.class);
            if(response.getErrorCode()==200){
                ContractBillsStatDTO res = response.getResponse();
                finalDto.setBillPeriodMonths(res.getMonthsTotalOwed());
                finalDto.setAmountOwed(new BigDecimal(res.getAmountTotal()));
            }else{
                LOGGER.error("调用张江高科失败"+response.getErrorDescription()+","+response.getErrorDetails());
            }
        }
        //找列表
        postJson = "";
        params=new HashMap<String, String> ();
        String zjgk_communityIdentifier = assetProvider.findZjgkCommunityIdentifierById(ownerId);
        String payFlag = "";
        if(isOwedBill==1){
            payFlag="0";
        }else if(isOwedBill==0){
            payFlag="";
        }
        params.put("payFlag", payFlag);
        params.put("sdateFrom","");
        params.put("sdateTo","");
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
                    dtos.add(dto);
                }
            }else{
                LOGGER.error("调用张江高科searchEnterpriseBills失败"+response.getErrorDescription()+","+response.getErrorDetails());
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
            url = ZjgkUrls.USER_BILLS_DETAIL;
        }else if(targetType.equals("eh_user")){
            url = ZjgkUrls.ENTERPRISE_BILLS_DETAIL;
        }else{
            throw new RuntimeException("查询账单传递了不正确的客户类型"+targetType+",个人应该为eh_user，企业为eh_organization");
        }
        try {
            postJson = HttpUtils.postJson(url, json, 120, HTTP.UTF_8);
        } catch (IOException e) {
            LOGGER.error("调用张江高科失败"+e);
            throw new RuntimeException("调用张江高科失败"+e);
        }
        if(postJson!=null&&postJson.trim().length()>0) {
            BillDetailResponse response = new BillDetailResponse();
            response = (BillDetailResponse)StringHelper.fromJsonString(postJson, BillDetailResponse.class);
            if(response.getErrorCode()==200){
                com.everhomes.asset.zjgkVOs.BillDetailDTO sourceDto = response.getResponse();
                ShowBillDetailForClientDTO dto = new ShowBillDetailForClientDTO();
                dto.setBillItemName(sourceDto.getFeeName());
                dto.setAmountOwed(sourceDto.getAmountOwed()==null?null:new BigDecimal(sourceDto.getAmountOwed()));
                List<CommunityAddressDTO> apartments = sourceDto.getApartments();
                String buildingName = "";
                String apartmentName = "";
                if(apartments!=null&&apartments.size()>0){
                    buildingName = apartments.get(0).getBuildingName()==null?"":apartments.get(0).getBuildingName();
                    apartmentName = apartments.get(0).getApartmentName()==null?"":apartments.get(0).getApartmentName();
                }
                dto.setAddressName(buildingName+apartmentName);
                list.add(dto);
                result.setDatestr(sourceDto.getBillDate());
                result.setAmountOwed(sourceDto.getAmountOwed()==null?null:new BigDecimal(sourceDto.getAmountOwed()));
                result.setAmountReceivable(sourceDto.getAmountReceivable()==null?null:new BigDecimal(sourceDto.getAmountReceivable()));
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
        params.put("sdateFrom","");
        params.put("sdateTo","");
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
            LOGGER.error("调用张江高科失败"+e);
            throw new RuntimeException("调用张江高科失败"+e);
        }
        if(postJson!=null&&postJson.trim().length()>0){
            BillCountResponse response = (BillCountResponse)StringHelper.fromJsonString(postJson, BillCountResponse.class);
            if(response.getErrorCode()==200){
                ContractBillsStatDTO res = response.getResponse();
                result.setAmountReceivable(res.getMonthsTotalOwed()==null?null:new BigDecimal(res.getMonthsTotalOwed()));
                result.setAmountOwed(res.getMonthsTotalOwed()==null?null:new BigDecimal(res.getMonthsTotalOwed()));
            }else{
                LOGGER.error("调用张江高科失败"+response.getErrorDescription()+","+response.getErrorDetails());
            }
        }
        //找列表
        postJson = "";
        params=new HashMap<String, String> ();
        String zjgk_communityIdentifier = assetProvider.findZjgkCommunityIdentifierById(ownerId);
        String payFlag = "";
        if(billStatus==1){
            payFlag="1";
        }else if(billStatus==0){
            payFlag="0";
        }else{
            payFlag = "";
        }
        params.put("payFlag", payFlag);
        check(dateStr,"切换的目标日期");
        params.put("sdateFrom",payFlag);
        params.put("sdateTo",payFlag);
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

        //列表
//        ShowBillDetailForClientResponse result = new ShowBillDetailForClientResponse();
//        String postJson = "";
//        Map<String, String> params = new HashMap<String, String> ();
//        String zjgk_communityIdentifier = assetProvider.findZjgkCommunityIdentifierById(ownerId);
//        params.put("customerName", "");
//        params.put("communityIdentifer", "");
//        params.put("buildingIdentifier","");
//        params.put("apartmentIdentifier", "");
//        String payFlag = "";
//        if(billStatus==1){
//            payFlag = "1";
//        }else if(billStatus==0){
//            payFlag = "0";
//        }else{
//            payFlag = "";
//        }
//        params.put("payFlag", payFlag);
//        check(dateStr,"目标日期");
//        params.put("sdateFrom",dateStr);
//        params.put("sdateTo",dateStr);
//        params.put("pageOffset","");
//        params.put("pageSize","");
//        check(contractId,"合同编号");
//        params.put("contractNum",contractId);
//        String json = generateJson(params);
//        String url;
//        if(targetType.equals("eh_organization")){
//            url = ZjgkUrls.SEARCH_ENTERPRISE_BILLS;
//        }else if(targetType.equals("eh_user")){
//            url = ZjgkUrls.SEARCH_USER_BILLS;
//        }else{
//            throw new RuntimeException("查询账单传递了不正确的客户类型"+targetType+",个人应该为eh_user，企业为eh_organization");
//        }
//        try {
//
//            postJson = HttpUtils.postJson(url, json, 120, HTTP.UTF_8);
//        } catch (IOException e) {
//            LOGGER.error("调用张江高科失败"+e);
//            throw new RuntimeException("调用张江高科失败"+e);
//        }
//        if(postJson!=null&&postJson.trim().length()>0){
//            SearchBillsResponse response = (SearchBillsResponse)StringHelper.fromJsonString(postJson, SearchBillsResponse.class);
//            if(response.getErrorCode()==200){
//                List<SearchEnterpriseBillsDTO> res = response.getResponse();
//                Integer nextPageOffset = response.getNextPageOffset();
//                for(int i = 0 ; i < res.size(); i++){
//                    SearchEnterpriseBillsDTO sourceDto = res.get(i);
//                    ListBillsDTO dto = new ListBillsDTO();
////                    dto.setContractId(sourceDto.getCont);
//                    dto.setContractNum(sourceDto.getContractNum());
//                    dto.setTargetId(sourceDto.getCustomerIdentifier());
//                    dto.setTargetType(targetType);
//                    dto.setBillStatus(sourceDto.getPayFlag());
//                    String noticeTel = "";
//                    String noticeTels = sourceDto.getNoticeTels();
//                    if(noticeTels!=null && sourceDto.getNoticeTels().split(",").length>1){
//                        noticeTel = sourceDto.getNoticeTels().split(",")[0];
//                    }else if(noticeTels!=null){
//                        noticeTel = sourceDto.getNoticeTels();
//                    }
//                    dto.setNoticeTel(noticeTel);
//                    dto.setBillId(sourceDto.getBillID());
//                    dto.setBillGroupName(sourceDto.getFeeName());
//                    dto.setAmountOwed(new BigDecimal(sourceDto.getAmountOwed()));
//                    dto.setAmountReceivable(new BigDecimal(sourceDto.getAmountReceivable()));
//                    dto.setAmountReceived(new BigDecimal(sourceDto.getAmountReceived()));
//                    list.add(dto);
//                    carrier.setNextPageAnchor(response.getNextPageOffset().longValue());
//                }
//            }else{
//                LOGGER.error("调用张江高科searchEnterpriseBills失败"+response.getErrorDescription()+","+response.getErrorDetails());
//            }
//        }
//        return list;
    }

    @Override
    public FindUserInfoForPaymentResponse findUserInfoForPayment(FindUserInfoForPaymentCommand cmd) {
        FindUserInfoForPaymentResponse result = new FindUserInfoForPaymentResponse();
        List<FindUserInfoForPaymentDTO> list = new ArrayList<>();
        String postJson = "";
        Map<String, String> params=new HashMap<String, String> ();
        String identifier;
        params.put("pageOffset", "1");
        params.put("pageSize", "999");
        String url;
        String targetType= cmd.getTargetType();
        if(targetType!=null && targetType.equals("eh_organization")){
            url = ZjgkUrls.ENTERPRISE_CONTRACT_LIST;
            Organization organization = organizationProvider.findOrganizationById(cmd.getTargetId());
            identifier = organization.getNamespaceOrganizationToken();
            params.put("enterpriseIdentifier",identifier);
        }else if(targetType!=null && targetType.equals("eh_user")){
            url = ZjgkUrls.USER_CONTRACT_LIST;
            User user = userProvider.findUserById(cmd.getTargetId());
            identifier = user.getIdentifierToken();
            params.put("userMobile",identifier);
        }else{
            throw new RuntimeException("查询账单传递了不正确的客户类型"+targetType+",个人应该为eh_user，企业为eh_organization");
        }
        String json = generateJson(params);
        try {
            postJson = HttpUtils.postJson(url, json, 120, HTTP.UTF_8);
        } catch (IOException e) {
            LOGGER.error("调用张江高科失败"+e);
            throw new RuntimeException("调用张江高科失败"+e);
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
                    customerName = sourceDto.getSettled();
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
                LOGGER.error("调用张江高科失败"+response.getErrorDescription());
                throw new RuntimeException("调用张江高科失败"+response.getErrorDescription());
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
        params.put("contractNum", cmd.getContractNumber());
        String json = generateJson(params);
        String url;
        String targetType= cmd.getTargetType();
        if(targetType!=null && targetType.equals("eh_organization")){
            url = ZjgkUrls.ENTERPRISE_BILLS_DETAIL;
        }else if(targetType!=null && targetType.equals("eh_user")){
            url = ZjgkUrls.USER_CONTRACT_DETAIL;
        }else{
            throw new RuntimeException("查询账单传递了不正确的客户类型"+targetType+",个人应该为eh_user，企业为eh_organization");
        }
        try {
            postJson = HttpUtils.postJson(url, json, 120, HTTP.UTF_8);
        } catch (IOException e) {
            LOGGER.error("调用张江高科失败"+e);
            throw new RuntimeException("调用张江高科失败"+e);
        }
        if(postJson!=null&&postJson.trim().length()>0){
            ContractDetailResponse response =(ContractDetailResponse) StringHelper.fromJsonString(postJson, ContractDetailResponse.class);
            if(response.getErrorCode()==200){
                ContractDTO sourceDto = response.getResponse();
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
                LOGGER.error("调用张江高科失败"+response.getErrorDescription());
                throw new RuntimeException("调用张江高科失败"+response.getErrorDescription());
            }
        }
        return result;
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
    public List<ListBillsDTO> listBills(String communityIdentifier,String contractNum,Integer currentNamespaceId, Long ownerId, String ownerType, String buildingName,String apartmentName, Long addressId, String billGroupName, Long billGroupId, Byte billStatus, String dateStrBegin, String dateStrEnd, int pageOffSet, Integer pageSize, String targetName, Byte status,String targetType,ListBillsResponse carrier) {
        List<ListBillsDTO> list = new ArrayList<>();
        String postJson = "";
        Map<String, String> params=new HashMap<String, String> ();
        String zjgk_communityIdentifier = assetProvider.findZjgkCommunityIdentifierById(ownerId);
        params.put("customerName", targetName==null?"":targetName);
        params.put("communityIdentifer", zjgk_communityIdentifier==null?"":communityIdentifier);
        params.put("buildingIdentifier", buildingName==null?"":String.valueOf(buildingName));
        params.put("apartmentIdentifier", apartmentName==null?"":String.valueOf(apartmentName));
        params.put("payFlag", billStatus==null?"":String.valueOf(billStatus));
        params.put("sdateFrom",dateStrBegin==null?"":dateStrBegin);
        params.put("sdateTo",dateStrEnd==null?"":dateStrEnd);
        params.put("pageOffset",String.valueOf(pageOffSet)==null?"":String.valueOf(pageOffSet));
        params.put("pageSize",pageSize==null?"":String.valueOf(pageSize));
        params.put("contractNum", StringUtils.isEmpty(contractNum)?"":contractNum);
        String json = generateJson(params);
        String url;
        if(targetType.equals("eh_organization")){
            url = ZjgkUrls.SEARCH_ENTERPRISE_BILLS;
        }else if(targetType.equals("eh_user")){
            url = ZjgkUrls.SEARCH_USER_BILLS;
        }else{
            throw new RuntimeException("查询账单传递了不正确的客户类型"+targetType+",个人应该为eh_user，企业为eh_organization");
        }
        try {

            postJson = HttpUtils.postJson(url, json, 120, HTTP.UTF_8);
        } catch (IOException e) {
            LOGGER.error("调用张江高科searchEnterpriseBills失败"+e);
            throw new RuntimeException("调用张江高科searchEnterpriseBills失败"+e);
        }
        if(postJson!=null&&postJson.trim().length()>0){
            SearchBillsResponse response = (SearchBillsResponse)StringHelper.fromJsonString(postJson, SearchBillsResponse.class);
            if(response.getErrorCode()==200){
                List<SearchEnterpriseBillsDTO> res = response.getResponse();
                Integer nextPageOffset = response.getNextPageOffset();
                carrier.setNextPageAnchor(nextPageOffset==null?pageOffSet:nextPageOffset.longValue());
                for(int i = 0 ; i < res.size(); i++){
                    SearchEnterpriseBillsDTO sourceDto = res.get(i);
                    ListBillsDTO dto = new ListBillsDTO();
//                    dto.setContractId(sourceDto.getCont);
                    dto.setContractNum(sourceDto.getContractNum());
                    dto.setTargetId(sourceDto.getCustomerIdentifier());
                    dto.setTargetType(targetType);
                    dto.setBillStatus(sourceDto.getPayFlag());
                    String noticeTel = "";
                    String noticeTels = sourceDto.getNoticeTels();
                    if(noticeTels!=null && sourceDto.getNoticeTels().split(",").length>1){
                        noticeTel = sourceDto.getNoticeTels().split(",")[0];
                    }else if(noticeTels!=null){
                        noticeTel = sourceDto.getNoticeTels();
                    }
                    dto.setNoticeTel(noticeTel);
                    dto.setBillId(sourceDto.getBillID());
                    dto.setBillGroupName(sourceDto.getFeeName());
                    dto.setAmountOwed(new BigDecimal(sourceDto.getAmountOwed()));
                    dto.setAmountReceivable(new BigDecimal(sourceDto.getAmountReceivable()));
                    dto.setAmountReceived(new BigDecimal(sourceDto.getAmountReceived()));
                    list.add(dto);
                    carrier.setNextPageAnchor(response.getNextPageOffset().longValue());
                }
            }else{
                LOGGER.error("调用张江高科searchEnterpriseBills失败"+response.getErrorDescription()+","+response.getErrorDetails());
            }
        }
        return list;
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
    public List<BillDTO> listBillItems(String targetType,String billId, String targetName, int pageOffSet, Integer pageSize) {
        List<BillDTO> list = new ArrayList<>();
        String postJson = "";
        Map<String, String> params=new HashMap<String, String> ();
        check(billId,"billId");
        params.put("billId", billId);
        String json = generateJson(params);
        String url;
        if(targetType.equals("eh_organization")){
            url = ZjgkUrls.USER_BILLS_DETAIL;
        }else if(targetType.equals("eh_user")){
            url = ZjgkUrls.ENTERPRISE_BILLS_DETAIL;
        }else{
            throw new RuntimeException("查询账单传递了不正确的客户类型"+targetType+",个人应该为eh_user，企业为eh_organization");
        }
        try {
            postJson = HttpUtils.postJson(url, json, 120, HTTP.UTF_8);
        } catch (IOException e) {
            LOGGER.error("调用张江高科失败"+e);
            throw new RuntimeException("调用张江高科失败"+e);
        }
        if(postJson!=null&&postJson.trim().length()>0) {
            BillDetailResponse response = new BillDetailResponse();
            response = (BillDetailResponse)StringHelper.fromJsonString(postJson, BillDetailResponse.class);
            if(response.getErrorCode()==200){
                com.everhomes.asset.zjgkVOs.BillDetailDTO sourceDto = response.getResponse();
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
                list.add(dto);
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
                url = ZjgkUrls.USER_BILLS_DETAIL;
            } else if (targetType.equals("eh_user")) {
                url = ZjgkUrls.ENTERPRISE_BILLS_DETAIL;
            } else {
                throw new RuntimeException("查询账单传递了不正确的客户类型" + targetType + ",个人应该为eh_user，企业为eh_organization");
            }
            try {
                postJson = HttpUtils.postJson(url, json, 120, HTTP.UTF_8);
            } catch (IOException e) {
                LOGGER.error("调用张江高科失败" + e);
                throw new RuntimeException("调用张江高科失败" + e);
            }
            if (postJson != null && postJson.trim().length() > 0) {
                BillDetailResponse response = new BillDetailResponse();
                response = (BillDetailResponse) StringHelper.fromJsonString(postJson, BillDetailResponse.class);
                if (response.getErrorCode() == 200) {
                    com.everhomes.asset.zjgkVOs.BillDetailDTO sourceDto = response.getResponse();
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
                    no.setAmountOwed(sourceDto.getAmountOwed() != null ? new BigDecimal(sourceDto.getAmountOwed()) : null);
                    no.setAmountRecevable(sourceDto.getAmountReceivable() != null ? new BigDecimal(sourceDto.getAmountReceivable()) : null);
                    String noticeTels = sourceDto.getNoticeTels();
                    String noticeTel = "";
                    if (noticeTels != null && sourceDto.getNoticeTels().split(",").length > 1) {
                        noticeTel = sourceDto.getNoticeTels().split(",")[0];
                    } else if (noticeTels != null) {
                        noticeTel = sourceDto.getNoticeTels();
                    }
                    no.setPhoneNum(noticeTel);
                    list.add(no);
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
