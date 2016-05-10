//
// EvhCommunityUserResponse.m
//
#import "EvhCommunityUserResponse.h"
#import "EvhCommunityUserDto.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCommunityUserResponse
//

@implementation EvhCommunityUserResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCommunityUserResponse* obj = [EvhCommunityUserResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _userCommunities = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.nextPageAnchor)
        [jsonObject setObject: self.nextPageAnchor forKey: @"nextPageAnchor"];
    if(self.userCommunities) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhCommunityUserDto* item in self.userCommunities) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"userCommunities"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.nextPageAnchor = [jsonObject objectForKey: @"nextPageAnchor"];
        if(self.nextPageAnchor && [self.nextPageAnchor isEqual:[NSNull null]])
            self.nextPageAnchor = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"userCommunities"];
            for(id itemJson in jsonArray) {
                EvhCommunityUserDto* item = [EvhCommunityUserDto new];
                
                [item fromJson: itemJson];
                [self.userCommunities addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
