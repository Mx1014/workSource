//
// EvhPropCommunityIdMessageCommand.m
//
#import "EvhPropCommunityIdMessageCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPropCommunityIdMessageCommand
//

@implementation EvhPropCommunityIdMessageCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPropCommunityIdMessageCommand* obj = [EvhPropCommunityIdMessageCommand new];
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
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.message)
        [jsonObject setObject: self.message forKey: @"message"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.message = [jsonObject objectForKey: @"message"];
        if(self.message && [self.message isEqual:[NSNull null]])
            self.message = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
