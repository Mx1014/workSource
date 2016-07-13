//
// EvhCancelRentalBillCommand.m
//
#import "EvhCancelRentalBillCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCancelRentalBillCommand
//

@implementation EvhCancelRentalBillCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCancelRentalBillCommand* obj = [EvhCancelRentalBillCommand new];
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
