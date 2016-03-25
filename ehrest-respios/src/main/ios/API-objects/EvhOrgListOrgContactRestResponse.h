//
// EvhOrgListOrgContactRestResponse.h
// generated at 2016-03-25 11:43:35 
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
