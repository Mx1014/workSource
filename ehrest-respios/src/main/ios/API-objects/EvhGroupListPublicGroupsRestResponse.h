//
// EvhGroupListPublicGroupsRestResponse.h
// generated at 2016-03-25 17:08:13 
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
