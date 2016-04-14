//
// EvhAclinkListAuthHistoryRestResponse.h
// generated at 2016-04-12 19:00:53 
//
#import "RestResponseBase.h"
#import "EvhListDoorAuthResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkListAuthHistoryRestResponse
//
@interface EvhAclinkListAuthHistoryRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListDoorAuthResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
