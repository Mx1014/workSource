//
// EvhAddressListUnassignedApartmentsByBuildingNameRestResponse.h
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddressListUnassignedApartmentsByBuildingNameRestResponse
//
@interface EvhAddressListUnassignedApartmentsByBuildingNameRestResponse : EvhRestResponseBase

// array of EvhApartmentDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
