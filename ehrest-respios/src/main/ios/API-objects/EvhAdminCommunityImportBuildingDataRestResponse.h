//
// EvhAdminCommunityImportBuildingDataRestResponse.h
// generated at 2016-04-07 10:47:32 
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
