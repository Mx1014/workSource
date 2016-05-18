//
// EvhAdminUserSendUserRichLinkMessageRestResponse.h
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
