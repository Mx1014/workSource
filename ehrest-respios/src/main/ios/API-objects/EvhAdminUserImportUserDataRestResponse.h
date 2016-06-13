//
// EvhAdminUserImportUserDataRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhImportDataResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminUserImportUserDataRestResponse
//
@interface EvhAdminUserImportUserDataRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhImportDataResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
