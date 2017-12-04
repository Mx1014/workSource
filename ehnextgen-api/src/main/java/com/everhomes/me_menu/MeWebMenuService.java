package com.everhomes.me_menu;


import com.everhomes.rest.me_menu.ListMeWebMenusCommand;
import com.everhomes.rest.me_menu.ListMeWebMenusResponse;

import java.util.List;

public interface MeWebMenuService {

    ListMeWebMenusResponse listMeWebMenus(ListMeWebMenusCommand cmd);
}