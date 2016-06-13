//
// EvhSendOrganizationMessageCommand.m
//
#import "EvhSendOrganizationMessageCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSendOrganizationMessageCommand
//

@implementation EvhSendOrganizationMessageCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSendOrganizationMessageCommand* obj = [EvhSendOrganizationMessageCommand new];
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
    if(self.message)
        [jsonObject setObject: self.message forKey: @"message"];
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
        self.message = [jsonObject objectForKey: @"message"];
        if(self.message && [self.message isEqual:[NSNull null]])
            self.message = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
