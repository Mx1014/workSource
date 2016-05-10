//
// EvhVerifyRentalBillCommandResponse.m
//
#import "EvhVerifyRentalBillCommandResponse.h"
#import "EvhRentalBillDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVerifyRentalBillCommandResponse
//

@implementation EvhVerifyRentalBillCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhVerifyRentalBillCommandResponse* obj = [EvhVerifyRentalBillCommandResponse new];
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

        self.rentalBill = [EvhRentalBillDTO new];
        self.rentalBill = [self.rentalBill fromJson: itemJson];
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
