//
// EvhUiOrgListGrabTaskTopicsRestResponse.h
// generated at 2016-04-26 18:22:57 
//
#import "RestResponseBase.h"
#import "EvhListTaskPostsResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUiOrgListGrabTaskTopicsRestResponse
//
@interface EvhUiOrgListGrabTaskTopicsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListTaskPostsResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
