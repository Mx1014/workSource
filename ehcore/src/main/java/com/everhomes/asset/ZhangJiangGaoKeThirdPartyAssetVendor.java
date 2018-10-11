//@formatter:off
package com.everhomes.asset;

import static com.everhomes.util.SignatureHelper.computeSignature;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.everhomes.asset.group.AssetGroupProvider;
import com.everhomes.asset.zjgkVOs.BillCountResponse;
import com.everhomes.asset.zjgkVOs.BillDetailResponse;
import com.everhomes.asset.zjgkVOs.CommunityAddressDTO;
import com.everhomes.asset.zjgkVOs.ContractBillResponse;
import com.everhomes.asset.zjgkVOs.ContractBillsDTO;
import com.everhomes.asset.zjgkVOs.ContractBillsStatDTO;
import com.everhomes.asset.zjgkVOs.ContractDTO;
import com.everhomes.asset.zjgkVOs.ContractDetailResponse;
import com.everhomes.asset.zjgkVOs.ContractListResponse;
import com.everhomes.asset.zjgkVOs.PaymentStatus;
import com.everhomes.asset.zjgkVOs.SearchBillsResponse;
import com.everhomes.asset.zjgkVOs.SearchEnterpriseBillsDTO;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.equipment.EquipmentService;
import com.everhomes.http.HttpUtils;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.asset.AssetBillStatDTO;
import com.everhomes.rest.asset.AssetBillTemplateValueDTO;
import com.everhomes.rest.asset.AssetTargetType;
import com.everhomes.rest.asset.BillDTO;
import com.everhomes.rest.asset.AssetBillStatDTO;
import com.everhomes.rest.asset.AssetBillTemplateValueDTO;
import com.everhomes.rest.asset.AssetTargetType;
import com.everhomes.rest.asset.BillDTO;
import com.everhomes.rest.asset.BillDetailDTO;
import com.everhomes.rest.asset.BillIdAndAmount;
import com.everhomes.rest.asset.BillIdAndType;
import com.everhomes.rest.asset.BillIdCommand;
import com.everhomes.rest.asset.BillItemIdCommand;
import com.everhomes.rest.asset.BillStaticsCommand;
import com.everhomes.rest.asset.BillStaticsDTO;
import com.everhomes.rest.asset.CreateBillCommand;
import com.everhomes.rest.asset.ExemptionItemIdCommand;
import com.everhomes.rest.asset.ExportBillTemplatesCommand;
import com.everhomes.rest.asset.FindUserInfoForPaymentCommand;
import com.everhomes.rest.asset.FindUserInfoForPaymentDTO;
import com.everhomes.rest.asset.FindUserInfoForPaymentResponse;
import com.everhomes.rest.asset.GetAreaAndAddressByContractCommand;
import com.everhomes.rest.asset.GetAreaAndAddressByContractDTO;
import com.everhomes.rest.asset.ListAllBillsForClientCommand;
import com.everhomes.rest.asset.ListAllBillsForClientDTO;
import com.everhomes.rest.asset.ListBillDetailCommand;
import com.everhomes.rest.asset.ListBillDetailResponse;
import com.everhomes.rest.asset.ListBillExpectanciesOnContractCommand;
import com.everhomes.rest.asset.ListBillGroupsDTO;
import com.everhomes.rest.asset.ListBillItemsResponse;
import com.everhomes.rest.asset.ListBillsCommand;
import com.everhomes.rest.asset.ListBillsDTO;
import com.everhomes.rest.asset.ListBillsResponse;
import com.everhomes.rest.asset.ListPaymentBillResp;
import com.everhomes.rest.asset.ListSettledBillExemptionItemsResponse;
import com.everhomes.rest.asset.ListSimpleAssetBillsResponse;
import com.everhomes.rest.asset.PaymentExpectanciesResponse;
import com.everhomes.rest.asset.PlaceAnAssetOrderCommand;
import com.everhomes.rest.asset.ShowBillDetailForClientDTO;
import com.everhomes.rest.asset.ShowBillDetailForClientResponse;
import com.everhomes.rest.asset.ShowBillForClientDTO;
import com.everhomes.rest.asset.ShowBillForClientV2Command;
import com.everhomes.rest.asset.ShowBillForClientV2DTO;
import com.everhomes.rest.asset.ShowCreateBillDTO;
import com.everhomes.rest.asset.listBillExemtionItemsCommand;
import com.everhomes.rest.asset.listBillRelatedTransacCommand;
import com.everhomes.rest.asset.BillIdAndType;
import com.everhomes.rest.asset.BillIdCommand;
import com.everhomes.rest.asset.BillItemIdCommand;
import com.everhomes.rest.asset.BillStaticsCommand;
import com.everhomes.rest.asset.BillStaticsDTO;
import com.everhomes.rest.asset.CreateBillCommand;
import com.everhomes.rest.asset.ExemptionItemIdCommand;
import com.everhomes.rest.asset.ExportBillTemplatesCommand;
import com.everhomes.rest.asset.FindUserInfoForPaymentCommand;
import com.everhomes.rest.asset.FindUserInfoForPaymentDTO;
import com.everhomes.rest.asset.FindUserInfoForPaymentResponse;
import com.everhomes.rest.asset.GetAreaAndAddressByContractCommand;
import com.everhomes.rest.asset.GetAreaAndAddressByContractDTO;
import com.everhomes.rest.asset.ListAllBillsForClientCommand;
import com.everhomes.rest.asset.ListAllBillsForClientDTO;
import com.everhomes.rest.asset.ListBillDetailCommand;
import com.everhomes.rest.asset.ListBillDetailResponse;
import com.everhomes.rest.asset.ListBillExpectanciesOnContractCommand;
import com.everhomes.rest.asset.ListBillGroupsDTO;
import com.everhomes.rest.asset.ListBillItemsResponse;
import com.everhomes.rest.asset.ListBillsCommand;
import com.everhomes.rest.asset.ListBillsDTO;
import com.everhomes.rest.asset.ListBillsResponse;
import com.everhomes.rest.asset.ListPaymentBillResp;
import com.everhomes.rest.asset.ListSettledBillExemptionItemsResponse;
import com.everhomes.rest.asset.ListSimpleAssetBillsResponse;
import com.everhomes.rest.asset.PaymentExpectanciesResponse;
import com.everhomes.rest.asset.ShowBillDetailForClientDTO;
import com.everhomes.rest.asset.ShowBillDetailForClientResponse;
import com.everhomes.rest.asset.ShowBillForClientDTO;
import com.everhomes.rest.asset.ShowBillForClientV2Command;
import com.everhomes.rest.asset.ShowBillForClientV2DTO;
import com.everhomes.rest.asset.ShowCreateBillDTO;
import com.everhomes.rest.asset.listBillExemtionItemsCommand;
import com.everhomes.rest.asset.listBillRelatedTransacCommand;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import static com.everhomes.util.SignatureHelper.computeSignature;

