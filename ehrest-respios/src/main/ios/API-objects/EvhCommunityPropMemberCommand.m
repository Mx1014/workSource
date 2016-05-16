//
// EvhCommunityPropMemberCommand.m
//
#import "EvhCommunityPropMemberCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCommunityPropMemberCommand
//

@implementation EvhCommunityPropMemberCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCommunityPropMemberCommand* obj = [EvhCommunityPropMemberCommand new];
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
    if(self.memberId)
        [jsonObject setObject: self.memberId forKey: @"memberId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.memberId = [jsonObject objectForKey: @"memberId"];
        if(self.memberId && [self.memberId isEqual:[NSNull null]])
            self.memberId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
