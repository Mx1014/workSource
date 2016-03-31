//
// EvhConfGetConferenceNamespaceIdListRestResponse.h
// generated at 2016-03-31 13:49:15 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfGetConferenceNamespaceIdListRestResponse
//
@interface EvhConfGetConferenceNamespaceIdListRestResponse : EvhRestResponseBase

// array of EvhGetNamespaceListResponse* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
