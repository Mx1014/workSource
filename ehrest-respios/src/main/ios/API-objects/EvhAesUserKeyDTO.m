//
// EvhAesUserKeyDTO.m
//
#import "EvhAesUserKeyDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAesUserKeyDTO
//

@implementation EvhAesUserKeyDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAesUserKeyDTO* obj = [EvhAesUserKeyDTO new];
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
    if(self.keyId)
        [jsonObject setObject: self.keyId forKey: @"keyId"];
    if(self.createTimeMs)
        [jsonObject setObject: self.createTimeMs forKey: @"createTimeMs"];
    if(self.creatorUid)
        [jsonObject setObject: self.creatorUid forKey: @"creatorUid"];
    if(self.keyType)
        [jsonObject setObject: self.keyType forKey: @"keyType"];
    if(self.userId)
        [jsonObject setObject: self.userId forKey: @"userId"];
    if(self.secret)
        [jsonObject setObject: self.secret forKey: @"secret"];
    if(self.expireTimeMs)
        [jsonObject setObject: self.expireTimeMs forKey: @"expireTimeMs"];
    if(self.doorId)
        [jsonObject setObject: self.doorId forKey: @"doorId"];
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.hardwareId)
        [jsonObject setObject: self.hardwareId forKey: @"hardwareId"];
    if(self.doorName)
        [jsonObject setObject: self.doorName forKey: @"doorName"];
    if(self.authId)
        [jsonObject setObject: self.authId forKey: @"authId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.keyId = [jsonObject objectForKey: @"keyId"];
        if(self.keyId && [self.keyId isEqual:[NSNull null]])
            self.keyId = nil;

        self.createTimeMs = [jsonObject objectForKey: @"createTimeMs"];
        if(self.createTimeMs && [self.createTimeMs isEqual:[NSNull null]])
            self.createTimeMs = nil;

        self.creatorUid = [jsonObject objectForKey: @"creatorUid"];
        if(self.creatorUid && [self.creatorUid isEqual:[NSNull null]])
            self.creatorUid = nil;

        self.keyType = [jsonObject objectForKey: @"keyType"];
        if(self.keyType && [self.keyType isEqual:[NSNull null]])
            self.keyType = nil;

        self.userId = [jsonObject objectForKey: @"userId"];
        if(self.userId && [self.userId isEqual:[NSNull null]])
            self.userId = nil;

        self.secret = [jsonObject objectForKey: @"secret"];
        if(self.secret && [self.secret isEqual:[NSNull null]])
            self.secret = nil;

        self.expireTimeMs = [jsonObject objectForKey: @"expireTimeMs"];
        if(self.expireTimeMs && [self.expireTimeMs isEqual:[NSNull null]])
            self.expireTimeMs = nil;

        self.doorId = [jsonObject objectForKey: @"doorId"];
        if(self.doorId && [self.doorId isEqual:[NSNull null]])
            self.doorId = nil;

        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.hardwareId = [jsonObject objectForKey: @"hardwareId"];
        if(self.hardwareId && [self.hardwareId isEqual:[NSNull null]])
            self.hardwareId = nil;

        self.doorName = [jsonObject objectForKey: @"doorName"];
        if(self.doorName && [self.doorName isEqual:[NSNull null]])
            self.doorName = nil;

        self.authId = [jsonObject objectForKey: @"authId"];
        if(self.authId && [self.authId isEqual:[NSNull null]])
            self.authId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
