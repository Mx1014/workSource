//
// EvhUpdateCardRechargeOrderCommand.m
//
#import "EvhUpdateCardRechargeOrderCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateCardRechargeOrderCommand
//

@implementation EvhUpdateCardRechargeOrderCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUpdateCardRechargeOrderCommand* obj = [EvhUpdateCardRechargeOrderCommand new];
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
    if(self.rechargeStatus)
        [jsonObject setObject: self.rechargeStatus forKey: @"rechargeStatus"];
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.rechargeStatus = [jsonObject objectForKey: @"rechargeStatus"];
        if(self.rechargeStatus && [self.rechargeStatus isEqual:[NSNull null]])
            self.rechargeStatus = nil;

        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
