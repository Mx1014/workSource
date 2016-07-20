//
// EvhBatchCompleteBillCommand.m
//
#import "EvhBatchCompleteBillCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBatchCompleteBillCommand
//

@implementation EvhBatchCompleteBillCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhBatchCompleteBillCommand* obj = [EvhBatchCompleteBillCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _rentalBillIds = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.rentalBillIds) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.rentalBillIds) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"rentalBillIds"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"rentalBillIds"];
            for(id itemJson in jsonArray) {
                [self.rentalBillIds addObject: itemJson];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
