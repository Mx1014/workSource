//
// EvhDeleteOrganizationCommunityCommand.m
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:55 
>>>>>>> 3.3.x
//
#import "EvhDeleteOrganizationCommunityCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteOrganizationCommunityCommand
//

@implementation EvhDeleteOrganizationCommunityCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhDeleteOrganizationCommunityCommand* obj = [EvhDeleteOrganizationCommunityCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _communityIds = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.organizationId)
        [jsonObject setObject: self.organizationId forKey: @"organizationId"];
    if(self.communityIds) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.communityIds) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"communityIds"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.organizationId = [jsonObject objectForKey: @"organizationId"];
        if(self.organizationId && [self.organizationId isEqual:[NSNull null]])
            self.organizationId = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"communityIds"];
            for(id itemJson in jsonArray) {
                [self.communityIds addObject: itemJson];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
