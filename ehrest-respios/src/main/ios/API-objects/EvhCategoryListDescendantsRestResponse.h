//
// EvhCategoryListDescendantsRestResponse.h
// generated at 2016-04-07 17:03:18 
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
