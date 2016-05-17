//
// EvhListBuildingByCommunityIdsCommand.m
//
#import "EvhListBuildingByCommunityIdsCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListBuildingByCommunityIdsCommand
//

@implementation EvhListBuildingByCommunityIdsCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListBuildingByCommunityIdsCommand* obj = [EvhListBuildingByCommunityIdsCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _communityIds = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.communityIds) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.communityIds) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"communityIds"];
    }
    if(self.keyword)
        [jsonObject setObject: self.keyword forKey: @"keyword"];
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"communityIds"];
            for(id itemJson in jsonArray) {
                [self.communityIds addObject: itemJson];
            }
        }
        self.keyword = [jsonObject objectForKey: @"keyword"];
        if(self.keyword && [self.keyword isEqual:[NSNull null]])
            self.keyword = nil;

        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
