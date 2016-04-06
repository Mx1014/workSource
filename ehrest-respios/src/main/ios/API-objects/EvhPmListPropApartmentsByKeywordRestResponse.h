//
// EvhPmListPropApartmentsByKeywordRestResponse.h
// generated at 2016-04-06 19:10:44 
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
