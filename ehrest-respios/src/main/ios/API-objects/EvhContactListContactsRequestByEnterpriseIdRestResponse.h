//
// EvhContactListContactsRequestByEnterpriseIdRestResponse.h
// generated at 2016-04-07 17:33:50 
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
