//
// EvhDepartmentDTO.m
//
#import "EvhDepartmentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDepartmentDTO
//

@implementation EvhDepartmentDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhDepartmentDTO* obj = [EvhDepartmentDTO new];
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
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.departmentName)
        [jsonObject setObject: self.departmentName forKey: @"departmentName"];
    if(self.superiorDepartment)
        [jsonObject setObject: self.superiorDepartment forKey: @"superiorDepartment"];
    if(self.departmentType)
        [jsonObject setObject: self.departmentType forKey: @"departmentType"];
    if(self.role)
        [jsonObject setObject: self.role forKey: @"role"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.departmentName = [jsonObject objectForKey: @"departmentName"];
        if(self.departmentName && [self.departmentName isEqual:[NSNull null]])
            self.departmentName = nil;

        self.superiorDepartment = [jsonObject objectForKey: @"superiorDepartment"];
        if(self.superiorDepartment && [self.superiorDepartment isEqual:[NSNull null]])
            self.superiorDepartment = nil;

        self.departmentType = [jsonObject objectForKey: @"departmentType"];
        if(self.departmentType && [self.departmentType isEqual:[NSNull null]])
            self.departmentType = nil;

        self.role = [jsonObject objectForKey: @"role"];
        if(self.role && [self.role isEqual:[NSNull null]])
            self.role = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
