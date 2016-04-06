//
// EvhOrgListOrgContactRestResponse.h
// generated at 2016-04-06 19:59:47 
//
#import "RestResponseBase.h"
#import "EvhListOrganizationContactCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrgListOrgContactRestResponse
//
@interface EvhOrgListOrgContactRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListOrganizationContactCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
