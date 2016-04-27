//
// EvhAdminOrgImportEnterpriseDataRestResponse.h
// generated at 2016-04-26 18:22:56 
//
#import "RestResponseBase.h"
#import "EvhImportDataResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminOrgImportEnterpriseDataRestResponse
//
@interface EvhAdminOrgImportEnterpriseDataRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhImportDataResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
