//
// EvhAclinkRemoteOpenCommand.m
//
#import "EvhAclinkRemoteOpenCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkRemoteOpenCommand
//

@implementation EvhAclinkRemoteOpenCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAclinkRemoteOpenCommand* obj = [EvhAclinkRemoteOpenCommand new];
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
    if(self.authId)
        [jsonObject setObject: self.authId forKey: @"authId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.authId = [jsonObject objectForKey: @"authId"];
        if(self.authId && [self.authId isEqual:[NSNull null]])
            self.authId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
