//
// EvhRentalv2FindRentalSiteWeekStatusCommandResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalv2RentalSiteDayRulesDTO.h"
#import "EvhRentalSitePicDTO.h"
#import "EvhAttachmentConfigDTO.h"
#import "EvhRentalv2SiteItemDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalv2FindRentalSiteWeekStatusCommandResponse
//
@interface EvhRentalv2FindRentalSiteWeekStatusCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* rentalSiteId;

@property(nonatomic, copy) NSString* siteName;

@property(nonatomic, copy) NSString* introduction;

@property(nonatomic, copy) NSString* notice;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSNumber* longitude;

@property(nonatomic, copy) NSNumber* latitude;

@property(nonatomic, copy) NSString* contactPhonenum;

@property(nonatomic, copy) NSNumber* discountType;

@property(nonatomic, copy) NSNumber* fullPrice;

@property(nonatomic, copy) NSNumber* cutPrice;

@property(nonatomic, copy) NSNumber* discountRatio;

@property(nonatomic, copy) NSNumber* rentalType;

@property(nonatomic, copy) NSNumber* rentalStep;

@property(nonatomic, copy) NSNumber* exclusiveFlag;

@property(nonatomic, copy) NSNumber* autoAssign;

@property(nonatomic, copy) NSNumber* multiUnit;

@property(nonatomic, copy) NSNumber* multiTimeInterval;

@property(nonatomic, copy) NSNumber* cancelFlag;

@property(nonatomic, copy) NSNumber* needPay;

@property(nonatomic, copy) NSNumber* anchorTime;

// item type EvhRentalv2RentalSiteDayRulesDTO*
@property(nonatomic, strong) NSMutableArray* siteDays;

// item type EvhRentalSitePicDTO*
@property(nonatomic, strong) NSMutableArray* sitePics;

// item type EvhAttachmentConfigDTO*
@property(nonatomic, strong) NSMutableArray* attachments;

// item type EvhRentalv2SiteItemDTO*
@property(nonatomic, strong) NSMutableArray* siteItems;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

