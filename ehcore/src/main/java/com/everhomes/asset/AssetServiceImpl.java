package com.everhomes.asset;

import com.everhomes.rest.asset.*;
import com.everhomes.rest.user.admin.ImportDataResponse;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/20.
 */
@Component
public class AssetServiceImpl implements AssetService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssetServiceImpl.class);

    @Autowired
    private AssetProvider assetProvider;

    @Override
    public List<AssetBillTemplateFieldDTO> listAssetBillTemplate(ListAssetBillTemplateCommand cmd) {

        List<AssetBillTemplateFieldDTO> dtos = new ArrayList<>();
        Long templateVersion = assetProvider.getTemplateVersion(cmd.getOwnerId(),cmd.getOwnerType(),cmd.getTargetId(),cmd.getTargetType());
        if(templateVersion == 0L) {
            dtos = assetProvider.findTemplateFieldByTemplateVersion(0L, cmd.getOwnerType(), 0L, cmd.getTargetType(), 0L);
        } else {
            dtos = assetProvider.findTemplateFieldByTemplateVersion(
                    cmd.getOwnerId(), cmd.getOwnerType(), cmd.getTargetId(), cmd.getTargetType(), templateVersion);
        }

        return dtos;
    }

    @Override
    public ListSimpleAssetBillsResponse listSimpleAssetBills(ListSimpleAssetBillsCommand cmd) {


        return null;
    }

    @Override
    public HttpServletResponse exportAssetBills(ListSimpleAssetBillsCommand cmd, HttpServletResponse response) {
        return null;
    }

    @Override
    public ImportDataResponse importAssetBills(ImportOwnerCommand cmd, MultipartFile mfile, Long userId) {
        return null;
    }

    //非当月则没有滞纳金 所以数据库里面不加lateFee
    private void getTotalAmount(AssetBill bill) {
        BigDecimal periodAccountAmount = bill.getRental().add(bill.getPropertyManagementFee()).add(bill.getUnitMaintenanceFund())
                .add(bill.getPrivateWaterFee()).add(bill.getPrivateElectricityFee()).add(bill.getPublicWaterFee())
                .add(bill.getPublicElectricityFee()).add(bill.getWasteDisposalFee()).add(bill.getPollutionDischargeFee())
                .add(bill.getExtraAirConditionFee()).add(bill.getCoolingWaterFee()).add(bill.getWeakCurrentSlotFee())
                .add(bill.getDepositFromLease()).add(bill.getMaintenanceFee()).add(bill.getGasOilProcessFee())
                .add(bill.getHatchServiceFee()).add(bill.getPressurizedFee()).add(bill.getParkingFee()).add(bill.getOther());

        bill.setPeriodAccountAmount(periodAccountAmount);
        bill.setPeriodUnpaidAccountAmount(bill.getPeriodAccountAmount());

    }

    @Override
    public AssetBillTemplateValueDTO creatAssetBill(CreatAssetBillCommand cmd) {
        AssetBill bill = ConvertHelper.convert(cmd, AssetBill.class);

        getTotalAmount(bill);
        assetProvider.creatAssetBill(bill);

        FindAssetBillCommand command = new FindAssetBillCommand();
        command.setId(bill.getId());
        command.setOwnerId(bill.getOwnerId());
        command.setOwnerType(bill.getOwnerType());
        command.setTargetId(bill.getTargetId());
        command.setTargetType(bill.getTargetType());
        command.setTemplateVersion(bill.getTemplateVersion());
        AssetBillTemplateValueDTO dto = findAssetBill(command);

        return dto;
    }

    @Override
    public AssetBillTemplateValueDTO findAssetBill(FindAssetBillCommand cmd) {
        return null;
    }

    @Override
    public AssetBillTemplateValueDTO updateAssetBill(UpdateAssetBillCommand cmd) {
        AssetBill bill = assetProvider.findAssetBill(cmd.getId(), cmd.getOwnerId(), cmd.getOwnerType(), cmd.getTargetId(), cmd.getTargetType());

        if (bill == null) {
            LOGGER.error("cannot find asset bill. bill: id = " + cmd.getId() + ", ownerId = " + cmd.getOwnerId()
                    + ", ownerType = " + cmd.getOwnerType() + ", targetId = " + cmd.getTargetId() + ", targetType = " + cmd.getTargetType());
            throw RuntimeErrorException.errorWith(AssetServiceErrorCode.SCOPE,
                    AssetServiceErrorCode.ASSET_BILL_NOT_EXIST,
                    "账单不存在");
        }

        bill = ConvertHelper.convert(cmd, AssetBill.class);

        getTotalAmount(bill);
        assetProvider.updateAssetBill(bill);

        FindAssetBillCommand command = new FindAssetBillCommand();
        command.setId(bill.getId());
        command.setOwnerId(bill.getOwnerId());
        command.setOwnerType(bill.getOwnerType());
        command.setTargetId(bill.getTargetId());
        command.setTargetType(bill.getTargetType());
        command.setTemplateVersion(bill.getTemplateVersion());
        AssetBillTemplateValueDTO dto = findAssetBill(command);

        return dto;
    }

    @Override
    public void notifyUnpaidBillsContact(NotifyUnpaidBillsContactCommand cmd) {

    }

    @Override
    public void setBillsStatus(BillIdListCommand cmd, AssetBillStatus status) {
        if(cmd.getIds() != null && cmd.getIds().size() > 0) {
            for(Long id : cmd.getIds()) {
                AssetBill bill = assetProvider.findAssetBill(id, cmd.getOwnerId(), cmd.getOwnerType(), cmd.getTargetId(), cmd.getTargetType());

                bill.setStatus(status.getCode());
                assetProvider.updateAssetBill(bill);
            }
        }

    }

    @Override
    public List<AssetBillTemplateFieldDTO> updateAssetBillTemplate(UpdateAssetBillTemplateCommand cmd) {

        if(cmd.getDtos() != null && cmd.getDtos().size() > 0) {
            for(AssetBillTemplateFieldDTO dto : cmd.getDtos()) {
                AssetBillTemplateFields field = ConvertHelper.convert(dto, AssetBillTemplateFields.class);
                field.setTemplateVersion(field.getTemplateVersion() + 1);
                assetProvider.creatTemplateField(field);
            }
        }

        ListAssetBillTemplateCommand command = new ListAssetBillTemplateCommand();
        command.setOwnerType(cmd.getDtos().get(0).getOwnerType());
        command.setOwnerId(cmd.getDtos().get(0).getOwnerId());
        command.setTargetType(cmd.getDtos().get(0).getTargetType());
        command.setTargetId(cmd.getDtos().get(0).getTargetId());

        List<AssetBillTemplateFieldDTO> dtos = listAssetBillTemplate(command);
        return dtos;
    }
}
