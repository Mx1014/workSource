//
// EvhOpenapiSyncuserinfoRestResponse.h
// generated at 2016-03-25 11:43:35 
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
