// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: (必填)域空间id</li>
 * <li>ownerType: (必填)归属的类型，{@link com.everhomes.rest.visitorsys.VisitorsysOwnerType}</li>
 * <li>ownerId: (必填)归属的ID,园区/公司的ID</li>
 * <li>id: (必填)黑名单表ID</li>
 * <li>visitorName: (必填)访客名称</li>
 * <li>visitorPhone: (必填)访客电话</li>
 * <li>reason: (选填)原因</li>
 * </ul>
 */
public class CreateOrUpdateBlackListResponse extends BaseBlackListDTO {}
