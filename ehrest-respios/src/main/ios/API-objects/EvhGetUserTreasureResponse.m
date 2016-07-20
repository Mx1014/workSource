//
// EvhGetUserTreasureResponse.m
//
#import "EvhGetUserTreasureResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetUserTreasureResponse
//

@implementation EvhGetUserTreasureResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetUserTreasureResponse* obj = [EvhGetUserTreasureResponse new];
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
    if(self.couponCount)
        [jsonObject setObject: self.couponCount forKey: @"couponCount"];
    if(self.topicFavoriteCount)
        [jsonObject setObject: self.topicFavoriteCount forKey: @"topicFavoriteCount"];
    if(self.sharedCount)
        [jsonObject setObject: self.sharedCount forKey: @"sharedCount"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.couponCount = [jsonObject objectForKey: @"couponCount"];
        if(self.couponCount && [self.couponCount isEqual:[NSNull null]])
            self.couponCount = nil;

        self.topicFavoriteCount = [jsonObject objectForKey: @"topicFavoriteCount"];
        if(self.topicFavoriteCount && [self.topicFavoriteCount isEqual:[NSNull null]])
            self.topicFavoriteCount = nil;

        self.sharedCount = [jsonObject objectForKey: @"sharedCount"];
        if(self.sharedCount && [self.sharedCount isEqual:[NSNull null]])
            self.sharedCount = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
