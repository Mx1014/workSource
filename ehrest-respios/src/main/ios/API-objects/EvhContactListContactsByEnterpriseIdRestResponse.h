//
// EvhContactListContactsByEnterpriseIdRestResponse.h
// generated at 2016-04-18 14:48:52 
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
