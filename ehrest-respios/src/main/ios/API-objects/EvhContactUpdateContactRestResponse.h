//
// EvhContactUpdateContactRestResponse.h
// generated at 2016-03-31 13:49:15 
//
#import "RestResponseBase.h"
#import "EvhEnterpriseContactDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhContactUpdateContactRestResponse
//
@interface EvhContactUpdateContactRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhEnterpriseContactDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
