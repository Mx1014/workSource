//
// EvhAppIdStatusResponse.m
//
#import "EvhAppIdStatusResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAppIdStatusResponse
//

@implementation EvhAppIdStatusResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAppIdStatusResponse* obj = [EvhAppIdStatusResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _appIds = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.appIds) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.appIds) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"appIds"];
    }
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"appIds"];
            for(id itemJson in jsonArray) {
                [self.appIds addObject: itemJson];
            }
        }
        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
