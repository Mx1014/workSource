//
// EvhAdminContactCreateContactEntryRestResponse.h
// generated at 2016-04-22 13:56:49 
//
#import "RestResponseBase.h"
#import "EvhEnterpriseContactEntryDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminContactCreateContactEntryRestResponse
//
@interface EvhAdminContactCreateContactEntryRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhEnterpriseContactEntryDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
