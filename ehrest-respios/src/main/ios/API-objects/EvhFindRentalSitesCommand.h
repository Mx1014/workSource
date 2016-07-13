//
// EvhFindRentalSitesCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFindRentalSitesCommand
//
@interface EvhFindRentalSitesCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSNumber* launchPadItemId;

@property(nonatomic, copy) NSString* keyword;

@property(nonatomic, copy) NSNumber* anchor;

@property(nonatomic, copy) NSNumber* pageSize;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* status;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

