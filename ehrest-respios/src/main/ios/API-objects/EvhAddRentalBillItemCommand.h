//
// EvhAddRentalBillItemCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalv2SiteItemDTO.h"
#import "EvhRentalv2AttachmentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddRentalBillItemCommand
//
@interface EvhAddRentalBillItemCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* rentalSiteId;

@property(nonatomic, copy) NSNumber* rentalBillId;

// item type EvhRentalv2SiteItemDTO*
@property(nonatomic, strong) NSMutableArray* rentalItems;

// item type EvhRentalv2AttachmentDTO*
@property(nonatomic, strong) NSMutableArray* rentalAttachments;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

