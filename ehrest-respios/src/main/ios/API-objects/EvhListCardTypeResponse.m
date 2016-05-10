//
// EvhListCardTypeResponse.m
//
#import "EvhListCardTypeResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListCardTypeResponse
//

@implementation EvhListCardTypeResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListCardTypeResponse* obj = [EvhListCardTypeResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _cardTypes = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.cardTypes) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSString* item in self.cardTypes) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"cardTypes"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"cardTypes"];
            for(id itemJson in jsonArray) {
                [self.cardTypes addObject: itemJson];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
