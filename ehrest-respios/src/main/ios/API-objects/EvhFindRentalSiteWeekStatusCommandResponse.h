//
// EvhFindRentalSiteWeekStatusCommandResponse.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:55 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRentalSiteDayRulesDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFindRentalSiteWeekStatusCommandResponse
//
@interface EvhFindRentalSiteWeekStatusCommandResponse
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

// item type EvhRentalSiteDayRulesDTO*
@property(nonatomic, strong) NSMutableArray* siteDays;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

