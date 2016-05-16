//
// EvhAddPmBuildingCommand.m
//
#import "EvhAddPmBuildingCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddPmBuildingCommand
//

@implementation EvhAddPmBuildingCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAddPmBuildingCommand* obj = [EvhAddPmBuildingCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _buildingIds = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.isAll)
        [jsonObject setObject: self.isAll forKey: @"isAll"];
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.organizationId)
        [jsonObject setObject: self.organizationId forKey: @"organizationId"];
    if(self.buildingIds) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.buildingIds) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"buildingIds"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.isAll = [jsonObject objectForKey: @"isAll"];
        if(self.isAll && [self.isAll isEqual:[NSNull null]])
            self.isAll = nil;

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.organizationId = [jsonObject objectForKey: @"organizationId"];
        if(self.organizationId && [self.organizationId isEqual:[NSNull null]])
            self.organizationId = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"buildingIds"];
            for(id itemJson in jsonArray) {
                [self.buildingIds addObject: itemJson];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
