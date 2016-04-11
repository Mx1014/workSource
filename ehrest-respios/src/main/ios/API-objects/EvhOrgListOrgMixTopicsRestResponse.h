//
// EvhOrgListOrgMixTopicsRestResponse.h
// generated at 2016-04-07 17:57:44 
//
#import "RestResponseBase.h"
#import "EvhListPostCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrgListOrgMixTopicsRestResponse
//
@interface EvhOrgListOrgMixTopicsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListPostCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
