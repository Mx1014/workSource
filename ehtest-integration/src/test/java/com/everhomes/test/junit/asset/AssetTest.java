package com.everhomes.test.junit.asset;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.asset.DeleteBillCommand;
import com.everhomes.rest.asset.ListAssetBillTemplateCommand;
import com.everhomes.rest.asset.ListAssetBillTemplateRestResponse;
import com.everhomes.rest.asset.UpdateAssetBillTemplateCommand;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Administrator on 2017/2/27.
 */
public class AssetTest extends BaseLoginAuthTestCase {
    //1. listAssetBillTemplate
    private static final String LIST_ASSET_BILL_TEMPLATE_URL = "/asset/listAssetBillTemplate";
    //2. updateAssetBillTemplate
    private static final String UPDATE_ASSET_BILL_TEMPLATE_URL = "/asset/updateAssetBillTemplate";
    //3. 搜索账单列表
    private static final String LIST_SIMPLE_ASSET_BILLS_URL = "/asset/listSimpleAssetBills";
    //4. 导出账单
    private static final String EXPORT_ASSET_BILLS_URL = "/asset/exportAssetBills";
    //5. 批量上传账单（与用户当前设置的模板字段按字段展示名对应）
    private static final String IMPORT_ASSET_BILLS_URL = "/asset/importAssetBills";
    //6. 新增账单
    private static final String CREAT_ASSET_BILL_URL = "/asset/creatAssetBill";
    //7. 查看账单
    private static final String FIND_ASSET_BILL_URL = "/asset/findAssetBill";
    //8. 编辑账单
    private static final String UPDATE_ASSET_BILL_URL = "/asset/updateAssetBill";
    //9. 一键催缴
    private static final String NOTIFY_UNPAID_BILLS_CONTACT_URL = "/asset/notifyUnpaidBillsContact";
    //10. 批量设置已缴
    private static final String SET_BILLS_PAID_URL = "/asset/setBillsPaid";
    //11. 批量设置待缴
    private static final String SET_BILLS_UNPAID_URL = "/asset/setBillsUnpaid";
    //12. 删除账单
    private static final String DELETE_BILL_URL = "/asset/deleteBill";

    @Before
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void initCustomData() {
        String jsonFilePath = "data/json/asset-test-data.txt";
        String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
    }

    @After
    public void tearDown() {
        logoff();
    }

    private void logon() {
        String userIdentifier = "19112349996";
        String plainTexPassword = "123456";
        Integer namespaceId = 999992;
        logon(namespaceId, userIdentifier, plainTexPassword);
    }

