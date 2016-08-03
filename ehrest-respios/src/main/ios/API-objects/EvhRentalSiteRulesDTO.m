//
// EvhRentalSiteRulesDTO.m
//
#import "EvhRentalSiteRulesDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalSiteRulesDTO
//

@implementation EvhRentalSiteRulesDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRentalSiteRulesDTO* obj = [EvhRentalSiteRulesDTO new];
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
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.rentalSiteId)
        [jsonObject setObject: self.rentalSiteId forKey: @"rentalSiteId"];
    if(self.rentalType)
        [jsonObject setObject: self.rentalType forKey: @"rentalType"];
    if(self.amorpm)
        [jsonObject setObject: self.amorpm forKey: @"amorpm"];
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
    if(self.originalPrice)
        [jsonObject setObject: self.originalPrice forKey: @"originalPrice"];
    if(self.halfsitePrice)
        [jsonObject setObject: self.halfsitePrice forKey: @"halfsitePrice"];
    if(self.halfsiteOriginalPrice)
        [jsonObject setObject: self.halfsiteOriginalPrice forKey: @"halfsiteOriginalPrice"];
    if(self.exclusiveFlag)
        [jsonObject setObject: self.exclusiveFlag forKey: @"exclusiveFlag"];
    if(self.autoAssign)
        [jsonObject setObject: self.autoAssign forKey: @"autoAssign"];
    if(self.multiUnit)
        [jsonObject setObject: self.multiUnit forKey: @"multiUnit"];
    if(self.multiTimeInterval)
        [jsonObject setObject: self.multiTimeInterval forKey: @"multiTimeInterval"];
    if(self.rentalStep)
        [jsonObject setObject: self.rentalStep forKey: @"rentalStep"];
    if(self.timeStep)
        [jsonObject setObject: self.timeStep forKey: @"timeStep"];
    if(self.ruleDate)
        [jsonObject setObject: self.ruleDate forKey: @"ruleDate"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.siteNumber)
        [jsonObject setObject: self.siteNumber forKey: @"siteNumber"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.rentalSiteId = [jsonObject objectForKey: @"rentalSiteId"];
        if(self.rentalSiteId && [self.rentalSiteId isEqual:[NSNull null]])
            self.rentalSiteId = nil;

        self.rentalType = [jsonObject objectForKey: @"rentalType"];
        if(self.rentalType && [self.rentalType isEqual:[NSNull null]])
            self.rentalType = nil;

        self.amorpm = [jsonObject objectForKey: @"amorpm"];
        if(self.amorpm && [self.amorpm isEqual:[NSNull null]])
            self.amorpm = nil;

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

        self.originalPrice = [jsonObject objectForKey: @"originalPrice"];
        if(self.originalPrice && [self.originalPrice isEqual:[NSNull null]])
            self.originalPrice = nil;

        self.halfsitePrice = [jsonObject objectForKey: @"halfsitePrice"];
        if(self.halfsitePrice && [self.halfsitePrice isEqual:[NSNull null]])
            self.halfsitePrice = nil;

        self.halfsiteOriginalPrice = [jsonObject objectForKey: @"halfsiteOriginalPrice"];
        if(self.halfsiteOriginalPrice && [self.halfsiteOriginalPrice isEqual:[NSNull null]])
            self.halfsiteOriginalPrice = nil;

        self.exclusiveFlag = [jsonObject objectForKey: @"exclusiveFlag"];
        if(self.exclusiveFlag && [self.exclusiveFlag isEqual:[NSNull null]])
            self.exclusiveFlag = nil;

        self.autoAssign = [jsonObject objectForKey: @"autoAssign"];
        if(self.autoAssign && [self.autoAssign isEqual:[NSNull null]])
            self.autoAssign = nil;

        self.multiUnit = [jsonObject objectForKey: @"multiUnit"];
        if(self.multiUnit && [self.multiUnit isEqual:[NSNull null]])
            self.multiUnit = nil;

        self.multiTimeInterval = [jsonObject objectForKey: @"multiTimeInterval"];
        if(self.multiTimeInterval && [self.multiTimeInterval isEqual:[NSNull null]])
            self.multiTimeInterval = nil;

        self.rentalStep = [jsonObject objectForKey: @"rentalStep"];
        if(self.rentalStep && [self.rentalStep isEqual:[NSNull null]])
            self.rentalStep = nil;

        self.timeStep = [jsonObject objectForKey: @"timeStep"];
        if(self.timeStep && [self.timeStep isEqual:[NSNull null]])
            self.timeStep = nil;

        self.ruleDate = [jsonObject objectForKey: @"ruleDate"];
        if(self.ruleDate && [self.ruleDate isEqual:[NSNull null]])
            self.ruleDate = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.siteNumber = [jsonObject objectForKey: @"siteNumber"];
        if(self.siteNumber && [self.siteNumber isEqual:[NSNull null]])
            self.siteNumber = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
