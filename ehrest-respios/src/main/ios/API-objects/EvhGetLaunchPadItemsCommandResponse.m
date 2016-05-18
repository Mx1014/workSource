//
// EvhGetLaunchPadItemsCommandResponse.m
//
#import "EvhGetLaunchPadItemsCommandResponse.h"
#import "EvhLaunchPadItemDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetLaunchPadItemsCommandResponse
//

@implementation EvhGetLaunchPadItemsCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetLaunchPadItemsCommandResponse* obj = [EvhGetLaunchPadItemsCommandResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _launchPadItems = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.launchPadItems) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhLaunchPadItemDTO* item in self.launchPadItems) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"launchPadItems"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"launchPadItems"];
            for(id itemJson in jsonArray) {
                EvhLaunchPadItemDTO* item = [EvhLaunchPadItemDTO new];
                
                [item fromJson: itemJson];
                [self.launchPadItems addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
