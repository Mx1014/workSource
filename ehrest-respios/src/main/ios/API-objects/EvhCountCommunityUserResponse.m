//
// EvhCountCommunityUserResponse.m
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:53 
>>>>>>> 3.3.x
//
#import "EvhCountCommunityUserResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCountCommunityUserResponse
//

@implementation EvhCountCommunityUserResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCountCommunityUserResponse* obj = [EvhCountCommunityUserResponse new];
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
    if(self.communityUsers)
        [jsonObject setObject: self.communityUsers forKey: @"communityUsers"];
    if(self.authUsers)
        [jsonObject setObject: self.authUsers forKey: @"authUsers"];
    if(self.notAuthUsers)
        [jsonObject setObject: self.notAuthUsers forKey: @"notAuthUsers"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.communityUsers = [jsonObject objectForKey: @"communityUsers"];
        if(self.communityUsers && [self.communityUsers isEqual:[NSNull null]])
            self.communityUsers = nil;

        self.authUsers = [jsonObject objectForKey: @"authUsers"];
        if(self.authUsers && [self.authUsers isEqual:[NSNull null]])
            self.authUsers = nil;

        self.notAuthUsers = [jsonObject objectForKey: @"notAuthUsers"];
        if(self.notAuthUsers && [self.notAuthUsers isEqual:[NSNull null]])
            self.notAuthUsers = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
