package com.everhomes.test.junit.asset;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.asset.*;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhAssetBills;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;
import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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

    Integer namespaceId = 999992;
    String userIdentifier = "19112349996";
    String plainTexPassword = "123456";

    Long ownerId = (long) 1000750;
    String ownerType = "PM";

    Long targetId = 240111044331051300L;
    String targetType = "community";

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
        logon(namespaceId, userIdentifier, plainTexPassword);
    }

    //1. listAssetBillTemplate
    @Test
    public void testListAssetBillTemplate() {
        logon();
        ListAssetBillTemplateCommand cmd = new ListAssetBillTemplateCommand();
        cmd.setOwnerId(ownerId);
        cmd.setOwnerType(ownerType);
        cmd.setTargetId(targetId);
        cmd.setTargetType(targetType);

        ListAssetBillTemplateRestResponse response = httpClientService.restPost(LIST_ASSET_BILL_TEMPLATE_URL, cmd, ListAssetBillTemplateRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        assertEquals(24, response.getResponse().size());
    }

    //2. updateAssetBillTemplate
    @Test
    public void testUpdateAssetBillTemplate() {
        logon();
        UpdateAssetBillTemplateCommand cmd = new UpdateAssetBillTemplateCommand();
        List<AssetBillTemplateFieldDTO> assetBillTemplateFieldDTOList = new ArrayList<>();

        AssetBillTemplateFieldDTO assetBillTemplateFieldDTO2 = new AssetBillTemplateFieldDTO();
        assetBillTemplateFieldDTO2.setNamespaceId(namespaceId);
        assetBillTemplateFieldDTO2.setOwnerId(ownerId);
        assetBillTemplateFieldDTO2.setOwnerType(ownerType);
        assetBillTemplateFieldDTO2.setTargetId(targetId);
        assetBillTemplateFieldDTO2.setTargetType(targetType);
        assetBillTemplateFieldDTO2.setFieldDisplayName("楼栋");
        assetBillTemplateFieldDTO2.setFieldName("buildingName");
        assetBillTemplateFieldDTO2.setFieldType("String");
        assetBillTemplateFieldDTO2.setRequiredFlag((byte)0);
        assetBillTemplateFieldDTO2.setSelectedFlag((byte)0);
        assetBillTemplateFieldDTO2.setDefaultOrder(0);
        assetBillTemplateFieldDTO2.setTemplateVersion(0L);
        assetBillTemplateFieldDTOList.add(assetBillTemplateFieldDTO2);

        AssetBillTemplateFieldDTO assetBillTemplateFieldDTO = new AssetBillTemplateFieldDTO();
        assetBillTemplateFieldDTO.setNamespaceId(namespaceId);
        assetBillTemplateFieldDTO.setOwnerId(ownerId);
        assetBillTemplateFieldDTO.setOwnerType(ownerType);
        assetBillTemplateFieldDTO.setTargetId(targetId);
        assetBillTemplateFieldDTO.setTargetType(targetType);
        assetBillTemplateFieldDTO.setFieldDisplayName("账期");
        assetBillTemplateFieldDTO.setFieldName("accountPeriod");
        assetBillTemplateFieldDTO.setFieldType("Timestamp");
        assetBillTemplateFieldDTO.setRequiredFlag((byte)1);
        assetBillTemplateFieldDTO.setSelectedFlag((byte)1);
        assetBillTemplateFieldDTO.setDefaultOrder(0);
        assetBillTemplateFieldDTO.setTemplateVersion(0L);
        assetBillTemplateFieldDTOList.add(assetBillTemplateFieldDTO);
        cmd.setDtos(assetBillTemplateFieldDTOList);


        UpdateAssetBillTemplateRestResponse response = httpClientService.restPost(UPDATE_ASSET_BILL_TEMPLATE_URL, cmd, UpdateAssetBillTemplateRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        assertEquals("accountPeriod", response.getResponse().get(0).getFieldName());
    }

    //3. 搜索账单列表
    @Test
    public void testListSimpleAssetBills() {
        logon();

        ListSimpleAssetBillsCommand cmd = new ListSimpleAssetBillsCommand();
        cmd.setOwnerId(ownerId);
        cmd.setOwnerType(ownerType);
        cmd.setTargetId(targetId);
        cmd.setTargetType(targetType);


        ListSimpleAssetBillsRestResponse response = httpClientService.restPost(LIST_SIMPLE_ASSET_BILLS_URL, cmd, ListSimpleAssetBillsRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        assertEquals("12342234123", response.getResponse().getBills().get(0).getContactNo());

    }

    //4. 导出账单
    @Test
    public void testExportAssetBills() {
        logon();
        ListSimpleAssetBillsCommand cmd = new ListSimpleAssetBillsCommand();
        cmd.setOwnerId(ownerId);
        cmd.setOwnerType(ownerType);
        cmd.setTargetId(targetId);
        cmd.setTargetType(targetType);

        RestResponseBase response = httpClientService.restPost(EXPORT_ASSET_BILLS_URL, cmd, RestResponseBase.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
    }

    //5. 批量上传账单（与用户当前设置的模板字段按字段展示名对应）
    @Test
    public void testImportAssetBills() {
        logon();
        deleteAssetbills();
        ImportAssetBills();

    }

    //6. 新增账单
    @Test
    public void testCreatAssetBill() {
        logon();
        CreatAssetBillCommand cmd = new CreatAssetBillCommand();
        cmd.setOwnerId(ownerId);
        cmd.setOwnerType(ownerType);
        cmd.setTargetId(targetId);
        cmd.setTargetType(targetType);

        cmd.setAccountPeriod(new Timestamp(System.currentTimeMillis()));
        cmd.setBuildingName("A1");
        cmd.setApartmentName("102");
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
        cmd.setTemplateVersion(0L);

        CreatAssetBillRestResponse response = httpClientService.restPost(CREAT_ASSET_BILL_URL, cmd, CreatAssetBillRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        List<EhAssetBills> bills = getDbAssetBills();
        assertEquals(2, bills.size());
    }

    //7. 查看账单
    @Test
    public void testFindAssetBill() {
        logon();

        FindAssetBillCommand cmd = new FindAssetBillCommand();
        cmd.setId(14L);
        cmd.setOwnerId(ownerId);
        cmd.setOwnerType(ownerType);
        cmd.setTargetId(targetId);
        cmd.setTargetType(targetType);
        cmd.setTemplateVersion(0L);

        FindAssetBillRestResponse billRestResponse = httpClientService.restPost(FIND_ASSET_BILL_URL, cmd, FindAssetBillRestResponse.class);
        assertNotNull(billRestResponse);
        assertTrue("response = " + StringHelper.toJsonString(billRestResponse), httpClientService.isReponseSuccess(billRestResponse));

        assertNotNull(billRestResponse.getResponse().getDtos());

    }

    //8. 编辑账单
    @Test
    public void testUpdateAssetBill() {
        logon();
        UpdateAssetBillCommand cmd = new UpdateAssetBillCommand();
        cmd.setId(14L);
        cmd.setOwnerId(ownerId);
        cmd.setOwnerType(ownerType);
        cmd.setTargetId(targetId);
        cmd.setTargetType(targetType);
        cmd.setSource((byte)1);
        cmd.setAccountPeriod(new Timestamp(System.currentTimeMillis()));
        cmd.setAddressId(1L);
        cmd.setContactNo("12200000001");
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
        cmd.setTemplateVersion(0L);

        UpdateAssetBillRestResponse response = httpClientService.restPost(UPDATE_ASSET_BILL_URL, cmd, UpdateAssetBillRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

    }

    //9. 一键催缴
    @Test
    public void testNotifyUnpaidBillsContact() {
        logon();
        NotifyUnpaidBillsContactCommand cmd = new NotifyUnpaidBillsContactCommand();
        cmd.setOwnerId(ownerId);
        cmd.setOwnerType(ownerType);
        cmd.setTargetId(targetId);
        cmd.setTargetType(targetType);

        RestResponseBase response = httpClientService.restPost(NOTIFY_UNPAID_BILLS_CONTACT_URL, cmd, RestResponseBase.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
    }

    //10. 批量设置已缴
    @Test
    public void testSetBillsPaid() {
        logon();
        BillIdListCommand cmd = new BillIdListCommand();
        cmd.setOwnerId(ownerId);
        cmd.setOwnerType(ownerType);
        cmd.setTargetId(targetId);
        cmd.setTargetType(targetType);
        List<Long> longList = new ArrayList<>();
        longList.add(14L);
        cmd.setIds(longList);


        RestResponseBase response = httpClientService.restPost(SET_BILLS_PAID_URL, cmd, RestResponseBase.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        List<EhAssetBills> bills = getDbAssetBills();
    }

    //11. 批量设置待缴
    @Test
    public void testSetBillsUnpaid() {
        logon();
        BillIdListCommand cmd = new BillIdListCommand();
        cmd.setOwnerId(ownerId);
        cmd.setOwnerType(ownerType);
        cmd.setTargetId(targetId);
        cmd.setTargetType(targetType);
        List<Long> longList = new ArrayList<>();
        longList.add(14L);
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
        cmd.setId(14L);
        cmd.setOwnerId(ownerId);
        cmd.setOwnerType(ownerType);
        cmd.setTargetId(targetId);
        cmd.setTargetType(targetType);

        RestResponseBase response = httpClientService.restPost(DELETE_BILL_URL, cmd, RestResponseBase.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        List<EhAssetBills> bills = getDbAssetBills();
        assertEquals(0, bills.size());
    }

    private List<EhAssetBills> getDbAssetBills() {
        DSLContext context = dbProvider.getDslContext();
        return context.select().from(Tables.EH_ASSET_BILLS)
                .where(Tables.EH_ASSET_BILLS.OWNER_ID.eq(ownerId))
                .and(Tables.EH_ASSET_BILLS.OWNER_TYPE.eq(ownerType))
                .and(Tables.EH_ASSET_BILLS.TARGET_ID.eq(targetId))
                .and(Tables.EH_ASSET_BILLS.TARGET_TYPE.eq(targetType))
                .fetch().map(r -> ConvertHelper.convert(r, EhAssetBills.class));
    }

    private void deleteAssetbills() {
        DSLContext context = dbProvider.getDslContext();
        context.truncate(Tables.EH_ASSET_BILLS).execute();
    }
    private void ImportAssetBills() {
        try {
            String uri = IMPORT_ASSET_BILLS_URL;
            ImportOwnerCommand cmd = new ImportOwnerCommand();
            cmd.setOwnerId(ownerId);
            cmd.setOwnerType(ownerType);
            cmd.setTargetId(targetId);
            cmd.setTargetType(targetType);


            File file;
            file = new File(new File("").getCanonicalPath() + "/src/test/data/excel/assetBill.xlsx");
            RestResponseBase response = httpClientService.postFile(uri, cmd, file, RestResponseBase.class);
            assertNotNull(response);
            assertTrue("response= " + StringHelper.toJsonString(response),
                    httpClientService.isReponseSuccess(response));
            assertTrue("errorCode should be 200", response.getErrorCode().intValue() == ErrorCodes.SUCCESS);

            DSLContext context = dbProvider.getDslContext();
            Integer count = (Integer) context.selectCount().from(Tables.EH_ASSET_BILLS).fetchOne().getValue(0);
			assertTrue("the count should be 1", count.intValue() == 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
