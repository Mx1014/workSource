//
// EvhListConfCategoryResponse.h
// generated at 2016-04-08 20:09:21 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhConfCategoryDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListConfCategoryResponse
//
@interface EvhListConfCategoryResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* enterpriseVaildAccounts;

// item type EvhConfCategoryDTO*
@property(nonatomic, strong) NSMutableArray* categories;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

