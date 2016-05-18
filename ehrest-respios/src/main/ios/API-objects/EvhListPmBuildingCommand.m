//
// EvhListPmBuildingCommand.m
//
#import "EvhListPmBuildingCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPmBuildingCommand
//

@implementation EvhListPmBuildingCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListPmBuildingCommand* obj = [EvhListPmBuildingCommand new];
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
    if(self.orgId)
        [jsonObject setObject: self.orgId forKey: @"orgId"];
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.orgId = [jsonObject objectForKey: @"orgId"];
        if(self.orgId && [self.orgId isEqual:[NSNull null]])
            self.orgId = nil;

        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
