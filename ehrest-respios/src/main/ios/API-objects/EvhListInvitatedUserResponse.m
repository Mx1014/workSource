//
// EvhListInvitatedUserResponse.m
//
#import "EvhListInvitatedUserResponse.h"
#import "EvhInvitatedUsers.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListInvitatedUserResponse
//

@implementation EvhListInvitatedUserResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListInvitatedUserResponse* obj = [EvhListInvitatedUserResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _invitatedUsers = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.invitatedUsers) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhInvitatedUsers* item in self.invitatedUsers) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"invitatedUsers"];
    }
    if(self.nextPageAnchor)
        [jsonObject setObject: self.nextPageAnchor forKey: @"nextPageAnchor"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"invitatedUsers"];
            for(id itemJson in jsonArray) {
                EvhInvitatedUsers* item = [EvhInvitatedUsers new];
                
                [item fromJson: itemJson];
                [self.invitatedUsers addObject: item];
            }
        }
        self.nextPageAnchor = [jsonObject objectForKey: @"nextPageAnchor"];
        if(self.nextPageAnchor && [self.nextPageAnchor isEqual:[NSNull null]])
            self.nextPageAnchor = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
