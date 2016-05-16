//
// EvhVideoConfInvitationResponse.m
//
#import "EvhVideoConfInvitationResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVideoConfInvitationResponse
//

@implementation EvhVideoConfInvitationResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhVideoConfInvitationResponse* obj = [EvhVideoConfInvitationResponse new];
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
    if(self.subject)
        [jsonObject setObject: self.subject forKey: @"subject"];
    if(self.body)
        [jsonObject setObject: self.body forKey: @"body"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.subject = [jsonObject objectForKey: @"subject"];
        if(self.subject && [self.subject isEqual:[NSNull null]])
            self.subject = nil;

        self.body = [jsonObject objectForKey: @"body"];
        if(self.body && [self.body isEqual:[NSNull null]])
            self.body = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
