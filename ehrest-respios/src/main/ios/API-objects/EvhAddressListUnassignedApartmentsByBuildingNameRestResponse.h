//
// EvhAddressListUnassignedApartmentsByBuildingNameRestResponse.h
// generated at 2016-03-30 10:13:09 
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
