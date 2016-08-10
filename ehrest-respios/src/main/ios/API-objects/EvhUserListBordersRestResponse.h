//
// EvhUserListBordersRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhBorderListResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserListBordersRestResponse
//
@interface EvhUserListBordersRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhBorderListResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
