//
// EvhUserListUserIdentifiersRestResponse.h
// generated at 2016-04-18 14:48:53 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserListUserIdentifiersRestResponse
//
@interface EvhUserListUserIdentifiersRestResponse : EvhRestResponseBase

// array of EvhUserIdentifierDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
