//
// EvhContactListContactsRequestByEnterpriseIdRestResponse.h
// generated at 2016-04-07 15:16:53 
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
