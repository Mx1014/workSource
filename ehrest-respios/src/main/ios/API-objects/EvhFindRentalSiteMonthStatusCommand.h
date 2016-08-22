//
// EvhFindRentalSiteMonthStatusCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFindRentalSiteMonthStatusCommand
//
@interface EvhFindRentalSiteMonthStatusCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* siteId;

@property(nonatomic, copy) NSNumber* ruleDate;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

