package com.everhomes.contract;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.customer.EnterpriseCustomer;
import com.everhomes.customer.EnterpriseCustomerProvider;
import com.everhomes.rest.asset.PaymentVariable;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.contract.*;
import com.everhomes.rest.openapi.shenzhou.ShenzhouJsonEntity;
import com.everhomes.rest.openapi.shenzhou.ZJContract;
import com.everhomes.rest.openapi.shenzhou.ZJContractDetail;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SignatureHelper;
import com.everhomes.varField.FieldProvider;
import com.everhomes.varField.ScopeFieldItem;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by ying.xiong on 2017/8/14.
 */
@Component(ContractHandler.CONTRACT_PREFIX + 999971)
public class ZJContractHandler implements ContractHandler{
    private final static Logger LOGGER = LoggerFactory.getLogger(ZJContractHandler.class);

    private CloseableHttpClient httpclient = null;
    private static final Integer NAMESPACE_ID = 999971;
    private static final String SEARCH_ENTERPRISE_CONTRACTS = "/openapi/contract/searchEnterpriseContracts";
    private static final String LIST_USER_CONTRACTS = "/openapi/contract/listUserContracts";
    private static final String LIST_CONTRACTS_BY_ENTERPRISE = "/openapi/contract/listContractsByEnterprise";
    private static final String LIST_CONTRACTS_BY_USER = "/openapi/contract/listContractsByUser";
    private static final String GET_ENTERPRISE_CONTRACT_DETAIL = "/openapi/contract/getEnterpriseContractDetail";
    private static final String GET_USER_CONTRACT_DETAIL = "/openapi/contract/getUserContractDetai";

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private CommunityProvider communityProvider;

    @Autowired
    private FieldProvider fieldProvider;

    @Autowired
    private EnterpriseCustomerProvider enterpriseCustomerProvider;

    @Override
    public ListContractsResponse listContracts(ListContractsCommand cmd) {
        String contractStatus  = convertContractStatus(cmd.getStatus());
        String contractAttribute = convertContractAttribute(cmd.getContractType());
        Community community = communityProvider.findCommunityById(cmd.getCommunityId());
        String communityName = community == null ? "" : community.getName();

        ScopeFieldItem item = fieldProvider.findScopeFieldItemByFieldItemId(cmd.getNamespaceId(), cmd.getCategoryItemId());
        String categoryName = item == null ? "" : item.getItemDisplayName();
        Map<String, String> params = generateParams(communityName, contractStatus, contractAttribute, categoryName, cmd.getKeywords());
        StringBuilder sb = new StringBuilder();
        if(community != null && CommunityType.COMMERCIAL.equals(CommunityType.fromCode(community.getCommunityType()))) {
            sb.append(postToShenzhou(params, SEARCH_ENTERPRISE_CONTRACTS, null));
        } else if(community != null && CommunityType.RESIDENTIAL.equals(CommunityType.fromCode(community.getCommunityType()))) {
            sb.append(postToShenzhou(params, LIST_USER_CONTRACTS, null));
        } else {
            sb.append(postToShenzhou(params, SEARCH_ENTERPRISE_CONTRACTS, null));
            sb.append(postToShenzhou(params, LIST_USER_CONTRACTS, null));
        }

        String enterprises = sb.toString();
        ShenzhouJsonEntity<List<ZJContract>> entity = JSONObject.parseObject(enterprises, new TypeReference<ShenzhouJsonEntity<List<ZJContract>>>(){});
        List<ZJContract> contracts = entity.getResponse();
        ListContractsResponse response = new ListContractsResponse();
        if(contracts != null && contracts.size() > 0) {
            List<ContractDTO> dtos = contracts.stream().map(contract -> {
                ContractDTO dto = ConvertHelper.convert(contract, ContractDTO.class);
                dto.setBuildings(contract.getApartments());
                dto.setContractStartDate(strToTimestamp(contract.getContractStartDate()));
                dto.setContractEndDate(strToTimestamp(contract.getContractEndDate()));
                dto.setStatus(convertToContractStatus(contract.getStatus()));
                dto.setContractType(convertToContractAttribute(contract.getContractAttribute()));
                return dto;
            }).collect(Collectors.toList());

            response.setContracts(dtos);
        }
        return response;
    }

