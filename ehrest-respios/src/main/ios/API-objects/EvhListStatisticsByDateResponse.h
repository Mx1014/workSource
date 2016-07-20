//
// EvhListStatisticsByDateResponse.h
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

