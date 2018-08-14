package com.everhomes.usercurrentscene;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.usercurrentscene.GetUserCurrentSceneCommand;
import com.everhomes.rest.usercurrentscene.UserCurrentSceneCommand;
import com.everhomes.rest.usercurrentscene.UserCurrentSceneDTO;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.springframework.stereotype.Service;

@Service
public class UserCurrentServerImpl implements UserCurrentServer {

    private UserCurrentProvider userCurrentProvider ;


    @Override
    public UserCurrentSceneDTO getUserCurrentSceneByUid(GetUserCurrentSceneCommand cmd) {
        if(cmd.getUid() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "can not find uid from this cmd :{} ",cmd);
        }
        Integer namespaceId = cmd.getNamespaceId() ;
        cmd.setNamespaceId(UserContext.getCurrentNamespaceId(namespaceId));
        return userCurrentProvider.getUserCurrentSceneByUid(cmd);
    }

    @Override
    public void saveUserCurrentScene(UserCurrentSceneCommand cmd) {
        if(cmd.getUid() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "can not find uid from this cmd :{} ",cmd);
        }
        Integer namespaceId = cmd.getNamespaceId() ;
        cmd.setNamespaceId(UserContext.getCurrentNamespaceId(namespaceId));

       //无则新增,有则更改
        if(cmd.getId()== null || cmd.getId()==0){//新增

            userCurrentProvider.addUserCurrentScene(ConvertHelper.convert(cmd, UserCurrentScene.class));

        }else{  //更改
            userCurrentProvider.updateUserCurrentScene(ConvertHelper.convert(cmd, UserCurrentScene.class));
        }

    }
}
