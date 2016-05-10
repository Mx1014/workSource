//
// EvhListPropPostCommandResponse.m
//
#import "EvhListPropPostCommandResponse.h"
#import "EvhPropertyPostDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPropPostCommandResponse
//

@implementation EvhListPropPostCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListPropPostCommandResponse* obj = [EvhListPropPostCommandResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _posts = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.nextPageOffset)
        [jsonObject setObject: self.nextPageOffset forKey: @"nextPageOffset"];
    if(self.posts) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhPropertyPostDTO* item in self.posts) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"posts"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.nextPageOffset = [jsonObject objectForKey: @"nextPageOffset"];
        if(self.nextPageOffset && [self.nextPageOffset isEqual:[NSNull null]])
            self.nextPageOffset = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"posts"];
            for(id itemJson in jsonArray) {
                EvhPropertyPostDTO* item = [EvhPropertyPostDTO new];
                
                [item fromJson: itemJson];
                [self.posts addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
