//
// EvhRegisterLoginCommand.m
//
#import "EvhRegisterLoginCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRegisterLoginCommand
//

@implementation EvhRegisterLoginCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRegisterLoginCommand* obj = [EvhRegisterLoginCommand new];
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
    if(self.borderId)
        [jsonObject setObject: self.borderId forKey: @"borderId"];
    if(self.loginToken)
        [jsonObject setObject: self.loginToken forKey: @"loginToken"];
    if(self.borderSessionId)
        [jsonObject setObject: self.borderSessionId forKey: @"borderSessionId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.borderId = [jsonObject objectForKey: @"borderId"];
        if(self.borderId && [self.borderId isEqual:[NSNull null]])
            self.borderId = nil;

        self.loginToken = [jsonObject objectForKey: @"loginToken"];
        if(self.loginToken && [self.loginToken isEqual:[NSNull null]])
            self.loginToken = nil;

        self.borderSessionId = [jsonObject objectForKey: @"borderSessionId"];
        if(self.borderSessionId && [self.borderSessionId isEqual:[NSNull null]])
            self.borderSessionId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
