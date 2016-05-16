//
// EvhConfListReservationConfRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListReservationConfResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfListReservationConfRestResponse
//
@interface EvhConfListReservationConfRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListReservationConfResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
