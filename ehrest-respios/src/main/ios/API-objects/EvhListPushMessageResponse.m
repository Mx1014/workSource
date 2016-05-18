//
// EvhListPushMessageResponse.m
//
#import "EvhListPushMessageResponse.h"
#import "EvhPushMessageDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPushMessageResponse
//

@implementation EvhListPushMessageResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListPushMessageResponse* obj = [EvhListPushMessageResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _pushMessages = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.nextPageAnchor)
        [jsonObject setObject: self.nextPageAnchor forKey: @"nextPageAnchor"];
    if(self.pushMessages) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhPushMessageDTO* item in self.pushMessages) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"pushMessages"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.nextPageAnchor = [jsonObject objectForKey: @"nextPageAnchor"];
        if(self.nextPageAnchor && [self.nextPageAnchor isEqual:[NSNull null]])
            self.nextPageAnchor = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"pushMessages"];
            for(id itemJson in jsonArray) {
                EvhPushMessageDTO* item = [EvhPushMessageDTO new];
                
                [item fromJson: itemJson];
                [self.pushMessages addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
