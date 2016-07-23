//
// EvhRentalAddRentalSiteSingleSimpleRule.m
//
#import "EvhRentalAddRentalSiteSingleSimpleRule.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalAddRentalSiteSingleSimpleRule
//

@implementation EvhRentalAddRentalSiteSingleSimpleRule

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRentalAddRentalSiteSingleSimpleRule* obj = [EvhRentalAddRentalSiteSingleSimpleRule new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _choosen = [NSMutableArray new];
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
    if(self.rentalStep)
        [jsonObject setObject: self.rentalStep forKey: @"rentalStep"];
    if(self.beginTime)
        [jsonObject setObject: self.beginTime forKey: @"beginTime"];
    if(self.endTime)
        [jsonObject setObject: self.endTime forKey: @"endTime"];
    if(self.counts)
        [jsonObject setObject: self.counts forKey: @"counts"];
    if(self.unit)
        [jsonObject setObject: self.unit forKey: @"unit"];
    if(self.workdayPrice)
        [jsonObject setObject: self.workdayPrice forKey: @"workdayPrice"];
    if(self.weekendPrice)
        [jsonObject setObject: self.weekendPrice forKey: @"weekendPrice"];
    if(self.workdayAMPrice)
        [jsonObject setObject: self.workdayAMPrice forKey: @"workdayAMPrice"];
    if(self.weekendAMPrice)
        [jsonObject setObject: self.weekendAMPrice forKey: @"weekendAMPrice"];
    if(self.workdayPMPrice)
        [jsonObject setObject: self.workdayPMPrice forKey: @"workdayPMPrice"];
    if(self.weekendPMPrice)
        [jsonObject setObject: self.weekendPMPrice forKey: @"weekendPMPrice"];
    if(self.loopType)
        [jsonObject setObject: self.loopType forKey: @"loopType"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.choosen) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.choosen) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"choosen"];
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

        self.rentalStep = [jsonObject objectForKey: @"rentalStep"];
        if(self.rentalStep && [self.rentalStep isEqual:[NSNull null]])
            self.rentalStep = nil;

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

        self.workdayPrice = [jsonObject objectForKey: @"workdayPrice"];
        if(self.workdayPrice && [self.workdayPrice isEqual:[NSNull null]])
            self.workdayPrice = nil;

        self.weekendPrice = [jsonObject objectForKey: @"weekendPrice"];
        if(self.weekendPrice && [self.weekendPrice isEqual:[NSNull null]])
            self.weekendPrice = nil;

        self.workdayAMPrice = [jsonObject objectForKey: @"workdayAMPrice"];
        if(self.workdayAMPrice && [self.workdayAMPrice isEqual:[NSNull null]])
            self.workdayAMPrice = nil;

        self.weekendAMPrice = [jsonObject objectForKey: @"weekendAMPrice"];
        if(self.weekendAMPrice && [self.weekendAMPrice isEqual:[NSNull null]])
            self.weekendAMPrice = nil;

        self.workdayPMPrice = [jsonObject objectForKey: @"workdayPMPrice"];
        if(self.workdayPMPrice && [self.workdayPMPrice isEqual:[NSNull null]])
            self.workdayPMPrice = nil;

        self.weekendPMPrice = [jsonObject objectForKey: @"weekendPMPrice"];
        if(self.weekendPMPrice && [self.weekendPMPrice isEqual:[NSNull null]])
            self.weekendPMPrice = nil;

        self.loopType = [jsonObject objectForKey: @"loopType"];
        if(self.loopType && [self.loopType isEqual:[NSNull null]])
            self.loopType = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"choosen"];
            for(id itemJson in jsonArray) {
                [self.choosen addObject: itemJson];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
