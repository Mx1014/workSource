//
// EvhListOrderByAccountResponse.m
//
#import "EvhListOrderByAccountResponse.h"
#import "EvhCountAccountOrdersAndMonths.h"
#import "EvhOrderBriefDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListOrderByAccountResponse
//

@implementation EvhListOrderByAccountResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListOrderByAccountResponse* obj = [EvhListOrderByAccountResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _orderBriefs = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.nextPageAnchor)
        [jsonObject setObject: self.nextPageAnchor forKey: @"nextPageAnchor"];
    if(self.counts) {
        NSMutableDictionary* dic = [NSMutableDictionary new];
        [self.counts toJson: dic];
        
        [jsonObject setObject: dic forKey: @"counts"];
    }
    if(self.orderBriefs) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhOrderBriefDTO* item in self.orderBriefs) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"orderBriefs"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.nextPageAnchor = [jsonObject objectForKey: @"nextPageAnchor"];
        if(self.nextPageAnchor && [self.nextPageAnchor isEqual:[NSNull null]])
            self.nextPageAnchor = nil;

        NSMutableDictionary* itemJson =  (NSMutableDictionary*)[jsonObject objectForKey: @"counts"];

        self.counts = [EvhCountAccountOrdersAndMonths new];
        self.counts = [self.counts fromJson: itemJson];
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"orderBriefs"];
            for(id itemJson in jsonArray) {
                EvhOrderBriefDTO* item = [EvhOrderBriefDTO new];
                
                [item fromJson: itemJson];
                [self.orderBriefs addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
