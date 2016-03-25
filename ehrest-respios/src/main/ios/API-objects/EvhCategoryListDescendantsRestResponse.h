//
// EvhCategoryListDescendantsRestResponse.h
// generated at 2016-03-25 17:08:12 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCategoryListDescendantsRestResponse
//
@interface EvhCategoryListDescendantsRestResponse : EvhRestResponseBase

// array of EvhCategoryDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
