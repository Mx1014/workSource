//
// EvhAddRentalBillItemCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalSiteItemDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddRentalBillItemCommand
//
@interface EvhAddRentalBillItemCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* rentalSiteId;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSNumber* invoiceFlag;

@property(nonatomic, copy) NSString* siteType;

@property(nonatomic, copy) NSNumber* rentalBillId;

// item type EvhRentalSiteItemDTO*
@property(nonatomic, strong) NSMutableArray* rentalItems;

// item type NSString*
@property(nonatomic, strong) NSMutableArray* rentalAttachments;

@property(nonatomic, copy) NSNumber* attachmentType;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

