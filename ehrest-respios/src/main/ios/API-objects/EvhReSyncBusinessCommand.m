//
// EvhReSyncBusinessCommand.m
//
#import "EvhReSyncBusinessCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhReSyncBusinessCommand
//

@implementation EvhReSyncBusinessCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhReSyncBusinessCommand* obj = [EvhReSyncBusinessCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _namespaceIds = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    [super toJson: jsonObject];
    if(self.userId)
        [jsonObject setObject: self.userId forKey: @"userId"];
    if(self.namespaceIds) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.namespaceIds) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"namespaceIds"];
    }
    if(self.scopeType)
        [jsonObject setObject: self.scopeType forKey: @"scopeType"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        [super fromJson: jsonObject];
        self.userId = [jsonObject objectForKey: @"userId"];
        if(self.userId && [self.userId isEqual:[NSNull null]])
            self.userId = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"namespaceIds"];
            for(id itemJson in jsonArray) {
                [self.namespaceIds addObject: itemJson];
            }
        }
        self.scopeType = [jsonObject objectForKey: @"scopeType"];
        if(self.scopeType && [self.scopeType isEqual:[NSNull null]])
            self.scopeType = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
