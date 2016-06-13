//
// EvhListMemberCommandResponse.m
//
#import "EvhListMemberCommandResponse.h"
#import "EvhGroupMemberDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListMemberCommandResponse
//

@implementation EvhListMemberCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListMemberCommandResponse* obj = [EvhListMemberCommandResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _members = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.nextPageAnchor)
        [jsonObject setObject: self.nextPageAnchor forKey: @"nextPageAnchor"];
    if(self.members) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhGroupMemberDTO* item in self.members) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"members"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.nextPageAnchor = [jsonObject objectForKey: @"nextPageAnchor"];
        if(self.nextPageAnchor && [self.nextPageAnchor isEqual:[NSNull null]])
            self.nextPageAnchor = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"members"];
            for(id itemJson in jsonArray) {
                EvhGroupMemberDTO* item = [EvhGroupMemberDTO new];
                
                [item fromJson: itemJson];
                [self.members addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
