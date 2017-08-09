package com.everhomes.asset;

import com.everhomes.rest.asset.*;
import com.everhomes.rest.pmkexing.ListOrganizationsByPmAdminDTO;
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
    AssetBillTemplateValueDTO creatAssetBill(CreatAssetBillCommand cmd);
    AssetBillTemplateValueDTO findAssetBill(FindAssetBillCommand cmd);
    AssetBillTemplateValueDTO updateAssetBill(UpdateAssetBillCommand cmd);
    void notifyUnpaidBillsContact(NotifyUnpaidBillsContactCommand cmd);
    void setBillsStatus(BillIdListCommand cmd, AssetBillStatus status);
    void deleteBill(DeleteBillCommand cmd);
    List<AssetBillTemplateFieldDTO> updateAssetBillTemplate(UpdateAssetBillTemplateCommand cmd);

    Boolean checkTokenRegister(CheckTokenRegisterCommand cmd);
    NotifyTimesResponse notifyTimes(ImportOwnerCommand cmd);

    AssetBillStatDTO getAssetBillStat(GetAssetBillStatCommand cmd);
    List<ListOrganizationsByPmAdminDTO> listOrganizationsByPmAdmin();

    ListSettledBillResponse listSettledBill(ListSettledBillCommand cmd);

    ListSettledBillItemsResponse listSettledBillItems(ListSettledBillItemsCommand cmd);
}
