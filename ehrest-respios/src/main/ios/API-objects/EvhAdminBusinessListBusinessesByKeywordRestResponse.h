//
// EvhAdminBusinessListBusinessesByKeywordRestResponse.h
// generated at 2016-04-06 19:59:47 
//
#import "RestResponseBase.h"
#import "EvhListBusinessesByKeywordAdminCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminBusinessListBusinessesByKeywordRestResponse
//
@interface EvhAdminBusinessListBusinessesByKeywordRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListBusinessesByKeywordAdminCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
