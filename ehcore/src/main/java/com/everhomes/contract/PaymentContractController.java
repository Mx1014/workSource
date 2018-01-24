package com.everhomes.contract;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.contract.*;
import com.everhomes.search.ContractSearcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ying.xiong on 2017/12/28.
 */
@RestController
@RequestMapping("/payment_contract")
public class PaymentContractController extends ControllerBase {

    @Autowired
    private ContractSearcher contractSearcher;

    /**
     * <p>搜索合同</p>
     * <b>URL: /payment_contract/searchContracts</b>
     */
    @RequestMapping("searchContracts")
    @RestReturn(ListContractsResponse.class)
    public RestResponse searchContracts(SearchContractCommand cmd){
        cmd.setPaymentFlag((byte)1);
        return new RestResponse(contractSearcher.queryContracts(cmd));
    }

    /**
     * <p>创建合同</p>
     * <b>URL: /payment_contract/createPaymentContract</b>
     */
    @RequestMapping("createPaymentContract")
    @RestReturn(ContractDetailDTO.class)
    public RestResponse createPaymentContract(CreatePaymentContractCommand cmd){
        return new RestResponse(getContractService().createPaymentContract(cmd));
    }

    /**
     * <p>修改合同</p>
     * <b>URL: /payment_contract/updatePaymentContract</b>
     */
    @RequestMapping("updatePaymentContract")
    @RestReturn(ContractDetailDTO.class)
    public RestResponse updatePaymentContract(UpdatePaymentContractCommand cmd){
        return new RestResponse(getContractService().updatePaymentContract(cmd));
    }

    /**
     * <p>删除合同</p>
     * <b>URL: /payment_contract/deletePaymentContract</b>
     */
    @RequestMapping("deletePaymentContract")
    @RestReturn(String.class)
    public RestResponse deletePaymentContract(DeleteContractCommand cmd){
        cmd.setPaymentFlag((byte)1);
        getContractService().deleteContract(cmd);
        return new RestResponse();
    }

    /**
     * <p>退约合同</p>
     * <b>URL: /payment_contract/denunciationContract</b>
     */
    @RequestMapping("denunciationContract")
    @RestReturn(String.class)
    public RestResponse denunciationContract(DenunciationContractCommand cmd){
        cmd.setPaymentFlag((byte)1);
        getContractService().denunciationContract(cmd);
        return new RestResponse();
    }

    /**
     * <p>查看合同详情</p>
     * <b>URL: /payment_contract/findContract</b>
     */
    @RequestMapping("findContract")
    @RestReturn(ContractDetailDTO.class)
    public RestResponse findContract(FindContractCommand cmd){
        return new RestResponse(getContractService().findContract(cmd));
    }


    /**
     * <p>合同发起审批/合同报废</p>
     * <b>URL: /payment_contract/reviewContract</b>
     */
    @RequestMapping("reviewContract")
    @RestReturn(String.class)
    public RestResponse reviewContract(ReviewContractCommand cmd){
        cmd.setPaymentFlag((byte)1);
        getContractService().reviewContract(cmd);
        return new RestResponse();
    }


    private ContractService getContractService() {
        return PlatformContext.getComponent(ContractService.CONTRACT_PREFIX + "");
    }

}
