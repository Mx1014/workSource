package com.everhomes.point;

import com.everhomes.rest.point.*;
import com.everhomes.rest.user.UserFavoriteDTO;
import com.everhomes.user.UserActivityProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserPointServiceImpl implements UserPointService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserPointServiceImpl.class);

    @Autowired
    private UserActivityProvider userActivityProvider;

    @Deprecated
    @Override
    public void addPoint(AddUserPointCommand cmd) {
        /*assert (cmd.getPoint() != null);
        assert (cmd.getUid() != null);
        UserScore userScore = ConvertHelper.convert(cmd, UserScore.class);
        userScore.setOwnerUid(cmd.getUid());
        userScore.setScore(cmd.getPoint());
        userScore.setScoreType(cmd.getPointType());
        PointType type = PointType.fromCode(cmd.getPointType());
        // handle point type to validate
        try {
            switch (type) {
                case ADDRESS_APPROVAL:
                    handleAddressPass(userScore);
                    break;
                case APP_OPENED:
                    handOpenApp(userScore);
                    break;
                case CREATE_TOPIC:
                case CREATE_COMMENT:
                case INVITED_USER:
                    handleRepeat(userScore);
                    break;
                case AVATAR:
                    handleAvatarPass(userScore);
                    break;
                case OTHER:
                default:
                    LOGGER.error("cannot known");
                    break;
            }
        } catch (Exception e) {
            LOGGER.error("handle score error", e);
        }
        */
    }

    @Override
    public GetUserTreasureResponse getUserTreasure(GetUserTreasureCommand cmd) {
        GetUserTreasureResponse rsp = new GetUserTreasureResponse();
        List<UserFavoriteDTO> result = userActivityProvider.findFavorite(cmd.getUid());
        if (result == null) {
            rsp.setTopicFavoriteCount(0);
        } else {
            int size = result.stream().filter(r -> "topic".equals(r.getTargetType())).collect(Collectors.toList())
                    .size();
            rsp.setTopicFavoriteCount(size);
        }
        return rsp;
    }

    @Deprecated
    @Override
    public Integer getItemPoint(PointType pointType) {
        return 0;
    }
}
