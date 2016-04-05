//
// EvhAddressListApartmentsByBuildingNameRestResponse.h
// generated at 2016-04-05 13:45:26 
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
