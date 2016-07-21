//
// EvhRentalListRentalBillsCommandResponse.m
//
#import "EvhRentalListRentalBillsCommandResponse.h"
#import "EvhRentalRentalBillDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalListRentalBillsCommandResponse
//

@implementation EvhRentalListRentalBillsCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRentalListRentalBillsCommandResponse* obj = [EvhRentalListRentalBillsCommandResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _rentalBills = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.nextPageOffset)
        [jsonObject setObject: self.nextPageOffset forKey: @"nextPageOffset"];
    if(self.rentalBills) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhRentalRentalBillDTO* item in self.rentalBills) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"rentalBills"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.nextPageOffset = [jsonObject objectForKey: @"nextPageOffset"];
        if(self.nextPageOffset && [self.nextPageOffset isEqual:[NSNull null]])
            self.nextPageOffset = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"rentalBills"];
            for(id itemJson in jsonArray) {
                EvhRentalRentalBillDTO* item = [EvhRentalRentalBillDTO new];
                
                [item fromJson: itemJson];
                [self.rentalBills addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
