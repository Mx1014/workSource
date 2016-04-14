//
// EvhListStatisticsByDateResponse.h
// generated at 2016-04-12 19:00:53 
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

