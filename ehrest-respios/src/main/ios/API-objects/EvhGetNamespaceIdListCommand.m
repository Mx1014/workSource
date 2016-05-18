//
// EvhGetNamespaceIdListCommand.m
//
#import "EvhGetNamespaceIdListCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetNamespaceIdListCommand
//

@implementation EvhGetNamespaceIdListCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetNamespaceIdListCommand* obj = [EvhGetNamespaceIdListCommand new];
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
    if(self.userIdentifier)
        [jsonObject setObject: self.userIdentifier forKey: @"userIdentifier"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.userIdentifier = [jsonObject objectForKey: @"userIdentifier"];
        if(self.userIdentifier && [self.userIdentifier isEqual:[NSNull null]])
            self.userIdentifier = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
