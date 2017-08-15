//@formatter:off
package com.everhomes.asset;

import com.everhomes.address.AddressProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.oauth2client.HttpResponseEntity;
import com.everhomes.oauth2client.handler.RestCallTemplate;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.asset.BillDetailDTO;
import com.everhomes.rest.asset.ShowBillDetailForClientDTO;
import com.everhomes.rest.asset.ShowBillDetailForClientResponse;
import com.everhomes.rest.asset.ShowBillForClientDTO;
import com.everhomes.search.OrganizationSearcher;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

import static com.everhomes.util.SignatureHelper.computeSignature;

/**
 * Created by Wentian Wang on 2017/8/10.
 */

@Component(AssetVendorHandler.ASSET_VENDOR_PREFIX+"ZJGK")
public class ZhangjianggaokeAssetVendor extends ZuolinAssetVendorHandler{

    private static final Logger LOGGER = LoggerFactory.getLogger(ZhangjianggaokeAssetVendor.class);

    @Autowired
    private AssetProvider assetProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private CommunityProvider communityProvider;

//    String appKey = configurationProvider.getValue(NAMESPACE_ID, "shenzhoushuma.app.key", "");
//    String secretKey = configurationProvider.getValue(NAMESPACE_ID, "shenzhoushuma.secret.key", "");
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

    @Override
    public ShowBillForClientDTO showBillForClient(Long ownerId, String ownerType, String targetType, Long targetId, Long billGroupId,Byte isOwedBill) {
        Map<String,String> map = new HashMap<>();
        Community communityById = new Community();
        if(targetType=="community"){
            communityById = communityProvider.findCommunityById(ownerId);
        } else {
            return null;
        }
        String communityName = communityById.getName();
        if (communityName == ""){
            return null;
        }
        ShowBillForClientDTO sortedBills = new ShowBillForClientDTO();
        if(targetType == "eh_user") {
            //个人用户，查询门牌
            List<String[]> list = userService.listBuildingAndApartmentById(UserContext.currentUserId());
            //调用getApartmentBills
            //当目前api为：门牌楼栋不是list
            List<ShowBillForClientZJGKENTITY> responseList = new ArrayList<>();
            for(int i = 0 ; i < list.size() ; i++) {
                String[] address = list.get(i);
                if (isOwedBill==1){
                    String body = getUrlBody4GetApartmentBill(communityName,address[0],address[1],"0");
                    HttpResponseEntity<?> postReturn = postGo(body, apartmentBillUrl, ShowBillForClientZJGKENTITY.class);
                    ShowBillForClientZJGKENTITY res = (ShowBillForClientZJGKENTITY)postReturn.getBody();
                    responseList.add(res);
                }else{
                    String body1 = getUrlBody4GetApartmentBill(communityName,address[0],address[1],"0");
                    String body2 = getUrlBody4GetApartmentBill(communityName,address[0],address[1],"1");
                    HttpResponseEntity<?> postReturn1 = postGo(body1, apartmentBillUrl, ShowBillForClientZJGKENTITY.class);
                    HttpResponseEntity<?> postReturn2 = postGo(body2, apartmentBillUrl, ShowBillForClientZJGKENTITY.class);
                    ShowBillForClientZJGKENTITY res1 = (ShowBillForClientZJGKENTITY)postReturn1.getBody();
                    ShowBillForClientZJGKENTITY res2 = (ShowBillForClientZJGKENTITY)postReturn2.getBody();
                    responseList.add(res1);
                    responseList.add(res2);
                }
                //将这个楼栋门牌返回的结果放到list中
            }
            //处理完全的返回结果，merge，排序，填充到我的dto中的一部分
            sortedBills = getSortedBills(responseList);
        } else if(targetType == "eh_organization"){
            //拿到enterpriseName
            //调用getCompanyBills
            String organizationName = organizationProvider.getOrganizationNameById(targetId);
            String body = getUrlBody4GetCompanyBills(communityName,organizationName,"");
            HttpResponseEntity<?> entity = postGo(body, companyBillUrl, "", GetCompanyBillsEntity.class);
            GetCompanyBillsEntity res = entity.getBody();
            sortedBills = getSortedBills(res);
        }
        return sortedBills;
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


//            List<BillDTO_zj> billDTOS = responseList.get(i).getResponse().getBillDTOS();
//            for (int j = 0; j<responseList.get(i).getResponse().getBillDTOS())
//            ShowBillForClientZJGKENTITY bill = responseList.get(i);
//            String payFlag = bill.getResponse().getBillDTOS().get(0).getDetails().get(0).getPayFlag();
//            if (payFlag.equals("0")) {
//                BillDetailDTO dto = new BillDetailDTO();
//                dto.setStatus(bill.getResponse().ge);
//                dto.setDateStr();
//                dto.setBillId();
//                dto.setAmountReceviable();
//                dto.setAmountOwed();
//            }
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
    public ShowBillDetailForClientResponse getBillDetailForClient(Long billId) {
        return null;
    }

    @Override
    public ShowBillDetailForClientResponse listBillDetailOnDateChange(Long ownerId, String ownerType, String targetType, Long targetId, String dateStr) {
        return null;
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
                .var("community",communityName)
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

    public HttpResponseEntity<?> postGo(String body,String url,Class T) {
    HttpResponseEntity<?> entity = RestCallTemplate.url(url)
            .body(body)
            .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .respType(T)
            .post();
    return (HttpResponseEntity<?>)entity;
    }
}
