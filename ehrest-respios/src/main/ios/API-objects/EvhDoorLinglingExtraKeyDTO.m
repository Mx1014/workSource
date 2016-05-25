//
// EvhDoorLinglingExtraKeyDTO.m
//
#import "EvhDoorLinglingExtraKeyDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDoorLinglingExtraKeyDTO
//

@implementation EvhDoorLinglingExtraKeyDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhDoorLinglingExtraKeyDTO* obj = [EvhDoorLinglingExtraKeyDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _keys = [NSMutableArray new];
        _storeyAuthList = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.keys) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSString* item in self.keys) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"keys"];
    }
    if(self.storeyAuthList) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.storeyAuthList) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"storeyAuthList"];
    }
    if(self.authStorey)
        [jsonObject setObject: self.authStorey forKey: @"authStorey"];
    if(self.authLevel)
        [jsonObject setObject: self.authLevel forKey: @"authLevel"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"keys"];
            for(id itemJson in jsonArray) {
                [self.keys addObject: itemJson];
            }
        }
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"storeyAuthList"];
            for(id itemJson in jsonArray) {
                [self.storeyAuthList addObject: itemJson];
            }
        }
        self.authStorey = [jsonObject objectForKey: @"authStorey"];
        if(self.authStorey && [self.authStorey isEqual:[NSNull null]])
            self.authStorey = nil;

        self.authLevel = [jsonObject objectForKey: @"authLevel"];
        if(self.authLevel && [self.authLevel isEqual:[NSNull null]])
            self.authLevel = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
