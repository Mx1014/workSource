//
// EvhYellowPageListResponse.h
// generated at 2016-04-07 14:16:31 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhYellowPageDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhYellowPageListResponse
//
@interface EvhYellowPageListResponse
    : NSObject<EvhJsonSerializable>


// item type EvhYellowPageDTO*
@property(nonatomic, strong) NSMutableArray* yellowPages;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

