//
// EvhFamilyListFamilyRequestsRestResponse.h
// generated at 2016-03-31 10:18:21 
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
