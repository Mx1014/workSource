package com.everhomes.remind;

import com.everhomes.rest.remind.BatchSortRemindCategoryCommand;
import com.everhomes.rest.remind.BatchSortRemindCommand;
import com.everhomes.rest.remind.CreateOrUpdateRemindCategoryCommand;
import com.everhomes.rest.remind.CreateOrUpdateRemindCommand;
import com.everhomes.rest.remind.DeleteRemindCategoryCommand;
import com.everhomes.rest.remind.DeleteRemindCommand;
import com.everhomes.rest.remind.GetCurrentUserDetailIdCommand;
import com.everhomes.rest.remind.GetCurrentUserDetailIdResponse;
import com.everhomes.rest.remind.GetRemindCategoryColorsResponse;
import com.everhomes.rest.remind.GetRemindCategoryCommand;
import com.everhomes.rest.remind.GetRemindCommand;
import com.everhomes.rest.remind.GetRemindSettingsResponse;
import com.everhomes.rest.remind.ListRemindCategoriesCommand;
import com.everhomes.rest.remind.ListRemindCategoriesResponse;
import com.everhomes.rest.remind.ListRemindResponse;
import com.everhomes.rest.remind.ListSelfRemindCommand;
import com.everhomes.rest.remind.ListShareRemindCommand;
import com.everhomes.rest.remind.ListSharingPersonsCommand;
import com.everhomes.rest.remind.ListSharingPersonsResponse;
import com.everhomes.rest.remind.RemindCategoryDTO;
import com.everhomes.rest.remind.RemindDTO;
import com.everhomes.rest.remind.SubscribeShareRemindCommand;
import com.everhomes.rest.remind.UnSubscribeShareRemindCommand;
import com.everhomes.rest.remind.UpdateRemindStatusCommand;
import com.everhomes.rest.remind.UpdateRemindStatusResponse;

public interface RemindService {

    GetRemindCategoryColorsResponse getRemindCategoryColors();

    GetRemindSettingsResponse listRemindSettings();

    void evictRemindSettingsCache();

    Long createOrUpdateRemindCategory(CreateOrUpdateRemindCategoryCommand cmd);

    void batchSortRemindCategories(BatchSortRemindCategoryCommand cmd);

    void deleteRemindCategory(DeleteRemindCategoryCommand cmd);

    RemindCategoryDTO getRemindCategoryDetail(GetRemindCategoryCommand cmd);

    ListRemindCategoriesResponse listRemindCategories(ListRemindCategoriesCommand cmd);

    ListSharingPersonsResponse listSharingPersons(ListSharingPersonsCommand cmd);

    Long createOrUpdateRemind(CreateOrUpdateRemindCommand cmd);

    void deleteRemind(DeleteRemindCommand cmd);

    RemindDTO getRemindDetail(GetRemindCommand cmd);

    void batchSortReminds(BatchSortRemindCommand cmd);

    ListRemindResponse listSelfReminds(ListSelfRemindCommand cmd);

    ListRemindResponse listShareReminds(ListShareRemindCommand cmd);

    UpdateRemindStatusResponse updateRemindStatus(UpdateRemindStatusCommand cmd);

    void subscribeShareRemind(SubscribeShareRemindCommand cmd);

    void unSubscribeShareRemind(UnSubscribeShareRemindCommand cmd);

    GetCurrentUserDetailIdResponse getCurrentUserContactSimpleInfo(GetCurrentUserDetailIdCommand cmd);

	void sendRemindMessage(Remind remind);

}
