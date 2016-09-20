//
// EvhAclinkFirmwareDTO.m
//
#import "EvhAclinkFirmwareDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkFirmwareDTO
//

@implementation EvhAclinkFirmwareDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAclinkFirmwareDTO* obj = [EvhAclinkFirmwareDTO new];
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
    if(self.infoUrl)
        [jsonObject setObject: self.infoUrl forKey: @"infoUrl"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.major)
        [jsonObject setObject: self.major forKey: @"major"];
    if(self.firmwareType)
        [jsonObject setObject: self.firmwareType forKey: @"firmwareType"];
    if(self.checksum)
        [jsonObject setObject: self.checksum forKey: @"checksum"];
    if(self.md5sum)
        [jsonObject setObject: self.md5sum forKey: @"md5sum"];
    if(self.ownerType)
        [jsonObject setObject: self.ownerType forKey: @"ownerType"];
    if(self.behaviorTime)
        [jsonObject setObject: self.behaviorTime forKey: @"behaviorTime"];
    if(self.downloadUrl)
        [jsonObject setObject: self.downloadUrl forKey: @"downloadUrl"];
    if(self.creatorId)
        [jsonObject setObject: self.creatorId forKey: @"creatorId"];
    if(self.ownerId)
        [jsonObject setObject: self.ownerId forKey: @"ownerId"];
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.minor)
        [jsonObject setObject: self.minor forKey: @"minor"];
    if(self.revision)
        [jsonObject setObject: self.revision forKey: @"revision"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.infoUrl = [jsonObject objectForKey: @"infoUrl"];
        if(self.infoUrl && [self.infoUrl isEqual:[NSNull null]])
            self.infoUrl = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.major = [jsonObject objectForKey: @"major"];
        if(self.major && [self.major isEqual:[NSNull null]])
            self.major = nil;

        self.firmwareType = [jsonObject objectForKey: @"firmwareType"];
        if(self.firmwareType && [self.firmwareType isEqual:[NSNull null]])
            self.firmwareType = nil;

        self.checksum = [jsonObject objectForKey: @"checksum"];
        if(self.checksum && [self.checksum isEqual:[NSNull null]])
            self.checksum = nil;

        self.md5sum = [jsonObject objectForKey: @"md5sum"];
        if(self.md5sum && [self.md5sum isEqual:[NSNull null]])
            self.md5sum = nil;

        self.ownerType = [jsonObject objectForKey: @"ownerType"];
        if(self.ownerType && [self.ownerType isEqual:[NSNull null]])
            self.ownerType = nil;

        self.behaviorTime = [jsonObject objectForKey: @"behaviorTime"];
        if(self.behaviorTime && [self.behaviorTime isEqual:[NSNull null]])
            self.behaviorTime = nil;

        self.downloadUrl = [jsonObject objectForKey: @"downloadUrl"];
        if(self.downloadUrl && [self.downloadUrl isEqual:[NSNull null]])
            self.downloadUrl = nil;

        self.creatorId = [jsonObject objectForKey: @"creatorId"];
        if(self.creatorId && [self.creatorId isEqual:[NSNull null]])
            self.creatorId = nil;

        self.ownerId = [jsonObject objectForKey: @"ownerId"];
        if(self.ownerId && [self.ownerId isEqual:[NSNull null]])
            self.ownerId = nil;

        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.minor = [jsonObject objectForKey: @"minor"];
        if(self.minor && [self.minor isEqual:[NSNull null]])
            self.minor = nil;

        self.revision = [jsonObject objectForKey: @"revision"];
        if(self.revision && [self.revision isEqual:[NSNull null]])
            self.revision = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
