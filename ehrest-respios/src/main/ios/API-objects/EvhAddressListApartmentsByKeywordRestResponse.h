//
// EvhAddressListApartmentsByKeywordRestResponse.h
// generated at 2016-03-31 15:43:23 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddressListApartmentsByKeywordRestResponse
//
@interface EvhAddressListApartmentsByKeywordRestResponse : EvhRestResponseBase

// array of EvhApartmentDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
