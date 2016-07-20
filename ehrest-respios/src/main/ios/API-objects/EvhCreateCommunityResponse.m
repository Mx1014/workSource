//
// EvhCreateCommunityResponse.m
//
#import "EvhCreateCommunityResponse.h"
#import "EvhCommunityDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateCommunityResponse
//

@implementation EvhCreateCommunityResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCreateCommunityResponse* obj = [EvhCreateCommunityResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.communityDTO) {
        NSMutableDictionary* dic = [NSMutableDictionary new];
        [self.communityDTO toJson: dic];
        
        [jsonObject setObject: dic forKey: @"communityDTO"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        NSMutableDictionary* itemJson =  (NSMutableDictionary*)[jsonObject objectForKey: @"communityDTO"];

        self.communityDTO = [EvhCommunityDTO new];
        self.communityDTO = [self.communityDTO fromJson: itemJson];
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
