//
// EvhOpenapiListUserRestResponse.h
// generated at 2016-03-31 10:18:21 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOpenapiListUserRestResponse
//
@interface EvhOpenapiListUserRestResponse : EvhRestResponseBase

// array of EvhUserDtoForBiz* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
