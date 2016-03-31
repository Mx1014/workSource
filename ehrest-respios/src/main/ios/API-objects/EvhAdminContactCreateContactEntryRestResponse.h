//
// EvhAdminContactCreateContactEntryRestResponse.h
// generated at 2016-03-31 11:07:27 
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
