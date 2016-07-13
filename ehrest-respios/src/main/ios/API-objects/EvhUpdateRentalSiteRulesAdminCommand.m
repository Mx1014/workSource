//
// EvhUpdateRentalSiteRulesAdminCommand.m
//
#import "EvhUpdateRentalSiteRulesAdminCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateRentalSiteRulesAdminCommand
//

@implementation EvhUpdateRentalSiteRulesAdminCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUpdateRentalSiteRulesAdminCommand* obj = [EvhUpdateRentalSiteRulesAdminCommand new];
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
    if(self.ruleId)
        [jsonObject setObject: self.ruleId forKey: @"ruleId"];
    if(self.beginTime)
        [jsonObject setObject: self.beginTime forKey: @"beginTime"];
    if(self.endTime)
        [jsonObject setObject: self.endTime forKey: @"endTime"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.originalPrice)
        [jsonObject setObject: self.originalPrice forKey: @"originalPrice"];
    if(self.price)
        [jsonObject setObject: self.price forKey: @"price"];
    if(self.halfsitePrice)
        [jsonObject setObject: self.halfsitePrice forKey: @"halfsitePrice"];
    if(self.halfsiteOriginalPrice)
        [jsonObject setObject: self.halfsiteOriginalPrice forKey: @"halfsiteOriginalPrice"];
    if(self.counts)
        [jsonObject setObject: self.counts forKey: @"counts"];
    if(self.loopType)
        [jsonObject setObject: self.loopType forKey: @"loopType"];
    if(self.beginDate)
        [jsonObject setObject: self.beginDate forKey: @"beginDate"];
    if(self.endDate)
        [jsonObject setObject: self.endDate forKey: @"endDate"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.ruleId = [jsonObject objectForKey: @"ruleId"];
        if(self.ruleId && [self.ruleId isEqual:[NSNull null]])
            self.ruleId = nil;

        self.beginTime = [jsonObject objectForKey: @"beginTime"];
        if(self.beginTime && [self.beginTime isEqual:[NSNull null]])
            self.beginTime = nil;

        self.endTime = [jsonObject objectForKey: @"endTime"];
        if(self.endTime && [self.endTime isEqual:[NSNull null]])
            self.endTime = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.originalPrice = [jsonObject objectForKey: @"originalPrice"];
        if(self.originalPrice && [self.originalPrice isEqual:[NSNull null]])
            self.originalPrice = nil;

        self.price = [jsonObject objectForKey: @"price"];
        if(self.price && [self.price isEqual:[NSNull null]])
            self.price = nil;

        self.halfsitePrice = [jsonObject objectForKey: @"halfsitePrice"];
        if(self.halfsitePrice && [self.halfsitePrice isEqual:[NSNull null]])
            self.halfsitePrice = nil;

        self.halfsiteOriginalPrice = [jsonObject objectForKey: @"halfsiteOriginalPrice"];
        if(self.halfsiteOriginalPrice && [self.halfsiteOriginalPrice isEqual:[NSNull null]])
            self.halfsiteOriginalPrice = nil;

        self.counts = [jsonObject objectForKey: @"counts"];
        if(self.counts && [self.counts isEqual:[NSNull null]])
            self.counts = nil;

        self.loopType = [jsonObject objectForKey: @"loopType"];
        if(self.loopType && [self.loopType isEqual:[NSNull null]])
            self.loopType = nil;

        self.beginDate = [jsonObject objectForKey: @"beginDate"];
        if(self.beginDate && [self.beginDate isEqual:[NSNull null]])
            self.beginDate = nil;

        self.endDate = [jsonObject objectForKey: @"endDate"];
        if(self.endDate && [self.endDate isEqual:[NSNull null]])
            self.endDate = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