    //1. listAssetBillTemplate
    @Test
    public void testListAssetBillTemplate() {
        logon();
        ListAssetBillTemplateCommand cmd = new ListAssetBillTemplateCommand();
        cmd.setOwnerId(1L);
        cmd.setOwnerType("");
        cmd.setTargetId(1L);
        cmd.setTargetType("");

        ListAssetBillTemplateRestResponse response = httpClientService.restPost(LIST_ASSET_BILL_TEMPLATE_URL, cmd, ListAssetBillTemplateRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        // ListAssetBillTemplateResponse myResponse = response.getResponse();
        // assertNotNull(myResponse);
    }

    //2. updateAssetBillTemplate
    @Test
    public void testUpdateAssetBillTemplate() {
        logon();
        UpdateAssetBillTemplateCommand cmd = new UpdateAssetBillTemplateCommand();
        List<AssetBillTemplateFieldDTO> assetBillTemplateFieldDTOList = new ArrayList<>();
        AssetBillTemplateFieldDTO assetBillTemplateFieldDTO = new AssetBillTemplateFieldDTO();
        assetBillTemplateFieldDTO.setId(1L);
        assetBillTemplateFieldDTO.setNamespaceId(0);
        assetBillTemplateFieldDTO.setOwnerId(1L);
        assetBillTemplateFieldDTO.setOwnerType("");
        assetBillTemplateFieldDTO.setTargetId(1L);
        assetBillTemplateFieldDTO.setTargetType("");
        assetBillTemplateFieldDTO.setFieldDisplayName("");
        assetBillTemplateFieldDTO.setFieldName("");
        assetBillTemplateFieldDTO.setFieldType("");
        assetBillTemplateFieldDTO.setFieldCustomName("");
        assetBillTemplateFieldDTO.setRequiredFlag((byte)1);
        assetBillTemplateFieldDTO.setSelectedFlag((byte)1);
        assetBillTemplateFieldDTO.setDefaultOrder(0);
        assetBillTemplateFieldDTO.setTemplateVersion(1L);
        assetBillTemplateFieldDTOList.add(assetBillTemplateFieldDTO);
        cmd.setDtos(assetBillTemplateFieldDTOList);


        UpdateAssetBillTemplateRestResponse response = httpClientService.restPost(UPDATE_ASSET_BILL_TEMPLATE_URL, cmd, UpdateAssetBillTemplateRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        // UpdateAssetBillTemplateResponse myResponse = response.getResponse();
        // assertNotNull(myResponse);
    }

    //3. 搜索账单列表
    @Test
    public void testListSimpleAssetBills() {
        logon();
        ListSimpleAssetBillsCommand cmd = new ListSimpleAssetBillsCommand();
        cmd.setOwnerId(1L);
        cmd.setOwnerType("");
        cmd.setTargetId(1L);
        cmd.setTargetType("");
        cmd.setAddressId(1L);
        cmd.setTenant("");
        cmd.setStatus((byte)1);
        cmd.setStartTime(1L);
        cmd.setEndTime(1L);
        cmd.setPageAnchor(1L);
        cmd.setPageSize(0);

        ListSimpleAssetBillsRestResponse response = httpClientService.restPost(LIST_SIMPLE_ASSET_BILLS_URL, cmd, ListSimpleAssetBillsRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        // ListSimpleAssetBillsResponse myResponse = response.getResponse();
        // assertNotNull(myResponse);
    }

    //4. 导出账单
    @Test
    public void testExportAssetBills() {
        logon();
        ExportAssetBillsCommand cmd = new ExportAssetBillsCommand();
        cmd.setOwnerId(1L);
        cmd.setOwnerType("");
        cmd.setTargetId(1L);
        cmd.setTargetType("");
        cmd.setAddressId(1L);
        cmd.setTenant("");
        cmd.setStatus((byte)1);
        cmd.setStartTime(1L);
        cmd.setEndTime(1L);
        cmd.setPageAnchor(1L);
        cmd.setPageSize(0);

        RestResponseBase response = httpClientService.restPost(EXPORT_ASSET_BILLS_URL, cmd, RestResponseBase.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
    }

    //5. 批量上传账单（与用户当前设置的模板字段按字段展示名对应）
    @Test
    public void testImportAssetBills() {
        logon();
        ImportAssetBillsCommand cmd = new ImportAssetBillsCommand();
        cmd.setOwnerId(1L);
        cmd.setOwnerType("");
        cmd.setTargetId(1L);
        cmd.setTargetType("");

        ImportAssetBillsRestResponse response = httpClientService.restPost(IMPORT_ASSET_BILLS_URL, cmd, ImportAssetBillsRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        // ImportAssetBillsResponse myResponse = response.getResponse();
        // assertNotNull(myResponse);
    }

    //6. 新增账单
    @Test
    public void testCreatAssetBill() {
        logon();
        CreatAssetBillCommand cmd = new CreatAssetBillCommand();
        cmd.setOwnerId(1L);
        cmd.setOwnerType("");
        cmd.setTargetId(1L);
        cmd.setTargetType("");
        cmd.setSource((byte)1);
        cmd.setAccountPeriod(new Timestamp(DateHelper.currentGMTTime().getTime()));
        cmd.setAddressId(1L);
        cmd.setContactNo("");
        cmd.setRental(new BigDecimal("1"));
        cmd.setPropertyManagementFee(new BigDecimal("1"));
        cmd.setUnitMaintenanceFund(new BigDecimal("1"));
        cmd.setLateFee(new BigDecimal("1"));
        cmd.setPrivateWaterFee(new BigDecimal("1"));
        cmd.setPrivateElectricityFee(new BigDecimal("1"));
        cmd.setPublicWaterFee(new BigDecimal("1"));
        cmd.setPublicElectricityFee(new BigDecimal("1"));
        cmd.setWasteDisposalFee(new BigDecimal("1"));
        cmd.setPollutionDischargeFee(new BigDecimal("1"));
        cmd.setExtraAirConditionFee(new BigDecimal("1"));
        cmd.setCoolingWaterFee(new BigDecimal("1"));
        cmd.setWeakCurrentSlotFee(new BigDecimal("1"));
        cmd.setDepositFromLease(new BigDecimal("1"));
        cmd.setMaintenanceFee(new BigDecimal("1"));
        cmd.setGasOilProcessFee(new BigDecimal("1"));
        cmd.setHatchServiceFee(new BigDecimal("1"));
        cmd.setPressurizedFee(new BigDecimal("1"));
        cmd.setParkingFee(new BigDecimal("1"));
        cmd.setOther(new BigDecimal("1"));
        cmd.setTemplateVersion(1L);

        CreatAssetBillRestResponse response = httpClientService.restPost(CREAT_ASSET_BILL_URL, cmd, CreatAssetBillRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        // CreatAssetBillResponse myResponse = response.getResponse();
        // assertNotNull(myResponse);
    }

    //7. 查看账单
    @Test
    public void testFindAssetBill() {
        logon();
        FindAssetBillCommand cmd = new FindAssetBillCommand();
        cmd.setId(1L);
        cmd.setOwnerId(1L);
        cmd.setOwnerType("");
        cmd.setTargetId(1L);
        cmd.setTargetType("");
        cmd.setTemplateVersion(1L);

        FindAssetBillRestResponse response = httpClientService.restPost(FIND_ASSET_BILL_URL, cmd, FindAssetBillRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        // FindAssetBillResponse myResponse = response.getResponse();
        // assertNotNull(myResponse);
    }

    //8. 编辑账单
    @Test
    public void testUpdateAssetBill() {
        logon();
        UpdateAssetBillCommand cmd = new UpdateAssetBillCommand();
        cmd.setId(1L);
        cmd.setOwnerId(1L);
        cmd.setOwnerType("");
        cmd.setTargetId(1L);
        cmd.setTargetType("");
        cmd.setSource((byte)1);
        cmd.setAccountPeriod(new Timestamp(DateHelper.currentGMTTime().getTime()));
        cmd.setAddressId(1L);
        cmd.setContactNo("");
        cmd.setRental(new BigDecimal("1"));
        cmd.setPropertyManagementFee(new BigDecimal("1"));
        cmd.setUnitMaintenanceFund(new BigDecimal("1"));
        cmd.setLateFee(new BigDecimal("1"));
        cmd.setPrivateWaterFee(new BigDecimal("1"));
        cmd.setPrivateElectricityFee(new BigDecimal("1"));
        cmd.setPublicWaterFee(new BigDecimal("1"));
        cmd.setPublicElectricityFee(new BigDecimal("1"));
        cmd.setWasteDisposalFee(new BigDecimal("1"));
        cmd.setPollutionDischargeFee(new BigDecimal("1"));
        cmd.setExtraAirConditionFee(new BigDecimal("1"));
        cmd.setCoolingWaterFee(new BigDecimal("1"));
        cmd.setWeakCurrentSlotFee(new BigDecimal("1"));
        cmd.setDepositFromLease(new BigDecimal("1"));
        cmd.setMaintenanceFee(new BigDecimal("1"));
        cmd.setGasOilProcessFee(new BigDecimal("1"));
        cmd.setHatchServiceFee(new BigDecimal("1"));
        cmd.setPressurizedFee(new BigDecimal("1"));
        cmd.setParkingFee(new BigDecimal("1"));
        cmd.setOther(new BigDecimal("1"));
        cmd.setTemplateVersion(1L);

        UpdateAssetBillRestResponse response = httpClientService.restPost(UPDATE_ASSET_BILL_URL, cmd, UpdateAssetBillRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        // UpdateAssetBillResponse myResponse = response.getResponse();
        // assertNotNull(myResponse);
    }

    //9. 一键催缴
    @Test
    public void testNotifyUnpaidBillsContact() {
        logon();
        NotifyUnpaidBillsContactCommand cmd = new NotifyUnpaidBillsContactCommand();
        cmd.setOwnerId(1L);
        cmd.setOwnerType("");
        cmd.setTargetId(1L);
        cmd.setTargetType("");

        RestResponseBase response = httpClientService.restPost(NOTIFY_UNPAID_BILLS_CONTACT_URL, cmd, RestResponseBase.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
    }

    //10. 批量设置已缴
    @Test
    public void testSetBillsPaid() {
        logon();
        SetBillsPaidCommand cmd = new SetBillsPaidCommand();
        cmd.setOwnerId(1L);
        cmd.setOwnerType("");
        cmd.setTargetId(1L);
        cmd.setTargetType("");
        List<Long> longList = new ArrayList<>();
        longList.add(1L);
        cmd.setIds(longList);


        RestResponseBase response = httpClientService.restPost(SET_BILLS_PAID_URL, cmd, RestResponseBase.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
    }

    //11. 批量设置待缴
    @Test
    public void testSetBillsUnpaid() {
        logon();
        SetBillsUnpaidCommand cmd = new SetBillsUnpaidCommand();
        cmd.setOwnerId(1L);
        cmd.setOwnerType("");
        cmd.setTargetId(1L);
        cmd.setTargetType("");
        List<Long> longList = new ArrayList<>();
        longList.add(1L);
        cmd.setIds(longList);


        RestResponseBase response = httpClientService.restPost(SET_BILLS_UNPAID_URL, cmd, RestResponseBase.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
    }

    //12. 删除账单
    @Test
    public void testDeleteBill() {
        logon();
        DeleteBillCommand cmd = new DeleteBillCommand();
        cmd.setId(1L);
        cmd.setOwnerId(1L);
        cmd.setOwnerType("");
        cmd.setTargetId(1L);
        cmd.setTargetType("");

        RestResponseBase response = httpClientService.restPost(DELETE_BILL_URL, cmd, RestResponseBase.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
    }
}
