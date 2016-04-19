//
// EvhBusinessListBusinessByKeywordRestResponse.h
// generated at 2016-04-19 13:40:01 
//
#import "RestResponseBase.h"
#import "EvhListBusinessByKeywordCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBusinessListBusinessByKeywordRestResponse
//
@interface EvhBusinessListBusinessByKeywordRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListBusinessByKeywordCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
