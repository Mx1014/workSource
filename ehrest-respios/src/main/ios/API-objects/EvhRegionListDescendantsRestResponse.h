//
// EvhRegionListDescendantsRestResponse.h
// generated at 2016-04-07 17:57:44 
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
