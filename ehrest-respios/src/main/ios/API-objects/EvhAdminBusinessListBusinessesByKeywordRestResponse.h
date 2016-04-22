//
// EvhAdminBusinessListBusinessesByKeywordRestResponse.h
// generated at 2016-04-22 13:56:49 
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
