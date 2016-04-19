//
// EvhCategoryListRootRestResponse.h
// generated at 2016-04-19 12:41:55 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCategoryListRootRestResponse
//
@interface EvhCategoryListRootRestResponse : EvhRestResponseBase

// array of EvhCategoryDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
