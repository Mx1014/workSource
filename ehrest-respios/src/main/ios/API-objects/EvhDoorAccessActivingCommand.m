//
// EvhDoorAccessActivingCommand.m
//
#import "EvhDoorAccessActivingCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDoorAccessActivingCommand
//

@implementation EvhDoorAccessActivingCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhDoorAccessActivingCommand* obj = [EvhDoorAccessActivingCommand new];
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
    if(self.hardwareId)
        [jsonObject setObject: self.hardwareId forKey: @"hardwareId"];
    if(self.firwareVer)
        [jsonObject setObject: self.firwareVer forKey: @"firwareVer"];
    if(self.rsaAclinkPub)
        [jsonObject setObject: self.rsaAclinkPub forKey: @"rsaAclinkPub"];
    if(self.ownerType)
        [jsonObject setObject: self.ownerType forKey: @"ownerType"];
    if(self.ownerId)
        [jsonObject setObject: self.ownerId forKey: @"ownerId"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.groupId)
        [jsonObject setObject: self.groupId forKey: @"groupId"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.address)
        [jsonObject setObject: self.address forKey: @"address"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.hardwareId = [jsonObject objectForKey: @"hardwareId"];
        if(self.hardwareId && [self.hardwareId isEqual:[NSNull null]])
            self.hardwareId = nil;

        self.firwareVer = [jsonObject objectForKey: @"firwareVer"];
        if(self.firwareVer && [self.firwareVer isEqual:[NSNull null]])
            self.firwareVer = nil;

        self.rsaAclinkPub = [jsonObject objectForKey: @"rsaAclinkPub"];
        if(self.rsaAclinkPub && [self.rsaAclinkPub isEqual:[NSNull null]])
            self.rsaAclinkPub = nil;

        self.ownerType = [jsonObject objectForKey: @"ownerType"];
        if(self.ownerType && [self.ownerType isEqual:[NSNull null]])
            self.ownerType = nil;

        self.ownerId = [jsonObject objectForKey: @"ownerId"];
        if(self.ownerId && [self.ownerId isEqual:[NSNull null]])
            self.ownerId = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.groupId = [jsonObject objectForKey: @"groupId"];
        if(self.groupId && [self.groupId isEqual:[NSNull null]])
            self.groupId = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.address = [jsonObject objectForKey: @"address"];
        if(self.address && [self.address isEqual:[NSNull null]])
            self.address = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
