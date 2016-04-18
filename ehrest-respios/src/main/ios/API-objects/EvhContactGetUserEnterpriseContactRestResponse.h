//
// EvhContactGetUserEnterpriseContactRestResponse.h
// generated at 2016-04-18 14:48:52 
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
