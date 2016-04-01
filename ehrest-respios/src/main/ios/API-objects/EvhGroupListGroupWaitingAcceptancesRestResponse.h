//
// EvhGroupListGroupWaitingAcceptancesRestResponse.h
// generated at 2016-03-31 20:15:33 
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
