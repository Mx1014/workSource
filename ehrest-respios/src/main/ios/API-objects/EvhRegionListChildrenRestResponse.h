//
// EvhRegionListChildrenRestResponse.h
// generated at 2016-03-31 13:49:15 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRegionListChildrenRestResponse
//
@interface EvhRegionListChildrenRestResponse : EvhRestResponseBase

// array of EvhRegionDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
