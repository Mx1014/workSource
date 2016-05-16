//
// EvhDeleteRentalSiteItemCommand.m
//
#import "EvhDeleteRentalSiteItemCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteRentalSiteItemCommand
//

@implementation EvhDeleteRentalSiteItemCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhDeleteRentalSiteItemCommand* obj = [EvhDeleteRentalSiteItemCommand new];
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
    if(self.enterpriseCommunityId)
        [jsonObject setObject: self.enterpriseCommunityId forKey: @"enterpriseCommunityId"];
    if(self.siteType)
        [jsonObject setObject: self.siteType forKey: @"siteType"];
    if(self.rentalSiteItemId)
        [jsonObject setObject: self.rentalSiteItemId forKey: @"rentalSiteItemId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.enterpriseCommunityId = [jsonObject objectForKey: @"enterpriseCommunityId"];
        if(self.enterpriseCommunityId && [self.enterpriseCommunityId isEqual:[NSNull null]])
            self.enterpriseCommunityId = nil;

        self.siteType = [jsonObject objectForKey: @"siteType"];
        if(self.siteType && [self.siteType isEqual:[NSNull null]])
            self.siteType = nil;

        self.rentalSiteItemId = [jsonObject objectForKey: @"rentalSiteItemId"];
        if(self.rentalSiteItemId && [self.rentalSiteItemId isEqual:[NSNull null]])
            self.rentalSiteItemId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
