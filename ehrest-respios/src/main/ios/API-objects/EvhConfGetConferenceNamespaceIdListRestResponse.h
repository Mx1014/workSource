//
// EvhConfGetConferenceNamespaceIdListRestResponse.h
// generated at 2016-03-25 09:26:44 
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
