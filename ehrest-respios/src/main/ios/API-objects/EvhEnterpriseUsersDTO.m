//
// EvhEnterpriseUsersDTO.m
//
#import "EvhEnterpriseUsersDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhEnterpriseUsersDTO
//

@implementation EvhEnterpriseUsersDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhEnterpriseUsersDTO* obj = [EvhEnterpriseUsersDTO new];
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
    if(self.department)
        [jsonObject setObject: self.department forKey: @"department"];
    if(self.userId)
        [jsonObject setObject: self.userId forKey: @"userId"];
    if(self.userName)
        [jsonObject setObject: self.userName forKey: @"userName"];
    if(self.mobile)
        [jsonObject setObject: self.mobile forKey: @"mobile"];
    if(self.contactId)
        [jsonObject setObject: self.contactId forKey: @"contactId"];
    if(self.enterpriseId)
        [jsonObject setObject: self.enterpriseId forKey: @"enterpriseId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.department = [jsonObject objectForKey: @"department"];
        if(self.department && [self.department isEqual:[NSNull null]])
            self.department = nil;

        self.userId = [jsonObject objectForKey: @"userId"];
        if(self.userId && [self.userId isEqual:[NSNull null]])
            self.userId = nil;

        self.userName = [jsonObject objectForKey: @"userName"];
        if(self.userName && [self.userName isEqual:[NSNull null]])
            self.userName = nil;

        self.mobile = [jsonObject objectForKey: @"mobile"];
        if(self.mobile && [self.mobile isEqual:[NSNull null]])
            self.mobile = nil;

        self.contactId = [jsonObject objectForKey: @"contactId"];
        if(self.contactId && [self.contactId isEqual:[NSNull null]])
            self.contactId = nil;

        self.enterpriseId = [jsonObject objectForKey: @"enterpriseId"];
        if(self.enterpriseId && [self.enterpriseId isEqual:[NSNull null]])
            self.enterpriseId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
