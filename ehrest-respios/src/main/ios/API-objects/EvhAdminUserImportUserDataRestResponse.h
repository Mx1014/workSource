//
// EvhAdminUserImportUserDataRestResponse.h
// generated at 2016-04-12 15:02:21 
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
