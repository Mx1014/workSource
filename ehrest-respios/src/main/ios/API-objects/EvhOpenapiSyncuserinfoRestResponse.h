//
// EvhOpenapiSyncuserinfoRestResponse.h
// generated at 2016-04-12 19:00:53 
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
