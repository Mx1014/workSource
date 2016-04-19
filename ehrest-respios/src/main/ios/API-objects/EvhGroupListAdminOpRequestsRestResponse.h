//
// EvhGroupListAdminOpRequestsRestResponse.h
// generated at 2016-04-19 13:40:01 
//
#import "RestResponseBase.h"
#import "EvhListAdminOpRequestCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGroupListAdminOpRequestsRestResponse
//
@interface EvhGroupListAdminOpRequestsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListAdminOpRequestCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
