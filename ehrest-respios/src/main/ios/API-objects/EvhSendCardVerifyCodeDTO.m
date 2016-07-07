//
// EvhSendCardVerifyCodeDTO.m
//
#import "EvhSendCardVerifyCodeDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSendCardVerifyCodeDTO
//

@implementation EvhSendCardVerifyCodeDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSendCardVerifyCodeDTO* obj = [EvhSendCardVerifyCodeDTO new];
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
    if(self.verifyCode)
        [jsonObject setObject: self.verifyCode forKey: @"verifyCode"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.verifyCode = [jsonObject objectForKey: @"verifyCode"];
        if(self.verifyCode && [self.verifyCode isEqual:[NSNull null]])
            self.verifyCode = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
