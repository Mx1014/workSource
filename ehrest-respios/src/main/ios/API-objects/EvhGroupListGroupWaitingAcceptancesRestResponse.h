//
// EvhGroupListGroupWaitingAcceptancesRestResponse.h
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGroupListGroupWaitingAcceptancesRestResponse
//
@interface EvhGroupListGroupWaitingAcceptancesRestResponse : EvhRestResponseBase

// array of EvhGroupMemberDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
