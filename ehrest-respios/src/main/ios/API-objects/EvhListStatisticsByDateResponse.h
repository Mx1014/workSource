//
// EvhListStatisticsByDateResponse.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhListStatisticsByDateDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListStatisticsByDateResponse
//
@interface EvhListStatisticsByDateResponse
    : NSObject<EvhJsonSerializable>


// item type EvhListStatisticsByDateDTO*
@property(nonatomic, strong) NSMutableArray* values;

@property(nonatomic, copy) NSNumber* nextPageAnchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

