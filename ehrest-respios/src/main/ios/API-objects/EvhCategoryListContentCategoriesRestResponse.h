//
// EvhCategoryListContentCategoriesRestResponse.h
// generated at 2016-04-12 19:00:53 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCategoryListContentCategoriesRestResponse
//
@interface EvhCategoryListContentCategoriesRestResponse : EvhRestResponseBase

// array of EvhCategoryDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
