//
// EvhPropCommunityBuildAddessCommand.m
//
#import "EvhPropCommunityBuildAddessCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPropCommunityBuildAddessCommand
//

@implementation EvhPropCommunityBuildAddessCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPropCommunityBuildAddessCommand* obj = [EvhPropCommunityBuildAddessCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _buildingNames = [NSMutableArray new];
        _buildingIds = [NSMutableArray new];
        _addressIds = [NSMutableArray new];
        _mobilePhones = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.buildingNames) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSString* item in self.buildingNames) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"buildingNames"];
    }
    if(self.buildingIds) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.buildingIds) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"buildingIds"];
    }
    if(self.addressIds) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.addressIds) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"addressIds"];
    }
    if(self.mobilePhones) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSString* item in self.mobilePhones) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"mobilePhones"];
    }
    if(self.message)
        [jsonObject setObject: self.message forKey: @"message"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"buildingNames"];
            for(id itemJson in jsonArray) {
                [self.buildingNames addObject: itemJson];
            }
        }
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"buildingIds"];
            for(id itemJson in jsonArray) {
                [self.buildingIds addObject: itemJson];
            }
        }
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"addressIds"];
            for(id itemJson in jsonArray) {
                [self.addressIds addObject: itemJson];
            }
        }
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"mobilePhones"];
            for(id itemJson in jsonArray) {
                [self.mobilePhones addObject: itemJson];
            }
        }
        self.message = [jsonObject objectForKey: @"message"];
        if(self.message && [self.message isEqual:[NSNull null]])
            self.message = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
