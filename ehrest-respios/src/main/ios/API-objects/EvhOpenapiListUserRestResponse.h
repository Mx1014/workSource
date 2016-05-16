//
// EvhOpenapiListUserRestResponse.h
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
