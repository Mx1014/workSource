//
// EvhListFactorsResponse.h
// generated at 2016-04-12 15:02:19 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhFactorsDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListFactorsResponse
//
@interface EvhListFactorsResponse
    : NSObject<EvhJsonSerializable>


// item type EvhFactorsDTO*
@property(nonatomic, strong) NSMutableArray* factorsdto;

@property(nonatomic, copy) NSNumber* nextPageAnchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

