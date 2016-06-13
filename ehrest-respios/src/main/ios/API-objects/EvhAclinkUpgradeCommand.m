//
// EvhAclinkUpgradeCommand.m
//
#import "EvhAclinkUpgradeCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkUpgradeCommand
//

@implementation EvhAclinkUpgradeCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAclinkUpgradeCommand* obj = [EvhAclinkUpgradeCommand new];
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
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.message)
        [jsonObject setObject: self.message forKey: @"message"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.message = [jsonObject objectForKey: @"message"];
        if(self.message && [self.message isEqual:[NSNull null]])
            self.message = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
