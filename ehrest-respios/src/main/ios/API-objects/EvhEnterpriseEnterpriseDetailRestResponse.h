//
// EvhEnterpriseEnterpriseDetailRestResponse.h
// generated at 2016-03-25 19:05:21 
//
#import "RestResponseBase.h"
#import "EvhEnterpriseDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhEnterpriseEnterpriseDetailRestResponse
//
@interface EvhEnterpriseEnterpriseDetailRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhEnterpriseDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
