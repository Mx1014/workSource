//
// EvhFindRentalSiteItemsAndAttachmentsCommand.m
//
#import "EvhFindRentalSiteItemsAndAttachmentsCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFindRentalSiteItemsAndAttachmentsCommand
//

@implementation EvhFindRentalSiteItemsAndAttachmentsCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhFindRentalSiteItemsAndAttachmentsCommand* obj = [EvhFindRentalSiteItemsAndAttachmentsCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _rentalSiteRuleIds = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.rentalSiteId)
        [jsonObject setObject: self.rentalSiteId forKey: @"rentalSiteId"];
    if(self.rentalSiteRuleIds) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.rentalSiteRuleIds) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"rentalSiteRuleIds"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.rentalSiteId = [jsonObject objectForKey: @"rentalSiteId"];
        if(self.rentalSiteId && [self.rentalSiteId isEqual:[NSNull null]])
            self.rentalSiteId = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"rentalSiteRuleIds"];
            for(id itemJson in jsonArray) {
                [self.rentalSiteRuleIds addObject: itemJson];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
