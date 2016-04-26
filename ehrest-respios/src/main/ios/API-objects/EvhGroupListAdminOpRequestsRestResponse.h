//
// EvhGroupListAdminOpRequestsRestResponse.h
// generated at 2016-04-26 18:22:56 
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
