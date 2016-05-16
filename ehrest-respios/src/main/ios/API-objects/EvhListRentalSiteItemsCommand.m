//
// EvhListRentalSiteItemsCommand.m
//
#import "EvhListRentalSiteItemsCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListRentalSiteItemsCommand
//

@implementation EvhListRentalSiteItemsCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListRentalSiteItemsCommand* obj = [EvhListRentalSiteItemsCommand new];
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
    if(self.rentalSiteId)
        [jsonObject setObject: self.rentalSiteId forKey: @"rentalSiteId"];
    if(self.enterpriseCommunityId)
        [jsonObject setObject: self.enterpriseCommunityId forKey: @"enterpriseCommunityId"];
    if(self.siteType)
        [jsonObject setObject: self.siteType forKey: @"siteType"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.rentalSiteId = [jsonObject objectForKey: @"rentalSiteId"];
        if(self.rentalSiteId && [self.rentalSiteId isEqual:[NSNull null]])
            self.rentalSiteId = nil;

        self.enterpriseCommunityId = [jsonObject objectForKey: @"enterpriseCommunityId"];
        if(self.enterpriseCommunityId && [self.enterpriseCommunityId isEqual:[NSNull null]])
            self.enterpriseCommunityId = nil;

        self.siteType = [jsonObject objectForKey: @"siteType"];
        if(self.siteType && [self.siteType isEqual:[NSNull null]])
            self.siteType = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
