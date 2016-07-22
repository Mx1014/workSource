//
// EvhRentalFindRentalSiteWeekStatusCommandResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalRentalSiteDayRulesDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalFindRentalSiteWeekStatusCommandResponse
//
@interface EvhRentalFindRentalSiteWeekStatusCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* rentalSiteId;

@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSString* ownerName;

@property(nonatomic, copy) NSString* siteType;

@property(nonatomic, copy) NSString* contactNum;

@property(nonatomic, copy) NSString* siteName;

@property(nonatomic, copy) NSString* introduction;

@property(nonatomic, copy) NSString* notice;

@property(nonatomic, copy) NSNumber* anchorTime;

// item type EvhRentalRentalSiteDayRulesDTO*
@property(nonatomic, strong) NSMutableArray* siteDays;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

