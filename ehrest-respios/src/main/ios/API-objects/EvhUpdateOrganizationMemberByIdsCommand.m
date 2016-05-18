//
// EvhUpdateOrganizationMemberByIdsCommand.m
//
#import "EvhUpdateOrganizationMemberByIdsCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateOrganizationMemberByIdsCommand
//

@implementation EvhUpdateOrganizationMemberByIdsCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUpdateOrganizationMemberByIdsCommand* obj = [EvhUpdateOrganizationMemberByIdsCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _ids = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.ids) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.ids) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"ids"];
    }
    if(self.orgId)
        [jsonObject setObject: self.orgId forKey: @"orgId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"ids"];
            for(id itemJson in jsonArray) {
                [self.ids addObject: itemJson];
            }
        }
        self.orgId = [jsonObject objectForKey: @"orgId"];
        if(self.orgId && [self.orgId isEqual:[NSNull null]])
            self.orgId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
