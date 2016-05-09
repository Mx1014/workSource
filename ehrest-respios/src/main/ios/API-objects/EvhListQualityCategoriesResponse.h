//
// EvhListQualityCategoriesResponse.h
// generated at 2016-04-29 18:56:02 
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

