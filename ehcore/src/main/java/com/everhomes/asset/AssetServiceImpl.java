package com.everhomes.asset;

import com.everhomes.rest.asset.*;
import com.everhomes.rest.user.admin.ImportDataResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2017/2/20.
 */
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
    public AssetBillDTO creatAssetBill(CreatAssetBillCommand cmd) {
        return null;
    }

    @Override
    public AssetBillDTO findAssetBill(FindAssetBillCommand cmd) {
        return null;
    }

    @Override
    public AssetBillDTO updateAssetBill(UpdateAssetBillCommand cmd) {
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
