//
// EvhParkingChargeRule.m
//
#import "EvhParkingChargeRule.h"

///////////////////////////////////////////////////////////////////////////////
// EvhParkingChargeRule
//

@implementation EvhParkingChargeRule

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhParkingChargeRule* obj = [EvhParkingChargeRule new];
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
    if(self.month)
        [jsonObject setObject: self.month forKey: @"month"];
    if(self.amount)
        [jsonObject setObject: self.amount forKey: @"amount"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.month = [jsonObject objectForKey: @"month"];
        if(self.month && [self.month isEqual:[NSNull null]])
            self.month = nil;

        self.amount = [jsonObject objectForKey: @"amount"];
        if(self.amount && [self.amount isEqual:[NSNull null]])
            self.amount = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
