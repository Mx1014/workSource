//
// EvhNotifyEntityCommand.m
//
#import "EvhNotifyEntityCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNotifyEntityCommand
//

@implementation EvhNotifyEntityCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhNotifyEntityCommand* obj = [EvhNotifyEntityCommand new];
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
    if(self.token)
        [jsonObject setObject: self.token forKey: @"token"];
    if(self.sign)
        [jsonObject setObject: self.sign forKey: @"sign"];
    if(self.msg)
        [jsonObject setObject: self.msg forKey: @"msg"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.token = [jsonObject objectForKey: @"token"];
        if(self.token && [self.token isEqual:[NSNull null]])
            self.token = nil;

        self.sign = [jsonObject objectForKey: @"sign"];
        if(self.sign && [self.sign isEqual:[NSNull null]])
            self.sign = nil;

        self.msg = [jsonObject objectForKey: @"msg"];
        if(self.msg && [self.msg isEqual:[NSNull null]])
            self.msg = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
