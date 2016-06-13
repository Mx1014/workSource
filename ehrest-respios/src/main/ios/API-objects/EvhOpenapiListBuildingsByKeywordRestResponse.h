//
// EvhOpenapiListBuildingsByKeywordRestResponse.h
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOpenapiListBuildingsByKeywordRestResponse
//
@interface EvhOpenapiListBuildingsByKeywordRestResponse : EvhRestResponseBase

// array of EvhBuildingDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
