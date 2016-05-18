//
// EvhAclinkDTO.m
//
#import "EvhAclinkDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkDTO
//

@implementation EvhAclinkDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAclinkDTO* obj = [EvhAclinkDTO new];
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
    if(self.deviceName)
        [jsonObject setObject: self.deviceName forKey: @"deviceName"];
    if(self.firwareVer)
        [jsonObject setObject: self.firwareVer forKey: @"firwareVer"];
    if(self.driver)
        [jsonObject setObject: self.driver forKey: @"driver"];
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.createTime)
        [jsonObject setObject: self.createTime forKey: @"createTime"];
    if(self.manufacturer)
        [jsonObject setObject: self.manufacturer forKey: @"manufacturer"];
    if(self.doorId)
        [jsonObject setObject: self.doorId forKey: @"doorId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.deviceName = [jsonObject objectForKey: @"deviceName"];
        if(self.deviceName && [self.deviceName isEqual:[NSNull null]])
            self.deviceName = nil;

        self.firwareVer = [jsonObject objectForKey: @"firwareVer"];
        if(self.firwareVer && [self.firwareVer isEqual:[NSNull null]])
            self.firwareVer = nil;

        self.driver = [jsonObject objectForKey: @"driver"];
        if(self.driver && [self.driver isEqual:[NSNull null]])
            self.driver = nil;

        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.createTime = [jsonObject objectForKey: @"createTime"];
        if(self.createTime && [self.createTime isEqual:[NSNull null]])
            self.createTime = nil;

        self.manufacturer = [jsonObject objectForKey: @"manufacturer"];
        if(self.manufacturer && [self.manufacturer isEqual:[NSNull null]])
            self.manufacturer = nil;

        self.doorId = [jsonObject objectForKey: @"doorId"];
        if(self.doorId && [self.doorId isEqual:[NSNull null]])
            self.doorId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
