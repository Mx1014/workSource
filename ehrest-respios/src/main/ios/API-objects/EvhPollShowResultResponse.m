//
// EvhPollShowResultResponse.m
//
#import "EvhPollShowResultResponse.h"
#import "EvhPollDTO.h"
#import "EvhPollItemDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPollShowResultResponse
//

@implementation EvhPollShowResultResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPollShowResultResponse* obj = [EvhPollShowResultResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _items = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.poll) {
        NSMutableDictionary* dic = [NSMutableDictionary new];
        [self.poll toJson: dic];
        
        [jsonObject setObject: dic forKey: @"poll"];
    }
    if(self.items) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhPollItemDTO* item in self.items) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"items"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        NSMutableDictionary* itemJson =  (NSMutableDictionary*)[jsonObject objectForKey: @"poll"];

        self.poll = [EvhPollDTO new];
        self.poll = [self.poll fromJson: itemJson];
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"items"];
            for(id itemJson in jsonArray) {
                EvhPollItemDTO* item = [EvhPollItemDTO new];
                
                [item fromJson: itemJson];
                [self.items addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
