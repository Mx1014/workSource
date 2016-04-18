//
// EvhParkingRequestParkingCardRestResponse.h
// generated at 2016-04-18 14:48:52 
//
#import "RestResponseBase.h"
#import "EvhParkingCardRequestDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhParkingRequestParkingCardRestResponse
//
@interface EvhParkingRequestParkingCardRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhParkingCardRequestDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
