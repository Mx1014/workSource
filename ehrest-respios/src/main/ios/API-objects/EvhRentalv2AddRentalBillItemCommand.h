//
// EvhRentalv2AddRentalBillItemCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhSiteItemDTO.h"
#import "EvhRentalv2AttachmentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalv2AddRentalBillItemCommand
//
@interface EvhRentalv2AddRentalBillItemCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* rentalSiteId;

@property(nonatomic, copy) NSNumber* rentalBillId;

// item type EvhSiteItemDTO*
@property(nonatomic, strong) NSMutableArray* rentalItems;

// item type EvhRentalv2AttachmentDTO*
@property(nonatomic, strong) NSMutableArray* rentalAttachments;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

