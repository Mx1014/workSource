//
// EvhCategoryListBusinessSubCategoriesRestResponse.h
// generated at 2016-04-08 20:09:23 
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
