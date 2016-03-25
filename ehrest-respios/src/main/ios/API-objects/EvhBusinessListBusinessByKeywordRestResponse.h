//
// EvhBusinessListBusinessByKeywordRestResponse.h
// generated at 2016-03-25 09:26:43 
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
