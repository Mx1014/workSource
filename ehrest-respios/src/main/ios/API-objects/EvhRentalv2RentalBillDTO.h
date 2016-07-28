//
// EvhRentalv2RentalBillDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalv2SiteItemDTO.h"
#import "EvhRentalSiteRulesDTO.h"
#import "EvhRentalv2BillAttachmentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalv2RentalBillDTO
//
@interface EvhRentalv2RentalBillDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* rentalBillId;

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

@property(nonatomic, copy) NSString* useDetail;

@property(nonatomic, copy) NSString* vendorType;

@property(nonatomic, copy) NSNumber* resourceTypeId;

// item type EvhRentalv2SiteItemDTO*
@property(nonatomic, strong) NSMutableArray* siteItems;

// item type EvhRentalSiteRulesDTO*
@property(nonatomic, strong) NSMutableArray* rentalSiteRules;

// item type EvhRentalv2BillAttachmentDTO*
@property(nonatomic, strong) NSMutableArray* billAttachments;

@property(nonatomic, copy) NSNumber* toastFlag;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

