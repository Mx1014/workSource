//
// EvhOpenapiListUserRestResponse.h
// generated at 2016-04-19 12:41:55 
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
