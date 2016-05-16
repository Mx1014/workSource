//
// EvhRejectJoinGroupInvitation.m
//
#import "EvhRejectJoinGroupInvitation.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRejectJoinGroupInvitation
//

@implementation EvhRejectJoinGroupInvitation

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRejectJoinGroupInvitation* obj = [EvhRejectJoinGroupInvitation new];
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
    if(self.groupId)
        [jsonObject setObject: self.groupId forKey: @"groupId"];
    if(self.rejectText)
        [jsonObject setObject: self.rejectText forKey: @"rejectText"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.groupId = [jsonObject objectForKey: @"groupId"];
        if(self.groupId && [self.groupId isEqual:[NSNull null]])
            self.groupId = nil;

        self.rejectText = [jsonObject objectForKey: @"rejectText"];
        if(self.rejectText && [self.rejectText isEqual:[NSNull null]])
            self.rejectText = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
