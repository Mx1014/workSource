//
// EvhBatchCompleteBillCommandResponse.m
//
#import "EvhBatchCompleteBillCommandResponse.h"
#import "EvhRentalRentalBillDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBatchCompleteBillCommandResponse
//

@implementation EvhBatchCompleteBillCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhBatchCompleteBillCommandResponse* obj = [EvhBatchCompleteBillCommandResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _bills = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.bills) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhRentalRentalBillDTO* item in self.bills) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"bills"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"bills"];
            for(id itemJson in jsonArray) {
                EvhRentalRentalBillDTO* item = [EvhRentalRentalBillDTO new];
                
                [item fromJson: itemJson];
                [self.bills addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
