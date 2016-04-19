//
// EvhOpenapiListUserByKeywordRestResponse.h
// generated at 2016-04-19 13:40:01 
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
