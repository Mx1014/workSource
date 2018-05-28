// @formatter:off
package com.everhomes.rest.visitorsys.ui;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.visitorsys.GetConfigurationResponse;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>namespaceId: (必填)域空间id</li>
 * <li>ownerType: (必填)归属的类型，{@link com.everhomes.rest.visitorsys.VisitorsysOwnerType}</li>
 * <li>ownerId: (必填)归属的ID,园区/公司的ID</li>
 * <li>configVersion: (必填)配置版本</li>
 * <li>logoUrl: (选填)客户端logourl地址</li>
 * <li>enterpriseName: (选填)ownerType为enterprise的时候，为此enterprise的名称</li>
 * <li>logoUri: (选填)客户端logouri地址</li>
 * <li>ipadThemeRgb: (选填)客户端主题色</li>
 * <li>guideInfo: (选填)指引信息</li>
 * <li>selfRegisterQrcodeUrl: (选填)自助登记二维码url</li>
 * <li>selfRegisterQrcodeUri: (选填)自助登记二维码uri</li>
 * <li>welcomeShowType: (选填)欢迎页面类型，image显示图片，text显示富文本</li>
 * <li>welcomePages: (选填)欢迎富文本</li>
 * <li>welcomePicUri: (选填)欢迎图片uri</li>
 * <li>secrecyAgreement: (选填)保密协议富文本</li>
 * <li>baseConfig: (选填)基本配置（配置中所有的是否配置和门禁配置），{@link com.everhomes.rest.visitorsys.VisitorsysBaseConfig}</li>
 * <li>formConfig: (选填)表单配置列表，{@link com.everhomes.rest.visitorsys.VisitorsysApprovalFormItem}</li>
 * <li>passCardConfig: (选填)通行证配置，{@link com.everhomes.rest.visitorsys.VisitorsysPassCardConfig}</li>
 * <li>officeLocationName: (选填)园区办公地点名称</li>
 * </ul>
 */
public class GetUIConfigurationResponse extends GetConfigurationResponse{
}
