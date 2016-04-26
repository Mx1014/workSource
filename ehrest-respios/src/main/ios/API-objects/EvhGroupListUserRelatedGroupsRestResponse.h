//
// EvhGroupListUserRelatedGroupsRestResponse.h
// generated at 2016-04-26 18:22:56 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGroupListUserRelatedGroupsRestResponse
//
@interface EvhGroupListUserRelatedGroupsRestResponse : EvhRestResponseBase

// array of EvhGroupDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
