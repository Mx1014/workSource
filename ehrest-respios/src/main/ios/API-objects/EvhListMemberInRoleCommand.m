//
// EvhListMemberInRoleCommand.m
//
#import "EvhListMemberInRoleCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListMemberInRoleCommand
//

@implementation EvhListMemberInRoleCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListMemberInRoleCommand* obj = [EvhListMemberInRoleCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _roleIds = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.groupId)
        [jsonObject setObject: self.groupId forKey: @"groupId"];
    if(self.roleIds) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.roleIds) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"roleIds"];
    }
    if(self.pageAnchor)
        [jsonObject setObject: self.pageAnchor forKey: @"pageAnchor"];
    if(self.pageSize)
        [jsonObject setObject: self.pageSize forKey: @"pageSize"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.groupId = [jsonObject objectForKey: @"groupId"];
        if(self.groupId && [self.groupId isEqual:[NSNull null]])
            self.groupId = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"roleIds"];
            for(id itemJson in jsonArray) {
                [self.roleIds addObject: itemJson];
            }
        }
        self.pageAnchor = [jsonObject objectForKey: @"pageAnchor"];
        if(self.pageAnchor && [self.pageAnchor isEqual:[NSNull null]])
            self.pageAnchor = nil;

        self.pageSize = [jsonObject objectForKey: @"pageSize"];
        if(self.pageSize && [self.pageSize isEqual:[NSNull null]])
            self.pageSize = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
