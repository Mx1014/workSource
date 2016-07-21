//
// EvhRentalSiteDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalSiteItemDTO.h"
#import "EvhRentalSiteRulesDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalSiteDTO
//
@interface EvhRentalSiteDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* rentalSiteId;

@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSString* siteType;

@property(nonatomic, copy) NSString* siteName;

@property(nonatomic, copy) NSString* buildingName;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSString* spec;

@property(nonatomic, copy) NSString* companyName;

@property(nonatomic, copy) NSString* contactName;

@property(nonatomic, copy) NSString* contactPhonenum;

@property(nonatomic, copy) NSString* introduction;

@property(nonatomic, copy) NSString* notice;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* createTime;

// item type EvhRentalSiteItemDTO*
@property(nonatomic, strong) NSMutableArray* siteItems;

// item type EvhRentalSiteRulesDTO*
@property(nonatomic, strong) NSMutableArray* siteRules;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

