//
// EvhDoorAccessQRKeyDTO.m
//
#import "EvhDoorAccessQRKeyDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDoorAccessQRKeyDTO
//

@implementation EvhDoorAccessQRKeyDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhDoorAccessQRKeyDTO* obj = [EvhDoorAccessQRKeyDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _hardwares = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.doorGroupId)
        [jsonObject setObject: self.doorGroupId forKey: @"doorGroupId"];
    if(self.doorName)
        [jsonObject setObject: self.doorName forKey: @"doorName"];
    if(self.qrCodeKey)
        [jsonObject setObject: self.qrCodeKey forKey: @"qrCodeKey"];
    if(self.qrDriver)
        [jsonObject setObject: self.qrDriver forKey: @"qrDriver"];
    if(self.creatorUid)
        [jsonObject setObject: self.creatorUid forKey: @"creatorUid"];
    if(self.createTimeMs)
        [jsonObject setObject: self.createTimeMs forKey: @"createTimeMs"];
    if(self.expireTimeMs)
        [jsonObject setObject: self.expireTimeMs forKey: @"expireTimeMs"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.hardwares) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSString* item in self.hardwares) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"hardwares"];
    }
    if(self.extra)
        [jsonObject setObject: self.extra forKey: @"extra"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.doorGroupId = [jsonObject objectForKey: @"doorGroupId"];
        if(self.doorGroupId && [self.doorGroupId isEqual:[NSNull null]])
            self.doorGroupId = nil;

        self.doorName = [jsonObject objectForKey: @"doorName"];
        if(self.doorName && [self.doorName isEqual:[NSNull null]])
            self.doorName = nil;

        self.qrCodeKey = [jsonObject objectForKey: @"qrCodeKey"];
        if(self.qrCodeKey && [self.qrCodeKey isEqual:[NSNull null]])
            self.qrCodeKey = nil;

        self.qrDriver = [jsonObject objectForKey: @"qrDriver"];
        if(self.qrDriver && [self.qrDriver isEqual:[NSNull null]])
            self.qrDriver = nil;

        self.creatorUid = [jsonObject objectForKey: @"creatorUid"];
        if(self.creatorUid && [self.creatorUid isEqual:[NSNull null]])
            self.creatorUid = nil;

        self.createTimeMs = [jsonObject objectForKey: @"createTimeMs"];
        if(self.createTimeMs && [self.createTimeMs isEqual:[NSNull null]])
            self.createTimeMs = nil;

        self.expireTimeMs = [jsonObject objectForKey: @"expireTimeMs"];
        if(self.expireTimeMs && [self.expireTimeMs isEqual:[NSNull null]])
            self.expireTimeMs = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"hardwares"];
            for(id itemJson in jsonArray) {
                [self.hardwares addObject: itemJson];
            }
        }
        self.extra = [jsonObject objectForKey: @"extra"];
        if(self.extra && [self.extra isEqual:[NSNull null]])
            self.extra = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
