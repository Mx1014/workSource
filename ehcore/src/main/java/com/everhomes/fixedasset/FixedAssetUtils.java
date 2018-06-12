package com.everhomes.fixedasset;

import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.rest.fixedasset.BatchUpdateFixedAssetCommand;
import com.everhomes.rest.fixedasset.CreateOrUpdateFixedAssetCommand;
import com.everhomes.rest.fixedasset.FixedAssetAddFrom;
import com.everhomes.rest.fixedasset.FixedAssetCategoryDTO;
import com.everhomes.rest.fixedasset.FixedAssetErrorCode;
import com.everhomes.rest.fixedasset.FixedAssetStatus;
import com.everhomes.rest.organization.ImportFileResultLog;
import com.everhomes.rest.organization.OrganizationMemberDTO;
import com.everhomes.rest.organization.OrganizationTreeDTO;
import com.everhomes.user.UserContext;
import com.everhomes.util.RuntimeErrorException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public final class FixedAssetUtils {
    @Autowired
    private LocaleStringService localeStringService;
    @Autowired
    private LocaleTemplateService localeTemplateService;

    // treeName格式为: 一级分类/二级分类/三级分类/..  从topCategories顶级分类中逐级按名称匹配
    public static Integer findFixedAssetCategoryByTreeName(String treeName, List<FixedAssetCategoryDTO> topCategories) {
        if (StringUtils.isBlank(treeName) || CollectionUtils.isEmpty(topCategories)) {
            return null;
        }
        String[] strList = treeName.split("/");
        List<String> validNames = new ArrayList<>();
        for (String str : strList) {
            if (!StringUtils.isBlank(str)) {
                validNames.add(str.trim());
            }
        }
        if (validNames.isEmpty()) {
            return null;
        }
        // level = 1 表示从顶级分类开始匹配
        return findFixedAssetCategoryLevelByLevel(1, validNames, topCategories);
    }

    private static Integer findFixedAssetCategoryLevelByLevel(int level, List<String> nameList, List<FixedAssetCategoryDTO> categoriesThisLevel) {
        String thisLevelName = nameList.get(level - 1);
        for (FixedAssetCategoryDTO fixedAssetCategoryDTO : categoriesThisLevel) {
            if (thisLevelName.equals(fixedAssetCategoryDTO.getName())) {
                if (nameList.size() == level) {
                    return fixedAssetCategoryDTO.getId();
                }
                if (!CollectionUtils.isEmpty(fixedAssetCategoryDTO.getSubCategories())) {
                    return findFixedAssetCategoryLevelByLevel(level + 1, nameList, fixedAssetCategoryDTO.getSubCategories());
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    // treeName格式为: 一级部门/二级部门/三级部门/..  从topOrganization顶级部分中逐级按名称匹配
    public static Long findOrganizationIdByTreeName(String treeName, OrganizationTreeDTO topOrganization) {
        if (StringUtils.isBlank(treeName) || topOrganization == null) {
            return null;
        }
        String[] strList = treeName.split("/");
        List<String> validNames = new ArrayList<>();
        for (String str : strList) {
            if (!StringUtils.isBlank(str)) {
                validNames.add(str.trim());
            }
        }
        if (validNames.isEmpty()) {
            return null;
        }
        // level = 1 表示从顶级部门开始匹配
        return findOrganizationIdByByLevel(1, validNames, Collections.singletonList(topOrganization));
    }

    private static Long findOrganizationIdByByLevel(int level, List<String> nameList, List<OrganizationTreeDTO> organizationsThisLevel) {
        String thisLevelName = nameList.get(level - 1);
        for (OrganizationTreeDTO organization : organizationsThisLevel) {
            if (thisLevelName.equals(organization.getOrganizationName())) {
                if (nameList.size() == level) {
                    return organization.getOrganizationId();
                }
                if (!CollectionUtils.isEmpty(organization.getTrees())) {
                    return findOrganizationIdByByLevel(level + 1, nameList, organization.getTrees());
                } else {
                    return null;
                }
            }
        }
        return null;
    }


    // 资产编号 不为空，字段长度 <= 20
    public <T> boolean checkFixedAssetItemNo(ImportFileResultLog<T> log, T data, String itemNo) {
        if (StringUtils.isBlank(itemNo)) {
            log.setData(data);
            log.setErrorLog(this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_ITEM_NUM_IS_EMPTY));
            log.setCode(FixedAssetErrorCode.FIXED_ASSET_ITEM_NUM_IS_EMPTY);
            log.setErrorDescription(log.getErrorLog());
            return false;
        }
        if (itemNo.trim().length() > 20) {
            log.setData(data);
            log.setErrorLog(this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_ITEM_NUM_EXCEEDS_LIMIT));
            log.setCode(FixedAssetErrorCode.FIXED_ASSET_ITEM_NUM_EXCEEDS_LIMIT);
            log.setErrorDescription(log.getErrorLog());
            return false;
        }
        return true;
    }

    // 资产分类
    public <T> boolean checkFixedAssetCategory(ImportFileResultLog<T> log, T data, Integer fixedAssetCategoryId, String treeName) {
        if (StringUtils.isBlank(treeName)) {
            return true;
        }
        if (fixedAssetCategoryId == null) {
            log.setData(data);
            log.setErrorLog(this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_CATEGORY_NO_EXIST));
            log.setCode(FixedAssetErrorCode.FIXED_ASSET_CATEGORY_NO_EXIST);
            log.setErrorDescription(log.getErrorLog());
            return false;
        }
        return true;
    }

    // 资产名称
    public <T> boolean checkFixedAssetName(ImportFileResultLog<T> log, T data, String fixedAssetName) {
        if (StringUtils.isBlank(fixedAssetName)) {
            log.setData(data);
            log.setErrorLog(this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_NAME_IS_EMPTY));
            log.setCode(FixedAssetErrorCode.FIXED_ASSET_NAME_IS_EMPTY);
            log.setErrorDescription(log.getErrorLog());
            return false;
        }
        if (fixedAssetName.trim().length() > 20) {
            log.setData(data);
            log.setErrorLog(this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_NAME_EXCEEDS_LIMIT));
            log.setCode(FixedAssetErrorCode.FIXED_ASSET_NAME_EXCEEDS_LIMIT);
            log.setErrorDescription(log.getErrorLog());
            return false;
        }
        return true;
    }

    // 资产规格
    public <T> boolean checkFixedAssetSpecification(ImportFileResultLog<T> log, T data, String specification) {
        if (!StringUtils.isBlank(specification) && specification.trim().length() > 50) {
            log.setData(data);
            log.setErrorLog(this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_SPECIFICATION_EXCEEDS_LIMIT));
            log.setCode(FixedAssetErrorCode.FIXED_ASSET_SPECIFICATION_EXCEEDS_LIMIT);
            log.setErrorDescription(log.getErrorLog());
            return false;
        }
        return true;
    }

    // 单价
    public <T> boolean checkFixedAssetPrice(ImportFileResultLog<T> log, T data, String price) {
        if (StringUtils.isBlank(price)) {
            return true;
        }
        Pattern p = Pattern.compile("^\\d+(\\.\\d+)?$");
        java.util.regex.Matcher m = p.matcher(price.trim());
        if (!m.matches()) {
            log.setData(data);
            log.setErrorLog(this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_PRICE_REQUIRE_DIGIT));
            log.setCode(FixedAssetErrorCode.FIXED_ASSET_PRICE_REQUIRE_DIGIT);
            log.setErrorDescription(log.getErrorLog());
            return false;
        }
        if (new BigDecimal(price.trim()).compareTo(new BigDecimal("999999999.99")) > 0) {
            log.setData(data);
            log.setErrorLog(this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_PRICE_EXCEEDS_LIMIT));
            log.setCode(FixedAssetErrorCode.FIXED_ASSET_PRICE_EXCEEDS_LIMIT);
            log.setErrorDescription(log.getErrorLog());
            return false;
        }
        return true;
    }

    // 资产购买时间
    public <T> boolean checkFixedAssetBuyDate(ImportFileResultLog<T> log, T data, String buyDate) {
        if (StringUtils.isBlank(buyDate)) {
            return true;
        }
        if (!matchDate(buyDate.trim())) {
            log.setData(data);
            log.setErrorLog(this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_BUYDATE_FORMAT));
            log.setCode(FixedAssetErrorCode.FIXED_ASSET_BUYDATE_FORMAT);
            log.setErrorDescription(log.getErrorLog());
            return false;
        }
        if (!validRealDate(buyDate)) {
            log.setData(data);
            log.setErrorLog(this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_PARSE_BUY_DATE_ERROR));
            log.setCode(FixedAssetErrorCode.FIXED_ASSET_PARSE_BUY_DATE_ERROR);
            log.setErrorDescription(log.getErrorLog());
            return false;
        }
        return true;
    }

    // 资产所属供应商
    public <T> boolean checkFixedAssetVendor(ImportFileResultLog<T> log, T data, String vendor) {
        if (!StringUtils.isBlank(vendor) && vendor.trim().length() > 50) {
            log.setData(data);
            log.setErrorLog(this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_VENDOR_EXCEEDS_LIMIT));
            log.setCode(FixedAssetErrorCode.FIXED_ASSET_VENDOR_EXCEEDS_LIMIT);
            log.setErrorDescription(log.getErrorLog());
            return false;
        }
        return true;
    }

    // 资产来源
    public <T> boolean checkFixedAssetAddFrom(ImportFileResultLog<T> log, T data, String addFrom) {
        if (StringUtils.isBlank(addFrom)) {
            return true;
        }
        if (FixedAssetAddFrom.fromName(addFrom.trim()) == null) {
            Map<String, String> model = new HashMap<>();
            model.put("addFromList", FixedAssetAddFrom.printAll());
            log.setData(data);
            log.setErrorLog(getLocalizedTemplateString(FixedAssetErrorCode.FIXED_ASSET_ADD_FROM_NO_EXIST, model));
            log.setCode(FixedAssetErrorCode.FIXED_ASSET_ADD_FROM_NO_EXIST);
            log.setErrorDescription(log.getErrorLog());
            return false;
        }
        return true;
    }

    // 资产其他信息
    public <T> boolean checkFixedAssetOtherInfo(ImportFileResultLog<T> log, T data, String otherInfo) {
        if (!StringUtils.isBlank(otherInfo) && otherInfo.trim().length() > 200) {
            log.setData(data);
            log.setErrorLog(this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_OTHER_EXCEEDS_LIMIT));
            log.setCode(FixedAssetErrorCode.FIXED_ASSET_OTHER_EXCEEDS_LIMIT);
            log.setErrorDescription(log.getErrorLog());
            return false;
        }
        return true;
    }

    // 资产状态
    public <T> boolean checkFixedAssetStatus(ImportFileResultLog<T> log, T data, String status, String occupyDate, String departmentName) {
        if (StringUtils.isBlank(status)) {
            log.setData(data);
            log.setErrorLog(this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_STATUS_IS_EMPTY));
            log.setCode(FixedAssetErrorCode.FIXED_ASSET_STATUS_IS_EMPTY);
            log.setErrorDescription(log.getErrorLog());
            return false;
        }
        if (FixedAssetStatus.fromName(status.trim()) == null) {
            Map<String, String> model = new HashMap<>();
            model.put("statusList", FixedAssetStatus.printAll());
            log.setData(data);
            log.setErrorLog(getLocalizedTemplateString(FixedAssetErrorCode.FIXED_ASSET_STATUS_NO_EXIST, model));
            log.setCode(FixedAssetErrorCode.FIXED_ASSET_STATUS_NO_EXIST);
            log.setErrorDescription(log.getErrorLog());
            return false;
        }
        if (FixedAssetStatus.IN_USE == FixedAssetStatus.fromName(status.trim())) {
            if (StringUtils.isBlank(occupyDate) || StringUtils.isBlank(departmentName)) {
                log.setData(data);
                log.setErrorLog(this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_DEPARTMENT_OCCUPY_DATE_ERROR));
                log.setCode(FixedAssetErrorCode.FIXED_ASSET_DEPARTMENT_OCCUPY_DATE_ERROR);
                log.setErrorDescription(log.getErrorLog());
                return false;
            }
        }
        return true;
    }

    // 使用部门
    public <T> boolean checkOccupyDepartment(ImportFileResultLog<T> log, T data, Long departmentId, String departmentName,String occupyDate) {
        if (StringUtils.isBlank(departmentName)) {
            return true;
        }
        if (departmentId == null) {
            log.setData(data);
            log.setErrorLog(this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_OCCUPY_DEPARTMENT_NO_EXIST));
            log.setCode(FixedAssetErrorCode.FIXED_ASSET_OCCUPY_DEPARTMENT_NO_EXIST);
            log.setErrorDescription(log.getErrorLog());
            return false;
        }
        if (StringUtils.isBlank(occupyDate)) {
            log.setData(data);
            log.setErrorLog(this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_DEPARTMENT_OCCUPY_DATE_BOTH_REQUIRE_ERROR));
            log.setCode(FixedAssetErrorCode.FIXED_ASSET_DEPARTMENT_OCCUPY_DATE_BOTH_REQUIRE_ERROR);
            log.setErrorDescription(log.getErrorLog());
            return false;
        }
        return true;
    }

    // 使用人
    public <T> boolean checkOccupyMember(ImportFileResultLog<T> log, T data, OrganizationMemberDTO memberDTO, String memberDetailsName, String departmentName, String occupyDate) {
        if (StringUtils.isBlank(memberDetailsName)) {
            return true;
        }
        if (StringUtils.isBlank(departmentName) || StringUtils.isBlank(occupyDate)) {
            log.setData(data);
            log.setErrorLog(this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_DEPARTMENT_NAME_REQUIRE_ERROR));
            log.setCode(FixedAssetErrorCode.FIXED_ASSET_DEPARTMENT_NAME_REQUIRE_ERROR);
            log.setErrorDescription(log.getErrorLog());
            return false;
        }
        if (memberDTO == null) {
            log.setData(data);
            log.setErrorLog(this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_OCCUPY_USER_NO_EXIST));
            log.setCode(FixedAssetErrorCode.FIXED_ASSET_OCCUPY_USER_NO_EXIST);
            log.setErrorDescription(log.getErrorLog());
            return false;
        }
        return true;
    }

    // 资产领用时间
    public <T> boolean checkFixedAssetOccupyDate(ImportFileResultLog<T> log, T data, String occupyDate,String departmentName) {
        if (StringUtils.isBlank(occupyDate)) {
            return true;
        }
        if (!matchDate(occupyDate.trim())) {
            log.setData(data);
            log.setErrorLog(this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_OCCUPYDATE_FORMAT));
            log.setCode(FixedAssetErrorCode.FIXED_ASSET_OCCUPYDATE_FORMAT);
            log.setErrorDescription(log.getErrorLog());
            return false;
        }
        if (!validRealDate(occupyDate)) {
            log.setData(data);
            log.setErrorLog(this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_PARSE_OCCUPY_DATE_ERROR));
            log.setCode(FixedAssetErrorCode.FIXED_ASSET_PARSE_OCCUPY_DATE_ERROR);
            log.setErrorDescription(log.getErrorLog());
            return false;
        }
        if (StringUtils.isBlank(departmentName)) {
            log.setData(data);
            log.setErrorLog(this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_DEPARTMENT_OCCUPY_DATE_BOTH_REQUIRE_ERROR));
            log.setCode(FixedAssetErrorCode.FIXED_ASSET_DEPARTMENT_OCCUPY_DATE_BOTH_REQUIRE_ERROR);
            log.setErrorDescription(log.getErrorLog());
            return false;
        }
        return true;
    }

    // 资产存放地点
    public <T> boolean checkFixedAssetLocation(ImportFileResultLog<T> log, T data, String location) {
        if (!StringUtils.isBlank(location) && location.trim().length() > 50) {
            log.setData(data);
            log.setErrorLog(this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_LOCATION_EXCEEDS_LIMIT));
            log.setCode(FixedAssetErrorCode.FIXED_ASSET_LOCATION_EXCEEDS_LIMIT);
            log.setErrorDescription(log.getErrorLog());
            return false;
        }
        return true;
    }

    // 资产备注信息
    public <T> boolean checkFixedAssetRemark(ImportFileResultLog<T> log, T data, String remark) {
        if (!StringUtils.isBlank(remark) && remark.trim().length() > 200) {
            log.setData(data);
            log.setErrorLog(this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_REMARK_EXCEEDS_LIMIT));
            log.setCode(FixedAssetErrorCode.FIXED_ASSET_REMARK_EXCEEDS_LIMIT);
            log.setErrorDescription(log.getErrorLog());
            return false;
        }
        return true;
    }

    private boolean matchDate(String date) {
        String eL = "\\d{4}-\\d{2}-\\d{2}";
        Pattern p = Pattern.compile(eL);
        Matcher m = p.matcher(date);
        return m.matches();
    }

    // 校验不存在的日期如2018-04-33,2018-02-30
    private boolean validRealDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date realDate = sdf.parse(date);
            String newStr = sdf.format(realDate);
            return newStr.equals(date);
        } catch (ParseException e) {
            return false;
        }
    }

    public void checkCreateOrUpdateFixedAssetCommand(CreateOrUpdateFixedAssetCommand cmd) {
        checkFixedAssetItemNo(cmd.getItemNo());
        checkFixedAssetCategory(cmd.getFixedAssetCategoryId());
        checkFixedAssetName(cmd.getName());
        checkFixedAssetSpecification(cmd.getSpecification());
        checkFixedAssetPrice(cmd.getPrice());
        checkFixedAssetBuyDate(cmd.getBuyDate());
        checkFixedAssetVendor(cmd.getVendor());
        checkFixedAssetAddFrom(cmd.getAddFrom());
        checkFixedAssetOtherInfo(cmd.getOtherInfo());
        checkFixedAssetStatus(cmd.getStatus(), cmd.getOccupiedDate());
        checkFixedAssetOccupyDate(cmd.getOccupiedDate(), cmd.getOccupiedDepartmentId(), cmd.getOccupiedMemberDetailId());
        checkFixedAssetLocation(cmd.getLocation());
        checkFixedAssetRemark(cmd.getRemark());
    }

    public void checkBatchUpdateFixedAssetCommand(BatchUpdateFixedAssetCommand cmd) {
        checkFixedAssetName(cmd.getName());
        checkFixedAssetCategory(cmd.getFixedAssetCategoryId());
        checkFixedAssetSpecification(cmd.getSpecification());
        checkFixedAssetPrice(cmd.getPrice());
        checkFixedAssetBuyDate(cmd.getBuyDate());
        checkFixedAssetVendor(cmd.getVendor());
        checkFixedAssetAddFrom(cmd.getAddFrom());
        checkFixedAssetOtherInfo(cmd.getOtherInfo());
        checkFixedAssetStatus(cmd.getStatus(), cmd.getOccupiedDate());
        checkFixedAssetOccupyDate(cmd.getOccupiedDate(), cmd.getOccupiedDepartmentId(), cmd.getOccupiedMemberDetailId());
        checkFixedAssetLocation(cmd.getLocation());
        checkFixedAssetRemark(cmd.getRemark());
    }

    // 资产编号 不为空，字段长度 <= 20
    private void checkFixedAssetItemNo(String itemNo) {
        if (StringUtils.isBlank(itemNo)) {
            throw RuntimeErrorException
                    .errorWith(
                            FixedAssetErrorCode.SCOPE,
                            FixedAssetErrorCode.FIXED_ASSET_ITEM_NUM_IS_EMPTY,
                            this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_ITEM_NUM_IS_EMPTY));
        }
        if (itemNo.trim().length() > 20) {
            throw RuntimeErrorException
                    .errorWith(
                            FixedAssetErrorCode.SCOPE,
                            FixedAssetErrorCode.FIXED_ASSET_ITEM_NUM_EXCEEDS_LIMIT,
                            this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_ITEM_NUM_EXCEEDS_LIMIT));
        }
    }

    // 资产分类
    private void checkFixedAssetCategory(Integer fixedAssetCategoryId) {
        if (fixedAssetCategoryId == null) {
            throw RuntimeErrorException
                    .errorWith(
                            FixedAssetErrorCode.SCOPE,
                            FixedAssetErrorCode.FIXED_ASSET_CATEGORY_IS_NULL_ERROR,
                            this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_CATEGORY_IS_NULL_ERROR));
        }
    }

    // 资产名称
    private void checkFixedAssetName(String fixedAssetName) {
        if (StringUtils.isBlank(fixedAssetName)) {
            throw RuntimeErrorException
                    .errorWith(
                            FixedAssetErrorCode.SCOPE,
                            FixedAssetErrorCode.FIXED_ASSET_NAME_IS_EMPTY,
                            this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_NAME_IS_EMPTY));
        }
        if (fixedAssetName.trim().length() > 20) {
            throw RuntimeErrorException
                    .errorWith(
                            FixedAssetErrorCode.SCOPE,
                            FixedAssetErrorCode.FIXED_ASSET_NAME_EXCEEDS_LIMIT,
                            this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_NAME_EXCEEDS_LIMIT));
        }
    }

    // 资产规格
    private void checkFixedAssetSpecification(String specification) {
        if (!StringUtils.isBlank(specification) && specification.trim().length() > 50) {
            throw RuntimeErrorException
                    .errorWith(
                            FixedAssetErrorCode.SCOPE,
                            FixedAssetErrorCode.FIXED_ASSET_SPECIFICATION_EXCEEDS_LIMIT,
                            this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_SPECIFICATION_EXCEEDS_LIMIT));
        }
    }

    // 单价
    private void checkFixedAssetPrice(BigDecimal price) {
        if (price == null) {
            return;
        }
        if (new BigDecimal(price.toString().trim()).compareTo(new BigDecimal("999999999.99")) > 0) {
            throw RuntimeErrorException
                    .errorWith(
                            FixedAssetErrorCode.SCOPE,
                            FixedAssetErrorCode.FIXED_ASSET_PRICE_EXCEEDS_LIMIT,
                            this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_PRICE_EXCEEDS_LIMIT));
        }
    }

    // 资产购买时间
    private void checkFixedAssetBuyDate(String buyDate) {
        if (!StringUtils.isBlank(buyDate) && !matchDate(buyDate.trim())) {
            throw RuntimeErrorException
                    .errorWith(
                            FixedAssetErrorCode.SCOPE,
                            FixedAssetErrorCode.FIXED_ASSET_BUYDATE_FORMAT,
                            this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_BUYDATE_FORMAT));
        }
    }

    // 资产所属供应商
    private void checkFixedAssetVendor(String vendor) {
        if (!StringUtils.isBlank(vendor) && vendor.trim().length() > 50) {
            throw RuntimeErrorException
                    .errorWith(
                            FixedAssetErrorCode.SCOPE,
                            FixedAssetErrorCode.FIXED_ASSET_VENDOR_EXCEEDS_LIMIT,
                            this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_VENDOR_EXCEEDS_LIMIT));
        }
    }

    // 资产来源
    private void checkFixedAssetAddFrom(Byte addFrom) {
        if (addFrom == null) {
            throw RuntimeErrorException
                    .errorWith(
                            FixedAssetErrorCode.SCOPE,
                            FixedAssetErrorCode.FIXED_ASSET_ADD_FROM_NO_EMPTY,
                            this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_ADD_FROM_NO_EMPTY));
        }
        if (FixedAssetAddFrom.fromCode(addFrom) == null) {
            Map<String, String> model = new HashMap<>();
            model.put("addFromList", FixedAssetAddFrom.printAll());
            throw RuntimeErrorException
                    .errorWith(
                            FixedAssetErrorCode.SCOPE,
                            FixedAssetErrorCode.FIXED_ASSET_ADD_FROM_NO_EXIST,
                            getLocalizedTemplateString(FixedAssetErrorCode.FIXED_ASSET_ADD_FROM_NO_EXIST, model));
        }
    }

    // 资产其他信息
    private void checkFixedAssetOtherInfo(String otherInfo) {
        if (!StringUtils.isBlank(otherInfo) && otherInfo.trim().length() > 200) {
            throw RuntimeErrorException
                    .errorWith(
                            FixedAssetErrorCode.SCOPE,
                            FixedAssetErrorCode.FIXED_ASSET_OTHER_EXCEEDS_LIMIT,
                            this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_OTHER_EXCEEDS_LIMIT));
        }
    }

    // 资产状态
    private void checkFixedAssetStatus(Byte status, String occupyDate) {
        if (status == null) {
            throw RuntimeErrorException
                    .errorWith(
                            FixedAssetErrorCode.SCOPE,
                            FixedAssetErrorCode.FIXED_ASSET_STATUS_IS_EMPTY,
                            this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_STATUS_IS_EMPTY));
        }
        if (FixedAssetStatus.fromCode(status) == null) {
            Map<String, String> model = new HashMap<>();
            model.put("statusList", FixedAssetStatus.printAll());
            throw RuntimeErrorException
                    .errorWith(
                            FixedAssetErrorCode.SCOPE,
                            FixedAssetErrorCode.FIXED_ASSET_STATUS_NO_EXIST,
                            getLocalizedTemplateString(FixedAssetErrorCode.FIXED_ASSET_STATUS_NO_EXIST, model));
        }
        if (FixedAssetStatus.IN_USE == FixedAssetStatus.fromCode(status) && StringUtils.isBlank(occupyDate)) {
            throw RuntimeErrorException
                    .errorWith(
                            FixedAssetErrorCode.SCOPE,
                            FixedAssetErrorCode.FIXED_ASSET_OCCUPYDATE_IS_EMPTY,
                            this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_OCCUPYDATE_IS_EMPTY));
        }
    }

    // 资产领用时间
    private void checkFixedAssetOccupyDate(String occupyDate, Long departmentId, Long detailId) {
        if (StringUtils.isBlank(occupyDate)) {
            return;
        }
        if (!matchDate(occupyDate.trim())) {
            throw RuntimeErrorException
                    .errorWith(
                            FixedAssetErrorCode.SCOPE,
                            FixedAssetErrorCode.FIXED_ASSET_OCCUPYDATE_FORMAT,
                            this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_OCCUPYDATE_FORMAT));
        }
        if (departmentId == null && detailId == null) {
            throw RuntimeErrorException
                    .errorWith(
                            FixedAssetErrorCode.SCOPE,
                            FixedAssetErrorCode.FIXED_ASSET_DEPARTMENT_OR_USER_REQUIRED_ERROR,
                            this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_DEPARTMENT_OR_USER_REQUIRED_ERROR));
        }
    }

    // 资产存放地点
    private void checkFixedAssetLocation(String location) {
        if (!StringUtils.isBlank(location) && location.trim().length() > 50) {
            throw RuntimeErrorException
                    .errorWith(
                            FixedAssetErrorCode.SCOPE,
                            FixedAssetErrorCode.FIXED_ASSET_LOCATION_EXCEEDS_LIMIT,
                            this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_LOCATION_EXCEEDS_LIMIT));
        }
    }

    // 资产备注信息
    private void checkFixedAssetRemark(String remark) {
        if (!StringUtils.isBlank(remark) && remark.trim().length() > 200) {
            throw RuntimeErrorException
                    .errorWith(
                            FixedAssetErrorCode.SCOPE,
                            FixedAssetErrorCode.FIXED_ASSET_REMARK_EXCEEDS_LIMIT,
                            this.getLocalizedString(FixedAssetErrorCode.FIXED_ASSET_REMARK_EXCEEDS_LIMIT));
        }
    }

    private String getLocalizedString(int code) {
        return localeStringService.getLocalizedString(
                FixedAssetErrorCode.SCOPE,
                String.valueOf(code),
                UserContext.current().getUser().getLocale(),
                "Parameter error");
    }

    private String getLocalizedTemplateString(int code, Map<String, String> model) {
        return localeTemplateService.getLocaleTemplateString(
                FixedAssetErrorCode.SCOPE,
                code,
                UserContext.current().getUser().getLocale(),
                model,
                "Parameter error");
    }

}
