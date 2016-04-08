//
// EvhConfListReservationConfRestResponse.h
// generated at 2016-04-07 17:57:43 
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
