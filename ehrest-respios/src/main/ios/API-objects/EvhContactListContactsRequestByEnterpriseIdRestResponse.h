//
// EvhContactListContactsRequestByEnterpriseIdRestResponse.h
// generated at 2016-03-31 13:49:15 
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
