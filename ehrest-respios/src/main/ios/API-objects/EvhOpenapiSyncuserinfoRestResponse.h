//
// EvhOpenapiSyncuserinfoRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhSyncUserResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOpenapiSyncuserinfoRestResponse
//
@interface EvhOpenapiSyncuserinfoRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhSyncUserResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