    @Override
    public ContractDetailDTO findContract(FindContractCommand cmd) {
        Community community = communityProvider.findCommunityById(cmd.getCommunityId());
        if(community != null) {
            String appKey = configurationProvider.getValue(NAMESPACE_ID, "shenzhoushuma.app.key", "");
            String secretKey = configurationProvider.getValue(NAMESPACE_ID, "shenzhoushuma.secret.key", "");
            Map<String, String> params= new HashMap<String,String>();
            params.put("appKey", appKey);
            params.put("timestamp", ""+System.currentTimeMillis());
            params.put("nonce", ""+(long)(Math.random()*100000));
            params.put("contractNum", cmd.getContractNumber());
            String signature = SignatureHelper.computeSignature(params, secretKey);
            params.put("signature", signature);
            ZJContractDetail zjContract = null;
            if(CommunityType.RESIDENTIAL.equals(CommunityType.fromCode(community.getCommunityType()))) {
                //住宅
                String contracts = postToShenzhou(params, GET_USER_CONTRACT_DETAIL, null);
                zjContract = dealZJContract(contracts);
            } else if(CommunityType.COMMERCIAL.equals(CommunityType.fromCode(community.getCommunityType()))) {
                //商用
                String contracts = postToShenzhou(params, GET_ENTERPRISE_CONTRACT_DETAIL, null);
                zjContract = dealZJContract(contracts);
            }

            if(zjContract != null) {
                ContractDetailDTO dto = convertZJContractDetailToContractDetailDTO(zjContract);
                return dto;
            }
        }
        return null;
    }

    private ContractDetailDTO convertZJContractDetailToContractDetailDTO(ZJContractDetail zjContract) {
        ContractDetailDTO dto = new ContractDetailDTO();
        dto.setContractNumber(zjContract.getContractNum());
        dto.setCustomerName(zjContract.getLessee());
        dto.setRentSize(zjContract.getAreaSize());
        dto.setContractStartDate(zjContract.getContractStartDate());
        dto.setContractEndDate(zjContract.getContractExpireDate());
        dto.setRent(zjContract.getRent());
        dto.setDeposit(zjContract.getDeposit());
        List<ContractChargingItemDTO> items = new ArrayList<>();
        ContractChargingItemDTO item = new ContractChargingItemDTO();
        item.setChargingItemName("物业费");
        PaymentVariable pv = new PaymentVariable();
        pv.setVariableName("物业费单价");
        pv.setVariableValue(zjContract.getPropertyFeeUnit());
        item.setChargingVariables(pv.toString());
        if(zjContract.getApartments() != null && zjContract.getApartments().size() > 0) {
            List<BuildingApartmentDTO> apartments = new ArrayList<>();
            zjContract.getApartments().forEach(apartment -> {
                BuildingApartmentDTO buildingApartmentDTO = new BuildingApartmentDTO();
                buildingApartmentDTO.setApartmentName(apartment.getApartmentName());
                buildingApartmentDTO.setBuildingName(apartment.getBuildingName());
                apartments.add(buildingApartmentDTO);
            });

        }
        return dto;
    }

    private ContractDTO convertZJContractDetailToContractDTO(ZJContractDetail zjContract) {
        ContractDTO dto = new ContractDTO();
        dto.setContractNumber(zjContract.getContractNum());
        dto.setOrganizationName(zjContract.getLessee());
        dto.setContractStartDate(zjContract.getContractStartDate());
        dto.setContractEndDate(zjContract.getContractExpireDate());
        dto.setRent(zjContract.getRent());
        List<ContractChargingItemDTO> items = new ArrayList<>();
        ContractChargingItemDTO item = new ContractChargingItemDTO();
        item.setChargingItemName("物业费");
        PaymentVariable pv = new PaymentVariable();
        pv.setVariableName("物业费单价");
        pv.setVariableValue(zjContract.getPropertyFeeUnit());
        item.setChargingVariables(pv.toString());
        if(zjContract.getApartments() != null && zjContract.getApartments().size() > 0) {
            List<BuildingApartmentDTO> apartments = new ArrayList<>();
            zjContract.getApartments().forEach(apartment -> {
                BuildingApartmentDTO buildingApartmentDTO = new BuildingApartmentDTO();
                buildingApartmentDTO.setApartmentName(apartment.getApartmentName());
                buildingApartmentDTO.setBuildingName(apartment.getBuildingName());
                apartments.add(buildingApartmentDTO);
            });

        }
        return dto;
    }

