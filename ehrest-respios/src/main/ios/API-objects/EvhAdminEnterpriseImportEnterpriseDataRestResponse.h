//
// EvhAdminEnterpriseImportEnterpriseDataRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhImportDataResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminEnterpriseImportEnterpriseDataRestResponse
//
@interface EvhAdminEnterpriseImportEnterpriseDataRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhImportDataResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
