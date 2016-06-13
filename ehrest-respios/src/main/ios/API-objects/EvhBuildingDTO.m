//
// EvhBuildingDTO.m
//
#import "EvhBuildingDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBuildingDTO
//

@implementation EvhBuildingDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhBuildingDTO* obj = [EvhBuildingDTO new];
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
    if(self.buildingName)
        [jsonObject setObject: self.buildingName forKey: @"buildingName"];
    if(self.buildingAliasName)
        [jsonObject setObject: self.buildingAliasName forKey: @"buildingAliasName"];
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.buildingName = [jsonObject objectForKey: @"buildingName"];
        if(self.buildingName && [self.buildingName isEqual:[NSNull null]])
            self.buildingName = nil;

        self.buildingAliasName = [jsonObject objectForKey: @"buildingAliasName"];
        if(self.buildingAliasName && [self.buildingAliasName isEqual:[NSNull null]])
            self.buildingAliasName = nil;

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
