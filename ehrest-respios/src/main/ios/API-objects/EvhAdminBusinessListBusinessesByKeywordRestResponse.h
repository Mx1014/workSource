//
// EvhAdminBusinessListBusinessesByKeywordRestResponse.h
// generated at 2016-03-25 09:26:43 
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
