//
// EvhFindRentalSiteWeekStatusCommand.m
//
#import "EvhFindRentalSiteWeekStatusCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFindRentalSiteWeekStatusCommand
//

@implementation EvhFindRentalSiteWeekStatusCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhFindRentalSiteWeekStatusCommand* obj = [EvhFindRentalSiteWeekStatusCommand new];
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
    if(self.ownerType)
        [jsonObject setObject: self.ownerType forKey: @"ownerType"];
    if(self.ownerId)
        [jsonObject setObject: self.ownerId forKey: @"ownerId"];
    if(self.siteType)
        [jsonObject setObject: self.siteType forKey: @"siteType"];
    if(self.siteId)
        [jsonObject setObject: self.siteId forKey: @"siteId"];
    if(self.ruleDate)
        [jsonObject setObject: self.ruleDate forKey: @"ruleDate"];
    if(self.rentalType)
        [jsonObject setObject: self.rentalType forKey: @"rentalType"];
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

        self.siteType = [jsonObject objectForKey: @"siteType"];
        if(self.siteType && [self.siteType isEqual:[NSNull null]])
            self.siteType = nil;

        self.siteId = [jsonObject objectForKey: @"siteId"];
        if(self.siteId && [self.siteId isEqual:[NSNull null]])
            self.siteId = nil;

        self.ruleDate = [jsonObject objectForKey: @"ruleDate"];
        if(self.ruleDate && [self.ruleDate isEqual:[NSNull null]])
            self.ruleDate = nil;

        self.rentalType = [jsonObject objectForKey: @"rentalType"];
        if(self.rentalType && [self.rentalType isEqual:[NSNull null]])
            self.rentalType = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
