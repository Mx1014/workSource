//
// EvhCoursePostRestResponse.h
// generated at 2016-04-18 14:48:52 
//
#import "RestResponseBase.h"
#import "EvhCourseDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCoursePostRestResponse
//
@interface EvhCoursePostRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhCourseDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
