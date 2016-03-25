//
// EvhAdminContactCreateContactRestResponse.h
// generated at 2016-03-25 09:26:43 
//
#import "RestResponseBase.h"
#import "EvhEnterpriseContactDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminContactCreateContactRestResponse
//
@interface EvhAdminContactCreateContactRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhEnterpriseContactDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
