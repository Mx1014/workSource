//
// EvhCreateDoorAuthCommand.m
//
#import "EvhCreateDoorAuthCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateDoorAuthCommand
//

@implementation EvhCreateDoorAuthCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCreateDoorAuthCommand* obj = [EvhCreateDoorAuthCommand new];
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
    if(self.userId)
        [jsonObject setObject: self.userId forKey: @"userId"];
    if(self.doorId)
        [jsonObject setObject: self.doorId forKey: @"doorId"];
    if(self.approveUserId)
        [jsonObject setObject: self.approveUserId forKey: @"approveUserId"];
    if(self.authType)
        [jsonObject setObject: self.authType forKey: @"authType"];
    if(self.rightOpen)
        [jsonObject setObject: self.rightOpen forKey: @"rightOpen"];
    if(self.rightVisitor)
        [jsonObject setObject: self.rightVisitor forKey: @"rightVisitor"];
    if(self.rightRemote)
        [jsonObject setObject: self.rightRemote forKey: @"rightRemote"];
    if(self.validFromMs)
        [jsonObject setObject: self.validFromMs forKey: @"validFromMs"];
    if(self.validEndMs)
        [jsonObject setObject: self.validEndMs forKey: @"validEndMs"];
    if(self.organization)
        [jsonObject setObject: self.organization forKey: @"organization"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
    if(self.phone)
        [jsonObject setObject: self.phone forKey: @"phone"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.userId = [jsonObject objectForKey: @"userId"];
        if(self.userId && [self.userId isEqual:[NSNull null]])
            self.userId = nil;

        self.doorId = [jsonObject objectForKey: @"doorId"];
        if(self.doorId && [self.doorId isEqual:[NSNull null]])
            self.doorId = nil;

        self.approveUserId = [jsonObject objectForKey: @"approveUserId"];
        if(self.approveUserId && [self.approveUserId isEqual:[NSNull null]])
            self.approveUserId = nil;

        self.authType = [jsonObject objectForKey: @"authType"];
        if(self.authType && [self.authType isEqual:[NSNull null]])
            self.authType = nil;

        self.rightOpen = [jsonObject objectForKey: @"rightOpen"];
        if(self.rightOpen && [self.rightOpen isEqual:[NSNull null]])
            self.rightOpen = nil;

        self.rightVisitor = [jsonObject objectForKey: @"rightVisitor"];
        if(self.rightVisitor && [self.rightVisitor isEqual:[NSNull null]])
            self.rightVisitor = nil;

        self.rightRemote = [jsonObject objectForKey: @"rightRemote"];
        if(self.rightRemote && [self.rightRemote isEqual:[NSNull null]])
            self.rightRemote = nil;

        self.validFromMs = [jsonObject objectForKey: @"validFromMs"];
        if(self.validFromMs && [self.validFromMs isEqual:[NSNull null]])
            self.validFromMs = nil;

        self.validEndMs = [jsonObject objectForKey: @"validEndMs"];
        if(self.validEndMs && [self.validEndMs isEqual:[NSNull null]])
            self.validEndMs = nil;

        self.organization = [jsonObject objectForKey: @"organization"];
        if(self.organization && [self.organization isEqual:[NSNull null]])
            self.organization = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        self.phone = [jsonObject objectForKey: @"phone"];
        if(self.phone && [self.phone isEqual:[NSNull null]])
            self.phone = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
