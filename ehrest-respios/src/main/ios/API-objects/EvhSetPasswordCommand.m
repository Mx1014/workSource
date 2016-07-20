//
// EvhSetPasswordCommand.m
//
#import "EvhSetPasswordCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetPasswordCommand
//

@implementation EvhSetPasswordCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSetPasswordCommand* obj = [EvhSetPasswordCommand new];
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
    if(self.oldPassword)
        [jsonObject setObject: self.oldPassword forKey: @"oldPassword"];
    if(self.theNewPassword)
        [jsonObject setObject: self.theNewPassword forKey: @"newPassword"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.oldPassword = [jsonObject objectForKey: @"oldPassword"];
        if(self.oldPassword && [self.oldPassword isEqual:[NSNull null]])
            self.oldPassword = nil;

        self.theNewPassword = [jsonObject objectForKey: @"newPassword"];
        if(self.theNewPassword && [self.theNewPassword isEqual:[NSNull null]])
            self.theNewPassword = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
