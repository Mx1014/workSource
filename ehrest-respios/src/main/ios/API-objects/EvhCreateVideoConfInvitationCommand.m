//
// EvhCreateVideoConfInvitationCommand.m
//
#import "EvhCreateVideoConfInvitationCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateVideoConfInvitationCommand
//

@implementation EvhCreateVideoConfInvitationCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCreateVideoConfInvitationCommand* obj = [EvhCreateVideoConfInvitationCommand new];
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
    if(self.reserveConfId)
        [jsonObject setObject: self.reserveConfId forKey: @"reserveConfId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.reserveConfId = [jsonObject objectForKey: @"reserveConfId"];
        if(self.reserveConfId && [self.reserveConfId isEqual:[NSNull null]])
            self.reserveConfId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
