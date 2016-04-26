//
// EvhAdminUserSendUserRichLinkMessageRestResponse.h
// generated at 2016-04-26 18:22:56 
//
#import "RestResponseBase.h"
#import "EvhRichLinkDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminUserSendUserRichLinkMessageRestResponse
//
@interface EvhAdminUserSendUserRichLinkMessageRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhRichLinkDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
