//
// EvhAddRentalBillItemCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhSiteItemDTO.h"
#import "EvhAttachmentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddRentalBillItemCommand
//
@interface EvhAddRentalBillItemCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* rentalSiteId;

@property(nonatomic, copy) NSNumber* rentalBillId;

// item type EvhSiteItemDTO*
@property(nonatomic, strong) NSMutableArray* rentalItems;

// item type EvhAttachmentDTO*
@property(nonatomic, strong) NSMutableArray* rentalAttachments;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

