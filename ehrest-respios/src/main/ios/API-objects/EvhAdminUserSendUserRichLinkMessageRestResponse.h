//
// EvhAdminUserSendUserRichLinkMessageRestResponse.h
// generated at 2016-04-19 14:25:57 
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
