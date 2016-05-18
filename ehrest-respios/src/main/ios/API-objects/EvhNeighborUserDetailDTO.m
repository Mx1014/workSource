//
// EvhNeighborUserDetailDTO.m
//
#import "EvhNeighborUserDetailDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNeighborUserDetailDTO
//

@implementation EvhNeighborUserDetailDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhNeighborUserDetailDTO* obj = [EvhNeighborUserDetailDTO new];
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
    [super toJson: jsonObject];
    if(self.longitude)
        [jsonObject setObject: self.longitude forKey: @"longitude"];
    if(self.latitude)
        [jsonObject setObject: self.latitude forKey: @"latitude"];
    if(self.buildingName)
        [jsonObject setObject: self.buildingName forKey: @"buildingName"];
    if(self.apartmentFloor)
        [jsonObject setObject: self.apartmentFloor forKey: @"apartmentFloor"];
    if(self.initial)
        [jsonObject setObject: self.initial forKey: @"initial"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        [super fromJson: jsonObject];
        self.longitude = [jsonObject objectForKey: @"longitude"];
        if(self.longitude && [self.longitude isEqual:[NSNull null]])
            self.longitude = nil;

        self.latitude = [jsonObject objectForKey: @"latitude"];
        if(self.latitude && [self.latitude isEqual:[NSNull null]])
            self.latitude = nil;

        self.buildingName = [jsonObject objectForKey: @"buildingName"];
        if(self.buildingName && [self.buildingName isEqual:[NSNull null]])
            self.buildingName = nil;

        self.apartmentFloor = [jsonObject objectForKey: @"apartmentFloor"];
        if(self.apartmentFloor && [self.apartmentFloor isEqual:[NSNull null]])
            self.apartmentFloor = nil;

        self.initial = [jsonObject objectForKey: @"initial"];
        if(self.initial && [self.initial isEqual:[NSNull null]])
            self.initial = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
