//
// EvhPostNewActionData.m
//
#import "EvhPostNewActionData.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPostNewActionData
//

@implementation EvhPostNewActionData

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPostNewActionData* obj = [EvhPostNewActionData new];
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
    if(self.forumId)
        [jsonObject setObject: self.forumId forKey: @"forumId"];
    if(self.actionCategory)
        [jsonObject setObject: self.actionCategory forKey: @"actionCategory"];
    if(self.contentCategory)
        [jsonObject setObject: self.contentCategory forKey: @"contentCategory"];
    if(self.entityTag)
        [jsonObject setObject: self.entityTag forKey: @"entityTag"];
    if(self.displayName)
        [jsonObject setObject: self.displayName forKey: @"displayName"];
    if(self.creatorEntityTag)
        [jsonObject setObject: self.creatorEntityTag forKey: @"creatorEntityTag"];
    if(self.targetEntityTag)
        [jsonObject setObject: self.targetEntityTag forKey: @"targetEntityTag"];
    if(self.visibleRegionType)
        [jsonObject setObject: self.visibleRegionType forKey: @"visibleRegionType"];
    if(self.visibleRegionId)
        [jsonObject setObject: self.visibleRegionId forKey: @"visibleRegionId"];
    if(self.embedAppId)
        [jsonObject setObject: self.embedAppId forKey: @"embedAppId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.forumId = [jsonObject objectForKey: @"forumId"];
        if(self.forumId && [self.forumId isEqual:[NSNull null]])
            self.forumId = nil;

        self.actionCategory = [jsonObject objectForKey: @"actionCategory"];
        if(self.actionCategory && [self.actionCategory isEqual:[NSNull null]])
            self.actionCategory = nil;

        self.contentCategory = [jsonObject objectForKey: @"contentCategory"];
        if(self.contentCategory && [self.contentCategory isEqual:[NSNull null]])
            self.contentCategory = nil;

        self.entityTag = [jsonObject objectForKey: @"entityTag"];
        if(self.entityTag && [self.entityTag isEqual:[NSNull null]])
            self.entityTag = nil;

        self.displayName = [jsonObject objectForKey: @"displayName"];
        if(self.displayName && [self.displayName isEqual:[NSNull null]])
            self.displayName = nil;

        self.creatorEntityTag = [jsonObject objectForKey: @"creatorEntityTag"];
        if(self.creatorEntityTag && [self.creatorEntityTag isEqual:[NSNull null]])
            self.creatorEntityTag = nil;

        self.targetEntityTag = [jsonObject objectForKey: @"targetEntityTag"];
        if(self.targetEntityTag && [self.targetEntityTag isEqual:[NSNull null]])
            self.targetEntityTag = nil;

        self.visibleRegionType = [jsonObject objectForKey: @"visibleRegionType"];
        if(self.visibleRegionType && [self.visibleRegionType isEqual:[NSNull null]])
            self.visibleRegionType = nil;

        self.visibleRegionId = [jsonObject objectForKey: @"visibleRegionId"];
        if(self.visibleRegionId && [self.visibleRegionId isEqual:[NSNull null]])
            self.visibleRegionId = nil;

        self.embedAppId = [jsonObject objectForKey: @"embedAppId"];
        if(self.embedAppId && [self.embedAppId isEqual:[NSNull null]])
            self.embedAppId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
