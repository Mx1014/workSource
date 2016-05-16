//
// EvhSyncUserCommand.m
//
#import "EvhSyncUserCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSyncUserCommand
//

@implementation EvhSyncUserCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSyncUserCommand* obj = [EvhSyncUserCommand new];
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
    if(self.crypto)
        [jsonObject setObject: self.crypto forKey: @"crypto"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.phone)
        [jsonObject setObject: self.phone forKey: @"phone"];
    if(self.cityName)
        [jsonObject setObject: self.cityName forKey: @"cityName"];
    if(self.areaName)
        [jsonObject setObject: self.areaName forKey: @"areaName"];
    if(self.communityNames)
        [jsonObject setObject: self.communityNames forKey: @"communityNames"];
    if(self.buildingName)
        [jsonObject setObject: self.buildingName forKey: @"buildingName"];
    if(self.unitName)
        [jsonObject setObject: self.unitName forKey: @"unitName"];
    if(self.apartmentName)
        [jsonObject setObject: self.apartmentName forKey: @"apartmentName"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.crypto = [jsonObject objectForKey: @"crypto"];
        if(self.crypto && [self.crypto isEqual:[NSNull null]])
            self.crypto = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.phone = [jsonObject objectForKey: @"phone"];
        if(self.phone && [self.phone isEqual:[NSNull null]])
            self.phone = nil;

        self.cityName = [jsonObject objectForKey: @"cityName"];
        if(self.cityName && [self.cityName isEqual:[NSNull null]])
            self.cityName = nil;

        self.areaName = [jsonObject objectForKey: @"areaName"];
        if(self.areaName && [self.areaName isEqual:[NSNull null]])
            self.areaName = nil;

        self.communityNames = [jsonObject objectForKey: @"communityNames"];
        if(self.communityNames && [self.communityNames isEqual:[NSNull null]])
            self.communityNames = nil;

        self.buildingName = [jsonObject objectForKey: @"buildingName"];
        if(self.buildingName && [self.buildingName isEqual:[NSNull null]])
            self.buildingName = nil;

        self.unitName = [jsonObject objectForKey: @"unitName"];
        if(self.unitName && [self.unitName isEqual:[NSNull null]])
            self.unitName = nil;

        self.apartmentName = [jsonObject objectForKey: @"apartmentName"];
        if(self.apartmentName && [self.apartmentName isEqual:[NSNull null]])
            self.apartmentName = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
