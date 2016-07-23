//
// EvhRentalRentalSiteRulesDTO.m
//
#import "EvhRentalRentalSiteRulesDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalRentalSiteRulesDTO
//

@implementation EvhRentalRentalSiteRulesDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRentalRentalSiteRulesDTO* obj = [EvhRentalRentalSiteRulesDTO new];
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
    if(self.rentalStep)
        [jsonObject setObject: self.rentalStep forKey: @"rentalStep"];
    if(self.timeStep)
        [jsonObject setObject: self.timeStep forKey: @"timeStep"];
    if(self.ruleDate)
        [jsonObject setObject: self.ruleDate forKey: @"ruleDate"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
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

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
