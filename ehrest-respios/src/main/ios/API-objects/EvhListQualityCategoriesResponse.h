//
// EvhListQualityCategoriesResponse.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:55 
>>>>>>> 3.3.x
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

