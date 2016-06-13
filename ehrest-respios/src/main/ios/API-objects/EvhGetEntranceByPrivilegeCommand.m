//
// EvhGetEntranceByPrivilegeCommand.m
//
#import "EvhGetEntranceByPrivilegeCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetEntranceByPrivilegeCommand
//

@implementation EvhGetEntranceByPrivilegeCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetEntranceByPrivilegeCommand* obj = [EvhGetEntranceByPrivilegeCommand new];
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
    if(self.module)
        [jsonObject setObject: self.module forKey: @"module"];
    if(self.sceneToken)
        [jsonObject setObject: self.sceneToken forKey: @"sceneToken"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.module = [jsonObject objectForKey: @"module"];
        if(self.module && [self.module isEqual:[NSNull null]])
            self.module = nil;

        self.sceneToken = [jsonObject objectForKey: @"sceneToken"];
        if(self.sceneToken && [self.sceneToken isEqual:[NSNull null]])
            self.sceneToken = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
