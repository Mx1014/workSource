//
// EvhGroupListGroupWaitingAcceptancesRestResponse.h
// generated at 2016-04-29 18:56:03 
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
