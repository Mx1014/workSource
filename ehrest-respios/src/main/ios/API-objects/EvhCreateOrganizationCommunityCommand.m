//
// EvhCreateOrganizationCommunityCommand.m
//
#import "EvhCreateOrganizationCommunityCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateOrganizationCommunityCommand
//

@implementation EvhCreateOrganizationCommunityCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCreateOrganizationCommunityCommand* obj = [EvhCreateOrganizationCommunityCommand new];
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
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
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
        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

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
