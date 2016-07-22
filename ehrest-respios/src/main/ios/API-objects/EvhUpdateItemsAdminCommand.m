//
// EvhUpdateItemsAdminCommand.m
//
#import "EvhUpdateItemsAdminCommand.h"
#import "EvhRentalv2SiteItemDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateItemsAdminCommand
//

@implementation EvhUpdateItemsAdminCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUpdateItemsAdminCommand* obj = [EvhUpdateItemsAdminCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _itemDTOs = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.itemDTOs) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhRentalv2SiteItemDTO* item in self.itemDTOs) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"itemDTOs"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"itemDTOs"];
            for(id itemJson in jsonArray) {
                EvhRentalv2SiteItemDTO* item = [EvhRentalv2SiteItemDTO new];
                
                [item fromJson: itemJson];
                [self.itemDTOs addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
