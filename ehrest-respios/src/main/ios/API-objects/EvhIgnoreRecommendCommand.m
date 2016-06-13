//
// EvhIgnoreRecommendCommand.m
//
#import "EvhIgnoreRecommendCommand.h"
#import "EvhIgnoreRecommandItem.h"

///////////////////////////////////////////////////////////////////////////////
// EvhIgnoreRecommendCommand
//

@implementation EvhIgnoreRecommendCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhIgnoreRecommendCommand* obj = [EvhIgnoreRecommendCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _recommendItems = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.recommendItems) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhIgnoreRecommandItem* item in self.recommendItems) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"recommendItems"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"recommendItems"];
            for(id itemJson in jsonArray) {
                EvhIgnoreRecommandItem* item = [EvhIgnoreRecommandItem new];
                
                [item fromJson: itemJson];
                [self.recommendItems addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
