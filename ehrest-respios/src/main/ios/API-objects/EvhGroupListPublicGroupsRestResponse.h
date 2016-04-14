//
// EvhGroupListPublicGroupsRestResponse.h
// generated at 2016-04-12 19:00:53 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGroupListPublicGroupsRestResponse
//
@interface EvhGroupListPublicGroupsRestResponse : EvhRestResponseBase

// array of EvhGroupDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
