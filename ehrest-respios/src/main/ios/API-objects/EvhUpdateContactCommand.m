//
// EvhUpdateContactCommand.m
//
#import "EvhUpdateContactCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateContactCommand
//

@implementation EvhUpdateContactCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUpdateContactCommand* obj = [EvhUpdateContactCommand new];
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
    if(self.contactId)
        [jsonObject setObject: self.contactId forKey: @"contactId"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.nickName)
        [jsonObject setObject: self.nickName forKey: @"nickName"];
    if(self.avatar)
        [jsonObject setObject: self.avatar forKey: @"avatar"];
    if(self.role)
        [jsonObject setObject: self.role forKey: @"role"];
    if(self.contactGroupId)
        [jsonObject setObject: self.contactGroupId forKey: @"contactGroupId"];
    if(self.employeeNo)
        [jsonObject setObject: self.employeeNo forKey: @"employeeNo"];
    if(self.sex)
        [jsonObject setObject: self.sex forKey: @"sex"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.contactId = [jsonObject objectForKey: @"contactId"];
        if(self.contactId && [self.contactId isEqual:[NSNull null]])
            self.contactId = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.nickName = [jsonObject objectForKey: @"nickName"];
        if(self.nickName && [self.nickName isEqual:[NSNull null]])
            self.nickName = nil;

        self.avatar = [jsonObject objectForKey: @"avatar"];
        if(self.avatar && [self.avatar isEqual:[NSNull null]])
            self.avatar = nil;

        self.role = [jsonObject objectForKey: @"role"];
        if(self.role && [self.role isEqual:[NSNull null]])
            self.role = nil;

        self.contactGroupId = [jsonObject objectForKey: @"contactGroupId"];
        if(self.contactGroupId && [self.contactGroupId isEqual:[NSNull null]])
            self.contactGroupId = nil;

        self.employeeNo = [jsonObject objectForKey: @"employeeNo"];
        if(self.employeeNo && [self.employeeNo isEqual:[NSNull null]])
            self.employeeNo = nil;

        self.sex = [jsonObject objectForKey: @"sex"];
        if(self.sex && [self.sex isEqual:[NSNull null]])
            self.sex = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
