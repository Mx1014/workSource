//
// EvhBusinessListBusinessByKeywordRestResponse.h
// generated at 2016-04-26 18:22:56 
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
