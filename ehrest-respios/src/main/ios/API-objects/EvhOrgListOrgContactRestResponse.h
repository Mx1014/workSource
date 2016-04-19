//
// EvhOrgListOrgContactRestResponse.h
// generated at 2016-04-19 12:41:55 
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
