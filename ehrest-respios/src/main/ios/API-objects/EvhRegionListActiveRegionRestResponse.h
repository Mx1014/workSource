//
// EvhRegionListActiveRegionRestResponse.h
// generated at 2016-03-25 09:26:44 
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
