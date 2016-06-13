//
// EvhContactListContactsRequestByEnterpriseIdRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListContactsRequestByEnterpriseIdCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhContactListContactsRequestByEnterpriseIdRestResponse
//
@interface EvhContactListContactsRequestByEnterpriseIdRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListContactsRequestByEnterpriseIdCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
