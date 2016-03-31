//
// EvhRegionListActiveRegionRestResponse.h
// generated at 2016-03-31 15:43:24 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRegionListActiveRegionRestResponse
//
@interface EvhRegionListActiveRegionRestResponse : EvhRestResponseBase

// array of EvhRegionDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
