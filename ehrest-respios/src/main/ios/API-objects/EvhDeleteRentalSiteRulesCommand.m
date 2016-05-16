//
// EvhDeleteRentalSiteRulesCommand.m
//
#import "EvhDeleteRentalSiteRulesCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteRentalSiteRulesCommand
//

@implementation EvhDeleteRentalSiteRulesCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhDeleteRentalSiteRulesCommand* obj = [EvhDeleteRentalSiteRulesCommand new];
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
    if(self.rentalSiteId)
        [jsonObject setObject: self.rentalSiteId forKey: @"rentalSiteId"];
    if(self.ruleDates)
        [jsonObject setObject: self.ruleDates forKey: @"ruleDates"];
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

        self.rentalSiteId = [jsonObject objectForKey: @"rentalSiteId"];
        if(self.rentalSiteId && [self.rentalSiteId isEqual:[NSNull null]])
            self.rentalSiteId = nil;

        self.ruleDates = [jsonObject objectForKey: @"ruleDates"];
        if(self.ruleDates && [self.ruleDates isEqual:[NSNull null]])
            self.ruleDates = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
