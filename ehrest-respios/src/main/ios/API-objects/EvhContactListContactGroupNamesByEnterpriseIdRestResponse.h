//
// EvhContactListContactGroupNamesByEnterpriseIdRestResponse.h
// generated at 2016-04-19 12:41:55 
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
