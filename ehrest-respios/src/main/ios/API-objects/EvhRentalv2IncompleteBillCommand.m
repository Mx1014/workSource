//
// EvhRentalv2IncompleteBillCommand.m
//
#import "EvhRentalv2IncompleteBillCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalv2IncompleteBillCommand
//

@implementation EvhRentalv2IncompleteBillCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRentalv2IncompleteBillCommand* obj = [EvhRentalv2IncompleteBillCommand new];
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
    if(self.rentalBillId)
        [jsonObject setObject: self.rentalBillId forKey: @"rentalBillId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.rentalBillId = [jsonObject objectForKey: @"rentalBillId"];
        if(self.rentalBillId && [self.rentalBillId isEqual:[NSNull null]])
            self.rentalBillId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
