//
// EvhContactListContactsByPhoneRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhEnterpriseContactDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhContactListContactsByPhoneRestResponse
//
@interface EvhContactListContactsByPhoneRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhEnterpriseContactDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
