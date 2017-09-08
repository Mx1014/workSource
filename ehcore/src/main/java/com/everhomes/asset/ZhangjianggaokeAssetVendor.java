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
    private UserService userService;

    @Autowired
    private CommunityProvider communityProvider;

    @Autowired
    private static final String secretKey = "2CQ7dgiGCIfdKyHfHzO772IltqC50e9w7fswbn6JezdEAZU+x4+VHsBE/RKQ5BCkz/irj0Kzg6te6Y9JLgAvbQ==";

    @Autowired
    private static final String appKey = "ee4c8905-9aa4-4d45-973c-ede4cbb3cf21";
//放到数据库，或者分离出http地址前缀+配成zjgk的配置项
    @Autowired
    private static final String apartmentBillUrl = "http://139.129.220.146:3578/openapi/getApartmentBills";

    @Autowired
    private static final String companyBillUrl = "http://139.129.220.146:3578/openapi/getCompanyBills";

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private OrganizationProvider organizationProvider;


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

//        Map<String,String> map = new HashMap<>();
//        String payFlag = "";
//        Community communityById = new Community();
//        if(targetType=="community"){
//            communityById = communityProvider.findCommunityById(ownerId);
//        } else {
//            return null;
//        }
//        String communityName = communityById.getName();
//        if (communityName == ""){
//            return null;
//        }
//        if(targetType == "eh_user") {
//            //个人用户，查询门牌
//            List<String[]> list = userService.listBuildingAndApartmentById(UserContext.currentUserId());
//            //调用getApartmentBills
//            //当目前api为：门牌楼栋不是list
//            List<ShowBillForClientZJGKENTITY> responseList = new ArrayList<>();
//            for(int i = 0 ; i < list.size() ; i++) {
//                String[] address = list.get(i);
//                if (isOwedBill==1){
//                    String body = getUrlBody4GetApartmentBill(communityName,address[0],address[1],"0");
//                    HttpResponseEntity<?> postReturn = postGo(body, apartmentBillUrl, ShowBillForClientZJGKENTITY.class);
//                    ShowBillForClientZJGKENTITY res = (ShowBillForClientZJGKENTITY)postReturn.getBody();
//                    responseList.add(res);
//                }else{
//                    String body1 = getUrlBody4GetApartmentBill(communityName,address[0],address[1],"0");
//                    String body2 = getUrlBody4GetApartmentBill(communityName,address[0],address[1],"1");
//                    HttpResponseEntity<?> postReturn1 = postGo(body1, apartmentBillUrl, ShowBillForClientZJGKENTITY.class);
//                    HttpResponseEntity<?> postReturn2 = postGo(body2, apartmentBillUrl, ShowBillForClientZJGKENTITY.class);
//                    ShowBillForClientZJGKENTITY res1 = (ShowBillForClientZJGKENTITY)postReturn1.getBody();
//                    ShowBillForClientZJGKENTITY res2 = (ShowBillForClientZJGKENTITY)postReturn2.getBody();
//                    responseList.add(res1);
//                    responseList.add(res2);
//                }
//                //将这个楼栋门牌返回的结果放到list中
//            }
//            //处理完全的返回结果，merge，排序，填充到我的dto中的一部分
//            sortedBills = getSortedBills(responseList);
//        } else if(targetType == "eh_organization"){
//            //拿到enterpriseName
//            //调用getCompanyBills
//            String organizationName = organizationProvider.getOrganizationNameById(targetId);
//            if(isOwedBill.equals("1")){
//                payFlag = "0";
//            }
//            //GetCompanyBillsEntity以及getSortedBills，countTotal，mergeToList剩下的开发
//            String body = getUrlBody4GetCompanyBills(communityName,organizationName,payFlag);
//            HttpResponseEntity<?> entity = postGo(body, companyBillUrl, GetCompanyBillsEntity.class);
//            GetCompanyBillsEntity res = (GetCompanyBillsEntity)entity.getBody();
//            sortedBills = getSortedBills(res);
//        }
//        return sortedBills;
    }

    private String getUrlBody4GetCompanyBills(String communityName, String organizationName, String payFlag) {
        HashMap<String,String> params = new HashMap<>();
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONDAY) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        String nowTime = String.valueOf(year) + String.valueOf(month) + String.valueOf(day);
        params.put("communityName",communityName);
        params.put("organizationName",organizationName);
        params.put("offset","0");
        params.put("pageSize","50");
        if (payFlag.equals("1")){
            params.put("payFlag","1");
        }else if(payFlag.equals("0")){
            params.put("payFlag","0");
        }
        int newMonth = month + 6>12? month+6-12:month+6;
        int before6Month = month - 6 < 0 ? 12 -(month - 5):month - 5;
        int newYear = month + 6 > 12? year+1 : year;
        int before6Year = month - 6 < 0 ? year-1 : year;
        String afterSixMonth = String.valueOf(newYear) + String.valueOf(newMonth) + String.valueOf(day);
        String beforeSixMonth = String.valueOf(before6Year) + String.valueOf(before6Month) + String.valueOf(day);

        params.put("sdateFrom",beforeSixMonth);
        params.put("sdateTo",nowTime);
        params.put("appKey",appKey);
        Long l = System.currentTimeMillis();
        params.put("timestamp",String.valueOf(l));
        Random r = new Random();
        int nonce = r.nextInt(1000);
        params.put("nonce",String.valueOf(nonce));
        params.put("crypto","Doc.Wentian");
        String signature = computeSignature(params,secretKey);
        String body = RestCallTemplate.queryStringBuilder()
                .var("communityName",communityName)
                .var("buildingName",organizationName)
                .var("offset","0")
                .var("pageSize","50")
                .var("payFlag",payFlag)
                .var("sdateFrom",nowTime)
                .var("sdateTo",beforeSixMonth)
                .var("appKey",appKey)
                .var("timestamp",String.valueOf(l))
                .var("nonce",String.valueOf(nonce))
                .var("crypto","Doc.Wentian")
                .var("signature",signature)
                .build();
        return body;
    }

    private ShowBillForClientDTO getSortedBills(GetCompanyBillsEntity res) {
        return null;
    }

    private ShowBillForClientDTO getSortedBills(List<ShowBillForClientZJGKENTITY> responseList) {
        Integer billPeriodMonths = 0;
        float amountOwed = 0;
        List<BillDetailDTO> listUnpaid = new ArrayList<>();
        List<BillDetailDTO> listPaid = new ArrayList<>();
        HashMap<String,BillDetailDTO> mergeList = new HashMap<>();

        for (int i = 0; i<responseList.size(); i++) {
            ShowBillForClientZJGKResponse billDTOs = responseList.get(i).getResponse();
            List<BillDTO_zj> billDTO = billDTOs.getBillDTOS();
            for(int j = 0; j < billDTO.size(); j++){
                BillDTO_zj var1 = billDTO.get(j);
                if(mergeList.containsKey(var1.getDateStr())) {
                    if(mergeList.get(var1.getDateStr()).getStatus().equals(var1.getDetails().get(0).getPayFlag())) {
                        mergeToList(mergeList,var1.getDateStr(),var1,true);
                    }else{
                        mergeToList(mergeList,var1.getDateStr(),var1,false);
                    }
                }else{
                    mergeToList(mergeList,null,var1,true);
                }
            }
        }
        return countTotal(mergeList);
    }

    private ShowBillForClientDTO countTotal(HashMap<String, BillDetailDTO> mergeList) {
        return null;
    }

    private void mergeToList(HashMap<String, BillDetailDTO> mergeList, String dateStr, BillDTO_zj var1, boolean b) {
        return;
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
        String json = generateJson(params);
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

    private String getUrlBody4GetApartmentBill(String communityName,String buildingName,String apartmentName,String payFlag){
        HashMap<String,String> params = new HashMap<>();
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        params.put("communityName",communityName);
        params.put("buildingName",buildingName);
        params.put("apartmentName",apartmentName);
        params.put("offset","0");
        params.put("pageSize","50");
        if (payFlag.equals("1")){
            params.put("payFlag","1");
        }else if(payFlag.equals("0")){
            params.put("payFlag","0");
        }
        params.put("sdateFrom",year+"-01");
        params.put("sdateTo",year+"-12");
        params.put("appKey",appKey);
        Long l = System.currentTimeMillis();
        params.put("timestamp",String.valueOf(l));
        Random r = new Random();
        int nonce = r.nextInt(1000);
        params.put("nonce",String.valueOf(nonce));
        params.put("crypto","Doc.Wentian");
        String signature = computeSignature(params,secretKey);
        String body = RestCallTemplate.queryStringBuilder()
                .var("communityName",communityName)
                .var("buildingName",buildingName)
                .var("apartmentName",apartmentName)
                .var("offset","0")
                .var("pageSize","")
                .var("payFlag","")
                .var("sdateFrom","")
                .var("sdateTo","")
                .var("appKey",appKey)
                .var("timestamp",String.valueOf(l))
                .var("nonce",String.valueOf(nonce))
                .var("crypto","Doc.Wentian")
                .var("signature",signature)
                .build();
        return body;
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

    public HttpResponseEntity<?> postGo(String body, String url, Class T) {
    HttpResponseEntity<?> entity = RestCallTemplate.url(url)
            .body(body)
            .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .respType(T)
            .post();
    return (HttpResponseEntity<?>)entity;
    }

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
