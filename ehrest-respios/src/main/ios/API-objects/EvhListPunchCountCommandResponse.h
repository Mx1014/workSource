//
// EvhListPunchCountCommandResponse.h
// generated at 2016-04-18 14:48:51 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhPunchCountDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPunchCountCommandResponse
//
@interface EvhListPunchCountCommandResponse
    : NSObject<EvhJsonSerializable>


// item type EvhPunchCountDTO*
@property(nonatomic, strong) NSMutableArray* punchCountList;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

