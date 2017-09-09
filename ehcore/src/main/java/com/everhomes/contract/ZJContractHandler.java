package com.everhomes.contract;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.customer.EnterpriseCustomer;
import com.everhomes.customer.EnterpriseCustomerProvider;
import com.everhomes.http.HttpUtils;
import com.everhomes.rest.asset.PaymentVariable;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.contract.*;
import com.everhomes.rest.customer.CustomerType;
import com.everhomes.rest.openapi.shenzhou.ShenzhouJsonEntity;
import com.everhomes.rest.openapi.shenzhou.ZJContract;
import com.everhomes.rest.openapi.shenzhou.ZJContractDetail;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SignatureHelper;
import com.everhomes.util.StringHelper;
import com.everhomes.varField.FieldProvider;
import com.everhomes.varField.ScopeFieldItem;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HTTP;
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
@Component(ContractService.CONTRACT_PREFIX + 999971)
public class ZJContractHandler implements ContractService{
    private final static Logger LOGGER = LoggerFactory.getLogger(ZJContractHandler.class);

    private CloseableHttpClient httpclient = null;
    private static final Integer NAMESPACE_ID = 999971;
    private static final String SEARCH_ENTERPRISE_CONTRACTS = "/openapi/contract/searchEnterpriseContracts";
    private static final String LIST_USER_CONTRACTS = "/openapi/contract/listUserContracts";
    private static final String LIST_CONTRACTS_BY_ENTERPRISE = "/openapi/contract/listContractsByEnterprise";
    private static final String LIST_CONTRACTS_BY_USER = "/openapi/contract/listContractsByUser";
    private static final String GET_ENTERPRISE_CONTRACT_DETAIL = "/openapi/contract/getEnterpriseContractDetail";
    private static final String GET_USER_CONTRACT_DETAIL = "/openapi/contract/getUserContractDetail";

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private CommunityProvider communityProvider;

    @Autowired
    private FieldProvider fieldProvider;

    @Autowired
    private EnterpriseCustomerProvider enterpriseCustomerProvider;

    @Autowired
    private UserProvider userProvider;

    @Override
    public ListContractsResponse listContracts(ListContractsCommand cmd) {
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("zjgk listContracts. cmd:{}", cmd);
        }
        String contractStatus  = convertContractStatus(cmd.getStatus());
        String contractAttribute = convertContractAttribute(cmd.getContractType());
        Community community = communityProvider.findCommunityById(cmd.getCommunityId());
        String communityIdentifier = community == null ? "" : community.getNamespaceCommunityToken();
        String pageOffset = cmd.getPageAnchor() == null ? "" : cmd.getPageAnchor().toString();
        String pageSize = cmd.getPageSize() == null ? "" : cmd.getPageSize().toString();

        ScopeFieldItem item = fieldProvider.findScopeFieldItemByFieldItemId(cmd.getNamespaceId(), cmd.getCategoryItemId());
        String categoryName = item == null ? "" : item.getItemDisplayName();
        Map<String, String> params = generateParams(communityIdentifier, contractStatus, contractAttribute, categoryName, cmd.getKeywords(), pageOffset, pageSize);
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
                if(dto.getContractNumber() == null) {
                    dto.setContractNumber(contract.getContracId());
                }
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
    public void contractSchedule() {

    }

    @Override
    public ListContractsResponse listContractsByOraganizationId(ListContractsByOraganizationIdCommand cmd) {
        return null;
    }

    @Override
    public List<Object> findCustomerByContractNum(String contractNum, Long ownerId, String ownerType) {
        return null;
    }

