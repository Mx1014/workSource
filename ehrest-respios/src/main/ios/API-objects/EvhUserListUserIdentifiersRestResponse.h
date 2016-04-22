//
// EvhUserListUserIdentifiersRestResponse.h
// generated at 2016-04-22 13:56:52 
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
