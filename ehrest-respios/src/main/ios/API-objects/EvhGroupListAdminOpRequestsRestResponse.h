//
// EvhGroupListAdminOpRequestsRestResponse.h
// generated at 2016-04-05 13:45:27 
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
