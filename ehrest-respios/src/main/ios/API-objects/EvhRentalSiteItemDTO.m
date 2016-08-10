//
// EvhRentalSiteItemDTO.m
//
#import "EvhRentalSiteItemDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalSiteItemDTO
//

@implementation EvhRentalSiteItemDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRentalSiteItemDTO* obj = [EvhRentalSiteItemDTO new];
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
    if(self.itemName)
        [jsonObject setObject: self.itemName forKey: @"itemName"];
    if(self.itemPrice)
        [jsonObject setObject: self.itemPrice forKey: @"itemPrice"];
    if(self.counts)
        [jsonObject setObject: self.counts forKey: @"counts"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.itemName = [jsonObject objectForKey: @"itemName"];
        if(self.itemName && [self.itemName isEqual:[NSNull null]])
            self.itemName = nil;

        self.itemPrice = [jsonObject objectForKey: @"itemPrice"];
        if(self.itemPrice && [self.itemPrice isEqual:[NSNull null]])
            self.itemPrice = nil;

        self.counts = [jsonObject objectForKey: @"counts"];
        if(self.counts && [self.counts isEqual:[NSNull null]])
            self.counts = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
