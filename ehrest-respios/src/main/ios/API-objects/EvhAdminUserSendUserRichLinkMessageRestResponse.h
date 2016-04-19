//
// EvhAdminUserSendUserRichLinkMessageRestResponse.h
// generated at 2016-04-19 12:41:55 
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