/**
 * Created by Wentian Wang on 2017/8/10.
 */

@Component
public class ZhangJiangGaoKeThirdPartyAssetVendor extends AssetVendorHandler{

    private static final Logger LOGGER = LoggerFactory.getLogger(ZhangJiangGaoKeThirdPartyAssetVendor.class);

    @Autowired
    private AssetProvider assetProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private EquipmentService equipmentService;
    
    @Autowired
    private AssetGroupProvider assetGroupProvider;
    

    @Override
    public ShowBillForClientDTO showBillForClient(Long ownerId, String ownerType, String targetType, Long targetId, Long billGroupId,Byte isOwedBill,String contractNum, Integer namespaceID) {
        ShowBillForClientDTO finalDto = new ShowBillForClientDTO();
        PaymentBillGroup group = assetGroupProvider.getBillGroupById(billGroupId);
        if(!group.getName().equals("租金")) return finalDto;
        List<BillDetailDTO> dtos = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");
        //用时间区分待缴
        String dateStrEnd = "";
        if(isOwedBill==1 || isOwedBill==0){
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            Calendar c3 = Calendar.getInstance();
            c2.add(Calendar.DAY_OF_MONTH,15);
            c3.add(Calendar.MONTH,1);
            c3.set(Calendar.DAY_OF_MONTH,c3.getActualMinimum(Calendar.DAY_OF_MONTH));
            if(c2.compareTo(c3) != -1){
                c1 = c3;
            }
            dateStrEnd = sdf.format(c1.getTime());
        }
        //找合计
        String postJson = "";
        Map<String, String> params=new HashMap<String, String> ();
        params.put("payFlag", "0");
        params.put("sdateFrom","");
        params.put("sdateTo",dateStrEnd);
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
        if(isOwedBill==1){
            payFlag="0";
        }else if(isOwedBill==0){
            payFlag="";
        }
        params.put("payFlag", payFlag);
        params.put("sdateFrom","");
        params.put("sdateTo",isOwedBill==1?dateStrEnd:"");
        params.put("pageOffset","1");
        params.put("pageSize",String.valueOf(Integer.MAX_VALUE));
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
//                fakeData(res,targetType);
                for(int i = 0; i < res.size(); i++){
                    ContractBillsDTO sourceDto = res.get(i);
                    BillDetailDTO dto = new BillDetailDTO();
                    dto.setAmountOwed(sourceDto.getAmountOwed()==null?null:new BigDecimal(sourceDto.getAmountOwed()));
                    dto.setAmountReceviable(sourceDto.getAmountReceivable()==null?null:new BigDecimal(sourceDto.getAmountReceivable()));
                    dto.setBillId(sourceDto.getBillID());
                    dto.setDateStr(sourceDto.getBillDate());
                    dto.setDateStrBegin(sourceDto.getDateStrBegin());
                    dto.setDateStrEnd(sourceDto.getDateStrEnd());
                    Byte billStatus = sourceDto.getPayFlag();
                    try{
                        Calendar local = Calendar.getInstance();

                        String dateStrBegin = sourceDto.getDateStrBegin();
                        Calendar dateBegin = Calendar.getInstance();
                        dateBegin.setTime(yyyy_MM_dd.parse(dateStrBegin));

                        if(targetType.equals(AssetPaymentConstants.EH_USER)){
                            Calendar localPlus15 = Calendar.getInstance();
                            localPlus15.add(Calendar.DAY_OF_MONTH,15);
                            //0:待缴；payflag为0，本地时间加15天大于等于 账期所在月,本地时间小于账期
                            if(billStatus == 0 && (local.compareTo(dateBegin)==-1 && localPlus15.compareTo(dateBegin)!=-1)){
                                billStatus = 0;
                            }else if(billStatus == 1){
                                // 1：已缴；payflag为1
                                billStatus = 1;
                            }else if(billStatus == 0 && dateBegin.compareTo(local)==-1){
                                // 2：欠费；payfalg为0，计费开始时间小于本地时间
                                billStatus = 2;
                            }else if(billStatus == 0 && dateBegin.compareTo(localPlus15)==1){
                                // 3：未缴，payflag为0，日期大于本地时间15天以上
                                billStatus = 3;
                            }
                        }else if(targetType.equals(AssetPaymentConstants.EH_ORGANIZATION)){
                            Calendar beginPlus10 = Calendar.getInstance();
                            beginPlus10.setTime(dateBegin.getTime());
                            beginPlus10.add(Calendar.DAY_OF_MONTH,10);
                            //0:待缴；payflag为0，本地时间处于账期开始和10天的区间之内
                            if(billStatus == 0 && (local.compareTo(dateBegin)!=-1 && local.compareTo(beginPlus10)!=1)){
                                billStatus = 0;
                            }else if(billStatus == 1){
                                // 1：已缴；payflag为1
                                billStatus = 1;
                            }else if(billStatus == 0 && local.compareTo(beginPlus10)==1){
                                // 2：欠费；payfalg为0，计费开始时间小于本地时间
                                billStatus = 2;
                            }else if(billStatus == 0 && local.compareTo(dateBegin)==-1){
                                // 3：未缴，payflag为0，日期大于本地时间15天以上
                                billStatus = 3;
                            }
                        }


                    }catch (Exception e){
                        LOGGER.error("billStatus parse failed");
                    }
                    dto.setStatus(billStatus);
                    String szsm_status = sourceDto.getStatus();
                    if(szsm_status.equals(PaymentStatus.SUSPEND.getCode())){
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

    private void fakeData(List<ContractBillsDTO> res,String targetType) {
        res = new ArrayList<>();



        if(targetType.equals(AssetPaymentConstants.EH_ORGANIZATION)){

            ContractBillsDTO dtos[] = new ContractBillsDTO[4];
            for(int i = 0; i < dtos.length; i++) {
                ContractBillsDTO dto = new ContractBillsDTO();
                dto.setCustomerName("账单"+i);
                dto.setFeeName("租金");
                dtos[i] = dto;
            }
            // local 待缴 10天
            ContractBillsDTO dto = dtos[0];
            dto.setAmountOwed("1000");
            dto.setDateStrBegin("2017-11-25");
            dto.setDateStrEnd("2017-12-25");
            dto.setPayFlag((byte)0);
            dto.setStatus("未缴");
            res.add(dto);
            // local  10天 欠费
            ContractBillsDTO dto1 = dtos[1];
            dto1.setAmountOwed("1000");
            dto1.setDateStrBegin("2017-11-10");
            dto1.setDateStrBegin("2017-12-10");
            dto1.setPayFlag((byte)0);
            dto1.setStatus("未缴");
            res.add(dto1);
            //   local  10天  未缴
            ContractBillsDTO dto2 = dtos[2];
            dto2.setAmountOwed("1000");
            dto2.setDateStrBegin("2017-12-05");
            dto2.setDateStrBegin("2018-01-05");
            dto2.setPayFlag((byte)0);
            dto2.setStatus("未缴");
            res.add(dto2);
            // 已缴
            ContractBillsDTO dto3 = dtos[3];
            dto3.setAmountOwed("1000");
            dto3.setDateStrBegin("2017-10-05");
            dto3.setDateStrBegin("2017-11-05");
            dto3.setPayFlag((byte)1);
            dto3.setStatus("已缴纳");
            res.add(dto3);
        }else if(targetType.equals(AssetPaymentConstants.EH_USER)){

            ContractBillsDTO dtos[] = new ContractBillsDTO[4];
            for(int i = 0; i < dtos.length; i++) {
                ContractBillsDTO dto = new ContractBillsDTO();
                dto.setCustomerName("账单"+i);
                dto.setFeeName("租金");
                dtos[i] = dto;
            }
            // local 待缴 10天
            ContractBillsDTO dto = dtos[0];
            dto.setAmountOwed("1000");
            dto.setDateStrBegin("2017-12-05");
            dto.setDateStrEnd("2018-01-25");
            dto.setPayFlag((byte)0);
            dto.setStatus("未缴");
            res.add(dto);
            // local  10天 欠费
            ContractBillsDTO dto1 = dtos[1];
            dto1.setAmountOwed("1000");
            dto1.setDateStrBegin("2017-11-10");
            dto1.setDateStrBegin("2017-12-10");
            dto1.setPayFlag((byte)0);
            dto1.setStatus("未缴");
            res.add(dto1);
            //   local  10天  未缴
            ContractBillsDTO dto2 = dtos[2];
            dto2.setAmountOwed("1000");
            dto2.setDateStrBegin("2018-01-05");
            dto2.setDateStrBegin("2018-02-05");
            dto2.setPayFlag((byte)0);
            dto2.setStatus("未缴");
            res.add(dto2);
            // 已缴
            ContractBillsDTO dto3 = dtos[3];
            dto3.setAmountOwed("1000");
            dto3.setDateStrBegin("2017-10-05");
            dto3.setDateStrBegin("2017-11-05");
            dto3.setPayFlag((byte)1);
            dto3.setStatus("已缴纳");
            res.add(dto3);


        }
    }

    @Override
    public ShowBillDetailForClientResponse getBillDetailForClient(Long ownerId, String billId,String targetType,Long organizationId) {
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
                TreeSet<Date> dates = new TreeSet<>();
                SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
                for(int i = 0 ; i < dtos.size(); i++){
                    com.everhomes.asset.zjgkVOs.BillDetailDTO sourceDto = dtos.get(i);
                    ShowBillDetailForClientDTO dto = new ShowBillDetailForClientDTO();
                    dto.setBillItemName(StringUtils.isEmpty(sourceDto.getFeeName())==true?"租金":sourceDto.getFeeName());
                    dto.setAmountOwed(sourceDto.getAmountOwed()==null?null:new BigDecimal(sourceDto.getAmountOwed()));
                    dto.setAmountReceivable(sourceDto.getAmountReceivable()==null?null:new BigDecimal(sourceDto.getAmountReceivable()));
                    amountOwed = amountOwed.add(sourceDto.getAmountOwed()==null?new BigDecimal("0"):new BigDecimal(sourceDto.getAmountOwed()));
                    amountReceivable = amountReceivable.add(sourceDto.getAmountReceivable()==null?new BigDecimal("0"):new BigDecimal(sourceDto.getAmountReceivable()));
                    List<CommunityAddressDTO> apartments = sourceDto.getApartments();
                    String buildingName = null;
                    String apartmentName = null;
                    if(apartments!=null&&apartments.size()>0){
                        buildingName = apartments.get(0).getBuildingName()==null?"":apartments.get(0).getBuildingName();
                        apartmentName = apartments.get(0).getApartmentName()==null?"":apartments.get(0).getApartmentName();
                    }
                    dto.setAddressName(buildingName==null?"":buildingName+apartmentName==null?"":apartmentName);
                    dto.setPayStatus(sourceDto.getStatus()!=null?sourceDto.getStatus().equals(PaymentStatus.SUSPEND.getCode())?PaymentStatus.IN_PROCESS.getCode():null:null);
                    dto.setDateStr(sourceDto.getBillDate());
                    dto.setDateStrBegin(sourceDto.getDateStrBegin());
                    dto.setDateStrEnd(sourceDto.getDateStrEnd());
                    try{
                        dates.add(yyyyMMdd.parse(sourceDto.getDateStrBegin()));
                        dates.add((yyyyMMdd.parse(sourceDto.getDateStrEnd())));
                    }catch (Exception e){
                    }
//                    result.setDatestr(sourceDto.getBillDate());
                    list.add(dto);
                }
                if(dates.first()!=null && dates.last()!=null){
                    result.setDatestr(yyyyMMdd.format(dates.first())+"至"+yyyyMMdd.format(dates.last()));
                }
                result.setAmountOwed(amountOwed);
                result.setAmountReceivable(amountReceivable);
                result.setShowBillDetailForClientDTOList(list);
            }
        }
        return result;
    }

    @Override
    public ShowBillDetailForClientResponse listBillDetailOnDateChange(Byte billStatus, Long ownerId, String ownerType, String targetType, Long targetId, String dateStr,String contractId, Long billGroupId) {
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
//                    result.setAmountReceivable(res.getAmountTotalOwed()==null?null:new BigDecimal(res.getAmountTotalOwed()));
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
        BigDecimal amountReceivable = new BigDecimal("0");
        if(postJson!=null&&postJson.trim().length()>0){
            ContractBillResponse response = (ContractBillResponse)StringHelper.fromJsonString(postJson, ContractBillResponse.class);
            if(response.getErrorCode()==200){
                List<ContractBillsDTO> res = response.getResponse();
                for(int i = 0; i < res.size(); i++){
                    ContractBillsDTO sourceDto = res.get(i);
                    ShowBillDetailForClientDTO dto = new ShowBillDetailForClientDTO();
                    dto.setAmountOwed(sourceDto.getAmountOwed()==null?null:new BigDecimal(sourceDto.getAmountOwed()));
                    dto.setAmountReceivable(sourceDto.getAmountReceivable()==null?null:new BigDecimal(sourceDto.getAmountReceivable()));
                    amountReceivable = amountReceivable.add(sourceDto.getAmountReceivable()==null?new BigDecimal("0"):new BigDecimal(sourceDto.getAmountReceivable()));
                    String buildingName = "";
                    String apartmentName = "";
                    if(sourceDto.getApartments()!=null&& sourceDto.getApartments().size()>0){
                        CommunityAddressDTO communityAddressDTO = sourceDto.getApartments().get(0);
                        buildingName = communityAddressDTO.getBuildingName();
                        apartmentName = communityAddressDTO.getApartmentName();
                    }
                    dto.setAddressName(buildingName+apartmentName);
                    dto.setBillItemName(sourceDto.getFeeName());
                    dto.setDateStr(sourceDto.getBillDate());
                    dto.setDateStrBegin(sourceDto.getDateStrBegin());
                    dto.setDateStrEnd(sourceDto.getDateStrEnd());
                    list.add(dto);
                }
            }else{
                LOGGER.error("调用张江高科searchEnterpriseBills失败"+response.getErrorDescription()+","+response.getErrorDetails());
            }
        }
        result.setAmountReceivable(amountReceivable);
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
                LOGGER.error("zjgkhandler organization_identifier is null");
//                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
//                        "Insufficient privilege");
            }
            params.put("enterpriseIdentifier",identifier);
        }else if(targetType!=null && targetType.equals(AssetTargetType.USER.getCode())){
            url = ZjgkUrls.USER_CONTRACT_LIST;
            identifier = assetProvider.findIdentifierByUid(UserContext.currentUserId());
            //测试
            if(identifier==null){
                LOGGER.error("zjgkhandler userMobile is null");
//                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
//                        "Insufficient privilege");
            }
            params.put("userMobile",identifier);
        }else{
            throw new RuntimeException("查询账单传递了不正确的客户类型"+targetType+",个人应该为eh_user，企业为eh_organization");
        }
        String json = generateJson(params);
        try {
            LOGGER.error("zjgk request json is "+json);
            postJson = HttpUtils.postJson(url, json, 120, HTTP.UTF_8);
        } catch (IOException e) {
            LOGGER.error("Request ShenZhouShuMa Failed"+e);
            LOGGER.error("zjgk response is "+postJson);
//            throw new RuntimeException("Request ShenZhouShuMa Failed"+e);
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
//                throw new RuntimeException("Request ShenZhouShuMa Failed"+response.getErrorDescription());
                String customerName = null;
                if(targetType.equals(AssetTargetType.ORGANIZATION.getCode())){
                    Organization organizationById = organizationProvider.findOrganizationById(cmd.getTargetId());
                    if(organizationById != null){
                        customerName = organizationById.getName();
                    }
                }else if(targetType.equals(AssetTargetType.USER.getCode())){
                    // 一定有用户，用户不会为null
                    customerName = UserContext.current().getUser().getNickName();
                }
                result.setCustomerName(customerName);
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
    public void deleteBill(String l, Long billGroupId) {
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
    public List<ShowBillForClientV2DTO> showBillForClientV2(ShowBillForClientV2Command cmd) {
        return null;
    }

    @Override
    public List<ListAllBillsForClientDTO> listAllBillsForClient(ListAllBillsForClientCommand cmd) {
        return null;
    }

    @Override
    public void exportBillTemplates(ExportBillTemplatesCommand cmd, HttpServletResponse response) {
        LOGGER.error("Insufficient privilege, zjgkhandler showCreateBill");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public ListPaymentBillResp listBillRelatedTransac(listBillRelatedTransacCommand cmd) {
        LOGGER.error("Insufficient privilege, zjgkhandler showCreateBill");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
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
    public List<ListBillsDTO> listBills(Integer currentNamespaceId,ListBillsResponse carrier, ListBillsCommand cmd) {
        //修改传递参数为一个对象，卸货
        String contractNum = cmd.getContractNum();
        Long ownerId = cmd.getOwnerId();
        String ownerType = cmd.getOwnerType();
        String buildingName = cmd.getBuildingName();
        String apartmentName = cmd.getApartmentName();
        Long addressId = cmd.getAddressId();
        String billGroupName = cmd.getBillGroupName();
        Long billGroupId = cmd.getBillGroupId();
        Byte billStatus = cmd.getBillStatus();
        String dateStrBegin = cmd.getDateStrBegin();
        String dateStrEnd = cmd.getDateStrEnd();
        Long pageAnchor = cmd.getPageAnchor();
        Integer pageSize = cmd.getPageSize();
        String targetName = cmd.getTargetName();
        Byte status = cmd.getStatus();
        String targetType = cmd.getTargetType();
        //卸货完毕
        if(pageAnchor ==null || pageAnchor == 0l){
            pageAnchor = 1l;
        }
        if(pageSize == null){
            pageSize = 20;
        }
        Long orgId = 0L;
        // asset zuolin base
        OrganizationDTO organization = equipmentService.getAuthOrgByProjectIdAndModuleId(ownerId, cmd.getNamespaceId(), PrivilegeConstants.ASSET_MODULE_ID);
        if(organization!=null)
            orgId = organization.getId();
        List<ListBillGroupsDTO> listBillGroupsDTOS = assetGroupProvider.listBillGroups(ownerId, ownerType, cmd.getCategoryId(), orgId, false);
        List<ListBillsDTO> list = new ArrayList<>();
        if(status!=1){
            LOGGER.error("Insufficient privilege, zjgkhandler listNotSettledBills");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                    "Insufficient privilege");
        }
        String zjgk_communityIdentifier = assetProvider.findZjgkCommunityIdentifierById(ownerId);
        if(StringUtils.isEmpty(zjgk_communityIdentifier)){
            LOGGER.error("Zjgk community id is empty, ownerType={}, ownerId={}, result={}", ownerType, ownerId, zjgk_communityIdentifier);
            throw RuntimeErrorException.errorWith("zjgk", 9999712,
                    "该园区暂没有和系统对接，无法查询");
        } else {
            LOGGER.info("Find zjgk community id from db, ownerType={}, ownerId={}, result={}", ownerType, ownerId, zjgk_communityIdentifier);
        }
        for(int i = 0; i < listBillGroupsDTOS.size(); i++){
            ListBillGroupsDTO billGroup = listBillGroupsDTOS.get(i);
            if(billGroupId == null || (billGroup.getBillGroupId()==billGroupId && billGroup.getBillGroupName().equals("租金"))){
                String postJson = "";
                Map<String, String> params=new HashMap<String, String> ();
                params.put("customerName", StringUtils.isEmpty(targetName)==true?"":targetName);
                params.put("communityIdentifer", StringUtils.isEmpty(zjgk_communityIdentifier)==true?"":zjgk_communityIdentifier);
                params.put("buildingIdentifier", StringUtils.isEmpty(buildingName)==true?"":String.valueOf(buildingName));
                params.put("apartmentIdentifier", StringUtils.isEmpty(apartmentName)==true?"":String.valueOf(apartmentName));
                params.put("payFlag", billStatus==null?"":String.valueOf(billStatus));
                params.put("sdateFrom",StringUtils.isEmpty(dateStrBegin)==true?"":dateStrBegin);
                params.put("sdateTo",StringUtils.isEmpty(dateStrEnd)==true?"":dateStrEnd);
                params.put("pageOffset",pageAnchor==null?"":String.valueOf(pageAnchor));
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
                    params.put("pageOffset",pageAnchor==null?"":String.valueOf(pageAnchor));
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
                        Long next = pageAnchor+1l;
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
            }
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
                    //dto.setNoticeTelList(phones.toString());
                    dto.setNoticeTelList(Arrays.asList(phones.toString().split(",")));
                    dto.setBillId(sourceDto.getBillID());
                    dto.setBillGroupName(sourceDto.getFeeName()==null?"租金":sourceDto.getFeeName());
//                    dto.setDateStr(sourceDto.getBillDate());
                    dto.setDateStr(sourceDto.getDateStrBegin()+"~"+sourceDto.getDateStrEnd());
                    dto.setTargetType(RelTargetType);
                    dto.setTargetName(sourceDto.getCustomerName());
                    dto.setAmountOwed(new BigDecimal(sourceDto.getAmountOwed()));
                    dto.setAmountReceivable(new BigDecimal(sourceDto.getAmountReceivable()));
                    dto.setAmountReceived(new BigDecimal(sourceDto.getAmountReceived()));
                    String szsm_status = sourceDto.getStatus();
                    if(szsm_status.equals(PaymentStatus.SUSPEND.getCode())){
                        dto.setPayStatus(PaymentStatus.IN_PROCESS.getCode());
                    }
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
    public List<BillDTO> listBillItems(String targetType,String billId, String targetName, Integer pageOffSet, Integer pageSize,Long ownerId, ListBillItemsResponse res, Long billGroupId) {
        if (pageOffSet == null) {
            pageOffSet = 1;
        }
        if(pageSize == null){
            pageSize = 20;
        }
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
//                    dto.setDateStr(sourceDto.getBillDate());
                    dto.setDateStr(sourceDto.getDateStrBegin()+"~"+sourceDto.getDateStrEnd());
//                    dto.setDefaultOrder();
                    dto.setTargetId(sourceDto.getCustomerIdentifier());
                    dto.setTargetName(sourceDto.getCustomerName());
                    dto.setTargetType(targetType);
                    dto.setPayStatus(sourceDto.getStatus()!=null?sourceDto.getStatus().equals(PaymentStatus.SUSPEND.getCode())?PaymentStatus.IN_PROCESS.getCode():null:null);
                    //增加计费周期
//                    dto.setDateStrBegin();
//                    dto.setDateStrEnd();
                    list.add(dto);
                }
            }
        }
        return list;
    }

    @Override
    public List<NoticeInfo> listNoticeInfoByBillId(List<BillIdAndType> billIdAndTypes, Long billGroupId) {
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
                    //过滤正在审核中的客户
                    for(int k = 0; k < dtos.size(); k++){
                        com.everhomes.asset.zjgkVOs.BillDetailDTO dto = dtos.get(k);
                        if(dto.getStatus().equals(PaymentStatus.SUSPEND.getCode())){
                            dtos.remove(k);
                            k--;
                        }
                    }
                    //过滤后的账单才是欠费的账单
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

    private void check(String fieldValue, String fieldName){
        if(fieldValue==null||fieldValue.trim().length()<1){
            LOGGER.error(fieldName+":"+fieldValue+"为必填，不能为空");
            throw new RuntimeException(fieldName+":"+fieldValue+"为必填，不能为空");
        }
    }



}

