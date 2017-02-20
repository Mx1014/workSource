package com.everhomes.asset;

import com.everhomes.rest.asset.*;
import com.everhomes.rest.user.admin.ImportDataResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2017/2/20.
 */
public interface AssetService {

    List<AssetBillTemplateFieldDTO> listAssetBillTemplate(ListAssetBillTemplateCommand cmd);
    ListSimpleAssetBillsResponse listSimpleAssetBills(ListSimpleAssetBillsCommand cmd);
    HttpServletResponse exportAssetBills(ListSimpleAssetBillsCommand cmd,HttpServletResponse response);
    ImportDataResponse importAssetBills(ImportOwnerCommand cmd, MultipartFile mfile, Long userId);
    AssetBillDTO creatAssetBill(CreatAssetBillCommand cmd);
    AssetBillDTO findAssetBill(FindAssetBillCommand cmd);
    AssetBillDTO updateAssetBill(UpdateAssetBillCommand cmd);
    void notifyUnpaidBillsContact(NotifyUnpaidBillsContactCommand cmd);
    void setBillsStatus(BillIdListCommand cmd, AssetBillStatus status);
    List<AssetBillTemplateFieldDTO> updateAssetBillTemplate(UpdateAssetBillTemplateCommand cmd);

}
