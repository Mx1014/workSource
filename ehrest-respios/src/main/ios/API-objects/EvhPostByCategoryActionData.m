//
// EvhPostByCategoryActionData.m
//
#import "EvhPostByCategoryActionData.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPostByCategoryActionData
//

@implementation EvhPostByCategoryActionData

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPostByCategoryActionData* obj = [EvhPostByCategoryActionData new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _excludeCategories = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.forumId)
        [jsonObject setObject: self.forumId forKey: @"forumId"];
    if(self.entityTag)
        [jsonObject setObject: self.entityTag forKey: @"entityTag"];
    if(self.displayName)
        [jsonObject setObject: self.displayName forKey: @"displayName"];
    if(self.contentCategory)
        [jsonObject setObject: self.contentCategory forKey: @"contentCategory"];
    if(self.actionCategory)
        [jsonObject setObject: self.actionCategory forKey: @"actionCategory"];
    if(self.organizationId)
        [jsonObject setObject: self.organizationId forKey: @"organizationId"];
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.embeddedAppId)
        [jsonObject setObject: self.embeddedAppId forKey: @"embeddedAppId"];
    if(self.excludeCategories) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.excludeCategories) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"excludeCategories"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.forumId = [jsonObject objectForKey: @"forumId"];
        if(self.forumId && [self.forumId isEqual:[NSNull null]])
            self.forumId = nil;

        self.entityTag = [jsonObject objectForKey: @"entityTag"];
        if(self.entityTag && [self.entityTag isEqual:[NSNull null]])
            self.entityTag = nil;

        self.displayName = [jsonObject objectForKey: @"displayName"];
        if(self.displayName && [self.displayName isEqual:[NSNull null]])
            self.displayName = nil;

        self.contentCategory = [jsonObject objectForKey: @"contentCategory"];
        if(self.contentCategory && [self.contentCategory isEqual:[NSNull null]])
            self.contentCategory = nil;

        self.actionCategory = [jsonObject objectForKey: @"actionCategory"];
        if(self.actionCategory && [self.actionCategory isEqual:[NSNull null]])
            self.actionCategory = nil;

        self.organizationId = [jsonObject objectForKey: @"organizationId"];
        if(self.organizationId && [self.organizationId isEqual:[NSNull null]])
            self.organizationId = nil;

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.embeddedAppId = [jsonObject objectForKey: @"embeddedAppId"];
        if(self.embeddedAppId && [self.embeddedAppId isEqual:[NSNull null]])
            self.embeddedAppId = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"excludeCategories"];
            for(id itemJson in jsonArray) {
                [self.excludeCategories addObject: itemJson];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
