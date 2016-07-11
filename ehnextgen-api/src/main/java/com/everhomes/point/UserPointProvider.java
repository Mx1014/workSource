package com.everhomes.point;

import java.util.List;

import org.jooq.Condition;
import org.jooq.Operator;

import com.everhomes.listing.ListingLocator;

public interface UserPointProvider {
    void addUserPoint(UserScore score);

    // find today record via condition
    UserScore findTodayRecord(Operator op, Condition... conditions);

    // pagination
    List<UserScore> listUserScore(ListingLocator locator, int count, Operator op, Condition... conditions);
}
