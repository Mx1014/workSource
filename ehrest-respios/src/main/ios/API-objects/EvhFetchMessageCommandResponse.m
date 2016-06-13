//
// EvhFetchMessageCommandResponse.m
//
#import "EvhFetchMessageCommandResponse.h"
#import "EvhMessageDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFetchMessageCommandResponse
//

@implementation EvhFetchMessageCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhFetchMessageCommandResponse* obj = [EvhFetchMessageCommandResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _messages = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.nextPageAnchor)
        [jsonObject setObject: self.nextPageAnchor forKey: @"nextPageAnchor"];
    if(self.messages) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhMessageDTO* item in self.messages) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"messages"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.nextPageAnchor = [jsonObject objectForKey: @"nextPageAnchor"];
        if(self.nextPageAnchor && [self.nextPageAnchor isEqual:[NSNull null]])
            self.nextPageAnchor = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"messages"];
            for(id itemJson in jsonArray) {
                EvhMessageDTO* item = [EvhMessageDTO new];
                
                [item fromJson: itemJson];
                [self.messages addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
