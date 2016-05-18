//
// EvhSearchInvitatedUserCommand.m
//
#import "EvhSearchInvitatedUserCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSearchInvitatedUserCommand
//

@implementation EvhSearchInvitatedUserCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSearchInvitatedUserCommand* obj = [EvhSearchInvitatedUserCommand new];
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
    if(self.userPhone)
        [jsonObject setObject: self.userPhone forKey: @"userPhone"];
    if(self.inviterPhone)
        [jsonObject setObject: self.inviterPhone forKey: @"inviterPhone"];
    if(self.pageAnchor)
        [jsonObject setObject: self.pageAnchor forKey: @"pageAnchor"];
    if(self.pageSize)
        [jsonObject setObject: self.pageSize forKey: @"pageSize"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.userPhone = [jsonObject objectForKey: @"userPhone"];
        if(self.userPhone && [self.userPhone isEqual:[NSNull null]])
            self.userPhone = nil;

        self.inviterPhone = [jsonObject objectForKey: @"inviterPhone"];
        if(self.inviterPhone && [self.inviterPhone isEqual:[NSNull null]])
            self.inviterPhone = nil;

        self.pageAnchor = [jsonObject objectForKey: @"pageAnchor"];
        if(self.pageAnchor && [self.pageAnchor isEqual:[NSNull null]])
            self.pageAnchor = nil;

        self.pageSize = [jsonObject objectForKey: @"pageSize"];
        if(self.pageSize && [self.pageSize isEqual:[NSNull null]])
            self.pageSize = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
