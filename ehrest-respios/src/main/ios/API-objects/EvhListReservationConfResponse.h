//
// EvhListReservationConfResponse.h
// generated at 2016-03-28 15:56:08 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhConfReservationsDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListReservationConfResponse
//
@interface EvhListReservationConfResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhConfReservationsDTO*
@property(nonatomic, strong) NSMutableArray* reserveConf;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

