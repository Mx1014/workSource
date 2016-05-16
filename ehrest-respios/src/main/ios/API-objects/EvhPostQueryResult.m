//
// EvhPostQueryResult.m
//
#import "EvhPostQueryResult.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPostQueryResult
//

@implementation EvhPostQueryResult

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPostQueryResult* obj = [EvhPostQueryResult new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _ids = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.pageAnchor)
        [jsonObject setObject: self.pageAnchor forKey: @"pageAnchor"];
    if(self.ids) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.ids) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"ids"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.pageAnchor = [jsonObject objectForKey: @"pageAnchor"];
        if(self.pageAnchor && [self.pageAnchor isEqual:[NSNull null]])
            self.pageAnchor = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"ids"];
            for(id itemJson in jsonArray) {
                [self.ids addObject: itemJson];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
