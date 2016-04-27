//
// EvhContactListContactsByEnterpriseIdRestResponse.h
// generated at 2016-04-26 18:22:56 
//
#import "RestResponseBase.h"
#import "EvhListEnterpriseContactResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhContactListContactsByEnterpriseIdRestResponse
//
@interface EvhContactListContactsByEnterpriseIdRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListEnterpriseContactResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
