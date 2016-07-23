//
// EvhRentalRentalBillDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhSiteItemDTO.h"
#import "EvhRentalRentalSiteRulesDTO.h"
#import "EvhRentalBillAttachmentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalRentalBillDTO
//
@interface EvhRentalRentalBillDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* rentalBillId;

@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSString* siteType;

@property(nonatomic, copy) NSString* siteName;

@property(nonatomic, copy) NSString* buildingName;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSString* spec;

@property(nonatomic, copy) NSString* companyName;

@property(nonatomic, copy) NSString* contactName;

@property(nonatomic, copy) NSString* contactPhonenum;

@property(nonatomic, copy) NSString* userName;

@property(nonatomic, copy) NSString* userPhone;

@property(nonatomic, copy) NSString* introduction;

@property(nonatomic, copy) NSString* notice;

@property(nonatomic, copy) NSNumber* startTime;

@property(nonatomic, copy) NSNumber* endTime;

@property(nonatomic, copy) NSNumber* reserveTime;

@property(nonatomic, copy) NSNumber* payStartTime;

@property(nonatomic, copy) NSNumber* payTime;

@property(nonatomic, copy) NSNumber* cancelTime;

@property(nonatomic, copy) NSNumber* payDeadLineTime;

@property(nonatomic, copy) NSNumber* sitePrice;

@property(nonatomic, copy) NSNumber* totalPrice;

@property(nonatomic, copy) NSNumber* reservePrice;

@property(nonatomic, copy) NSNumber* paidPrice;

@property(nonatomic, copy) NSNumber* unPayPrice;

@property(nonatomic, copy) NSNumber* invoiceFlag;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* rentalCount;

// item type EvhSiteItemDTO*
@property(nonatomic, strong) NSMutableArray* siteItems;

// item type EvhRentalRentalSiteRulesDTO*
@property(nonatomic, strong) NSMutableArray* rentalSiteRules;

// item type EvhRentalBillAttachmentDTO*
@property(nonatomic, strong) NSMutableArray* billAttachments;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

