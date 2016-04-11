//
// EvhCategoryListBusinessSubCategoriesRestResponse.h
// generated at 2016-04-07 17:57:43 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCategoryListBusinessSubCategoriesRestResponse
//
@interface EvhCategoryListBusinessSubCategoriesRestResponse : EvhRestResponseBase

// array of EvhCategoryDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
