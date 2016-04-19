//
// EvhCategoryListBusinessSubCategoriesRestResponse.h
// generated at 2016-04-19 13:40:01 
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
