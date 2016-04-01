//
// EvhAddressListApartmentsByBuildingNameRestResponse.h
// generated at 2016-04-01 15:40:24 
//
#import "RestResponseBase.h"
#import "EvhListApartmentByBuildingNameCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddressListApartmentsByBuildingNameRestResponse
//
@interface EvhAddressListApartmentsByBuildingNameRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListApartmentByBuildingNameCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
