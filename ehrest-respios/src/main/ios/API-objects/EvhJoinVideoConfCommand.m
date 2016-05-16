//
// EvhJoinVideoConfCommand.m
//
#import "EvhJoinVideoConfCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhJoinVideoConfCommand
//

@implementation EvhJoinVideoConfCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhJoinVideoConfCommand* obj = [EvhJoinVideoConfCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.confId)
        [jsonObject setObject: self.confId forKey: @"confId"];
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.confId = [jsonObject objectForKey: @"confId"];
        if(self.confId && [self.confId isEqual:[NSNull null]])
            self.confId = nil;

        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
