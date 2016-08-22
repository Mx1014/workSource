//
// EvhListRentalBillsCommandResponse.m
//
#import "EvhListRentalBillsCommandResponse.h"
#import "EvhRentalv2RentalBillDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListRentalBillsCommandResponse
//

@implementation EvhListRentalBillsCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListRentalBillsCommandResponse* obj = [EvhListRentalBillsCommandResponse new];
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
    if(self.nextPageAnchor)
        [jsonObject setObject: self.nextPageAnchor forKey: @"nextPageAnchor"];
    if(self.rentalBills) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhRentalv2RentalBillDTO* item in self.rentalBills) {
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
        self.nextPageAnchor = [jsonObject objectForKey: @"nextPageAnchor"];
        if(self.nextPageAnchor && [self.nextPageAnchor isEqual:[NSNull null]])
            self.nextPageAnchor = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"rentalBills"];
            for(id itemJson in jsonArray) {
                EvhRentalv2RentalBillDTO* item = [EvhRentalv2RentalBillDTO new];
                
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
