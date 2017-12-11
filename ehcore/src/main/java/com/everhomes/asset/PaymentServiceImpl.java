//@formatter:off
package com.everhomes.asset;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.entity.EntityType;
import com.everhomes.order.PaymentAccount;
import com.everhomes.order.PaymentServiceConfig;
import com.everhomes.order.PaymentUser;
import com.everhomes.pay.order.TransactionType;
import com.everhomes.portal.PortalService;
import com.everhomes.rest.asset.*;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wentian Wang on 2017/9/28.
 */
@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private AssetProvider assetProvider;
    @Autowired
    private RemoteAccessService remoteAccessService;
    @Autowired
    private PortalService portalService;
    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);
    @Override
    public ListPaymentBillResp listPaymentBill(ListPaymentBillCmd cmd) throws Exception {
        ListServiceModuleAppsCommand cmd1 = new ListServiceModuleAppsCommand();
        cmd1.setActionType((byte)13);
        cmd1.setModuleId(20400l);
        cmd1.setNamespaceId(UserContext.getCurrentNamespaceId());
        ListServiceModuleAppsResponse res = portalService.listServiceModuleAppsWithConditon(cmd1);
        Long appId = res.getServiceModuleApps().get(0).getId();
        if(!userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), EntityType.ORGANIZATIONS.getCode(), 1000001L, 1000001L, 40078L, appId, null, 240111044331048623L)){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_GENERAL_EXCEPTION,"checkUserPrivilege false");
        }

        if(cmd.getNamespaceId() == null){
            cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
        }
        if(cmd.getOrderType()==null) return new ListPaymentBillResp(cmd.getPageAnchor(), cmd.getPageSize());

        if(cmd.getOrderType().equals(OrderType.OrderTypeEnum.WUYE_CODE.getPycode())||cmd.getOrderType().equals(OrderType.OrderTypeEnum.ZJGK_RENTAL_CODE.getPycode())){
            cmd.setOrderType(getRealOrderType(cmd.getNamespaceId()));
            if(cmd.getOrderType()==null) return new ListPaymentBillResp(cmd.getPageAnchor(), cmd.getPageSize());
        }

        if (cmd.getPageSize() == null) {
            cmd.setPageSize(21l);
        }else{
            cmd.setPageSize(cmd.getPageSize()+1l);
        }
        if(cmd.getPageAnchor() == null){
            cmd.setPageAnchor(0l);
        }
        if (cmd.getTransactionType() == null) {
            List<Integer> transactionTypes = new ArrayList<Integer>();
            transactionTypes.add(TransactionType.ORDERPAYMENT.getCode());
            transactionTypes.add(TransactionType.REFUND.getCode());
            cmd.setTransactionTypes(transactionTypes);
        }
        if (cmd.getSorts() == null) {
            ReSortCmd sortCmd = new ReSortCmd();
            sortCmd.setSortField("orderId");
            sortCmd.setSortType("desc");
            List<ReSortCmd> sorts = new ArrayList<ReSortCmd>();
            sorts.add(sortCmd);
            cmd.setSorts(sorts);
        }
        //payeeUserId
        PaymentUser paymentUser = assetProvider.findByOwner(cmd.getUserType(),cmd.getUserId());
        if(paymentUser == null) {
            return new ListPaymentBillResp(cmd.getPageAnchor(), cmd.getPageSize());
        }
        cmd.setUserId(paymentUser.getPaymentUserId());
        LOGGER.info("payee user payer id is = {}",paymentUser.getPaymentUserId());
        return remoteAccessService.listOrderPayment(cmd);
    }

    private String getRealOrderType(Integer namespaceId) {
        PaymentServiceConfig config = assetProvider.findServiceConfig(namespaceId);
        return config.getOrderType();
    }

    @Override
    public PaymentAccountResp findPaymentAccount(){
        PaymentAccount entity = this.assetProvider.findPaymentAccount();
        if(entity != null) {
            return ConvertHelper.convert(entity, PaymentAccountResp.class);
        }
        return null;
    }

}
