//
// EvhAddContactCommand.m
//
#import "EvhAddContactCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddContactCommand
//

@implementation EvhAddContactCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAddContactCommand* obj = [EvhAddContactCommand new];
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
    if(self.enterpriseId)
        [jsonObject setObject: self.enterpriseId forKey: @"enterpriseId"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.employeeNo)
        [jsonObject setObject: self.employeeNo forKey: @"employeeNo"];
    if(self.groupId)
        [jsonObject setObject: self.groupId forKey: @"groupId"];
    if(self.sex)
        [jsonObject setObject: self.sex forKey: @"sex"];
    if(self.phone)
        [jsonObject setObject: self.phone forKey: @"phone"];
    if(self.role)
        [jsonObject setObject: self.role forKey: @"role"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.enterpriseId = [jsonObject objectForKey: @"enterpriseId"];
        if(self.enterpriseId && [self.enterpriseId isEqual:[NSNull null]])
            self.enterpriseId = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.employeeNo = [jsonObject objectForKey: @"employeeNo"];
        if(self.employeeNo && [self.employeeNo isEqual:[NSNull null]])
            self.employeeNo = nil;

        self.groupId = [jsonObject objectForKey: @"groupId"];
        if(self.groupId && [self.groupId isEqual:[NSNull null]])
            self.groupId = nil;

        self.sex = [jsonObject objectForKey: @"sex"];
        if(self.sex && [self.sex isEqual:[NSNull null]])
            self.sex = nil;

        self.phone = [jsonObject objectForKey: @"phone"];
        if(self.phone && [self.phone isEqual:[NSNull null]])
            self.phone = nil;

        self.role = [jsonObject objectForKey: @"role"];
        if(self.role && [self.role isEqual:[NSNull null]])
            self.role = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
