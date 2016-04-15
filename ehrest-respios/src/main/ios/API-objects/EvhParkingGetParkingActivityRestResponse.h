//
// EvhParkingGetParkingActivityRestResponse.h
// generated at 2016-04-12 15:02:21 
//
#import "RestResponseBase.h"
#import "EvhParkingActivityDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhParkingGetParkingActivityRestResponse
//
@interface EvhParkingGetParkingActivityRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhParkingActivityDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
