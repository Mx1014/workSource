//
// EvhSendMessageTestCommand.m
//
#import "EvhSendMessageTestCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSendMessageTestCommand
//

@implementation EvhSendMessageTestCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSendMessageTestCommand* obj = [EvhSendMessageTestCommand new];
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
    if(self.userId)
        [jsonObject setObject: self.userId forKey: @"userId"];
    if(self.loginId)
        [jsonObject setObject: self.loginId forKey: @"loginId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        self.userId = [jsonObject objectForKey: @"userId"];
        if(self.userId && [self.userId isEqual:[NSNull null]])
            self.userId = nil;

        self.loginId = [jsonObject objectForKey: @"loginId"];
        if(self.loginId && [self.loginId isEqual:[NSNull null]])
            self.loginId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
