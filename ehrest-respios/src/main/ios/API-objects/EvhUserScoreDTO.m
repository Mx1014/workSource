//
// EvhUserScoreDTO.m
//
#import "EvhUserScoreDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserScoreDTO
//

@implementation EvhUserScoreDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUserScoreDTO* obj = [EvhUserScoreDTO new];
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
    if(self.ownerUid)
        [jsonObject setObject: self.ownerUid forKey: @"ownerUid"];
    if(self.scoreType)
        [jsonObject setObject: self.scoreType forKey: @"scoreType"];
    if(self.score)
        [jsonObject setObject: self.score forKey: @"score"];
    if(self.operatorUid)
        [jsonObject setObject: self.operatorUid forKey: @"operatorUid"];
    if(self.operateTime)
        [jsonObject setObject: self.operateTime forKey: @"operateTime"];
    if(self.behaviorTime)
        [jsonObject setObject: self.behaviorTime forKey: @"behaviorTime"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.ownerUid = [jsonObject objectForKey: @"ownerUid"];
        if(self.ownerUid && [self.ownerUid isEqual:[NSNull null]])
            self.ownerUid = nil;

        self.scoreType = [jsonObject objectForKey: @"scoreType"];
        if(self.scoreType && [self.scoreType isEqual:[NSNull null]])
            self.scoreType = nil;

        self.score = [jsonObject objectForKey: @"score"];
        if(self.score && [self.score isEqual:[NSNull null]])
            self.score = nil;

        self.operatorUid = [jsonObject objectForKey: @"operatorUid"];
        if(self.operatorUid && [self.operatorUid isEqual:[NSNull null]])
            self.operatorUid = nil;

        self.operateTime = [jsonObject objectForKey: @"operateTime"];
        if(self.operateTime && [self.operateTime isEqual:[NSNull null]])
            self.operateTime = nil;

        self.behaviorTime = [jsonObject objectForKey: @"behaviorTime"];
        if(self.behaviorTime && [self.behaviorTime isEqual:[NSNull null]])
            self.behaviorTime = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
