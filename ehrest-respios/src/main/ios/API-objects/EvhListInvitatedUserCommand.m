//
// EvhListInvitatedUserCommand.m
//
#import "EvhListInvitatedUserCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListInvitatedUserCommand
//

@implementation EvhListInvitatedUserCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListInvitatedUserCommand* obj = [EvhListInvitatedUserCommand new];
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
    if(self.anchor)
        [jsonObject setObject: self.anchor forKey: @"anchor"];
    if(self.pageSize)
        [jsonObject setObject: self.pageSize forKey: @"pageSize"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.anchor = [jsonObject objectForKey: @"anchor"];
        if(self.anchor && [self.anchor isEqual:[NSNull null]])
            self.anchor = nil;

        self.pageSize = [jsonObject objectForKey: @"pageSize"];
        if(self.pageSize && [self.pageSize isEqual:[NSNull null]])
            self.pageSize = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
