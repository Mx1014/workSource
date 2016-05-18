//
// EvhCreateParkingChargeCommand.m
//
#import "EvhCreateParkingChargeCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateParkingChargeCommand
//

@implementation EvhCreateParkingChargeCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCreateParkingChargeCommand* obj = [EvhCreateParkingChargeCommand new];
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
    if(self.months)
        [jsonObject setObject: self.months forKey: @"months"];
    if(self.amount)
        [jsonObject setObject: self.amount forKey: @"amount"];
    if(self.cardType)
        [jsonObject setObject: self.cardType forKey: @"cardType"];
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.months = [jsonObject objectForKey: @"months"];
        if(self.months && [self.months isEqual:[NSNull null]])
            self.months = nil;

        self.amount = [jsonObject objectForKey: @"amount"];
        if(self.amount && [self.amount isEqual:[NSNull null]])
            self.amount = nil;

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
