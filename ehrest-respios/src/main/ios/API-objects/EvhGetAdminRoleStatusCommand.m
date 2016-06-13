//
// EvhGetAdminRoleStatusCommand.m
//
#import "EvhGetAdminRoleStatusCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetAdminRoleStatusCommand
//

@implementation EvhGetAdminRoleStatusCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetAdminRoleStatusCommand* obj = [EvhGetAdminRoleStatusCommand new];
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
    if(self.groupId)
        [jsonObject setObject: self.groupId forKey: @"groupId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.groupId = [jsonObject objectForKey: @"groupId"];
        if(self.groupId && [self.groupId isEqual:[NSNull null]])
            self.groupId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
