//
// EvhSendUserTestSmsCommand.m
//
#import "EvhSendUserTestSmsCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSendUserTestSmsCommand
//

@implementation EvhSendUserTestSmsCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSendUserTestSmsCommand* obj = [EvhSendUserTestSmsCommand new];
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
    if(self.phoneNumber)
        [jsonObject setObject: self.phoneNumber forKey: @"phoneNumber"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        self.phoneNumber = [jsonObject objectForKey: @"phoneNumber"];
        if(self.phoneNumber && [self.phoneNumber isEqual:[NSNull null]])
            self.phoneNumber = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
