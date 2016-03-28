//
// EvhEnterpriseEnterpriseDetailRestResponse.h
// generated at 2016-03-28 15:56:09 
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