    private ZJContractDetail dealZJContract(String contract) {
        ShenzhouJsonEntity<ZJContractDetail> entity = JSONObject.parseObject(contract, new TypeReference<ShenzhouJsonEntity<ZJContractDetail>>(){});
        ZJContractDetail zjContract = entity.getResponse();
        return zjContract;
    }

    @Override
    public List<ContractDTO> listEnterpriseCustomerContracts(ListEnterpriseCustomerContractsCommand cmd) {
        EnterpriseCustomer enterpriseCustomer = enterpriseCustomerProvider.findById(cmd.getEnterpriseCustomerId());
        if(enterpriseCustomer != null) {
            String appKey = configurationProvider.getValue(NAMESPACE_ID, "shenzhoushuma.app.key", "");
            String secretKey = configurationProvider.getValue(NAMESPACE_ID, "shenzhoushuma.secret.key", "");
            Map<String, String> params= new HashMap<String,String>();
            params.put("appKey", appKey);
            params.put("timestamp", ""+System.currentTimeMillis());
            params.put("nonce", ""+(long)(Math.random()*100000));
            params.put("enterpriseIdentifier", enterpriseCustomer.getNamespaceCustomerToken());
            String signature = SignatureHelper.computeSignature(params, secretKey);
            params.put("signature", signature);
            String contracts = postToShenzhou(params, LIST_CONTRACTS_BY_ENTERPRISE, null);
            ShenzhouJsonEntity<List<ZJContractDetail>> entity = JSONObject.parseObject(contracts, new TypeReference<ShenzhouJsonEntity<List<ZJContractDetail>>>(){});
            List<ZJContractDetail> zjContracts = entity.getResponse();
            if(zjContracts != null && zjContracts.size() > 0) {
                List<ContractDTO> dtos = new ArrayList<>();
                zjContracts.forEach(zjContract -> {
                    dtos.add(convertZJContractDetailToContractDTO(zjContract));
                });
                return dtos;
            }
        }
        return null;
    }

    @Override
    public List<ContractDTO> listIndividualCustomerContracts(ListIndividualCustomerContractsCommand cmd) {
        String appKey = configurationProvider.getValue(NAMESPACE_ID, "shenzhoushuma.app.key", "");
        String secretKey = configurationProvider.getValue(NAMESPACE_ID, "shenzhoushuma.secret.key", "");
        Map<String, String> params= new HashMap<String,String>();
        params.put("appKey", appKey);
        params.put("timestamp", ""+System.currentTimeMillis());
        params.put("nonce", ""+(long)(Math.random()*100000));
        params.put("userMobile", cmd.getContactToken());
        String signature = SignatureHelper.computeSignature(params, secretKey);
        params.put("signature", signature);
        String contracts = postToShenzhou(params, LIST_CONTRACTS_BY_USER, null);
        ShenzhouJsonEntity<List<ZJContractDetail>> entity = JSONObject.parseObject(contracts, new TypeReference<ShenzhouJsonEntity<List<ZJContractDetail>>>(){});
        List<ZJContractDetail> zjContracts = entity.getResponse();
        if(zjContracts != null && zjContracts.size() > 0) {
            List<ContractDTO> dtos = new ArrayList<>();
            zjContracts.forEach(zjContract -> {
                dtos.add(convertZJContractDetailToContractDTO(zjContract));
            });
            return dtos;
        }
        return null;
    }

