//
// EvhQualityGetGroupMembersRestResponse.h
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQualityGetGroupMembersRestResponse
//
@interface EvhQualityGetGroupMembersRestResponse : EvhRestResponseBase

// array of EvhGroupUserDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
