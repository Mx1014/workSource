//
// EvhGetLaunchPadItemsCommandResponse.m
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:50 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:53 
>>>>>>> 3.3.x
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
