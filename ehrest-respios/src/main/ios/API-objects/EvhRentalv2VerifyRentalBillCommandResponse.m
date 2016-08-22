//
// EvhRentalv2VerifyRentalBillCommandResponse.m
//
#import "EvhRentalv2VerifyRentalBillCommandResponse.h"
#import "EvhRentalv2RentalBillDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalv2VerifyRentalBillCommandResponse
//

@implementation EvhRentalv2VerifyRentalBillCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRentalv2VerifyRentalBillCommandResponse* obj = [EvhRentalv2VerifyRentalBillCommandResponse new];
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
    if(self.addBillCode)
        [jsonObject setObject: self.addBillCode forKey: @"addBillCode"];
    if(self.rentalBill) {
        NSMutableDictionary* dic = [NSMutableDictionary new];
        [self.rentalBill toJson: dic];
        
        [jsonObject setObject: dic forKey: @"rentalBill"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.addBillCode = [jsonObject objectForKey: @"addBillCode"];
        if(self.addBillCode && [self.addBillCode isEqual:[NSNull null]])
            self.addBillCode = nil;

        NSMutableDictionary* itemJson =  (NSMutableDictionary*)[jsonObject objectForKey: @"rentalBill"];

        self.rentalBill = [EvhRentalv2RentalBillDTO new];
        self.rentalBill = [self.rentalBill fromJson: itemJson];
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
