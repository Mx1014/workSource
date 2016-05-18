//
// EvhResendVerificationCodeCommand.m
//
#import "EvhResendVerificationCodeCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhResendVerificationCodeCommand
//

@implementation EvhResendVerificationCodeCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhResendVerificationCodeCommand* obj = [EvhResendVerificationCodeCommand new];
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
    if(self.signupToken)
        [jsonObject setObject: self.signupToken forKey: @"signupToken"];
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.signupToken = [jsonObject objectForKey: @"signupToken"];
        if(self.signupToken && [self.signupToken isEqual:[NSNull null]])
            self.signupToken = nil;

        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
