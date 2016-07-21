//
// EvhFindRentalSiteItemsCommand.m
//
#import "EvhFindRentalSiteItemsCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFindRentalSiteItemsCommand
//

@implementation EvhFindRentalSiteItemsCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhFindRentalSiteItemsCommand* obj = [EvhFindRentalSiteItemsCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _rentalSiteRuleIds = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.rentalSiteId)
        [jsonObject setObject: self.rentalSiteId forKey: @"rentalSiteId"];
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.ownerType)
        [jsonObject setObject: self.ownerType forKey: @"ownerType"];
    if(self.ownerId)
        [jsonObject setObject: self.ownerId forKey: @"ownerId"];
    if(self.siteType)
        [jsonObject setObject: self.siteType forKey: @"siteType"];
    if(self.startTime)
        [jsonObject setObject: self.startTime forKey: @"startTime"];
    if(self.endTime)
        [jsonObject setObject: self.endTime forKey: @"endTime"];
    if(self.rentalSiteRuleIds) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.rentalSiteRuleIds) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"rentalSiteRuleIds"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.rentalSiteId = [jsonObject objectForKey: @"rentalSiteId"];
        if(self.rentalSiteId && [self.rentalSiteId isEqual:[NSNull null]])
            self.rentalSiteId = nil;

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.ownerType = [jsonObject objectForKey: @"ownerType"];
        if(self.ownerType && [self.ownerType isEqual:[NSNull null]])
            self.ownerType = nil;

        self.ownerId = [jsonObject objectForKey: @"ownerId"];
        if(self.ownerId && [self.ownerId isEqual:[NSNull null]])
            self.ownerId = nil;

        self.siteType = [jsonObject objectForKey: @"siteType"];
        if(self.siteType && [self.siteType isEqual:[NSNull null]])
            self.siteType = nil;

        self.startTime = [jsonObject objectForKey: @"startTime"];
        if(self.startTime && [self.startTime isEqual:[NSNull null]])
            self.startTime = nil;

        self.endTime = [jsonObject objectForKey: @"endTime"];
        if(self.endTime && [self.endTime isEqual:[NSNull null]])
            self.endTime = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"rentalSiteRuleIds"];
            for(id itemJson in jsonArray) {
                [self.rentalSiteRuleIds addObject: itemJson];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
