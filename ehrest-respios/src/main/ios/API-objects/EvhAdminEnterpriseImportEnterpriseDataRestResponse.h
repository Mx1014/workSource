//
// EvhAdminEnterpriseImportEnterpriseDataRestResponse.h
// generated at 2016-04-07 17:57:43 
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
