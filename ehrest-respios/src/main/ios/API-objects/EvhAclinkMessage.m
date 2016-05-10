//
// EvhAclinkMessage.m
//
#import "EvhAclinkMessage.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkMessage
//

@implementation EvhAclinkMessage

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAclinkMessage* obj = [EvhAclinkMessage new];
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
    if(self.cmd)
        [jsonObject setObject: self.cmd forKey: @"cmd"];
    if(self.secretVersion)
        [jsonObject setObject: self.secretVersion forKey: @"secretVersion"];
    if(self.encrypted)
        [jsonObject setObject: self.encrypted forKey: @"encrypted"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.cmd = [jsonObject objectForKey: @"cmd"];
        if(self.cmd && [self.cmd isEqual:[NSNull null]])
            self.cmd = nil;

        self.secretVersion = [jsonObject objectForKey: @"secretVersion"];
        if(self.secretVersion && [self.secretVersion isEqual:[NSNull null]])
            self.secretVersion = nil;

        self.encrypted = [jsonObject objectForKey: @"encrypted"];
        if(self.encrypted && [self.encrypted isEqual:[NSNull null]])
            self.encrypted = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
