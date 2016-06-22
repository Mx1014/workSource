//
// EvhRentalSiteDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhSiteItemDTO.h"
#import "EvhRentalSitePicDTO.h"
#import "EvhSiteOwnerDTO.h"
#import "EvhAttachmentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalSiteDTO
//
@interface EvhRentalSiteDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* rentalSiteId;

@property(nonatomic, copy) NSString* siteName;

@property(nonatomic, copy) NSString* buildingName;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSNumber* avgPrice;

@property(nonatomic, copy) NSString* spec;

@property(nonatomic, copy) NSString* companyName;

@property(nonatomic, copy) NSString* contactName;

@property(nonatomic, copy) NSString* contactPhonenum;

@property(nonatomic, copy) NSString* introduction;

@property(nonatomic, copy) NSString* notice;

@property(nonatomic, copy) NSString* coverUri;

@property(nonatomic, copy) NSString* coverUrl;

@property(nonatomic, copy) NSNumber* discountType;

@property(nonatomic, copy) NSNumber* fullPrice;

@property(nonatomic, copy) NSNumber* cutPrice;

@property(nonatomic, copy) NSNumber* discountRatio;

@property(nonatomic, copy) NSNumber* rentalType;

@property(nonatomic, copy) NSNumber* rentalStep;

@property(nonatomic, copy) NSNumber* dayBeginTime;

@property(nonatomic, copy) NSNumber* dayEndTime;

@property(nonatomic, copy) NSNumber* exclusiveFlag;

@property(nonatomic, copy) NSNumber* autoAssign;

@property(nonatomic, copy) NSNumber* multiUnit;

@property(nonatomic, copy) NSNumber* multiTimeInterval;

@property(nonatomic, copy) NSNumber* cancelFlag;

@property(nonatomic, copy) NSNumber* needPay;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* createTime;

// item type EvhSiteItemDTO*
@property(nonatomic, strong) NSMutableArray* siteItems;

// item type EvhRentalSitePicDTO*
@property(nonatomic, strong) NSMutableArray* sitePics;

// item type EvhSiteOwnerDTO*
@property(nonatomic, strong) NSMutableArray* owners;

// item type EvhAttachmentDTO*
@property(nonatomic, strong) NSMutableArray* attachments;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

