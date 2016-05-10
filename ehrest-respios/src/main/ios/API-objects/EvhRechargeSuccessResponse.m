//
// EvhRechargeSuccessResponse.m
//
#import "EvhRechargeSuccessResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRechargeSuccessResponse
//

@implementation EvhRechargeSuccessResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRechargeSuccessResponse* obj = [EvhRechargeSuccessResponse new];
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
    if(self.billId)
        [jsonObject setObject: self.billId forKey: @"billId"];
    if(self.validityPeriod)
        [jsonObject setObject: self.validityPeriod forKey: @"validityPeriod"];
    if(self.payStatus)
        [jsonObject setObject: self.payStatus forKey: @"payStatus"];
    if(self.rechargeStatus)
        [jsonObject setObject: self.rechargeStatus forKey: @"rechargeStatus"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.billId = [jsonObject objectForKey: @"billId"];
        if(self.billId && [self.billId isEqual:[NSNull null]])
            self.billId = nil;

        self.validityPeriod = [jsonObject objectForKey: @"validityPeriod"];
        if(self.validityPeriod && [self.validityPeriod isEqual:[NSNull null]])
            self.validityPeriod = nil;

        self.payStatus = [jsonObject objectForKey: @"payStatus"];
        if(self.payStatus && [self.payStatus isEqual:[NSNull null]])
            self.payStatus = nil;

        self.rechargeStatus = [jsonObject objectForKey: @"rechargeStatus"];
        if(self.rechargeStatus && [self.rechargeStatus isEqual:[NSNull null]])
            self.rechargeStatus = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
