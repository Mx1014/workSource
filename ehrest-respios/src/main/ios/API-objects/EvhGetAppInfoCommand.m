//
// EvhGetAppInfoCommand.m
//
#import "EvhGetAppInfoCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetAppInfoCommand
//

@implementation EvhGetAppInfoCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetAppInfoCommand* obj = [EvhGetAppInfoCommand new];
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
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
    if(self.osType)
        [jsonObject setObject: self.osType forKey: @"osType"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        self.osType = [jsonObject objectForKey: @"osType"];
        if(self.osType && [self.osType isEqual:[NSNull null]])
            self.osType = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
