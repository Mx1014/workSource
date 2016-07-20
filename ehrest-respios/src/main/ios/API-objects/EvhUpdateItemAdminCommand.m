//
// EvhUpdateItemAdminCommand.m
//
#import "EvhUpdateItemAdminCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateItemAdminCommand
//

@implementation EvhUpdateItemAdminCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUpdateItemAdminCommand* obj = [EvhUpdateItemAdminCommand new];
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
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.rentalSiteId)
        [jsonObject setObject: self.rentalSiteId forKey: @"rentalSiteId"];
    if(self.itemName)
        [jsonObject setObject: self.itemName forKey: @"itemName"];
    if(self.itemPrice)
        [jsonObject setObject: self.itemPrice forKey: @"itemPrice"];
    if(self.counts)
        [jsonObject setObject: self.counts forKey: @"counts"];
    if(self.imgUri)
        [jsonObject setObject: self.imgUri forKey: @"imgUri"];
    if(self.defaultOrder)
        [jsonObject setObject: self.defaultOrder forKey: @"defaultOrder"];
    if(self.itemType)
        [jsonObject setObject: self.itemType forKey: @"itemType"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.rentalSiteId = [jsonObject objectForKey: @"rentalSiteId"];
        if(self.rentalSiteId && [self.rentalSiteId isEqual:[NSNull null]])
            self.rentalSiteId = nil;

        self.itemName = [jsonObject objectForKey: @"itemName"];
        if(self.itemName && [self.itemName isEqual:[NSNull null]])
            self.itemName = nil;

        self.itemPrice = [jsonObject objectForKey: @"itemPrice"];
        if(self.itemPrice && [self.itemPrice isEqual:[NSNull null]])
            self.itemPrice = nil;

        self.counts = [jsonObject objectForKey: @"counts"];
        if(self.counts && [self.counts isEqual:[NSNull null]])
            self.counts = nil;

        self.imgUri = [jsonObject objectForKey: @"imgUri"];
        if(self.imgUri && [self.imgUri isEqual:[NSNull null]])
            self.imgUri = nil;

        self.defaultOrder = [jsonObject objectForKey: @"defaultOrder"];
        if(self.defaultOrder && [self.defaultOrder isEqual:[NSNull null]])
            self.defaultOrder = nil;

        self.itemType = [jsonObject objectForKey: @"itemType"];
        if(self.itemType && [self.itemType isEqual:[NSNull null]])
            self.itemType = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
