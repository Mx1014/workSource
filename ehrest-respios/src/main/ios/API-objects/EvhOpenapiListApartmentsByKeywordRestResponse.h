//
// EvhOpenapiListApartmentsByKeywordRestResponse.h
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOpenapiListApartmentsByKeywordRestResponse
//
@interface EvhOpenapiListApartmentsByKeywordRestResponse : EvhRestResponseBase

// array of EvhApartmentDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
