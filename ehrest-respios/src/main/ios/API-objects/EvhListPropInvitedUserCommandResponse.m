//
// EvhListPropInvitedUserCommandResponse.m
//
#import "EvhListPropInvitedUserCommandResponse.h"
#import "EvhPropInvitedUserDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPropInvitedUserCommandResponse
//

@implementation EvhListPropInvitedUserCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListPropInvitedUserCommandResponse* obj = [EvhListPropInvitedUserCommandResponse new];
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
    if(self.nextPageAnchor)
        [jsonObject setObject: self.nextPageAnchor forKey: @"nextPageAnchor"];
    if(self.users) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhPropInvitedUserDTO* item in self.users) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"users"];
    }
    if(self.pageCount)
        [jsonObject setObject: self.pageCount forKey: @"pageCount"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.nextPageAnchor = [jsonObject objectForKey: @"nextPageAnchor"];
        if(self.nextPageAnchor && [self.nextPageAnchor isEqual:[NSNull null]])
            self.nextPageAnchor = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"users"];
            for(id itemJson in jsonArray) {
                EvhPropInvitedUserDTO* item = [EvhPropInvitedUserDTO new];
                
                [item fromJson: itemJson];
                [self.users addObject: item];
            }
        }
        self.pageCount = [jsonObject objectForKey: @"pageCount"];
        if(self.pageCount && [self.pageCount isEqual:[NSNull null]])
            self.pageCount = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
