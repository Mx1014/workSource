//
// EvhRentalListRentalBillCountCommandResponse.m
//
#import "EvhRentalListRentalBillCountCommandResponse.h"
#import "EvhRentalBillCountDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalListRentalBillCountCommandResponse
//

@implementation EvhRentalListRentalBillCountCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRentalListRentalBillCountCommandResponse* obj = [EvhRentalListRentalBillCountCommandResponse new];
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
        for(EvhRentalBillCountDTO* item in self.rentalBillCounts) {
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
                EvhRentalBillCountDTO* item = [EvhRentalBillCountDTO new];
                
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
