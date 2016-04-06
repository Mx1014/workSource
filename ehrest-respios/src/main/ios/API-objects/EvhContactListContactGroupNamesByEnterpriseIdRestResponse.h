//
// EvhContactListContactGroupNamesByEnterpriseIdRestResponse.h
// generated at 2016-04-06 19:10:43 
//
#import "RestResponseBase.h"
#import "EvhListContactGroupNamesByEnterpriseIdCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhContactListContactGroupNamesByEnterpriseIdRestResponse
//
@interface EvhContactListContactGroupNamesByEnterpriseIdRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListContactGroupNamesByEnterpriseIdCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
