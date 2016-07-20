//
// EvhBorderListResponse.m
//
#import "EvhBorderListResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBorderListResponse
//

@implementation EvhBorderListResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhBorderListResponse* obj = [EvhBorderListResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _borders = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.borders) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSString* item in self.borders) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"borders"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"borders"];
            for(id itemJson in jsonArray) {
                [self.borders addObject: itemJson];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
