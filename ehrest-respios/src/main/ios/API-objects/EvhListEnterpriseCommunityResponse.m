//
// EvhListEnterpriseCommunityResponse.m
//
#import "EvhListEnterpriseCommunityResponse.h"
#import "EvhEnterpriseCommunityDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListEnterpriseCommunityResponse
//

@implementation EvhListEnterpriseCommunityResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListEnterpriseCommunityResponse* obj = [EvhListEnterpriseCommunityResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _enterpriseCommunities = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.nextPageAnchor)
        [jsonObject setObject: self.nextPageAnchor forKey: @"nextPageAnchor"];
    if(self.enterpriseCommunities) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhEnterpriseCommunityDTO* item in self.enterpriseCommunities) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"enterpriseCommunities"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.nextPageAnchor = [jsonObject objectForKey: @"nextPageAnchor"];
        if(self.nextPageAnchor && [self.nextPageAnchor isEqual:[NSNull null]])
            self.nextPageAnchor = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"enterpriseCommunities"];
            for(id itemJson in jsonArray) {
                EvhEnterpriseCommunityDTO* item = [EvhEnterpriseCommunityDTO new];
                
                [item fromJson: itemJson];
                [self.enterpriseCommunities addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
