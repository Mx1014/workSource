//
// EvhAddressListUnassignedApartmentsByBuildingNameRestResponse.h
// generated at 2016-04-05 13:45:26 
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
