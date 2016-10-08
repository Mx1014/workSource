//
// EvhListOrganizationAdministratorCommand.m
//
#import "EvhListOrganizationAdministratorCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListOrganizationAdministratorCommand
//

@implementation EvhListOrganizationAdministratorCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListOrganizationAdministratorCommand* obj = [EvhListOrganizationAdministratorCommand new];
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
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.roleIds) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.roleIds) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"roleIds"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"roleIds"];
            for(id itemJson in jsonArray) {
                [self.roleIds addObject: itemJson];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
