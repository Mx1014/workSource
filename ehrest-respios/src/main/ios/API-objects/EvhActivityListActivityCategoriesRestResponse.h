//
// EvhActivityListActivityCategoriesRestResponse.h
// generated at 2016-03-30 10:13:09 
//
#import "RestResponseBase.h"
#import "EvhListActivityCategories.h"

///////////////////////////////////////////////////////////////////////////////
// EvhActivityListActivityCategoriesRestResponse
//
@interface EvhActivityListActivityCategoriesRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListActivityCategories* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
