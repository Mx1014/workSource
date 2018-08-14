package com.everhomes.usercurrentscene;

import com.everhomes.rest.usercurrentscene.GetUserCurrentSceneCommand;
import com.everhomes.rest.usercurrentscene.UserCurrentSceneCommand;
import com.everhomes.rest.usercurrentscene.UserCurrentSceneDTO;

public interface UserCurrentProvider {

    UserCurrentSceneDTO  getUserCurrentSceneByUid(GetUserCurrentSceneCommand cmd);

    Long addUserCurrentScene(UserCurrentScene bo);

    void updateUserCurrentScene(UserCurrentScene bo);
}
