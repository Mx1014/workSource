//
// EvhFamilyListFamilyRequestsRestResponse.h
// generated at 2016-03-31 13:49:15 
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
