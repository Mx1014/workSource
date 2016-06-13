//
// EvhAddRentalSiteRulesCommand.m
//
#import "EvhAddRentalSiteRulesCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddRentalSiteRulesCommand
//

@implementation EvhAddRentalSiteRulesCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAddRentalSiteRulesCommand* obj = [EvhAddRentalSiteRulesCommand new];
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
    if(self.rentalType)
        [jsonObject setObject: self.rentalType forKey: @"rentalType"];
    if(self.beginDate)
        [jsonObject setObject: self.beginDate forKey: @"beginDate"];
    if(self.endDate)
        [jsonObject setObject: self.endDate forKey: @"endDate"];
    if(self.timeStep)
        [jsonObject setObject: self.timeStep forKey: @"timeStep"];
    if(self.beginTime)
        [jsonObject setObject: self.beginTime forKey: @"beginTime"];
    if(self.endTime)
        [jsonObject setObject: self.endTime forKey: @"endTime"];
    if(self.counts)
        [jsonObject setObject: self.counts forKey: @"counts"];
    if(self.unit)
        [jsonObject setObject: self.unit forKey: @"unit"];
    if(self.price)
        [jsonObject setObject: self.price forKey: @"price"];
    if(self.loopType)
        [jsonObject setObject: self.loopType forKey: @"loopType"];
    if(self.Status)
        [jsonObject setObject: self.Status forKey: @"Status"];
    if(self.choosen)
        [jsonObject setObject: self.choosen forKey: @"choosen"];
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

        self.rentalType = [jsonObject objectForKey: @"rentalType"];
        if(self.rentalType && [self.rentalType isEqual:[NSNull null]])
            self.rentalType = nil;

        self.beginDate = [jsonObject objectForKey: @"beginDate"];
        if(self.beginDate && [self.beginDate isEqual:[NSNull null]])
            self.beginDate = nil;

        self.endDate = [jsonObject objectForKey: @"endDate"];
        if(self.endDate && [self.endDate isEqual:[NSNull null]])
            self.endDate = nil;

        self.timeStep = [jsonObject objectForKey: @"timeStep"];
        if(self.timeStep && [self.timeStep isEqual:[NSNull null]])
            self.timeStep = nil;

        self.beginTime = [jsonObject objectForKey: @"beginTime"];
        if(self.beginTime && [self.beginTime isEqual:[NSNull null]])
            self.beginTime = nil;

        self.endTime = [jsonObject objectForKey: @"endTime"];
        if(self.endTime && [self.endTime isEqual:[NSNull null]])
            self.endTime = nil;

        self.counts = [jsonObject objectForKey: @"counts"];
        if(self.counts && [self.counts isEqual:[NSNull null]])
            self.counts = nil;

        self.unit = [jsonObject objectForKey: @"unit"];
        if(self.unit && [self.unit isEqual:[NSNull null]])
            self.unit = nil;

        self.price = [jsonObject objectForKey: @"price"];
        if(self.price && [self.price isEqual:[NSNull null]])
            self.price = nil;

        self.loopType = [jsonObject objectForKey: @"loopType"];
        if(self.loopType && [self.loopType isEqual:[NSNull null]])
            self.loopType = nil;

        self.Status = [jsonObject objectForKey: @"Status"];
        if(self.Status && [self.Status isEqual:[NSNull null]])
            self.Status = nil;

        self.choosen = [jsonObject objectForKey: @"choosen"];
        if(self.choosen && [self.choosen isEqual:[NSNull null]])
            self.choosen = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
