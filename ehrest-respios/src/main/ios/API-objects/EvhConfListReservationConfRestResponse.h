//
// EvhConfListReservationConfRestResponse.h
// generated at 2016-03-30 10:13:09 
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
