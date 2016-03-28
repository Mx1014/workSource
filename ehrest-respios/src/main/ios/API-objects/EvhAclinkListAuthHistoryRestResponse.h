//
// EvhAclinkListAuthHistoryRestResponse.h
// generated at 2016-03-28 15:56:09 
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
