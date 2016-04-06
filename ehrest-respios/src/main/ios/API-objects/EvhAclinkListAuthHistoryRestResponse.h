//
// EvhAclinkListAuthHistoryRestResponse.h
// generated at 2016-04-06 19:10:43 
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
