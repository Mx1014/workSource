//
// EvhCommunityStatusResponse.m
//
#import "EvhCommunityStatusResponse.h"
#import "EvhContact.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCommunityStatusResponse
//

@implementation EvhCommunityStatusResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCommunityStatusResponse* obj = [EvhCommunityStatusResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _contacts = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.userCount)
        [jsonObject setObject: self.userCount forKey: @"userCount"];
    if(self.contacts) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhContact* item in self.contacts) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"contacts"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.userCount = [jsonObject objectForKey: @"userCount"];
        if(self.userCount && [self.userCount isEqual:[NSNull null]])
            self.userCount = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"contacts"];
            for(id itemJson in jsonArray) {
                EvhContact* item = [EvhContact new];
                
                [item fromJson: itemJson];
                [self.contacts addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
