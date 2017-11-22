package com.everhomes.point;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.point.*;
import com.everhomes.rest.user.UserFavoriteDTO;
import com.everhomes.server.schema.Tables;
import com.everhomes.user.User;
import com.everhomes.user.UserActivityProvider;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.Operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Component
public class UserPointServiceImpl implements UserPointService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserPointServiceImpl.class);
    @Autowired
    private UserPointProvider userPointProvider;

    @Autowired
    private ConfigurationProvider configrationProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private UserActivityProvider userActivityProvider;

    @Override
    public void addPoint(AddUserPointCommand cmd) {
        assert (cmd.getPoint() != null);
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

    }

    @Override
    public GetUserPointResponse getUserPoint(GetUserPointCommand cmd) {
        ListingLocator locator = new ListingLocator();
        // locator.setAnchor(cmd.getAnchor());
        int pageSize = configrationProvider.getIntValue("pagination.page.size", AppConstants.PAGINATION_DEFAULT_SIZE);
        List<UserScore> result = userPointProvider.listUserScore(locator, pageSize + 1, Operator.AND,
                Tables.EH_USER_SCORES.OWNER_UID.eq(cmd.getUid()));
        List<UserScoreDTO> dtos = result.stream().map(r -> ConvertHelper.convert(r, UserScoreDTO.class))
                .collect(Collectors.toList());
        return new GetUserPointResponse(cmd.getUid(), dtos, locator.getAnchor());
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

    private void handOpenApp(UserScore userScore) {
        // get today start time
        UserScore old = userPointProvider.findTodayRecord(Operator.AND,
                Tables.EH_USER_SCORES.OWNER_UID.eq(userScore.getOwnerUid()),
                Tables.EH_USER_SCORES.CREATE_TIME.lt(getCurrentTime()),
                Tables.EH_USER_SCORES.CREATE_TIME.gt(getTodayStartTime()));
        if (old == null) {
            userScore.setCreateTime(getCurrentTime());
            userScore.setOperateTime(getCurrentTime());
            userScore.setOperatorUid(userScore.getOperatorUid() == null ? userScore.getOwnerUid() : userScore
                    .getOperatorUid());
            userScore.setScoreType(PointType.APP_OPENED.name());
            dbProvider.execute((status) -> {
                userPointProvider.addUserPoint(userScore);

                User user = userProvider.findUserById(userScore.getOwnerUid());
                int val = user.getPoints() == null ? 0 : user.getPoints().intValue();
                user.setPoints(val + userScore.getScore());
                user.setLevel(UserLevel.getLevel(user.getPoints()).getCode());
                userProvider.updateUser(user);
                return status;
            });
            return;
        }
        userScore.setCreateTime(getCurrentTime());
        userScore.setOperateTime(getCurrentTime());
        // if record one row,insert 0 to row
        userScore.setScore(0);
        userScore.setOperatorUid(userScore.getOperatorUid() == null ? userScore.getOwnerUid() : userScore
                .getOperatorUid());
        userScore.setScoreType(PointType.APP_OPENED.name());
        userPointProvider.addUserPoint(userScore);

    }

    private void handleAddressPass(UserScore userScore) {
        User user = userProvider.findUserById(userScore.getOwnerUid());
        userScore.setCreateTime(getCurrentTime());
        userScore.setOperateTime(getCurrentTime());
        userScore.setScoreType(PointType.ADDRESS_APPROVAL.name());
        userScore.setOperatorUid(userScore.getOperatorUid() == null ? userScore.getOwnerUid() : userScore
                .getOperatorUid());
        if (user.getAddressId() != null) {
            userScore.setScore(0);
            userPointProvider.addUserPoint(userScore);
            return;
        }
        int val = user.getPoints() == null ? 0 : user.getPoints().intValue();
        user.setPoints(val + userScore.getScore());
        user.setLevel(UserLevel.getLevel(user.getPoints()).getCode());
        dbProvider.execute((status) -> {
            userProvider.updateUser(user);
            userPointProvider.addUserPoint(userScore);
            return status;
        });
    }

    private void handleAvatarPass(UserScore userScore) {
        User user = userProvider.findUserById(userScore.getOwnerUid());
        userScore.setCreateTime(getCurrentTime());
        userScore.setOperateTime(getCurrentTime());
        userScore.setScoreType(PointType.AVATAR.name());
        userScore.setOperatorUid(userScore.getOperatorUid() == null ? userScore.getOwnerUid() : userScore
                .getOperatorUid());
        if (user.getAvatar() != null) {
            // if upload avatar before,skip
            userScore.setScore(0);
            userPointProvider.addUserPoint(userScore);
            return;
        }
        int val = user.getPoints() == null ? 0 : user.getPoints().intValue();
        user.setPoints(val + userScore.getScore());
        user.setLevel(UserLevel.getLevel(user.getPoints()).getCode());
        userProvider.updateUser(user);
        userPointProvider.addUserPoint(userScore);
    }

    private void handleRepeat(UserScore userScore) {
        PointType type = PointType.fromCode(userScore.getScoreType());
        User user = userProvider.findUserById(userScore.getOwnerUid());
        userScore.setCreateTime(getCurrentTime());
        userScore.setOperateTime(getCurrentTime());
        userScore.setScoreType(type.name());
        userScore.setOperatorUid(userScore.getOperatorUid() == null ? userScore.getOwnerUid() : userScore
                .getOperatorUid());
        dbProvider.execute((status) -> {
            userPointProvider.addUserPoint(userScore);
            user.setPoints(user.getPoints() + userScore.getScore());
            user.setLevel(UserLevel.getLevel(user.getPoints()).getCode());
            userProvider.updateUser(user);
            return status;
        });
    }

    private static Timestamp getCurrentTime() {
        return new Timestamp(DateHelper.currentGMTTime().getTime());
    }

    private static Timestamp getTodayStartTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        return new Timestamp(cal.getTimeInMillis());
    }

    @Override
    public Integer getItemPoint(PointType pointType) {
        return configrationProvider.getIntValue(pointType.getValue(), 0);
    }

}
