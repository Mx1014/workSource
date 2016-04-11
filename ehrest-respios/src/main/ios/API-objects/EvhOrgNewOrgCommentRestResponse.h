//
// EvhOrgNewOrgCommentRestResponse.h
// generated at 2016-04-07 17:57:44 
//
#import "RestResponseBase.h"
#import "EvhPostDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrgNewOrgCommentRestResponse
//
@interface EvhOrgNewOrgCommentRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhPostDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
