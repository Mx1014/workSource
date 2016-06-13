//
// EvhOpenapiGetCommunitiesByNameAndCityIdRestResponse.h
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOpenapiGetCommunitiesByNameAndCityIdRestResponse
//
@interface EvhOpenapiGetCommunitiesByNameAndCityIdRestResponse : EvhRestResponseBase

// array of EvhCommunityDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
