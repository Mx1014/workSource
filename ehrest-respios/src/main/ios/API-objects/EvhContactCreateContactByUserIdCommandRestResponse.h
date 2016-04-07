//
// EvhContactCreateContactByUserIdCommandRestResponse.h
// generated at 2016-04-07 17:33:50 
//
#import "RestResponseBase.h"
#import "EvhEnterpriseContactDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhContactCreateContactByUserIdCommandRestResponse
//
@interface EvhContactCreateContactByUserIdCommandRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhEnterpriseContactDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
