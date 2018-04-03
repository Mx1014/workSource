// @formatter:off
package com.everhomes.me_menu;


import java.util.List;

public interface MeWebMenuProvider {

    List<MeWebMenu> listMeWebMenus(Integer namespaceId);

    MeWebMenu findMeWebMenuById(Long id);
}
