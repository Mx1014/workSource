//
// EvhContactGetUserEnterpriseContactRestResponse.h
// generated at 2016-04-06 19:59:47 
//
#import "RestResponseBase.h"
#import "EvhEnterpriseContactDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhContactGetUserEnterpriseContactRestResponse
//
@interface EvhContactGetUserEnterpriseContactRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhEnterpriseContactDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
