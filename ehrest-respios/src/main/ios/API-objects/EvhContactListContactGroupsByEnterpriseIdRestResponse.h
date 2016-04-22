//
// EvhContactListContactGroupsByEnterpriseIdRestResponse.h
// generated at 2016-04-22 13:56:50 
//
#import "RestResponseBase.h"
#import "EvhListContactGroupsByEnterpriseIdCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhContactListContactGroupsByEnterpriseIdRestResponse
//
@interface EvhContactListContactGroupsByEnterpriseIdRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListContactGroupsByEnterpriseIdCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
