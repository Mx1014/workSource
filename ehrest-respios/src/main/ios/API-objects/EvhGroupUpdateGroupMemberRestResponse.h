//
// EvhGroupUpdateGroupMemberRestResponse.h
// generated at 2016-04-19 13:40:01 
//
#import "RestResponseBase.h"
#import "EvhGroupMemberDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGroupUpdateGroupMemberRestResponse
//
@interface EvhGroupUpdateGroupMemberRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhGroupMemberDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
