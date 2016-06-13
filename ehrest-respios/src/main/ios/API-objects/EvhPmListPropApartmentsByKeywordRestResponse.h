//
// EvhPmListPropApartmentsByKeywordRestResponse.h
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmListPropApartmentsByKeywordRestResponse
//
@interface EvhPmListPropApartmentsByKeywordRestResponse : EvhRestResponseBase

// array of EvhPropFamilyDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
