//
// EvhAdminSampleRestResponse.h
// generated at 2016-04-06 19:59:47 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminSampleRestResponse
//
@interface EvhAdminSampleRestResponse : EvhRestResponseBase

// array of EvhSampleObject* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
