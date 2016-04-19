//
// EvhAddressListApartmentsByBuildingNameRestResponse.h
// generated at 2016-04-19 14:25:57 
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
