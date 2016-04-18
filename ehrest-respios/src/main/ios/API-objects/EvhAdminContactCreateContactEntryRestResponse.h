//
// EvhAdminContactCreateContactEntryRestResponse.h
// generated at 2016-04-18 14:48:52 
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
