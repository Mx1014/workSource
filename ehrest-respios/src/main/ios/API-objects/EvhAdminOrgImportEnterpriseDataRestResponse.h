//
// EvhAdminOrgImportEnterpriseDataRestResponse.h
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
