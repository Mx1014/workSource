//
// EvhCategoryListDescendantsRestResponse.h
// generated at 2016-04-07 10:47:33 
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
