//
// EvhGetUserPointResponse.m
//
#import "EvhGetUserPointResponse.h"
#import "EvhUserScoreDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetUserPointResponse
//

@implementation EvhGetUserPointResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetUserPointResponse* obj = [EvhGetUserPointResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _userPoints = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.uid)
        [jsonObject setObject: self.uid forKey: @"uid"];
    if(self.userPoints) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhUserScoreDTO* item in self.userPoints) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"userPoints"];
    }
    if(self.nextPageAnchor)
        [jsonObject setObject: self.nextPageAnchor forKey: @"nextPageAnchor"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.uid = [jsonObject objectForKey: @"uid"];
        if(self.uid && [self.uid isEqual:[NSNull null]])
            self.uid = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"userPoints"];
            for(id itemJson in jsonArray) {
                EvhUserScoreDTO* item = [EvhUserScoreDTO new];
                
                [item fromJson: itemJson];
                [self.userPoints addObject: item];
            }
        }
        self.nextPageAnchor = [jsonObject objectForKey: @"nextPageAnchor"];
        if(self.nextPageAnchor && [self.nextPageAnchor isEqual:[NSNull null]])
            self.nextPageAnchor = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
