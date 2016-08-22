//
// EvhRentalDeleteRentalSiteCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalDeleteRentalSiteCommand
//
@interface EvhRentalDeleteRentalSiteCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSString* siteType;

@property(nonatomic, copy) NSNumber* rentalSiteId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

