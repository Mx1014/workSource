package com.everhomes.asset;

import com.everhomes.rest.asset.*;
import com.everhomes.rest.user.admin.ImportDataResponse;
import com.everhomes.util.ConvertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
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
        return null;
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

    @Override
    public AssetBillTemplateValueDTO creatAssetBill(CreatAssetBillCommand cmd) {
        AssetBill bill = ConvertHelper.convert(cmd, AssetBill.class);


        BigDecimal periodAccountAmount = bill.getRental().add(bill.getPropertyManagementFee()).add(bill.getUnitMaintenanceFund())
                .add(bill.getLateFee()).add(bill.getPrivateWaterFee()).add(bill.getPrivateElectricityFee()).add(bill.getPublicWaterFee())
                .add(bill.getPublicElectricityFee()).add(bill.getWasteDisposalFee()).add(bill.getPollutionDischargeFee())
                .add(bill.getExtraAirConditionFee()).add(bill.getCoolingWaterFee()).add(bill.getWeakCurrentSlotFee())
                .add(bill.getDepositFromLease()).add(bill.getMaintenanceFee()).add(bill.getGasOilProcessFee())
                .add(bill.getHatchServiceFee()).add(bill.getPressurizedFee()).add(bill.getParkingFee()).add(bill.getOther());

        bill.setPeriodAccountAmount(periodAccountAmount);
        bill.setPeriodUnpaidAccountAmount(bill.getPeriodAccountAmount());
        assetProvider.creatAssetBill(bill);

        AssetBillTemplateValueDTO dto = ConvertHelper.convert(bill, AssetBillTemplateValueDTO.class);


        return null;
    }

    @Override
    public AssetBillTemplateValueDTO findAssetBill(FindAssetBillCommand cmd) {
        return null;
    }

    @Override
    public AssetBillTemplateValueDTO updateAssetBill(UpdateAssetBillCommand cmd) {
        return null;
    }

    @Override
    public void notifyUnpaidBillsContact(NotifyUnpaidBillsContactCommand cmd) {

    }

    @Override
    public void setBillsStatus(BillIdListCommand cmd, AssetBillStatus status) {

    }

    @Override
    public List<AssetBillTemplateFieldDTO> updateAssetBillTemplate(UpdateAssetBillTemplateCommand cmd) {
        return null;
    }
}
