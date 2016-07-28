//
// EvhRentalv2ListRentalBillCountCommandResponse.m
//
#import "EvhRentalv2ListRentalBillCountCommandResponse.h"
#import "EvhRentalv2RentalBillCountDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalv2ListRentalBillCountCommandResponse
//

@implementation EvhRentalv2ListRentalBillCountCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRentalv2ListRentalBillCountCommandResponse* obj = [EvhRentalv2ListRentalBillCountCommandResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _rentalBillCounts = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.rentalBillCounts) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhRentalv2RentalBillCountDTO* item in self.rentalBillCounts) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"rentalBillCounts"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"rentalBillCounts"];
            for(id itemJson in jsonArray) {
                EvhRentalv2RentalBillCountDTO* item = [EvhRentalv2RentalBillCountDTO new];
                
                [item fromJson: itemJson];
                [self.rentalBillCounts addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
