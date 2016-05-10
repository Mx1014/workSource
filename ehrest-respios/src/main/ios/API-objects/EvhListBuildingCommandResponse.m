//
// EvhListBuildingCommandResponse.m
//
#import "EvhListBuildingCommandResponse.h"
#import "EvhCommunityBuildingDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListBuildingCommandResponse
//

@implementation EvhListBuildingCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListBuildingCommandResponse* obj = [EvhListBuildingCommandResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _buildings = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.nextPageAnchor)
        [jsonObject setObject: self.nextPageAnchor forKey: @"nextPageAnchor"];
    if(self.buildings) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhCommunityBuildingDTO* item in self.buildings) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"buildings"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.nextPageAnchor = [jsonObject objectForKey: @"nextPageAnchor"];
        if(self.nextPageAnchor && [self.nextPageAnchor isEqual:[NSNull null]])
            self.nextPageAnchor = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"buildings"];
            for(id itemJson in jsonArray) {
                EvhCommunityBuildingDTO* item = [EvhCommunityBuildingDTO new];
                
                [item fromJson: itemJson];
                [self.buildings addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
