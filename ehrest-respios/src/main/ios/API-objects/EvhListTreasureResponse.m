//
// EvhListTreasureResponse.m
//
#import "EvhListTreasureResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListTreasureResponse
//

@implementation EvhListTreasureResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListTreasureResponse* obj = [EvhListTreasureResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.points)
        [jsonObject setObject: self.points forKey: @"points"];
    if(self.couponCount)
        [jsonObject setObject: self.couponCount forKey: @"couponCount"];
    if(self.topicFavoriteCount)
        [jsonObject setObject: self.topicFavoriteCount forKey: @"topicFavoriteCount"];
    if(self.sharedCount)
        [jsonObject setObject: self.sharedCount forKey: @"sharedCount"];
    if(self.pointRuleUrl)
        [jsonObject setObject: self.pointRuleUrl forKey: @"pointRuleUrl"];
    if(self.level)
        [jsonObject setObject: self.level forKey: @"level"];
    if(self.myCoupon)
        [jsonObject setObject: self.myCoupon forKey: @"myCoupon"];
    if(self.myOrderUrl)
        [jsonObject setObject: self.myOrderUrl forKey: @"myOrderUrl"];
    if(self.applyShopUrl)
        [jsonObject setObject: self.applyShopUrl forKey: @"applyShopUrl"];
    if(self.isAppliedShop)
        [jsonObject setObject: self.isAppliedShop forKey: @"isAppliedShop"];
    if(self.orderCount)
        [jsonObject setObject: self.orderCount forKey: @"orderCount"];
    if(self.businessUrl)
        [jsonObject setObject: self.businessUrl forKey: @"businessUrl"];
    if(self.businessRealm)
        [jsonObject setObject: self.businessRealm forKey: @"businessRealm"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.points = [jsonObject objectForKey: @"points"];
        if(self.points && [self.points isEqual:[NSNull null]])
            self.points = nil;

        self.couponCount = [jsonObject objectForKey: @"couponCount"];
        if(self.couponCount && [self.couponCount isEqual:[NSNull null]])
            self.couponCount = nil;

        self.topicFavoriteCount = [jsonObject objectForKey: @"topicFavoriteCount"];
        if(self.topicFavoriteCount && [self.topicFavoriteCount isEqual:[NSNull null]])
            self.topicFavoriteCount = nil;

        self.sharedCount = [jsonObject objectForKey: @"sharedCount"];
        if(self.sharedCount && [self.sharedCount isEqual:[NSNull null]])
            self.sharedCount = nil;

        self.pointRuleUrl = [jsonObject objectForKey: @"pointRuleUrl"];
        if(self.pointRuleUrl && [self.pointRuleUrl isEqual:[NSNull null]])
            self.pointRuleUrl = nil;

        self.level = [jsonObject objectForKey: @"level"];
        if(self.level && [self.level isEqual:[NSNull null]])
            self.level = nil;

        self.myCoupon = [jsonObject objectForKey: @"myCoupon"];
        if(self.myCoupon && [self.myCoupon isEqual:[NSNull null]])
            self.myCoupon = nil;

        self.myOrderUrl = [jsonObject objectForKey: @"myOrderUrl"];
        if(self.myOrderUrl && [self.myOrderUrl isEqual:[NSNull null]])
            self.myOrderUrl = nil;

        self.applyShopUrl = [jsonObject objectForKey: @"applyShopUrl"];
        if(self.applyShopUrl && [self.applyShopUrl isEqual:[NSNull null]])
            self.applyShopUrl = nil;

        self.isAppliedShop = [jsonObject objectForKey: @"isAppliedShop"];
        if(self.isAppliedShop && [self.isAppliedShop isEqual:[NSNull null]])
            self.isAppliedShop = nil;

        self.orderCount = [jsonObject objectForKey: @"orderCount"];
        if(self.orderCount && [self.orderCount isEqual:[NSNull null]])
            self.orderCount = nil;

        self.businessUrl = [jsonObject objectForKey: @"businessUrl"];
        if(self.businessUrl && [self.businessUrl isEqual:[NSNull null]])
            self.businessUrl = nil;

        self.businessRealm = [jsonObject objectForKey: @"businessRealm"];
        if(self.businessRealm && [self.businessRealm isEqual:[NSNull null]])
            self.businessRealm = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
