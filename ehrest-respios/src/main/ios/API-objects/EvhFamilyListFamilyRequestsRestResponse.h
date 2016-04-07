//
// EvhFamilyListFamilyRequestsRestResponse.h
// generated at 2016-04-07 15:16:53 
//
#import "RestResponseBase.h"
#import "EvhListFamilyRequestsCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFamilyListFamilyRequestsRestResponse
//
@interface EvhFamilyListFamilyRequestsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListFamilyRequestsCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
