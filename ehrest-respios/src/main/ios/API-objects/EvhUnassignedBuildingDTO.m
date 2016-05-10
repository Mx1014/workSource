//
// EvhUnassignedBuildingDTO.m
//
#import "EvhUnassignedBuildingDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUnassignedBuildingDTO
//

@implementation EvhUnassignedBuildingDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUnassignedBuildingDTO* obj = [EvhUnassignedBuildingDTO new];
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
    if(self.buildingId)
        [jsonObject setObject: self.buildingId forKey: @"buildingId"];
    if(self.buildingName)
        [jsonObject setObject: self.buildingName forKey: @"buildingName"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.buildingId = [jsonObject objectForKey: @"buildingId"];
        if(self.buildingId && [self.buildingId isEqual:[NSNull null]])
            self.buildingId = nil;

        self.buildingName = [jsonObject objectForKey: @"buildingName"];
        if(self.buildingName && [self.buildingName isEqual:[NSNull null]])
            self.buildingName = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
