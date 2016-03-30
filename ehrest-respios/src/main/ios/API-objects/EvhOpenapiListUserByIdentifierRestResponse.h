//
// EvhOpenapiListUserByIdentifierRestResponse.h
// generated at 2016-03-30 10:13:09 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOpenapiListUserByIdentifierRestResponse
//
@interface EvhOpenapiListUserByIdentifierRestResponse : EvhRestResponseBase

// array of EvhUserInfo* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
