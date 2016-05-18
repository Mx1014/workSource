//
// EvhDoorCommandDTO.m
//
#import "EvhDoorCommandDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDoorCommandDTO
//

@implementation EvhDoorCommandDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhDoorCommandDTO* obj = [EvhDoorCommandDTO new];
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
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.serverKeyVer)
        [jsonObject setObject: self.serverKeyVer forKey: @"serverKeyVer"];
    if(self.ownerType)
        [jsonObject setObject: self.ownerType forKey: @"ownerType"];
    if(self.cmdBody)
        [jsonObject setObject: self.cmdBody forKey: @"cmdBody"];
    if(self.userId)
        [jsonObject setObject: self.userId forKey: @"userId"];
    if(self.cmdResp)
        [jsonObject setObject: self.cmdResp forKey: @"cmdResp"];
    if(self.cmdId)
        [jsonObject setObject: self.cmdId forKey: @"cmdId"];
    if(self.aclinkKeyVer)
        [jsonObject setObject: self.aclinkKeyVer forKey: @"aclinkKeyVer"];
    if(self.cmdType)
        [jsonObject setObject: self.cmdType forKey: @"cmdType"];
    if(self.doorId)
        [jsonObject setObject: self.doorId forKey: @"doorId"];
    if(self.cmdSeq)
        [jsonObject setObject: self.cmdSeq forKey: @"cmdSeq"];
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.ownerId)
        [jsonObject setObject: self.ownerId forKey: @"ownerId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.serverKeyVer = [jsonObject objectForKey: @"serverKeyVer"];
        if(self.serverKeyVer && [self.serverKeyVer isEqual:[NSNull null]])
            self.serverKeyVer = nil;

        self.ownerType = [jsonObject objectForKey: @"ownerType"];
        if(self.ownerType && [self.ownerType isEqual:[NSNull null]])
            self.ownerType = nil;

        self.cmdBody = [jsonObject objectForKey: @"cmdBody"];
        if(self.cmdBody && [self.cmdBody isEqual:[NSNull null]])
            self.cmdBody = nil;

        self.userId = [jsonObject objectForKey: @"userId"];
        if(self.userId && [self.userId isEqual:[NSNull null]])
            self.userId = nil;

        self.cmdResp = [jsonObject objectForKey: @"cmdResp"];
        if(self.cmdResp && [self.cmdResp isEqual:[NSNull null]])
            self.cmdResp = nil;

        self.cmdId = [jsonObject objectForKey: @"cmdId"];
        if(self.cmdId && [self.cmdId isEqual:[NSNull null]])
            self.cmdId = nil;

        self.aclinkKeyVer = [jsonObject objectForKey: @"aclinkKeyVer"];
        if(self.aclinkKeyVer && [self.aclinkKeyVer isEqual:[NSNull null]])
            self.aclinkKeyVer = nil;

        self.cmdType = [jsonObject objectForKey: @"cmdType"];
        if(self.cmdType && [self.cmdType isEqual:[NSNull null]])
            self.cmdType = nil;

        self.doorId = [jsonObject objectForKey: @"doorId"];
        if(self.doorId && [self.doorId isEqual:[NSNull null]])
            self.doorId = nil;

        self.cmdSeq = [jsonObject objectForKey: @"cmdSeq"];
        if(self.cmdSeq && [self.cmdSeq isEqual:[NSNull null]])
            self.cmdSeq = nil;

        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.ownerId = [jsonObject objectForKey: @"ownerId"];
        if(self.ownerId && [self.ownerId isEqual:[NSNull null]])
            self.ownerId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
