//
// EvhRegionListRestResponse.h
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRegionListRestResponse
//
@interface EvhRegionListRestResponse : EvhRestResponseBase

// array of EvhRegionDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
