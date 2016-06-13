//
// EvhParkingListParkingLotsRestResponse.h
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhParkingListParkingLotsRestResponse
//
@interface EvhParkingListParkingLotsRestResponse : EvhRestResponseBase

// array of EvhParkingLotDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
