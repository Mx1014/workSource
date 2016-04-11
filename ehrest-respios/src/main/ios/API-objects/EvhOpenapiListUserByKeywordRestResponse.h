//
// EvhOpenapiListUserByKeywordRestResponse.h
// generated at 2016-04-07 17:57:44 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOpenapiListUserByKeywordRestResponse
//
@interface EvhOpenapiListUserByKeywordRestResponse : EvhRestResponseBase

// array of EvhUserInfo* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
