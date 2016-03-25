//
// EvhBusinessListBusinessByKeywordRestResponse.h
// generated at 2016-03-25 17:08:12 
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
