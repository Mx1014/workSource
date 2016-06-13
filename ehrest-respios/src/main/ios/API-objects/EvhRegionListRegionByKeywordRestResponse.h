//
// EvhRegionListRegionByKeywordRestResponse.h
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRegionListRegionByKeywordRestResponse
//
@interface EvhRegionListRegionByKeywordRestResponse : EvhRestResponseBase

// array of EvhRegionDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
