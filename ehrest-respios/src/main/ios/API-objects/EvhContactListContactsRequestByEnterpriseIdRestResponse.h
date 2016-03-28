//
// EvhContactListContactsRequestByEnterpriseIdRestResponse.h
// generated at 2016-03-25 19:05:21 
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
