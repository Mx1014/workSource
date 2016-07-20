//
// EvhListStatisticsByDateResponse.m
//
#import "EvhListStatisticsByDateResponse.h"
#import "EvhListStatisticsByDateDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListStatisticsByDateResponse
//

@implementation EvhListStatisticsByDateResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListStatisticsByDateResponse* obj = [EvhListStatisticsByDateResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _values = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.values) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhListStatisticsByDateDTO* item in self.values) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"values"];
    }
    if(self.nextPageAnchor)
        [jsonObject setObject: self.nextPageAnchor forKey: @"nextPageAnchor"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"values"];
            for(id itemJson in jsonArray) {
                EvhListStatisticsByDateDTO* item = [EvhListStatisticsByDateDTO new];
                
                [item fromJson: itemJson];
                [self.values addObject: item];
            }
        }
        self.nextPageAnchor = [jsonObject objectForKey: @"nextPageAnchor"];
        if(self.nextPageAnchor && [self.nextPageAnchor isEqual:[NSNull null]])
            self.nextPageAnchor = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
