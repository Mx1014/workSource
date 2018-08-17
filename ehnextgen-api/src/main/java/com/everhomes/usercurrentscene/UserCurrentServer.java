package com.everhomes.usercurrentscene;

import com.everhomes.rest.usercurrentscene.GetUserCurrentSceneCommand;
import com.everhomes.rest.usercurrentscene.UserCurrentSceneCommand;
import com.everhomes.rest.usercurrentscene.UserCurrentSceneDTO;

public interface UserCurrentServer {

    /*
     * 通过人员ID查询当前所在项目
     */
    UserCurrentSceneDTO getUserCurrentSceneByUid(GetUserCurrentSceneCommand cmd);

    /**
     * 保存
     * @param cmd
     */
    void saveUserCurrentScene(UserCurrentSceneCommand cmd) ;

}
