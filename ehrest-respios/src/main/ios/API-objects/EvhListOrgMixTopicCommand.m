//
// EvhListOrgMixTopicCommand.m
//
#import "EvhListOrgMixTopicCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListOrgMixTopicCommand
//

@implementation EvhListOrgMixTopicCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListOrgMixTopicCommand* obj = [EvhListOrgMixTopicCommand new];
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
    if(self.organizationId)
        [jsonObject setObject: self.organizationId forKey: @"organizationId"];
    if(self.mixType)
        [jsonObject setObject: self.mixType forKey: @"mixType"];
    if(self.pageAnchor)
        [jsonObject setObject: self.pageAnchor forKey: @"pageAnchor"];
    if(self.pageSize)
        [jsonObject setObject: self.pageSize forKey: @"pageSize"];
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
        self.organizationId = [jsonObject objectForKey: @"organizationId"];
        if(self.organizationId && [self.organizationId isEqual:[NSNull null]])
            self.organizationId = nil;

        self.mixType = [jsonObject objectForKey: @"mixType"];
        if(self.mixType && [self.mixType isEqual:[NSNull null]])
            self.mixType = nil;

        self.pageAnchor = [jsonObject objectForKey: @"pageAnchor"];
        if(self.pageAnchor && [self.pageAnchor isEqual:[NSNull null]])
            self.pageAnchor = nil;

        self.pageSize = [jsonObject objectForKey: @"pageSize"];
        if(self.pageSize && [self.pageSize isEqual:[NSNull null]])
            self.pageSize = nil;

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
