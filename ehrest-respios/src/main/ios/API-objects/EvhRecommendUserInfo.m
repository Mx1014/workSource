//
// EvhRecommendUserInfo.m
//
#import "EvhRecommendUserInfo.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRecommendUserInfo
//

@implementation EvhRecommendUserInfo

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRecommendUserInfo* obj = [EvhRecommendUserInfo new];
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
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.nickName)
        [jsonObject setObject: self.nickName forKey: @"nickName"];
    if(self.avatarUri)
        [jsonObject setObject: self.avatarUri forKey: @"avatarUri"];
    if(self.avatarUrl)
        [jsonObject setObject: self.avatarUrl forKey: @"avatarUrl"];
    if(self.communityName)
        [jsonObject setObject: self.communityName forKey: @"communityName"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.userName)
        [jsonObject setObject: self.userName forKey: @"userName"];
    if(self.userSourceType)
        [jsonObject setObject: self.userSourceType forKey: @"userSourceType"];
    if(self.floorRelation)
        [jsonObject setObject: self.floorRelation forKey: @"floorRelation"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.nickName = [jsonObject objectForKey: @"nickName"];
        if(self.nickName && [self.nickName isEqual:[NSNull null]])
            self.nickName = nil;

        self.avatarUri = [jsonObject objectForKey: @"avatarUri"];
        if(self.avatarUri && [self.avatarUri isEqual:[NSNull null]])
            self.avatarUri = nil;

        self.avatarUrl = [jsonObject objectForKey: @"avatarUrl"];
        if(self.avatarUrl && [self.avatarUrl isEqual:[NSNull null]])
            self.avatarUrl = nil;

        self.communityName = [jsonObject objectForKey: @"communityName"];
        if(self.communityName && [self.communityName isEqual:[NSNull null]])
            self.communityName = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.userName = [jsonObject objectForKey: @"userName"];
        if(self.userName && [self.userName isEqual:[NSNull null]])
            self.userName = nil;

        self.userSourceType = [jsonObject objectForKey: @"userSourceType"];
        if(self.userSourceType && [self.userSourceType isEqual:[NSNull null]])
            self.userSourceType = nil;

        self.floorRelation = [jsonObject objectForKey: @"floorRelation"];
        if(self.floorRelation && [self.floorRelation isEqual:[NSNull null]])
            self.floorRelation = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
