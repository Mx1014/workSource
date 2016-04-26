//
// EvhListQualityCategoriesResponse.h
// generated at 2016-04-26 18:22:55 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhQualityCategoriesDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListQualityCategoriesResponse
//
@interface EvhListQualityCategoriesResponse
    : NSObject<EvhJsonSerializable>


// item type EvhQualityCategoriesDTO*
@property(nonatomic, strong) NSMutableArray* categories;

@property(nonatomic, copy) NSNumber* nextPageAnchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