    private Map<String, String> generateParams(String communityName, String contractStatus, String contractAttribute,
                                               String category, String contractName) {
        String appKey = configurationProvider.getValue(NAMESPACE_ID, "shenzhoushuma.app.key", "");
        String secretKey = configurationProvider.getValue(NAMESPACE_ID, "shenzhoushuma.secret.key", "");
        Map<String, String> params= new HashMap<String,String>();
        params.put("appKey", appKey);
        params.put("timestamp", ""+System.currentTimeMillis());
        params.put("nonce", ""+(long)(Math.random()*100000));
        params.put("communityName", communityName);
        params.put("contractStatus", contractStatus);
        params.put("contractAttribute", contractAttribute);
        params.put("category", category);
        params.put("contractName", contractName);
        String signature = SignatureHelper.computeSignature(params, secretKey);
        params.put("signature", signature);

        return params;
    }

    private String convertContractAttribute(Byte type) {
        ContractType contractType = ContractType.fromStatus(type);
        switch (contractType) {
            case NEW: return "新签合同";
            case RENEW: return "续约合同";
            case CHANGE: return "变更合同";
            default: return null;
        }
    }

    private Byte convertToContractAttribute(String type) {
        switch (type) {
            case "新签合同": return ContractType.NEW.getCode();
            case "续约合同": return ContractType.RENEW.getCode();
            case "变更合同": return ContractType.CHANGE.getCode();
            default: return null;
        }
    }

    private String convertContractStatus(Byte status) {
        ContractStatus contractStatus = ContractStatus.fromStatus(status);
        switch (contractStatus) {
            case WAITING_FOR_LAUNCH: return "待发起";
            case ACTIVE: return "正常合同";
            case WAITING_FOR_APPROVAL: return "审批中";
            case APPROVE_QUALITIED: return "审批通过";
            case APPROVE_NOT_QUALITIED: return "审批不通过";
            case EXPIRING: return "即将到期";
            case EXPIRED: return "已过期";
            case HISTORY: return "历史合同";
            case INVALID: return "作废合同";
            default: return null;
        }
    }

    private Byte convertToContractStatus(String status) {
        switch (status) {
            case "待发起": return ContractStatus.WAITING_FOR_LAUNCH.getCode();
            case "正常合同": return ContractStatus.ACTIVE.getCode();
            case "审批中": return ContractStatus.WAITING_FOR_APPROVAL.getCode();
            case "审批通过": return ContractStatus.APPROVE_QUALITIED.getCode();
            case "审批不通过": return ContractStatus.APPROVE_NOT_QUALITIED.getCode();
            case "即将到期": return ContractStatus.EXPIRING.getCode();
            case "已过期": return ContractStatus.EXPIRED.getCode();
            case "历史合同": return ContractStatus.HISTORY.getCode();
            case "作废合同": return ContractStatus.INVALID.getCode();

            default: return null;
        }
    }

    private String postToShenzhou(Map<String, String> params, String method, Map<String, String> headers) {

        String shenzhouUrl = configurationProvider.getValue(NAMESPACE_ID, "shenzhou.host.url", "");
        HttpPost httpPost = new HttpPost(shenzhouUrl + method);
        CloseableHttpResponse response = null;

        String json = null;
        try {
            StringEntity stringEntity = new StringEntity(params.toString(), "utf8");
            httpPost.setEntity(stringEntity);

            response = httpclient.execute(httpPost);

            int status = response.getStatusLine().getStatusCode();
            if(status == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    json = EntityUtils.toString(entity, "utf8");
                }
            }

        } catch (Exception e) {
            LOGGER.error("sync from shenzhou request error, param={}", params, e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "sync from shenzhou request error.");
        } finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    LOGGER.error("sync from shenzhou close instream, response error, param={}", params, e);
                }
            }
        }

        if(LOGGER.isDebugEnabled())
            LOGGER.debug("Data from shenzhou, param={}, json={}", params, json);

        return json;
    }

    private Timestamp strToTimestamp(String str) {
        Timestamp ts = null;
        try {
            ts = new Timestamp(sdf.parse(str).getTime());
        } catch (ParseException e) {
            LOGGER.error("validityPeriod data format is not yyyymmdd.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "validityPeriod data format is not yyyymmdd.");
        }

        return ts;
    }
}
