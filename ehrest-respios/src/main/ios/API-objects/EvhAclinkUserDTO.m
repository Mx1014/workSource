//
// EvhAclinkUserDTO.m
//
#import "EvhAclinkUserDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkUserDTO
//

@implementation EvhAclinkUserDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAclinkUserDTO* obj = [EvhAclinkUserDTO new];
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
    if(self.userName)
        [jsonObject setObject: self.userName forKey: @"userName"];
    if(self.phone)
        [jsonObject setObject: self.phone forKey: @"phone"];
    if(self.nickName)
        [jsonObject setObject: self.nickName forKey: @"nickName"];
    if(self.authId)
        [jsonObject setObject: self.authId forKey: @"authId"];
    if(self.rightOpen)
        [jsonObject setObject: self.rightOpen forKey: @"rightOpen"];
    if(self.rightVisitor)
        [jsonObject setObject: self.rightVisitor forKey: @"rightVisitor"];
    if(self.rightRemote)
        [jsonObject setObject: self.rightRemote forKey: @"rightRemote"];
    if(self.companyId)
        [jsonObject setObject: self.companyId forKey: @"companyId"];
    if(self.buildingName)
        [jsonObject setObject: self.buildingName forKey: @"buildingName"];
    if(self.buildingId)
        [jsonObject setObject: self.buildingId forKey: @"buildingId"];
    if(self.company)
        [jsonObject setObject: self.company forKey: @"company"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.userName = [jsonObject objectForKey: @"userName"];
        if(self.userName && [self.userName isEqual:[NSNull null]])
            self.userName = nil;

        self.phone = [jsonObject objectForKey: @"phone"];
        if(self.phone && [self.phone isEqual:[NSNull null]])
            self.phone = nil;

        self.nickName = [jsonObject objectForKey: @"nickName"];
        if(self.nickName && [self.nickName isEqual:[NSNull null]])
            self.nickName = nil;

        self.authId = [jsonObject objectForKey: @"authId"];
        if(self.authId && [self.authId isEqual:[NSNull null]])
            self.authId = nil;

        self.rightOpen = [jsonObject objectForKey: @"rightOpen"];
        if(self.rightOpen && [self.rightOpen isEqual:[NSNull null]])
            self.rightOpen = nil;

        self.rightVisitor = [jsonObject objectForKey: @"rightVisitor"];
        if(self.rightVisitor && [self.rightVisitor isEqual:[NSNull null]])
            self.rightVisitor = nil;

        self.rightRemote = [jsonObject objectForKey: @"rightRemote"];
        if(self.rightRemote && [self.rightRemote isEqual:[NSNull null]])
            self.rightRemote = nil;

        self.companyId = [jsonObject objectForKey: @"companyId"];
        if(self.companyId && [self.companyId isEqual:[NSNull null]])
            self.companyId = nil;

        self.buildingName = [jsonObject objectForKey: @"buildingName"];
        if(self.buildingName && [self.buildingName isEqual:[NSNull null]])
            self.buildingName = nil;

        self.buildingId = [jsonObject objectForKey: @"buildingId"];
        if(self.buildingId && [self.buildingId isEqual:[NSNull null]])
            self.buildingId = nil;

        self.company = [jsonObject objectForKey: @"company"];
        if(self.company && [self.company isEqual:[NSNull null]])
            self.company = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
