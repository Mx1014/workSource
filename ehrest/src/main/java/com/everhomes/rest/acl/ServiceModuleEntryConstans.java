package com.everhomes.rest.acl;

/**
 * <ul>
 *     <li>app_oa_client: 移动端oa客户端</li>
 *     <li>app_oa_management: 移动端oa管理</li>
 *     <li>app_community_client: 移动端园区客户端</li>
 *     <li>app_community_management: 移动端园区管理端</li>
 *     <li>app_service_client: 移动端服务客户端</li>
 *     <li>app_service_management: 移动端服务管理端</li>
 *     <li>pc_oa_client: PC端oa客户端</li>
 *     <li>pc_oa_management: PC端oa管理端</li>
 *     <li>pc_community_client: PC端园区客户端端</li>
 *     <li>pc_community_management: PC端园区管理端</li>
 *     <li>pc_service_client: PC端服务客户端</li>
 *     <li>pc_service_management: PC端服务管理端</li>
 * </ul>
 */
public interface ServiceModuleEntryConstans {

    byte app_oa_client = 11;
    byte app_oa_management = 12;
    byte app_community_client = 13;
    byte app_community_management = 14;
    byte app_service_client = 15;
    byte app_service_management = 16;

    byte pc_oa_client = 21;
    byte pc_oa_management = 22;
    byte pc_community_client = 23;
    byte pc_community_management = 24;
    byte pc_service_client = 25;
    byte pc_service_management = 26;
}