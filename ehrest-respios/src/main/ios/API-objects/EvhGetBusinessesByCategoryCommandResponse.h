//
// EvhGetBusinessesByCategoryCommandResponse.h
// generated at 2016-03-25 09:26:39 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhBusinessDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetBusinessesByCategoryCommandResponse
//
@interface EvhGetBusinessesByCategoryCommandResponse
    : NSObject<EvhJsonSerializable>


// item type EvhBusinessDTO*
@property(nonatomic, strong) NSMutableArray* requests;

@property(nonatomic, copy) NSNumber* nextPageOffset;

@property(nonatomic, copy) NSNumber* favoriteCount;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

