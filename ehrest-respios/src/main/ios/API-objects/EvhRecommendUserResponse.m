//
// EvhRecommendUserResponse.m
//
#import "EvhRecommendUserResponse.h"
#import "EvhRecommendUserInfo.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRecommendUserResponse
//

@implementation EvhRecommendUserResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRecommendUserResponse* obj = [EvhRecommendUserResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _users = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.users) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhRecommendUserInfo* item in self.users) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"users"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"users"];
            for(id itemJson in jsonArray) {
                EvhRecommendUserInfo* item = [EvhRecommendUserInfo new];
                
                [item fromJson: itemJson];
                [self.users addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
