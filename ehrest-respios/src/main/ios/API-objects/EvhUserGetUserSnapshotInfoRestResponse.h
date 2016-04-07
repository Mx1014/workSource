//
// EvhUserGetUserSnapshotInfoRestResponse.h
// generated at 2016-04-07 17:33:50 
//
#import "RestResponseBase.h"
#import "EvhUserInfo.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserGetUserSnapshotInfoRestResponse
//
@interface EvhUserGetUserSnapshotInfoRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhUserInfo* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
