//
// EvhGroupListUserRelatedGroupsRestResponse.h
// generated at 2016-04-01 15:40:24 
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
