//
// EvhOrgListOrgTopicsRestResponse.h
// generated at 2016-04-07 14:16:31 
//
#import "RestResponseBase.h"
#import "EvhListPostCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrgListOrgTopicsRestResponse
//
@interface EvhOrgListOrgTopicsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListPostCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
