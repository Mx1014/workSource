//
// EvhContactCreateContactByPhoneCommandRestResponse.h
// generated at 2016-03-31 10:18:21 
//
#import "RestResponseBase.h"
#import "EvhEnterpriseContactDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhContactCreateContactByPhoneCommandRestResponse
//
@interface EvhContactCreateContactByPhoneCommandRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhEnterpriseContactDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
