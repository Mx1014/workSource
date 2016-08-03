//
// EvhRentalBatchIncompleteBillCommand.m
//
#import "EvhRentalBatchIncompleteBillCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalBatchIncompleteBillCommand
//

@implementation EvhRentalBatchIncompleteBillCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRentalBatchIncompleteBillCommand* obj = [EvhRentalBatchIncompleteBillCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _rentalBillIds = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.ownerType)
        [jsonObject setObject: self.ownerType forKey: @"ownerType"];
    if(self.ownerId)
        [jsonObject setObject: self.ownerId forKey: @"ownerId"];
    if(self.siteType)
        [jsonObject setObject: self.siteType forKey: @"siteType"];
    if(self.rentalBillIds) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.rentalBillIds) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"rentalBillIds"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.ownerType = [jsonObject objectForKey: @"ownerType"];
        if(self.ownerType && [self.ownerType isEqual:[NSNull null]])
            self.ownerType = nil;

        self.ownerId = [jsonObject objectForKey: @"ownerId"];
        if(self.ownerId && [self.ownerId isEqual:[NSNull null]])
            self.ownerId = nil;

        self.siteType = [jsonObject objectForKey: @"siteType"];
        if(self.siteType && [self.siteType isEqual:[NSNull null]])
            self.siteType = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"rentalBillIds"];
            for(id itemJson in jsonArray) {
                [self.rentalBillIds addObject: itemJson];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
