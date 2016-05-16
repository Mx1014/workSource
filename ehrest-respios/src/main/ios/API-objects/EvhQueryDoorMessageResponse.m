//
// EvhQueryDoorMessageResponse.m
//
#import "EvhQueryDoorMessageResponse.h"
#import "EvhDoorMessage.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQueryDoorMessageResponse
//

@implementation EvhQueryDoorMessageResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhQueryDoorMessageResponse* obj = [EvhQueryDoorMessageResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _outputs = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.doorId)
        [jsonObject setObject: self.doorId forKey: @"doorId"];
    if(self.outputs) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhDoorMessage* item in self.outputs) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"outputs"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.doorId = [jsonObject objectForKey: @"doorId"];
        if(self.doorId && [self.doorId isEqual:[NSNull null]])
            self.doorId = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"outputs"];
            for(id itemJson in jsonArray) {
                EvhDoorMessage* item = [EvhDoorMessage new];
                
                [item fromJson: itemJson];
                [self.outputs addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
