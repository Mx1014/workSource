//
// EvhListResourceCommand.m
//
#import "EvhListResourceCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListResourceCommand
//

@implementation EvhListResourceCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListResourceCommand* obj = [EvhListResourceCommand new];
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
    if(self.userName)
        [jsonObject setObject: self.userName forKey: @"userName"];
    if(self.userContact)
        [jsonObject setObject: self.userContact forKey: @"userContact"];
    if(self.payerId)
        [jsonObject setObject: self.payerId forKey: @"payerId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.userName = [jsonObject objectForKey: @"userName"];
        if(self.userName && [self.userName isEqual:[NSNull null]])
            self.userName = nil;

        self.userContact = [jsonObject objectForKey: @"userContact"];
        if(self.userContact && [self.userContact isEqual:[NSNull null]])
            self.userContact = nil;

        self.payerId = [jsonObject objectForKey: @"payerId"];
        if(self.payerId && [self.payerId isEqual:[NSNull null]])
            self.payerId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
