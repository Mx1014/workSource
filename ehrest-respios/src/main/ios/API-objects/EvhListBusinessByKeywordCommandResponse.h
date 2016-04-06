//
// EvhListBusinessByKeywordCommandResponse.h
// generated at 2016-04-06 19:10:43 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhBusinessDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListBusinessByKeywordCommandResponse
//
@interface EvhListBusinessByKeywordCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageOffset;

// item type EvhBusinessDTO*
@property(nonatomic, strong) NSMutableArray* list;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

