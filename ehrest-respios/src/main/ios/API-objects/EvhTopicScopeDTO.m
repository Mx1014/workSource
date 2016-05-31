//
// EvhTopicScopeDTO.m
//
#import "EvhTopicScopeDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTopicScopeDTO
//

@implementation EvhTopicScopeDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhTopicScopeDTO* obj = [EvhTopicScopeDTO new];
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
    if(self.parentId)
        [jsonObject setObject: self.parentId forKey: @"parentId"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.avatar)
        [jsonObject setObject: self.avatar forKey: @"avatar"];
    if(self.avatarUrl)
        [jsonObject setObject: self.avatarUrl forKey: @"avatarUrl"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.sceneToken)
        [jsonObject setObject: self.sceneToken forKey: @"sceneToken"];
    if(self.forumId)
        [jsonObject setObject: self.forumId forKey: @"forumId"];
    if(self.targetTag)
        [jsonObject setObject: self.targetTag forKey: @"targetTag"];
    if(self.leafFlag)
        [jsonObject setObject: self.leafFlag forKey: @"leafFlag"];
    if(self.defaultFlag)
        [jsonObject setObject: self.defaultFlag forKey: @"defaultFlag"];
    if(self.visibleRegionId)
        [jsonObject setObject: self.visibleRegionId forKey: @"visibleRegionId"];
    if(self.visibleRegionType)
        [jsonObject setObject: self.visibleRegionType forKey: @"visibleRegionType"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.parentId = [jsonObject objectForKey: @"parentId"];
        if(self.parentId && [self.parentId isEqual:[NSNull null]])
            self.parentId = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.avatar = [jsonObject objectForKey: @"avatar"];
        if(self.avatar && [self.avatar isEqual:[NSNull null]])
            self.avatar = nil;

        self.avatarUrl = [jsonObject objectForKey: @"avatarUrl"];
        if(self.avatarUrl && [self.avatarUrl isEqual:[NSNull null]])
            self.avatarUrl = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.sceneToken = [jsonObject objectForKey: @"sceneToken"];
        if(self.sceneToken && [self.sceneToken isEqual:[NSNull null]])
            self.sceneToken = nil;

        self.forumId = [jsonObject objectForKey: @"forumId"];
        if(self.forumId && [self.forumId isEqual:[NSNull null]])
            self.forumId = nil;

        self.targetTag = [jsonObject objectForKey: @"targetTag"];
        if(self.targetTag && [self.targetTag isEqual:[NSNull null]])
            self.targetTag = nil;

        self.leafFlag = [jsonObject objectForKey: @"leafFlag"];
        if(self.leafFlag && [self.leafFlag isEqual:[NSNull null]])
            self.leafFlag = nil;

        self.defaultFlag = [jsonObject objectForKey: @"defaultFlag"];
        if(self.defaultFlag && [self.defaultFlag isEqual:[NSNull null]])
            self.defaultFlag = nil;

        self.visibleRegionId = [jsonObject objectForKey: @"visibleRegionId"];
        if(self.visibleRegionId && [self.visibleRegionId isEqual:[NSNull null]])
            self.visibleRegionId = nil;

        self.visibleRegionType = [jsonObject objectForKey: @"visibleRegionType"];
        if(self.visibleRegionType && [self.visibleRegionType isEqual:[NSNull null]])
            self.visibleRegionType = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
