//
// EvhListNeighborUsersCommandResponse.m
//
#import "EvhListNeighborUsersCommandResponse.h"
#import "EvhNeighborUserDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListNeighborUsersCommandResponse
//

@implementation EvhListNeighborUsersCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListNeighborUsersCommandResponse* obj = [EvhListNeighborUsersCommandResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _neighborUserList = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.neighborCount)
        [jsonObject setObject: self.neighborCount forKey: @"neighborCount"];
    if(self.neighborUserList) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhNeighborUserDTO* item in self.neighborUserList) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"neighborUserList"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.neighborCount = [jsonObject objectForKey: @"neighborCount"];
        if(self.neighborCount && [self.neighborCount isEqual:[NSNull null]])
            self.neighborCount = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"neighborUserList"];
            for(id itemJson in jsonArray) {
                EvhNeighborUserDTO* item = [EvhNeighborUserDTO new];
                
                [item fromJson: itemJson];
                [self.neighborUserList addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
