//
// EvhLoginToken.m
//
#import "EvhLoginToken.h"

///////////////////////////////////////////////////////////////////////////////
// EvhLoginToken
//

@implementation EvhLoginToken

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhLoginToken* obj = [EvhLoginToken new];
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
    if(self.userId)
        [jsonObject setObject: self.userId forKey: @"userId"];
    if(self.loginId)
        [jsonObject setObject: self.loginId forKey: @"loginId"];
    if(self.loginInstanceNumber)
        [jsonObject setObject: self.loginInstanceNumber forKey: @"loginInstanceNumber"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.userId = [jsonObject objectForKey: @"userId"];
        if(self.userId && [self.userId isEqual:[NSNull null]])
            self.userId = nil;

        self.loginId = [jsonObject objectForKey: @"loginId"];
        if(self.loginId && [self.loginId isEqual:[NSNull null]])
            self.loginId = nil;

        self.loginInstanceNumber = [jsonObject objectForKey: @"loginInstanceNumber"];
        if(self.loginInstanceNumber && [self.loginInstanceNumber isEqual:[NSNull null]])
            self.loginInstanceNumber = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
