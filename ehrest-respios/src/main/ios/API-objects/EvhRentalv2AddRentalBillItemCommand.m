//
// EvhRentalv2AddRentalBillItemCommand.m
//
#import "EvhRentalv2AddRentalBillItemCommand.h"
#import "EvhSiteItemDTO.h"
#import "EvhRentalv2AttachmentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalv2AddRentalBillItemCommand
//

@implementation EvhRentalv2AddRentalBillItemCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRentalv2AddRentalBillItemCommand* obj = [EvhRentalv2AddRentalBillItemCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _rentalItems = [NSMutableArray new];
        _rentalAttachments = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.rentalSiteId)
        [jsonObject setObject: self.rentalSiteId forKey: @"rentalSiteId"];
    if(self.rentalBillId)
        [jsonObject setObject: self.rentalBillId forKey: @"rentalBillId"];
    if(self.rentalItems) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhSiteItemDTO* item in self.rentalItems) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"rentalItems"];
    }
    if(self.rentalAttachments) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhRentalv2AttachmentDTO* item in self.rentalAttachments) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"rentalAttachments"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.rentalSiteId = [jsonObject objectForKey: @"rentalSiteId"];
        if(self.rentalSiteId && [self.rentalSiteId isEqual:[NSNull null]])
            self.rentalSiteId = nil;

        self.rentalBillId = [jsonObject objectForKey: @"rentalBillId"];
        if(self.rentalBillId && [self.rentalBillId isEqual:[NSNull null]])
            self.rentalBillId = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"rentalItems"];
            for(id itemJson in jsonArray) {
                EvhSiteItemDTO* item = [EvhSiteItemDTO new];
                
                [item fromJson: itemJson];
                [self.rentalItems addObject: item];
            }
        }
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"rentalAttachments"];
            for(id itemJson in jsonArray) {
                EvhRentalv2AttachmentDTO* item = [EvhRentalv2AttachmentDTO new];
                
                [item fromJson: itemJson];
                [self.rentalAttachments addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
