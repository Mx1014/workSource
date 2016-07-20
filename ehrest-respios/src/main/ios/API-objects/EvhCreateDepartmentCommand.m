//
// EvhCreateDepartmentCommand.m
//
#import "EvhCreateDepartmentCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateDepartmentCommand
//

@implementation EvhCreateDepartmentCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCreateDepartmentCommand* obj = [EvhCreateDepartmentCommand new];
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
    if(self.departmentName)
        [jsonObject setObject: self.departmentName forKey: @"departmentName"];
    if(self.parentId)
        [jsonObject setObject: self.parentId forKey: @"parentId"];
    if(self.departmentType)
        [jsonObject setObject: self.departmentType forKey: @"departmentType"];
    if(self.roleId)
        [jsonObject setObject: self.roleId forKey: @"roleId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.departmentName = [jsonObject objectForKey: @"departmentName"];
        if(self.departmentName && [self.departmentName isEqual:[NSNull null]])
            self.departmentName = nil;

        self.parentId = [jsonObject objectForKey: @"parentId"];
        if(self.parentId && [self.parentId isEqual:[NSNull null]])
            self.parentId = nil;

        self.departmentType = [jsonObject objectForKey: @"departmentType"];
        if(self.departmentType && [self.departmentType isEqual:[NSNull null]])
            self.departmentType = nil;

        self.roleId = [jsonObject objectForKey: @"roleId"];
        if(self.roleId && [self.roleId isEqual:[NSNull null]])
            self.roleId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
