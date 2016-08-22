//
// EvhListCommunityResponse.m
//
#import "EvhListCommunityResponse.h"
#import "EvhCommunityDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListCommunityResponse
//

@implementation EvhListCommunityResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListCommunityResponse* obj = [EvhListCommunityResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _communities = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.nextPageAnchor)
        [jsonObject setObject: self.nextPageAnchor forKey: @"nextPageAnchor"];
    if(self.communities) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhCommunityDTO* item in self.communities) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"communities"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.nextPageAnchor = [jsonObject objectForKey: @"nextPageAnchor"];
        if(self.nextPageAnchor && [self.nextPageAnchor isEqual:[NSNull null]])
            self.nextPageAnchor = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"communities"];
            for(id itemJson in jsonArray) {
                EvhCommunityDTO* item = [EvhCommunityDTO new];
                
                [item fromJson: itemJson];
                [self.communities addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
