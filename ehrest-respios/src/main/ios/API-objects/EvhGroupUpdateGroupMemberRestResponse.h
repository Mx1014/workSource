//
// EvhGroupUpdateGroupMemberRestResponse.h
// generated at 2016-03-31 11:07:27 
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
