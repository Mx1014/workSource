//
// EvhCoursePostRestResponse.h
// generated at 2016-03-25 19:05:21 
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
