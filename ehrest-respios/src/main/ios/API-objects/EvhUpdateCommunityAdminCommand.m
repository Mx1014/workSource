//
// EvhUpdateCommunityAdminCommand.m
//
#import "EvhUpdateCommunityAdminCommand.h"
#import "EvhCommunityGeoPointDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateCommunityAdminCommand
//

@implementation EvhUpdateCommunityAdminCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUpdateCommunityAdminCommand* obj = [EvhUpdateCommunityAdminCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _geoPointList = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.address)
        [jsonObject setObject: self.address forKey: @"address"];
    if(self.cityId)
        [jsonObject setObject: self.cityId forKey: @"cityId"];
    if(self.areaId)
        [jsonObject setObject: self.areaId forKey: @"areaId"];
    if(self.areaSize)
        [jsonObject setObject: self.areaSize forKey: @"areaSize"];
    if(self.geoPointList) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhCommunityGeoPointDTO* item in self.geoPointList) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"geoPointList"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.address = [jsonObject objectForKey: @"address"];
        if(self.address && [self.address isEqual:[NSNull null]])
            self.address = nil;

        self.cityId = [jsonObject objectForKey: @"cityId"];
        if(self.cityId && [self.cityId isEqual:[NSNull null]])
            self.cityId = nil;

        self.areaId = [jsonObject objectForKey: @"areaId"];
        if(self.areaId && [self.areaId isEqual:[NSNull null]])
            self.areaId = nil;

        self.areaSize = [jsonObject objectForKey: @"areaSize"];
        if(self.areaSize && [self.areaSize isEqual:[NSNull null]])
            self.areaSize = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"geoPointList"];
            for(id itemJson in jsonArray) {
                EvhCommunityGeoPointDTO* item = [EvhCommunityGeoPointDTO new];
                
                [item fromJson: itemJson];
                [self.geoPointList addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
