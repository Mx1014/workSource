//
// EvhParkingListParkingLotsRestResponse.h
// generated at 2016-04-07 17:03:18 
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
