//
// EvhCreateOrganizationCommand.m
//
#import "EvhCreateOrganizationCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateOrganizationCommand
//

@implementation EvhCreateOrganizationCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCreateOrganizationCommand* obj = [EvhCreateOrganizationCommand new];
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
    if(self.parentId)
        [jsonObject setObject: self.parentId forKey: @"parentId"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.address)
        [jsonObject setObject: self.address forKey: @"address"];
    if(self.groupType)
        [jsonObject setObject: self.groupType forKey: @"groupType"];
    if(self.naviFlag)
        [jsonObject setObject: self.naviFlag forKey: @"naviFlag"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.parentId = [jsonObject objectForKey: @"parentId"];
        if(self.parentId && [self.parentId isEqual:[NSNull null]])
            self.parentId = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.address = [jsonObject objectForKey: @"address"];
        if(self.address && [self.address isEqual:[NSNull null]])
            self.address = nil;

        self.groupType = [jsonObject objectForKey: @"groupType"];
        if(self.groupType && [self.groupType isEqual:[NSNull null]])
            self.groupType = nil;

        self.naviFlag = [jsonObject objectForKey: @"naviFlag"];
        if(self.naviFlag && [self.naviFlag isEqual:[NSNull null]])
            self.naviFlag = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
