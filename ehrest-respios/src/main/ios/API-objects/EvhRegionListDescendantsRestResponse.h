//
// EvhRegionListDescendantsRestResponse.h
// generated at 2016-03-25 17:08:13 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRegionListDescendantsRestResponse
//
@interface EvhRegionListDescendantsRestResponse : EvhRestResponseBase

// array of EvhRegionDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
