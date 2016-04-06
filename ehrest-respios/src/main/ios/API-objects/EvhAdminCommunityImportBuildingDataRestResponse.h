//
// EvhAdminCommunityImportBuildingDataRestResponse.h
// generated at 2016-04-06 19:59:47 
//
#import "RestResponseBase.h"
#import "EvhImportDataResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminCommunityImportBuildingDataRestResponse
//
@interface EvhAdminCommunityImportBuildingDataRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhImportDataResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
