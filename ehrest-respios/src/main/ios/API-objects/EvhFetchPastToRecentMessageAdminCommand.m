//
// EvhFetchPastToRecentMessageAdminCommand.m
//
#import "EvhFetchPastToRecentMessageAdminCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFetchPastToRecentMessageAdminCommand
//

@implementation EvhFetchPastToRecentMessageAdminCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhFetchPastToRecentMessageAdminCommand* obj = [EvhFetchPastToRecentMessageAdminCommand new];
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
    [super toJson: jsonObject];
    if(self.userId)
        [jsonObject setObject: self.userId forKey: @"userId"];
    if(self.loginId)
        [jsonObject setObject: self.loginId forKey: @"loginId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        [super fromJson: jsonObject];
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
