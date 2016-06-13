//
// EvhApproveCommunityCommand.m
//
#import "EvhApproveCommunityCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhApproveCommunityCommand
//

@implementation EvhApproveCommunityCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhApproveCommunityCommand* obj = [EvhApproveCommunityCommand new];
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
    if(self.operatorRole)
        [jsonObject setObject: self.operatorRole forKey: @"operatorRole"];
    if(self.reason)
        [jsonObject setObject: self.reason forKey: @"reason"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.operatorRole = [jsonObject objectForKey: @"operatorRole"];
        if(self.operatorRole && [self.operatorRole isEqual:[NSNull null]])
            self.operatorRole = nil;

        self.reason = [jsonObject objectForKey: @"reason"];
        if(self.reason && [self.reason isEqual:[NSNull null]])
            self.reason = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