    @Override
    public ContractDetailDTO createContract(CreateContractCommand cmd) {
        LOGGER.error("Insufficient privilege, zjgkhandler createContract");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public ContractDTO updateContract(UpdateContractCommand cmd) {
        LOGGER.error("Insufficient privilege, zjgkhandler createContract");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public void denunciationContract(DenunciationContractCommand cmd) {
        LOGGER.error("Insufficient privilege, zjgkhandler createContract");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public ContractDetailDTO findContract(FindContractCommand cmd) {
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("zjgk findContract. cmd:{}", cmd);
        }
        Community community = communityProvider.findCommunityById(cmd.getCommunityId());
        if(community != null) {
            String appKey = configurationProvider.getValue(NAMESPACE_ID, "shenzhoushuma.app.key", "");
            String secretKey = configurationProvider.getValue(NAMESPACE_ID, "shenzhoushuma.secret.key", "");
            Map<String, String> params= new HashMap<String,String>();
            params.put("appKey", appKey);
            params.put("timestamp", ""+System.currentTimeMillis());
            params.put("nonce", ""+(long)(Math.random()*100000));
            params.put("crypto", "sssss");
//            params.put("contractNum", "T1716170622");
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

    @Override
    public void deleteContract(DeleteContractCommand cmd) {
        LOGGER.error("Insufficient privilege, zjgkhandler createContract");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public List<ContractDTO> listCustomerContracts(ListCustomerContractsCommand cmd) {
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("zjgk listCustomerContracts. cmd:{}", cmd);
        }
        UserIdentifier userIdentifier = userProvider.findUserIdentifiersOfUser(cmd.getTargetId(), cmd.getNamespaceId());
        if(CustomerType.INDIVIDUAL.equals(CustomerType.fromStatus(cmd.getTargetType()))) {
            ListIndividualCustomerContractsCommand command = new ListIndividualCustomerContractsCommand();
            command.setNamespaceId(cmd.getNamespaceId());
            command.setCommunityId(cmd.getCommunityId());
            command.setContactToken(userIdentifier.getIdentifierToken());
            List<ContractDTO> response = listIndividualCustomerContracts(command);
            return response;
        } else if(CustomerType.ENTERPRISE.equals(CustomerType.fromStatus(cmd.getTargetType()))) {
            EnterpriseCustomer customer = enterpriseCustomerProvider.findByOrganizationId(cmd.getTargetId());
            if(customer != null) {
                ListEnterpriseCustomerContractsCommand command = new ListEnterpriseCustomerContractsCommand();
                command.setNamespaceId(cmd.getNamespaceId());
                command.setCommunityId(cmd.getCommunityId());
                command.setEnterpriseCustomerId(customer.getId());
                return listEnterpriseCustomerContracts(command);
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
        items.add(item);
        dto.setChargingItems(items);
        if(zjContract.getApartments() != null && zjContract.getApartments().size() > 0) {
            List<BuildingApartmentDTO> apartments = new ArrayList<>();
            zjContract.getApartments().forEach(apartment -> {
                BuildingApartmentDTO buildingApartmentDTO = new BuildingApartmentDTO();
                buildingApartmentDTO.setApartmentName(apartment.getApartmentName());
                buildingApartmentDTO.setBuildingName(apartment.getBuildingName());
                apartments.add(buildingApartmentDTO);
            });
            dto.setApartments(apartments);
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
        if(zjContract.getApartments() != null && zjContract.getApartments().size() > 0) {
            List<BuildingApartmentDTO> buildings = new ArrayList<>();
            zjContract.getApartments().forEach(apartment -> {
                buildings.add(ConvertHelper.convert(apartment, BuildingApartmentDTO.class));
            });
            dto.setBuildings(buildings);
        }

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
        ShenzhouJsonEntity<List<ZJContractDetail>> entity = JSONObject.parseObject(contract, new TypeReference<ShenzhouJsonEntity<List<ZJContractDetail>>>(){});
        List<ZJContractDetail> zjContract = entity.getResponse();
        if(zjContract != null && zjContract.size() > 0) {
            return zjContract.get(0);
        }
        return null;
    }

    @Override
    public List<ContractDTO> listEnterpriseCustomerContracts(ListEnterpriseCustomerContractsCommand cmd) {
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("zjgk listEnterpriseCustomerContracts. cmd:{}", cmd);
        }
        EnterpriseCustomer enterpriseCustomer = enterpriseCustomerProvider.findById(cmd.getEnterpriseCustomerId());
        if(enterpriseCustomer != null) {
            String appKey = configurationProvider.getValue(NAMESPACE_ID, "shenzhoushuma.app.key", "");
            String secretKey = configurationProvider.getValue(NAMESPACE_ID, "shenzhoushuma.secret.key", "");
            Map<String, String> params= new HashMap<String,String>();
            params.put("appKey", appKey);
//            params.put("timestamp", ""+System.currentTimeMillis());
//            params.put("nonce", ""+(long)(Math.random()*100000));
//            params.put("crypto", "sssss");
            params.put("timestamp", "1504681998261");
            params.put("nonce", "57903");
            params.put("crypto", "sssss");
            params.put("pageOffset", "0");
            params.put("pageSize", "");
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
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("zjgk listIndividualCustomerContracts. cmd:{}", cmd);
        }
        String appKey = configurationProvider.getValue(NAMESPACE_ID, "shenzhoushuma.app.key", "");
        String secretKey = configurationProvider.getValue(NAMESPACE_ID, "shenzhoushuma.secret.key", "");
        Map<String, String> params= new HashMap<String,String>();
        params.put("appKey", appKey);
        params.put("timestamp", ""+System.currentTimeMillis());
        params.put("nonce", ""+(long)(Math.random()*100000));
        params.put("crypto", "sssss");
        params.put("pageOffset", "");
        params.put("pageSize", "");
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

    @Override
    public void entryContract(EntryContractCommand cmd) {
        LOGGER.error("Insufficient privilege, zjgkhandler createContract");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public void reviewContract(ReviewContractCommand cmd) {
        LOGGER.error("Insufficient privilege, zjgkhandler createContract");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public void setContractParam(SetContractParamCommand cmd) {
        LOGGER.error("Insufficient privilege, zjgkhandler createContract");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public ContractParamDTO getContractParam(GetContractParamCommand cmd) {
        LOGGER.error("Insufficient privilege, zjgkhandler createContract");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public String generateContractNumber() {
        return null;
    }

    private Map<String, String> generateParams(String communityIdentifier, String contractStatus, String contractAttribute,
                                               String category, String customerName, String pageOffset, String pageSize) {
        String appKey = configurationProvider.getValue(NAMESPACE_ID, "shenzhoushuma.app.key", "");
        String secretKey = configurationProvider.getValue(NAMESPACE_ID, "shenzhoushuma.secret.key", "");
        Map<String, String> params= new HashMap<String,String>();
        params.put("appKey", appKey);
        params.put("timestamp", ""+System.currentTimeMillis());
        params.put("nonce", ""+(long)(Math.random()*100000));
        params.put("crypto", "sssss");
        params.put("contractNum", "");
        if(communityIdentifier==null) {
            communityIdentifier = "";
        }
        params.put("communityIdentifier", communityIdentifier);
        params.put("contractStatus", contractStatus);
        params.put("contractAttribute", contractAttribute);
        params.put("category", category);
        if(customerName == null) {
            customerName = "";
        }
        params.put("customerName", customerName);
        params.put("pageOffset", pageOffset);
        params.put("pageSize", pageSize);
        String signature = SignatureHelper.computeSignature(params, secretKey);
        params.put("signature", signature);

        return params;
    }

    private String convertContractAttribute(Byte type) {
        ContractType contractType = ContractType.fromStatus(type);
        if(contractType != null) {
            switch (contractType) {
                case NEW: return "新签合同";
                case RENEW: return "续约合同";
                case CHANGE: return "变更合同";
                default: return "";
            }
        }
        return "";
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
        if(contractStatus != null) {
            switch (contractStatus) {
                case ACTIVE: return "执行中";
                case WAITING_FOR_APPROVAL: return "审核中";
                case EXPIRED: return "已到期";
                case HISTORY: return "终止";
                case DENUNCIATION: return "退租完成";
                case DRAFT: return "草稿";
                default: return "";
            }
        }
        return "";
    }

    private Byte convertToContractStatus(String status) {
        if(status != null) {
            switch (status) {
                case "执行中": return ContractStatus.ACTIVE.getCode();
                case "等待账务主管审核":
                case "等待账务中心主任审核":
                case "退租等待物业审核":
                case "退租等待账务中心主任审核":
                case "退租等待客户经理审核":
                case "退租等待资产事业部副总经理审核":
                case "退租等待资产事业部总经理审核":
                    return ContractStatus.WAITING_FOR_APPROVAL.getCode();
                case "已到期": return ContractStatus.EXPIRED.getCode();
                case "终止": return ContractStatus.HISTORY.getCode();
                case "退租完成": return ContractStatus.DENUNCIATION.getCode();
                case "草稿": return ContractStatus.DRAFT.getCode();

                default: return null;
            }
        }
        return null;
    }

    private String postToShenzhou(Map<String, String> params, String method, Map<String, String> headers) {

        String shenzhouUrl = configurationProvider.getValue(NAMESPACE_ID, "shenzhou.host.url", "");
        String json = null;

        try {
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("post to shenzhou, method: {}, params: {}", shenzhouUrl + method, StringHelper.toJsonString(params));
            }
            json = HttpUtils.postJson(shenzhouUrl + method, StringHelper.toJsonString(params), 30, HTTP.UTF_8);

        } catch (Exception e) {
            LOGGER.error("sync from shenzhou request error, param={}", params, e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "sync from shenzhou request error.");
        }
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
