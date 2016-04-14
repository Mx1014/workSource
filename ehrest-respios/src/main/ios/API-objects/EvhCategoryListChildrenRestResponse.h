//
// EvhCategoryListChildrenRestResponse.h
// generated at 2016-04-12 19:00:53 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCategoryListChildrenRestResponse
//
@interface EvhCategoryListChildrenRestResponse : EvhRestResponseBase

// array of EvhCategoryDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
