//
// EvhCreateRechargeOrderCommand.m
//
#import "EvhCreateRechargeOrderCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateRechargeOrderCommand
//

@implementation EvhCreateRechargeOrderCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCreateRechargeOrderCommand* obj = [EvhCreateRechargeOrderCommand new];
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
    if(self.amount)
        [jsonObject setObject: self.amount forKey: @"amount"];
    if(self.months)
        [jsonObject setObject: self.months forKey: @"months"];
    if(self.plateNumber)
        [jsonObject setObject: self.plateNumber forKey: @"plateNumber"];
    if(self.ownerName)
        [jsonObject setObject: self.ownerName forKey: @"ownerName"];
    if(self.validityPeriod)
        [jsonObject setObject: self.validityPeriod forKey: @"validityPeriod"];
    if(self.cardType)
        [jsonObject setObject: self.cardType forKey: @"cardType"];
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.amount = [jsonObject objectForKey: @"amount"];
        if(self.amount && [self.amount isEqual:[NSNull null]])
            self.amount = nil;

        self.months = [jsonObject objectForKey: @"months"];
        if(self.months && [self.months isEqual:[NSNull null]])
            self.months = nil;

        self.plateNumber = [jsonObject objectForKey: @"plateNumber"];
        if(self.plateNumber && [self.plateNumber isEqual:[NSNull null]])
            self.plateNumber = nil;

        self.ownerName = [jsonObject objectForKey: @"ownerName"];
        if(self.ownerName && [self.ownerName isEqual:[NSNull null]])
            self.ownerName = nil;

        self.validityPeriod = [jsonObject objectForKey: @"validityPeriod"];
        if(self.validityPeriod && [self.validityPeriod isEqual:[NSNull null]])
            self.validityPeriod = nil;

        self.cardType = [jsonObject objectForKey: @"cardType"];
        if(self.cardType && [self.cardType isEqual:[NSNull null]])
            self.cardType = nil;

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
