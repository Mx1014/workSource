//
// EvhCreateDoorAccessLingLing.m
//
#import "EvhCreateDoorAccessLingLing.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateDoorAccessLingLing
//

@implementation EvhCreateDoorAccessLingLing

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCreateDoorAccessLingLing* obj = [EvhCreateDoorAccessLingLing new];
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
    if(self.ownerType)
        [jsonObject setObject: self.ownerType forKey: @"ownerType"];
    if(self.ownerId)
        [jsonObject setObject: self.ownerId forKey: @"ownerId"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.hardwareId)
        [jsonObject setObject: self.hardwareId forKey: @"hardwareId"];
    if(self.existsId)
        [jsonObject setObject: self.existsId forKey: @"existsId"];
    if(self.doorGroupId)
        [jsonObject setObject: self.doorGroupId forKey: @"doorGroupId"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.address)
        [jsonObject setObject: self.address forKey: @"address"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.ownerType = [jsonObject objectForKey: @"ownerType"];
        if(self.ownerType && [self.ownerType isEqual:[NSNull null]])
            self.ownerType = nil;

        self.ownerId = [jsonObject objectForKey: @"ownerId"];
        if(self.ownerId && [self.ownerId isEqual:[NSNull null]])
            self.ownerId = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.hardwareId = [jsonObject objectForKey: @"hardwareId"];
        if(self.hardwareId && [self.hardwareId isEqual:[NSNull null]])
            self.hardwareId = nil;

        self.existsId = [jsonObject objectForKey: @"existsId"];
        if(self.existsId && [self.existsId isEqual:[NSNull null]])
            self.existsId = nil;

        self.doorGroupId = [jsonObject objectForKey: @"doorGroupId"];
        if(self.doorGroupId && [self.doorGroupId isEqual:[NSNull null]])
            self.doorGroupId = nil;

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
