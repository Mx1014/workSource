//
// EvhFindRentalSitesCommand.m
//
#import "EvhFindRentalSitesCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFindRentalSitesCommand
//

@implementation EvhFindRentalSitesCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhFindRentalSitesCommand* obj = [EvhFindRentalSitesCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _status = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.ownerType)
        [jsonObject setObject: self.ownerType forKey: @"ownerType"];
    if(self.ownerId)
        [jsonObject setObject: self.ownerId forKey: @"ownerId"];
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.resourceTypeId)
        [jsonObject setObject: self.resourceTypeId forKey: @"resourceTypeId"];
    if(self.keyword)
        [jsonObject setObject: self.keyword forKey: @"keyword"];
    if(self.anchor)
        [jsonObject setObject: self.anchor forKey: @"anchor"];
    if(self.pageSize)
        [jsonObject setObject: self.pageSize forKey: @"pageSize"];
    if(self.status) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.status) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"status"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.ownerType = [jsonObject objectForKey: @"ownerType"];
        if(self.ownerType && [self.ownerType isEqual:[NSNull null]])
            self.ownerType = nil;

        self.ownerId = [jsonObject objectForKey: @"ownerId"];
        if(self.ownerId && [self.ownerId isEqual:[NSNull null]])
            self.ownerId = nil;

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.resourceTypeId = [jsonObject objectForKey: @"resourceTypeId"];
        if(self.resourceTypeId && [self.resourceTypeId isEqual:[NSNull null]])
            self.resourceTypeId = nil;

        self.keyword = [jsonObject objectForKey: @"keyword"];
        if(self.keyword && [self.keyword isEqual:[NSNull null]])
            self.keyword = nil;

        self.anchor = [jsonObject objectForKey: @"anchor"];
        if(self.anchor && [self.anchor isEqual:[NSNull null]])
            self.anchor = nil;

        self.pageSize = [jsonObject objectForKey: @"pageSize"];
        if(self.pageSize && [self.pageSize isEqual:[NSNull null]])
            self.pageSize = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"status"];
            for(id itemJson in jsonArray) {
                [self.status addObject: itemJson];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
