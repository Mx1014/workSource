//
// EvhListPersonnelNotJoinGroupCommand.m
//
#import "EvhListPersonnelNotJoinGroupCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPersonnelNotJoinGroupCommand
//

@implementation EvhListPersonnelNotJoinGroupCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListPersonnelNotJoinGroupCommand* obj = [EvhListPersonnelNotJoinGroupCommand new];
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
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.departmentId)
        [jsonObject setObject: self.departmentId forKey: @"departmentId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.departmentId = [jsonObject objectForKey: @"departmentId"];
        if(self.departmentId && [self.departmentId isEqual:[NSNull null]])
            self.departmentId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
