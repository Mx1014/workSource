//
// EvhAddRentalBillItemCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhSiteItemDTO.h"
#import "EvhRentalAttachmentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddRentalBillItemCommand
//
@interface EvhAddRentalBillItemCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* rentalSiteId;

@property(nonatomic, copy) NSNumber* rentalBillId;

// item type EvhSiteItemDTO*
@property(nonatomic, strong) NSMutableArray* rentalItems;

// item type EvhRentalAttachmentDTO*
@property(nonatomic, strong) NSMutableArray* rentalAttachments;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

